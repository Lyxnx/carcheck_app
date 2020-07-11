package net.lyxnx.carcheck.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.lyxnx.carcheck.R;
import net.lyxnx.carcheck.VehicleInfoActivity;
import net.lyxnx.carcheck.util.History;
import net.lyxnx.carcheck.util.RegFetcher;
import net.lyxnx.carcheck.util.RxUtils;
import net.lyxnx.carcheck.util.Util;

import java.lang.ref.WeakReference;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HistoryRecyclerViewAdapter extends RecyclerView.Adapter<HistoryRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = HistoryRecyclerViewAdapter.class.getSimpleName();

    private final WeakReference<Activity> activityReference;
    private final List<History.Item> data;
    private final LayoutInflater inflater;

    public HistoryRecyclerViewAdapter(Activity activity, List<History.Item> data) {
        this.activityReference = new WeakReference<>(activity);
        this.data = data;
        this.inflater = LayoutInflater.from(activity);
    }

    public History.Item getItem(int position) {
        return data.get(position);
    }

    public void removeItem(int position) {
        this.data.remove(position);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HistoryRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.layout_history_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        History.Item item = getItem(position);

        holder.vehicleType.setImageResource(Util.getDrawableId(
                "vehicletype_" + item.getVehicleType().toLowerCase(),
                R.drawable.vehicletype_car
        ));
        holder.vrm.setText(item.getVrm());
        holder.date.setText(Util.formatDate(item.getDate()));

        holder.itemView.setOnClickListener(view -> {
            String vrm = getItem(position).getVrm();

            RegFetcher.fetchVehicle(vrm)
                    .compose(RxUtils.applySchedulers(getActivity()))
                    .subscribe(
                            result -> {
                                Intent i = new Intent(getActivity(), VehicleInfoActivity.class);
                                i.putExtra("info", result);
                                getActivity().startActivity(i);
                            },
                            RxUtils.ERROR_CONSUMER.apply(TAG)
                    );
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private Activity getActivity() {
        return activityReference.get();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        private ImageView vehicleType;
        private TextView vrm;
        private TextView date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.findViewById(R.id.historyRoot).setOnCreateContextMenuListener(this);

            this.vehicleType = itemView.findViewById(R.id.historyVehicleType);
            this.vrm = itemView.findViewById(R.id.historyVrm);
            this.date = itemView.findViewById(R.id.historyDate);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            menu.add(this.getAdapterPosition(), 0, 0, "Delete");
        }
    }
}