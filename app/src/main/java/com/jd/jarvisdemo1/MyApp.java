package com.jd.jarvisdemo1;

import android.app.Application;

import com.iflytek.cloud.SpeechUtility;

/**
 * Created by JarvisDong on 2017/8/22.
 */

public class MyApp extends Application {
    @Override
    public void onCreate() {
        SpeechUtility.createUtility(MyApp.this, "appid=" + getString(R.string.app_id));
        super.onCreate();

    }
}
