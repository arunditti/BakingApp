package com.arunditti.android.bakingapp.ui.activities;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.arunditti.android.bakingapp.R;
import com.arunditti.android.bakingapp.model.Recipe;
import com.arunditti.android.bakingapp.model.RecipeStep;
import com.arunditti.android.bakingapp.ui.fragments.MainActivityFragment;
import com.arunditti.android.bakingapp.ui.fragments.RecipeStepFragment;

public class RecipeStepActivity extends AppCompatActivity {

    private static final String LOG_TAG = RecipeStepActivity.class.getSimpleName();

    private static final String DETAILS_KEY = "Recipe_parcel";
    private static final String STEP_KEY = "Recipe_step";
    private Recipe mCurrentRecipe;
    private RecipeStep mCurrentStep;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step);

        // Create a new RecipeStepFragment instance and display it using the FragmentManager
        RecipeStepFragment recipeStepFragment = new RecipeStepFragment();

        //Use a FragmentManager and transaction to add the fragment to the screen
        FragmentManager fragmentManager = getSupportFragmentManager();

        //Fragment transaction
        fragmentManager.beginTransaction().add(R.id.recipe_step_fragment, recipeStepFragment).commit();

        Intent intentThatStartedThisActivity = getIntent();
        mCurrentRecipe = intentThatStartedThisActivity.getParcelableExtra(DETAILS_KEY);
        mCurrentStep = intentThatStartedThisActivity.getParcelableExtra(STEP_KEY);
    }
}
