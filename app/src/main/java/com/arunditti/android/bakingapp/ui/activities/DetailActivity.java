package com.arunditti.android.bakingapp.ui.activities;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.arunditti.android.bakingapp.R;
import com.arunditti.android.bakingapp.model.Recipe;
import com.arunditti.android.bakingapp.model.RecipeStep;
import com.arunditti.android.bakingapp.ui.fragments.DetailActivityFragment;

public class DetailActivity extends AppCompatActivity implements DetailActivityFragment.onRecipeStepClickListener {

    private static final String LOG_TAG = DetailActivity.class.getSimpleName();

    private static final String DETAILS_KEY = "Recipe_parcel";
    private static final String STEP_KEY ="Recipe_step";
    private Recipe mCurrentRecipe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intentThatStartedThisActivity = getIntent();
        mCurrentRecipe = intentThatStartedThisActivity.getParcelableExtra(DETAILS_KEY);
    }

    @Override
    public void onRecipeStepSelected(RecipeStep recipeStepClicked) {
        Intent intentToStartRecipeStepActivity = new Intent(this, RecipeStepActivity.class);
        intentToStartRecipeStepActivity.putExtra(DETAILS_KEY, mCurrentRecipe);
        intentToStartRecipeStepActivity.putExtra(STEP_KEY, recipeStepClicked);
        startActivity(intentToStartRecipeStepActivity);
    }
}
