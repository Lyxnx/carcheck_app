package net.lyxnx.carcheck.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import net.lyxnx.carcheck.R
import net.lyxnx.carcheck.widgets.FinanceDisplayItem
import net.lyxnx.carcheck.widgets.InfoTableRow
import kotlin.math.pow

class FinanceCalculatorFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_finance_calc, container, false)
    }

    private lateinit var costItem: FinanceDisplayItem
    private lateinit var periodItem: FinanceDisplayItem
    private lateinit var aprItem: FinanceDisplayItem

    private lateinit var monthlyRow: InfoTableRow
    private lateinit var totalRow: InfoTableRow

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        costItem = view.findViewById(R.id.cost_item)
        periodItem = view.findViewById(R.id.period_item)
        aprItem = view.findViewById(R.id.apr_item)

        monthlyRow = view.findViewById(R.id.monthly_row)
        totalRow = view.findViewById(R.id.total_row)

        costItem.apply {
            setDisplay(getString(R.string.price_text, getProgress().toFloat() * 1000))

            onProgressChanged.subscribe {
                setDisplay(getString(R.string.price_text, getProgress().toFloat() * 1000))
                calculateValues()
            }
        }
        periodItem.apply {
            setDisplay(resources.getQuantityString(R.plurals.period_text, getProgress(), getProgress()))

            onProgressChanged.subscribe {
                setDisplay(resources.getQuantityString(R.plurals.period_text, getProgress(), getProgress()))
                calculateValues()
            }
        }
        aprItem.onTextChanged.subscribe {
            calculateValues()
        }
    }

    private fun calculateValues() {
        val interest = with(aprItem.getText()) {
            if (this.isEmpty()) 0.toDouble() else this.toDouble()
        }

        val total = costItem.getProgress() * 1000.toDouble()
        val months = periodItem.getProgress()

        if (interest == 0.toDouble()) {
            monthlyRow.setValue(getString(R.string.price_text, total / months))
            totalRow.setValue(getString(R.string.price_text, total))
            return
        }

        val monthlyInterest = interest / 12 * .01
        val monthlyCost = total /
                (((1 + monthlyInterest).pow(months.toDouble()) - 1) / (monthlyInterest * (1 + monthlyInterest).pow(months.toDouble())))

        monthlyRow.setValue(getString(R.string.price_text, monthlyCost))
        totalRow.setValue(getString(R.string.price_text, monthlyCost * months))
    }
}