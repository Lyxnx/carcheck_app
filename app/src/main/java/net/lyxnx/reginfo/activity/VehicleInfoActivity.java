package net.lyxnx.reginfo.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.TooltipCompat;
import net.lyxnx.reginfo.R;
import net.lyxnx.reginfo.reg.VehicleInfo;
import net.lyxnx.reginfo.tasks.IntentlessInfoRetrieveTask;

import java.lang.ref.WeakReference;
import java.util.stream.Stream;

import static net.lyxnx.reginfo.util.Utils.mkString;
import static net.lyxnx.reginfo.util.Utils.setText;

public class VehicleInfoActivity extends InfoActivity {
    
    private VehicleInfo info;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        
        info = (VehicleInfo) getIntent().getSerializableExtra("info");
    
        TableLayout table = findViewById(R.id.infoTable);
        populateTable(table, info);
        
        setText(findViewById(R.id.reg), info.getReg());
    
        String make = info.getMake();
        String model = info.getModel();
        String colour = info.getColour();
        String year = info.getRegisteredDate();
        
        Button gallery = findViewById(R.id.gallery);
        TooltipCompat.setTooltipText(gallery, getString(R.string.tooltip_gallery));
        gallery.setOnClickListener(v -> {
            if (Stream.of(make, model, year)
                    .anyMatch(s -> s == null || s.isEmpty() || s.equals("N/A"))) {
                Toast.makeText(this, R.string.insuff_info, Toast.LENGTH_SHORT).show();
                return;
            }
            
            String url = "http://google.com#q="
                    + make + "+"
                    + mkString(model.split(" "), "+") + "+"
                    + colour + "+"
                    + year
                    + "&tbm=isch";
            
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(i);
        });
        
        Button more = findViewById(R.id.more);
        TooltipCompat.setTooltipText(more, getString(R.string.tooltip_more));
        more.setOnClickListener(v -> {
            Intent i = new Intent(VehicleInfoActivity.this, MoreInfoActivity.class);
            i.putExtra("info", info);
            startActivity(i);
        });
        
        EditText reg = findViewById(R.id.reg);
        reg.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId != EditorInfo.IME_ACTION_DONE)
                return false;
            
            String text = reg.getText().toString();
            
            if (text.isEmpty()) {
                Toast.makeText(this, R.string.empty_reg, Toast.LENGTH_LONG).show();
                return false;
            }
            
            // hide the keyboard or it looks bad
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(v.getWindowToken(), 0);
            
            new InlineInfoTask(
                    text,
                    this,
                    findViewById(R.id.progressContainer)
            ).execute();
            
            return true;
        });
    }
    
    private static class InlineInfoTask extends IntentlessInfoRetrieveTask {
        private final WeakReference<VehicleInfoActivity> ia;
        private InlineInfoTask(String reg, VehicleInfoActivity ia, FrameLayout progressContainer) {
            super(reg, ia, progressContainer);
            this.ia = new WeakReference<>(ia);
        }
        
        @Override
        public void processResult(VehicleInfo info) {
            TableLayout table = activity.get().findViewById(R.id.infoTable);
            table.removeAllViews();
            
            ia.get().info = info;
            ia.get().populateTable(table, info);
        }
    }
    
    private void populateTable(TableLayout table, VehicleInfo info) {
        addToTable(table, getString(R.string.make), info.getMake());
        addToTable(table, getString(R.string.model), info.getModel());
        addToTable(table, getString(R.string.colour), info.getColour());
        addToTable(table, getString(R.string.bhp), info.getBHP());
        addToTable(table, getString(R.string.engine), info.getEngineSize());
        addToTable(table, getString(R.string.year), info.getRegisteredDate());
    }
}
