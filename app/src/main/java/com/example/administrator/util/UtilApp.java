package com.example.administrator.util;

import android.app.Application;
import android.content.Context;

/**
 * Created by Administrator on 2016/12/28.
 */
public class UtilApp extends Application {
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getUtilAppContext() {
        return context;
    }
}
