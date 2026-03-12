package com.example.cinefast;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class ActivityMovieList extends AppCompatActivity {

    TabLayout movieListTabLayout;
    TabItem movieListTabLayoutNowShowing, getMovieListTabLayoutComingSoon;
    ViewPager2 movieListViewPager;
    MovieListViewPagerAdapter movieListViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_movie_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();
        attachTabSelectedListener();
    }

    public void init()
    {
        movieListTabLayout = findViewById(R.id.movie_list_tab_layout);
        movieListTabLayoutNowShowing = findViewById(R.id.movie_list_tab_layout_now_showing);
        getMovieListTabLayoutComingSoon = findViewById(R.id.movie_list_tab_layout_coming_soon);

        movieListViewPager = findViewById(R.id.movie_list_view_pager);

        movieListViewPagerAdapter = new MovieListViewPagerAdapter(this);
        movieListViewPager.setAdapter(movieListViewPagerAdapter);
    }

    public void attachTabSelectedListener()
    {
        movieListViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                movieListTabLayout.selectTab(movieListTabLayout.getTabAt(position));
            }
        });

        movieListTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                movieListViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }
}