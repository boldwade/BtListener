package com.example.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;

public class MediaButtonReceiver extends BroadcastReceiver {
    private static String TAG = "MediaButtonReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        KeyEvent event = intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);

        if (Intent.ACTION_MEDIA_BUTTON.equals(action)) {

            // Get the key code
            int keycode = event.getKeyCode();

            switch (keycode) {
                case KeyEvent.KEYCODE_MEDIA_NEXT:
                    //Play the next one
                    break;
                case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
                    //Play the previous one
                    break;
                case KeyEvent.KEYCODE_HEADSETHOOK:
                    //You can pause or play the video by sending a new broadcast notification to the video page being played.
                    break;
                default:
                    break;
            }
        }
    }
}
