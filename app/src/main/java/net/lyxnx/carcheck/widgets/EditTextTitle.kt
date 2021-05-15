package net.lyxnx.carcheck.widgets

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.withStyledAttributes
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import net.lyxnx.carcheck.R

class EditTextTitle : LinearLayout {

    private val inputLayout: TextInputLayout
    private val inputEditText: TextInputEditText
    private val descriptionTextView: TextView

    constructor(context: Context) : this(context, null, 0)

    constructor(context: Context, attributeSet: AttributeSet) : this(context, attributeSet, 0)

    constructor(context: Context, attributeSet: AttributeSet?, defStyle: Int) : super(context, attributeSet, defStyle) {
        val layout = LayoutInflater.from(context).inflate(R.layout.edit_text_title_layout, this, true)

        inputLayout = layout.findViewById(R.id.input_layout)
        inputEditText = layout.findViewById(R.id.input)
        descriptionTextView = layout.findViewById(R.id.description)

        context.withStyledAttributes(attributeSet, R.styleable.EditTextTitle) {
            inputLayout.hint = getString(R.styleable.EditTextTitle_hint)
            descriptionTextView.text = getString(R.styleable.EditTextTitle_description)
            inputEditText.inputType = getInt(R.styleable.EditTextTitle_android_inputType, InputType.TYPE_CLASS_TEXT)
        }
    }

    fun getText() = inputEditText.text.toString()

    fun setText(text: String) {
        inputEditText.setText(text)
    }

    fun setHint(hint: String?) {
        inputLayout.hint = hint
    }

    fun setError(error: String?) {
        inputLayout.error = error
    }

    fun setDescription(description: String) {
        descriptionTextView.text = description
    }

    fun getInput() = inputEditText
}