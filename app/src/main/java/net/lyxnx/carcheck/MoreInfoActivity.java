package net.lyxnx.carcheck;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import net.lyxnx.carcheck.adapter.CardItemRecyclerAdapter;
import net.lyxnx.carcheck.model.ExtraInfoItem;
import net.lyxnx.carcheck.model.VehicleInfo;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MoreInfoActivity extends InfoActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moreinfo);
        setTitle(getString(R.string.title_extra_info));

        VehicleInfo info = getIntent().getParcelableExtra("info");

        if (info == null) {
            Toast.makeText(this, getString(R.string.null_info), Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        RecyclerView recyclerView = findViewById(R.id.card_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        CardItemRecyclerAdapter adapter = new CardItemRecyclerAdapter(this);
        recyclerView.setAdapter(adapter);

        populateAdapter(adapter, info);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void populateAdapter(CardItemRecyclerAdapter adapter, VehicleInfo info) {
        List<ExtraInfoItem> items = new ArrayList<>();

        ExtraInfoItem basicItem = new ExtraInfoItem(getString(R.string.moreinfo_header_general));

        if (info.getEuroStatus() != null) {
            basicItem.addItem(getString(R.string.euro_status), info.getEuroStatus());
        }

        basicItem.addItem(getString(R.string.v5c_issued), info.getV5CIssueDate());

        if (info.getRegistryLocation() != null) {
            basicItem.addItem(getString(R.string.registered_near), info.getRegistryLocation());
        }

        if (info.getInsuranceGroup() != null) {
            basicItem.addItem(getString(R.string.insurance_group), info.getInsuranceGroup());
        }

        items.add(basicItem);

        ExtraInfoItem taxItem = new ExtraInfoItem(getString(R.string.moreinfo_header_tax));
        taxItem.addItem(getString(R.string.status), info.getTaxStatus().getStatus());

        if (info.getTaxStatus().getDaysLeft() != null) {
            taxItem.addItem(getString(R.string.days_left), info.getTaxStatus().getDaysLeft());
        }

        items.add(taxItem);

        ExtraInfoItem motItem = new ExtraInfoItem(getString(R.string.moreinfo_header_mot));
        motItem.addItem(getString(R.string.status), info.getMotStatus().getStatus());

        if (info.getMotStatus().getDaysLeft() != null) {
            motItem.addItem(getString(R.string.days_left), info.getMotStatus().getDaysLeft());
        }

        items.add(motItem);

        ExtraInfoItem co2Item = new ExtraInfoItem(getString(R.string.moreinfo_header_co2));
        co2Item.addItem(getString(R.string.cost_12_months), info.getCo2Info().getCost12Months());
        co2Item.addItem(getString(R.string.cost_6_months), info.getCo2Info().getCost6Months());
        co2Item.addItem(getString(R.string.output), info.getCo2Info().getOutput());

        items.add(co2Item);

        if (info.getPerformance() != null) {
            ExtraInfoItem performanceItem = new ExtraInfoItem(getString(R.string.moreinfo_header_performance));
            performanceItem.addItem(getString(R.string.zeroTo60), info.getPerformance().getZeroTo60());
            performanceItem.addItem(getString(R.string.top_speed), info.getPerformance().getTopSpeed());

            items.add(performanceItem);
        }

        if (info.getFuelEconomy() != null) {
            ExtraInfoItem fuelItem = new ExtraInfoItem(getString(R.string.moreinfo_header_fuel_economy));
            fuelItem.addItem(getString(R.string.urban), info.getFuelEconomy().getUrbanMpg());
            fuelItem.addItem(getString(R.string.extra_urban), info.getFuelEconomy().getExtraUrbanMpg());
            fuelItem.addItem(getString(R.string.combined), info.getFuelEconomy().getCombined());

            items.add(fuelItem);
        }

        adapter.setItems(items);
    }
}