package net.lyxnx.carcheck;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;

import net.lyxnx.carcheck.adapter.HistoryRecyclerViewAdapter;
import net.lyxnx.carcheck.util.History;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.TooltipCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HistoryActivity extends AppCompatActivity {

    private HistoryRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        RecyclerView recyclerView = findViewById(R.id.vrmHistory);
        adapter = new HistoryRecyclerViewAdapter(this, History.getHistory());

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(adapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(HistoryActivity.this, R.drawable.recycler_divider));
        recyclerView.addItemDecoration(dividerItemDecoration);

        Button clear = findViewById(R.id.buttonClear);
        TooltipCompat.setTooltipText(clear, getString(R.string.tooltip_clear));
        clear.setOnClickListener(v ->
                new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.confirm_clear_history))
                        .setMessage(getString(R.string.confirm_clear_history))
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                            History.getHistory().clear();
                            finish();
                        })
                        .setNegativeButton(android.R.string.cancel, null)
                        .show()
        );
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        adapter.removeItem(item.getGroupId());

        if (History.getHistory().isEmpty()) {
            finish();
        }

        return true;
    }
}