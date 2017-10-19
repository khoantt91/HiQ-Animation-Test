package com.khoantt91.hiqanimationtest.helper;

import android.util.Log;

/**
 * Created by ThienKhoa on 3/9/17.
 */

public class Logger {

    /***
     * In debug: flag = false
     * In release: flag = true
     */
    private static final boolean flag = false;

    public static void d(String tag, String msg) {
        if (!flag) Log.d(tag, msg);
    }

    public static void e(String tag, String msg) {
        if (!flag) Log.e(tag, msg);
    }

    public static void i(String tag, String msg) {
        if (!flag) Log.i(tag, msg);
    }

    public static void v(String tag, String msg) {
        if (!flag) Log.v(tag, msg);
    }

    public static void w(String tag, String msg) {
        if (!flag) Log.w(tag, msg);
    }

}
