package net.lyxnx.carcheck;

import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CalculatorsActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calcs2);
    
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Calculators");
        }
    
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_journey:
                    Fragment f = new JourneyCostActivity();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_content, f)
                            .addToBackStack(null)
                            .commit();
                    return true;
                case R.id.action_mpg:
                    
                    return true;
                case R.id.action_finance:
                    
                    return true;
                default:
                    return false;
            }
        });
        
//        Button buttonMpg = findViewById(R.id.buttonMpg);
//        TooltipCompat.setTooltipText(buttonMpg, getString(R.string.tooltip_mpg));
//        buttonMpg.setOnClickListener(v -> startActivity(new Intent(this, JourneyCostActivity.class)));
//
//        Button buttonFinance = findViewById(R.id.buttonFinance);
//        TooltipCompat.setTooltipText(buttonFinance, getString(R.string.tooltip_finance));
//        buttonFinance.setOnClickListener(v -> startActivity(new Intent(this, FinanceActivity.class)));
//
//        Button buttonJourneyCost = findViewById(R.id.buttonJourneyCost);
//        TooltipCompat.setTooltipText(buttonJourneyCost, getString(R.string.tooltip_journey));
//        buttonJourneyCost.setOnClickListener(v -> startActivity(new Intent(this, FuelCostActivity.class)));
    }
    
    public static double round(double value, int dp) {
        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(dp, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
    
    public static double toDouble(EditText e) {
        return Double.valueOf(e.getText().toString());
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
