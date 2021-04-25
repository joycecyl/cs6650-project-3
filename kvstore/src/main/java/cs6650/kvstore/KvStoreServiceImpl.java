package cs6650.kvstore;

import static cs6650.kvstore.Utils.initializeKvStore;

import java.util.HashMap;
import java.util.*;
import java.util.Map;
import java.util.concurrent.*;

import cs6650.kvstore.KvStoreServiceGrpc.KvStoreServiceBlockingStub;
import cs6650.kvstore.KvStoreServiceOuterClass.KvMessage;
import cs6650.kvstore.KvStoreServiceOuterClass.LogEntries;
import cs6650.kvstore.KvStoreServiceOuterClass.KvMessage.Builder;
import cs6650.kvstore.KvStoreServiceOuterClass.KvMessage.KvMessageType;
import cs6650.kvstore.ServerStatus.Status;
import io.grpc.Grpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.ServerCall;
import io.grpc.stub.StreamObserver;

public class KvStoreServiceImpl extends KvStoreServiceGrpc.KvStoreServiceImplBase {


    private int numKvStoreServer;
    private boolean isCrashed;
    private KvStoreServiceGrpc.KvStoreServiceBlockingStub[] KvStoreServerStubs;
    private int proposerId;
    private  ScheduledExecutorService daemonExecutor;
    private int servNum;
    private int localPort;
    private Status status;
    private int[] nextIndex;


    private Map<String, String> kvStore;
    List<KvMessage> logFile = new ArrayList<>();
    private int appliedIndex;



    private static String REQUEST_MESSAGE_TEMPLATE  = "[%s]: Request (%s %s) from Client%d";
    private String START_TPC_REQUEST_TEMPLATE_STRING = "[%s]: Received %s from Client%d, starting 2pc";
    private static String GET_RESPONSE_MESSAGE_TEMPLATE = "[%s]: Get %s as <%s, %s> from KvStoreServer%s(127.0.0.0:%s)";
    private static String INFO_TMPLATE = "[%s]: %s";
   
    public KvStoreServiceImpl(ConfigReader config, int servNum) {
        this.numKvStoreServer = config.getNumServers();
        this.isCrashed = false;
        this.KvStoreServerStubs = new KvStoreServiceGrpc.KvStoreServiceBlockingStub[numKvStoreServer];
        this.servNum = servNum;
        this.localPort = config.getServerPort(servNum);
        this.kvStore = new HashMap<>();
        this.appliedIndex = 0;
        this.status = ServerStatus.Status.Idle;
        this.proposerId = numKvStoreServer - 1;
        this.daemonExecutor = Executors.newSingleThreadScheduledExecutor();
        this.nextIndex = new int[numKvStoreServer];
        


        for (int i = 0; i  < numKvStoreServer; i++) {
            if (i != servNum) {
                ManagedChannel mChannel = ManagedChannelBuilder.forAddress("127.0.0.1", config.getServerPort(i)).usePlaintext(true).build();
                this.KvStoreServerStubs[i] = KvStoreServiceGrpc.newBlockingStub(mChannel);
            }
        }
        
        if (proposerId == servNum) {
            SyncFollowerJobs();
        }

        initializeKvStore(kvStore);
    }

    private void SyncFollowerJobs() {
        daemonExecutor.scheduleAtFixedRate(this::sendAppendEntries, 2000, 2000, TimeUnit.MILLISECONDS);
    }



    private synchronized void sendAppendEntries() {
        if (status != Status.Idle) return;
        if (servNum != proposerId) return;

        status = Status.Busy;

        Utils.println("(SyncFollowerJob) Start Syncing followers....current logFile size " + logFile.size());
        ExecutorService executor = Executors.newFixedThreadPool(numKvStoreServer);
        List<Future<KvMessage>> results = new ArrayList<>();

        for (int i = 0; i < numKvStoreServer; i++) {
            if (i == proposerId) continue;
            List<KvMessage> logEntries = logFile.subList(nextIndex[i], logFile.size());
            LogEntries request = LogEntries.newBuilder().addAllEntries(logEntries).build();
            KvStoreServiceBlockingStub stub = KvStoreServerStubs[i];
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



    private boolean prepareFollowers(KvMessage request) {

        int commitIndex = logFile.size();
        KvMessage req = KvMessage.newBuilder().setCommitIndex(commitIndex)
                                    .setKey(request.getKey()).setValue(request.getValue())
                                    .setMsgType(request.getMsgType()).build();
        logFile.add(req);

        int counter = 0;
        ExecutorService executor = Executors.newFixedThreadPool(numKvStoreServer);
        List<Future<KvMessage>> results = new ArrayList<>();

        for (int i = 0; i < KvStoreServerStubs.length; i++) {
            if (i != servNum) {
                KvStoreServiceBlockingStub follower = KvStoreServerStubs[i];
                results.add(executor.submit(() -> follower.prepare(req)));
            }
        }

        for (Future<KvMessage> f : results) {
            try {
                counter += f.get().getVote() ? 1 : 0;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        executor.shutdownNow();
        
        if (counter < numKvStoreServer / 2) {
            // Remove an log entry on TCP manager server
            logFile.remove(commitIndex);
            return false;
        }

        return true;
    }

    private KvMessage startPaxos(KvMessage request) {
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

            int commitIndex = logFile.size();
            KvMessage req = KvMessage.newBuilder().setCommitIndex(commitIndex)
                .setKey(request.getKey()).setValue(request.getValue())
                .setMsgType(request.getMsgType()).build();

            if (prepareFollowers(req)) {
                //if more than half of accpter agrees, start append log
                sendAcceptToFollowers(req);
            } else {
                // clean up follower
                cleanUp(req);
            }
            
    
    
    
            KvMessage response = KvMessage.newBuilder().setMessage("Finish Tpc").build();
    

            this.status = Status.Idle;
            notifyAll();
            return response;
        }
    }
    
    private void cleanUp(KvMessage request) {
        ExecutorService executor = Executors.newFixedThreadPool(numKvStoreServer);
        List<Future<KvMessage>> results = new ArrayList<>();
        KvMessage req = KvMessage.newBuilder().setCommitIndex(logFile.size()).build();

        for (KvStoreServiceBlockingStub follower : KvStoreServerStubs) {
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

        Utils.println(String.format("(cleanUp Follower) %d out of %d has aborted %s change", counter, numKvStoreServer, Utils.getMessageType(request.getMsgType())));

        executor.shutdownNow();
    }
    
    private void sendAcceptToFollowers(KvMessage request) {
        ExecutorService executor = Executors.newFixedThreadPool(numKvStoreServer);
        List<Future<KvMessage>> results = new ArrayList<>();
        
        for (int i = 0; i < KvStoreServerStubs.length; i++) {
            if (i != servNum) {
                KvStoreServiceBlockingStub follower = KvStoreServerStubs[i];
                results.add(executor.submit(() -> follower.accept(request)));
            }
        }

        int counter = 0;
        for (Future<KvMessage> f : results) {
            try {
                counter += f.get().getCommitIndex() == logFile.size() ? 1 : 0;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (counter > numKvStoreServer / 2) {
            update();
        }

        Utils.println(String.format("(commitFollowers) %d out of %d has applied %s change", counter, numKvStoreServer, Utils.getMessageType(request.getMsgType())));

        executor.shutdownNow();
    }


    @Override
    public void startPaxos(KvMessage request, StreamObserver<KvMessage> responseObserver) {
        // TODO Auto-generated method stub
        KvMessage response = startPaxos(request);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void put(KvMessage request, StreamObserver<KvMessage> responseObserver) {
        // TODO Auto-generated method stub
        String key = request.getKey();
        String value = request.getValue();
        String keyValuePair = String.format("<%s, %s>", key, value);
        int clientId = request.getClientId();

        System.out.println(String.format(REQUEST_MESSAGE_TEMPLATE, Utils.getCurrentTimeString(), "Put", keyValuePair, clientId));
        Utils.println("Starting Paxos to append log, wait for result");

        KvMessage res = null;
        if (servNum != proposerId) {
            res = KvStoreServerStubs[proposerId].startPaxos(request);
        } else {
            res = startPaxos(request);
        }
        


        System.out.println(res.getMessage());

        responseObserver.onNext(res);
        responseObserver.onCompleted();
    }

    @Override
    public void get(KvMessage request, StreamObserver<KvMessage> responseObserver) {
            
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
            String message = "";
            String key = request.getKey();
            String value = "";
            if (kvStore.containsKey(key)) {
                value = kvStore.get(key);
                message = String.format(GET_RESPONSE_MESSAGE_TEMPLATE, Utils.getCurrentTimeString(), key, key, value, String.valueOf(servNum), String.valueOf(localPort));
            } else {
                message = String.format(INFO_TMPLATE, Utils.getCurrentTimeString(), String.format("Get %s, key doesnt exist", key));
            }
            

            KvMessage response = KvMessage.newBuilder().setKey(key).setValue(value)
                                          .setMsgType(KvMessageType.getRes).setMessage(message).build();
            
            this.status = Status.Idle;

           
            System.out.println(String.format(REQUEST_MESSAGE_TEMPLATE, Utils.getCurrentTimeString(), "Get", key, request.getClientId()));

            
            responseObserver.onNext(response);
            notifyAll();
        }
        responseObserver.onCompleted();
    }

    @Override
    public void delete(KvMessage request, StreamObserver<KvMessage> responseObserver) {
        // TODO Auto-generated method stub
        // TODO Auto-generated method stub
        String key = request.getKey();
        int clientId = request.getClientId();

        System.out.println(String.format(REQUEST_MESSAGE_TEMPLATE, Utils.getCurrentTimeString(), "Delete", key, clientId));
        Utils.println("Starting 2pc, waiting for 2pc result");
        
        KvMessage res = null;
        if (servNum != proposerId) {
            res = KvStoreServerStubs[proposerId].startPaxos(request);
        } else {
            res = startPaxos(request);
        }


        System.out.println(res.getMessage());

        responseObserver.onNext(res);
        responseObserver.onCompleted();
    }



    @Override
    public void prepare(KvMessage request, StreamObserver<KvMessage> responseObserver) {
        


        Builder responseMessageBuilder = KvMessage.newBuilder();
        if (isCrashed) {
            responseMessageBuilder.setVote(false);
        }

        synchronized (this) {
            while (this.status != Status.Idle) {
                try {
                    wait();
                } catch (Exception e) {
                    //TODO: handle exception
                    System.out.println(e.toString());
                }
            }

            // change status, block other request
            this.status = Status.Busy;

            if (logFile.size() > request.getCommitIndex()) {
                responseMessageBuilder.setVote(false);
            } else {
                logFile.add(request);
                responseMessageBuilder.setVote(true);
            }
            notifyAll();
        }

        Utils.println(String.format("2pc vote result is %s", String.valueOf(responseMessageBuilder.getVote())));
        responseObserver.onNext(responseMessageBuilder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void commit(KvMessage request, StreamObserver<KvMessage> responseObserver) {
        // TODO Auto-generated method stub
        update();
        KvMessage response = KvMessage.newBuilder().setCommitIndex(appliedIndex).setVote(appliedIndex == logFile.size()).build();
        responseObserver.onNext(response);
        this.status = Status.Idle;
        responseObserver.onCompleted();
    }

    @Override
    public void accept(KvMessage request, StreamObserver<KvMessage> responseObserver) {
        // TODO Auto-generated method stub
        if (request.getCommitIndex() + 1 == logFile.size()) {
            logFile.set(logFile.size() - 1, request);
            update();
            KvMessage response = KvMessage.newBuilder().setCommitIndex(appliedIndex).setVote(true).build();
            responseObserver.onNext(response);
        } else {
            KvMessage response = KvMessage.newBuilder().setCommitIndex(appliedIndex).setVote(false).build();
            responseObserver.onNext(response);
        }
    
        this.status = Status.Idle;
        responseObserver.onCompleted();
    }

    @Override
    public void abort(KvMessage request, StreamObserver<KvMessage> responseObserver) {
        // TODO Auto-generated method stub
        // remove invalid tails of logfile.
        for(int i = logFile.size() - 1; i >= request.getCommitIndex(); i--) {
            logFile.remove(i);
        }

        KvMessage response = KvMessage.newBuilder().setCommitIndex(logFile.size()).build();
        
        responseObserver.onNext(response);
        this.status = Status.Idle;
        responseObserver.onCompleted();
    }

    // Sync log
    @Override
    public void sync(LogEntries request, StreamObserver<KvMessage> responseObserver) {
        
        // TODO Auto-generated method stub
        List<KvMessage> logEntries = request.getEntriesList();
        synchronized(this) {
            if(logEntries.isEmpty() || logEntries.get(0).getCommitIndex() > logFile.size()) {
                responseObserver.onNext(KvMessage.newBuilder().setCommitIndex(logFile.size()).build());
                responseObserver.onCompleted();
                Utils.println(String.format("Finished syncing with TcpManager, appliedIndex: %d, logFileIndex %d", appliedIndex, logFile.size()));
                return;
            }
            
            // roll back uncommitted log tails
            int start = logEntries.get(0).getCommitIndex();
            for (int i = logFile.size() - 1; i >= start; i--) {
                logFile.remove(i);
            }

            logFile.addAll(logEntries);

            update();

            //ToDo: handle partial applied.
            Utils.println(String.format("Finished syncing with TcpManager, appliedIndex: %d, logFileIndex %d", appliedIndex, logFile.size()));
            responseObserver.onNext(KvMessage.newBuilder().setCommitIndex(logFile.size()).build());
            responseObserver.onCompleted();
        }
    }


    private void update() {
        synchronized (this) {
            while(appliedIndex < logFile.size()) {
                applyLogEntry(logFile.get(appliedIndex));
                appliedIndex++;
            }
        }  
    }


    private void applyLogEntry(KvMessage kvMessage) {
        String key = kvMessage.getKey();
        String value = kvMessage.getValue();
        if(kvMessage.getMsgType().equals(KvMessage.KvMessageType.putReq)) {
            kvStore.put(key, value);
        } else if (kvMessage.getMsgType().equals(KvMessage.KvMessageType.deleteReq)) {
            kvStore.remove(key);
        }
    }
}
