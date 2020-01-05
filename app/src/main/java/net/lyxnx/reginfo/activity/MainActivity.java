package net.lyxnx.reginfo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.TooltipCompat;
import net.lyxnx.reginfo.R;
import net.lyxnx.reginfo.activity.calculators.CalculatorsActivity;
import net.lyxnx.reginfo.tasks.History;
import net.lyxnx.reginfo.tasks.InfoRetrieveTask;

public class MainActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    
        History.getInstance().init(this);
        
        Button go = findViewById(R.id.buttonGo);
        EditText input = findViewById(R.id.input);
        
        input.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId != EditorInfo.IME_ACTION_DONE)
                return false;
            
            go.performClick();
            return true;
        });
        
        TooltipCompat.setTooltipText(go, getString(R.string.tooltip_go));
        go.setOnClickListener(v -> {
            String text = input.getText().toString();

            if (text.isEmpty()) {
                Toast.makeText(this, R.string.empty_reg, Toast.LENGTH_LONG).show();
                return;
            }

            // hide the keyboard or it looks bad
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(v.getWindowToken(), 0);

            new InfoRetrieveTask(
                    text,
                    this,
                    findViewById(R.id.progressContainer)
            ).execute();
        });
    
        Button calcs = findViewById(R.id.buttonCalcs);
        TooltipCompat.setTooltipText(calcs, getString(R.string.tooltip_calcs));
        calcs.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, CalculatorsActivity.class)));
        
        Button history = findViewById(R.id.buttonHistory);
        TooltipCompat.setTooltipText(history, getString(R.string.tooltip_history));
        history.setOnClickListener(v -> {
            if (History.getInstance().isEmpty()) {
                Toast.makeText(this, R.string.empty_history, Toast.LENGTH_SHORT).show();
                return;
            }
            
            startActivity(new Intent(MainActivity.this, HistoryActivity.class));
        });
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        ((EditText) findViewById(R.id.input)).setText("");
    }
}
