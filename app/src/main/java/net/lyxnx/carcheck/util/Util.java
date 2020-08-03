package net.lyxnx.carcheck.util;

import android.widget.EditText;

import net.lyxnx.carcheck.R;

import java.lang.reflect.Field;
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

    public static @DrawableRes Integer getDrawableId(String name) {
        try {
            Field f = R.drawable.class.getDeclaredField(name);
            return f.getInt(f);
        } catch (Exception e) {
            return null;
        }
    }

    public static @DrawableRes Integer getDrawableId(String name, Integer def) {
        Integer val = getDrawableId(name);

        return val == null ? def : val;
    }

    public static double round(double value, int dp) {
        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(dp, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static double toDouble(EditText e) {
        return Double.parseDouble(e.getText().toString());
    }

    public static boolean isDouble(EditText e) {
        if (e.getText().toString().isEmpty())
            return false;

        try {
            toDouble(e);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }
}