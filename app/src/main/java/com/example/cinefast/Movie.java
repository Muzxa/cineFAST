package com.example.cinefast;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;

public class Movie implements Parcelable {

    private String name;
    private String runtime;
    private String genre;
    private int cinema;
    private String showTime;
    public Movie(String name, String runtime, String genre, int cinema, String showTime)
    {
        this.name = name;
        this.runtime = runtime;
        this.genre = genre;
        this.cinema = cinema;
        this.showTime = showTime;
    }

    protected Movie(Parcel in)
    {
        name = in.readString();
        runtime = in.readString();
        genre = in.readString();
        cinema = in.readInt();
        showTime = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(runtime);
        dest.writeString(genre);
        dest.writeInt(cinema);
        dest.writeString(showTime);
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getCinema() {
        return cinema;
    }

    public void setCinema(int cinema) {
        this.cinema = cinema;
    }

    public String getShowTime() {
        return showTime;
    }

    public void setShowTime(String showTime) {
        this.showTime = showTime;
    }
}