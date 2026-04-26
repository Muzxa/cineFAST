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
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class FragmentNowShowing extends Fragment {
    private static final String ARG_MOVIES = "arg_movies";

    RecyclerView nowShowingRecyclerView;
    ArrayList<Movie> movies;
    RadioGroup datePickerGroup;
    LocalDate showDate;

    public static FragmentNowShowing newInstance(ArrayList<Movie> movies) {
        FragmentNowShowing fragment = new FragmentNowShowing();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_MOVIES, movies);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        movies = new ArrayList<>();

        if (getArguments() != null) {
            ArrayList<Movie> passedMovies = getArguments().getParcelableArrayList(ARG_MOVIES);
            if (passedMovies != null) {
                movies.addAll(passedMovies);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_now_showing, container, false);

        nowShowingRecyclerView = view.findViewById(R.id.now_showing_recycler_view);

        showDate = LocalDate.now();
        datePickerGroup = view.findViewById(R.id.rg_date_picker);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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