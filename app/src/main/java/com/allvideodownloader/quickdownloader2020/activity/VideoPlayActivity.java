package com.allvideodownloader.quickdownloader2020.activity;

import android.app.Activity;
import android.app.PictureInPictureParams;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Rational;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.allvideodownloader.quickdownloader2020.R;
import com.allvideodownloader.quickdownloader2020.util.Utils;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player.EventListener;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.LoopingMediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.measurement.api.AppMeasurementSdk.ConditionalUserProperty;

public class VideoPlayActivity extends AppCompatActivity {
    PictureInPictureParams.Builder pipBuilder;
    ProgressBar loader;
    SimpleExoPlayerView simpleExoPlayerView;
    private Boolean playing;

    private ImageView imBack, ivPip;
    private String name;
    private SimpleExoPlayer player;
    private TextView videoName;
    private String videourl;
    private Activity context;

    public VideoPlayActivity() {
        playing = false;
    }


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_play);

        context = this;

        initView();

//        // Initialize ads
//        Utils.onlyInitializeAds(context);
//        // Show banner Ads
//        Utils.showBannerAds(context);

        Intent intent = getIntent();
        if (intent.getExtras() != null) {

            name = intent.getStringExtra(ConditionalUserProperty.NAME);
            videourl = intent.getStringExtra("path");

            initplayer();

        }


        imBack.setOnClickListener(view ->

                onBackPressed());

        ivPip.setOnClickListener(view ->

                startPipWindow());
    }

    private void initView() {
        imBack = findViewById(R.id.back);
        videoName = findViewById(R.id.video_name);
        loader = findViewById(R.id.loader);
        ivPip = findViewById(R.id.img_pictureinpicture);

    }

    private void initplayer() {

        videoName.setText(name);
        ThumbnailUtils.createVideoThumbnail(videourl, 1);
        Uri parse = Uri.parse(videourl);
        player = ExoPlayerFactory.newSimpleInstance(this, new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(new DefaultBandwidthMeter())));
        simpleExoPlayerView = findViewById(R.id.video_view);
        simpleExoPlayerView.setPlayer(player);
        ExtractorMediaSource extractorMediaSource = new ExtractorMediaSource(parse, new DefaultDataSourceFactory(this, Util.getUserAgent(this, "CloudinaryExoplayer")), new DefaultExtractorsFactory(), null, null);
        player.prepare(new LoopingMediaSource(extractorMediaSource));
        player.setPlayWhenReady(true);
        simpleExoPlayerView.setControllerShowTimeoutMs(ConnectionResult.DRIVE_EXTERNAL_STORAGE_REQUIRED);
        player.addListener(new EventListener() {
            @Override
            public void onLoadingChanged(boolean z) {
            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
            }

            @Override
            public void onPlayerError(ExoPlaybackException exoPlaybackException) {
            }

            @Override
            public void onPositionDiscontinuity(int i) {
            }

            @Override
            public void onRepeatModeChanged(int i) {
            }

            @Override
            public void onShuffleModeEnabledChanged(boolean z) {
            }

            @Override
            public void onTimelineChanged(Timeline timeline, Object obj, int i) {
            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroupArray, TrackSelectionArray trackSelectionArray) {
            }

            @Override
            public void onPlayerStateChanged(boolean z, int i) {
                if (i == 3) {
                    try {
                        loader.setVisibility(View.GONE);
                        simpleExoPlayerView.setVisibility(View.VISIBLE);
                        playing = Boolean.valueOf(true);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return;
                    }
                }
                if (i == 2) {
                    loader.setVisibility(View.VISIBLE);
                    simpleExoPlayerView.setVisibility(View.GONE);
                }
            }
        });


        simpleExoPlayerView.setOnTouchListener(new View.OnTouchListener() {
            private GestureDetector gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {

                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                    super.onFling(e1, e2, velocityX, velocityY);


                    return true;
                }

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    super.onSingleTapUp(e);

                    if (!player.getPlayWhenReady()) {
                        player.setPlayWhenReady(true);
                    } else {

                        new Handler().postDelayed(() -> player.setPlayWhenReady(false), 200);
                    }


                    return true;
                }


            });

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (player == null && playing.booleanValue()) {
            initplayer();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releaseplayer();
    }

    @Override
    public void onPause() {
        super.onPause();

        // If called while in PIP mode, do not pause playback
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (isInPictureInPictureMode()) {
                // Continue playback

            } else {
                // Use existing playback logic for paused Activity behavior.
                releaseplayer();
            }
        }

    }

    private void releaseplayer() {
        SimpleExoPlayer simpleExoPlayer = player;
        if (simpleExoPlayer != null) {
            simpleExoPlayer.release();
            player = null;

        }
    }

    @Override
    public void onStop() {
        super.onStop();
        releaseplayer();
    }


    @Override
    protected void onNewIntent(Intent intent) {

        name = intent.getStringExtra(ConditionalUserProperty.NAME);
        videourl = intent.getStringExtra("videourl");

        releaseplayer();
        initplayer();

        super.onNewIntent(intent);
    }

    private void startPipWindow() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            pipBuilder = new PictureInPictureParams.Builder();

            Rational aspectRatio = new Rational(16, 9);

            pipBuilder.setAspectRatio(aspectRatio).build();

            enterPictureInPictureMode(pipBuilder.build());
        }
    }

    @Override
    public void onUserLeaveHint() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (!isInPictureInPictureMode()) {
                Rational aspectRatio = new Rational(simpleExoPlayerView.getWidth(), simpleExoPlayerView.getHeight());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    try {
                        pipBuilder.setAspectRatio(aspectRatio).build();

                        enterPictureInPictureMode(pipBuilder.build());

                        releaseplayer();
                    } catch (Exception e) {
                        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    @Override
    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode,
                                              Configuration newConfig) {
        if (isInPictureInPictureMode) {

            Toast.makeText(context, "Pip Mode On", Toast.LENGTH_SHORT).show();

        }
    }


}
