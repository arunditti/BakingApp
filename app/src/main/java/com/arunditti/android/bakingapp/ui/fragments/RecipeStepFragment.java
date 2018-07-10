package com.arunditti.android.bakingapp.ui.fragments;

import android.graphics.BitmapFactory;
import android.net.Uri;
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
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by arunditti on 6/18/18.
 */

public class RecipeStepFragment extends Fragment implements Player.EventListener {

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
    private ExoPlayer mExoPlayer;
    private PlayerView mExoPlayerView;
    private MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;
    private long playbackPosition = 0;
    private boolean playWhenReady;

    private String mThumbnailUrl;
    private String mVideoUrl;
    private View mRootView;
    long mVideoCurrentPosition;

    //Mandatory empty constructor
    public RecipeStepFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {

        mRootView = inflater.inflate(R.layout.fragment_recipe_steps, container, false);

        if (saveInstanceState == null) {
            Intent intent = getActivity().getIntent();
            mCurrentRecipe = intent.getParcelableExtra(DETAILS_KEY);
            mClickedStepNumber = intent.getIntExtra(STEP_KEY, 0);
        } else {
            mCurrentRecipe = saveInstanceState.getParcelable(DETAILS_KEY);
            mClickedStepNumber = saveInstanceState.getInt(STEP_KEY);
            mVideoCurrentPosition = saveInstanceState.getLong(VIDEO_KEY);
        }

        mRecipeName = mCurrentRecipe.getRecipeName();
        mRecipeSteps = mCurrentRecipe.getRecipeSteps();

        mCurrentStepClicked = mRecipeSteps.get(mClickedStepNumber);

        mExoPlayerView = mRootView.findViewById(R.id.exo_player_view_step_video);
        mExoPlayerView.setDefaultArtwork(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_background));

        ProgressBar pbExoLoadingIndicator = mRootView.findViewById(R.id.pb_exo_loading_indicator);

        mButtonPreviousStep = mRootView.findViewById(R.id.button_previous_step);
        mButtonPreviousStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mClickedStepNumber == 0) {
                    return;
                }
                mClickedStepNumber--;
                Toast.makeText(getActivity(), "clicked step " + mClickedStepNumber, Toast.LENGTH_SHORT).show();
                mVideoCurrentPosition = playbackPosition;
                populateStepDetailUI(mClickedStepNumber);

            }
        });

        mButtonNextStep = mRootView.findViewById(R.id.button_next_step);
        mButtonNextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mClickedStepNumber == mRecipeSteps.size() - 1) {
                    return;
                }
                mClickedStepNumber++;
                Toast.makeText(getActivity(), "clicked step " + mClickedStepNumber, Toast.LENGTH_SHORT).show();
                mVideoCurrentPosition = playbackPosition;
                populateStepDetailUI(mClickedStepNumber);

            }
        });

        // Initialize the Media Session.
        initializeMediaSession();

        populateStepDetailUI(mClickedStepNumber);

        return mRootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(DETAILS_KEY, mCurrentRecipe);
        outState.putInt(STEP_KEY, mClickedStepNumber);
        if (mExoPlayer != null) {
            outState.putLong(VIDEO_KEY, mExoPlayer.getCurrentPosition());
        } else {
            outState.putLong(VIDEO_KEY, mVideoCurrentPosition);
        }
    }

    public void populateStepDetailUI(int clickedStepNumber) {

        mClickedStepNumber = clickedStepNumber;

        if (clickedStepNumber == 0) {
            mButtonPreviousStep.setEnabled(false);
            mButtonPreviousStep.setClickable(false);
        } else {
            mButtonPreviousStep.setEnabled(true);
            mButtonPreviousStep.setClickable(true);
        }

        if (clickedStepNumber == mRecipeSteps.size() - 1) {
            mButtonNextStep.setEnabled(false);
            mButtonNextStep.setClickable(false);
        } else {
            mButtonNextStep.setEnabled(true);
            mButtonNextStep.setClickable(true);
        }

        mCurrentStepClicked = mRecipeSteps.get(clickedStepNumber);
        TextView StepDescription = mRootView.findViewById(R.id.tv_step_description);
        StepDescription.setText(mCurrentStepClicked.getDescription());

        mThumbnail = mRootView.findViewById(R.id.iv_step_thumbnail);
        mThumbnailUrl = mCurrentStepClicked.getThumbnailUrl();
        Log.d(LOG_TAG, "***** Thumbnail url is: " + mThumbnailUrl);

        mVideoUrl = mCurrentStepClicked.getVideoUrl();
        Log.d(LOG_TAG, "***** Video url is: " + mVideoUrl);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle(mRecipeName + " Step " + clickedStepNumber);

        if (mVideoUrl.isEmpty()) {
            mExoPlayerView.setVisibility(View.GONE);
            releasePlayer();
            showThumbnail();
            mThumbnail.setVisibility(View.VISIBLE);
        } else {
            mThumbnail.setVisibility(View.GONE);
            mExoPlayerView.setVisibility(View.VISIBLE);

            //Release the player
            releasePlayer();

            // Initialize the player.
            initializePlayer(Uri.parse(mCurrentStepClicked.getVideoUrl()));
        }
    }

    public void showThumbnail() {
        if (mThumbnailUrl.isEmpty()) {
            mThumbnail.setImageResource(R.drawable.placeholder_image);
        } else {
            Picasso.with(getActivity())
                    .load(mThumbnailUrl)
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.ic_launcher_foreground)
                    .into(mThumbnail);
        }
    }

    //Initializes the Media Session to be enabled with media buttons, transport controls, callbacks and media controller.
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

    //Media Session Callbacks, where all external clients control the player.
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

    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(getActivity()), trackSelector, loadControl);

            mExoPlayerView.setPlayer(mExoPlayer);
            mExoPlayer.setPlayWhenReady(true);

            // Set ExoPlayer.EventListener to this activity
            mExoPlayer.addListener(this);

            // Prepare the MediaSource.
            Uri uri = Uri.parse(mCurrentStepClicked.getVideoUrl());
            MediaSource mediaSource = buildMediaSource(uri);
            mExoPlayer.prepare(mediaSource, true, false);
            mExoPlayer.seekTo(mVideoCurrentPosition);
        }
    }

    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource.Factory(new DefaultHttpDataSourceFactory(getString(R.string.app_name)))
                .createMediaSource(uri);
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

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

        if ((playbackState == Player.STATE_READY) && playWhenReady) {
            Log.d(LOG_TAG, "onPlayerStateChanged: PLAYING");
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    mExoPlayer.getCurrentPosition(), 1f);
        } else if ((playbackState == Player.STATE_READY)) {
            Log.d(LOG_TAG, "onPlayerStateChanged: PAUSED");
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    mExoPlayer.getCurrentPosition(), 1f);
        }
        mMediaSession.setPlaybackState(mStateBuilder.build());

    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity(int reason) {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSeekProcessed() {

    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer(Uri.parse(mCurrentStepClicked.getVideoUrl()));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Util.SDK_INT <= 23 || mExoPlayer == null) {
            initializePlayer(Uri.parse(mCurrentStepClicked.getVideoUrl()));
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    //Release the player when the activity is destroyed.
    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
        mMediaSession.setActive(false);
    }

    //Release ExoPlayer.
    private void releasePlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }
}
