package net.lyxnx.carcheck.util;

import android.widget.EditText;

import net.lyxnx.carcheck.R;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import androidx.annotation.DrawableRes;

public class Util {

    private Util() {
    }

    public static final String DATE_PATTERN = "dd MMMM yyyy HH:mm";

    public static String formatDate(LocalDateTime date) {
        return date.format(DateTimeFormatter.ofPattern(DATE_PATTERN));
    }

    @DrawableRes
    public static int getVehicleType(String type) {
        switch (type) {
            case "HCV":
                return R.drawable.ic_vehicle_hgv;
            case "LCV":
                return R.drawable.ic_vehicle_lcv;
            case "Motorcycle":
                return R.drawable.ic_vehicle_motorcycle;
            default:
                return R.drawable.ic_vehicle_car;
        }
    }

    public static double round(double value, int dp) {
        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(dp, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static double toDouble(EditText e) {
        return Double.parseDouble(e.getText().toString());
    }

    public static boolean isNotDouble(EditText e) {
        if (e.getText().toString().isEmpty())
            return true;

        try {
            toDouble(e);
            return false;
        } catch (NumberFormatException ex) {
            return true;
        }
    }
}