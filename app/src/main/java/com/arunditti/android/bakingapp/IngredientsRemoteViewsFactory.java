package com.arunditti.android.bakingapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.arunditti.android.bakingapp.model.Recipe;
import com.arunditti.android.bakingapp.model.RecipeIngredient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.security.acl.LastOwnerException;
import java.util.ArrayList;

/**
 * Created by arunditti on 7/5/18.
 */

public class IngredientsRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private  static final String LOG_TAG = IngredientsRemoteViewsFactory.class.getSimpleName();
    private static final String SHARED_PREFERENCE_KEY = "shared_preference_key";
    private Context mContext;
    private ArrayList<RecipeIngredient> mIngredients;

    private Recipe mRecipe;
    public IngredientsRemoteViewsFactory(Context applicationContext) {
        mContext = applicationContext;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        String json = sharedPreferences.getString(SHARED_PREFERENCE_KEY, "");

       if(!json.equals("")){
            Gson gson = new Gson();
            mIngredients = gson.fromJson(json, new TypeToken<ArrayList<RecipeIngredient>>() {
            }.getType());
        }
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if(mIngredients != null) {
            return mIngredients.size();
        } else
            return 0;
    }

    @Override
    public RemoteViews getViewAt(int i) {
        RecipeIngredient recipeIngredients = mIngredients.get(i);

        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.widget_list_item);

        String ingredients = recipeIngredients.getIngredientName() +
                " " + recipeIngredients.getQuantity() +
                " " + recipeIngredients.getMeasure();

        remoteViews.setTextViewText(R.id.widget_text_view, ingredients);

        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
