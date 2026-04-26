package com.example.cinefast;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class FragmentMovieList extends Fragment {

    private TabLayout movieListTabLayout;
    private TabItem movieListTabLayoutNowShowing;
    private TabItem movieListTabLayoutComingSoon;
    private ViewPager2 movieListViewPager;
    private MovieListViewPagerAdapter movieListViewPagerAdapter;
    private Toolbar toolbarMovieList;

    public FragmentMovieList() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movie_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        attachTabSelectedListener();
    }

    private void init(View rootView) {
        movieListTabLayout = rootView.findViewById(R.id.movie_list_tab_layout);
        movieListTabLayoutNowShowing = rootView.findViewById(R.id.movie_list_tab_layout_now_showing);
        movieListTabLayoutComingSoon = rootView.findViewById(R.id.movie_list_tab_layout_coming_soon);
        movieListViewPager = rootView.findViewById(R.id.movie_list_view_pager);

        movieListViewPagerAdapter = new MovieListViewPagerAdapter(this);
        movieListViewPager.setAdapter(movieListViewPagerAdapter);
    }

    private void attachTabSelectedListener() {
        new TabLayoutMediator(movieListTabLayout, movieListViewPager, (tab, position) -> {
            if (position == 0) {
                tab.setText("Now Showing");
                BadgeDrawable badge = tab.getOrCreateBadge();
                badge.setNumber(3);
                badge.setMaxCharacterCount(3);
            } else if (position == 1) {
                tab.setText("Coming Soon");
                BadgeDrawable badge = tab.getOrCreateBadge();
                badge.setNumber(30000);
                badge.setMaxCharacterCount(3);
            }
        }).attach();
    }
}