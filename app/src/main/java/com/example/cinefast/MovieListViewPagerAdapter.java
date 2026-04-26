package com.example.cinefast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class MovieListViewPagerAdapter extends FragmentStateAdapter {

    final private int FRAGMENT_COUNT = 2;

    public MovieListViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public MovieListViewPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position)
        {
            case 1:
                return new FragmentComingSoon();
            default:
                return new FragmentNowShowing();
        }
    }

    @Override
    public int getItemCount() {
        return FRAGMENT_COUNT;
    }
}
