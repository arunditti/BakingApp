package com.arunditti.android.bakingapp.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by arunditti on 6/12/18.
 */

public class RecipeStep implements Parcelable{

    private int id;
    private String shortDescription;
    private String description;
    private String videoUrl;
    private String thumbnailUrl;

    public RecipeStep(int id, String shortDescription, String description, String videoUrl, String thumbnailUrl) {
        this.id = id;
        this.shortDescription = shortDescription;
        this.description = description;
        this.videoUrl = videoUrl;
        this.thumbnailUrl = thumbnailUrl;

    }

    private RecipeStep(Parcel in) {
        id = in.readInt();
        shortDescription = in.readString();
        description = in.readString();
        videoUrl = in. readString();
        thumbnailUrl = in.readString();
    }

    public static final Creator<RecipeStep> CREATOR = new Creator<RecipeStep>() {
        @Override
        public RecipeStep createFromParcel(Parcel in) {
            return new RecipeStep(in);
        }

        @Override
        public RecipeStep[] newArray(int size) {
            return new RecipeStep[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(shortDescription);
        parcel.writeString(description);
        parcel.writeString(videoUrl);
        parcel.writeString(thumbnailUrl);
    }

    public int getId() {
        return id;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }
}
