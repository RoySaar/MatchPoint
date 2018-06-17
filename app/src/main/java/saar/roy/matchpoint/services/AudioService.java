package saar.roy.matchpoint.services;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;

import saar.roy.matchpoint.R;
import saar.roy.matchpoint.services.Jukebox;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class AudioService extends Service {

    static MediaPlayer mediaPlayer;
    Jukebox jukebox;

    ArrayList<Integer> requestedTracks;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        jukebox = new Jukebox();
    }

    MediaPlayer createJukeboxPlayer(final List<Integer> tracks) {
        MediaPlayer mPlayer = MediaPlayer.create(this, jukebox.getRandomTrack(tracks));

        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mediaPlayer.release();
                mediaPlayer = createJukeboxPlayer(tracks);
            }
        });

        return mPlayer;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        requestedTracks = intent.getExtras().getIntegerArrayList("tracks");

        mediaPlayer = createJukeboxPlayer(requestedTracks);
        mediaPlayer.start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        mediaPlayer.stop();
        mediaPlayer.release();

        super.onDestroy();
    }
}
