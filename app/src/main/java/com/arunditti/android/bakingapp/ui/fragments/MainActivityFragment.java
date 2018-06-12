package com.arunditti.android.bakingapp.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.arunditti.android.bakingapp.R;
import com.arunditti.android.bakingapp.model.Recipe;
import com.arunditti.android.bakingapp.ui.adapters.RecipeAdapter;

import java.util.ArrayList;

/**
 * Created by arunditti on 6/12/18.
 */

public class MainActivityFragment extends Fragment implements RecipeAdapter.RecipeAdapterOnClickHandler{

    private RecipeAdapter mAdapter;
    RecyclerView mRecyclerView;
    private TextView mErrorMessageDisplay;
    private Toast mToast;
    private ProgressBar mLoadingIndicator;

    //Interface that triggers a callback in the host activity
    onRecipeClickListener mCallBack;

    //Interface that triggers a callback in the host activity
    public interface onRecipeClickListener {
        void onRecipeSelected(Recipe recipe);
    }

    //Override onAttach to make sure that the conteiner activity has implemented the callback
    @Override
    public void onAttach(Context context) {

        super.onAttach(context);
    }

    //Mandatory empty constructor
    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {

        //Inflate MainActivityFragment layout
        View rootView = inflater.inflate(R.layout.fragment_recipe_list, container, false);
        mRecyclerView = rootView.findViewById(R.id.rv_recipe_list);

        GridLayoutManager mGridLayoutManager = new GridLayoutManager(getActivity(), 2);
        mRecyclerView.setLayoutManager(mGridLayoutManager);

        ArrayList<Recipe> recipe = new ArrayList<Recipe>();

        mAdapter = new RecipeAdapter(this, this, recipe);

        //Link the adapter to the RecyclerView
        mRecyclerView.setAdapter(mAdapter);

         /* This TextView is used to display errors and will be hidden if there are no errors */
        mErrorMessageDisplay = rootView.findViewById(R.id.tv_error_message_display);
        mLoadingIndicator = rootView.findViewById(R.id.pb_loading_indicator);

        return rootView;
    }

    @Override
    public void onClick(Recipe recipeClicked) {
        mCallBack.onRecipeSelected(recipeClicked);
    }
}
