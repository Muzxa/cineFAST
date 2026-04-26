package com.example.cinefast;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class ActivitySnacks extends AppCompatActivity {

    Button continueButton;
    ArrayList<Snack> snacks;

    int QUANTITY_LIMIT = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_snacks);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        DatabaseManager manager = new DatabaseManager(this);
        manager.open();
        snacks = manager.getAllSnacks();

        ListView lvSnacks = findViewById(R.id.lv_snacks);

        SnackAdapter adapter = new SnackAdapter(this, snacks, QUANTITY_LIMIT);
        lvSnacks.setAdapter(adapter);

        initButtonHandler();
    }


    private void initButtonHandler()
    {
        continueButton = findViewById(R.id.button_snacks_continue);
        String showDate = getIntent().getStringExtra("date");
        Movie movie = getIntent().getParcelableExtra("movie");
        ArrayList<Seat> selectedSeats = getIntent().getParcelableArrayListExtra("seats");

        continueButton.setOnClickListener(v -> {

            snacks.removeIf(snack -> snack.getQuantity() <= 0);

            Intent intent = new Intent(ActivitySnacks.this, ActivitySummary.class);
            intent.putExtra("date", showDate);
            intent.putExtra("movie", movie);
            intent.putParcelableArrayListExtra("seats", selectedSeats);
            intent.putParcelableArrayListExtra("snacks", snacks);
            startActivity(intent);
        });
    }
}