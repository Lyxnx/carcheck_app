package net.lyxnx.carcheck.dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.StringRes
import androidx.annotation.StyleRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import net.lyxnx.carcheck.R

fun popup(context: Context, block: PopupDialogBuilder.() -> Unit) = PopupDialogBuilder(context)
        .apply(block)
        .build()

@DslMarker
annotation class PopupDsl

@PopupDsl
class PopupDialogBuilder(val context: Context) {
    internal var title: CharSequence? = null
    internal var message: CharSequence? = null
    internal var positiveButtonText: CharSequence? = null
    internal var negativeButtonText: CharSequence? = null
    internal var neutralButtonText: CharSequence? = null
    internal var positiveButtonListener: DialogInterface.OnClickListener? = null
    internal var negativeButtonListener: DialogInterface.OnClickListener? = null
    internal var neutralButtonListener: DialogInterface.OnClickListener? = null
    internal var onCancelListener: DialogInterface.OnCancelListener? = null
    internal var onDismissListener: DialogInterface.OnDismissListener? = null
    internal var cancelable = false
    internal var dismissOnButtonClick = true
    internal var showDividers = false
    internal var customViewLayoutResId = 0
    internal var customView: View? = null
    @ColorInt internal var titleColor: Int? = null
    @ColorInt internal var messageColor: Int? = null

    fun title(@StringRes titleId: Int) { title = context.getText(titleId) }
    fun title(title: CharSequence?) { this.title = title }

    fun message(@StringRes messageId: Int) { message = context.getText(messageId) }
    fun message(message: CharSequence?) { this.message = message }

    fun positiveButton(@StringRes textId: Int, listener: DialogInterface.OnClickListener?) {
        positiveButtonText = context.getText(textId)
        positiveButtonListener = listener
    }
    fun positiveButton(text: CharSequence?, listener: DialogInterface.OnClickListener?) {
        positiveButtonText = text
        positiveButtonListener = listener
    }

    fun negativeButton(@StringRes textId: Int, listener: DialogInterface.OnClickListener?) {
        negativeButtonText = context.getText(textId)
        negativeButtonListener = listener
    }
    fun negativeButton(text: CharSequence?, listener: DialogInterface.OnClickListener?) {
        negativeButtonText = text
        negativeButtonListener = listener
    }

    fun neutralButton(@StringRes textId: Int, listener: DialogInterface.OnClickListener?) {
        neutralButtonText = context.getText(textId)
        neutralButtonListener = listener
    }
    fun neutralButton(text: CharSequence?, listener: DialogInterface.OnClickListener?) {
        neutralButtonText = text
        neutralButtonListener = listener
    }

    fun cancelable() { cancelable = true }

    fun showDividers() { showDividers = true }

    fun autoDismissOnButtonClick() { dismissOnButtonClick = true }

    fun onCancel(onCancelListener: DialogInterface.OnCancelListener?) { this.onCancelListener = onCancelListener }

    fun onDismiss(onDismissListener: DialogInterface.OnDismissListener?) { this.onDismissListener = onDismissListener }

    fun messageColor(@ColorInt messageColor: Int) { this.messageColor = messageColor }

    fun titleColor(@ColorInt titleColor: Int) { this.titleColor = titleColor }

    fun view(layoutResId: Int) {
        customView = null
        customViewLayoutResId = layoutResId
    }

    fun view(view: View) {
        customView = view
        customViewLayoutResId = 0
    }

    fun build(): PopupDialog {
        val dialog = PopupDialog(context)
        dialog.init(this)
        return dialog
    }
}

class PopupDialog : Dialog {

    internal class DialogButtonListener(private val dialog: Dialog, private val dialogOnClickListener: DialogInterface.OnClickListener?, private val buttonId: Int) : View.OnClickListener {
        private var dismissOnButtonClick = true

        fun setAutoDismissOnButtonClick(dismissOnButtonClick: Boolean): DialogButtonListener {
            this.dismissOnButtonClick = dismissOnButtonClick
            return this
        }

        override fun onClick(v: View) {
            if (dialog.isShowing) {
                dialogOnClickListener?.onClick(dialog, buttonId)
                if (dismissOnButtonClick) {
                    dialog.dismiss()
                }
            }
        }
    }

    private var positiveButtonTextView: TextView? = null
    private var negativeButtonTextView: TextView? = null
    private var neutralButtonTextView: TextView? = null

    internal constructor(context: Context) : super(context, R.style.PopupDialog)
    internal constructor(context: Context, @StyleRes themeResId: Int) : super(context, themeResId)

    internal fun init(builder: PopupDialogBuilder) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setOnDismissListener(builder.onDismissListener)
        setOnCancelListener(builder.onCancelListener)
        setCancelable(builder.cancelable)

        val context = builder.context
        val inflater = LayoutInflater.from(context)
        val rootLayout = LinearLayout(context)
        rootLayout.orientation = LinearLayout.VERTICAL

        builder.title?.let {
            // Show dialog title.
            val titleTextView = inflater.inflate(R.layout.item_dialog_title, rootLayout, false) as TextView
            titleTextView.text = it

            builder.titleColor?.let { color ->
                titleTextView.setTextColor(color)
            }

            rootLayout.addView(titleTextView)

            if (builder.showDividers) {
                rootLayout.addView(inflater.inflate(R.layout.item_divider, rootLayout, false))
            }
        }

        when {
            builder.message != null -> {
                // Standard dialog message.
                val layoutResId = if (builder.title == null) R.layout.item_dialog_message_no_title else R.layout.item_dialog_message
                val messageTextView = inflater.inflate(layoutResId, rootLayout, false) as TextView
                messageTextView.text = builder.message

                if (builder.messageColor != null) {
                    messageTextView.setTextColor(builder.messageColor!!)
                }

                rootLayout.addView(messageTextView)
            }
            builder.customViewLayoutResId != 0 -> {
                rootLayout.addView(inflater.inflate(builder.customViewLayoutResId, rootLayout, false))
            }
            builder.customView != null -> {
                rootLayout.addView(builder.customView)
            }
        }

        if (builder.showDividers && (builder.positiveButtonText != null || builder.negativeButtonText != null || builder.neutralButtonText != null)) {
            // Divider between the list and the buttons.
            rootLayout.addView(inflater.inflate(R.layout.item_divider, rootLayout, false))
        }

        val buttonsLayout = inflater.inflate(R.layout.item_dialog_buttons, rootLayout, false)

        builder.positiveButtonText?.let {
            // Show positive button.
            positiveButtonTextView = buttonsLayout.findViewById(R.id.positive_button)
            positiveButtonTextView!!.text = it
            positiveButtonTextView!!.visibility = View.VISIBLE
            positiveButtonTextView!!.setOnClickListener(
                    DialogButtonListener(this, builder.positiveButtonListener, BUTTON_POSITIVE)
                            .setAutoDismissOnButtonClick(builder.dismissOnButtonClick))
        }

        builder.negativeButtonText?.let {
            // Show negative button.
            negativeButtonTextView = buttonsLayout.findViewById(R.id.negative_button)
            negativeButtonTextView!!.text = it
            negativeButtonTextView!!.visibility = View.VISIBLE
            negativeButtonTextView!!.setOnClickListener(
                    DialogButtonListener(this, builder.negativeButtonListener, BUTTON_NEGATIVE)
                            .setAutoDismissOnButtonClick(builder.dismissOnButtonClick))
            if (builder.positiveButtonText != null) {
                // Spacing between the positive and negative buttons.
                buttonsLayout.findViewById<View>(R.id.negative_button_divider).visibility = View.VISIBLE
            }
        }

        builder.neutralButtonText?.let {
            // Show neutral button.
            neutralButtonTextView = buttonsLayout.findViewById(R.id.neutral_button)
            neutralButtonTextView!!.text = it
            neutralButtonTextView!!.visibility = View.VISIBLE
            neutralButtonTextView!!.setOnClickListener(
                    DialogButtonListener(this, builder.neutralButtonListener, BUTTON_NEUTRAL)
                            .setAutoDismissOnButtonClick(builder.dismissOnButtonClick))

            if (builder.negativeButtonText != null) {
                // Spacing between the negative and neutral buttons.
                buttonsLayout.findViewById<View>(R.id.neutral_button_divider).visibility = View.VISIBLE
            }
        }

        rootLayout.addView(buttonsLayout)
        setContentView(rootLayout)
    }
}