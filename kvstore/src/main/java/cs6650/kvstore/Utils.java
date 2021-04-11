package cs6650.kvstore;

import java.text.SimpleDateFormat;
import java.util.*;

import cs6650.kvstore.KvStoreServiceOuterClass.KvMessage;
import cs6650.kvstore.KvStoreServiceOuterClass.KvMessage.KvMessageType;

public class Utils {
    public static String getCurrentTimeString() {
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS z");
        Date date = new Date(System.currentTimeMillis());
        String curTime = formatter.format(date);
        return curTime;
    }

    public static void initializeKvStore(Map<String, String> map) {
        map.put("k1", "v1");
        map.put("k2", "v2");
    }

    public static void println(String str) {
        System.out.println("[" + getCurrentTimeString() + "]" + " " + str);
    }

    public static String getMessageType(KvMessageType msgType) {
        switch(msgType.getNumber()) {
            case KvMessageType.putReq_VALUE:
                return "Put Request";
            case KvMessageType.putRes_VALUE:
                return "Put Response";
            case KvMessageType.getReq_VALUE:
                return "Get Request";
            case KvMessageType.getRes_VALUE:
                return "Get Response";
            case KvMessageType.deleteReq_VALUE:
                return "Delete Request";
            case KvMessageType.deleteRes_VALUE:
                return "Delete Response";
            default:
                return null;
        }
    }
}
