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

import static net.lyxnx.carcheck.util.Util.isDouble;
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

        EditText journeyDistance = view.findViewById(R.id.journeyDistance);
        EditText journeyConsumption = view.findViewById(R.id.journeyConsumption);
        EditText fuelCost = view.findViewById(R.id.fuelCost);

        TextView journeyCost = view.findViewById(R.id.journeyCost);
        journeyCost.setText(getString(R.string.cost_text, 0.0));

        JourneyCostWatcher jcw = new JourneyCostWatcher(journeyDistance, journeyConsumption, fuelCost, journeyCost);
        journeyDistance.addTextChangedListener(jcw);
        journeyConsumption.addTextChangedListener(jcw);
        fuelCost.addTextChangedListener(jcw);
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

            double gallonsUsed = toDouble(distance) / toDouble(mpg);
            double totalCost = round((gallonsUsed * 4.54609188 * toDouble(cost)) / 100, 2);

            journeyCost.setText(
                    getString(R.string.cost_text, totalCost)
            );
        }
    }
}
