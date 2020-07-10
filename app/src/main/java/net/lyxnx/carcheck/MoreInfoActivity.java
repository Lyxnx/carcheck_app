package net.lyxnx.carcheck;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.Toast;
import net.lyxnx.carcheck.model.TaxInfo;
import net.lyxnx.carcheck.model.VehicleInfo;

import java.lang.reflect.Field;

public class MoreInfoActivity extends InfoActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moreinfo);

        VehicleInfo info = getIntent().getParcelableExtra("info");

        TableLayout table = findViewById(R.id.infoTable);
        populateTable(table, info);

        Button backBtn = findViewById(R.id.backButton);
        backBtn.setText(info.getReg());
        backBtn.setOnClickListener(v -> finish());

        ImageView img = findViewById(R.id.co2Image);

        if (info.getTaxInfo() != null) {
            String co2Output = info.getTaxInfo().getCo2Output().toLowerCase(); // ... g/km (A)

            img.setImageResource(getResId("co2_" + co2Output.charAt(co2Output.length() - 2)));
        }
    }

    private void populateTable(TableLayout table, VehicleInfo info) {
        addToTable(table, getString(R.string.mot), info.getMotInfo().getStatus());

        TaxInfo taxInfo = info.getTaxInfo();
        boolean isNull = taxInfo == null;

        addToTable(table, getString(R.string.tax), isNull ? "N/A" : taxInfo.getStatus());
        addToTable(table, getString(R.string.tax_cost), isNull ? "N/A" : taxInfo.getCost());
        addToTable(table, getString(R.string.tax_output), isNull ? "N/A" : taxInfo.getCo2Output());
    }

    /**
     * converts a string into its R.id value
     */
    private int getResId(String str) {
        try {
            Field f = R.drawable.class.getDeclaredField(str);
            return f.getInt(f);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "An error occurred: " + e.getMessage(), Toast.LENGTH_LONG).show();
            return -1;
        }
    }
}