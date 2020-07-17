package net.lyxnx.carcheck.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

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
