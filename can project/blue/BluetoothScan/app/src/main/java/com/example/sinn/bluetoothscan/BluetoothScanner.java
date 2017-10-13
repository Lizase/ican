package com.example.sinn.bluetoothscan;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.ScanRecord;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 * Created by alvinyeh on 2017/7/16.
 */

public class BluetoothScanner {

    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothManager bluetoothManager;
    private boolean mScanning;
    private Handler mHandler;
    private Context context;
    private BluetoothAdapter.LeScanCallback mLeScanCallback;
    private ArrayAdapter listAdapter;
    private ListView listView;
    private ArrayList<BluetoothDevice> devices;
    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 10000;
    int TX_POWER = 60;



    public BluetoothScanner(Context context, ListView listView)
    {
        devices = new ArrayList<BluetoothDevice>();
        this.context = context;
        listAdapter = new ArrayAdapter<String>(context,R.layout.list_item);
        //listView = (ListView)((Activity)this.context).findViewById(R.id.listViewBle);
        this.listView = listView;



        bluetoothManager = (BluetoothManager) this.context.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        mHandler = new Handler();
        setmLeScanCallback();


    }

    public void scanLeDevice(final boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);

                }
            }, SCAN_PERIOD);

            mScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }

    }

    public void stopBleScan()
    {
        mBluetoothAdapter.stopLeScan(mLeScanCallback);
        mBluetoothAdapter.cancelDiscovery();
    }

    private void setmLeScanCallback()
    {

        mLeScanCallback = new BluetoothAdapter.LeScanCallback(){

            @Override
            public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
                Log.i("Found device: ", device.getAddress() + device.getName());
                int startByte = 2;
                boolean patternFound = false;
                // 寻找ibeacon
                while (startByte <= 5) {
                    if (((int) scanRecord[startByte + 2] & 0xff) == 0x02 && // Identifies
                            // an
                            // iBeacon
                            ((int) scanRecord[startByte + 3] & 0xff) == 0x15) { // Identifies
                        // correct
                        // data
                        // length
                        patternFound = true;
                        break;
                    }
                    startByte++;
                }
                // ibeacon的Major值
                int major = (scanRecord[startByte + 20] & 0xff) * 0x100
                        + (scanRecord[startByte + 21] & 0xff);

                // ibeacon的Minor值
                int minor = (scanRecord[startByte + 22] & 0xff) * 0x100
                        + (scanRecord[startByte + 23] & 0xff);
                if(major == 1){
                    if (device.getName() != null)
                    {
                        for (int i = 0; i < listAdapter.getCount(); i++)
                        {
                            if (String.valueOf(listAdapter.getItem(i)).contains(device.getName()) )
                            {
                                listAdapter.remove(listAdapter.getItem(i));
                                devices.remove(i);
                                break;
                            }
                        }


                        double distance = calculateDistance(59,rssi);

                        listAdapter.add(device.getName() + "\n(" + (int)distance+ " m )" + rssi);

                        listAdapter.notifyDataSetChanged();
                        listView.setAdapter(listAdapter);
                        devices.add(device);


                    }/*END IF*/
                }



            }
        }; /*END scan callback configuration*/
    } /* END FUNCTION*/


    public ArrayAdapter getListAdapter() {
        return listAdapter;
    }

    public ArrayList<BluetoothDevice> getDevices() {
        return devices;
    }






    public double calculateDistance(int txPower, double rssi) {
        if (rssi == 0)
        {
            return -1.0;
        }

        double ratio = rssi * 1.0 / txPower;

        if (ratio < 1.0)
        {
            return Math.pow(ratio, 10);
        }
        else
        {
            double accuracy = (0.89976) * Math.pow(ratio, 7.7095) + 0.111;
            return accuracy;
        }
    }

}
