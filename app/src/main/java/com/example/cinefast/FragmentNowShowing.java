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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class FragmentNowShowing extends Fragment {
    RecyclerView nowShowingRecyclerView;
    ArrayList<Movie> movies;
    RadioGroup datePickerGroup;
    LocalDate showDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_now_showing, container, false);

        nowShowingRecyclerView = view.findViewById(R.id.now_showing_recycler_view);
        movies = new ArrayList<>();

        showDate = LocalDate.now();
        datePickerGroup = view.findViewById(R.id.rg_date_picker);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        try {
            movies.add(new Movie("Oppenheimer", "180 mins", "Thriller", 1, "22:45", R.drawable.oppenheimer, "https://www.youtube.com/watch?v=uYPbbksJxIg", format.parse("2026-02-1")));
            movies.add(new Movie("Whiplash", "107 mins", "Drama", 4, "12:30", R.drawable.whiplash, "https://www.youtube.com/watch?v=Df1xkYYbYrY", format.parse("2026-02-2")));
            movies.add(new Movie("The Silence of The Lambs", "118 mins", "Crime", 12, "17:00", R.drawable.silence_of_the_lambs, "https://www.youtube.com/watch?v=6iB21hsprAQ", format.parse("2026-02-3")));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        nowShowingRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        MovieListAdapter adapter = new MovieListAdapter(requireContext(), movies, showDate.atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli()
        );
        nowShowingRecyclerView.setAdapter(adapter);

        initDateRadioButtons();
    }

    public void initDateRadioButtons()
    {
        datePickerGroup.setOnCheckedChangeListener((g, checkedId) -> {
            if(checkedId == R.id.rb_movie_list_1)
            {
                showDate = showDate.minusDays(1);
            }
            else if(checkedId == R.id.rb_movie_list_2)
            {
                showDate = showDate.plusDays(1);
            }
            String formattedDate = showDate.format(DateTimeFormatter.ofPattern("dd MMM yyyy"));
            String toastText = "Showing Movies On " + formattedDate;
            Toast.makeText(requireContext(), toastText, Toast.LENGTH_SHORT).show();
        });
    }
}