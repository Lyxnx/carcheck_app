package net.lyxnx.carcheck.util;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;

import net.lyxnx.carcheck.R;

import org.reactivestreams.Subscription;

import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;

public class Animations {
    
    public static Consumer<Subscription> showProgressSubscriber(Activity activity) {
        return subscription -> {
            FrameLayout progressContainer = getContainer(activity);
            
            AlphaAnimation in = new AlphaAnimation(0f, 1f);
            in.setDuration(200);
            
            progressContainer.setAnimation(in);
            progressContainer.setVisibility(View.VISIBLE);
            
            activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        };
    }
    
    public static Action hideProgressSubscriber(Activity activity) {
        return () -> {
            FrameLayout progressContainer = getContainer(activity);
    
            AlphaAnimation out = new AlphaAnimation(1f, 0f);
            out.setDuration(200);
    
            progressContainer.setAnimation(out);
            progressContainer.setVisibility(View.GONE);
    
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    
            Vibrator vibrator = (Vibrator) activity.getSystemService(Context.VIBRATOR_SERVICE);
    
            if (Build.VERSION.SDK_INT >= 26) {
                vibrator.vibrate(VibrationEffect.createOneShot(150, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                vibrator.vibrate(150);
            }
        };
    }
    
    private static FrameLayout getContainer(Activity activity) {
        return activity.findViewById(R.id.progress_container);
    }
}