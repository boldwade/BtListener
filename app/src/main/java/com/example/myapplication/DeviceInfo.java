package com.example.myapplication;

import android.bluetooth.BluetoothDevice;

public class DeviceInfo {
    private BluetoothDevice bluetoothDevice;

    public DeviceInfo(BluetoothDevice bluetoothDevice) {
        this.bluetoothDevice = bluetoothDevice;
    }

    // Return Device name
    public String getName() {
        return this.bluetoothDevice.getName();
    }

    // Return Device address
    public String getAddress() {
        return this.bluetoothDevice.getAddress();
    }

    public BluetoothDevice getBluetoothDevice() {
        return this.bluetoothDevice;
    }

    @Override
    public String toString() {
        return getName();
    }
}
