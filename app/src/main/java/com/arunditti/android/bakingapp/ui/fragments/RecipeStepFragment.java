package com.arunditti.android.bakingapp.ui.fragments;

import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.arunditti.android.bakingapp.R;
import com.arunditti.android.bakingapp.model.Recipe;
import com.arunditti.android.bakingapp.model.RecipeStep;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by arunditti on 6/18/18.
 */

public class RecipeStepFragment extends Fragment {

    private static final String LOG_TAG = RecipeStepFragment.class.getSimpleName();

    private static final String DETAILS_KEY = "Recipe_parcel";
    private static final String STEP_KEY = "Recipe_step";

    private Recipe mCurrentRecipe;
    private String mRecipeName;
    private RecipeStep mCurrentStep;
    private RecipeStep mCurrentStepClicked;
    private ArrayList<RecipeStep> mRecipeSteps = new ArrayList<RecipeStep>();
    private int mClickedStepNumber;
    private Button mButtonPreviousStep;
    private Button mButtonNextStep;
    private ImageView mThumbnail;
    private SimpleExoPlayer mExoPlayer;
    private SimpleExoPlayerView mExoPlayerView;
    private String mThumbnailUrl;
    private String mVideoUrl;

    //Mandatory empty constructor
    public RecipeStepFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle onsaveInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_recipe_steps, container, false);

        final Intent intent = getActivity().getIntent();
        mCurrentRecipe = intent.getParcelableExtra(DETAILS_KEY);
        mCurrentStep = intent.getParcelableExtra(STEP_KEY);

        mRecipeName = mCurrentRecipe.getRecipeName();
        mRecipeSteps = mCurrentRecipe.getRecipeSteps();

        mClickedStepNumber = mCurrentStep.getId();
        mCurrentStepClicked = mRecipeSteps.get(mClickedStepNumber);

        mExoPlayerView = rootView.findViewById(R.id.exo_player_view_step_video);
        mExoPlayerView.setDefaultArtwork(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_background));

        TextView StepDescription = rootView.findViewById(R.id.tv_step_description);
        StepDescription.setText(mCurrentStepClicked.getDescription());

        mThumbnail = rootView.findViewById(R.id.iv_step_thumbnail);
        mThumbnailUrl = mCurrentStepClicked.getThumbnailUrl();
        mVideoUrl = mCurrentStepClicked.getVideoUrl();
        Log.d(LOG_TAG, "***** Thumbnail url is: " + mThumbnailUrl);
        Log.d(LOG_TAG, "***** Video url is: " + mVideoUrl);

        // Initialize the player.
       // initializePlayer(Uri.parse(thumbnailUrl));
        initializePlayer(Uri.parse(mCurrentStepClicked.getVideoUrl()));

        if(mThumbnailUrl.isEmpty()) {
            mThumbnail.setImageResource(R.drawable.ic_launcher_foreground);
        } else {
            Picasso.with(getActivity())
                    .load(mThumbnailUrl)
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .error(R.drawable.ic_launcher_background)
                    .into(mThumbnail);
        }

        mButtonPreviousStep = rootView.findViewById(R.id.button_previous_step);
        mButtonPreviousStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Code here to go to previous step
            }
        });

        mButtonNextStep = rootView.findViewById(R.id.button_next_step);
        mButtonNextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Code for next step comes here
            }
        });
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle(mRecipeName + " Step: " + mClickedStepNumber);

        return rootView;
    }

    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector, loadControl);
            mExoPlayerView.setPlayer(mExoPlayer);

            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(getActivity(), "BakingApp");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getActivity(), userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
        }
    }

    /**
     * Release ExoPlayer.
     */
    private void releasePlayer() {
        mExoPlayer.stop();
        mExoPlayer.release();
        mExoPlayer = null;
    }

    /**
     * Release the player when the activity is destroyed.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

}
