package net.lyxnx.carcheck.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import androidx.fragment.app.Fragment
import io.reactivex.rxjava3.subjects.PublishSubject
import net.lyxnx.carcheck.R
import kotlin.math.pow

class FinanceCalculatorFragment : Fragment() {

    private val progressChangeListener = PublishSubject.create<String>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_finance_calc, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHeader(view.findViewById(R.id.monthlyRow), getString(R.string.monthly_payment))
        setHeader(view.findViewById(R.id.totalRow), getString(R.string.total_payment))

        val price = view.findViewById<SeekBar>(R.id.finance_price_item)
        val priceDisplay = view.findViewById<TextView>(R.id.finance_price_display)
        priceDisplay.text = getString(R.string.price_text, price.progress.toFloat() * 1000)

        val period = view.findViewById<SeekBar>(R.id.finance_period_item)
        val periodDisplay = view.findViewById<TextView>(R.id.finance_period_display)
        periodDisplay.text = resources.getQuantityString(R.plurals.period_text, period.progress, period.progress)

        val apr = view.findViewById<EditText>(R.id.finance_apr_item)
        progressChangeListener.subscribe { result: String ->
            if (result.isEmpty()) {
                return@subscribe
            }

            val total = price.progress * 1000.toDouble()
            val interest = apr.text.toString().toDouble()
            val months = period.progress
            val monthlyInterest = interest / 12 * .01
            val monthlyCost = total /
                    (((1 + monthlyInterest).pow(months.toDouble()) - 1) / (monthlyInterest * (1 + monthlyInterest).pow(months.toDouble())))

            setValue(view.findViewById(R.id.monthlyRow), getString(R.string.price_text, monthlyCost))
            setValue(view.findViewById(R.id.totalRow), getString(R.string.price_text, monthlyCost * months))
        }

        apr.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                progressChangeListener.onNext(s.toString())
            }
        })

        price.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                priceDisplay.text = getString(R.string.price_text, progress.toFloat() * 1000)
                progressChangeListener.onNext(apr.text.toString())
            }
        })

        period.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                periodDisplay.text = resources.getQuantityString(R.plurals.period_text, progress, progress)
                progressChangeListener.onNext(apr.text.toString())
            }
        })
    }

    private fun setHeader(view: View, header: String) {
        view.findViewById<TextView>(R.id.rowHeader).text = header
    }

    private fun setValue(view: View, value: String) {
        view.findViewById<TextView>(R.id.rowValue).text = value
    }
}