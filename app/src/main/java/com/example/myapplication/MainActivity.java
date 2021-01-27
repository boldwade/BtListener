package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.BluetoothSocket;
import android.companion.AssociationRequest;
import android.companion.BluetoothDeviceFilter;
import android.companion.CompanionDeviceManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.session.MediaSession;
import android.media.session.PlaybackState;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {
    private CompanionDeviceManager deviceManager;
    private AssociationRequest pairingRequest;
    private BluetoothDeviceFilter deviceFilter;
    private View mLayout;
    private final ArrayList deviceList = new ArrayList<BluetoothDevice>();
    BluetoothHeadset bluetoothHeadset;

    private BluetoothAdapter bluetoothAdapter;
    private byte[] mmBuffer = new byte[1024];
    private InputStream in;
    private BluetoothSocket socket;
    private boolean isRunning = true;
    private BluetoothPtt.OnMessageReceived pttState = null;

    MyReceiver myReceiver;

    private static final int SELECT_DEVICE_REQUEST_CODE = 42;
    private static final int REQUEST_ENABLE_BT = 0;
    private static final int REQUEST_DISCOVERABLE_BT = 0;
    private static final int PERMISSION_REQUEST_BT = 3;

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
        public boolean onMediaButtonEvent(@NonNull Intent mediaButtonIntent) {
            String a = mediaButtonIntent.getAction();
            if (Intent.EXTRA_KEY_EVENT == a) {
                KeyEvent ev = mediaButtonIntent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
                if (ev != null) {
                    boolean action = ev.getAction() == KeyEvent.ACTION_DOWN;
                }
            }
            if (Intent.ACTION_MEDIA_BUTTON == a) {
                KeyEvent ev = mediaButtonIntent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
                if (ev != null) {
                    boolean action = ev.getAction() == KeyEvent.ACTION_DOWN;
                }
            }
            return super.onMediaButtonEvent(mediaButtonIntent);
        }
    };

    private final BluetoothProfile.ServiceListener profileListener = new BluetoothProfile.ServiceListener() {
        public void onServiceConnected(int profile, BluetoothProfile proxy) {
            if (profile == BluetoothProfile.HEADSET) {
                MediaSession mediaSession = new MediaSession(getApplicationContext(), getPackageName());
                mediaSession.setCallback(mediaSessionCallback);

                AudioTrack at = new AudioTrack(AudioManager.STREAM_MUSIC, 48000, AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_16BIT,
                        AudioTrack.getMinBufferSize(48000, AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_16BIT), AudioTrack.MODE_STREAM);
                at.play();

                mediaSession.setActive(true);

//                BluetoothGattCallback cb = new BluetoothGattCallback() {
//                    @Override
//                    public void onPhyRead(BluetoothGatt gatt, int txPhy, int rxPhy, int status) {
//                        super.onPhyRead(gatt, txPhy, rxPhy, status);
//                    }
//
//                    @Override
//                    public void onPhyUpdate(BluetoothGatt gatt, int txPhy, int rxPhy, int status) {
//                        super.onPhyUpdate(gatt, txPhy, rxPhy, status);
//                    }
//                };
//
//                bluetoothHeadset = (BluetoothHeadset) proxy;
//                List<BluetoothDevice> devices = bluetoothHeadset.getConnectedDevices();
//                Log.d(getPackageName(), "onServiceConnected: " + devices.size());

//                devices.forEach(bluetoothDevice -> bluetoothDevice.connectGatt(getApplicationContext(), true, cb));
            }
        }

        public void onServiceDisconnected(int profile) {
            if (profile == BluetoothProfile.HEADSET) {
                bluetoothHeadset = null;
            }
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLayout = findViewById(R.id.main_layout);
        final Button btnTurnOn = findViewById(R.id.btnTurnOn);
        final Button btnTurnOff = findViewById(R.id.btnTurnOff);
        final Button btnDiscover = findViewById(R.id.btnDiscoverable);

        myReceiver = new MyReceiver();

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Snackbar.make(mLayout, R.string.bt_permission_not_available, Snackbar.LENGTH_SHORT).show();
            return;
        }

        bluetoothAdapter.getProfileProxy(getApplicationContext(), profileListener, BluetoothProfile.HEADSET);

//        AudioManager audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
//        audioManager.setMode(AudioManager.MODE_NORMAL);
//        audioManager.setBluetoothScoOn(true);

        //        this.takeKeyEvents(true);
//
//        MediaSession mediaSession = new MediaSession(getApplicationContext(), getPackageName());
//        mediaSession.setCallback(mediaSessionCallback);
//        mediaSession.setFlags(MediaSession.FLAG_HANDLES_MEDIA_BUTTONS | MediaSession.FLAG_HANDLES_TRANSPORT_CONTROLS);
//
//        AudioTrack at = new AudioTrack(AudioManager.STREAM_MUSIC, 48000, AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_16BIT,
//                AudioTrack.getMinBufferSize(48000, AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_16BIT), AudioTrack.MODE_STREAM);
//        at.play();
//
//        mediaSession.setActive(true);

//        at.stop();
//        at.release();


//        btnTurnOn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                turnOnBluetooth();
//            }
//        });
//
//        btnTurnOff.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View arg0) {
//                turnOffBluetooth();
////                bluetoothAdapter.disable();
//////            out.append("TURN_OFF BLUETOOTH");
////                Context context = getApplicationContext();
////                CharSequence text = "TURNING_OFF BLUETOOTH";
////                Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
////                toast.show();
//            }
//        });

        btnDiscover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                discover();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        intentFilter.addAction("android.bluetooth.headset.action.VENDOR_SPECIFIC_HEADSET_EVENT");
        intentFilter.addAction("android.intent.extra.KEY_EVENT");
        intentFilter.addAction("android.intent.action.MEDIA_BUTTON");

        registerReceiver(myReceiver, intentFilter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // BEGIN_INCLUDE(onRequestPermissionsResult)
        if (requestCode == PERMISSION_REQUEST_BT) {
            // Request for camera permission.
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission has been granted. Start camera preview Activity.
                Snackbar.make(mLayout, R.string.bt_permission_granted, Snackbar.LENGTH_SHORT).show();
//                startCamera();
            } else {
                // Permission request was denied.
                Snackbar.make(mLayout, R.string.bt_permission_denied, Snackbar.LENGTH_SHORT).show();
            }
        }
        // END_INCLUDE(onRequestPermissionsResult)
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == SELECT_DEVICE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // User has chosen to pair with the Bluetooth device.
            BluetoothDevice deviceToPair = data.getParcelableExtra(CompanionDeviceManager.EXTRA_DEVICE);
            deviceToPair.createBond();

            // ... Continue interacting with the paired device.
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void turnOnBluetooth() {
        if (bluetoothAdapter == null) {
            // Device doesn't support Bluetooth
            Snackbar.make(mLayout, R.string.bt_permission_not_available, Snackbar.LENGTH_SHORT).show();
            return;
        }
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
//        // Permission has not been granted and must be requested.
//        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
//            // Provide an additional rationale to the user if the permission was not granted
//            // and the user would benefit from additional context for the use of the permission.
//            // Display a SnackBar with cda button to request the missing permission.
//            Snackbar.make(mLayout, R.string.bt_access_required, Snackbar.LENGTH_INDEFINITE).setAction(R.string.ok, new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    // Request the permission
//                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, PERMISSION_REQUEST_BT);
//                }
//            }).show();
//
//        } else {
//            Snackbar.make(mLayout, R.string.bt_unavailable, Snackbar.LENGTH_SHORT).show();
//            // Request the permission. The result will be received in onRequestPermissionResult().
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, PERMISSION_REQUEST_BT);
//        }

//        if (!mBluetoothAdapter.isEnabled()) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                    ActivityCompat.requestPermissions(
//                            MainActivity.this,
//                            new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION},
//                            PERMISSION_CODE);
//                }
//            }
//            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
//        }
    }

    private void turnOffBluetooth() {
        bluetoothAdapter.closeProfileProxy(BluetoothProfile.HEADSET, bluetoothHeadset);
        bluetoothAdapter.disable();
        Toast.makeText(getApplicationContext(), "Bluetooth Turned OFF", Toast.LENGTH_SHORT).show();
    }

    private void discover() {
        bluetoothAdapter.getProfileProxy(getApplicationContext(), profileListener, BluetoothProfile.HEADSET);

//        BluetoothDevice device = bluetoothHeadset.getConnectedDevices().get(0);

//        bluetoothAdapter.closeProfileProxy(BluetoothProfile.HEADSET, bluetoothHeadset);


//        if (bluetoothAdapter == null) {
//            Toast.makeText(getApplicationContext(), "Bluetooth Not Supported", Toast.LENGTH_SHORT).show();
//
//        } else {
//            Set<BluetoothDevice> bondedDevices = bluetoothAdapter.getBondedDevices();
//            if (bondedDevices.size() > 0) {
//                for (BluetoothDevice device : bondedDevices) {
//                    if (device.getName().contains("PTT")) {
//                        try {
////                              socket = device.createRfcommSocketToServiceRecord(UUID.fromString("00001800-0000-1000-8000-00805f9b34fb"));
////                              socket = device.createRfcommSocketToServiceRecord(UUID.fromString("000001801-0000-1000-8000-00805f9b34fb"));
////                              socket = device.createRfcommSocketToServiceRecord(UUID.fromString("0000ffe0-0000-1000-8000-00805f9b34fb"));
////                            socket = device.createRfcommSocketToServiceRecord(UUID.fromString("0000ffe1-0000-1000-8000-00805f9b34fb"));
//
////                              socket = device.createInsecureRfcommSocketToServiceRecord(UUID.fromString("00001800-0000-1000-8000-00805f9b34fb"));
////                              socket = device.createInsecureRfcommSocketToServiceRecord(UUID.fromString("000001801-0000-1000-8000-00805f9b34fb"));
////                              socket = device.createInsecureRfcommSocketToServiceRecord(UUID.fromString("0000ffe0-0000-1000-8000-00805f9b34fb"));
////                            socket = device.createInsecureRfcommSocketToServiceRecord(UUID.fromString("0000ffe1-0000-1000-8000-00805f9b34fb"));
//
////
////                            socket = device.createRfcommSocketToServiceRecord(UUID.fromString("00001108-0000-1000-8000-00805f9b34fb"));
////                            start();
//
////                            socket = device.createRfcommSocketToServiceRecord(UUID.fromString("0000111e-0000-1000-8000-00805f9b34fb"));
////                            start();
////
////                            socket = device.createRfcommSocketToServiceRecord(UUID.fromString("0000110b-0000-1000-8000-00805f9b34fb"));
////                            start();
//
////                            socket = device.createRfcommSocketToServiceRecord(UUID.fromString("0000110e-0000-1000-8000-00805f9b34fb"));
////                            start();
////
////                            socket = device.createRfcommSocketToServiceRecord(UUID.fromString("00000000-0000-1000-8000-00805f9b34fb"));
////                            start();
//
//                            socket = device.createRfcommSocketToServiceRecord(UUID.fromString("00000000-0000-1000-8000-00805f9b34fb"));
//                            start();
//                        } catch (IOException e) {
//                            return;
//                        }
//                    }
//                }
//
//
////                for (BluetoothDevice device : bondedDevices) {
////                    deviceList.add(device);
////                }
////                ListView lsVw = (ListView) findViewById(R.id.deviceList);
////                lsVw.setOnItemClickListener((parent, view, position, id) -> {
////                    BluetoothDevice device = (BluetoothDevice) deviceList.get(position);
////                    Snackbar.make(mLayout, "item clicked: " + device.getName(), Snackbar.LENGTH_SHORT).show();
////                });
////                ArrayAdapter aAdapter = new ArrayAdapter<BluetoothDevice>(getApplicationContext(), android.R.layout.simple_list_item_1, deviceList);
////                lsVw.setAdapter(aAdapter);
//            }
//        }

//        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
//
//        if (pairedDevices.size() > 0) {
//            // There are paired devices. Get the name and address of each paired device.
//            for (BluetoothDevice device : pairedDevices) {
//                String deviceName = device.getName();
//                String deviceHardwareAddress = device.getAddress(); // MAC address
//            }
//        }

//        if (!bluetoothAdapter.isDiscovering()) {
////                   out.append("MAKING YOUR DEVICE DISCOVERABLE");
//            Context context = getApplicationContext();
//            CharSequence text = "MAKING YOUR DEVICE DISCOVERABLE";
//            int duration = Toast.LENGTH_SHORT;
//
//            Toast toast = Toast.makeText(context, text, duration);
//            toast.show();
//            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
//            startActivityForResult(enableBtIntent, REQUEST_DISCOVERABLE_BT);
//        }

//        deviceManager = getSystemService(CompanionDeviceManager.class);
//        deviceFilter = new BluetoothDeviceFilter.Builder().build();
//        pairingRequest = new AssociationRequest.Builder()
//                .addDeviceFilter(deviceFilter)
//                .setSingleDevice(false)
//                .build();
//
////         When the app tries to pair with the Bluetooth device, show the
////         appropriate pairing request dialog to the user.
//        deviceManager.associate(pairingRequest,
//                new CompanionDeviceManager.Callback() {
//                    @Override
//                    public void onDeviceFound(IntentSender chooserLauncher) {
//                        try {
//                            startIntentSenderForResult(chooserLauncher, SELECT_DEVICE_REQUEST_CODE, null, 0, 0, 0);
//                        } catch (IntentSender.SendIntentException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(CharSequence error) {
//                        Snackbar.make(mLayout, "Error: " + error.toString(), Snackbar.LENGTH_SHORT).show();
//                    }
//                },
//                null);
    }

    private void start() {
        new Thread() {
            @Override
            public void run() {
                try {
                    socket.connect();
                    in = socket.getInputStream();
                } catch (IOException e) {
                    socket = null;
                    return;
                }

                while (isRunning) {
                    try {
                        if (socket.isConnected()) {
                            in.read(mmBuffer);
                            if (mmBuffer[5] == 80) {
                                pttState.isPressed(true);
                            } else {
                                pttState.isPressed(false);
                            }
                        }
                    } catch (IOException e) {

                    }
                }
            }
        }.start();
    }

    public void release() {
        isRunning = false;
        try {
            socket.close();
        } catch (IOException e) {

        }
        socket = null;
    }

    public interface OnMessageReceived {
        public void isPressed(boolean state);
    }

}