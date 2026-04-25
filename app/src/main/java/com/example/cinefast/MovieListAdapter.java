package com.example.cinefast;

import static androidx.core.content.ContentProviderCompat.requireContext;
import static androidx.core.content.ContextCompat.startActivity;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Date;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListViewHolder> {

    Context context;
    ArrayList<Movie> movies;
    Long showDate;

    public MovieListAdapter(Context context, ArrayList<Movie> movies) {
        this.context = context;
        this.movies = movies;
        this.showDate = new Date().getTime();
    }

    public MovieListAdapter(Context context, ArrayList<Movie> movies, Long showDate) {
        this.context = context;
        this.movies = movies;
        this.showDate = showDate;
    }

    @NonNull
    @Override
    public MovieListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MovieListViewHolder(LayoutInflater.from(context).inflate(R.layout.movie_card_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MovieListViewHolder holder, int position) {

        Movie movie = movies.get(position);
        holder.tvTitle.setText(movie.getName());
        holder.tvGenreRuntime.setText(movie.getGenre() + " | " + movie.getRuntime());
        holder.ivPoster.setImageResource(movie.getPosterId());

        holder.btnTrailer.setOnClickListener(v -> {

            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(movie.getTrailerURL()));
            i.setPackage("com.google.android.youtube");
            try{
                context.startActivity(i);
            } catch (ActivityNotFoundException e) {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(movie.getTrailerURL())));
            }
        });

        holder.itemView.setOnLongClickListener(view -> {
            int currentPosition = holder.getBindingAdapterPosition();
            if (currentPosition == RecyclerView.NO_POSITION) return true;

            AlertDialog.Builder builder = new AlertDialog.Builder(context);

            builder
                    .setTitle("Delete Movie")
                    .setMessage("Are you sure that you want to delete " + movie.getName())
                    .setPositiveButton("Delete", (dialog, which) ->{
                        movies.remove(currentPosition);
                        notifyItemRemoved(currentPosition);
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
            return true;
        });

        holder.btnBook.setOnClickListener(v -> {
            Intent intent = new Intent(context, ActivitySeatSelection.class);
            intent.putExtra("date", showDate);
            intent.putExtra("movie", movie);
            context.startActivity(intent);
        });
    }
    @Override
    public int getItemCount() {
        return movies.size();
    }

}