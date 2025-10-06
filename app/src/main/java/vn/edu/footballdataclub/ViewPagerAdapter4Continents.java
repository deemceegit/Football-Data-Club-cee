package vn.edu.footballdataclub;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerAdapter4Continents extends FragmentStateAdapter {

    public ViewPagerAdapter4Continents(@NonNull Fragment fragment) {
        super(fragment);
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new AsiaFragment();
            case 1:
                return new AfricaFragment();
            case 2:
                return new north_america();
            case 3:
                return new SudAmericaFragment();
            case 4:
                return new EuropeTitleAndRanking();
            default:
                return new SudAmericaFragment();
        }
    }
}
