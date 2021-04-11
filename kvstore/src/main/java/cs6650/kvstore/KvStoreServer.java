package cs6650.kvstore;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.*;
import java.util.logging.Logger;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

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
        server = ServerBuilder.forPort(port)
        .addService(new KvStoreServiceImpl(config, servNum))
        .executor(Executors.newFixedThreadPool(numThreads))
        .build()
        .start();
        logger.info("Server started, listening on " + port);
        
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                System.err.println("*** shutting down gRPC server since JVM is shutting down");
                KvStoreServer.this.stop();
                System.err.println("*** server shut down");
            }
        });
    } 
 
    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    private void stop() {
        if (server != null) {
            server.shutdown();
        }
    }

    private static Namespace parseArgs(String[] args) {
        ArgumentParser parser = ArgumentParsers.newFor("KvStoreServer").build()
                .description("KvStoreServer server for KvStore");
        parser.addArgument("config_file").type(String.class)
                .help("Path to configuration file");
        parser.addArgument("-n", "--number").type(Integer.class).setDefault(1)
                .help("Set which number this server is");
        parser.addArgument("-t", "--threads").type(Integer.class).setDefault(10)
                .help("Maximum number of concurrent threads");

        Namespace res = null;
        try {
            res = parser.parseArgs(args);
        } catch (ArgumentParserException e) {
            parser.handleError(e);
        }

        return res;
    }
    public static void main(String[] args) throws Exception {
        Namespace c_args = parseArgs(args);
        if (c_args == null) {
            throw new RuntimeException("Argument parsing failed");
        }

        File configf = new File(c_args.getString("config_file"));
        ConfigReader config = new ConfigReader(configf);
        int servNum = c_args.getInt("number");


        if (servNum > config.getNumServers()) {
            throw new RuntimeException(String.format("metadata%d not in config file", c_args.getInt("number")));
        }

        final KvStoreServer server = new KvStoreServer(config);
        server.start(config.getServerPort(servNum), servNum, c_args.getInt("threads"));
        server.blockUntilShutdown();
    }
}
