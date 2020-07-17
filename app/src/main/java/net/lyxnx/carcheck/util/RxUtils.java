package net.lyxnx.carcheck.util;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import net.lyxnx.carcheck.BuildConfig;
import net.lyxnx.carcheck.R;
import net.lyxnx.carcheck.model.VehicleInfo;

import java.util.function.Function;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.FlowableTransformer;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class RxUtils {

    private RxUtils() {
    }

    public static final Function<String, Consumer<Throwable>> ERROR_CONSUMER = s -> throwable -> {
        if (BuildConfig.DEBUG) {
            Log.e(s, "An error occurred");
            for (StackTraceElement el : throwable.getStackTrace()) {
                Log.e(s, el.toString());
            }
        }
    };

    public static final Consumer<Object> EMPTY_CONSUMER = v -> {
    };

    public static FlowableTransformer<VehicleInfo, VehicleInfo> applySchedulers(Activity activity) {
        return observable -> observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(Animations.showProgressSubscriber(activity))
                .doOnTerminate(Animations.hideProgressSubscriber(activity))
                .switchIfEmpty(val ->
                        Toast.makeText(activity, activity.getString(R.string.no_data), Toast.LENGTH_LONG).show()
                )
                .doOnNext(result -> {
                    if (result == null) {
                        Toast.makeText(activity, activity.getString(R.string.invalid_plate), Toast.LENGTH_LONG).show();
                        return;
                    }

                    History.getHistory().insert(result.getReg(), result.getVehicleType());
                });
    }
}