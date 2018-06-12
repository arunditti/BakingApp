package com.arunditti.android.bakingapp.ui.activities;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.arunditti.android.bakingapp.R;
import com.arunditti.android.bakingapp.model.Recipe;
import com.arunditti.android.bakingapp.ui.adapters.RecipeAdapter;
import com.arunditti.android.bakingapp.ui.fragments.MainActivityFragment;

public class MainActivity extends AppCompatActivity implements MainActivityFragment.onRecipeClickListener {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private RecipeAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onRecipeSelected(Recipe recipe) {

    }
}
