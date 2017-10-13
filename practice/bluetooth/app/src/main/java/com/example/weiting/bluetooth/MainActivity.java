package com.example.weiting.bluetooth;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.UUID;


public class MainActivity extends AppCompatActivity {
    //方位,溫濕度
    String temperature=" ";
    int angle = 0,humidity = 0;
    //magnetic
    SensorManager mySensorManager;
    String result ="";
    int change = 0;
    int var = 0;
    char deviceVar =' ';
    int check = 0;
    TextView current;
    TextView device;
    TextView command;
    char uservar;
    int adjust = 0;
    int x = 0;
    private static final int REQUEST_ENABLE_BT = 1;

    BluetoothAdapter bluetoothAdapter;
    BluetoothScanner bt;//beacon
    boolean start = true;
    ArrayList<BluetoothDevice> pairedDeviceArrayList;
    TextView textInfo, textStatus,warring;
    ListView listViewPairedDevice , beacon;
    LinearLayout inputPane;
    EditText inputField;
    Button btnSend;

    ArrayAdapter<BluetoothDevice> pairedDeviceAdapter;
    private UUID myUUID;
    private final String UUID_STRING_WELL_KNOWN_SPP =
            "00001101-0000-1000-8000-00805F9B34FB";

    ThreadConnectBTdevice myThreadConnectBTdevice;
    ThreadConnected myThreadConnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        command = (TextView)findViewById(R.id.command);
        beacon = (ListView)findViewById(R.id.beacon);
        bt = new BluetoothScanner(MainActivity.this,beacon,command,warring);
        warring = (TextView)findViewById(R.id.textView);
        textInfo = (TextView)findViewById(R.id.info);
        textStatus = (TextView)findViewById(R.id.status);
        current = (TextView)findViewById(R.id.current);
        device = (TextView)findViewById(R.id.device);
        listViewPairedDevice = (ListView)findViewById(R.id.pairedlist);
        inputPane = (LinearLayout)findViewById(R.id.inputpane);
        inputField = (EditText)findViewById(R.id.input);
        btnSend = (Button)findViewById(R.id.send);
        mySensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);//感測器啟用
        Adjust();
        command.setText("stop");
        btnSend.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                check++;
                if(check%2 == 0) {
                    Adjust();
                   // onPause();
                }
                else {
                    storeThread();
                    onResume();
                }
            }});


        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH)){
            Toast.makeText(this,
                    "FEATURE_BLUETOOTH NOT support",
                    Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        //using the well-known SPP UUID
        myUUID = UUID.fromString(UUID_STRING_WELL_KNOWN_SPP);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(this,
                    "Bluetooth is not supported on this hardware platform",
                    Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        String stInfo = bluetoothAdapter.getName() + "\n" +
                bluetoothAdapter.getAddress();
        textInfo.setText(stInfo);
    }

//  ***********************************************接收Thread中傳來的方向************************************************************
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case 1:
                    if(myThreadConnected!=null && (check%2 == 1)){
                        warring.setText("rssiAverage: "+bt.rssiAverge);
                        command.setText("right");
                        byte[] bytesToSend = command.getText().toString().getBytes();
                        myThreadConnected.write(bytesToSend);
                        current.setText("current:"+var);
                        device.setText("device:"+angle);
                    }else if(check%2 == 0 && check != 0){
                        command.setText("stop");
                        warring.setText("rssiAverage: "+bt.rssiAverge);
                        current.setText("current:"+var);
                        device.setText("device:"+angle);
                        byte[] bytesToSend = command.getText().toString().getBytes();
                        myThreadConnected.write(bytesToSend);
                    }
                    break;
                case 2:
                    if(myThreadConnected!=null && (check%2 == 1)){
                        warring.setText("rssiAverage: "+bt.rssiAverge);
                        command.setText("left");
                        byte[] bytesToSend = command.getText().toString().getBytes();
                        myThreadConnected.write(bytesToSend);
                        current.setText("current:"+var);
                        device.setText("device:"+angle);
                    }else if(check%2 == 0 && check != 0){
                        command.setText("stop");
                        warring.setText("rssiAverage: "+bt.rssiAverge);
                        current.setText("current:"+var);
                        device.setText("device:"+angle);
                        byte[] bytesToSend = command.getText().toString().getBytes();
                        myThreadConnected.write(bytesToSend);
                    }
                    break;
                case 3:
                    if(myThreadConnected!=null && (check%2 == 1)){
                        if(bt.rssiAverge >=-65){
                            command.setText("stop");
                            byte[] bytesToSend = command.getText().toString().getBytes();
                            myThreadConnected.write(bytesToSend);
                        }else if(bt.rssiAverge <-65){
                            command.setText("forward");
                            byte[] bytesToSend = command.getText().toString().getBytes();
                            myThreadConnected.write(bytesToSend);
                        }
                        warring.setText("rssiAverage: "+bt.rssiAverge);
                        current.setText("current:"+var);
                        device.setText("device:"+angle);
                    }else if(check%2 == 0 && check != 0){
                        command.setText("stop");
                        byte[] bytesToSend = command.getText().toString().getBytes();
                        myThreadConnected.write(bytesToSend);
                    }

                    break;
            }
        }
    };

    //
    // 偵測是否九軸加速器發生變化

 private SensorListener mySensorListener = new SensorListener(){
        @Override
        public void onAccuracyChanged(int sensor, int accuracy) {}
        @Override
        public void onSensorChanged(int sensor, float[] values) {
            if(sensor == SensorManager.SENSOR_ORIENTATION){
                if(sensor == SensorManager.SENSOR_ORIENTATION){
                    var = (int)values[0];
                }
            }
        }
    };
   @Override
    protected void onResume() {

        mySensorManager.registerListener(
                mySensorListener,
                SensorManager.SENSOR_TEMPERATURE |
                        SensorManager.SENSOR_MAGNETIC_FIELD |
                        SensorManager.SENSOR_ACCELEROMETER |
                        SensorManager.SENSOR_LIGHT |
                        SensorManager.SENSOR_ORIENTATION,
                SensorManager.SENSOR_DELAY_UI
        );
        super.onResume();
    }
    @Override
    protected void onPause() {
        mySensorManager.unregisterListener(mySensorListener);
        super.onPause();
    }

//************************************************************************************************************

    @Override
    protected void onStart() {
        super.onStart();

        //Turn ON BlueTooth if it is OFF
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }

        setup();
    }

    //
    // 將以配對的藍芽匯入listview

    private void setup() {
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            pairedDeviceArrayList = new ArrayList<BluetoothDevice>();

            for (BluetoothDevice device : pairedDevices) {
                pairedDeviceArrayList.add(device);
            }

            pairedDeviceAdapter = new ArrayAdapter<BluetoothDevice>(this,
                    android.R.layout.simple_list_item_1, pairedDeviceArrayList);
            listViewPairedDevice.setAdapter(pairedDeviceAdapter);

            //******************************************
                    //藍牙清單選擇連到哪個裝置
            //*******************************************
            listViewPairedDevice.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    BluetoothDevice device =
                            (BluetoothDevice) parent.getItemAtPosition(position);
                    Toast.makeText(MainActivity.this,
                            "Name: " + device.getName() + "\n"
                                    + "Address: " + device.getAddress() + "\n"
                                    + "BondState: " + device.getBondState() + "\n"
                                    + "BluetoothClass: " + device.getBluetoothClass() + "\n"
                                    + "Class: " + device.getClass(),
                            Toast.LENGTH_LONG).show();

                    textStatus.setText("start ThreadConnectBTdevice");
                    myThreadConnectBTdevice = new ThreadConnectBTdevice(device);
                    myThreadConnectBTdevice.start();
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(myThreadConnectBTdevice!=null){
            myThreadConnectBTdevice.cancel();
        }
    }
    //*************************************************
                // 藍牙是否正常運作
    //*************************************************
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==REQUEST_ENABLE_BT){
            if(resultCode == Activity.RESULT_OK){
                setup();
            }else{
                Toast.makeText(this,
                        "BlueTooth NOT enabled",
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    //Called in ThreadConnectBTdevice once connect successed
    //to start ThreadConnected
    private void startThreadConnected(BluetoothSocket socket){

        myThreadConnected = new ThreadConnected(socket);
        myThreadConnected.start();
    }

    /*
    ThreadConnectBTdevice:
    Background Thread to handle BlueTooth connecting
    */
    private class ThreadConnectBTdevice extends Thread {

        private BluetoothSocket bluetoothSocket = null;
        private final BluetoothDevice bluetoothDevice;


        private ThreadConnectBTdevice(BluetoothDevice device) {
            bluetoothDevice = device;

            try {
                bluetoothSocket = device.createRfcommSocketToServiceRecord(myUUID);
                textStatus.setText("bluetoothSocket: \n" + bluetoothSocket);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            boolean success = false;
            try {
                bluetoothSocket.connect();
                //*************************
                // 變數success 代表 Socket 連接是否成功
                //******************************
                success = true;
            } catch (IOException e) {
                e.printStackTrace();

                final String eMessage = e.getMessage();
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        textStatus.setText("something wrong bluetoothSocket.connect(): \n" + eMessage);
                    }
                });

                try {
                    bluetoothSocket.close();
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }

            if(success){
                //**************************
                  // Socket connect successful
                //**************************
                final String msgconnected = "connect successful:\n"
                        + "BluetoothSocket: " + bluetoothSocket + "\n"
                        + "BluetoothDevice: " + bluetoothDevice;

                runOnUiThread(new Runnable(){

                    @Override
                    public void run() {
                        textStatus.setText(msgconnected);

                        listViewPairedDevice.setVisibility(View.GONE);
                        inputPane.setVisibility(View.VISIBLE);
                        bt.scanLeDevice(true);//start beacon scan
                    }});
                startThreadConnected(bluetoothSocket);
                //storeThread();
            }else{
                //fail
            }
        }

        public void cancel() {

            Toast.makeText(getApplicationContext(),
                    "close bluetoothSocket",
                    Toast.LENGTH_LONG).show();
            try {
                bluetoothSocket.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /*
    ThreadConnected:
    Background Thread to handle Bluetooth data communication
    after connected
     */
    private class ThreadConnected extends Thread {
        private final BluetoothSocket connectedBluetoothSocket;
        private final InputStream connectedInputStream;
        private final OutputStream connectedOutputStream;

        public ThreadConnected(BluetoothSocket socket) {
            connectedBluetoothSocket = socket;
            InputStream in = null;
            OutputStream out = null;

            try {
                in = socket.getInputStream();
                out = socket.getOutputStream();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            connectedInputStream = in;
            connectedOutputStream = out;
        }

        @Override
        public void run() {
            byte[] buffer = new byte[1024];
            int bytes;
            while (true) {
                try {

                    bytes = connectedInputStream.read(buffer);
                    final String strReceived = new String(buffer, 0, bytes);
                   // Log.d("Token","Token: "+strReceived);
                    //********************
                        // 處理逗號分割
                    //********************
                    int tokenCount = 0;
                    int error = 0;
                    //String a ="12,23,45 "
                    String[] split_line = strReceived.split(",");
                    String show_split_line = "";
                    for (String s: split_line) {
                        show_split_line = s;
                       error++;
                    }
                    Log.d("error","error: " + error+", s: "+show_split_line);
                    for (String s: split_line) {
                        show_split_line =   s ;
                        if(show_split_line.indexOf(" ") < 0 && error > 2) {
                            switch (tokenCount){
                                case 0:
                                    angle = Integer.parseInt(show_split_line);
                                    break;
                                case 1:
                                    if(Integer.parseInt(show_split_line) < 100)
                                        humidity = Integer.parseInt(show_split_line);
                                    else
                                        tokenCount++;
                                    break;
                                case 2:
                                    temperature = show_split_line;
                                    break;
                            }
                            tokenCount++;
                        }
                    }

                    // stream中的資料
                    //final String msgReceived = String.valueOf(bytes);
                    //deviceVar = strReceived.charAt(0);
                    runOnUiThread(new Runnable(){
                        @Override
                        public void run() {
                           textStatus.setText("deviceVar: "+strReceived);
                        }});
                } catch (IOException e) {

                    // Bluetooth Connection lost

                    // TODO Auto-generated catch block
                    e.printStackTrace();

                    final String msgConnectionLost = "Connection lost:\n"
                            + e.getMessage();
                    runOnUiThread(new Runnable(){

                        @Override
                        public void run() {
                            textStatus.setText(msgConnectionLost);
                        }});
                }
            }

        }

        public void write(byte[] buffer) {
            try {
                connectedOutputStream.write(buffer);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        public void cancel() {
            try {
                connectedBluetoothSocket.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    //
    //
    //
    // Thread用來上傳資料到資料庫
    // 發送指令
    //
    protected void storeThread(){
        new Thread(){
            public void run(){
                while(check % 2 ==1) {
                    try {
                        Thread.sleep(500);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    String url = "http://140.134.26.143/ican/php/ADHT.php?humidity="+humidity+"&temperature="+temperature;
                    DBconnected.updateData(url);
                    Log.d("addd","x: "+x);
                    if(var + x > 360){
                        var = 360 - var+x;
                    }else if(var + x < 0){
                        var = 360 + var + x;
                    }else{
                        var = var + x;
                    }
                    Log.d("addd","var: "+var);
                    if ((angle - var)>180){
                        change = angle-360-var;
                    }else if((angle- var)<-180){
                        change = angle+360-var;
                    }else{
                        change =angle- var;
                    }

                    if ((change) > 35 ) {
                        Message msg = new Message();
                        msg.what = 2;
                        mHandler.sendMessage(msg);
                    } else if((change) < -35){
                        Message msg = new Message();
                        msg.what = 1;
                        mHandler.sendMessage(msg);
                    }else if((change) > -35 && change < 35){
                        Message msg = new Message();
                        msg.what = 3;
                        mHandler.sendMessage(msg);
                    }
                   /*if(var >= 0 && var <= 22)
                        uservar = 'a';
                    else if(var > 22 && var <= 45)
                        uservar = 'b';
                    else if(var> 45 && var <= 67)
                        uservar = 'c';
                    else if(var > 67 && var <= 90)
                        uservar = 'd';
                    else if(var > 90 && var <= 112)
                        uservar = 'e';
                    else if(var > 112 && var <= 135)
                        uservar = 'f';
                    else if(var > 135 && var <= 157)
                        uservar = 'g';
                    else if(var > 157 && var <= 180)
                        uservar = 'h';
                    else if(var > 180 && var <= 202)
                        uservar = 'i';
                    else if(var > 202 && var <= 225)
                        uservar = 'j';
                    else if(var > 225 && var <= 247)
                        uservar = 'k';
                    else if(var > 247 && var <= 270)
                        uservar = 'l';
                    else if(var > 270 && var <= 292)
                        uservar = 'm';
                    else if(var > 292 && var <= 315)
                        uservar = 'n';
                    else if(var > 315 && var <= 337)
                        uservar = 'o';
                    else if(var > 337 && var <= 360)
                        uservar = 'p';
                    change = uservar - deviceVar;
                    Log.d("change","change: "+change);
                    String url = "http://140.134.26.143/ican/php/ADHT.php?humidity="+humidity+"&temperature="+temperature;
                    DBconnected.updateData(url);
                    if(change == 15 || change == -15) { // 人在H,A方位角時forward
                        Message msg = new Message();
                        msg.what = 3;
                        mHandler.sendMessage(msg);
                    }else if (change < -2) {
                        Message msg = new Message();
                        if (change < -7) {
                            msg.what = 1;
                        } else {
                            msg.what = 2;
                        }
                        mHandler.sendMessage(msg);
                    } else if (change > 2) {
                        Message msg = new Message();
                        if (change > 7) {
                            msg.what = 2;
                        } else {
                            msg.what = 1;
                        }
                        mHandler.sendMessage(msg);
                    } else if (change >= -2 && change <= 2 ) {
                        Message msg = new Message();
                        msg.what = 3;
                        mHandler.sendMessage(msg);
                    }*/
                }
            }
        }.start();

    }

    protected void Adjust(){
        new Thread(){
            @Override
            public void run() {
                super.run();
                while (check%2 == 0){
                    try{
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    if ((angle - var)>180){
                        x = angle-360-var;
                        Log.d("xxxx", "x1: "+ x);
                    }else if((angle- var)<-180){
                        Log.d("xxxx", "x2: "+ x);
                        x = angle+360-var;
                    }else{
                        Log.d("xxxx", "x3: "+ x);
                        x =angle- var;
                    }
                }
            }
        }.start();
    }

}


