package com.arunditti.android.bakingapp.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arunditti.android.bakingapp.R;
import com.arunditti.android.bakingapp.model.RecipeStep;

import java.util.ArrayList;

/**
 * Created by arunditti on 6/18/18.
 */

public class RecipeStepsAdapter extends RecyclerView.Adapter<RecipeStepsAdapter.RecipeStepsAdapterViewHolder> {

    private static final String LOG_TAG = RecipeAdapter.class.getSimpleName();

    private ArrayList<RecipeStep> mRecipeStep;
    private final Context mContext;
    private final RecipeStepsAdapter.RecipeStepsAdapterOnClickHandler mClickHandler;

    public interface RecipeStepsAdapterOnClickHandler {
        void onClick(RecipeStep recipeStepClicked);
    }

    public RecipeStepsAdapter(Context context, RecipeStepsAdapter.RecipeStepsAdapterOnClickHandler mClickHandler, ArrayList<RecipeStep> recipeSteps) {
        this.mContext = context;
        this.mClickHandler = mClickHandler;
        this.mRecipeStep = recipeSteps;
    }

    @NonNull
    @Override
    public RecipeStepsAdapter.RecipeStepsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        //Get the RecyclerView item layout
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recipe_step_item, parent, false);
        return new RecipeStepsAdapter.RecipeStepsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeStepsAdapter.RecipeStepsAdapterViewHolder holder, int position) {

        int recipeNumber = mRecipeStep.get(position).getId();
        String recipeDescription = mRecipeStep.get(position).getShortDescription();

        StringBuffer stringBuffer = new StringBuffer();

        stringBuffer.append("Step: ")
                .append(recipeNumber + " ")
                .append(recipeDescription);
        String steps = stringBuffer.toString();
        holder.recipeShortDescription.setText(steps);

        //holder.recipeShortDescription.setText(mRecipeStep.get(position).getShortDescription());
    }

    @Override
    public int getItemCount() {
        return this.mRecipeStep.size();
    }

    public class RecipeStepsAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CardView cardView;
        TextView recipeShortDescription;

        public RecipeStepsAdapterViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cv_steps);
            recipeShortDescription = itemView.findViewById(R.id.recipe_short_Description);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            RecipeStep recipeStepClicked = mRecipeStep.get(clickedPosition);
            mClickHandler.onClick(recipeStepClicked);
        }
    }

    public void updateRecipeStepsList(ArrayList<RecipeStep> recipeStep) {
        this.mRecipeStep = recipeStep;
        this.notifyDataSetChanged();
    }

}
