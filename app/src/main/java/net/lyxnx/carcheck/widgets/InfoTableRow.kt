package net.lyxnx.carcheck.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.withStyledAttributes
import net.lyxnx.carcheck.R

class InfoTableRow : LinearLayout {

    private val rowHeader: TextView
    private val rowValue: TextView

    constructor(context: Context) : this(context, null, 0)

    constructor(context: Context, attributeSet: AttributeSet) : this(context, attributeSet, 0)

    constructor(context: Context, attributeSet: AttributeSet?, defStyle: Int) : super(context, attributeSet, defStyle) {
        val layout = LayoutInflater.from(context).inflate(R.layout.infotable_row_layout, this, true)

        rowHeader = layout.findViewById(R.id.row_header)
        rowValue = layout.findViewById(R.id.row_value)

        context.withStyledAttributes(attributeSet, R.styleable.InfoTableRow) {
            rowHeader.text = getString(R.styleable.InfoTableRow_header)
            rowValue.text = getString(R.styleable.InfoTableRow_value)
        }
    }

    fun setHeader(header: String) {
        rowHeader.text = header
    }

    fun setValue(value: String) {
        rowValue.text = value
    }
}