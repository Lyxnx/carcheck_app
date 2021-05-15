package net.lyxnx.carcheck.util

import android.app.Activity
import android.text.Editable
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import android.widget.SeekBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import net.lyxnx.carcheck.R
import net.lyxnx.carcheck.dialog.popup
import kotlin.math.round

fun AppCompatActivity.toggleProgress(show: Boolean) {
    findViewById<View>(R.id.progress_container).visibility = if (show) View.VISIBLE else View.GONE
}

fun AppCompatActivity.showAlert(title: String, message: String) {
    popup(this) {
        title(title)
        message(message)
        positiveButton(android.R.string.ok, null)
    }.show()
}

fun AppCompatActivity.showAlert(message: String) {
    showAlert(getString(R.string.info), message)
}

fun AppCompatActivity.showError(message: String) {
    showAlert(getString(R.string.error), message)
}

fun AppCompatActivity.showError(throwable: Throwable) {
    throwable.message?.let { showAlert(getString(R.string.error), it) }
}

fun Activity.hideKeyboard(view: View) {
    (getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager)
            .hideSoftInputFromWindow(view.windowToken, 0)
}

fun String.join(delim: String, vararg strings: String): String {
    return TextUtils.join(delim, listOf(this, *strings))
}

fun Double.round(decimals: Int): Double {
    var multiplier = 1.0
    repeat(decimals) { multiplier *= 10 }
    return round(this * multiplier) / multiplier
}

fun SeekBar.onProgressChanged(onProgressChanged: (Int, Boolean) -> Unit) {
    setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
        override fun onStartTrackingTouch(seekBar: SeekBar) {}
        override fun onStopTrackingTouch(seekBar: SeekBar) {}
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            onProgressChanged(progress, fromUser)
        }
    })
}