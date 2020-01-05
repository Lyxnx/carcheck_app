package net.lyxnx.reginfo.util;

import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.widget.TextView;

public class Utils {
    
    private Utils() { }
    
    // ************************************************************************************ //
    
    public static Spanned styleText(CharSequence text, String spanText, Object what) {
        SpannableStringBuilder sb = new SpannableStringBuilder(text);
    
        int start = text.toString().indexOf(spanText);
        int end = start + spanText.length();
    
        sb.setSpan(what, start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
    
        return sb;
    }
    
    public static Spanned styleText(Spanned text, String spanText, Object what) {
        return styleText(text.toString(), spanText, what);
    }
    
    public static Spanned changeFont(Spanned text, String toChange, int dip) {
        return changeFont(text.toString(), toChange, dip);
    }
    
    public static Spanned changeFont(CharSequence text, String toChange, int dip) {
        return styleText(text, toChange, new AbsoluteSizeSpan(dip, true));
    }
    
    public static Spanned htmlify(String text) {
        return Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT);
    }
    
    public static <T> String mkString(T[] arr, String delim) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
    
        for (T x : arr) {
            if (first)
                first = false;
            else
                sb.append(delim);
        
            sb.append(x);
        }
        
        return sb.toString();
    }
    
    public static void setText(TextView tv, String text) {
        tv.setText(text);
    }
    
    public static void setText(TextView tv, Spanned text) {
        tv.setText(text);
    }
}