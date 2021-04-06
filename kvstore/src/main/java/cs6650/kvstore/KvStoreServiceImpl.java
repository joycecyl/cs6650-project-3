package cs6650.kvstore;

import java.util.HashMap;
import java.util.Map;



import cs6650.kvstore.KvStoreServiceOuterClass.KvMessage;
import cs6650.kvstore.KvStoreServiceOuterClass.KvMessage.KvMessageType;
import cs6650.kvstore.ServerStatus.Status;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

public class KvStoreServiceImpl extends KvStoreServiceGrpc.KvStoreServiceImplBase {


    private int numKvStoreServer;
    private boolean isCrashed;
    private KvStoreServiceGrpc.KvStoreServiceBlockingStub[] KvStoreServerStubs;

    private int servNum;
    private int localPort;
    private Status status;


    private Map<String, String> kvStore;


    private static String GET_RESPONSE_MESSAGE_TEMPLATE = "[%s]: Get %s as <%s, %s> from KvStoreServer%s(127.0.0.0:%s)";

    public KvStoreServiceImpl(ConfigReader config, int servNum) {
        this.numKvStoreServer = config.getNumServers();
        this.isCrashed = false;
        this.KvStoreServerStubs = new KvStoreServiceGrpc.KvStoreServiceBlockingStub[numKvStoreServer - 1];
        this.servNum = servNum;
        this.localPort = config.getServerPort(servNum);
        this.kvStore = new HashMap<>();
        this.status = ServerStatus.Status.Init;

        for (int i = 0, j = 0; i < numKvStoreServer; i++) {
            if (i != servNum) {
                ManagedChannel mChannel = ManagedChannelBuilder.forAddress("127.0.0.1", config.getServerPort(i)).build();
                this.KvStoreServerStubs[j++] = KvStoreServiceGrpc.newBlockingStub(mChannel);
            }
        }
    }

    @Override
    public void put(KvMessage request, StreamObserver<KvMessage> responseObserver) {
        // TODO Auto-generated method stub
        super.put(request, responseObserver);
    }


    @Override
    public void get(KvMessage request, StreamObserver<KvMessage> responseObserver) {
            
        synchronized (this.status) {
            while (this.status != Status.Init) {
                try {
                    wait();
                } catch (Exception e) {
                    //TODO: handle exception
                    System.out.println(e.toString());
                }
            }
            this.status = Status.InGet;

            String key = request.getKey();
            String value = kvStore.get(key);
            String message = String.format(GET_RESPONSE_MESSAGE_TEMPLATE, Utils.getCurrentTimeString(), key, key, value, String.valueOf(localPort));

            KvMessage response = KvMessage.newBuilder().setKey(key).setValue(value)
                                          .setMsgType(KvMessageType.getRes).setMessage(message).build();

            this.status = Status.Init;
            responseObserver.onNext(response);
            notifyAll();
        }
        responseObserver.onCompleted();
    }

    @Override
    public void delete(KvMessage request, StreamObserver<KvMessage> responseObserver) {
        // TODO Auto-generated method stub
        super.delete(request, responseObserver);
    }



    @Override
    public void prepare(KvMessage request, StreamObserver<KvMessage> responseObserver) {
        // TODO Auto-generated method stub
        super.prepare(request, responseObserver);
    }


    @Override
    public void commit(KvMessage request, StreamObserver<KvMessage> responseObserver) {
        // TODO Auto-generated method stub
        super.commit(request, responseObserver);
    }


    @Override
    public void abort(KvMessage request, StreamObserver<KvMessage> responseObserver) {
        // TODO Auto-generated method stub
        super.abort(request, responseObserver);
    }



}
