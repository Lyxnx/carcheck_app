package net.lyxnx.carcheck.adapter;

import net.lyxnx.carcheck.FinanceCalculatorFragment;
import net.lyxnx.carcheck.FuelCostCalculatorFragment;
import net.lyxnx.carcheck.JourneyCostCalculatorFragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class CalculatorsPagerAdapter extends FragmentStateAdapter {

    public static final int JOURNEY = 0;
    public static final int MPG = 1;
    public static final int FINANCE = 2;

    private static final int TAB_COUNT = 3;

    private final String[] titles = new String[]{"Journey", "Fuel Cost", "Finance"};

    public CalculatorsPagerAdapter(@NonNull FragmentActivity fa) {
        super(fa);
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