package net.lyxnx.reginfo.view;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

public class RowLine extends View {
    
    {
        setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.white));
    }
    
    public RowLine(Context context) {
        super(context);
    }
    
    public RowLine(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
    
    public RowLine(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    
    public RowLine(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
