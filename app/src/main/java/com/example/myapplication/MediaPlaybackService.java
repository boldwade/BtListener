//package com.example.myapplication;
//
//import android.content.Intent;
//import android.media.session.MediaSession;
//import android.os.IBinder;
//
//import androidx.annotation.Nullable;
//
//public class MediaPlaybackService extends android.app.Service {
//
//    private MediaSession mMediaSession;
//
//    @Nullable
//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        MediaButtonReceiver.handleIntent(mMediaSessionCompat, intent);
//        return super.onStartCommand(intent, flags, startId);
//    }
//}
