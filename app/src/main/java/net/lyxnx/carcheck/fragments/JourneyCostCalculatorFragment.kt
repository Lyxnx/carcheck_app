package net.lyxnx.carcheck.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.*
import net.lyxnx.carcheck.R
import net.lyxnx.carcheck.util.round
import net.lyxnx.carcheck.widgets.EditTextTitle

class JourneyCostCalculatorFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_journey_cost_calc, container, false)
    }

    private lateinit var distanceItem: EditTextTitle
    private lateinit var consumptionItem: EditTextTitle
    private lateinit var fuelCostItem: EditTextTitle

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        distanceItem = view.findViewById(R.id.distanceItem)
        consumptionItem = view.findViewById(R.id.consumptionItem)
        fuelCostItem = view.findViewById(R.id.fuelCostItem)

        val journeyCost = view.findViewById<TextView>(R.id.journeyCost)
        journeyCost.text = getString(R.string.cost_text, 0.0)

        arrayOf(distanceItem, consumptionItem, fuelCostItem)
                .map { it.getInput() }
                .forEach {
                    it.doOnTextChanged { _, _, _, _ ->
                        val distanceNum = distanceItem.getText().toDoubleOrNull()
                        val mpgNum = consumptionItem.getText().toDoubleOrNull()
                        val costNum = fuelCostItem.getText().toDoubleOrNull()

                        if (distanceNum == null || mpgNum == null || costNum == null) {
                            return@doOnTextChanged
                        }

                        val gallonsUsed = distanceNum / mpgNum
                        val totalCost = (gallonsUsed * 4.54609188 * costNum / 100).round(2)
                        journeyCost.text = getString(R.string.cost_text, totalCost)
                    }
                }
    }
}