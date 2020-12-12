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

class FuelCostCalculatorFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_fuel_cost_calc, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val milesItem = view.findViewById<View>(R.id.milesItem)
        val distance = milesItem.findViewById<EditText>(R.id.calcEditText)
        val milesDesc = milesItem.findViewById<TextView>(R.id.calcDescription)

        setupItem(distance, getString(R.string.distance), milesDesc, getString(R.string.miles))

        val mpgItem = view.findViewById<View>(R.id.mpgItem)
        val mpg = mpgItem.findViewById<EditText>(R.id.calcEditText)
        val mpgDesc = mpgItem.findViewById<TextView>(R.id.calcDescription)

        setupItem(mpg, getString(R.string.mpg), mpgDesc, getString(R.string.mpg))

        val requiredFuel = view.findViewById<TextView>(R.id.requiredFuel)
        requiredFuel.text = getString(R.string.fuel_text, 0.0)

        val rfw = RequiredFuelWatcher(distance, mpg, requiredFuel)

        distance.addTextChangedListener(rfw)
        mpg.addTextChangedListener(rfw)
    }

    private fun setupItem(editText: EditText, hint: String, desc: TextView, description: String) {
        editText.hint = hint
        desc.text = description
    }

    inner class RequiredFuelWatcher internal constructor(private val distance: EditText, private val mpg: EditText, private val requiredFuel: TextView) : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun afterTextChanged(s: Editable) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            val distanceNum = distance.text.toString().toDoubleOrNull()
            val mpgNum = mpg.text.toString().toDoubleOrNull()

            if (distanceNum == null || mpgNum == null) {
                return
            }

            requiredFuel.text = getString(R.string.fuel_text, (distanceNum / mpgNum * 3.785).round(2))
        }
    }
}