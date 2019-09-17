package net.lyxnx.reginfo.tasks;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;

import java.lang.ref.WeakReference;

public abstract class RetrieveTask<T> extends AsyncTask<Void, Void, T> {
    private static Vibrator vibrator;
    
    final String reg;
    protected final WeakReference<Activity> activity;
    private final WeakReference<FrameLayout> progressContainer;
    
    RetrieveTask(String reg, Activity activity, FrameLayout progressContainer) {
        this.reg = reg;
        this.activity = new WeakReference<>(activity);
        this.progressContainer = new WeakReference<>(progressContainer);
        
        if (vibrator == null) {
            vibrator = (Vibrator) activity.getSystemService(Context.VIBRATOR_SERVICE);
        }
    }
    
    @Override
    protected final void onPreExecute() {
        AlphaAnimation in = new AlphaAnimation(0f, 1f);
        in.setDuration(200);
        progressContainer.get().setAnimation(in);
        progressContainer.get().setVisibility(View.VISIBLE);
        
        activity.get().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
    
    @Override
    protected void onPostExecute(T info) {
        AlphaAnimation out = new AlphaAnimation(1f, 0f);
        out.setDuration(200);
        progressContainer.get().setAnimation(out);
        progressContainer.get().setVisibility(View.GONE);
        
        activity.get().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        
        if (Build.VERSION.SDK_INT >= 26)
            vibrator.vibrate(VibrationEffect.createOneShot(150, VibrationEffect.DEFAULT_AMPLITUDE));
        else
            vibrator.vibrate(150);
    
        onSuccess(info);
    }
    
    @Override
    protected T doInBackground(Void... voids) {
        return fetch();
    }
    
    abstract T fetch();
    abstract void onSuccess(T info);
}