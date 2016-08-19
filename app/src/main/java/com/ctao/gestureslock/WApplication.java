package com.ctao.gestureslock;

import android.app.Application;
import android.content.SharedPreferences;

/**
 * Created by 陈飞(ex-chenfei008@pingan.com.cn) on 16/8/18
 */
public class WApplication extends Application {

    private static final String spName = "com.ctao.gestureslock";

    private static WApplication sApplication;

    private SharedPreferences sp;

    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;
        sp = getSharedPreferences(spName, MODE_PRIVATE);
    }

    public static WApplication getApplication(){
        return sApplication;
    }


    public SharedPreferences getSp(){
        return sp;
    }
}
