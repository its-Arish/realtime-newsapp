package com.finalapp.bestappnews;

import android.app.Application;
import android.content.Context;


public class MyApplication extends Application {

    private static Context context;

    public static Context takeappContext() {
        return MyApplication.context;
    }

    public void onCreate() {
        super.onCreate();
        MyApplication.context = getApplicationContext();
    }
}
