package net.lyxnx.carcheck.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.rxjava3.subjects.PublishSubject
import net.lyxnx.carcheck.R
import net.lyxnx.carcheck.util.DateUtil
import net.lyxnx.carcheck.util.UiUtil
import net.lyxnx.carcheck.viewmodels.SavedVehicle

class HistoryBottomSheetAdapter : RecyclerView.Adapter<HistoryBottomSheetAdapter.ViewHolder>() {

    var vehicleHistory: List<SavedVehicle> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    val clickListener = PublishSubject.create<SavedVehicle>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_history_row, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = vehicleHistory[position]

        holder.vehicleType.setImageResource(UiUtil.getVehicleType(item.vehicle.vehicleType))
        holder.vrm.text = item.vehicle.vrm
        holder.date.text = DateUtil.formatDate(item.date)

        holder.itemView.setOnClickListener {
            clickListener.onNext(item)
        }
    }

    override fun getItemCount(): Int = vehicleHistory.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val vehicleType = itemView.findViewById<ImageView>(R.id.historyVehicleType)
        val vrm = itemView.findViewById<TextView>(R.id.historyVrm)
        val date = itemView.findViewById<TextView>(R.id.historyDate)
    }
}