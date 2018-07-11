package com.arunditti.android.bakingapp.ui.loaders;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import com.arunditti.android.bakingapp.model.Recipe;
import com.arunditti.android.bakingapp.utils.JsonUtils;
import com.arunditti.android.bakingapp.utils.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by arunditti on 6/14/18.
 */

public class RecipeListLoader extends AsyncTaskLoader<ArrayList<Recipe>> {

    private static final String LOG_TAG = RecipeListLoader.class.getSimpleName();

    public RecipeListLoader(@NonNull Context context) {
        super(context);
    }

        private ArrayList<Recipe> mRecipeData = null;

        @Override
        protected void onStartLoading() {
            if(mRecipeData != null) {
                deliverResult(mRecipeData);
            } else {
                forceLoad();
            }
        }

        @Nullable
        @Override
        public ArrayList<Recipe> loadInBackground() {

            URL recipeUrl = NetworkUtils.getRecipeUrl();

            try {
                String jsonRecipeResponse = NetworkUtils.getResponseFromHttpUrl(recipeUrl);
                return JsonUtils.getRecipesInformation(getContext(), jsonRecipeResponse);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

    public void deliverResult(ArrayList<Recipe> data) {
        mRecipeData = data;
        super.deliverResult(data);
    }
}
