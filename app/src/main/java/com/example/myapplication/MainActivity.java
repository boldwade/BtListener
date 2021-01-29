package com.example.myapplication;

import android.Manifest;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallback;

public class MainActivity extends AppCompatActivity implements OnRequestPermissionsResultCallback {
    private View mLayout;
    private BluetoothProfileListener bluetoothProfileListener;
    private MyReceiver myReceiver;

    // Requesting permission to RECORD_AUDIO
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private boolean permissionToRecordAccepted = false;
    private String[] permissions = {
            Manifest.permission.ACCESS_BACKGROUND_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission_group.ACTIVITY_RECOGNITION,
            Manifest.permission_group.MICROPHONE,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLayout = findViewById(R.id.main_layout);

        RegisterReceiver();
//        bluetoothProfileListener = new BluetoothProfileListener(getApplicationContext());
//        if (!bluetoothProfileListener.SetupBluetoothAdapter()) {
//            Log.e("setupBluetoothAdapter", "NO bluetooth adapter found!");
//            return;
//        }
//
//        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);

        //        myReceiver = new MyReceiver();
    }

    private void RegisterReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.bluetooth.headset.action.VENDOR_SPECIFIC_HEADSET_EVENT");
        filter.addAction("android.intent.extra.KEY_EVENT");
        filter.addAction("android.intent.action.MEDIA_BUTTON");
        filter.addAction("android.bluetooth.adapter.action.STATE_CHANGED");
        filter.addAction("android.bluetooth.device.action.BOND_STATE_CHANGED");
        filter.addAction("android.bluetooth.a2dp.profile.action.CONNECTION_STATE_CHANGED");
        getApplicationContext().registerReceiver(new MyReceiver(), filter);
    }

    @Override
    protected void onResume() {
        RegisterReceiver();
        super.onResume();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // BEGIN_INCLUDE(onRequestPermissionsResult)
        Log.d(getPackageName(), "onRequestPermissionsResult: " + requestCode);
//        if (requestCode == PERMISSION_REQUEST_BT) {
//            // Request for camera permission.
//            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                // Permission has been granted. Start camera preview Activity.
//                Snackbar.make(mLayout, R.string.bt_permission_granted, Snackbar.LENGTH_SHORT).show();
////                startCamera();
//            } else {
//                // Permission request was denied.
//                Snackbar.make(mLayout, R.string.bt_permission_denied, Snackbar.LENGTH_SHORT).show();
//            }
//        }
        // END_INCLUDE(onRequestPermissionsResult)
    }

}