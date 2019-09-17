package net.lyxnx.reginfo.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.TooltipCompat;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import net.lyxnx.reginfo.R;
import net.lyxnx.reginfo.reg.Attribute;
import net.lyxnx.reginfo.tasks.IntentlessInfoRetrieveTask;
import net.lyxnx.reginfo.tasks.MOTRetrieveTask;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.stream.Stream;

import static net.lyxnx.reginfo.reg.Utils.mkString;
import static net.lyxnx.reginfo.reg.Utils.setText;

public class VehicleInfoActivity extends InfoActivity {
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        
        Map<Attribute, String> info = (Map<Attribute, String>) getIntent().getSerializableExtra("map");
        
        String make = info.get(Attribute.MAKE);
        String model = info.get(Attribute.MODEL);
        String body = info.get(Attribute.BODY);
        String colour = info.get(Attribute.COLOUR);
        String year = info.get(Attribute.YEAR);
        
        TableLayout table = findViewById(R.id.infoTable);
        populateTable(table, make, model, body, colour, info.get(Attribute.BHP),
                info.get(Attribute.ENGINE_SIZE), year);
        
        setText(findViewById(R.id.reg), info.get(Attribute.REG));
        
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
                    + mkString(body.split(" "), "+") + "+"
                    + colour + "+"
                    + year
                    + "&tbm=isch";
            
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(i);
        });
        
        Button mot = findViewById(R.id.mot);
        TooltipCompat.setTooltipText(mot, getString(R.string.tooltip_mot));
        mot.setOnClickListener(v ->
                new MOTRetrieveTask(
                        info.get(Attribute.REG),
                        this,
                        findViewById(R.id.progressContainer)
                ).execute()
        );
        
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
        public void processResult(Map<Attribute, String> info) {
            TableLayout table = activity.get().findViewById(R.id.infoTable);
            table.removeAllViews();
    
            String make = info.get(Attribute.MAKE);
            String model = info.get(Attribute.MODEL);
            String body = info.get(Attribute.BODY);
            String colour = info.get(Attribute.COLOUR);
            String year = info.get(Attribute.YEAR);
    
            ia.get().populateTable(table, make, model, body, colour, info.get(Attribute.BHP),
                    info.get(Attribute.ENGINE_SIZE), year);
        }
    }
    
    private void populateTable(TableLayout table, String make, String model, String body, String colour, String bhp,
                               String engine, String year) {
        addToTable(table, getString(R.string.make), make);
        addToTable(table, getString(R.string.model), model);
        addToTable(table, getString(R.string.body), body);
        addToTable(table, getString(R.string.colour), colour);
        addToTable(table, getString(R.string.bhp), bhp);
        addToTable(table, getString(R.string.engine), engine);
        addToTable(table, getString(R.string.year), year);
    }
}
