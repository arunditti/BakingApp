package com.arunditti.android.bakingapp.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.arunditti.android.bakingapp.R;
import com.arunditti.android.bakingapp.model.Recipe;
import com.arunditti.android.bakingapp.ui.fragments.MainActivityFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by arunditti on 6/12/18.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeAdapterViewHolder> {

    private static final String LOG_TAG = RecipeAdapter.class.getSimpleName();

    private ArrayList<Recipe> mRecipe;
    private final Context mContext;
    private final RecipeAdapterOnClickHandler mClickHandler;

    public interface RecipeAdapterOnClickHandler {
        void onClick(Recipe recipeClicked);
    }

    public RecipeAdapter(Context context, RecipeAdapterOnClickHandler mClickHandler, ArrayList<Recipe> recipe) {
        this.mContext = context;
        this.mClickHandler = mClickHandler;
        this.mRecipe = recipe;
    }

    @NonNull
    @Override
    public RecipeAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        //Get the RecyclerView item layout
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recipe_list_item, parent, false);
        return new RecipeAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeAdapterViewHolder holder, int position) {
        holder.recipeName.setText(mRecipe.get(position).recipeName);
        int recipePicture;

        //Downloaded these pics from pixabay.com
        switch(mRecipe.get(position).recipeName) {
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

        if(mRecipe.get(position).recipeImage.isEmpty()) {
            Picasso.with(mContext)
                    .load(recipePicture)
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.ic_launcher_foreground)
                    .fit()
                    .into(holder.recipeImage);
        } else {
            Picasso.with(mContext)
                    .load(mRecipe.get(position).recipeImage)
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.ic_launcher_foreground)
                    .fit()
                    .into(holder.recipeImage);
        }
    }

    @Override
    public int getItemCount() {
        return this.mRecipe.size();
    }

    public class RecipeAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.cv_list) CardView cardView;
        @BindView(R.id.recipe_name) TextView recipeName;
        @BindView(R.id.recipe_image) ImageView recipeImage;

        public RecipeAdapterViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            Recipe recipeClicked = mRecipe.get(clickedPosition);
            mClickHandler.onClick(recipeClicked);
        }
    }

    public void updateRecipeList(ArrayList<Recipe> recipe) {
        this.mRecipe = recipe;
        this.notifyDataSetChanged();
    }

}
