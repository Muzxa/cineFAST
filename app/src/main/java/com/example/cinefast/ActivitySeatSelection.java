package com.example.cinefast;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.content.ContextCompat;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ActivitySeatSelection extends AppCompatActivity {

    Button backButton, getSnacksButton, bookSeatsButton;
    TextView movieTitle, movieSubtitle, movieHall, movieDate, movieTime;
    private final ArrayList<Seat> selectedSeats = new ArrayList<>();
    Movie movie;
    Long showDateUTC;
    int ticketPrice = 16;
    Date today, showDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_seat_selection);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();
    }

    private void init()
    {

        backButton = findViewById(R.id.button_seat_selection_back);
        backButton.setOnClickListener(v -> finish());

        initMovieInfo();
        initSeatSelector();
        initButtonHandlers();
    }

    private void initButtonHandlers(){
        bookSeatsButton = findViewById(R.id.button_seat_selection_book_seats);
        getSnacksButton = findViewById(R.id.button_seat_selection_get_snacks);
        if(hasMovieReleased(today, movie.getReleaseDate())) {
            getSnacksButton.setOnClickListener(v -> {

                if (!selectedSeats.isEmpty()) {
                    Intent intent = new Intent(ActivitySeatSelection.this, ActivitySnacks.class);
                    intent.putExtra("date", showDate);
                    intent.putExtra("movie", movie);
                    intent.putParcelableArrayListExtra("seats", selectedSeats);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "Please select a seat before proceeding", Toast.LENGTH_LONG).show();
                }
            });

            bookSeatsButton.setOnClickListener(v -> {
                if (!selectedSeats.isEmpty()) {
                    Intent intent = new Intent(ActivitySeatSelection.this, ActivitySummary.class);
                    intent.putExtra("date", showDate);
                    intent.putExtra("movie", movie);
                    intent.putParcelableArrayListExtra("seats", selectedSeats);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "Please select a seat before proceeding", Toast.LENGTH_LONG).show();
                }
            });
        }
        else{
            getSnacksButton.setText("Watch Trailer");
            getSnacksButton.setOnClickListener(v -> {

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(movie.getTrailerURL()));
                try{
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(movie.getTrailerURL())));
                }
            });

            bookSeatsButton.setText("Coming Soon");
            bookSeatsButton.setEnabled(false);
        }
    }

    private void initMovieInfo()
    {

        movieTitle = findViewById(R.id.tv_seat_selection_heading);
        movieSubtitle =  findViewById(R.id.tv_seat_selection_subheading);
        movieDate = findViewById(R.id.tv_seat_selection_date);
        movieHall = findViewById(R.id.tv_seat_selection_hall);
        movieTime = findViewById(R.id.tv_seat_selection_time);

        showDateUTC = getIntent().getLongExtra("date", 0);
        movie = getIntent().getParcelableExtra("movie");

        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        if(movie != null && showDateUTC != null)
        {
            today = new Date();
            showDate = new Date(showDateUTC);

            movieTitle.setText(movie.getName());
            movieSubtitle.setText(movie.getGenre() + " | " + movie.getRuntime());

            if (hasMovieReleased(today, movie.getReleaseDate())) {
                movieDate.setText(sdf.format(showDate));
                movieHall.setText(String.valueOf(movie.getCinema()));
                movieTime.setText(movie.getShowTime());
            } else {
                movieDate.setText(sdf.format(movie.getReleaseDate()));
                movieHall.setText("TBD");
                movieTime.setText("TBD");
            }


        }
        else{
            Toast.makeText(this, "An Error Occurred. Please Try Again Later", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void initSeatSelector() {
        GridLayout gridSeats = findViewById(R.id.gl_seats);

        int rows = 5;
        int cols = 9;
        int mid = cols / 2;

        List<String> bookedSeats = new ArrayList<>();

        if(hasMovieReleased(today, movie.getReleaseDate()))
        {
            bookedSeats.add("0-2");
            bookedSeats.add("1-5");
            bookedSeats.add("3-0");
        }

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Button seat = new Button(this);

                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = 120;
                params.height = 120;
                params.setMargins(12, 12, 12, 12);
                seat.setLayoutParams(params);

                if(c == mid)
                {
                    seat.setBackgroundColor(Color.TRANSPARENT);
                    seat.setEnabled(false);
                    gridSeats.addView(seat);
                    continue;
                }

                seat.setBackground(ContextCompat.getDrawable(this, R.drawable.seat_button));
                GradientDrawable bg = (GradientDrawable) seat.getBackground();

                String seatKey = r + "-" + c;
                Seat currentSeat = new Seat(String.valueOf(r), String.valueOf(c), ticketPrice);

                if (bookedSeats.contains(seatKey)) {
                    bg.setColor(Color.RED);
                    seat.setEnabled(false);
                } else {
                    bg.setColor(Color.LTGRAY);
                    seat.setTag(false);

                    if(hasMovieReleased(today, movie.getReleaseDate())) {
                        seat.setOnClickListener(v -> {
                            boolean selected = (boolean) seat.getTag();
                            if (selected) {
                                bg.setColor(Color.LTGRAY);
                                selectedSeats.remove(currentSeat);
                                seat.setTag(false);
                            } else {
                                bg.setColor(Color.GREEN);
                                selectedSeats.add(currentSeat);
                                seat.setTag(true);
                            }
                        });
                    }
                    else{
                        seat.setOnClickListener(v -> {

                            Toast.makeText(this, "Movie Not Released Yet.", Toast.LENGTH_SHORT).show();
                        });
                    }
                }

                gridSeats.addView(seat);
            }
        }
    }

    private boolean hasMovieReleased(Date currentDate, Date releaseDate)
    {
        if(currentDate != null && releaseDate != null)
        {
            return currentDate.compareTo(releaseDate) >= 0;
        }

        return false;
    }
}