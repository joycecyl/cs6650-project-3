package cs6650.kvstore;

import java.io.IOError;
import java.io.IOException;
import java.util.concurrent.*;
import java.util.logging.Logger;

import io.grpc.Server;
import io.grpc.ServerBuilder;

public class KvStoreServer {

    protected Server server;
    protected ConfigReader config;
    private static final Logger logger = Logger.getLogger(KvStoreServer.class.getName());




    public KvStoreServer(ConfigReader config) {
        this.config = config;
    }


    private void start(int port, int servNum, int numThreads) throws IOException {
        // server = ServerBuilder.forPort(port)
        // .addService(new KvStoreServiceImpl(config, servNum))
        // .executor(Executors.newFixedThreadPool(numThreads))
        // .build()
        // .start();
        // logger.info("Server started, listening on " + port);
        // Runtime.getRuntime().addShutdownHook(new Thread() {
        //     @Override
        //     public void run() {
        //         System.err.println("*** shutting down gRPC server since JVM is shutting down");
        //         MetadataStore.this.stop();
        //         System.err.println("*** server shut down");
        //     }
        // });
    } 

    public static void main(String[] args) {
        
    }
}
