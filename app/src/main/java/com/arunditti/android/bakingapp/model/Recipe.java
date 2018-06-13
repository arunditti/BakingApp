package com.arunditti.android.bakingapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by arunditti on 6/12/18.
 */

public class Recipe implements Parcelable {
    public int recipeId;
    public String recipeName;
    private ArrayList<RecipeIngredient> recipeIngredients;
    private ArrayList<RecipeStep> recipeSteps;
    private String recipeThumbnailUrl;
    private String servings;
    public String recipeImage;

    public Recipe(int id, String name, ArrayList<RecipeIngredient> ingredients,
                  ArrayList<RecipeStep> steps, String thumbnailUrl, String servings, String image) {
        this.recipeId = id;
        this.recipeName = name;
        this.recipeIngredients = ingredients;
        this.recipeSteps = steps;
        this.recipeThumbnailUrl = thumbnailUrl;
        this.servings = servings;
        this.recipeImage = image;
    }

    private Recipe(Parcel in) {
        recipeId = in.readInt();
        recipeName = in.readString();
        //in.readList(recipeIngredients, RecipeIngredient.class.getClassLoader());
        recipeIngredients = in.createTypedArrayList(RecipeIngredient.CREATOR);
        //in.readList(recipeSteps, RecipeStep.class.getClassLoader());
        recipeSteps = in.createTypedArrayList(RecipeStep.CREATOR);
        recipeThumbnailUrl = in.readString();
        servings = in.readString();
        recipeImage = in.readString();
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(recipeId);
        parcel.writeString(recipeName);
        parcel.writeTypedList(recipeIngredients);
        parcel.writeTypedList(recipeSteps);
        parcel.writeString(recipeThumbnailUrl);
        parcel.writeString(servings);
        parcel.writeString(recipeImage);
    }

    public int getRecipeId() {
        return recipeId;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public ArrayList<RecipeIngredient> getRecipeIngredients() {
        return recipeIngredients;
    }

    public ArrayList<RecipeStep> getRecipeSteps() {
        return recipeSteps;
    }

    public String getRecipeThumbnailUrl() {
        return recipeThumbnailUrl;
    }

    public String getServings() {
        return servings;
    }

    public String getRecipeImage() {
        return recipeImage;
    }
}
