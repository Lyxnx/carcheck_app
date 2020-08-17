package net.lyxnx.carcheck.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.lyxnx.carcheck.R;
import net.lyxnx.carcheck.util.Util;
import net.lyxnx.carcheck.model.SavedVehicle;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.rxjava3.subjects.PublishSubject;

public class HistoryBottomSheetAdapter extends RecyclerView.Adapter<HistoryBottomSheetAdapter.ViewHolder> {

    private List<SavedVehicle> data;
    private final PublishSubject<SavedVehicle> clickListener = PublishSubject.create();

    public HistoryBottomSheetAdapter() {
    }

    @NonNull
    @Override
    public HistoryBottomSheetAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_history_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryBottomSheetAdapter.ViewHolder holder, int position) {
        SavedVehicle item = data.get(position);

        holder.vehicleType.setImageResource(Util.getVehicleType(item.getVehicleType()));
        holder.vrm.setText(item.getVrm());
        holder.date.setText(Util.formatDate(item.getDate()));

        holder.itemView.setOnClickListener(view -> clickListener.onNext(item));
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    public void setData(List<SavedVehicle> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public PublishSubject<SavedVehicle> getClickListener() {
        return clickListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView vehicleType;
        private final TextView vrm;
        private final TextView date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.vehicleType = itemView.findViewById(R.id.historyVehicleType);
            this.vrm = itemView.findViewById(R.id.historyVrm);
            this.date = itemView.findViewById(R.id.historyDate);
        }
    }
}