package net.lyxnx.carcheck.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_journey_cost_calc.*
import net.lyxnx.carcheck.R
import net.lyxnx.carcheck.util.round
import net.lyxnx.carcheck.widgets.EditTextTitle

class FuelCostCalculatorFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_fuel_cost_calc, container, false)
    }

    private lateinit var distanceItem: EditTextTitle
    private lateinit var consumptionItem: EditTextTitle

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        distanceItem = view.findViewById(R.id.distanceItem)
        consumptionItem = view.findViewById(R.id.consumptionItem)

        val requiredFuel = view.findViewById<TextView>(R.id.requiredFuel)
        requiredFuel.text = getString(R.string.fuel_text, 0.0)

        arrayOf(distanceItem, consumptionItem)
                .map { it.getInput() }
                .forEach {
                    it.doOnTextChanged { _, _, _, _ ->
                        val distanceNum = distanceItem.getText().toDoubleOrNull()
                        val mpgNum = consumptionItem.getText().toDoubleOrNull()

                        if (distanceNum == null || mpgNum == null) {
                            return@doOnTextChanged
                        }

                        requiredFuel.text = getString(R.string.fuel_text, (distanceNum / mpgNum * 3.785).round(2))
                    }
                }
    }
}