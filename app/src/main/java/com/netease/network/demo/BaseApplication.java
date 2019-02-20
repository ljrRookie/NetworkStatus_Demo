package com.netease.network.demo;

import android.app.Application;

import com.netease.network.library.NetworkManager;

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        NetworkManager.getDefault().init(this);
    }
}
