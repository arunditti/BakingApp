package com.arunditti.android.bakingapp.ui.fragments;

import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.arunditti.android.bakingapp.R;
import com.arunditti.android.bakingapp.model.Recipe;
import com.arunditti.android.bakingapp.model.RecipeStep;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.SimpleTimeZone;

/**
 * Created by arunditti on 6/18/18.
 */

public class RecipeStepFragment extends Fragment implements ExoPlayer.EventListener {

    private static final String LOG_TAG = RecipeStepFragment.class.getSimpleName();

    private static final String DETAILS_KEY = "Recipe_parcel";
    private static final String STEP_KEY = "Recipe_step";
    private static final String VIDEO_KEY = "Video_key";
    private static final String STEP_NUMBER_KEY = "step_number";

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
    private MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;

    private String mThumbnailUrl;
    private String mVideoUrl;
    private View mRootView;
    long mVideoCurrentPosition;


    //Mandatory empty constructor
    public RecipeStepFragment() {

    }

    @Override
    public void onActivityCreated(@Nullable Bundle saveInstanceState) {

        super.onActivityCreated(saveInstanceState);

//        if(saveInstanceState != null ) {
//            mCurrentRecipe = saveInstanceState.getParcelable(DETAILS_KEY);
//            mCurrentStep = saveInstanceState.getParcelable(STEP_KEY);
//            mClickedStepNumber = saveInstanceState.getInt(STEP_NUMBER_KEY);
//            long mVideoCurrentPosition = saveInstanceState.getLong(VIDEO_KEY);
//            mExoPlayer.seekTo(mVideoCurrentPosition);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {

        mRootView = inflater.inflate(R.layout.fragment_recipe_steps, container, false);

        if(saveInstanceState == null) {
            Intent intent = getActivity().getIntent();
                mCurrentRecipe = intent.getParcelableExtra(DETAILS_KEY);
                //mCurrentStep = intent.getParcelableExtra(STEP_KEY);
                mClickedStepNumber = intent.getIntExtra(STEP_KEY, 0);
        } else {
            mCurrentRecipe = saveInstanceState.getParcelable(DETAILS_KEY);
           // mCurrentStep = saveInstanceState.getParcelable(STEP_KEY);
            mClickedStepNumber = saveInstanceState.getInt(STEP_KEY);
            mVideoCurrentPosition = saveInstanceState.getLong(VIDEO_KEY);
        }

        mRecipeName = mCurrentRecipe.getRecipeName();
        mRecipeSteps = mCurrentRecipe.getRecipeSteps();

       // mClickedStepNumber = mCurrentStep.getId();
        mCurrentStepClicked = mRecipeSteps.get(mClickedStepNumber);

        mExoPlayerView = mRootView.findViewById(R.id.exo_player_view_step_video);
        mExoPlayerView.setDefaultArtwork(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_background));

        ProgressBar pbExoLoadingIndicator = mRootView.findViewById(R.id.pb_exo_loading_indicator);

        mButtonPreviousStep = mRootView.findViewById(R.id.button_previous_step);
        mButtonPreviousStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickedStepNumber--;
                Toast.makeText(getActivity(), "clicked step " + mClickedStepNumber, Toast.LENGTH_SHORT).show();
                releasePlayer();
                populateStepDetailUI(mClickedStepNumber);
            }
        });

        mButtonNextStep = mRootView.findViewById(R.id.button_next_step);
        mButtonNextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickedStepNumber++;
                Toast.makeText(getActivity(), "clicked step " + mClickedStepNumber, Toast.LENGTH_SHORT).show();
                releasePlayer();
                populateStepDetailUI(mClickedStepNumber);
            }
        });
//
//        // Initialize the Media Session.
//        initializeMediaSession();
//
//        // Initialize the player.
//        initializePlayer(Uri.parse(mCurrentStepClicked.getVideoUrl()));

        populateStepDetailUI(mClickedStepNumber);

//                if(saveInstanceState != null ) {
//            mCurrentRecipe = saveInstanceState.getParcelable(DETAILS_KEY);
//            mCurrentStep = saveInstanceState.getParcelable(STEP_KEY);
//            mClickedStepNumber = saveInstanceState.getInt(STEP_NUMBER_KEY);
//            long mVideoCurrentPosition = saveInstanceState.getLong(VIDEO_KEY);
//            mExoPlayer.seekTo(mVideoCurrentPosition);
//        }

        return mRootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(DETAILS_KEY, mCurrentRecipe);
        //outState.putParcelable(STEP_KEY, mCurrentStep);
        outState.putInt(STEP_KEY, mClickedStepNumber);
        if(mExoPlayer != null) {
            outState.putLong(VIDEO_KEY, mExoPlayer.getCurrentPosition());
        } else {
            outState.putLong(VIDEO_KEY, mVideoCurrentPosition);
        }
    }

    public void populateStepDetailUI(int mClickedStepNumber) {

        if (mClickedStepNumber == 0) {
            mButtonPreviousStep.setEnabled(false);
            mButtonPreviousStep.setClickable(false);
        } else {
            mButtonPreviousStep.setEnabled(true);
            mButtonPreviousStep.setClickable(true);
        }

        if (mClickedStepNumber == mRecipeSteps.size() - 1) {
            mButtonNextStep.setEnabled(false);
            mButtonNextStep.setClickable(false);
        } else {
            mButtonNextStep.setEnabled(true);
            mButtonNextStep.setClickable(true);
        }

        mCurrentStepClicked = mRecipeSteps.get(mClickedStepNumber);
        TextView StepDescription = mRootView.findViewById(R.id.tv_step_description);
        StepDescription.setText(mCurrentStepClicked.getDescription());

        mThumbnail = mRootView.findViewById(R.id.iv_step_thumbnail);
        mThumbnailUrl = mCurrentStepClicked.getThumbnailUrl();
        Log.d(LOG_TAG, "***** Thumbnail url is: " + mThumbnailUrl);

        mVideoUrl = mCurrentStepClicked.getVideoUrl();
        Log.d(LOG_TAG, "***** Video url is: " + mVideoUrl);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle(mRecipeName + " Step: " + mClickedStepNumber);

        //Initialize the Media Session.
        initializeMediaSession();

        initializePlayer(Uri.parse(mCurrentStepClicked.getVideoUrl()));

        if (mVideoUrl.isEmpty()) {
            mExoPlayerView.setVisibility(View.GONE);
            showThumbnail();
        } else {
            mThumbnail.setVisibility(View.GONE);
            mExoPlayerView.setVisibility(View.VISIBLE);
        }

    }

    /**
     * Initializes the Media Session to be enabled with media buttons, transport controls, callbacks
     * and media controller.
     */
    private void initializeMediaSession() {

        // Create a MediaSessionCompat.
        mMediaSession = new MediaSessionCompat(getActivity(), LOG_TAG);

        // Enable callbacks from MediaButtons and TransportControls.
        mMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        // Do not let MediaButtons restart the player when the app is not visible.
        mMediaSession.setMediaButtonReceiver(null);

        // Set an initial PlaybackState with ACTION_PLAY, so media buttons can start the player.
        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mMediaSession.setPlaybackState(mStateBuilder.build());

        // MySessionCallback has methods that handle callbacks from a media controller.
        mMediaSession.setCallback(new MySessionCallback());

        // Start the Media Session since the activity is active.
        mMediaSession.setActive(true);

    }

    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector, loadControl);
            mExoPlayerView.setPlayer(mExoPlayer);

            // Set ExoPlayer.EventListener to this activity
            mExoPlayer.addListener(this);

            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(getActivity(), "BakingApp");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getActivity(), userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
            mExoPlayer.seekTo( mVideoCurrentPosition);
        }
    }

    public void showThumbnail() {
        if (mThumbnailUrl.isEmpty()) {
            mThumbnail.setImageResource(R.drawable.ic_launcher_foreground);
        } else {
            Picasso.with(getActivity())
                    .load(mThumbnailUrl)
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_foreground)
                    .into(mThumbnail);
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
        mMediaSession.setActive(false);
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        ProgressBar pbVideoLoadingIndicator = mRootView.findViewById(R.id.pb_exo_loading_indicator);

        if ((playbackState == ExoPlayer.STATE_READY) && playWhenReady) {
            Log.d(LOG_TAG, "onPlayerStateChanged: PLAYING");
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    mExoPlayer.getCurrentPosition(), 1f);
        } else if ((playbackState == ExoPlayer.STATE_READY)) {
            Log.d(LOG_TAG, "onPlayerStateChanged: PAUSED");
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    mExoPlayer.getCurrentPosition(), 1f);
        }

        mMediaSession.setPlaybackState(mStateBuilder.build());
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }

    /**
     * Media Session Callbacks, where all external clients control the player.
     */
    private class MySessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            mExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            mExoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            mExoPlayer.seekTo(0);
        }
    }

}
