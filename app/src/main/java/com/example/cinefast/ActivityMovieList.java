package com.example.cinefast;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ActivityMovieList extends AppCompatActivity {
    Button oppenheimerTrailer, oppenheimerBookNow;
    Button twelveAngryMenTrailer, twelveAngryMenBookNow;
    Button theDarkKnightTrailer, theDarkKnightBookNow;
    Button silenceOfTheLambsTrailer, silenceOfTheLambsBookNow;
    RadioGroup datePickerGroup;
    RadioButton radioButtonToday, radioButtonTomorrow;
    LocalDate showDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_movie_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initOppenheimer();
        initTwelveAngryMen();
        initTheDarkKnight();
        initSilenceOfTheLambs();

        initDateRadioButtons();
    }

    private void initOppenheimer() {
       oppenheimerBookNow = findViewById(R.id.button_oppenheimer_book_seats);
       oppenheimerTrailer  = findViewById(R.id.button_oppenheimer_trailer);
       String oppenheimerTrailerURL = "https://www.youtube.com/watch?v=bK6ldnjE3Y0&pp=ygUTb3BwZW5oZWltZXIgdHJhaWxlcg%3D%3D";

       initMovie(oppenheimerBookNow, oppenheimerTrailer, oppenheimerTrailerURL);
    }

    private void initTwelveAngryMen()
    {
        twelveAngryMenTrailer = findViewById(R.id.button_12_angry_men_trailer);
        twelveAngryMenBookNow = findViewById(R.id.button_12_angry_men_book_seats);

        String twelveAngryMenTrailerURL = "https://www.youtube.com/watch?v=TEN-2uTi2c0";

       initMovie(twelveAngryMenBookNow, twelveAngryMenTrailer, twelveAngryMenTrailerURL);
    }

    private void initTheDarkKnight()
    {
        theDarkKnightTrailer = findViewById(R.id.button_the_dark_knight_trailer);
        theDarkKnightBookNow = findViewById(R.id.button_the_dark_knight_book_seats);

        String theDarkKnightTrailerURL = "https://www.youtube.com/watch?v=kmJLuwP3MbY&pp=ygUXdGhlIGRhcmsga25pZ2h0IHRyYWlsZXI%3D";

        initMovie(theDarkKnightBookNow, theDarkKnightTrailer, theDarkKnightTrailerURL);
    }

    private void initSilenceOfTheLambs()
    {
        silenceOfTheLambsTrailer = findViewById(R.id.button_silence_of_the_lambs_trailer);
        silenceOfTheLambsBookNow = findViewById(R.id.button_silence_of_the_lambs_book_seats);

        String silenceOfTheLambsTrailerURL = "https://www.youtube.com/watch?v=6iB21hsprAQ&pp=ygUcc2lsZW5jZSBvZiB0aGUgbGFtYnMgdHJhaWxlcg%3D%3D";

        initMovie(silenceOfTheLambsBookNow, silenceOfTheLambsTrailer, silenceOfTheLambsTrailerURL);
    }
    private void initMovie(Button bookNowButton, Button trailerButton, String trailerURL)
    {
        trailerButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(trailerURL));
            intent.setPackage("com.google.android.youtube");
            try{
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(trailerURL)));
            }
        });
    }

    private void initDateRadioButtons(){
        showDate = LocalDate.now();
        datePickerGroup = findViewById(R.id.rg_date_picker);
        radioButtonToday = findViewById(R.id.rb_movie_list_1);
        radioButtonTomorrow = findViewById(R.id.rb_movie_list_2);

        datePickerGroup.setOnCheckedChangeListener((g, checkedId) -> {
            RadioButton selected = findViewById(checkedId);
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
            Toast.makeText(this, toastText, Toast.LENGTH_LONG).show();
        });

    }
}