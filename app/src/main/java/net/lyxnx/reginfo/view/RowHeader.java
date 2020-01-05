package net.lyxnx.reginfo.view;

import android.content.Context;
import android.graphics.Typeface;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.AppCompatTextView;
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
