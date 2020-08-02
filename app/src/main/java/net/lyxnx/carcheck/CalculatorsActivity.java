package net.lyxnx.carcheck;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import net.lyxnx.carcheck.adapter.CalculatorsPagerAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

public class CalculatorsActivity extends AppCompatActivity {

    private ViewPager2 pager;
    private MenuItem previousItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calcs);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        BottomNavigationView navigation = findViewById(R.id.navigation);

        navigation.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_journey:
                    pager.setCurrentItem(CalculatorsPagerAdapter.JOURNEY);
                    return true;
                case R.id.action_mpg:
                    pager.setCurrentItem(CalculatorsPagerAdapter.MPG);
                    return true;
                case R.id.action_finance:
                    pager.setCurrentItem(CalculatorsPagerAdapter.FINANCE);
                    return true;
                default:
                    return false;
            }
        });

        CalculatorsPagerAdapter adapter = new CalculatorsPagerAdapter(this);

        pager = findViewById(R.id.main_content);
        pager.setOffscreenPageLimit(1);
        pager.setAdapter(adapter);
        pager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                if (previousItem != null) {
                    previousItem.setChecked(false);
                } else {
                    navigation.getMenu().getItem(0).setChecked(false);
                }

                MenuItem item = navigation.getMenu().getItem(position);
                item.setChecked(true);
                previousItem = item;
                setTitle(getString(R.string.title_calculators, adapter.getTitle(position)));
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
