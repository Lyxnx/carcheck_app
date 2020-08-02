package net.lyxnx.carcheck.util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import net.lyxnx.carcheck.BuildConfig;
import net.lyxnx.carcheck.R;

import io.reactivex.rxjava3.functions.Consumer;

public class RxUtils {

    private RxUtils() {
    }

    public static final BiFunction<String, Context, Consumer<Throwable>> ERROR_CONSUMER = (tag, context) -> throwable -> {
        if (BuildConfig.DEBUG) {
            Log.e(tag, "An error occurred: " + throwable.getMessage());
            for (StackTraceElement el : throwable.getStackTrace()) {
                Log.e(tag, el.toString());
            }
        }

        if (context != null) {
            Toast.makeText(context, context.getString(R.string.error), Toast.LENGTH_LONG).show();
        }
    };

    public static final Consumer<Object> EMPTY_CONSUMER = v -> {
    };
}