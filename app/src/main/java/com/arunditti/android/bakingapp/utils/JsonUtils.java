package com.arunditti.android.bakingapp.utils;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import com.arunditti.android.bakingapp.model.Recipe;
import com.arunditti.android.bakingapp.model.RecipeIngredient;
import com.arunditti.android.bakingapp.model.RecipeStep;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.DTDHandler;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.SimpleTimeZone;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by arunditti on 6/13/18.
 */

public class JsonUtils {

    private static final String LOG_TAG = JsonUtils.class.getSimpleName();

    private static final String JSON_RECIPE_ID = "id";
    private static final String JSON_RECIPE_NAME = "name";
    private static final String JSON_RECIPE_INGREDIENTS = "ingredients";
    private static final String JSON_RECIPE_QUALTITY = "quantity";
    private static final String JSON_RECIPE_MEASURE = "measure";
    private static final String JSON_RECIPE_INGREDIENT = "ingredient";
    private static final String JSON_RECIPE_STEPS = "steps";
    private static final String JSON_RECIPE_SHORT_DESCRIPTION = "shortDescription";
    private static final String JSON_RECIPE_DESCRIPTION = "description";
    private static final String JSON_RECIPE_VIDEO_URL = "videoURL";
    private static final String JSON_RECIPE_THUMBNAIL = "thumbnail";
    private static final String JSON_RECIPE_SERVINGS = "servings";
    private static final String JSON_RECIPE_IMAGE = "image";


    public static ArrayList<Recipe> getRecipesInformation(Context context, String recipeJsonStr) throws JSONException {

        JSONArray recipeArray = new JSONArray(recipeJsonStr);
        ArrayList<Recipe> recipesList= new ArrayList<Recipe>();

        recipesList.clear();
        for(int i = 0; i < recipeArray.length(); i++) {
            JSONObject recipe = recipeArray.getJSONObject(i);
            int recipeId = recipe.optInt(JSON_RECIPE_ID);
            String recipeName = recipe.optString(JSON_RECIPE_NAME);
            String recipeServings = recipe.optString(JSON_RECIPE_SERVINGS);
            String recipeImage = recipe.optString(JSON_RECIPE_IMAGE);

            JSONArray recipeIngredientsArray = recipe.getJSONArray(JSON_RECIPE_INGREDIENTS);
            ArrayList<RecipeIngredient> recipeIngredients = new ArrayList<RecipeIngredient>();

            for( int iIngredients = 0; iIngredients < recipeIngredientsArray.length(); iIngredients++) {
                JSONObject ingredients = recipeIngredientsArray.getJSONObject(i);
                int quantiy = ingredients.optInt(JSON_RECIPE_QUALTITY);
                String measure = ingredients.optString(JSON_RECIPE_MEASURE);
                String ingredient = ingredients.optString(JSON_RECIPE_INGREDIENT);

                recipeIngredients.add(new RecipeIngredient(quantiy, measure, ingredient));
            }

            JSONArray recipeStepsArray = recipe.getJSONArray(JSON_RECIPE_STEPS);
            ArrayList<RecipeStep> recipeSteps = new ArrayList<RecipeStep>();

            for( int iSteps = 0; iSteps < recipeStepsArray.length(); iSteps++) {
                JSONObject steps = recipeStepsArray.getJSONObject(i);
                int stepsId = steps.optInt(JSON_RECIPE_ID);
                String shortDescription = steps.optString(JSON_RECIPE_SHORT_DESCRIPTION);
                String description = steps.optString(JSON_RECIPE_DESCRIPTION);
                String videoUrl = steps.optString(JSON_RECIPE_VIDEO_URL);
                String thumbnail = steps.optString(JSON_RECIPE_THUMBNAIL);

                recipeSteps.add(new RecipeStep(stepsId, shortDescription, description, videoUrl, thumbnail));
            }

            recipesList.add(new Recipe(recipeId, recipeName, recipeIngredients, recipeSteps, recipeImage, recipeServings, recipeImage));

        }
        return recipesList;
    }

}
