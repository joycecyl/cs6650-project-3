package cs6650.kvstore;

import java.text.SimpleDateFormat;
import java.util.*;

public class Utils {
    public static String getCurrentTimeString() {
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS z");
        Date date = new Date(System.currentTimeMillis());
        String curTime = formatter.format(date);
        return curTime;
    }
}
