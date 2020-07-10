package net.lyxnx.carcheck.util;

import android.widget.TextView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Util {
    
    private Util() { }

    public static final String DATE_PATTERN = "dd MMMM yyyy HH:mm";

    public static void setText(TextView tv, String text) {
        tv.setText(text);
    }

    public static String formatDate(LocalDateTime date) {
        return date.format(DateTimeFormatter.ofPattern(DATE_PATTERN));
    }
}