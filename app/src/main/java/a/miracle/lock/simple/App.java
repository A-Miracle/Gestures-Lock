package a.miracle.lock.simple;

import android.app.Application;
import android.content.SharedPreferences;

/**
 * Created by A Miracle on 2016/8/18.
 */
public class App extends Application {

    private static final String spName = "a.miracle.lock.simple";

    private static App sApp;

    private SharedPreferences sp;

    @Override
    public void onCreate() {
        super.onCreate();
        sApp = this;
        sp = getSharedPreferences(spName, MODE_PRIVATE);
    }

    public static App get(){
        return sApp;
    }

    public SharedPreferences getSp(){
        return sp;
    }
}
