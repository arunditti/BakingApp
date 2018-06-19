package com.arunditti.android.bakingapp.ui.activities;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.arunditti.android.bakingapp.R;
import com.arunditti.android.bakingapp.model.Recipe;
import com.arunditti.android.bakingapp.model.RecipeStep;

public class RecipeStepActivity extends AppCompatActivity {

    private static final String LOG_TAG = RecipeStepActivity.class.getSimpleName();

    private static final String STEP_KEY = "Recipe_step";
    private Recipe mCurrentRecipe;
    private RecipeStep mCurrentStep;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step);

        Intent intentThatStartedThisActivity = getIntent();
        mCurrentStep = intentThatStartedThisActivity.getParcelableExtra(STEP_KEY);
    }
}
