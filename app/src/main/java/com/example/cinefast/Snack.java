package com.example.cinefast;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Snack implements Parcelable {

    private String name;
    private String description;
    private double price;
    private int quantity;

    public Snack(String name, String description, double price, int quantity) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
    }

    protected Snack(Parcel in)
    {
        name = in.readString();
        description = in.readString();
        price = in.readDouble();
        quantity = in.readInt();
    }

    public static final Parcelable.Creator<Snack> CREATOR = new Parcelable.Creator<>() {
        @Override
        public Snack createFromParcel(Parcel in) {
            return new Snack(in);
        }

        @Override
        public Snack[] newArray(int size) {
            return new Snack[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(description);
        dest.writeDouble(price);
        dest.writeInt(quantity);
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
