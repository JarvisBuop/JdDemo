//package com.jd.jarvisdemo1;
//
//import android.annotation.TargetApi;
//import android.app.Application;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Build;
//import android.support.multidex.MultiDex;
//
//import com.iflytek.cloud.SpeechUtility;
//import com.jd.jarvisdemo1.Log.MyLogImp;
//import com.jd.jarvisdemo1.util.SampleApplicationContext;
//import com.jd.jarvisdemo1.util.TinkerManager;
//import com.tencent.tinker.anno.DefaultLifeCycle;
//import com.tencent.tinker.lib.tinker.Tinker;
//import com.tencent.tinker.lib.tinker.TinkerInstaller;
//import com.tencent.tinker.loader.app.DefaultApplicationLike;
//import com.tencent.tinker.loader.shareutil.ShareConstants;
//
///**
// * Created by JarvisDong on 2017/8/22.
// */
//@SuppressWarnings("unused")
//@DefaultLifeCycle(application = "tinker.sample.android.app.SampleApplication",
//        flags = ShareConstants.TINKER_ENABLE_ALL,
//        loadVerifyFlag = false)
//public class MyApp extends DefaultApplicationLike {
//
//    public MyApp(Application application, int tinkerFlags, boolean tinkerLoadVerifyFlag, long applicationStartElapsedTime, long applicationStartMillisTime, Intent tinkerResultIntent) {
//        super(application, tinkerFlags, tinkerLoadVerifyFlag, applicationStartElapsedTime, applicationStartMillisTime, tinkerResultIntent);
//    }
//
//    /**
//     * install multiDex before install tinker
//     * so we don't need to put the tinker lib classes in the main dex
//     *
//     * @param base
//     */
//    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
//    @Override
//    public void onBaseContextAttached(Context base) {
//        super.onBaseContextAttached(base);
//        //you must install multiDex whatever tinker is installed!
//        MultiDex.install(base);
//
//        SampleApplicationContext.application = getApplication();
//        SampleApplicationContext.context = getApplication();
//        TinkerManager.setTinkerApplicationLike(this);
//
//        TinkerManager.initFastCrashProtect();
//        //should set before tinker is installed
//        TinkerManager.setUpgradeRetryEnable(true);
//
//        //optional set logIml, or you can use default debug log
//        TinkerInstaller.setLogIml(new MyLogImp());
//
//        //installTinker after load multiDex
//        //or you can put com.tencent.tinker.** to main dex
//        TinkerManager.installTinker(this);
//        Tinker tinker = Tinker.with(getApplication());
//    }
//
//    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
//    public void registerActivityLifecycleCallbacks(Application.ActivityLifecycleCallbacks callback) {
//        getApplication().registerActivityLifecycleCallbacks(callback);
//    }
//
//    //public class MyApp extends Application {
//    @Override
//    public void onCreate() {
//        SpeechUtility.createUtility(getApplication(), "appid=" + getApplication().getString(R.string.app_id));
//        super.onCreate();
//
//    }
//}
