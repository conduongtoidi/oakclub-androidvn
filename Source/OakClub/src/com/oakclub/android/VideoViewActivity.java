package com.oakclub.android;

import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;
import android.os.Bundle;
import android.widget.Toast;

import com.oakclub.android.base.OakClubBaseActivity;

public class VideoViewActivity extends OakClubBaseActivity {
    
    private String path = "";
    private VideoView mVideoView;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        if (!LibsChecker.checkVitamioLibs(this))
            return;
        setContentView(R.layout.activity_video_player);
        mVideoView = (VideoView) findViewById(R.id.activity_video_player_surface_view);

        path = getIntent().getStringExtra("url_video");
        if (path == "") {
            Toast.makeText(this, "Please edit VideoViewDemo Activity, and set path" + " variable to your media file URL/path", Toast.LENGTH_LONG).show();
            return;
        } else {
            mVideoView.setVideoPath(path);
            mVideoView.setMediaController(new MediaController(this));
            mVideoView.requestFocus();

            mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.setPlaybackSpeed(1.0f);
                }
            });
        }

    }
}
