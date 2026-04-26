package com.example.cinefast;

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

import java.util.ArrayList;

public class FragmentMovieList extends Fragment {

    private static final String ARG_NOW_SHOWING = "arg_now_showing";
    private static final String ARG_COMING_SOON = "arg_coming_soon";

    private TabLayout movieListTabLayout;
    private TabItem movieListTabLayoutNowShowing;
    private TabItem movieListTabLayoutComingSoon;
    private ViewPager2 movieListViewPager;
    private MovieListViewPagerAdapter movieListViewPagerAdapter;
    private Toolbar toolbarMovieList;
    private ArrayList<Movie> nowShowing;
    private ArrayList<Movie> comingSoon;

    public FragmentMovieList() {
    }

    public static FragmentMovieList newInstance(ArrayList<Movie> nowShowing, ArrayList<Movie> comingSoon) {
        FragmentMovieList fragmentMovieList = new FragmentMovieList();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_NOW_SHOWING, nowShowing);
        args.putParcelableArrayList(ARG_COMING_SOON, comingSoon);
        fragmentMovieList.setArguments(args);
        return fragmentMovieList;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        nowShowing = new ArrayList<>();
        comingSoon = new ArrayList<>();

        if (getArguments() != null) {
            ArrayList<Movie> passedNowShowing = getArguments().getParcelableArrayList(ARG_NOW_SHOWING);
            ArrayList<Movie> passedComingSoon = getArguments().getParcelableArrayList(ARG_COMING_SOON);

            if (passedNowShowing != null) {
                nowShowing.addAll(passedNowShowing);
            }

            if (passedComingSoon != null) {
                comingSoon.addAll(passedComingSoon);
            }
        }
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

        movieListViewPagerAdapter = new MovieListViewPagerAdapter(this, nowShowing, comingSoon);
        movieListViewPager.setAdapter(movieListViewPagerAdapter);
    }

    private void attachTabSelectedListener() {
        new TabLayoutMediator(movieListTabLayout, movieListViewPager, (tab, position) -> {
            if (position == 0) {
                tab.setText("Now Showing");
                BadgeDrawable badge = tab.getOrCreateBadge();
                badge.setNumber(nowShowing.size());
                badge.setMaxCharacterCount(3);
            } else if (position == 1) {
                tab.setText("Coming Soon");
                BadgeDrawable badge = tab.getOrCreateBadge();
                badge.setNumber(comingSoon.size());
                badge.setMaxCharacterCount(3);
            }
        }).attach();
    }
}