package net.lyxnx.carcheck.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

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
