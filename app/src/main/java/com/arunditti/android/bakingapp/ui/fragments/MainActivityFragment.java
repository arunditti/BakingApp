package com.arunditti.android.bakingapp.ui.fragments;

import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.arunditti.android.bakingapp.R;
import com.arunditti.android.bakingapp.model.Recipe;
import com.arunditti.android.bakingapp.ui.adapters.RecipeAdapter;
import com.arunditti.android.bakingapp.ui.loaders.RecipeListLoader;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by arunditti on 6/12/18.
 */

public class MainActivityFragment extends Fragment implements RecipeAdapter.RecipeAdapterOnClickHandler {

    private static final String LOG_TAG = MainActivityFragment.class.getSimpleName();

    int RECIPE_LOADER_ID = 100;

    private RecipeAdapter mAdapter;

    @BindView(R.id.rv_recipe_list) RecyclerView mRecyclerView;
    @BindView(R.id.tv_error_message_display) TextView mErrorMessageDisplay;
    @BindView(R.id.pb_loading_indicator) ProgressBar mLoadingIndicator;

    //Interface that triggers a callback in the host activity
    onRecipeClickListener mCallBack;

    //Interface that triggers a callback in the host activity
    public interface onRecipeClickListener {
        void onRecipeSelected(Recipe recipeClicked);
    }

    //Override onAttach to make sure that the conteiner activity has implemented the callback
    @Override
    public void onAttach(Context context) {

        super.onAttach(context);

        //This makes sure that the host activity has implemented the callback interface. If not, it throws an exception
        try {
            mCallBack = (onRecipeClickListener) context;
        } catch (ClassCastException e ) {
            throw new ClassCastException(context.toString() + " must implement OnImageClickListener");
        }
    }

    //Mandatory empty constructor
    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {

        //Inflate MainActivityFragment layout
        View rootView = inflater.inflate(R.layout.fragment_recipe_list, container, false);
        ButterKnife.bind(this, rootView);

        //Size in pixels
        int imageWidth = 500;
        GridLayoutManager mGridLayoutManager =
                new GridLayoutManager(getActivity(), calculateBestSpanCount(imageWidth));
        mRecyclerView.setLayoutManager(mGridLayoutManager);

        ArrayList<Recipe> recipe = new ArrayList<Recipe>();

        mAdapter = new RecipeAdapter(getActivity(), this, recipe);

        //Link the adapter to the RecyclerView
        mRecyclerView.setAdapter(mAdapter);

        //Ensure a loader is initialized and active. If the loader doesn't already exist, one is created and starts the loader. Othe
        getLoaderManager().initLoader(RECIPE_LOADER_ID, null, mRecipeListLoader);

        return rootView;
    }

    private void showRecipeDataView() {
        //First make sure error is invisible
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        //Then make sure recipe data is visible
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        //First hide currently visible data
        mRecyclerView.setVisibility(View.INVISIBLE);
        //Then show the error
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(Recipe recipeClicked) {
        mCallBack.onRecipeSelected(recipeClicked);
    }

    private int calculateBestSpanCount(int imageWidth) {
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        float screenWidth = outMetrics.widthPixels;
        return Math.round(screenWidth / imageWidth);
    }

    private LoaderManager.LoaderCallbacks<ArrayList<Recipe>> mRecipeListLoader = new LoaderCallbacks<ArrayList<Recipe>>() {
        @NonNull
        @Override
        public Loader<ArrayList<Recipe>> onCreateLoader(int id, @Nullable Bundle args) {
            Log.i(LOG_TAG, "onCreateLoader is called");

            return new RecipeListLoader(getActivity());
        }

        @Override
        public void onLoadFinished(@NonNull Loader<ArrayList<Recipe>> loader, ArrayList<Recipe> data) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if(data != null) {
                showRecipeDataView();
                mAdapter.updateRecipeList(data);
            } else {
                showErrorMessage();
            }
        }

        @Override
        public void onLoaderReset(@NonNull Loader<ArrayList<Recipe>> loader) {

        }
    };
}
