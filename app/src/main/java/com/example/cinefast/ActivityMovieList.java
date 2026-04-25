package com.example.cinefast;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class ActivityMovieList extends AppCompatActivity {

    TabLayout movieListTabLayout;
    TabItem movieListTabLayoutNowShowing, getMovieListTabLayoutComingSoon;
    ViewPager2 movieListViewPager;
    MovieListViewPagerAdapter movieListViewPagerAdapter;
    Toolbar toolbarMovieList;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activtity_movie_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            SharedPreferences preferences = getSharedPreferences("cineFAST", MODE_PRIVATE);
            String movieName = preferences.getString("movieName", null);
            float totalPrice = preferences.getFloat("totalPrice", 0.0F);
            int numberOfSeats = preferences.getInt("numberOfSeats", 0);

            StringBuilder bookingInfo = new StringBuilder();


            if(movieName != null)
            {
                bookingInfo.append("Movie Name: ");
                bookingInfo.append(movieName);
                bookingInfo.append("\n");
                bookingInfo.append("Number of Seats: ");
                bookingInfo.append(numberOfSeats);
                bookingInfo.append("\n");
                bookingInfo.append("Total Price: ");
                bookingInfo.append(totalPrice);
            }
            else{
                bookingInfo.append("No Booking Found.");
            }

            new AlertDialog.Builder(this)
                    .setTitle("Last Booking")
                    .setMessage(bookingInfo)
                    .setPositiveButton("OK", null)
                    .show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void init()
    {
        toolbarMovieList = findViewById(R.id.toolbar_movie_list);
        setSupportActionBar(toolbarMovieList);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        movieListTabLayout = findViewById(R.id.movie_list_tab_layout);
        movieListTabLayoutNowShowing = findViewById(R.id.movie_list_tab_layout_now_showing);
        getMovieListTabLayoutComingSoon = findViewById(R.id.movie_list_tab_layout_coming_soon);

        movieListViewPager = findViewById(R.id.movie_list_view_pager);

        movieListViewPagerAdapter = new MovieListViewPagerAdapter(this);
        movieListViewPager.setAdapter(movieListViewPagerAdapter);
    }

    public void attachTabSelectedListener()
    {

        new TabLayoutMediator(movieListTabLayout, movieListViewPager, (tab, position) -> {

            if(position == 0)
            {
                tab.setText("Now Showing");

                BadgeDrawable badge = tab.getOrCreateBadge();
                badge.setNumber(3);
                badge.setMaxCharacterCount(3);
            }
            else if(position == 1)
            {
                tab.setText("Coming Soon");

                BadgeDrawable badge = tab.getOrCreateBadge();
                badge.setNumber(30000);
                badge.setMaxCharacterCount(3);
            }
        }).attach();
    }
}