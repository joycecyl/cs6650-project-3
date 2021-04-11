package cs6650.kvstore;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import java.util.logging.Logger;

import cs6650.kvstore.KvStoreServiceOuterClass.KvMessage;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

import io.grpc.Server;
import io.grpc.ServerBuilder;

public class TcpManager {
    protected Server server;
    protected int port;
    protected int numThreads;
    protected ConfigReader config;
    private static final Logger logger = Logger.getLogger(TcpManager.class.getName());

    

    TcpManager(int port, int numThreads, ConfigReader config) {
        this.port = port;
        this.numThreads = numThreads;
        this.config = config;
    }

    private void start() throws IOException {
        server = ServerBuilder.forPort(port)
        .addService(new TcpManagerImpl(config))
        .executor(Executors.newFixedThreadPool(numThreads))
        .build()
        .start();
        logger.info("TcpManager Server started, listening on " + this.port);
        
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                System.err.println("*** shutting down XManager server since JVM is shutting down");
                TcpManager.this.stop();
                System.err.println("*** server shut down");
            }
        });
    }
    
    private void stop() {
        if (server != null) {
            server.shutdown();
        }
    }   

    private static Namespace parseArgs(String[] args) {
        ArgumentParser parser = ArgumentParsers.newFor("TcpManager").build()
                .description("TcpManager server for Tcp Service");
        parser.addArgument("config_file").type(String.class)
                .setDefault("/Users/joyceliu/Desktop/6650_all/cs6650-project-3/kvstore/configs/configDistributed.txt").help("Path to configuration file");
        parser.addArgument("-t", "--threads").type(Integer.class).setDefault(10)
                .help("Maximum number of concurrent threads");
        parser.addArgument("-p", "--port").type(Integer.class).setDefault(1000)
                .help("port number");
        Namespace res = null;
        try {
            res = parser.parseArgs(args);
        } catch (ArgumentParserException e) {
            parser.handleError(e);
        }

        return res;
    }

    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    public static void main(String[] args) throws Exception {
        Namespace c_args = parseArgs(args);
        if (c_args == null) {
            throw new RuntimeException("Argument parsing failed");
        }
        File configf = new File(c_args.getString("config_file"));
        ConfigReader config = new ConfigReader(configf);


        final TcpManager server = new TcpManager(c_args.getInt("port"), c_args.getInt("threads"), config);
        server.start();
        server.blockUntilShutdown();
    }
}
