package net.lyxnx.carcheck;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import net.lyxnx.carcheck.adapter.HistoryRecyclerViewAdapter;
import net.lyxnx.carcheck.util.History;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HistoryActivity extends AppCompatActivity {

    private HistoryRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("History");
        }

        RecyclerView recyclerView = findViewById(R.id.vrmHistory);
        adapter = new HistoryRecyclerViewAdapter(this, History.getHistory());

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_history, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.confirm_clear_history_title))
                        .setMessage(getString(R.string.confirm_clear_history_text))
                        .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                            History.getHistory().clear();
                            finish();
                        })
                        .setNegativeButton(android.R.string.cancel, null)
                        .show();
                return true;
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        adapter.removeItem(item.getGroupId());

        if (History.getHistory().isEmpty()) {
            finish();
        }

        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        adapter.notifyDataSetChanged();
    }
}