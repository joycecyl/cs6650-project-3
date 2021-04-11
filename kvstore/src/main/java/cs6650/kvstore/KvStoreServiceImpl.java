package cs6650.kvstore;

import static cs6650.kvstore.Utils.initializeKvStore;

import java.util.HashMap;
import java.util.*;
import java.util.Map;



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
    private TpcManagerServiceGrpc.TpcManagerServiceBlockingStub tcpManagerStub;

    private int servNum;
    private int localPort;
    private Status status;


    private Map<String, String> kvStore;
    List<KvMessage> logFile = new ArrayList<>();
    private int appliedIndex;



    private static String REQUEST_MESSAGE_TEMPLATE  = "[%s]: Request (%s %s) from Client%d";
    private static String GET_RESPONSE_MESSAGE_TEMPLATE = "[%s]: Get %s as <%s, %s> from KvStoreServer%s(127.0.0.0:%s)";
    private static String INFO_TMPLATE = "[%s]: %s";

    public KvStoreServiceImpl(ConfigReader config, int servNum) {
        this.numKvStoreServer = config.getNumServers();
        this.isCrashed = false;
        this.KvStoreServerStubs = new KvStoreServiceGrpc.KvStoreServiceBlockingStub[numKvStoreServer - 1];
        this.servNum = servNum;
        this.localPort = config.getServerPort(servNum);
        this.kvStore = new HashMap<>();
        this.appliedIndex = 0;
        this.status = ServerStatus.Status.Idle;

        for (int i = 0, j = 0; i < numKvStoreServer; i++) {
            if (i != servNum) {
                ManagedChannel mChannel = ManagedChannelBuilder.forAddress("127.0.0.1", config.getServerPort(i)).usePlaintext(true).build();
                this.KvStoreServerStubs[j++] = KvStoreServiceGrpc.newBlockingStub(mChannel);
            }
        }

        tcpManagerStub = TpcManagerServiceGrpc.newBlockingStub(ManagedChannelBuilder.forAddress("127.0.0.1", 1000).usePlaintext(true).build());

        initializeKvStore(kvStore);
    }


    @Override
    public void put(KvMessage request, StreamObserver<KvMessage> responseObserver) {
        // TODO Auto-generated method stub
        String key = request.getKey();
        String value = request.getValue();
        String keyValuePair = String.format("<%s, %s>", key, value);
        int clientId = request.getClientId();

        System.out.println(String.format(REQUEST_MESSAGE_TEMPLATE, Utils.getCurrentTimeString(), "Put", keyValuePair, clientId));
        Utils.println("Starting 2pc, waiting for 2pc result");
        KvMessage res = tcpManagerStub.startTpc(request);


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
        KvMessage res = tcpManagerStub.startTpc(request);

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

            if (logFile.size() != request.getCommitIndex()) {
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
