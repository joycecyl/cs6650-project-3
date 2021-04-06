package cs6650.kvstore;

import java.util.Scanner;

import cs6650.kvstore.KvStoreServiceGrpc.KvStoreServiceBlockingStub;
import cs6650.kvstore.KvStoreServiceOuterClass.KvMessage;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

public class Client {

    private static Namespace parseArgs(String[] args) {
        ArgumentParser parser = ArgumentParsers.newFor("KvStoreServer").build()
                .description("KvStoreServer server for KvStore");

        parser.addArgument("-p", "--port").type(Integer.class).setDefault(1)
                .help("Set which port this client is");
        parser.addArgument("-a", "--address").type(Integer.class).setDefault(10)
                .help("address");

        Namespace res = null;
        try {
            res = parser.parseArgs(args);
        } catch (ArgumentParserException e) {
            parser.handleError(e);
        }
        return res;
    }

    public static void main( String[] args ) throws Exception
    {
        Namespace c_args = parseArgs(args);
        String ip = c_args.getString("address");
        int port = c_args.getInt("port");
        if (port < 0) {
            System.out.println(String.format("Input invalid port %s: port should be in range of 0 -- 60000", String.valueOf(port)));
            return;
        }
        String target = String.format("%s:%d", ip, port);
      // Channel is the abstraction to connect to a service endpoint
      // Let's use plaintext communication because we don't have certs
      final ManagedChannel channel = ManagedChannelBuilder.forTarget(target).build();
      KvStoreServiceBlockingStub stub = KvStoreServiceGrpc.newBlockingStub(channel);

      String op = "";
      String key = null;
      String val = null;
      String input = "";

    //   String [] inputDefaultArray = {"put one 1", "put two 2", "put three 3", "put four 4", "put five 5",
    //                                  "get one", "get two", "get three", "get four", "get five",
    //                                  "delete one", "delete two", "delete three", "delete four", "delete five"};

    //   for (int i = 0; i < 15; i++) {
    //         input = inputDefaultArray[i];
    //         String[] strs = input.split(" ");
    //         String message = "";
            
    //         op = strs[0];
    //         key = strs[1];
    //         val = null;
    //         if (strs.length > 2) {
    //             val = strs[2];
    //         }

    //         // update data based on operation
    //         if (op.equalsIgnoreCase("put")) {
    //             KvMessage request =
    //             KvMessage.newBuilder()
    //             .setKey(key).setValue(val)
    //             .build();

    //             KvMessage response = stub.put(request);
    //             message = response.getMessage();

    //         } else if (op.equalsIgnoreCase("get")) { 
    //             KvMessage request =
    //             KvMessage.newBuilder()
    //             .setKey(key)
    //             .build();

    //             KvMessage response = stub.get(request);
    //             message = response.getMessage();
    //         } else if (op.equalsIgnoreCase("delete")) {
    //             KvMessage request =
    //             KvMessage.newBuilder()
    //             .setKey(key)
    //             .build();

    //             KvMessage response = stub.delete(request);
    //             message = response.getMessage();
    //         }
    //         System.out.println(message);
    //   }

      while (!op.equalsIgnoreCase("quit")) {
            System.out.println("Usage: put <key> <value>\n" + "       get <key>\n" + "       delete <key>");
            System.out.print("Enter input: ");
            input = new Scanner(System.in).nextLine();
            String[] strs = input.split(" ");
            String message = "";
            
            op = strs[0];

            if(op.equalsIgnoreCase("put") & strs.length != 3){
                message = "bad put command type 1, usage: put <key> <value>";
                System.out.println(message);
                continue;
            } else if (op.equalsIgnoreCase("get") & strs.length != 2) {
                message = "bad put command type 2, usage: get <key>";
                System.out.println(message);
                continue;
            } else if (op.equalsIgnoreCase("delete") & strs.length != 2) {
                message = "bad put command type 3, usage: delete <key>";
                System.out.println(message);
                continue;
            } else if (op.equalsIgnoreCase("put") || op.equalsIgnoreCase("get") || op.equalsIgnoreCase("delete")) {
                key = strs[1];
                val = null;
                if (strs.length > 2) {
                    val = strs[2];
                }
            } else {
                message = "bad put command type 4, use any of the following: put <key> <value>, get <key>, delete <key>";
                System.out.println(message);
                continue;
            }

            
            // update data based on operation
            if (op.equalsIgnoreCase("put")) {
                KvMessage request =
                KvMessage.newBuilder()
                .setKey(key).setValue(val)
                .build();

                KvMessage response = stub.put(request);
                message = response.getMessage();

            } else if (op.equalsIgnoreCase("get")) { 
                KvMessage request =
                KvMessage.newBuilder()
                .setKey(key)
                .build();

                KvMessage response = stub.get(request);
                message = response.getMessage();
            } else if (op.equalsIgnoreCase("delete")) {
                KvMessage request =
                KvMessage.newBuilder()
                .setKey(key)
                .build();

                KvMessage response = stub.delete(request);
                message = response.getMessage();
            } else {
                message = "Invalid command.";
            }
            System.out.println(message);
      }

      channel.shutdownNow();
    }
}
