package com.example.testinput;

import android.app.Application;

import com.example.emotionkeyboard.util.FileUtil;

/**
 * Created by chasing on 2017/12/18.
 */

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FileUtil.initFile(getApplicationContext());
    }
}
