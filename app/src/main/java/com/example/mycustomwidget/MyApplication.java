package com.example.mycustomwidget;

import android.app.Application;

import com.example.mycustomwidget.utils.UIUtils;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        UIUtils.init(this);
    }
}
