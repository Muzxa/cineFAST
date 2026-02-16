package com.example.cinefast;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Seat implements Parcelable {

    private String row;
    private String column;
    private int price;

    public Seat(String row, String column, int price) {
        this.row = row;
        this.price = price;
        this.column = column;
    }

    protected Seat(Parcel in)
    {
        row = in.readString();
        column = in.readString();
        price = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(row);
        dest.writeString(column);
        dest.writeInt(price);
    }

    public static final Parcelable.Creator<Seat> CREATOR = new Parcelable.Creator<>() {
        @Override
        public Seat createFromParcel(Parcel in) {
            return new Seat(in);
        }

        @Override
        public Seat[] newArray(int size) {
            return new Seat[size];
        }
    };

    public String getRow() {
        return row;
    }

    public void setRow(String row) {
        this.row = row;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
