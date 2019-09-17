package net.lyxnx.reginfo.reg;

import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.widget.TextView;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * TAKEN FROM APACHE COMMONS-TEXT
 * - Don't need the whole library as that adds bloat when we only use a couple of methods.
 */
public class Utils {
    
    private Utils() { }
    
    /**
     * <p>Converts all the whitespace separated words in a String into capitalized words,
     * that is each word is made up of a titlecase character and then a series of
     * lowercase characters.  </p>
     *
     * <p>Whitespace is defined by {@link Character#isWhitespace(char)}.
     * A <code>null</code> input String returns <code>null</code>.
     * Capitalization uses the Unicode title case, normally equivalent to
     * upper case.</p>
     *
     * <pre>
     * WordUtils.capitalizeFully(null)        = null
     * WordUtils.capitalizeFully("")          = ""
     * WordUtils.capitalizeFully("i am FINE") = "I Am Fine"
     * </pre>
     *
     * @param str  the String to capitalize, may be null
     * @return capitalized String, <code>null</code> if null String input
     */
    public static String capitalizeFully(final String str) {
        return capitalizeFully(str, null);
    }
    
    /**
     * <p>Converts all the delimiter separated words in a String into capitalized words,
     * that is each word is made up of a titlecase character and then a series of
     * lowercase characters. </p>
     *
     * <p>The delimiters represent a set of characters understood to separate words.
     * The first string character and the first non-delimiter character after a
     * delimiter will be capitalized. </p>
     *
     * <p>A <code>null</code> input String returns <code>null</code>.
     * Capitalization uses the Unicode title case, normally equivalent to
     * upper case.</p>
     *
     * <pre>
     * WordUtils.capitalizeFully(null, *)            = null
     * WordUtils.capitalizeFully("", *)              = ""
     * WordUtils.capitalizeFully(*, null)            = *
     * WordUtils.capitalizeFully(*, new char[0])     = *
     * WordUtils.capitalizeFully("i aM.fine", {'.'}) = "I am.Fine"
     * </pre>
     *
     * @param str  the String to capitalize, may be null
     * @param delimiters  set of characters to determine capitalization, null means whitespace
     * @return capitalized String, <code>null</code> if null String input
     */
    public static String capitalizeFully(String str, final char... delimiters) {
        if (str == null || str.length() == 0) {
            return str;
        }
        str = str.toLowerCase();
        return capitalize(str, delimiters);
    }
    
    /**
     * <p>Capitalizes all the delimiter separated words in a String.
     * Only the first character of each word is changed. To convert the
     * rest of each word to lowercase at the same time,
     * use {@link #capitalizeFully(String, char[])}.</p>
     *
     * <p>The delimiters represent a set of characters understood to separate words.
     * The first string character and the first non-delimiter character after a
     * delimiter will be capitalized. </p>
     *
     * <p>A <code>null</code> input String returns <code>null</code>.
     * Capitalization uses the Unicode title case, normally equivalent to
     * upper case.</p>
     *
     * <pre>
     * WordUtils.capitalize(null, *)            = null
     * WordUtils.capitalize("", *)              = ""
     * WordUtils.capitalize(*, new char[0])     = *
     * WordUtils.capitalize("i am fine", null)  = "I Am Fine"
     * WordUtils.capitalize("i aM.fine", {'.'}) = "I aM.Fine"
     * WordUtils.capitalize("i am fine", new char[]{}) = "I am fine"
     * </pre>
     *
     * @param str  the String to capitalize, may be null
     * @param delimiters  set of characters to determine capitalization, null means whitespace
     * @return capitalized String, <code>null</code> if null String input
     * @see #capitalizeFully(String)
     */
    public static String capitalize(final String str, final char... delimiters) {
        if (str == null || str.length() == 0) {
            return str;
        }
        final Set<Integer> delimiterSet = generateDelimiterSet(delimiters);
        final int strLen = str.length();
        final int[] newCodePoints = new int[strLen];
        int outOffset = 0;
        
        boolean capitalizeNext = true;
        for (int index = 0; index < strLen;) {
            final int codePoint = str.codePointAt(index);
            
            if (delimiterSet.contains(codePoint)) {
                capitalizeNext = true;
                newCodePoints[outOffset++] = codePoint;
                index += Character.charCount(codePoint);
            } else if (capitalizeNext) {
                final int titleCaseCodePoint = Character.toTitleCase(codePoint);
                newCodePoints[outOffset++] = titleCaseCodePoint;
                index += Character.charCount(titleCaseCodePoint);
                capitalizeNext = false;
            } else {
                newCodePoints[outOffset++] = codePoint;
                index += Character.charCount(codePoint);
            }
        }
        return new String(newCodePoints, 0, outOffset);
    }
    
    private static Set<Integer> generateDelimiterSet(final char[] delimiters) {
        final Set<Integer> delimiterHashSet = new HashSet<>();
        if (delimiters == null || delimiters.length == 0) {
            if (delimiters == null) {
                delimiterHashSet.add(Character.codePointAt(new char[] {' '}, 0));
            }
            
            return delimiterHashSet;
        }
        
        for (int index = 0; index < delimiters.length; index++) {
            delimiterHashSet.add(Character.codePointAt(delimiters, index));
        }
        return delimiterHashSet;
    }
    
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
    
    public static String listToBullets(List<String> list) {
        StringBuilder sb = new StringBuilder();
        
        for (String s : list) {
            sb.append("• ").append(s).append("\n");
        }
        
        return sb.toString();
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