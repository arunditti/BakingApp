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

        if(mRecipe.get(position).recipeImage.isEmpty()) {
            holder.recipeImage.setImageResource(R.drawable.ic_launcher_foreground);
        } else {
            Picasso.with(mContext)
                    .load(mRecipe.get(position).recipeImage)
                    .placeholder(R.drawable.ic_launcher_background)
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

        CardView cardView;
        ImageView recipeImage;
        TextView recipeName;

        public RecipeAdapterViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cv);
            recipeImage = itemView.findViewById(R.id.recipe_image);
            recipeName = itemView.findViewById(R.id.recipe_name);

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
