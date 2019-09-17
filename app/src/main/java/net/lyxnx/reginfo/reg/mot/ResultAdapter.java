package net.lyxnx.reginfo.reg.mot;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import net.lyxnx.reginfo.R;

import java.util.List;

import static net.lyxnx.reginfo.reg.Utils.listToBullets;

public class ResultAdapter extends BaseExpandableListAdapter {
    private final Context context;
    private final List<MOTResult> results;
    
    public ResultAdapter(Context context, List<MOTResult> results) {
        this.context = context;
        this.results = results;
    }
    
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return results.get(groupPosition);
    }
    
    @Override
    public int getGroupCount() {
        return results.size();
    }
    
    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }
    
    @Override
    public Object getGroup(int groupPosition) {
        return results.get(groupPosition);
    }
    
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }
    
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }
    
    @Override
    public boolean hasStableIds() {
        return true;
    }
    
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ViewHolder holder;
        
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = LayoutInflater.from(context).inflate(R.layout.mot_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        
        MOTResult item = results.get(groupPosition);
        
        if (item == null) {
            throw new IllegalArgumentException("Null MOT Result item");
        }
        
        holder.icon.setImageResource(item.isPass() ? R.drawable.yes : R.drawable.no);
        holder.date.setText(item.getDate());
        holder.mileage.setText(item.getMileage());
        
        return convertView;
    }
    
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        // this took a long time to fix, but dont reuse views because that fucks things right up :)
        convertView = LayoutInflater.from(context).inflate(R.layout.mot_entry, parent, false);
        
        MOTResult result = results.get(groupPosition);
        
        TextView expiry = convertView.findViewById(R.id.expiry);
        if (result.getExpiry() != null) {
            expiry.setText(result.getExpiry());
        } else {
            convertView.findViewById(R.id.group_expiry).setVisibility(View.GONE);
        }

        TextView refusals = convertView.findViewById(R.id.refusals);
        if (!result.getRefusalNotices().isEmpty()) {
            refusals.setText(listToBullets(result.getRefusalNotices()));
        } else {
            convertView.findViewById(R.id.group_refusals).setVisibility(View.GONE);
        }
        
        TextView advisories = convertView.findViewById(R.id.advisories);
        if (!result.getAdvisoryNotices().isEmpty()) {
            advisories.setText(listToBullets(result.getAdvisoryNotices()));
        } else {
            convertView.findViewById(R.id.group_advisories).setVisibility(View.GONE);
        }
        
        return convertView;
    }
    
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
    
    private class ViewHolder {
        private ImageView icon;
        private TextView date;
        private TextView mileage;
        
        public ViewHolder(View v) {
            this.icon = v.findViewById(R.id.icon);
            this.date = v.findViewById(R.id.itemDate);
            this.mileage = v.findViewById(R.id.itemMileage);
        }
    }
}