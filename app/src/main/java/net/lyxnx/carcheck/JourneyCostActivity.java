package net.lyxnx.carcheck;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static net.lyxnx.carcheck.CalculatorsActivity.isDouble;
import static net.lyxnx.carcheck.CalculatorsActivity.toDouble;

public class JourneyCostActivity extends Fragment {

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.fragment_journey_cost);
//
//        EditText journeyDistance = findViewById(R.id.journeyDistance);
//        EditText journeyConsumption = findViewById(R.id.journeyConsumption);
//        EditText fuelCost = findViewById(R.id.fuelCost);
//        TextView journeyCost = findViewById(R.id.journeyCost);
//        journeyCost.setText(getString(R.string.cost_text, 0.0));
//
//        JourneyCostWatcher jcw = new JourneyCostWatcher(journeyDistance, journeyConsumption, fuelCost, journeyCost);
//        journeyDistance.addTextChangedListener(jcw);
//        journeyConsumption.addTextChangedListener(jcw);
//        fuelCost.addTextChangedListener(jcw);
//    }
    
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_journey_cost, container, false);
    }
    
    public class JourneyCostWatcher implements TextWatcher {
        private final EditText distance;
        private final EditText mpg;
        private final EditText cost;
        private final TextView journeyCost;

        JourneyCostWatcher(EditText distance, EditText mpg, EditText cost, TextView journeyCost) {
            this.distance = distance;
            this.mpg = mpg;
            this.cost = cost;
            this.journeyCost = journeyCost;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!isDouble(distance) || !isDouble(mpg) || !isDouble(cost)) {
                return;
            }

            journeyCost.setText(
                    getString(R.string.cost_text, journeyCost(toDouble(distance), toDouble(mpg), toDouble(cost)))
            );
        }

        private double journeyCost(double miles, double mpg, double cost) {
            double gallonsUsed = miles / mpg;
            return CalculatorsActivity.round(((gallonsUsed * 4.54609188) * cost) / 100, 2);
        }
    }
}
