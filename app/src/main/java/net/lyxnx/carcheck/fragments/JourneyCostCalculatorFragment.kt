package net.lyxnx.carcheck.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import net.lyxnx.carcheck.R
import net.lyxnx.carcheck.util.round

class JourneyCostCalculatorFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_journey_cost_calc, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val milesItem = view.findViewById<View>(R.id.milesItem)
        val journeyDistance = milesItem.findViewById<EditText>(R.id.calcEditText)
        val milesDesc = milesItem.findViewById<TextView>(R.id.calcDescription)

        setupItem(journeyDistance, getString(R.string.distance), milesDesc, getString(R.string.miles))

        val mpgItem = view.findViewById<View>(R.id.mpgItem)
        val journeyConsumption = mpgItem.findViewById<EditText>(R.id.calcEditText)
        val mpgDesc = mpgItem.findViewById<TextView>(R.id.calcDescription)

        setupItem(journeyConsumption, getString(R.string.mpg), mpgDesc, getString(R.string.mpg))

        val fuelCostItem = view.findViewById<View>(R.id.fuelCostItem)
        val fuelCost = fuelCostItem.findViewById<EditText>(R.id.calcEditText)
        val fuelCostDesc = fuelCostItem.findViewById<TextView>(R.id.calcDescription)

        setupItem(fuelCost, getString(R.string.fuel_cost), fuelCostDesc, getString(R.string.pence_litre))

        val journeyCost = view.findViewById<TextView>(R.id.journeyCost)
        journeyCost.text = getString(R.string.cost_text, 0.0)

        val jcw = JourneyCostWatcher(journeyDistance, journeyConsumption, fuelCost, journeyCost)

        journeyDistance.addTextChangedListener(jcw)
        journeyConsumption.addTextChangedListener(jcw)
        fuelCost.addTextChangedListener(jcw)
    }

    private fun setupItem(editText: EditText, hint: String, desc: TextView, description: String) {
        editText.hint = hint
        desc.text = description
    }

    inner class JourneyCostWatcher internal constructor(private val distance: EditText, private val mpg: EditText, private val cost: EditText, private val journeyCost: TextView) : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun afterTextChanged(s: Editable) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            val distanceNum = distance.text.toString().toDoubleOrNull()
            val mpgNum = mpg.text.toString().toDoubleOrNull()
            val costNum = cost.text.toString().toDoubleOrNull()

            if (distanceNum == null || mpgNum == null || costNum == null) {
                return
            }

            val gallonsUsed = distanceNum / mpgNum
            val totalCost = (gallonsUsed * 4.54609188 * costNum / 100).round(2)
            journeyCost.text = getString(R.string.cost_text, totalCost)
        }
    }
}