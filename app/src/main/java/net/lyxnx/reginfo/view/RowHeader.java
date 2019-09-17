package net.lyxnx.reginfo.view;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

public class RowHeader extends AppCompatTextView {
    
    {
        setTextColor(ContextCompat.getColor(getContext(), android.R.color.white));
        setTypeface(null, Typeface.BOLD);
        setTextSize(28);
    }
    
    public RowHeader(Context context) {
        super(context);
    }
    
    public RowHeader(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
    
    public RowHeader(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
