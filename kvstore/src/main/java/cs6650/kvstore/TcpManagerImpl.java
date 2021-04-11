package cs6650.kvstore;

import java.util.*;
import java.util.concurrent.*;

import cs6650.kvstore.ServerStatus.Status;
import cs6650.kvstore.KvStoreServiceGrpc.KvStoreServiceBlockingStub;
import cs6650.kvstore.KvStoreServiceOuterClass.KvMessage;
import cs6650.kvstore.KvStoreServiceOuterClass.LogEntries;
import cs6650.kvstore.TpcManagerServiceGrpc.TpcManagerServiceImplBase;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Server;
import io.grpc.stub.StreamObserver;



public class TcpManagerImpl extends TpcManagerServiceImplBase{
    private String START_TPC_REQUEST_TEMPLATE_STRING = "[%s]: Received %s from Client%d, starting 2pc";
    private ConfigReader config;
    private int numKvStoreServer;
    private KvStoreServiceGrpc.KvStoreServiceBlockingStub[] kvStoreServerStubs;
    ScheduledExecutorService daemonExecutor;

    List<KvMessage> logFile = new ArrayList<>();
    int[] nextIndex;
    ServerStatus.Status status;




    TcpManagerImpl(ConfigReader config) {
        this.config = config;
        this.numKvStoreServer = config.getNumServers();
        this.kvStoreServerStubs = new KvStoreServiceGrpc.KvStoreServiceBlockingStub[numKvStoreServer];
        this.daemonExecutor = Executors.newSingleThreadScheduledExecutor();
        this.nextIndex = new int[numKvStoreServer];
        this.status = ServerStatus.Status.Idle;

        for (int i = 0; i < numKvStoreServer; i++) {
            ManagedChannel mChannel = ManagedChannelBuilder.forAddress("127.0.0.1", config.getServerPort(i)).usePlaintext(true).build();
                  this.kvStoreServerStubs[i] = KvStoreServiceGrpc.newBlockingStub(mChannel);
        }

        // periodic job to sync KvStoreServer followers
        SyncFollowerJobs();


    }


    private synchronized void sendAppendEntries() {
        if (status != Status.Idle) return;

        status = Status.Busy;

        Utils.println("(SyncFollowerJob) Start Syncing followers....current logFile size " + logFile.size());
        ExecutorService executor = Executors.newFixedThreadPool(numKvStoreServer);
        List<Future<KvMessage>> results = new ArrayList<>();

        for (int i = 0; i < numKvStoreServer; i++) {
            List<KvMessage> logEntries = logFile.subList(nextIndex[i], logFile.size());
            LogEntries request = LogEntries.newBuilder().addAllEntries(logEntries).build();
            KvStoreServiceBlockingStub stub = kvStoreServerStubs[i];
            results.add(executor.submit(() -> stub.sync(request)));
        }

        int counter = 0;
        for (int i = 0; i < numKvStoreServer; i++) {
            try {
                nextIndex[i] = results.get(i).get().getCommitIndex();
                if (nextIndex[i] == logFile.size()) counter++;

            } catch (Exception e) {
                Utils.println(String.format("KvStoreServer%d cannot be reached....try later....", i));
            }
        }
        Utils.println(String.format("(SyncFollowerJob)%d out of %d followers in sync", counter, numKvStoreServer));
        executor.shutdown();
        status = Status.Idle;
    }


    private void SyncFollowerJobs() {
        daemonExecutor.scheduleAtFixedRate(this::sendAppendEntries, 2000, 2000, TimeUnit.MILLISECONDS);
    }

    @Override
    public void startTpc(KvMessage request, StreamObserver<KvMessage> responseObserver) {
        synchronized (this) {
            while (this.status != Status.Idle) {
                try {
                    wait();
                } catch (Exception e) {
                    //TODO: handle exception
                    System.out.println(e.toString());
                }
            }
            this.status = Status.Busy;
            Utils.println(
                String.format(START_TPC_REQUEST_TEMPLATE_STRING, Utils.getCurrentTimeString(), Utils.getMessageType(request.getMsgType()), request.getClientId())
                );
    
            if (prepareFollowers(request)) {
                commitFollowers(request);
            } else {
                abortFollowers(request);
            }
            
    
    
    
            KvMessage response = KvMessage.newBuilder().setMessage("Finish Tpc").build();
    
    
            //finish 2pc
            responseObserver.onNext(response);
            this.status = Status.Idle;
            notifyAll();
            responseObserver.onCompleted();
        }

    }

    private void abortFollowers(KvMessage request) {
        ExecutorService executor = Executors.newFixedThreadPool(numKvStoreServer);
        List<Future<KvMessage>> results = new ArrayList<>();
        KvMessage req = KvMessage.newBuilder().setCommitIndex(logFile.size()).build();

        for (KvStoreServiceBlockingStub follower : kvStoreServerStubs) {
            results.add(executor.submit(() -> follower.abort(req)));
        }

        int counter = 0;
        for (Future<KvMessage> f : results) {
            try {
                counter += f.get().getCommitIndex() == logFile.size() ? 1 : 0;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Utils.println(String.format("(abortFollowers) %d out of %d has aborted %s change", counter, numKvStoreServer, Utils.getMessageType(request.getMsgType())));

        executor.shutdownNow();
    }

    private void commitFollowers(KvMessage request) {
        ExecutorService executor = Executors.newFixedThreadPool(numKvStoreServer);
        List<Future<KvMessage>> results = new ArrayList<>();

        for (KvStoreServiceBlockingStub follower : kvStoreServerStubs) {
            results.add(executor.submit(() -> follower.commit(request)));
        }

        int counter = 0;
        for (Future<KvMessage> f : results) {
            try {
                counter += f.get().getCommitIndex() == logFile.size() ? 1 : 0;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Utils.println(String.format("(commitFollowers) %d out of %d has applied %s change", counter, numKvStoreServer, Utils.getMessageType(request.getMsgType())));

        executor.shutdownNow();
    }

    private boolean prepareFollowers(KvMessage request) {

        int commitIndex = logFile.size();
        KvMessage req = KvMessage.newBuilder().setCommitIndex(commitIndex)
                                    .setKey(request.getKey()).setValue(request.getValue())
                                    .setMsgType(request.getMsgType()).build();
        logFile.add(req);

        int counter = 0;
        ExecutorService executor = Executors.newFixedThreadPool(numKvStoreServer);
        List<Future<KvMessage>> results = new ArrayList<>();

        for (KvStoreServiceBlockingStub follower : kvStoreServerStubs) {
            results.add(executor.submit(() -> follower.prepare(req)));
        }

        for (Future<KvMessage> f : results) {
            try {
                counter += f.get().getVote() ? 1 : 0;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        executor.shutdownNow();
        
        if (counter < numKvStoreServer) {
            // Remove an log entry on TCP manager server
            logFile.remove(commitIndex);
            return false;
        }

        return true;
    }
}
