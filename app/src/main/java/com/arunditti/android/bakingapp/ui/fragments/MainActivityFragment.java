package com.arunditti.android.bakingapp.ui.fragments;

import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.arunditti.android.bakingapp.R;
import com.arunditti.android.bakingapp.model.Recipe;
import com.arunditti.android.bakingapp.ui.adapters.RecipeAdapter;
import com.arunditti.android.bakingapp.utils.JsonUtils;
import com.arunditti.android.bakingapp.utils.NetworkUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by arunditti on 6/12/18.
 */

public class MainActivityFragment extends Fragment implements RecipeAdapter.RecipeAdapterOnClickHandler,
        LoaderManager.LoaderCallbacks<ArrayList<Recipe>> {

    private static final String LOG_TAG = MainActivityFragment.class.getSimpleName();

    int RECIPE_LOADER_ID = 100;

    private RecipeAdapter mAdapter;
    RecyclerView mRecyclerView;
    private TextView mErrorMessageDisplay;
    private Toast mToast;
    private ProgressBar mLoadingIndicator;

    //Interface that triggers a callback in the host activity
    onRecipeClickListener mCallBack;

    @NonNull
    @Override
    public Loader<ArrayList<Recipe>> onCreateLoader(int id, @Nullable Bundle args) {
        Log.i(LOG_TAG, "onCreateLoder is called");

        return new AsyncTaskLoader<ArrayList<Recipe>>(getActivity()) {

            ArrayList<Recipe> mRecipeData = null;

            @Override
            protected void onStartLoading() {
                if(mRecipeData != null) {
                    deliverResult(mRecipeData);
                } else {
                    mLoadingIndicator.setVisibility(View.VISIBLE);
                    forceLoad();
                }
            }

            @Nullable
            @Override
            public ArrayList<Recipe> loadInBackground() {

                URL recipeUrl = NetworkUtils.getRecipeUrl();

                try {
                    String jsonRecipeResponse = NetworkUtils.getResponseFromHttpUrl(recipeUrl);
                    ArrayList<Recipe> simpleJsonRecipeData = JsonUtils.getRecipesInformation(getContext(), jsonRecipeResponse);
                    return simpleJsonRecipeData;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            public void deliverResult(ArrayList<Recipe> data) {
                mRecipeData = data;
                super.deliverResult(data);
            }
        };
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

        GridLayoutManager mGridLayoutManager = new GridLayoutManager(getActivity(), 1);
        mRecyclerView.setLayoutManager(mGridLayoutManager);

        ArrayList<Recipe> recipe = new ArrayList<Recipe>();

        mAdapter = new RecipeAdapter(getActivity(), this, recipe);

        //Link the adapter to the RecyclerView
        mRecyclerView.setAdapter(mAdapter);

         /* This TextView is used to display errors and will be hidden if there are no errors */
        mErrorMessageDisplay = rootView.findViewById(R.id.tv_error_message_display);
        mLoadingIndicator = rootView.findViewById(R.id.pb_loading_indicator);

        //Ensure a loader is initialized and active. If the loader doesn't already exist, one is created and starts the loader. Othe
        getLoaderManager().initLoader(RECIPE_LOADER_ID, null, this);

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


}
