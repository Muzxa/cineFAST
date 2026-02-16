package com.example.cinefast;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class ActivitySnacks extends AppCompatActivity {

    Button addPopcorn, removePopcorn;
    Button addNachos, removeNachos;
    Button addFries, removeFries;
    Button addHotDogs, removeHotDogs;
    EditText popcornQuantity, nachosQuantity, friesQuantity, hotDogsQuantity;
    TextView popcornPrice, nachosPrice, friesPrice, hotDogsPrice;
    TextView popcornTitle, nachosTitle, friesTitle, hotDogsTitle;
    TextView popcornDescription, nachosDescription, friesDescription,  hotDogsDescription;
    Snack popcorn, nachos, fries, hotDogs;
    Button continueButton;

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

        initPopcorn();
        initNachos();
        initFries();
        initHotDogs();
        initButtonHandler();
    }

    private void initPopcorn()
    {
        popcorn = new Snack("Popcorn", "Large | Buttered", 9.99, 0);

        popcornTitle = findViewById(R.id.tv_popcorn_title);
        popcornDescription = findViewById(R.id.tv_popcorn_description);
        popcornPrice = findViewById(R.id.tv_popcorn_price);
        popcornQuantity = findViewById(R.id.et_popcorn_quantity);

        addPopcorn = findViewById(R.id.add_popcorn);
        removePopcorn = findViewById(R.id.remove_popcorn);

        initSnack(popcorn, popcornTitle, popcornDescription, popcornPrice, popcornQuantity, addPopcorn, removePopcorn);
    }

    private void initNachos()
    {
        nachos = new Snack("Nachos", "Large | With Cheese and Salsa", 15.99, 0);

        nachosTitle = findViewById(R.id.tv_nachos_title);
        nachosDescription = findViewById(R.id.tv_nachos_description);
        nachosPrice = findViewById(R.id.tv_nachos_price);
        nachosQuantity = findViewById(R.id.et_nachos_quantity);

        addNachos = findViewById(R.id.add_nachos);
        removeNachos = findViewById(R.id.remove_nachos);

        initSnack(nachos, nachosTitle, nachosDescription, nachosPrice, nachosQuantity, addNachos, removeNachos);
    }

    private void initFries()
    {
        fries = new Snack("French Fries", "Large | Salted", 5.49, 0);

        friesTitle = findViewById(R.id.tv_fries_title);
        friesDescription = findViewById(R.id.tv_fries_description);
        friesPrice = findViewById(R.id.tv_fries_price);
        friesQuantity = findViewById(R.id.et_fries_quantity);

        addFries = findViewById(R.id.add_fries);
        removeFries = findViewById(R.id.remove_fries);

        initSnack(fries, friesTitle, friesDescription, friesPrice, friesQuantity, addFries, removeFries);
    }

    private void initHotDogs()
    {
        hotDogs = new Snack("Hot Dogs", "Large | With Ketchup and Mustard", 7.49, 0);

        hotDogsTitle = findViewById(R.id.tv_hot_dogs_title);
        hotDogsDescription = findViewById(R.id.tv_hot_dogs_description);
        hotDogsPrice = findViewById(R.id.tv_hot_dogs_price);
        hotDogsQuantity = findViewById(R.id.et_hot_dogs_quantity);

        addHotDogs = findViewById(R.id.add_hot_dogs);
        removeHotDogs = findViewById(R.id.remove_hot_dogs);

        initSnack(hotDogs, hotDogsTitle, hotDogsDescription, hotDogsPrice, hotDogsQuantity, addHotDogs, removeHotDogs);

    }



    private void initSnack(Snack snack, TextView title, TextView description, TextView price, EditText quantity, Button addButton, Button removeButton)
    {
        title.setText(snack.getName());
        description.setText(snack.getDescription());
        price.setText("$ " + String.valueOf(snack.getPrice()));
        quantity.setText(String.valueOf(snack.getQuantity()));

        addButton.setOnClickListener(v -> {

            int currentQuantity = snack.getQuantity();

            if(currentQuantity < QUANTITY_LIMIT)
            {
                currentQuantity += 1;
                snack.setQuantity(currentQuantity);
                quantity.setText(String.valueOf(currentQuantity));
            }
        });

        removeButton.setOnClickListener(v -> {

            int currentQuantity = snack.getQuantity();

            if(currentQuantity > 0)
            {
                currentQuantity -= 1;
                snack.setQuantity(currentQuantity);
                quantity.setText(String.valueOf(snack.getQuantity()));
            }
        });

        quantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().isEmpty()) {

                    int value = Integer.parseInt(s.toString());

                    if (value > QUANTITY_LIMIT) {
                        value = QUANTITY_LIMIT;
                        quantity.setText(String.valueOf(value));
                        quantity.setSelection(String.valueOf(value).length());
                    }

                    snack.setQuantity(value);
                }
            }

            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
        });

    }

    private void initButtonHandler()
    {
        continueButton = findViewById(R.id.button_snacks_continue);
        String showDate = getIntent().getStringExtra("date");
        Movie movie = getIntent().getParcelableExtra("movie");
        ArrayList<Seat> selectedSeats = getIntent().getParcelableArrayListExtra("seats");

        continueButton.setOnClickListener(v -> {
            ArrayList<Snack> purchasedSnacks = new ArrayList<>();

            if(popcorn != null && popcorn.getQuantity() > 0)
            {
                purchasedSnacks.add(popcorn);
            }
            if(nachos != null && nachos.getQuantity() > 0)
            {
                purchasedSnacks.add(nachos);
            }
            if(fries != null && fries.getQuantity() > 0)
            {
                purchasedSnacks.add(fries);
            }
            if(hotDogs != null && hotDogs.getQuantity() > 0)
            {
                purchasedSnacks.add(hotDogs);  // Fixed: was popcorn
            }

            Intent intent = new Intent(ActivitySnacks.this, ActivitySummary.class);
            intent.putExtra("date", showDate);
            intent.putExtra("movie", movie);
            intent.putParcelableArrayListExtra("seats", selectedSeats);
            intent.putParcelableArrayListExtra("snacks", purchasedSnacks);
            startActivity(intent);
        });
    }
}