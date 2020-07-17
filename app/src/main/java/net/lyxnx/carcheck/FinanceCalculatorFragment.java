package net.lyxnx.carcheck;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import io.reactivex.rxjava3.subjects.PublishSubject;

import static net.lyxnx.carcheck.CalculatorsActivity.toDouble;

public class FinanceCalculatorFragment extends Fragment {

    private PublishSubject<String> progressChangeListener = PublishSubject.create();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_finance_calc, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setHeader(view.findViewById(R.id.monthlyRow), getString(R.string.monthly_payment));
        setHeader(view.findViewById(R.id.totalRow), getString(R.string.total_payment));

        SeekBar price = view.findViewById(R.id.price);
        TextView priceDisplay = view.findViewById(R.id.priceDisplay);
        priceDisplay.setText(getString(R.string.price_text, (float) price.getProgress() * 1000));

        SeekBar period = view.findViewById(R.id.period);
        TextView periodDisplay = view.findViewById(R.id.periodDisplay);
        periodDisplay.setText(getString(R.string.period_text, period.getProgress()));

        EditText apr = view.findViewById(R.id.apr);

        progressChangeListener
                .subscribe(result -> {
                    if (result.isEmpty()) {
                        return;
                    }

                    double total = price.getProgress() * 1000;
                    double interest = toDouble(apr);
                    int months = period.getProgress();

                    double monthlyInterest = (interest / 12) * .01;
                    double monthlyCost = total /
                            (((Math.pow(1 + monthlyInterest, months)) - 1) / (monthlyInterest * Math.pow((1 + monthlyInterest), months)));

                    setValue(view.findViewById(R.id.monthlyRow), getString(R.string.price_text, monthlyCost));
                    setValue(view.findViewById(R.id.totalRow), getString(R.string.price_text, monthlyCost * months));
                });

        apr.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                progressChangeListener.onNext(s.toString());
            }
        });

        price.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                priceDisplay.setText(getString(R.string.price_text, (float) progress * 1000));

                progressChangeListener.onNext(apr.getText().toString());
            }
        });

        period.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                periodDisplay.setText(getString(R.string.period_text, progress));

                progressChangeListener.onNext(apr.getText().toString());
            }
        });
    }

    private void setHeader(View view, String header) {
        ((TextView) view.findViewById(R.id.rowHeader)).setText(header);
    }

    private void setValue(View view, String value) {
        ((TextView) view.findViewById(R.id.rowValue)).setText(value);
    }
}