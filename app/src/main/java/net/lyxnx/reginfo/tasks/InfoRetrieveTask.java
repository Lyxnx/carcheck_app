package net.lyxnx.reginfo.tasks;

import android.widget.FrameLayout;
import androidx.appcompat.app.AppCompatActivity;

public class InfoRetrieveTask extends IntentlessInfoRetrieveTask {
    
    public InfoRetrieveTask(String reg, AppCompatActivity activity, FrameLayout progressContainer) {
        super(reg, activity, progressContainer);
    }
}