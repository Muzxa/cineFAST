package com.example.cinefast;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class ActivitySummary extends AppCompatActivity {

    Button sendTickets, back;
    ImageView poster;
    TextView title, subtitle, hall, date, time, total;
    LinearLayout ticketLayout, snackLayout;
    String showDate;
    Movie movie;
    ArrayList<Snack> purchasedSnacks;
    ArrayList<Seat> selectedSeats;
    double runningTotal = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_summary);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();
        fillSummaryLayout();
        fillTicketsLayout();
        fillSnacksLayout();

        setBackButtonHandler();
        setSendTicketsButtonHandler();

        saveToSharedPreferences();
        total.setText("$ " + String.format("%.2f", runningTotal));
        Toast.makeText(this, "Booking Confirmed!", Toast.LENGTH_LONG).show();
    }

    private void saveToSharedPreferences() {
        SharedPreferences preferences = getSharedPreferences("cineFAST", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString("movieName", movie.getName());
        editor.putInt("numberOfSeats", selectedSeats.size());
        editor.putFloat("totalPrice", (float) runningTotal);
    }

    private void setBackButtonHandler()
    {
        back.setOnClickListener(v -> {
            finish();
        });
    }

    private void setSendTicketsButtonHandler()
    {
        sendTickets.setOnClickListener(v -> {
            String ticketInfo = buildTicketInfo();

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "CineFast Movie Ticket");
            shareIntent.putExtra(Intent.EXTRA_TEXT, ticketInfo);

            // Create a chooser to show WhatsApp, Gmail, and other sharing apps
            Intent chooser = Intent.createChooser(shareIntent, "Send Ticket via");

            if (shareIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(chooser);
            } else {
                Toast.makeText(this, "No apps available to share", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String buildTicketInfo() {
        StringBuilder ticket = new StringBuilder();
        ticket.append("CINEFAST TICKET\n\n");
        ticket.append("Movie: ").append(movie.getName()).append("\n");
        ticket.append("Genre: ").append(movie.getGenre()).append("\n");
        ticket.append("Date: ").append(showDate).append("\n");
        ticket.append("Time: ").append(movie.getShowTime()).append("\n\n");

        ticket.append("SEATS:\n");
        for(Seat seat : selectedSeats) {
            ticket.append("  Row ").append(seat.getRow())
                    .append(", Seat ").append(seat.getColumn())
                    .append(" - $").append(seat.getPrice()).append("\n");
        }

        if(purchasedSnacks != null && !purchasedSnacks.isEmpty()) {
            ticket.append("\nSNACKS:\n");
            for(Snack snack : purchasedSnacks) {
                ticket.append("  ").append(snack.getQuantity())
                        .append("x ").append(snack.getName())
                        .append(" - $").append(String.format("%.2f", snack.getPrice() * snack.getQuantity()))
                        .append("\n");
            }
        }

        ticket.append("\nTOTAL: $").append(String.format("%.2f", runningTotal));
        ticket.append("\n\nEnjoy your movie! 🍿");

        return ticket.toString();
    }

    private void init()
    {

        sendTickets = findViewById(R.id.button_summary_send_tickets);
        back = findViewById(R.id.button_summary_back);

        poster = findViewById(R.id.iv_summary_poster);
        title = findViewById(R.id.tv_summary_title);
        subtitle = findViewById(R.id.tv_summary_subtitle);
        hall = findViewById(R.id.tv_summary_hall);
        date = findViewById(R.id.tv_summary_date);
        time = findViewById(R.id.tv_summary_time);
        total = findViewById(R.id.tv_summary_total);

        ticketLayout = findViewById(R.id.layout_summary_tickets);
        snackLayout = findViewById(R.id.layout_summary_snacks);

        showDate = getIntent().getStringExtra("date");
        movie = getIntent().getParcelableExtra("movie");
        selectedSeats = getIntent().getParcelableArrayListExtra("seats");
        purchasedSnacks = getIntent().getParcelableArrayListExtra("snacks");

        if(movie == null || selectedSeats == null || showDate == null)
        {
            Toast.makeText(this, "An Error Occurred. Please try again", Toast.LENGTH_LONG).show();
            finish();
        }


    }

    private void fillSummaryLayout()
    {
        poster.setImageResource(movie.getPosterId());
        title.setText(movie.getName());
        subtitle.setText(movie.getGenre() + " | " + movie.getRuntime());
        date.setText(showDate);
        time.setText(movie.getShowTime());
    }

    private void fillTicketsLayout()
    {
        for(Seat seat : selectedSeats)
        {
            String description = "Row " + seat.getRow() + ", Seat " + seat.getColumn();
            runningTotal += seat.getPrice();
            String price = String.valueOf(seat.getPrice());

            ConstraintLayout row = new ConstraintLayout(this);
            row.setPadding(12, 12, 24, 12);

            TextView descriptionView = new TextView(this);
            descriptionView.setId(View.generateViewId());
            descriptionView.setText(description);
            descriptionView.setTextColor(ContextCompat.getColor(this, R.color.silver));
            descriptionView.setTextSize(20);

            int leftPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12, getResources().getDisplayMetrics());
            int topPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics());
            descriptionView.setPadding(leftPadding, topPadding, leftPadding, 0);

            TextView priceView = new TextView(this);
            priceView.setId(View.generateViewId());
            priceView.setText("$ " + price);
            priceView.setTextColor(Color.WHITE);
            priceView.setTextSize(20);
            priceView.setPadding(0, 0, 8, 0);

            ConstraintLayout.LayoutParams descParams = new ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.WRAP_CONTENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
            );

            ConstraintLayout.LayoutParams priceParams = new ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.WRAP_CONTENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
            );

            descParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID; // left
            descParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;

            priceParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID; // right
            priceParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;

            descriptionView.setLayoutParams(descParams);
            priceView.setLayoutParams(priceParams);

            row.addView(descriptionView);
            row.addView(priceView);

            ticketLayout.addView(row);
        }
    }

    private void fillSnacksLayout()
    {
        if(purchasedSnacks != null)
        {
            for(Snack snack: purchasedSnacks)
            {
                String description = snack.getQuantity() + "X " + snack.getName();
                double itemTotal = snack.getPrice() * snack.getQuantity();

                runningTotal += itemTotal;

                String price = String.valueOf(itemTotal);

                ConstraintLayout row = new ConstraintLayout(this);
                row.setPadding(12, 12, 24, 12);

                TextView descriptionView = new TextView(this);
                descriptionView.setId(View.generateViewId());
                descriptionView.setText(description);
                descriptionView.setTextColor(ContextCompat.getColor(this, R.color.silver));
                descriptionView.setTextSize(20);

                int leftPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12, getResources().getDisplayMetrics());
                int topPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics());
                descriptionView.setPadding(leftPadding, topPadding, leftPadding, 0);

                TextView priceView = new TextView(this);
                priceView.setId(View.generateViewId());
                priceView.setText("$ " + price);
                priceView.setTextColor(Color.WHITE);
                priceView.setTextSize(20);
                priceView.setPadding(0, 0, 8, 0);

                ConstraintLayout.LayoutParams descParams = new ConstraintLayout.LayoutParams(
                        ConstraintLayout.LayoutParams.WRAP_CONTENT,
                        ConstraintLayout.LayoutParams.WRAP_CONTENT
                );

                ConstraintLayout.LayoutParams priceParams = new ConstraintLayout.LayoutParams(
                        ConstraintLayout.LayoutParams.WRAP_CONTENT,
                        ConstraintLayout.LayoutParams.WRAP_CONTENT
                );

                descParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
                descParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;

                priceParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
                priceParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;

                descriptionView.setLayoutParams(descParams);
                priceView.setLayoutParams(priceParams);

                row.addView(descriptionView);
                row.addView(priceView);

                snackLayout.addView(row);
            }

        }
    }

}

