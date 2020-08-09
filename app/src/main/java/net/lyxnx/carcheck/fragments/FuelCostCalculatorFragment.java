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

import static net.lyxnx.carcheck.util.Util.isDouble;
import static net.lyxnx.carcheck.util.Util.round;
import static net.lyxnx.carcheck.util.Util.toDouble;

public class FuelCostCalculatorFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fuel_cost_calc, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        View milesItem = view.findViewById(R.id.milesItem);
        EditText distance = milesItem.findViewById(R.id.calcEditText);
        TextView milesDesc = milesItem.findViewById(R.id.calcDescription);

        setupItem(distance, getString(R.string.distance), milesDesc, getString(R.string.miles));

        View mpgItem = view.findViewById(R.id.mpgItem);
        EditText mpg = mpgItem.findViewById(R.id.calcEditText);
        TextView mpgDesc = mpgItem.findViewById(R.id.calcDescription);

        setupItem(mpg, getString(R.string.mpg), mpgDesc, getString(R.string.mpg));

        TextView requiredFuel = view.findViewById(R.id.requiredFuel);
        requiredFuel.setText(getString(R.string.fuel_text, 0.0));

        RequiredFuelWatcher rfw = new RequiredFuelWatcher(distance, mpg, requiredFuel);
        distance.addTextChangedListener(rfw);
        mpg.addTextChangedListener(rfw);
    }

    private void setupItem(EditText editText, String hint, TextView desc, String description) {
        editText.setHint(hint);
        desc.setText(description);
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
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!isDouble(distance) || !isDouble(mpg)) {
                return;
            }

            requiredFuel.setText(
                    getString(R.string.fuel_text, round((toDouble(distance) / toDouble(mpg)) * 3.785, 2))
            );
        }
    }
}
