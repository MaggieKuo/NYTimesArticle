package com.mag.nytimesarticle.util;

import android.app.Application;
import android.content.Context;

import com.androidnetworking.AndroidNetworking;

/**
 * Created by perfumekuo on 2017/2/24.
 */

public class NYApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

        AndroidNetworking.initialize(getApplicationContext());
    }

    public static Context getContext(){
        return context;
    }


}
