package com.example.sinn.bluetoothscan;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button buttonScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView)findViewById(R.id.listViewBle);
        devices = new ArrayList<BluetoothDevice>();
        //this.context = context;
        listAdapter = new ArrayAdapter<String>(this,R.layout.list_item);
        //listView = (ListView)((Activity)this.context).findViewById(R.id.listViewBle);
       // this.listView = listView;



        bluetoothManager = (BluetoothManager) this.getSystemService(this.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        mHandler = new Handler();
        buttonScan = (Button)findViewById(R.id.buttonScan);
        buttonScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanLeDevice(true);
            }
        });
    }

    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothManager bluetoothManager;
    private boolean mScanning;
    private Handler mHandler;
    private Context context;

    private ArrayAdapter listAdapter;
    private ListView listView;
    private ArrayList<BluetoothDevice> devices;
    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 10000;
    int TX_POWER = 60;


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

    public BluetoothAdapter.LeScanCallback mLeScanCallback= new BluetoothAdapter.LeScanCallback(){

        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            Log.i("Found device: ", device.getAddress() + device.getName());

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


                double distance = calculateDistance(-59,rssi);

                listAdapter.add(device.getName() + "\n(" + (int)distance+ " m )");

                listAdapter.notifyDataSetChanged();
                listView.setAdapter(listAdapter);
                devices.add(device);


            }/*END IF*/


        }
    }; /*END scan callback configuration*/



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

