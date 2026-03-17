package com.example.cinefast;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class FragmentSnacks extends Fragment {

    Button continueButton;
    ArrayList<Snack> snacks;
    ListView lvSnacks;

    int QUANTITY_LIMIT = 10;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_snacks, container, false);

        snacks = new ArrayList<>();
        snacks.add(new Snack("Hot Dogs", "Large | With Ketchup and Mustard", 7.49, 0, R.drawable.hot_dogs));
        snacks.add(new Snack("Popcorn", "Large | Buttered", 9.99, 0, R.drawable.popcorn));
        snacks.add(new Snack("Nachos", "Large | With Cheese and Salsa", 15.99, 0, R.drawable.nachos));
        snacks.add(new Snack("French Fries", "Large | Salted", 5.49, 0, R.drawable.fries));

        lvSnacks = view.findViewById(R.id.lv_snacks);
        continueButton = view.findViewById(R.id.button_snacks_continue);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SnackAdapter adapter = new SnackAdapter(requireContext(), snacks, QUANTITY_LIMIT);
        lvSnacks.setAdapter(adapter);

        initButtonHandler();
    }

    private void initButtonHandler()
    {

        String showDate = requireActivity().getIntent().getStringExtra("date");
        Movie movie = requireActivity().getIntent().getParcelableExtra("movie");
        ArrayList<Seat> selectedSeats = requireActivity().getIntent().getParcelableArrayListExtra("seats");

        continueButton.setOnClickListener(v -> {

            snacks.removeIf(snack -> snack.getQuantity() <= 0);

            Intent intent = new Intent(requireContext(), ActivitySummary.class);
            intent.putExtra("date", showDate);
            intent.putExtra("movie", movie);
            intent.putParcelableArrayListExtra("seats", selectedSeats);
            intent.putParcelableArrayListExtra("snacks", snacks);
            startActivity(intent);
        });
    }
}