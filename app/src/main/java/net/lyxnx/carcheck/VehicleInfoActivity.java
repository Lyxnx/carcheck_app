package net.lyxnx.carcheck;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.Toast;

import net.lyxnx.carcheck.model.VehicleInfo;
import net.lyxnx.carcheck.util.RegFetcher;
import net.lyxnx.carcheck.util.RxUtils;

import java.util.stream.Stream;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.TooltipCompat;

import static net.lyxnx.carcheck.util.Util.setText;

public class VehicleInfoActivity extends InfoActivity {

    private static final String TAG = VehicleInfoActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        VehicleInfo info = (VehicleInfo) getIntent().getParcelableExtra("info");

        TableLayout table = findViewById(R.id.infoTable);
        populateTable(table, info);

        setText(findViewById(R.id.reg), info.getReg());

        String make = info.getMake();
        String model = info.getModel();
        String colour = info.getColour();
        String year = info.getRegisteredDate();

        Button gallery = findViewById(R.id.gallery);
        TooltipCompat.setTooltipText(gallery, getString(R.string.tooltip_gallery));
        gallery.setOnClickListener(v -> {
            if (Stream.of(make, model, year)
                    .anyMatch(s -> s == null || s.isEmpty() || s.equals("N/A"))) {
                Toast.makeText(this, R.string.insufficient_vrm_info, Toast.LENGTH_SHORT).show();
                return;
            }

            String url = "http://google.com#q="
                    + make + "+"
                    + TextUtils.join("+", model.split(" ")) + "+"
                    + colour + "+"
                    + year
                    + "&tbm=isch";

            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        });

        Button more = findViewById(R.id.more);
        TooltipCompat.setTooltipText(more, getString(R.string.tooltip_more));
        more.setOnClickListener(v -> {
            Intent i = new Intent(VehicleInfoActivity.this, MoreInfoActivity.class);
            i.putExtra("info", info);
            startActivity(i);
        });

        EditText reg = findViewById(R.id.reg);
        reg.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId != EditorInfo.IME_ACTION_DONE)
                return false;

            String text = reg.getText().toString();

            if (text.isEmpty()) {
                Toast.makeText(this, R.string.empty_reg, Toast.LENGTH_LONG).show();
                return false;
            }

            // hide the keyboard or it looks bad
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(v.getWindowToken(), 0);

            RegFetcher.fetchVehicle(text)
                    .compose(RxUtils.applySchedulers(this))
                    .subscribe(
                            result -> {
                                // Clear the table for new info
                                table.removeAllViews();
                                populateTable(table, result);
                            },
                            RxUtils.ERROR_CONSUMER.apply(TAG)
                    );

            return true;
        });
    }

    private void populateTable(TableLayout table, VehicleInfo info) {
        addToTable(table, getString(R.string.make), info.getMake());
        addToTable(table, getString(R.string.model), info.getModel());
        addToTable(table, getString(R.string.colour), info.getColour());
        addToTable(table, getString(R.string.bhp), info.getBHP());
        addToTable(table, getString(R.string.engine_size), info.getEngineSize());
        addToTable(table, getString(R.string.year), info.getRegisteredDate());
    }
}
