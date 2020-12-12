package net.lyxnx.carcheck.base;

import android.app.Application;
import net.lyxnx.carcheck.rest.Singletons;

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Singletons.INSTANCE.setApiKey(getApiKey());
    }

    static {
        System.loadLibrary("carcheck");
    }

    public native String getApiKey();
}