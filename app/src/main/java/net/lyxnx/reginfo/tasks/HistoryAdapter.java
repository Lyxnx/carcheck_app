package net.lyxnx.reginfo.tasks;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import net.lyxnx.reginfo.R;

import java.util.List;

public class HistoryAdapter extends ArrayAdapter<History.HistoryItem> {
    private final Context context;
    private final List<History.HistoryItem> values;
    
    public HistoryAdapter(@NonNull Context context, List<History.HistoryItem> values) {
        super(context, R.layout.history_layout, values);
        this.context = context;
        this.values = values;
    }
    
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.history_layout, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        
        History.HistoryItem item = values.get(position);
        
        holder.vrm.setText(item.getVrm());
        holder.date.setText(item.getDate());
        
        return convertView;
    }
    
    private class ViewHolder {
        private TextView vrm;
        private TextView date;
    
        public ViewHolder(View v) {
            this.vrm = v.findViewById(R.id.historyVrm);
            this.date = v.findViewById(R.id.historyDate);
        }
    }
}