/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sage.hedonicmentality.service;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.util.Log;

import com.lidroid.xutils.util.LogUtils;

import org.simple.eventbus.EventBus;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Service for managing connection and data communication with a GATT server hosted on a
 * given Bluetooth LE device.
 */
public class BluetoothLeService extends Service {


    public static final UUID UUID_SERVICE_DATA                 = UUID.fromString("49535343-fe7d-4ae5-8fa9-9fafd205e455");
    public static final UUID       UUID_CHARACTER_RECEIVE       = UUID.fromString("49535343-1e4d-4bd9-ba61-23c647249616");

    public static final UUID UUID_CLIENT_CHARACTER_CONFIG       = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");

    public static final int  MESSAGE_OXIMETER_PARAMS            = 2003;
    public static final int  MESSAGE_OXIMETER_WAVE              = 2004;
    private final static String TAG = BluetoothLeService.class.getSimpleName();

    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private String mBluetoothDeviceAddress;
    private BluetoothGatt mBluetoothGatt;
    private int mConnectionState = STATE_DISCONNECTED;

    private static final int STATE_DISCONNECTED = 0;
    private static final int STATE_CONNECTING = 1;
    private static final int STATE_CONNECTED = 2;

    public final static String ACTION_GATT_CONNECTED =
            "com.example.bluetooth.le.ACTION_GATT_CONNECTED";
    public final static String ACTION_GATT_DISCONNECTED =
            "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED";
    public final static String ACTION_GATT_SERVICES_DISCOVERED =
            "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";
    public final static String ACTION_DATA_AVAILABLE =
            "com.example.bluetooth.le.ACTION_DATA_AVAILABLE";
    public final static String ACTION_SPO2_DATA_AVAILABLE =
            "com.example.bluetooth.le.ACTION_SPO2_DATA_AVAILABLE";
    public final static String EXTRA_DATA =
            "com.example.bluetooth.le.EXTRA_DATA";

    private byte[] buf = new byte[10];
    private int bufIndex = 0;

    public static final String TAG_CONNECTED="connected";/**连接上蓝牙设备*/
    public static final String TAG_DISCONNECTEDS="disconnecteds";/**断开蓝牙设备*/
    public static final String TAG_GATT_SERVICES_DISCOVERED="gatt_services_discovered";/**获取服务*/
    public static final String TAG_GATT_DATA_AVAILABLE="data_available";/**获取服务*/
    public static final String TAG_SPO2_DATA_AVAILABLE="spo2_data_available";
    private Handler mHandler;
    public static BluetoothDevice device_connect;
    @Override
    public void onCreate() {
        super.onCreate();
        if(device_connect!=null){
            if(initBluetoothAdapter()){
                connect(device_connect.getAddress());
            }

        }else{
            initialize();
        }
            HandlerThread thread=new HandlerThread("handle data");
            thread.start();
            mHandler= new Handler(thread.getLooper());
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    handleData();
                }
            });


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mBluetoothAdapter!=null){
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
        if(mHandler!=null){
            mHandler.removeCallbacksAndMessages(null);
        }
        go=false;
        close();
        device_connect=null;
    }
    public static String deviecname = "";
    // Device scan callback.
    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {

                @Override
                public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
                    System.err.println("onLeScan===="+device.getName());

//                            if (device.getName()!=null&&device.getName().startsWith("Berry")) {
                    if (device.getName()!=null&&device.getName().startsWith(deviecname)) {
                                mBluetoothAdapter.stopLeScan(mLeScanCallback);
                                device_connect=device;
                                connect(device.getAddress());
                            }

                }
            };
    /**获取数据*/
    // Implements callback methods for GATT events that the app cares about.  For example,
    // connection change and services discovered.
    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            System.err.println("++++++++++++onConnectionStateChange"+newState);


            switch(newState){
                case BluetoothProfile.STATE_CONNECTED:
                    mConnectionState = STATE_CONNECTED;
                    EventBus.getDefault().post(1,TAG_CONNECTED);
                    Log.i(TAG, "Attempting to start service discovery:" +
                            mBluetoothGatt.discoverServices());
                    break;
                case BluetoothProfile.STATE_DISCONNECTED:
                    EventBus.getDefault().post(2,TAG_DISCONNECTEDS);
                    mConnectionState = STATE_DISCONNECTED;
                    LogUtils.e("++++++++++++onConnectionStat++++++++++++");
                    initialize();/**断开后重新扫描连接*/
                    break;
            }



        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            System.err.println("++++++++++++onServicesDiscovered");
            switch (status){
                case BluetoothGatt.GATT_SUCCESS:
                    List<BluetoothGattService> services =
                            getSupportedGattServices();
                    BluetoothGattService mDataService = null;
                    for(BluetoothGattService service : services)
                    {
                        if(service.getUuid().equals(UUID_SERVICE_DATA))
                        {
                            mDataService = service;
                        }
                    }
                    if(mDataService != null)
                    {
                        List<BluetoothGattCharacteristic> characteristics =
                                mDataService.getCharacteristics();
                        for(BluetoothGattCharacteristic ch: characteristics)
                        {
                            if(ch.getUuid().equals(UUID_CHARACTER_RECEIVE))
                            {

                                setCharacteristicNotification(ch,true);
                            }
                        }
                    }
                    break;
            }

        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic characteristic,
                                         int status) {
            System.err.println("++++++++++++onCharacteristicRead"+Arrays.toString(characteristic.getValue()));
//            if (status == BluetoothGatt.GATT_SUCCESS) {
//                broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
//            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt,
                                            BluetoothGattCharacteristic characteristic) {
            //System.err.println("++++++++++++onCharacteristicChanged"+characteristic.getUuid());
            //broadcastUpdate(ACTION_SPO2_DATA_AVAILABLE, characteristic);

            if (UUID_CHARACTER_RECEIVE.equals(characteristic.getUuid())) {
                System.err.println("++++++++++++onCharacteristicChanged" + Arrays.toString(characteristic.getValue()));
               byte[]  data= characteristic.getValue();
                for(int i=0;i<data.length;i++){
                    try {
                       // oxiData.put(((int) data[i]) & 0xff);
                        oxiData.put((int) data[i]);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    };
    private static int averageBpmIndex = 0;
    private static final int averageBpmArraySize = 3;
    private static final float[] averageBpmArray = new float[averageBpmArraySize];
    private boolean go=true;
    private LinkedBlockingQueue<Integer> oxiData = new LinkedBlockingQueue<Integer>(1256);
    private void handleData() {
        int bufferIndex=0;
        byte[] buffer=new byte[1024];
        int preWaveValue=-1;
        int updown=0;
        long peakTime;
        long prePeakTime=-1;
        double hr;
        int count=0;
        int so2;
        while (go) {
            synchronized (this) {
                int b = 0;
                try {
                    b = oxiData.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    continue;
                }
                //
                if (b < 0) {
                    bufferIndex = 0;
                    System.out.println();
                }
                System.out.print(b+",");
                buffer[bufferIndex] = (byte) b;
                if (bufferIndex == 2) {
                    if(b==0||b>20){/**获取数据异常*/
                        continue;
                    }
                    if (preWaveValue < 0) {
                        preWaveValue = b;
                    }
                    if (b > preWaveValue) {
                        updown = 1;
                    } else if (b < preWaveValue) {
                        if (updown == 1) {//previous is up and now it is down, peak value detected
                            peakTime = System.currentTimeMillis();
                            if (prePeakTime < 0) {
                                prePeakTime = peakTime;
                            } else {
                                hr = (1000 * 60.00) / (peakTime - prePeakTime);
                                System.out.println();
                                System.out.println("b=" + preWaveValue + " interval=" + (peakTime - prePeakTime)+"hr=" + hr);
                                averageBpmIndex = averageBpmIndex % averageBpmArraySize;
                                averageBpmArray[averageBpmIndex] = (float) hr;
                                averageBpmIndex++;
                                float beatsBpmArraySum = 0;
                                int beatsBpmArrayCount = 0;
                                for (int i = 0; i < averageBpmArray.length; i++) {
                                    if (averageBpmArray[i] > 0) {
                                        beatsBpmArraySum += averageBpmArray[i];
                                        beatsBpmArrayCount++;
                                    }
                                }
                                hr = (beatsBpmArraySum / beatsBpmArrayCount);
                                EventBus.getDefault().post((int) hr, TAG_SPO2_DATA_AVAILABLE);
                                prePeakTime = peakTime;
                            }
                        }
                        updown = 0;
                    }
                    preWaveValue = b;
                }
                bufferIndex++;
            }
        }

    }










    private void broadcastUpdate(final String action) {
        final Intent intent = new Intent(action);
        sendBroadcast(intent);
    }


    private void broadcastUpdate(final String action,
                                 final BluetoothGattCharacteristic characteristic) {
        final Intent intent = new Intent(action);

        // This is special handling for the Heart Rate Measurement profile.  Data parsing is
        // carried out as per profile specifications:
        // http://developer.bluetooth.org/gatt/characteristics/Pages/CharacteristicViewer.aspx?u=org.bluetooth.characteristic.heart_rate_measurement.xml
        if (UUID_CHARACTER_RECEIVE.equals(characteristic.getUuid())) {
            final byte[] data = characteristic.getValue();
            System.err.println("data******"+ Arrays.toString(data));
            for(byte b : data)
            {
                buf[bufIndex] = b;
                bufIndex++;
                if(bufIndex == 10)
                {
                    intent.putExtra(EXTRA_DATA,buf);
                    sendBroadcast(intent);
                    bufIndex = 0;
                }
            }

//            int flag = characteristic.getProperties();
//            int format = -1;
//            if ((flag & 0x01) != 0) {
//                format = BluetoothGattCharacteristic.FORMAT_UINT16;
//                Log.d(TAG, "Heart rate format UINT16.");
//            } else {
//                format = BluetoothGattCharacteristic.FORMAT_UINT8;
//                Log.d(TAG, "Heart rate format UINT8.");
//            }
//            System.err.println("format="+format);
//            final int heartRate = characteristic.getIntValue(format, 1);
//            Log.d(TAG, String.format("Received heart rate: %d", heartRate));








        } else {
            // For all other profiles, writes the data formatted in HEX.
            final byte[] data = characteristic.getValue();
            System.err.println("else data******"+ Arrays.toString(data));
            if (data != null && data.length > 0) {
                System.err.println("data else******"+ Arrays.toString(data));
                final StringBuilder stringBuilder = new StringBuilder(data.length);
                intent.putExtra(EXTRA_DATA, new String(data));
                sendBroadcast(intent);
            }
        }

    }

    public class LocalBinder extends Binder {
        BluetoothLeService getService() {
            return BluetoothLeService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        // After using a given device, you should make sure that BluetoothGatt.close() is called
        // such that resources are cleaned up properly.  In this particular example, close() is
        // invoked when the UI is disconnected from the Service.
        close();
        return super.onUnbind(intent);
    }

    private final IBinder mBinder = new LocalBinder();

    /**
     * Initializes a reference to the local Bluetooth adapter.
     *
     * @return Return true if the initialization is successful.
     */
    public boolean initialize() {
       initBluetoothAdapter();
        mBluetoothAdapter.startLeScan(mLeScanCallback);
        return true;
    }

    private boolean initBluetoothAdapter(){
        // For API level 18 and above, get a reference to BluetoothAdapter through
        // BluetoothManager.
        if (mBluetoothManager == null) {
            mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            if (mBluetoothManager == null) {
                Log.e(TAG, "Unable to initialize BluetoothManager.");
                return false;
            }
        }

        mBluetoothAdapter = mBluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) {
            Log.e(TAG, "Unable to obtain a BluetoothAdapter.");
            return false;
        }
        if(!mBluetoothAdapter.isEnabled()){
            mBluetoothAdapter.enable();
            return true;
        }
        return true;
    }
    /**
     * Connects to the GATT server hosted on the Bluetooth LE device.
     *
     * @param address The device address of the destination device.
     *
     * @return Return true if the connection is initiated successfully. The connection result
     *         is reported asynchronously through the
     *         {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     *         callback.
     */
    public boolean connect(final String address) {
        if (mBluetoothAdapter == null || address == null) {
            System.err.println("BluetoothAdapter not initialized or unspecified address.");
            return false;
        }

        // Previously connected device.  Try to reconnect.
        if (mBluetoothDeviceAddress != null && address.equals(mBluetoothDeviceAddress)
                && mBluetoothGatt != null) {
            System.err.println("Trying to use an existing mBluetoothGatt for connection.");
            if (mBluetoothGatt.connect()) {
                mConnectionState = STATE_CONNECTING;
                return true;
            } else {
                return false;
            }
        }

        final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        if (device == null) {
            System.err.println("Device not found.  Unable to connect.");
            return false;
        }
        // We want to directly connect to the device, so we are setting the autoConnect
        // parameter to false.
        mBluetoothGatt = device.connectGatt(this, false, mGattCallback);
        System.err.println("Trying to create a new connection.");
        mBluetoothDeviceAddress = address;
        mConnectionState = STATE_CONNECTING;
        return true;
    }

    /**
     * Disconnects an existing connection or cancel a pending connection. The disconnection result
     * is reported asynchronously through the
     * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     * callback.
     */
    public void disconnect() {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.disconnect();
    }

    /**
     * After using a given BLE device, the app must call this method to ensure resources are
     * released properly.
     */
    public void close() {
        if (mBluetoothGatt == null) {
            return;
        }
        mBluetoothGatt.close();
        mBluetoothGatt = null;
    }

    /**
     * Request a read on a given {@code BluetoothGattCharacteristic}. The read result is reported
     * asynchronously through the {@code BluetoothGattCallback#onCharacteristicRead(android.bluetooth.BluetoothGatt, android.bluetooth.BluetoothGattCharacteristic, int)}
     * callback.
     *
     * @param characteristic The characteristic to read from.
     */
    public void readCharacteristic(BluetoothGattCharacteristic characteristic) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.readCharacteristic(characteristic);
    }

    /**
     * Enables or disables notification on a give characteristic.
     *
     * @param characteristic Characteristic to act on.
     * @param enabled If true, enable notification.  False otherwise.
     */
    public void setCharacteristicNotification(BluetoothGattCharacteristic characteristic,
                                              boolean enabled) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);

        // This is specific to Oximeter Data Transfer.
        if (UUID_CHARACTER_RECEIVE.equals(characteristic.getUuid())) {
            BluetoothGattDescriptor descriptor = characteristic.getDescriptor(UUID_CLIENT_CHARACTER_CONFIG);
            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            mBluetoothGatt.writeDescriptor(descriptor);
        }
    }

    /**
     * Retrieves a list of supported GATT services on the connected device. This should be
     * invoked only after {@code BluetoothGatt#discoverServices()} completes successfully.
     *
     * @return A {@code List} of supported services.
     */
    public List<BluetoothGattService> getSupportedGattServices() {
        if (mBluetoothGatt == null) return null;

        return mBluetoothGatt.getServices();
    }
}
