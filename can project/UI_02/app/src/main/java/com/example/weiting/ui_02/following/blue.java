package com.example.weiting.ui_02.following;
/**
 * Created by Weiting on 2017/8/15.
 */

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import com.example.weiting.ui_02.R;


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
    int major = 0, minor = 0, rssiValue = 0;
    boolean found = false;
    int TX_POWER = 60;
    int count = 0;
    int count3=0;
    int rssiAverge2 = 0;
    int check_count = 0;
    int rssiAverge = 0;
    int pastRssi = 0;
    int[] rssiArray = new int[10];
    SinglyLinkedList rssiArray2 = new SinglyLinkedList();


    public blue(Context context, ListView listView, TextView view)
    {
        devices = new ArrayList<BluetoothDevice>();
        listAdapter = new ArrayAdapter<String>(context, R.layout.list_item);
        this.context = context;
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
                            rssiValue = rssi;
                            found = true;
                            if(count < 20){
                                rssiArray2.addLast(rssi);
                                Log.d("qqqq","rssiArray2: "+rssiArray2.getFirst().getValue());
                            }else{
                                Node ptr = rssiArray2.getFirst();
                                rssiAverge = 0;
                                int count2 =0;
                                while(ptr != rssiArray2.getLast()){
                                    rssiAverge = rssiAverge + (int)ptr.getValue();
                                    ptr = ptr.getNext();
                                    count2++;
                                }
                                //Log.d("rssi Test","rssi: "+rssiAverge);
                                Log.d("count","count: "+count2);
                                rssiAverge = rssiAverge / 20;
                                view.setText("rssiAverge"+rssiAverge);
                                if(rssi - (int)rssiArray2.getFirst().getValue() < 8 && (rssi - (int)rssiArray2.getFirst().getValue()) > -8){
                                    rssiArray2.addLast(rssi);
                                    rssiArray2.removeFirst();
                                }

                            }
                            count++;
                        }
                    }
                    /**/
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
    public abstract class LinkedList<T> {
        protected int count;
        protected Node<T> first;
        protected Node<T> last;

        public Node<T> getFirst()
        {
            return first;
        }
        public Node<T> getLast()
        {
            return last;
        }
        public int size()
        {
            return count;
        }

        abstract public void addFirst(T value);
        abstract public void addLast(T value);
        abstract public void addBefore(Node<T> node, T value);
        abstract public void addAfter(Node<T> node, T value);
        abstract public void removeFirst();
        abstract public void removeLast();
        abstract public void remove(Node<T> node);
    }

    public class Node<T>
    {
        private Node<T> previous;
        private Node<T> next;
        private T value;

        public Node<T> getPrevious()
        {
            return previous;
        }

        void setPrevious(Node<T> previous)
        {
            this.previous = previous;
        }

        public Node<T> getNext()
        {
            return next;
        }

        void setNext(Node<T> next)
        {
            this.next = next;
        }

        public T getValue()
        {
            return value;
        }

        void setValue(T value)
        {
            this.value = value;
        }

        public Node(T value)
        {
            this.value = value;
        }
    }
    public class SinglyLinkedList<T> extends LinkedList<T> {

        @Override
        public void addFirst(T value) {
            // TODO Auto-generated method stub
            Node node = new Node (value);
            if (count == 0)
                last = node;
            else
                node.setNext(first);
            first = node;
            ++count;
        }

        @Override
        public void addLast(T value) {
            // TODO Auto-generated method stub
            Node<T> node = new Node<T>(value);
            if (count == 0)
                first = node;
            else
                last.setNext(node);
            last = node;
            ++count;
        }

        @Override
        public void addBefore(Node<T> node, T value) {
            // TODO Auto-generated method stub
            Node<T> newNode = new Node<T>(value);
            if (node == first)
            {
                first = newNode;
            }
            else
            {
                Node<T> preNode = findPreviousNode(node);
                preNode.setNext(newNode);
            }
            newNode.setNext(node);
            ++count;
        }

        @Override
        public void addAfter(Node<T> node, T value) {
            // TODO Auto-generated method stub
            Node<T> newNode = new Node<T>(value);
            newNode.setNext(node.getNext());
            node.setNext(newNode);
            if (node == last)
            {
                last = newNode;
            }
            ++count;
        }

        @Override
        public void removeFirst() {
            // TODO Auto-generated method stub
            if (count == 0)
                throw new ArrayIndexOutOfBoundsException();
            else if (count == 1)
            {
                first = null;
                last = null;
            }
            else
            {
                Node<T> node = first.getNext();
                first.setNext(null);
                first = node;
            }
            --count;
        }

        @Override
        public void removeLast() {
            // TODO Auto-generated method stub
            if (count == 0)
                throw new ArrayIndexOutOfBoundsException();
            else if (count == 1)
            {
                first = null;
                last = null;
            }
            else
            {
                Node<T> node = findPreviousNode(last);
                node.setNext(null);
                last = node;
            }
            --count;
        }

        @Override
        public void remove(Node<T> node) {
            // TODO Auto-generated method stub
            if (node == first)
                removeFirst();
            else if (node == last)
                removeLast();
            else
            {
                Node<T> preNode = findPreviousNode(node);
                if (preNode == null)
                    throw new ArrayIndexOutOfBoundsException();
                preNode.setNext(node.getNext());
                node.setNext(null);
                --count;
            }
        }

        private Node<T> findPreviousNode(Node<T> node)
        {
            Node<T> preNode = first;
            while (preNode != null)
            {
                if (node == preNode.getNext())
                    break;
                preNode = preNode.getNext();
            }
            return preNode;
        }
    }

}