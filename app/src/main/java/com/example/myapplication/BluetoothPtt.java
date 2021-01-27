package com.example.myapplication;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import java.util.UUID;

public class BluetoothPtt {
    private  byte[] mmBuffer = new byte[1024];
    private InputStream in;
    private BluetoothSocket socket;
    private boolean isRunning = true;
    private OnMessageReceived pttState = null;

    public BluetoothPtt(OnMessageReceived listener){
        pttState = listener;
        Set<BluetoothDevice> bs = BluetoothAdapter.getDefaultAdapter().getBondedDevices();
        for (BluetoothDevice device : bs){
            if (device.getName().contains("PTT")){
                try{
                    socket = device.createRfcommSocketToServiceRecord(UUID.fromString("00001800-0000-1000-8000-00805f9b34fb"));
//                    socket = device.createRfcommSocketToServiceRecord(UUID.fromString("000001801-0000-1000-8000-00805f9b34fb"));
//                    socket = device.createRfcommSocketToServiceRecord(UUID.fromString("0000ffe0-0000-1000-8000-00805f9b34fb"));
                    start();
                }
                catch (IOException e){
                    return;
                }
            }
            return;
        }
    }

    private void start(){
        new Thread(){
            @Override
            public void run(){
                try {
                    socket.connect();
                    in = socket.getInputStream();
                }
                catch (IOException e){
                    socket = null;
                    return;
                }

                while (isRunning){
                    try {
                        if (socket.isConnected()){
                            in.read(mmBuffer);
                            if (mmBuffer[5] == 80){
                                pttState.isPressed(true);
                            }
                            else
                            {
                                pttState.isPressed(false);
                            }
                        }
                    } catch (IOException e) {

                    }
                }
            }
        }.start();
    }

    public void release(){
        isRunning = false;
        try {
            socket.close();
        }
        catch (IOException e){

        }
        socket = null;
    }

    public interface OnMessageReceived {
        public void isPressed(boolean state);
    }
}