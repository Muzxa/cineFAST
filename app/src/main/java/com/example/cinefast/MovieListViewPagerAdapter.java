package com.example.cinefast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class MovieListViewPagerAdapter extends FragmentStateAdapter {

    final private int FRAGMENT_COUNT = 2;
    private final ArrayList<Movie> nowShowingMovies;
    private final ArrayList<Movie> comingSoonMovies;

    public MovieListViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        this.nowShowingMovies = new ArrayList<>();
        this.comingSoonMovies = new ArrayList<>();
    }

    public MovieListViewPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
        this.nowShowingMovies = new ArrayList<>();
        this.comingSoonMovies = new ArrayList<>();
    }

    public MovieListViewPagerAdapter(@NonNull Fragment fragment, ArrayList<Movie> nowShowingMovies, ArrayList<Movie> comingSoonMovies) {
        super(fragment);
        this.nowShowingMovies = nowShowingMovies;
        this.comingSoonMovies = comingSoonMovies;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position)
        {
            case 1:
                return FragmentComingSoon.newInstance(comingSoonMovies);
            default:
                return FragmentNowShowing.newInstance(nowShowingMovies);
        }
    }

    @Override
    public int getItemCount() {
        return FRAGMENT_COUNT;
    }
}
