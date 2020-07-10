package net.lyxnx.carcheck;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import static net.lyxnx.carcheck.CalculatorsActivity.*;

public class FuelCostActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mpg);
    
        EditText distance = findViewById(R.id.distance);
        EditText mpg = findViewById(R.id.mpg);
        TextView requiredFuel = findViewById(R.id.requiredFuel);
        requiredFuel.setText(getString(R.string.fuel_text, 0.0));
    
        RequiredFuelWatcher rfw = new RequiredFuelWatcher(distance, mpg, requiredFuel);
        distance.addTextChangedListener(rfw);
        mpg.addTextChangedListener(rfw);
    }
    
    public class RequiredFuelWatcher implements TextWatcher {
        private final EditText distance;
        private final EditText mpg;
        private final TextView requiredFuel;
        
        RequiredFuelWatcher(EditText distance, EditText mpg, TextView requiredFuel) {
            this.distance = distance;
            this.mpg = mpg;
            this.requiredFuel = requiredFuel;
        }
        
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
        
        @Override
        public void afterTextChanged(Editable s) { }
        
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!isDouble(distance) || !isDouble(mpg)) {
                return;
            }
            
            requiredFuel.setText(
                    getString(R.string.fuel_text, requiredFuel(toDouble(distance), toDouble(mpg)))
            );
        }
        
        private double requiredFuel(double distance, double mpg) {
            return CalculatorsActivity.round((distance / mpg) * 3.785, 2);
        }
    }
}
