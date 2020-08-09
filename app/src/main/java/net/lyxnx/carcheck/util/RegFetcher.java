package net.lyxnx.carcheck.util;

import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.Toast;

import net.lyxnx.carcheck.R;
import net.lyxnx.carcheck.model.Attribute;
import net.lyxnx.carcheck.model.CO2Info;
import net.lyxnx.carcheck.model.FuelEconomy;
import net.lyxnx.carcheck.model.StatusItem;
import net.lyxnx.carcheck.model.VehicleInfo;
import net.lyxnx.carcheck.model.VehiclePerformance;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class RegFetcher {

    // Omit the www or it'll throw a wobbly
    private static final String URL = "https://totalcarcheck.co.uk/FreeCheck";

    private static final String TR_NOT_COLSPAN = "tr:not(:has(td[colspan]))";
    private static final String REGNO = "regno";

    private static final int VEHICLE_MOT_SUMMARY_TABLE = 6;
    private static final int VEHICLE_TAX_SUMMARY_TABLE = 7;
    private static final int VEHICLE_DETAILS_TABLE = 8;
    private static final int VEHICLE_TAX_DETAIL_TABLE = 11;
    private static final int VEHICLE_EMISSION_TABLE = 12;

    private static final int INSURANCE_GROUP = 0;

    private static final List<String> INVALID_STATUSES = Arrays.asList("Expired", "SORN", "No data available");

    private final AppCompatActivity activity;

    private RegFetcher(AppCompatActivity activity) {
        this.activity = activity;
    }

    public static RegFetcher of(AppCompatActivity activity) {
        return new RegFetcher(activity);
    }

    public Flowable<VehicleInfo> fetchVehicle(String reg) {
        return Flowable.defer(() -> {
            try {
                VehicleInfo info = getVehicleInfo0(reg);

                return info != null ? Flowable.just(info) : Flowable.empty();
            } catch (Exception ex) {
                return Flowable.error(ex);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(subscription -> {
                    FrameLayout progressContainer = activity.findViewById(R.id.progress_container);

                    AlphaAnimation in = new AlphaAnimation(0f, 1f);
                    in.setDuration(200);

                    progressContainer.setAnimation(in);
                    progressContainer.setVisibility(View.VISIBLE);

                    activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                })
                .doOnTerminate(() -> {
                    FrameLayout progressContainer = activity.findViewById(R.id.progress_container);

                    AlphaAnimation out = new AlphaAnimation(1f, 0f);
                    out.setDuration(200);

                    progressContainer.setAnimation(out);
                    progressContainer.setVisibility(View.GONE);

                    activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                    Vibrator vibrator = (Vibrator) activity.getSystemService(Context.VIBRATOR_SERVICE);

                    if (Build.VERSION.SDK_INT >= 26) {
                        vibrator.vibrate(VibrationEffect.createOneShot(150, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        vibrator.vibrate(150);
                    }
                })
                .switchIfEmpty(val ->
                        Toast.makeText(activity, activity.getString(R.string.no_data), Toast.LENGTH_LONG).show()
                )
                .doOnNext(result -> {
                    if (result == null) {
                        Toast.makeText(activity, activity.getString(R.string.invalid_plate), Toast.LENGTH_LONG).show();
                        return;
                    }

                    Singletons.getHistoryManager(activity).insert(result);
                });
    }

    private VehicleInfo getVehicleInfo0(String reg) throws IOException {
        Element root = Jsoup.connect(URL)
                .data(REGNO, reg)
                .get()
                .body();

        Elements tables = root.select("table.basicResults");

        if (tables == null || tables.isEmpty()) {
            return null;
        }

        // The title row spans 2 columns, so select all but that
        Elements trs = tables.get(VEHICLE_DETAILS_TABLE)
                .select(TR_NOT_COLSPAN);

        Map<Attribute, String> attributes = new HashMap<>();
        attributes.put(Attribute.REG, reg.toUpperCase());

        for (Element el : trs) {
            Attribute attribute = Attribute.of(el.selectFirst("td.certLabel").text());

            if (attribute == null) {
                continue;
            }

            String data = el.selectFirst("td.certData").text();

            attributes.put(attribute, attribute.mutate(data));
        }

        Elements extraRows = root.selectFirst("table:not([class])")
                .select(TR_NOT_COLSPAN);

        String insuranceGroup = getTableRowEntry(extraRows.get(INSURANCE_GROUP));

        return new VehicleInfo(
                attributes, getMotStatus(tables), getTaxInfo(tables), getCo2Info(tables),
                insuranceGroup, getFuelEconomy(extraRows), getPerformance(extraRows)
        );
    }

    private StatusItem getMotStatus(Elements tables) {
        Elements data = tables.get(VEHICLE_MOT_SUMMARY_TABLE)
                .select(TR_NOT_COLSPAN);

        String status = getTableRowEntry(data.get(0));

        // If it is expired, data.get(1) will throw an error since there are no days left
        if (status != null && isInvalidStatus(status)) {
            return new StatusItem(status, null);
        }

        return new StatusItem(status, getTableRowEntry(data.get(1)));
    }

    private static boolean isInvalidStatus(String status) {
        return INVALID_STATUSES.stream()
                .anyMatch(status::contains);
    }

    private String getTableRowEntry(Element row) {
        Element item = row.selectFirst("td span");

        if (item == null) {
            return null;
        }

        return item.text();
    }

    private StatusItem getTaxInfo(Elements tables) {
        Elements data = tables.get(VEHICLE_TAX_SUMMARY_TABLE)
                .select(TR_NOT_COLSPAN);

        String status = getTableRowEntry(data.get(0));

        // If it is expired, data.get(1) will throw an error since there are no days left
        if (status != null && isInvalidStatus(status)) {
            return new StatusItem(status, null);
        }

        return new StatusItem(status, getTableRowEntry(data.get(1)));
    }

    private CO2Info getCo2Info(Elements tables) {
        Elements taxInfoTable = tables.get(VEHICLE_TAX_DETAIL_TABLE)
                .select(TR_NOT_COLSPAN);

        String cost12;
        String cost6;

        Elements costElements = taxInfoTable.select("td span");
        // Is null if the tax info is unavailable
        if (costElements == null || costElements.isEmpty()) {
            cost12 = "N/A";
            cost6 = "N/A";
        } else {
            cost12 = costElements.get(0).text();
            cost6 = costElements.get(1).text();
        }

        String co2Output = tables.get(VEHICLE_EMISSION_TABLE).selectFirst("td span").text();

        return new CO2Info(cost12, cost6, co2Output);
    }

    private FuelEconomy getFuelEconomy(Elements rows) {
        Elements mpgRows = rows.select("td:contains(mpg)");

        if (mpgRows.isEmpty()) {
            return null;
        }

        return new FuelEconomy(
                getTableRowEntry(mpgRows.get(0)),
                getTableRowEntry(mpgRows.get(1)),
                getTableRowEntry(mpgRows.get(2))
        );
    }

    private VehiclePerformance getPerformance(Elements rows) {
        Elements perfRows = rows.select("td.certData:contains(mph),td.certData:contains(secs)");

        if (perfRows.isEmpty()) {
            return null;
        }

        if (perfRows.size() == 1) {
            return new VehiclePerformance(getTableRowEntry(perfRows.get(0)), null);
        }

        return new VehiclePerformance(
                getTableRowEntry(perfRows.get(0)),
                getTableRowEntry(perfRows.get(1))
        );
    }
}