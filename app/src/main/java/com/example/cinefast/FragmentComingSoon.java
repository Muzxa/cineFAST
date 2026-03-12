package com.example.cinefast;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FragmentComingSoon extends Fragment {
    RecyclerView comingSoonRecyclerView;
    ArrayList<Movie> movies;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_coming_soon, container, false);

        comingSoonRecyclerView = view.findViewById(R.id.coming_soon_recycler_view);
        movies = new ArrayList<>();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        try {
            movies.add(new Movie("The Dark Knight", "152 mins", "Action", 1, "20:15", R.drawable.the_dark_knight, "https://www.youtube.com/watch?v=EXeTwQWrcwY", format.parse("2026-03-01")));
            movies.add(new Movie("12 Angry Men", "96 mins", "Crime", 12, "15:30", R.drawable.twelve_angry_men, "https://www.youtube.com/watch?v=TEN-2uTi2c0", format.parse("2026-03-02")));
            movies.add(new Movie("Saving Private Ryan", "169 mins", "War", 5, "13:00", R.drawable.saving_private_ryan, "https://www.youtube.com/watch?v=9CiW_DgxCnQ", format.parse("2026-03-03")));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        comingSoonRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        comingSoonRecyclerView.setAdapter(new MovieListAdapter(requireContext(), movies));
    }
}