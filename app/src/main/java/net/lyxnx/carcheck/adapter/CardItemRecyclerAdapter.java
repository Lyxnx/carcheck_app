package net.lyxnx.carcheck.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;

import net.lyxnx.carcheck.R;
import net.lyxnx.carcheck.model.CardItem;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CardItemRecyclerAdapter extends RecyclerView.Adapter<CardItemRecyclerAdapter.ViewHolder> {

    private final Context context;
    private List<CardItem> items;

    public CardItemRecyclerAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public CardItemRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.info_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CardItemRecyclerAdapter.ViewHolder holder, int position) {
        CardItem item = items.get(position);

        holder.header.setText(item.getHeader());

        item.getItems().entrySet().stream()
                .map(e -> {
                    View view = LayoutInflater.from(context).inflate(R.layout.row_layout, null, false);

                    ((TextView) view.findViewById(R.id.rowHeader)).setText(e.getKey());
                    ((TextView) view.findViewById(R.id.rowValue)).setText(e.getValue());

                    return view;
                })
                .forEach(view -> holder.table.addView(view));
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    public void setItems(List<CardItem> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView header;
        TableLayout table;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            header = itemView.findViewById(R.id.cardTitle);
            table = itemView.findViewById(R.id.table);
        }
    }
}