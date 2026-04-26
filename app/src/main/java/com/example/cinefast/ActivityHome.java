package com.example.cinefast;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ActivityHome extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout dlMain;
    Toolbar toolbar;
    NavigationView navigationDrawer;
    private ArrayList<Movie> nowShowingMovies;
    private ArrayList<Movie> comingSoonMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();

        String email = fetchUserEmail();
        if(email == null) {logOut();}

        loadMoviesFromJson();

        initDrawerLayout();
        initNavHeaderView(email);

        if (savedInstanceState == null) {
            loadFragment(FragmentMovieList.newInstance(nowShowingMovies, comingSoonMovies), "Home");
            navigationDrawer.setCheckedItem(R.id.action_home);
        }

        navigationDrawer.setNavigationItemSelectedListener(this);
    }

    private void init() {
        dlMain = findViewById(R.id.main);
        toolbar = findViewById(R.id.toolbar);
        navigationDrawer = findViewById(R.id.navigation_drawer);
        nowShowingMovies = new ArrayList<>();
        comingSoonMovies = new ArrayList<>();
    }

    private String fetchUserEmail() {
        SharedPreferences preferences = getSharedPreferences(getString(R.string.shared_preferences_name), MODE_PRIVATE);
        return preferences.getString("email", null);
    }

    private void logOut() {
        SharedPreferences preferences = getSharedPreferences(getString(R.string.shared_preferences_name), MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.remove("email");
        editor.remove("remember_user");
        editor.apply();
        FirebaseAuth.getInstance().signOut();

        finish();
    }

    private void initNavHeaderView(String email) {

        View header = navigationDrawer.getHeaderView(0);

        TextView textViewUserEmail = header.findViewById(R.id.text_view_user_email);
        textViewUserEmail.setText(email);
    }

    private void initDrawerLayout() {

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                dlMain,
                toolbar, /* THE TOOLBAR TO ATTACH THE DRAWER TO */
                R.string.navigation_drawer_open, /* ACCESSIBILITY STRING */
                R.string.navigation_drawer_close /* ACCESSIBILITY STRING */
        );

        dlMain.addDrawerListener(toggle); /* LISTENING FOR DRAWER TOGGLE */
        toggle.syncState(); /* SYNCING HAMBURGER MENU TO DRAWER */
    }

    private void loadMoviesFromJson() {
        nowShowingMovies.clear();
        comingSoonMovies.clear();

        try {
            InputStream inputStream = getResources().openRawResource(R.raw.movies);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            StringBuilder builder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }

            reader.close();

            JSONObject root = new JSONObject(builder.toString());
            JSONArray nowShowingArray = root.getJSONArray("now_showing");
            JSONArray comingSoonArray = root.getJSONArray("coming_soon");

            for (int i = 0; i < nowShowingArray.length(); i++) {
                nowShowingMovies.add(parseMovie(nowShowingArray.getJSONObject(i)));
            }

            for (int i = 0; i < comingSoonArray.length(); i++) {
                comingSoonMovies.add(parseMovie(comingSoonArray.getJSONObject(i)));
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse movies.json", e);
        }
    }

    private Movie parseMovie(JSONObject movieObject) throws Exception {
        String name = movieObject.getString("title");
        String runtime = movieObject.getString("duration");
        String genre = movieObject.getString("genre");
        int cinema = movieObject.getInt("rating");
        String showTime = movieObject.getString("show_time");
        String trailerURL = movieObject.getString("trailer_url");
        String imageName = movieObject.getString("image_res");
        String releaseDateValue = movieObject.getString("release_date");

        int posterId = getResources().getIdentifier(imageName, "drawable", getPackageName());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        return new Movie(
                name,
                runtime,
                genre,
                cinema,
                showTime,
                posterId,
                trailerURL,
                dateFormat.parse(releaseDateValue)
        );
    }

    public void loadFragment(Fragment fragment, String title) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        Fragment selectedFragment = null;
        String selectedTitle = null;

        if (itemId == R.id.action_home) {
            selectedFragment = FragmentMovieList.newInstance(nowShowingMovies, comingSoonMovies);
            selectedTitle = "Home";
        }
        else if (itemId == R.id.action_my_bookings) {
            selectedFragment = new FragmentMyBookings();
            selectedTitle = "My Bookings";
        }
        else if (itemId == R.id.action_log_out) {
           logOut();
        }

        if (selectedFragment != null && selectedTitle != null) {
            loadFragment(selectedFragment, selectedTitle);
            navigationDrawer.setCheckedItem(menuItem);
        }

        dlMain.closeDrawer(GravityCompat.START);
        return false;
    }
}