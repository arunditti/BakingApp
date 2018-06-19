package com.arunditti.android.bakingapp.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arunditti.android.bakingapp.R;

/**
 * Created by arunditti on 6/18/18.
 */

public class RecipeStepFragment extends Fragment {

    //Mandatory empty constructor
    public RecipeStepFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle onsaveInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_recipe_steps, container, false);

        return rootView;
    }
}
