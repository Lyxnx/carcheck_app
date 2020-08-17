package net.lyxnx.carcheck.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import net.lyxnx.carcheck.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static net.lyxnx.carcheck.util.Util.isNotDouble;
import static net.lyxnx.carcheck.util.Util.round;
import static net.lyxnx.carcheck.util.Util.toDouble;

public class JourneyCostCalculatorFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_journey_cost_calc, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        View milesItem = view.findViewById(R.id.milesItem);
        EditText journeyDistance = milesItem.findViewById(R.id.calcEditText);
        TextView milesDesc = milesItem.findViewById(R.id.calcDescription);

        setupItem(journeyDistance, getString(R.string.distance), milesDesc, getString(R.string.miles));

        View mpgItem = view.findViewById(R.id.mpgItem);
        EditText journeyConsumption = mpgItem.findViewById(R.id.calcEditText);
        TextView mpgDesc = mpgItem.findViewById(R.id.calcDescription);

        setupItem(journeyConsumption, getString(R.string.mpg), mpgDesc, getString(R.string.mpg));

        View fuelCostItem = view.findViewById(R.id.fuelCostItem);
        EditText fuelCost = fuelCostItem.findViewById(R.id.calcEditText);
        TextView fuelCostDesc = fuelCostItem.findViewById(R.id.calcDescription);

        setupItem(fuelCost, getString(R.string.fuel_cost), fuelCostDesc, getString(R.string.pence_litre));

        TextView journeyCost = view.findViewById(R.id.journeyCost);
        journeyCost.setText(getString(R.string.cost_text, 0.0));

        JourneyCostWatcher jcw = new JourneyCostWatcher(journeyDistance, journeyConsumption, fuelCost, journeyCost);
        journeyDistance.addTextChangedListener(jcw);
        journeyConsumption.addTextChangedListener(jcw);
        fuelCost.addTextChangedListener(jcw);
    }

    private void setupItem(EditText editText, String hint, TextView desc, String description) {
        editText.setHint(hint);
        desc.setText(description);
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
            if (isNotDouble(distance) || isNotDouble(mpg) || isNotDouble(cost)) {
                return;
            }

            double gallonsUsed = toDouble(distance) / toDouble(mpg);
            double totalCost = round((gallonsUsed * 4.54609188 * toDouble(cost)) / 100, 2);

            journeyCost.setText(
                    getString(R.string.cost_text, totalCost)
            );
        }
    }
}
