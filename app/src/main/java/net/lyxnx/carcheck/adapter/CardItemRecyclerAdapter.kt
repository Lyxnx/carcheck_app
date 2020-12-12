package net.lyxnx.carcheck.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import net.lyxnx.carcheck.R

class CardItemRecyclerAdapter(val context: Context) : RecyclerView.Adapter<CardItemRecyclerAdapter.ViewHolder>() {

    var items: List<MoreInfoCardItem>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.info_card, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items!![position]

        holder.header.text = item.category

        item.items.filterNotNull().forEach {
            val view = LayoutInflater.from(context).inflate(R.layout.row_layout, null, false)

            view.findViewById<TextView>(R.id.rowHeader).text = it.first
            view.findViewById<TextView>(R.id.rowValue).text = it.second

            holder.table.addView(view)
        }
    }

    override fun getItemCount(): Int = if (items == null) 0 else items!!.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val header = itemView.findViewById<TextView>(R.id.cardTitle)
        val table = itemView.findViewById<TableLayout>(R.id.table)
    }
}

data class MoreInfoCardItem(val category: String, val items: List<Pair<String, String>?>)