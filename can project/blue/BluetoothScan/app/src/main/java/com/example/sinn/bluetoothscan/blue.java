package com.example.sinn.bluetoothscan;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by angsinn on 2017/7/16.
 */

public class blue {

    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothManager bluetoothManager;
    private boolean mScanning;
    private Handler mHandler;
    private Context context;
    private BluetoothAdapter.LeScanCallback mLeScanCallback;
    private ArrayAdapter listAdapter;
    private ListView listView;
    private TextView view;
    private ArrayList<BluetoothDevice> devices;
    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 600000;
    int TX_POWER = 60;
    int count = 0;
    int rssiAverge = 0;
    int pastRssi = 0;
    int[] rssiArray = new int[10];


    public blue(Context context, ListView listView,TextView view)
    {
        devices = new ArrayList<BluetoothDevice>();
        this.context = context;
        listAdapter = new ArrayAdapter<String>(context,R.layout.list_item);
        //listView = (ListView)((Activity)this.context).findViewById(R.id.listViewBle);
        this.listView = listView;
        this.view = view;



        bluetoothManager = (BluetoothManager) this.context.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        mHandler = new Handler();
        setmLeScanCallback();


    }

    public void scanLeDevice(final boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined s can period.
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

    public String getMajor(byte[] mScanRecord) {
        String major = String.valueOf( (mScanRecord[25] & 0xff) * 0x100 + (mScanRecord[26] & 0xff));
        return major;
    }

    public String getMinor(byte[] mScanRecord) {
        String minor =     String.valueOf( (mScanRecord[27] & 0xff) * 0x100 + (mScanRecord[28] & 0xff));
        return minor;
    }

    private void setmLeScanCallback()
    {

        mLeScanCallback = new BluetoothAdapter.LeScanCallback(){

            @Override
            public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
                Log.i("Found device: ", device.getAddress() + device.getName());
                int major = 0, minor = 0;

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


                    //double distance = calculateDistance(59,rssi);

                    /** Identify major and minor**/
                    int startByte = 2;
                    boolean patternFound = false;
                    while (startByte <= 5) {
                        if (    ((int) scanRecord[startByte + 2] & 0xff) == 0x02 && //Identifies an iBeacon
                                ((int) scanRecord[startByte + 3] & 0xff) == 0x15) { //Identifies correct data length
                            patternFound = true;
                           // Log.d("pattern",String.valueOf(patternFound));
                            break;
                        }
                        startByte++;
                    }
                    //Log.d("pattern2",String.valueOf(patternFound));
                    if (patternFound) {
                        //Convert to hex String
                        byte[] uuidBytes = new byte[16];
                        System.arraycopy(scanRecord, startByte+4, uuidBytes, 0, 16);
                        String hexString = bytesToHex(uuidBytes);

                        //Here is your UUID
                        String uuid =  hexString.substring(0,8) + "-" +
                                hexString.substring(8,12) + "-" +
                                hexString.substring(12,16) + "-" +
                                hexString.substring(16,20) + "-" +
                                hexString.substring(20,32);

                        //Here is your Major value
                        major = (scanRecord[startByte+20] & 0xff) * 0x100 + (scanRecord[startByte+21] & 0xff);

                        //Here is your Minor value
                        minor = (scanRecord[startByte+22] & 0xff) * 0x100 + (scanRecord[startByte+23] & 0xff);

                       if(major == 04 && minor == 28) {
                           listAdapter.add(device.getName() + "\nrssi: " + rssi + "\nmajor: " + major + "\n minor:" + minor);
                           listAdapter.notifyDataSetChanged();
                           listView.setAdapter(listAdapter);
                           devices.add(device);

                           if(count<10){
                                rssiArray[count] = rssi;
                           }
                           else{
                               int temp = 0;
                               for(int i=0; i<10;i++){
                                   for(int j=i+1;j<10;j++){
                                       if(rssiArray[i]>rssiArray[j] ){
                                           temp = rssiArray[i];
                                           rssiArray[i] = rssiArray[j];
                                           rssiArray[j] = temp;
                                       }
                                   }
                               }
                               rssiAverge = (rssiArray[3] + rssiArray[4] + rssiArray[5] +rssiArray[6])/4;
                               Log.d("Debug","rssiAverge: "+String.valueOf(rssiAverge));

                               if(rssiAverge >=-79){
                                   //Log.d("rssia",String.valueOf(a));
                                   view.setText("距離過近!!"+rssiAverge);
                               }else if(rssiAverge <-84){
                                   //Log.d("rssib",String.valueOf(a));
                                   view.setText("距離過遠!!"+rssiAverge);
                               }else{
                                   view.setText("normal!!"+rssiAverge);
                               }
                               count = -1;
                           }

                           count++;
                       }

                    }
                    /**/
                    //listAdapter.add(device.getName() + "\nrssi: " + rssi+ "\nmajor: " + getMajor(scanRecord) + "\n minor:" + getMinor(scanRecord));
                }/*END IF*/
            }
        }; /*END scan callback configuration*/
    } /* END FUNCTION*/


    /*public ArrayAdapter getListAdapter() {
        return listAdapter;
    }

    public ArrayList<BluetoothDevice> getDevices() {
        return devices;
    }*/


    static final char[] hexArray = "0123456789ABCDEF".toCharArray();
    private static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
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
