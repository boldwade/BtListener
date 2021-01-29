package com.example.myapplication;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.media.Rating;
import android.media.session.MediaSession;
import android.net.Uri;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.KeyEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class BluetoothProfileListener {
    Context context;
    BluetoothAdapter bluetoothAdapter;
    BluetoothHeadset bluetoothHeadset;

    public BluetoothAdapter getBluetoothAdapter() {
        return bluetoothAdapter;
    }

    public BluetoothProfileListener(Context context) {
        this.context = context;
    }

    public boolean SetupBluetoothAdapter() {
        Log.d("setupBluetoothAdapter", "On Create");
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Log.e(this.getClass().getName(), "BT Permission not available");
//            Snackbar.make(mLayout, R.string.bt_permission_not_available, Snackbar.LENGTH_SHORT).show();
            return false;
        }
        bluetoothAdapter.getProfileProxy(context, profileListener, BluetoothProfile.HEADSET);
        return true;
    }

    private final MediaSession.Callback mediaSessionCallback = new MediaSession.Callback() {
        @Override
        public void onPlay() {
            super.onPlay();
        }

        @Override
        public void onCommand(@NonNull String command, @Nullable Bundle args, @Nullable ResultReceiver cb) {
            super.onCommand(command, args, cb);
        }

        @Override
        public void onCustomAction(@NonNull String action, @Nullable Bundle extras) {
            super.onCustomAction(action, extras);
        }

        @Override
        public void onPause() {
            super.onPause();
        }

        @Override
        public void onPrepare() {
            super.onPrepare();
        }

        @Override
        public void onPrepareFromMediaId(String mediaId, Bundle extras) {
            super.onPrepareFromMediaId(mediaId, extras);
        }

        @Override
        public void onPrepareFromSearch(String query, Bundle extras) {
            super.onPrepareFromSearch(query, extras);
        }

        @Override
        public void onPrepareFromUri(Uri uri, Bundle extras) {
            super.onPrepareFromUri(uri, extras);
        }

        @Override
        public void onPlayFromSearch(String query, Bundle extras) {
            super.onPlayFromSearch(query, extras);
        }

        @Override
        public void onPlayFromMediaId(String mediaId, Bundle extras) {
            super.onPlayFromMediaId(mediaId, extras);
        }

        @Override
        public void onPlayFromUri(Uri uri, Bundle extras) {
            super.onPlayFromUri(uri, extras);
        }

        @Override
        public void onSkipToQueueItem(long id) {
            super.onSkipToQueueItem(id);
        }

        @Override
        public void onSkipToNext() {
            super.onSkipToNext();
        }

        @Override
        public void onSkipToPrevious() {
            super.onSkipToPrevious();
        }

        @Override
        public void onFastForward() {
            super.onFastForward();
        }

        @Override
        public void onRewind() {
            super.onRewind();
        }

        @Override
        public void onStop() {
            super.onStop();
        }

        @Override
        public void onSeekTo(long pos) {
            super.onSeekTo(pos);
        }

        @Override
        public void onSetRating(@NonNull Rating rating) {
            super.onSetRating(rating);
        }

        @Override
        public void onSetPlaybackSpeed(float speed) {
            super.onSetPlaybackSpeed(speed);
        }



        @Override
        public boolean onMediaButtonEvent(@NonNull Intent mediaButtonIntent) {
//            String a = mediaButtonIntent.getAction();
//            if (Intent.EXTRA_KEY_EVENT == a) {
//                KeyEvent ev = mediaButtonIntent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
//                if (ev != null) {
//                    boolean action = ev.getAction() == KeyEvent.ACTION_DOWN;
//                }
//            }
//            if (Intent.ACTION_MEDIA_BUTTON == a) {
//                Bundle bundle = mediaButtonIntent.getExtras();
//                Object p = bundle.getParcelable(Intent.ACTION_MEDIA_BUTTON);
//                KeyEvent ev = mediaButtonIntent.getParcelableExtra(Intent.ACTION_MEDIA_BUTTON);
//                if (ev != null) {
//                    boolean action = ev.getAction() == KeyEvent.ACTION_DOWN;
//                }
//            }
            return super.onMediaButtonEvent(mediaButtonIntent);
        }
    };

    private final BluetoothProfile.ServiceListener profileListener = new BluetoothProfile.ServiceListener() {
        public void onServiceConnected(int profile, BluetoothProfile proxy) {
            if (profile == BluetoothProfile.HEADSET) {
//                MediaRecorder mediaRecorder = new MediaRecorder()
                MediaSession mediaSession = new MediaSession(context, this.getClass().getName());
                mediaSession.setCallback(mediaSessionCallback);

                bluetoothHeadset = (BluetoothHeadset) proxy;

//                AudioTrack at = new AudioTrack(AudioManager.STREAM_MUSIC, 48000, AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_16BIT,
//                        AudioTrack.getMinBufferSize(48000, AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_16BIT), AudioTrack.MODE_STREAM);
//                at.play();

//                try {
//                    microphoneRecorder = new AudioRecord(MediaRecorder.AudioSource.MIC, 16000, AudioFormat.CHANNEL_IN_DEFAULT, AudioFormat.ENCODING_PCM_16BIT, bufferSize);
//                    List<MicrophoneInfo> activeMics = microphoneRecorder.getActiveMicrophones();
//                    Log.d("ActiveMics", String.valueOf(activeMics.size()));
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }

                mediaSession.setActive(true);
            }
        }

        public void onServiceDisconnected(int profile) {
            if (profile == BluetoothProfile.HEADSET) {
                Log.d(this.getClass().getName(), "On Service Disconnected: " + profile);
//                bluetoothHeadset = null;
            }
        }
    };

}
