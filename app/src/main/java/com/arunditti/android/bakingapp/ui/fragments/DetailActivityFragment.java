package com.arunditti.android.bakingapp.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import android.widget.ImageView;
import android.widget.TextView;

import com.arunditti.android.bakingapp.R;
import com.arunditti.android.bakingapp.model.Recipe;
import com.arunditti.android.bakingapp.model.RecipeIngredient;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by arunditti on 6/14/18.
 */

public class DetailActivityFragment extends Fragment {

    private static final String LOG_TAG = DetailActivityFragment.class.getSimpleName();

    private static final String DETAILS_KEY = "Recipe_parcel";
    private Recipe mCurrentRecipe;
//    //Declare an ActivityDetailBinding field called mDetailBinding
//    private FragmentRecipeDetailBinding mDetailBinding;

    //Mandatory empty constructor
    public DetailActivityFragment(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_recipe_detail, container, false);

        Intent intent = getActivity().getIntent();
        mCurrentRecipe = intent.getParcelableExtra(DETAILS_KEY);

        ImageView recipeImage = rootView.findViewById(R.id.iv_recipe_detail_image);
        TextView name = rootView.findViewById(R.id.tv_recipe_name_detail);
        TextView servings = rootView.findViewById(R.id.tv_servings_detail);
        TextView ingredients = rootView.findViewById(R.id.tv_ingredients_list_detail);
//        TextView steps = rootView.findViewById(R.id.rv_recipe_list);

        name.setText(mCurrentRecipe.getRecipeName());
        servings.setText(mCurrentRecipe.getServings());

        StringBuilder stringBuilder = new StringBuilder();

        if (mCurrentRecipe.getRecipeIngredients().isEmpty()) {
            ingredients.setText(R.string.error_message);
        } else {
            ArrayList<RecipeIngredient> ingredientsList = mCurrentRecipe.getRecipeIngredients();
            for (int i = 0; i < ingredientsList.size(); i++) {
                Log.d(LOG_TAG, "*****Ingredient list is: " +ingredientsList);
                Log.d(LOG_TAG, "*****Ingredient list size is: " +ingredientsList.size());

                stringBuilder.append(ingredientsList.get(i).getIngredientName());
               stringBuilder.append(ingredientsList.get(i).getQuantity());
               stringBuilder.append(ingredientsList.get(i).getMeasure());

//                ingredientsList.add(new RecipeIngredient(ingredientsList.get(i).getQuantity(),
//                        ingredientsList.get(i).getMeasure(),
//                        ingredientsList.get(i).getIngredientName()));
            }

            String displayIngredients = stringBuilder.toString();
            ingredients.setText(displayIngredients);
        }


//        if (mCurrentRecipe.getRecipeSteps().isEmpty()) {
//            ingredients.setText(R.string.error_message);
//        } else {
//            ArrayList<RecipeStep> recipeStepsList = mCurrentRecipe.getRecipeSteps();
//            steps.setText(TextUtils.join("", recipeStepsList));
//        }

        if(mCurrentRecipe.getRecipeImage().isEmpty()) {
            recipeImage.setImageResource(R.drawable.ic_launcher_foreground);
        } else {
            Picasso.with(getActivity())
                    .load(mCurrentRecipe.getRecipeImage())
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_foreground)
                    .fit()
                    .into(recipeImage);
        }

//        ActionBar actionBar = (AppCompatActivity) getActivity().getSupportActionBar();
//        actionBar.setTitle(mCurrentRecipe.getRecipeName());

        return rootView;
    }
}
