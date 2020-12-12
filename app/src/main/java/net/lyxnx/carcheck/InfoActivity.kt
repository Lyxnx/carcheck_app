package net.lyxnx.carcheck

import android.view.LayoutInflater
import android.widget.TableLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

open class InfoActivity : AppCompatActivity() {

    override fun onBackPressed() {
        finish()
    }

    fun TableLayout.add(header: String, value: String) {
        val v = LayoutInflater.from(this@InfoActivity).inflate(R.layout.row_layout, null)

        v.findViewById<TextView>(R.id.rowHeader).text = header
        v.findViewById<TextView>(R.id.rowValue).text = value

        addView(v)
    }
}