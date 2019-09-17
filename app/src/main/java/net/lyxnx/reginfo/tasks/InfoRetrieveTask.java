package net.lyxnx.reginfo.tasks;

import android.app.Activity;
import android.widget.FrameLayout;

public class InfoRetrieveTask extends IntentlessInfoRetrieveTask {
    
    public InfoRetrieveTask(String reg, Activity activity, FrameLayout progressContainer) {
        super(reg, activity, progressContainer);
    }
}