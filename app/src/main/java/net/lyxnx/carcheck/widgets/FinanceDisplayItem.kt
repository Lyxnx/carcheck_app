package net.lyxnx.carcheck.widgets

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.content.withStyledAttributes
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import io.reactivex.rxjava3.subjects.PublishSubject
import net.lyxnx.carcheck.R
import net.lyxnx.carcheck.util.onProgressChanged

class FinanceDisplayItem : LinearLayout {

    private val descriptor: TextView
    private val seekBar: SeekBar
    private val display: TextView
    private val inputLayout: TextInputLayout
    private val input: TextInputEditText

    val onProgressChanged: PublishSubject<Int> = PublishSubject.create()
    val onTextChanged: PublishSubject<String> = PublishSubject.create()

    constructor(context: Context) : this(context, null, 0)

    constructor(context: Context, attributeSet: AttributeSet) : this(context, attributeSet, 0)

    constructor(context: Context, attributeSet: AttributeSet?, defStyle: Int) : super(context, attributeSet, defStyle) {
        val layout = LayoutInflater.from(context).inflate(R.layout.finance_display_layout, this, true)

        descriptor = layout.findViewById(R.id.finance_descriptor)
        seekBar = layout.findViewById(R.id.finance_bar)
        display = layout.findViewById(R.id.finance_display)
        inputLayout = layout.findViewById(R.id.input_layout)
        input = layout.findViewById(R.id.input)

        context.withStyledAttributes(attributeSet, R.styleable.FinanceProgressItem) {
            descriptor.text = getString(R.styleable.FinanceProgressItem_description)
            display.text = getString(R.styleable.FinanceProgressItem_display)
            seekBar.min = getInt(R.styleable.FinanceProgressItem_min, 0)
            seekBar.max = getInt(R.styleable.FinanceProgressItem_max, 0)
            if (!getBoolean(R.styleable.FinanceProgressItem_is_seekbar, true)) {
                seekBar.isVisible = false
                inputLayout.isVisible = true
                inputLayout.hint = getString(R.styleable.FinanceProgressItem_hint)
                input.inputType = getInt(R.styleable.FinanceProgressItem_android_inputType, InputType.TYPE_CLASS_TEXT)
                input.doOnTextChanged { text, _, _, _ ->
                    onTextChanged.onNext(text.toString())
                }
            } else {
                seekBar.isVisible = true
                inputLayout.isVisible = false
                seekBar.onProgressChanged { progress, _ ->
                    onProgressChanged.onNext(progress)
                }
            }
        }
    }

    fun setDisplay(display: String) {
        this.display.text = display
    }

    fun getProgress() = seekBar.progress

    fun getText() = input.text.toString()

    fun setText(text: String) {
        input.setText(text)
    }

    fun setHint(hint: String?) {
        inputLayout.hint = hint
    }

    fun setError(error: String?) {
        inputLayout.error = error
    }
}