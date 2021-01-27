package com.example.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;

public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equalsIgnoreCase(Intent.ACTION_MEDIA_BUTTON))
        {
            KeyEvent event = (KeyEvent) intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
            if (event == null)
                return;

            if (event.getKeyCode() != KeyEvent.KEYCODE_HEADSETHOOK
                    && event.getKeyCode() != KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE
                    && event.getAction() != KeyEvent.ACTION_DOWN) {
                return;
            }

            Intent i = null;

            Log.e("MEDIA CONTROLLER","Power Button Play app destroy");
            switch (event.getKeyCode())
            {
                case KeyEvent.KEYCODE_HEADSETHOOK:
                    Log.e("MEDIA CONTROLLER","Power Button Play KEYCODE_HEADSETHOOK");
                    break;
                case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
                    Log.e("MEDIA CONTROLLER","Power Button Play MEDIA_PLAY_PAUSE");
                    switch (event.getAction())
                    {
                        case KeyEvent.ACTION_DOWN:
                            if (event.getRepeatCount() > 0)
                                break;

                            break;
                        case KeyEvent.ACTION_UP:
                            // long click
                            Log.e("MEDIA CONTROLLER","Power Button Play Action Up");
                            break;
                        case KeyEvent.ACTION_MULTIPLE:
                            Log.e("MEDIA", "Action Multiple");
                            break;
                    }
                    break;
                //The following is the received from PTT device button
                case KeyEvent.KEYCODE_MEDIA_PLAY:
                    Log.e("MEDIA CONTROLLER>>>","Button Play");
                    break;
                case KeyEvent.KEYCODE_MEDIA_PAUSE:
                    Log.e("MEDIA CONTROLLER","Power Button Pause");
                    break;
                case KeyEvent.KEYCODE_MEDIA_AUDIO_TRACK:
                    Log.e("MEDIA CONTROLLER","audio track");
                    break;
                case KeyEvent.KEYCODE_MEDIA_CLOSE:
                    Log.e("MEDIA CONTROLLER","close");
                    break;
                case KeyEvent.KEYCODE_MEDIA_EJECT:
                    Log.e("MEDIA CONTROLLER","eject");
                    break;
                case KeyEvent.KEYCODE_MEDIA_FAST_FORWARD:
                    Log.e("MEDIA CONTROLLER","fast forward");
                    break;
                case KeyEvent.KEYCODE_MEDIA_NEXT:
                    Log.e("MEDIA CONTROLLER","next");
                    break;
                case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
                    Log.e("MEDIA CONTROLLER","previous");
                    break;
                case KeyEvent.KEYCODE_MEDIA_RECORD:
                    Log.e("MEDIA CONTROLLER","record");
                    break;

                case KeyEvent.KEYCODE_MEDIA_REWIND:
                    Log.e("MEDIA CONTROLLER","rewind");
                    break;

                case KeyEvent.KEYCODE_MEDIA_STOP:
                    Log.e("MEDIA CONTROLLER","stop");
                    break;
            }

            if (isOrderedBroadcast())
                abortBroadcast();
            if (i != null)
                context.sendBroadcast(i);
        }
    }
}