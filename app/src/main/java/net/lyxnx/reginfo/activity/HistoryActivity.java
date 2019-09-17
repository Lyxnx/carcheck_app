package net.lyxnx.reginfo.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.TooltipCompat;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import net.lyxnx.reginfo.R;
import net.lyxnx.reginfo.tasks.History;
import net.lyxnx.reginfo.tasks.HistoryAdapter;
import net.lyxnx.reginfo.tasks.InfoRetrieveTask;

public class HistoryActivity extends AppCompatActivity {
    
    private HistoryAdapter adapter;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        
        ListView list = findViewById(R.id.vrmHistory);
        this.adapter = new HistoryAdapter(this, History.getInstance().getItems());
        list.setAdapter(adapter);
        list.setOnItemClickListener((parent, view, position, id) ->
                new InfoRetrieveTask(
                        ((History.HistoryItem) parent.getItemAtPosition(position)).getVrm(),
                        this,
                        findViewById(R.id.progressContainer)
                ).execute()
        );
        registerForContextMenu(list);
        
        Button clear = findViewById(R.id.buttonClear);
        TooltipCompat.setTooltipText(clear, getString(R.string.tooltip_clear));
        clear.setOnClickListener(v ->
                new AlertDialog.Builder(this)
                        .setTitle("Clear History?")
                        .setMessage("Clear History?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                            History.getInstance().clear();
                            History.getInstance().save(this);
                            finish();
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .show()
        );
    }
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.vrmHistory) {
            menu.add(Menu.NONE, 0, 0, "Delete");
        }
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        History.HistoryItem e = adapter.getItem(((AdapterView.AdapterContextMenuInfo) item.getMenuInfo()).position);
        History.getInstance().remove(e);
        History.getInstance().save(this);
        adapter.remove(e);
        adapter.notifyDataSetChanged();
        
        if (History.getInstance().getItems().isEmpty()) {
            finish();
        }
        
        return true;
    }
}