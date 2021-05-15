package net.lyxnx.carcheck.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import net.lyxnx.carcheck.R
import net.lyxnx.carcheck.widgets.InfoTableRow

class CardItemRecyclerAdapter : RecyclerView.Adapter<CardItemRecyclerAdapter.ViewHolder>() {

    var items: List<MoreInfoCardItem>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.info_card, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items!![position]

        holder.header.text = item.category

        item.items.filterNotNull().forEach { (header, value) ->
            val row = InfoTableRow(holder.itemView.context)

            row.setHeader(header)
            row.setValue(value)

            holder.table.addView(row)
        }
    }

    override fun getItemCount() = items?.size ?: 0

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val header: TextView = itemView.findViewById(R.id.cardTitle)
        val table: TableLayout = itemView.findViewById(R.id.table)
    }
}

data class MoreInfoCardItem(val category: String, val items: List<Pair<String, String>?>)