package net.lyxnx.reginfo.tasks;

import android.content.Intent;
import android.widget.FrameLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import net.lyxnx.reginfo.R;
import net.lyxnx.reginfo.activity.VehicleInfoActivity;
import net.lyxnx.reginfo.reg.RegFetcher;
import net.lyxnx.reginfo.reg.VehicleInfo;

public class IntentlessInfoRetrieveTask extends RetrieveTask<VehicleInfo> {
    
    public IntentlessInfoRetrieveTask(String reg, AppCompatActivity activity, FrameLayout progressContainer) {
        super(reg, activity, progressContainer);
    }
    
    @Override
    final VehicleInfo fetch() {
        return RegFetcher.getVehicleInfo(reg);
    }
    
    @Override
    final void onSuccess(VehicleInfo info) {
        if (info == null) {
            Toast.makeText(activity.get(), activity.get().getString(R.string.invalid_plate), Toast.LENGTH_LONG).show();
            return;
        }
    
        History.getInstance().insert(reg);
        History.getInstance().save(activity.get());
        
        processResult(info);
    }
    
    /**
     * Override this to not start VehicleInfoActivity
     */
    public void processResult(VehicleInfo info) {
        Intent i = new Intent(activity.get(), VehicleInfoActivity.class);
        i.putExtra("info", info);
        activity.get().startActivity(i);
    }
}