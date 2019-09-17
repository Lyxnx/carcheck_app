package net.lyxnx.reginfo.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

public class RowValue extends AppCompatTextView {
 
    {
        setTextSize(22);
    }
 
    public RowValue(Context context) {
        super(context);
    }
    
    public RowValue(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
    
    public RowValue(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
