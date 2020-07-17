package net.lyxnx.carcheck.util;

import net.lyxnx.carcheck.R;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Util {

    private Util() {
    }

    public static final String DATE_PATTERN = "dd MMMM yyyy HH:mm";

    public static String formatDate(LocalDateTime date) {
        return date.format(DateTimeFormatter.ofPattern(DATE_PATTERN));
    }

    public static Integer getDrawableId(String name) {
        try {
            Field f = R.drawable.class.getDeclaredField(name);
            return f.getInt(f);
        } catch (Exception e) {
            return null;
        }
    }

    public static Integer getDrawableId(String name, Integer def) {
        Integer val = getDrawableId(name);

        return val == null ? def : val;
    }
}