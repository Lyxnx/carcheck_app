package net.lyxnx.reginfo.tasks;

import android.app.Activity;
import android.content.Intent;
import android.widget.FrameLayout;
import android.widget.Toast;
import net.lyxnx.reginfo.R;
import net.lyxnx.reginfo.activity.VehicleInfoActivity;
import net.lyxnx.reginfo.reg.Attribute;
import net.lyxnx.reginfo.reg.RegFetcher;

import java.io.Serializable;
import java.util.Map;

public class IntentlessInfoRetrieveTask extends RetrieveTask<Map<Attribute, String>> {
    
    public IntentlessInfoRetrieveTask(String reg, Activity activity, FrameLayout progressContainer) {
        super(reg, activity, progressContainer);
    }
    
    @Override
    final Map<Attribute, String> fetch() {
        return RegFetcher.getVehicleInfo(reg);
    }
    
    @Override
    final void onSuccess(Map<Attribute, String> map) {
        if (map == null) {
            Toast.makeText(activity.get(), R.string.empty_reg, Toast.LENGTH_LONG).show();
            return;
        }
    
        if (map.isEmpty()) {
            Toast.makeText(activity.get(), R.string.no_plate, Toast.LENGTH_SHORT).show();
            return;
        }
    
        History.getInstance().insert(reg);
        History.getInstance().save(activity.get());
        
        processResult(map);
    }
    
    /**
     * Override this to not start VehicleInfoActivity
     */
    public void processResult(Map<Attribute, String> info) {
        Intent i = new Intent(activity.get(), VehicleInfoActivity.class);
        i.putExtra("map", (Serializable) info);
        activity.get().startActivity(i);
    }
}