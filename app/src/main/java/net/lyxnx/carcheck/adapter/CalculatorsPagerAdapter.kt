package net.lyxnx.carcheck.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import net.lyxnx.carcheck.R
import net.lyxnx.carcheck.fragments.FinanceCalculatorFragment
import net.lyxnx.carcheck.fragments.FuelCostCalculatorFragment
import net.lyxnx.carcheck.fragments.JourneyCostCalculatorFragment

class CalculatorsPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {

    companion object {
        const val JOURNEY = 0
        const val MPG = 1
        const val FINANCE = 2
        private const val TAB_COUNT = 3
    }

    private val titles: Array<String> = arrayOf(
            fa.getString(R.string.title_journey),
            fa.getString(R.string.title_fuel_cost),
            fa.getString(R.string.title_finance)
    )

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            MPG -> FuelCostCalculatorFragment()
            FINANCE -> FinanceCalculatorFragment()
            else -> JourneyCostCalculatorFragment()
        }
    }

    override fun getItemCount(): Int {
        return TAB_COUNT
    }

    fun getTitle(position: Int): String {
        return titles[position]
    }
}