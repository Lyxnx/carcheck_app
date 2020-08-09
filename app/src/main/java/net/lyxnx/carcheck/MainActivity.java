package net.lyxnx.carcheck;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import net.lyxnx.carcheck.dialog.HistoryBottomSheetDialog;
import net.lyxnx.carcheck.dialog.SavedVehicleBottomSheetDialog;
import net.lyxnx.carcheck.util.RegFetcher;
import net.lyxnx.carcheck.util.RxUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.TooltipCompat;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button go = findViewById(R.id.buttonGo);
        EditText input = findViewById(R.id.input);

        input.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId != EditorInfo.IME_ACTION_DONE)
                return false;

            go.performClick();
            return true;
        });

        RegFetcher fetcher = RegFetcher.of(this);

        TooltipCompat.setTooltipText(go, getString(R.string.tooltip_lookup));
        go.setOnClickListener(v -> {
            String text = input.getText().toString();

            if (text.isEmpty()) {
                Toast.makeText(this, R.string.empty_reg, Toast.LENGTH_LONG).show();
                return;
            }

            // hide the keyboard or it looks bad
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(v.getWindowToken(), 0);

            fetcher.fetchVehicle(text)
                    .subscribe(
                            result -> {
                                Intent i = new Intent(this, VehicleInfoActivity.class);
                                i.putExtra("info", result);
                                startActivity(i);
                            },
                            RxUtils.ERROR_CONSUMER.apply(TAG, this)
                    );
        });

        Button calcs = findViewById(R.id.buttonCalcs);
        TooltipCompat.setTooltipText(calcs, getString(R.string.tooltip_calcs));
        calcs.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, CalculatorsActivity.class)));

        HistoryBottomSheetDialog historyDialog = new HistoryBottomSheetDialog(this);

        Button historyButton = findViewById(R.id.buttonHistory);
        TooltipCompat.setTooltipText(historyButton, getString(R.string.tooltip_history));
        historyButton.setOnClickListener(view -> {
            if (historyDialog.hasItems()) {
                Toast.makeText(this, getString(R.string.empty_history), Toast.LENGTH_SHORT).show();
                return;
            }

            if (!historyDialog.isAdded()) {
                historyDialog.show(getSupportFragmentManager(), HistoryBottomSheetDialog.class.getSimpleName());
            }
        });

        historyDialog.getSelectedListener()
                .subscribe(item -> {
                            Intent i = new Intent(this, VehicleInfoActivity.class);
                            i.putExtra("info", item.getInfo());
                            startActivity(i);
                        }
                );

        SavedVehicleBottomSheetDialog savedDialog = new SavedVehicleBottomSheetDialog(this);

        Button savedButton = findViewById(R.id.buttonSaved);
        TooltipCompat.setTooltipText(savedButton, getString(R.string.tooltip_saved));
        savedButton.setOnClickListener(view -> {
            if (savedDialog.hasItems()) {
                Toast.makeText(this, getString(R.string.empty_saved), Toast.LENGTH_SHORT).show();
                return;
            }

            if (!savedDialog.isAdded()) {
                savedDialog.show(getSupportFragmentManager(), SavedVehicleBottomSheetDialog.class.getSimpleName());
            }
        });

        savedDialog.getSelectedListener()
                .subscribe(item -> {
                    Intent i = new Intent(this, VehicleInfoActivity.class);
                    i.putExtra("info", item.getInfo());
                    startActivity(i);
                });
    }

    @Override
    protected void onPause() {
        super.onPause();
        ((EditText) findViewById(R.id.input)).setText("");
    }
}
