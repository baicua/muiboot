package com.muiboot.core.log;

import java.util.UUID;

/**
 * <p>Description: </p>
 *
 * @version 1.0 2019/2/25
 */
public class LogTrackContext {
    static final ThreadLocal<String> trackNumberContext = new InheritableThreadLocal();
    public LogTrackContext() {
    }
    public static void setTrackNumber(String trackNumber) {
        trackNumberContext.set(trackNumber);
    }
    public static String getTrackNumber() {
        return (String)trackNumberContext.get();
    }
    public static void remove() {
        try {
            trackNumberContext.remove();
        } catch (Exception var2) {
            ;
        }

    }
    public static void initTrackNumber() {
        trackNumberContext.set(getRandom18String());
    }
    private static String random() {
        return UUID.randomUUID().toString();
    }

    public static String getRandom18String() {
        Long millis = Long.valueOf(System.currentTimeMillis());
        Integer n = Integer.valueOf((int)(Math.random() * 100000.0D));
        int zeroNumber = 5 - n.toString().length();
        String pre = "";

        for(int r = 0; r < zeroNumber; ++r) {
            pre = pre + "0";
        }

        String var5 = millis.toString();
        if(!pre.equals("")) {
            var5 = var5 + pre + n.toString();
        } else {
            var5 = var5 + n.toString();
        }

        return Long.toHexString(Long.parseLong(var5));
    }
}
