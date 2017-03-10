package utils;

import android.util.Log;

/**
 * Created by Administrator on 2016/11/23.
 */
public class Logger {
    private static final String PREFIX = "[Dylan_Logger]-->";

    public static void d(String tag, String msg) {
            Log.d(PREFIX + tag, msg);
    }

    public static void e(String tag, String msg) {
            Log.e(PREFIX + tag, msg);
    }

    public static void i(String tag, String msg) {
            Log.i(PREFIX + tag, msg);
    }

    public static void v(String tag, String msg) {
            Log.v(PREFIX + tag, msg);
    }

    public static void w(String tag, String msg) {
            Log.w(PREFIX + tag, msg);
    }

    public static void wtf(String tag, String msg) {
            Log.wtf(PREFIX + tag, msg);
    }
}
