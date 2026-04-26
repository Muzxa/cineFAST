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

import java.util.ArrayList;

public class FragmentComingSoon extends Fragment {
    private static final String ARG_MOVIES = "arg_movies";

    RecyclerView comingSoonRecyclerView;
    ArrayList<Movie> movies;

    public static FragmentComingSoon newInstance(ArrayList<Movie> movies) {
        FragmentComingSoon fragment = new FragmentComingSoon();
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
        View view = inflater.inflate(R.layout.fragment_coming_soon, container, false);

        comingSoonRecyclerView = view.findViewById(R.id.coming_soon_recycler_view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        comingSoonRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        comingSoonRecyclerView.setAdapter(new MovieListAdapter(requireContext(), movies));
    }
}