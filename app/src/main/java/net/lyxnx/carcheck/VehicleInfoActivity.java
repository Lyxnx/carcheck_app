package net.lyxnx.carcheck;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import net.lyxnx.carcheck.model.VehicleInfo;
import net.lyxnx.carcheck.util.RegFetcher;
import net.lyxnx.carcheck.util.RxUtils;

import java.util.stream.Stream;

import static net.lyxnx.carcheck.util.Util.setText;

public class VehicleInfoActivity extends InfoActivity {

    private static final String TAG = VehicleInfoActivity.class.getSimpleName();

    private VehicleInfo info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        info = getIntent().getParcelableExtra("info");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Results");
        }

        TableLayout table = findViewById(R.id.infoTable);
        populateTable(table, info);

        setText(findViewById(R.id.reg), info.getReg());

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (info != null) {
            getMenuInflater().inflate(R.menu.menu_info, menu);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_more:
                Intent i = new Intent(VehicleInfoActivity.this, MoreInfoActivity.class);
                i.putExtra("info", info);
                startActivity(i);
                return true;
            case R.id.action_gallery:
                String make = info.getMake();
                String model = info.getModel();
                String colour = info.getColour();
                String year = info.getRegisteredDate();

                if (Stream.of(make, model, year)
                        .anyMatch(s -> s == null || s.isEmpty() || s.equals("N/A"))) {
                    Toast.makeText(this, R.string.insufficient_vrm_info, Toast.LENGTH_SHORT).show();
                    return true;
                }

                String url = "http://google.com#q="
                        + make + "+"
                        + TextUtils.join("+", model.split(" ")) + "+"
                        + colour + "+"
                        + year
                        + "&tbm=isch";

                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                return true;
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void populateTable(TableLayout table, VehicleInfo info) {
        addToTable(table, getString(R.string.make), info.getMake());
        addToTable(table, getString(R.string.model), info.getModel());
        addToTable(table, getString(R.string.colour), info.getColour());
        addToTable(table, getString(R.string.type), info.getVehicleType());
        addToTable(table, getString(R.string.bhp), info.getBHP());
        addToTable(table, getString(R.string.engine_size), info.getEngineSize());
        addToTable(table, getString(R.string.year), info.getRegisteredDate());
    }
}
