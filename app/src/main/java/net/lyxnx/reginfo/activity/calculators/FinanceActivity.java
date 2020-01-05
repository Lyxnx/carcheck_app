package net.lyxnx.reginfo.activity.calculators;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Pair;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import net.lyxnx.reginfo.R;

import java.util.function.Consumer;

public class FinanceActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finance);
    
        TextView monthlyPayments = findViewById(R.id.monthly);
        TextView totalPayment = findViewById(R.id.total);
    
        SeekBar price = findViewById(R.id.price);
        TextView priceDisplay = findViewById(R.id.priceDisplay);
        priceDisplay.setText(getString(R.string.price_text, (float) price.getProgress()*1000));
    
        SeekBar period = findViewById(R.id.period);
        TextView periodDisplay = findViewById(R.id.periodDisplay);
        periodDisplay.setText(getString(R.string.period_text, period.getProgress()));
        
        EditText apr = findViewById(R.id.apr);
    
        Consumer<String> f = (s) -> {
            if (s.isEmpty()) {
                return;
            }
        
            Pair<Double, Double> cost = calculateCost(price, apr, period);
            monthlyPayments.setText(getString(R.string.price_text, cost.first));
            totalPayment.setText(getString(R.string.price_text, cost.second));
        };
        
        apr.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override public void afterTextChanged(Editable s) { }
    
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                f.accept(s.toString());
            }
        });
        
        price.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override public void onStopTrackingTouch(SeekBar seekBar) { }
    
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                priceDisplay.setText(getString(R.string.price_text, (float) progress*1000));
            
                f.accept(apr.getText().toString());
            }
        });
        
        period.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override public void onStopTrackingTouch(SeekBar seekBar) { }
    
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                periodDisplay.setText(getString(R.string.period_text, progress));
    
                f.accept(apr.getText().toString());
            }
        });
    }
    
    // Convenience method for calculations
    
    private static Pair<Double, Double> calculateCost(SeekBar price, EditText apr, SeekBar period) {
        return calculateCost(price.getProgress() * 1000, Double.valueOf(apr.getText().toString()), period.getProgress());
    }
    
    private static Pair<Double, Double> calculateCost(double total, double interest, int months) {
        double monthlyInterest = (interest / 12) * .01;
        double monthlyCost = total /
                (((Math.pow(1 + monthlyInterest, months)) - 1) / (monthlyInterest * Math.pow((1 + monthlyInterest), months)));
        
        return new Pair<>(monthlyCost, monthlyCost * months);
    }
}