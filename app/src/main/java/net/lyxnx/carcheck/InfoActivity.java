package net.lyxnx.carcheck;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public abstract class InfoActivity extends AppCompatActivity {

    @Override
    public void onBackPressed() {
        finish();
    }

    protected void addToTable(TableLayout table, String header, String value) {
        View v = LayoutInflater.from(this).inflate(R.layout.row_layout, null);

        setText(v.findViewById(R.id.rowHeader), header);
        setText(v.findViewById(R.id.rowValue), value);

        table.addView(v);
    }

    protected void setText(TextView textView, String text) {
        textView.setText(text);
    }
}