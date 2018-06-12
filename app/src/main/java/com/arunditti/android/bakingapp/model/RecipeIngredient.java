package com.arunditti.android.bakingapp.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by arunditti on 6/12/18.
 */

public class RecipeIngredient implements Parcelable{

    private int quantity;
    private String measure;
    private String ingredientName;

    public RecipeIngredient(int quantity, String measure, String ingredientName) {
        this.quantity = quantity;
        this.measure = measure;
        this.ingredientName = ingredientName;
    }

    private RecipeIngredient(Parcel in) {
        quantity = in.readInt();
        measure = in.readString();
        ingredientName = in.readString();
    }

    public static final Creator<RecipeIngredient> CREATOR = new Creator<RecipeIngredient>() {
        @Override
        public RecipeIngredient createFromParcel(Parcel in) {
            return new RecipeIngredient(in);
        }

        @Override
        public RecipeIngredient[] newArray(int size) {
            return new RecipeIngredient[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(quantity);
        parcel.writeString(measure);
        parcel.writeString(ingredientName);
    }

    public int getQuantity() {
        return quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public String getIngredientName() {
        return ingredientName;
    }
}
