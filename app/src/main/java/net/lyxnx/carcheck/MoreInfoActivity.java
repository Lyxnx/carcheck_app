package net.lyxnx.carcheck;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.Toast;

import net.lyxnx.carcheck.model.TaxInfo;
import net.lyxnx.carcheck.model.VehicleInfo;
import net.lyxnx.carcheck.util.Util;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

public class MoreInfoActivity extends InfoActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moreinfo);
        setTitle(getString(R.string.title_extra_info));

        VehicleInfo info = getIntent().getParcelableExtra("info");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        TableLayout table = findViewById(R.id.infoTable);
        populateTable(table, info);

        ImageView img = findViewById(R.id.co2Image);

        if (info.getTaxInfo() != null) {
            String co2Output = info.getTaxInfo().getCo2Output().toLowerCase(); // ... g/km (A)

            Integer resourceId = Util.getDrawableId("co2_" + co2Output.charAt(co2Output.length() - 2));

            if (resourceId == null) {
                Toast.makeText(this, getString(R.string.error), Toast.LENGTH_LONG).show();
                return;
            }

            img.setImageResource(resourceId);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void populateTable(TableLayout table, VehicleInfo info) {
        addToTable(table, getString(R.string.mot_status), info.getMotInfo().getStatus());

        TaxInfo taxInfo = info.getTaxInfo();
        boolean isNull = taxInfo == null;

        addToTable(table, getString(R.string.tax_status), isNull ? "N/A" : taxInfo.getStatus());
        addToTable(table, getString(R.string.tax_cost), isNull ? "N/A" : taxInfo.getCost());
        addToTable(table, getString(R.string.tax_output), isNull ? "N/A" : taxInfo.getCo2Output());
        addToTable(table, getString(R.string.euro_status), info.getEuroStatus());
        addToTable(table, getString(R.string.v5c_issued), info.getV5CIssueDate());
        addToTable(table, getString(R.string.registered_near), info.getRegistryLocation());
    }
}