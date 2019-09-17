package net.lyxnx.reginfo.activity.calculators;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import net.lyxnx.reginfo.R;

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
        
        apr.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override public void afterTextChanged(Editable s) { }
    
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().isEmpty()) {
                    return;
                }
                
                double monthlyCost = calculateCost(price.getProgress()*1000, Double.valueOf(s.toString()), period.getProgress());
                double total = monthlyCost * period.getProgress();
                monthlyPayments.setText(getString(R.string.price_text, monthlyCost));
                totalPayment.setText(getString(R.string.price_text, total));
            }
        });
        
        price.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override public void onStopTrackingTouch(SeekBar seekBar) { }
    
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                priceDisplay.setText(getString(R.string.price_text, (float) progress*1000));
    
                if (apr.getText().toString().isEmpty()) {
                    return;
                }
    
                double monthlyCost = calculateCost(progress*1000, Double.valueOf(apr.getText().toString()), period.getProgress());
                double total = monthlyCost * period.getProgress();
                monthlyPayments.setText(getString(R.string.price_text, monthlyCost));
                totalPayment.setText(getString(R.string.price_text, total));
            }
        });
        
        period.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override public void onStopTrackingTouch(SeekBar seekBar) { }
    
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                periodDisplay.setText(getString(R.string.period_text, progress));
    
                if (apr.getText().toString().isEmpty()) {
                    return;
                }
                
                double monthlyCost = calculateCost(price.getProgress()*1000, Double.valueOf(apr.getText().toString()), progress);
                double total = monthlyCost * progress;
                monthlyPayments.setText(getString(R.string.price_text, monthlyCost));
                totalPayment.setText(getString(R.string.price_text, total));
            }
        });
    }
    
    private static double calculateCost(double total, double interest, int months) {
        double monthlyInterest = (interest / 12) * .01;
        return total /
                (((Math.pow(1 + monthlyInterest, months)) - 1) / (monthlyInterest * Math.pow((1 + monthlyInterest), months)));
    }
}