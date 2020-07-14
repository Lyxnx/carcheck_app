package net.lyxnx.carcheck;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.TooltipCompat;

import net.lyxnx.carcheck.dialog.HistoryBottomSheetDialog;
import net.lyxnx.carcheck.util.History;
import net.lyxnx.carcheck.util.RegFetcher;
import net.lyxnx.carcheck.util.RxUtils;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private History history;
    private HistoryBottomSheetDialog historyDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        history = History.getHistory();
        history.initialise(this);
    
        Button go = findViewById(R.id.buttonGo);
        EditText input = findViewById(R.id.input);

        input.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId != EditorInfo.IME_ACTION_DONE)
                return false;

            go.performClick();
            return true;
        });

        TooltipCompat.setTooltipText(go, getString(R.string.tooltip_go));
        go.setOnClickListener(v -> {
            String text = input.getText().toString();

            if (text.isEmpty()) {
                Toast.makeText(this, R.string.empty_reg, Toast.LENGTH_LONG).show();
                return;
            }

            // hide the keyboard or it looks bad
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(v.getWindowToken(), 0);

            RegFetcher.fetchVehicle(text)
                    .compose(RxUtils.applySchedulers(this))
                    .subscribe(
                            result -> {
                                Intent i = new Intent(this, VehicleInfoActivity.class);
                                i.putExtra("info", result);
                                startActivity(i);
                            },
                            RxUtils.ERROR_CONSUMER.apply(TAG)
                    );
        });

        Button calcs = findViewById(R.id.buttonCalcs);
        TooltipCompat.setTooltipText(calcs, getString(R.string.tooltip_calcs));
        calcs.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, CalculatorsActivity.class)));

        historyDialog = new HistoryBottomSheetDialog();

        Button historyButton = findViewById(R.id.buttonHistory);
        TooltipCompat.setTooltipText(historyButton, getString(R.string.tooltip_history));
        historyButton.setOnClickListener(view -> {
            if (history.isEmpty()) {
                Toast.makeText(this, getString(R.string.empty_history), Toast.LENGTH_SHORT).show();
                return;
            }

            if (!historyDialog.isAdded()) {
                historyDialog.show(getSupportFragmentManager(), HistoryBottomSheetDialog.class.getSimpleName());
            }
        });

        historyDialog.getSelectedListener()
                .subscribe(item ->
                        RegFetcher.fetchVehicle(item.getVrm())
                                .compose(RxUtils.applySchedulers(this))
                                .subscribe(
                                        result -> {
                                            Intent i = new Intent(this, VehicleInfoActivity.class);
                                            i.putExtra("info", result);
                                            startActivity(i);
                                        },
                                        RxUtils.ERROR_CONSUMER.apply(TAG)
                                )
                );
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        ((EditText) findViewById(R.id.input)).setText("");
    }
}
