package net.lyxnx.carcheck.adapter;

import net.lyxnx.carcheck.fragments.FinanceCalculatorFragment;
import net.lyxnx.carcheck.fragments.FuelCostCalculatorFragment;
import net.lyxnx.carcheck.fragments.JourneyCostCalculatorFragment;
import net.lyxnx.carcheck.R;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class CalculatorsPagerAdapter extends FragmentStateAdapter {

    public static final int JOURNEY = 0;
    public static final int MPG = 1;
    public static final int FINANCE = 2;

    private static final int TAB_COUNT = 3;

    private final String[] titles;

    public CalculatorsPagerAdapter(@NonNull FragmentActivity fa) {
        super(fa);

        this.titles = new String[]{
                fa.getString(R.string.title_journey),
                fa.getString(R.string.title_fuel_cost),
                fa.getString(R.string.title_finance)
        };
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case MPG:
                return new FuelCostCalculatorFragment();
            case FINANCE:
                return new FinanceCalculatorFragment();
            default:
                return new JourneyCostCalculatorFragment();
        }
    }

    @Override
    public int getItemCount() {
        return TAB_COUNT;
    }

    public String getTitle(int position) {
        return titles[position];
    }
}