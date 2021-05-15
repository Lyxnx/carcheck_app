package net.lyxnx.carcheck

import android.widget.TableLayout
import androidx.appcompat.app.AppCompatActivity
import net.lyxnx.carcheck.widgets.InfoTableRow

open class InfoActivity : AppCompatActivity() {

    override fun onBackPressed() {
        finish()
    }

    fun TableLayout.add(header: String, value: String) {
        val row = InfoTableRow(context)

        row.setHeader(header)
        row.setValue(value)

        addView(row)
    }
}