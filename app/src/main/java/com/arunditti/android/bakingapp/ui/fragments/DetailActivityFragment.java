package com.arunditti.android.bakingapp.ui.fragments;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.arunditti.android.bakingapp.BakingAppWidgetProvider;
import com.arunditti.android.bakingapp.R;
import com.arunditti.android.bakingapp.model.Recipe;
import com.arunditti.android.bakingapp.model.RecipeIngredient;
import com.arunditti.android.bakingapp.model.RecipeStep;
import com.arunditti.android.bakingapp.ui.adapters.RecipeStepsAdapter;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by arunditti on 6/14/18.
 */

public class DetailActivityFragment extends Fragment implements RecipeStepsAdapter.RecipeStepsAdapterOnClickHandler {

    private static final String LOG_TAG = DetailActivityFragment.class.getSimpleName();

    private static final String SHARED_PREFERENCE_KEY = "shared_preference_key";
    private static final String DETAILS_KEY = "Recipe_parcel";
    private Recipe mCurrentRecipe;
    private RecipeStepsAdapter mStepsAdapter;
    private RecyclerView mRecyclerView;

    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;
    private int recipePicture;

    private ArrayList<RecipeIngredient> mRecipeIngredients = new ArrayList<RecipeIngredient>();

    private ArrayList<RecipeStep> mRecipeSteps = new ArrayList<RecipeStep>();


//    //Declare an ActivityDetailBinding field called mDetailBinding
//    private FragmentRecipeDetailBinding mDetailBinding;

    //Interface that triggers a callback in the host activity
    onRecipeStepClickListener mCallBack;

//   // Interface that triggers a callback in the host activity
    public interface onRecipeStepClickListener {
        void onRecipeStepSelected(int recipeClicked);
    }

    //Override onAttach to make sure that the conteiner activity has implemented the callback
    @Override
    public void onAttach(Context context) {

        super.onAttach(context);

        //This makes sure that the host activity has implemented the callback interface. If not, it throws an exception
        try {
            mCallBack = (onRecipeStepClickListener) context;
        } catch (ClassCastException e ) {
            throw new ClassCastException(context.toString() + " must implement OnRecipesStepClickListener");
        }
    }

    //Mandatory empty constructor
    public DetailActivityFragment(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_recipe_detail, container, false);

        if(saveInstanceState == null) {
            Intent intent = getActivity().getIntent();
            mCurrentRecipe = intent.getParcelableExtra(DETAILS_KEY);

            //Save recipe ingredients to SharedPreferences
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(getString(R.string.recipe_name_key), mCurrentRecipe.getRecipeName());
            editor.putString(getString(R.string.recipe_thumbnail_key), mCurrentRecipe.getRecipeThumbnailUrl());
            editor.commit();

            Gson gson = new Gson();
            String json = gson.toJson(mCurrentRecipe.getRecipeIngredients());
            editor.putString(SHARED_PREFERENCE_KEY, json);
            editor.apply();

            Intent widgetIntent = new Intent(getActivity(), BakingAppWidgetProvider.class);
            widgetIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            getContext().sendBroadcast(widgetIntent);

            Toast.makeText(getActivity(), "Widget is added", Toast.LENGTH_SHORT).show();

        } else {
            mCurrentRecipe = saveInstanceState.getParcelable(DETAILS_KEY);
        }

        ImageView recipeImage = rootView.findViewById(R.id.iv_recipe_detail_image);
        TextView name = rootView.findViewById(R.id.tv_recipe_name_detail);
        TextView servings = rootView.findViewById(R.id.tv_servings_detail);
        TextView ingredients = rootView.findViewById(R.id.tv_ingredients_list_detail);

        name.setText(mCurrentRecipe.getRecipeName());
        servings.setText(mCurrentRecipe.getServings());

        StringBuffer stringBuffer = new StringBuffer();

        if (mCurrentRecipe.getRecipeIngredients().isEmpty()) {
            ingredients.setText(R.string.error_message);
        } else {
            ArrayList<RecipeIngredient> ingredientsList = mCurrentRecipe.getRecipeIngredients();
            for (int i = 0; i < ingredientsList.size(); i++) {
                stringBuffer.append(" ")
                        .append(ingredientsList.get(i).getIngredientName() + "  ")
                        .append(ingredientsList.get(i).getQuantity() + " ")
                        .append(ingredientsList.get(i).getMeasure().toLowerCase() + "\n");
            }

            String displayIngredients = stringBuffer.toString();
            ingredients.setText(displayIngredients);

            Log.d(LOG_TAG, "*****Ingredient list is: " +ingredientsList);
            Log.d(LOG_TAG, "*****Ingredient list size is: " +ingredientsList.size());
        }

        switch(mCurrentRecipe.getRecipeName()) {
            case "Nutella Pie":
                recipePicture = R.drawable.nutella_pie;
                break;
            case "Brownies":
                recipePicture = R.drawable.brownies;
                break;
            case "Yellow Cake":
                recipePicture = R.drawable.yellow_cake;
                break;
            case "Cheesecake":
                recipePicture = R.drawable.cheesecake;
                break;
            default:
                recipePicture = R.drawable.ic_launcher_foreground;
        }

        if(mCurrentRecipe.getRecipeImage().isEmpty()) {
            //recipeImage.setImageResource(R.drawable.ic_launcher_foreground);
            Picasso.with(getActivity())
                    .load(recipePicture)
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_foreground)
                    .fit()
                    .into(recipeImage);
        } else {
            Picasso.with(getActivity())
                    .load(mCurrentRecipe.getRecipeImage())
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_foreground)
                    .fit()
                    .into(recipeImage);
        }

        //RecyclerView to display recipe steps
        mRecyclerView = rootView.findViewById(R.id.rv_recipe_steps);

        LinearLayoutManager mLinearLayoutManager =
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        mRecipeSteps = mCurrentRecipe.getRecipeSteps();
        mStepsAdapter = new RecipeStepsAdapter(getActivity(), this, mRecipeSteps);

        //Link adapter to the RecyclerView
        mRecyclerView.setAdapter(mStepsAdapter);

        //Action to display recipe name
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle(mCurrentRecipe.recipeName);

         /* This TextView is used to display errors and will be hidden if there are no errors */
        mErrorMessageDisplay = rootView.findViewById(R.id.tv_error_message_display);
        mLoadingIndicator = rootView.findViewById(R.id.pb_loading_indicator);

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(DETAILS_KEY, mCurrentRecipe);
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
    public void onClick(int recipeStepClicked) {
        mCallBack.onRecipeStepSelected(recipeStepClicked);
    }

}
