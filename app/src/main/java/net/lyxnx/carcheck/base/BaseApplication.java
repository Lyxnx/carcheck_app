package net.lyxnx.carcheck.base;

import android.app.Application;
import net.lyxnx.carcheck.rest.CarCheckApi;
import net.lyxnx.carcheck.rest.Singletons;
import net.lyxnx.simplerest.RestSingletons;

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Singletons.INSTANCE.setApiKey(getApiKey());
        RestSingletons.setApi(new CarCheckApi().build());
    }

    static {
        System.loadLibrary("carcheck");
    }

    public native String getApiKey();
}