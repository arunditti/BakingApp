package com.arunditti.android.bakingapp.ui.activities;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.arunditti.android.bakingapp.R;
import com.arunditti.android.bakingapp.model.Recipe;
import com.arunditti.android.bakingapp.model.RecipeStep;
import com.arunditti.android.bakingapp.ui.fragments.DetailActivityFragment;
import com.arunditti.android.bakingapp.ui.fragments.RecipeStepFragment;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity implements DetailActivityFragment.onRecipeStepClickListener {

    private static final String LOG_TAG = DetailActivity.class.getSimpleName();

    private static final String DETAILS_KEY = "Recipe_parcel";
    private static final String STEP_KEY ="Recipe_step";
    private Recipe mCurrentRecipe;
    private boolean mTwoPane;
    private int mClickedStepNumber;
    private ArrayList<RecipeStep> mRecipeSteps = new ArrayList<RecipeStep>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intentThatStartedThisActivity = getIntent();
        mCurrentRecipe = intentThatStartedThisActivity.getParcelableExtra(DETAILS_KEY);

        if(findViewById(R.id.recipe_step_fragment) != null) {

            mTwoPane = true;

            if(savedInstanceState == null) {
                // Create a new RecipeStepFragment instance and display it using the FragmentManager
                RecipeStepFragment recipeStepFragment = new RecipeStepFragment();

                //Use a FragmentManager and transaction to add the fragment to the screen
                FragmentManager fragmentManager = getSupportFragmentManager();

                //Fragment transaction
                fragmentManager.beginTransaction().add(R.id.recipe_step_fragment, recipeStepFragment).commit();
            }

        } else {
            mTwoPane = false;
        }

    }

    @Override
    public void onRecipeStepSelected(int recipeStepClicked) {

        if(mTwoPane) {
            RecipeStepFragment recipeStepFragment = (RecipeStepFragment) getSupportFragmentManager().findFragmentById(R.id.recipe_step_fragment);
            recipeStepFragment.populateStepDetailUI(recipeStepClicked);

//            Bundle args = new Bundle();
//            args.putInt("Arguments", recipeStepClicked.getId());
//            Log.d(LOG_TAG, "Step clicked is:" + recipeStepClicked.getId());
//
//            RecipeStepFragment recipeStepFragment = new RecipeStepFragment();
//            recipeStepFragment.setArguments(args);
//            FragmentManager fragmentManager = getSupportFragmentManager();
//            fragmentManager.beginTransaction().replace(R.id.recipe_step_fragment, recipeStepFragment).commit();

        } else {

            Intent intentToStartRecipeStepActivity = new Intent(this, RecipeStepActivity.class);
            intentToStartRecipeStepActivity.putExtra(DETAILS_KEY, mCurrentRecipe);
            intentToStartRecipeStepActivity.putExtra(STEP_KEY, recipeStepClicked);
            startActivity(intentToStartRecipeStepActivity);
        }
    }
}
