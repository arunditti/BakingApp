package com.arunditti.android.bakingapp.ui.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.arunditti.android.bakingapp.R;
import com.arunditti.android.bakingapp.model.Recipe;
import com.arunditti.android.bakingapp.ui.fragments.MainActivityFragment;

public class MainActivity extends AppCompatActivity implements MainActivityFragment.onRecipeClickListener {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final String DETAILS_KEY = "Recipe_parcel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onRecipeSelected(Recipe recipeClicked) {
        Intent intentToStartDetailActivity = new Intent(MainActivity.this, DetailActivity.class);
        intentToStartDetailActivity.putExtra(DETAILS_KEY, recipeClicked);
        startActivity(intentToStartDetailActivity);
    }
}
