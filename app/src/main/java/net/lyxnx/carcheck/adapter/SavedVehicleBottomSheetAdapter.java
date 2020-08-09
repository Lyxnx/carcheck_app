package net.lyxnx.carcheck.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.lyxnx.carcheck.R;
import net.lyxnx.carcheck.model.SavedVehicle;
import net.lyxnx.carcheck.model.VehicleInfo;
import net.lyxnx.carcheck.util.Util;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.rxjava3.subjects.PublishSubject;

public class SavedVehicleBottomSheetAdapter extends RecyclerView.Adapter<SavedVehicleBottomSheetAdapter.ViewHolder> {

    private Context context;
    private List<SavedVehicle> data;
    private PublishSubject<SavedVehicle> clickListener = PublishSubject.create();

    public SavedVehicleBottomSheetAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public SavedVehicleBottomSheetAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SavedVehicleBottomSheetAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_saved_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SavedVehicleBottomSheetAdapter.ViewHolder holder, int position) {
        SavedVehicle item = data.get(position);

        VehicleInfo info = item.getInfo();

        holder.vehicleType.setImageResource(Util.getVehicleType(item.getVehicleType()));
        holder.vrm.setText(item.getVrm());
        holder.saveInfo.setText(
                context.getString(
                        R.string.saved_info,
                        info.getRegisteredDate(),
                        info.getMake(),
                        info.getModel(),
                        info.getColour(),
                        info.getEngineSize(),
                        info.getFuelType()
                )
        );

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

        private ImageView vehicleType;
        private TextView vrm;
        private TextView saveInfo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.vehicleType = itemView.findViewById(R.id.savedVehicleType);
            this.vrm = itemView.findViewById(R.id.savedVrm);
            this.saveInfo = itemView.findViewById(R.id.saveInfo);
        }
    }
}