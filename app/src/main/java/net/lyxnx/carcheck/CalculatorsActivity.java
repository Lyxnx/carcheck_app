package net.lyxnx.carcheck;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.math.BigDecimal;
import java.math.RoundingMode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

public class CalculatorsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calcs);
        setTitle(getString(R.string.title_calculators));

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        loadFragment(new JourneyCostCalculatorFragment());

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_journey:
                    loadFragment(new JourneyCostCalculatorFragment());
                    return true;
                case R.id.action_mpg:
                    loadFragment(new FuelCostCalculatorFragment());
                    return true;
                case R.id.action_finance:
                    loadFragment(new FinanceCalculatorFragment());
                    return true;
                default:
                    return false;
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_content, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static double round(double value, int dp) {
        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(dp, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static double toDouble(EditText e) {
        return Double.parseDouble(e.getText().toString());
    }

    public static boolean isDouble(EditText e) {
        if (e.getText().toString().isEmpty())
            return false;

        try {
            toDouble(e);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }
}
