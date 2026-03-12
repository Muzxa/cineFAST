package com.example.cinefast;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MovieListViewHolder extends RecyclerView.ViewHolder {

    Button btnTrailer, btnBook;
    ImageView ivPoster;
    TextView tvTitle, tvGenreRuntime;

    public MovieListViewHolder(@NonNull View itemView) {
        super(itemView);
        ivPoster = itemView.findViewById(R.id.iv_movie_card_poster);
        btnTrailer = itemView.findViewById(R.id.button_movie_card_trailer);
        btnBook = itemView.findViewById(R.id.button_movie_card_book_seats);
        tvTitle = itemView.findViewById((R.id.tv_movie_card_title));
        tvGenreRuntime=itemView.findViewById(R.id.tv_movie_card_subtitle);
    }
}
