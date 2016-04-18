package com.christophermathew.memleaktests;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

public class DelivApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
    }
}
