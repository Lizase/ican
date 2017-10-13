package com.example.weiting.ui_02.following;

import android.content.Intent;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.example.weiting.ui_02.myAdapter;
import com.example.weiting.ui_02.Data;
import com.example.weiting.ui_02.R;
import com.example.weiting.ui_02.contorller;
import com.example.weiting.ui_02.voice;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by Weiting on 2017/8/16.
 */

public class follow extends AppCompatActivity {
    SensorManager mySensorManager;
    String result ="";
    ListView list;
    blue blue;
    int change = 0;
    int var = 0;
    int count = 0;
    int deviceVar = 0;
    int check = 0;
    TextView view,view2,view3,view4;
    Button button2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_main3);
        //******************drawer*********************
        Data[] menuData = new Data[4];
        for(int i = 0;i<4;i++){
            menuData[i] = new Data();
        }
        menuData[0].name = "使用者";
        menuData[0].photo = R.drawable.user;
        menuData[1].name = "首頁";
        menuData[1].photo = R.drawable.home;
        menuData[2].name = "訊息通知";
        menuData[2].photo = R.drawable.mail;
        menuData[3].name = "設定";
        menuData[3].photo = R.drawable.settings;
        myAdapter adapter = new myAdapter(menuData,R.layout.menu_list,follow.this);
        ListView listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent,View view,int position,long id){  //第幾個item被點選
                if(position == 0){

                }
                else if(position == 1){

                }
                else if(position== 2){

                }
                else if(position== 3){

                }
            }
        });
        Data[] contentData = new Data[2];
        for(int i = 0;i<2;i++){
            contentData[i] = new Data();
        }
        contentData[0].name = "Controller";
        contentData[1].name = "Voice control";

        contentData[0].photo = R.drawable.controller;
        contentData[1].photo = R.drawable.voice;

        GridView gridView = (GridView) findViewById(R.id.gridview);
        myAdapter menuAdapter = new myAdapter(contentData,R.layout.mode_page_bar,follow.this);
        gridView.setNumColumns(2);
        gridView.setAdapter(menuAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    Intent intent = new Intent();
                    intent.setClass(follow.this,contorller.class);
                    startActivity(intent);
                }
                else if(position == 1){
                    Intent intent = new Intent();
                    intent.setClass(follow.this,voice.class);
                    startActivity(intent);
                }
            }
        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout3);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        list = (ListView)findViewById(R.id.listViewBle);
        button2 = (Button) findViewById(R.id.buttonScan);
        view = (TextView) findViewById(R.id.textView);
        view2 = (TextView) findViewById(R.id.textView5);
        view3 = (TextView) findViewById(R.id.textView6);
        view4 = (TextView) findViewById(R.id.textView7);
        mySensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        blue = new blue(follow.this, list,view);
        blue.scanLeDevice(true);
        button2.setOnClickListener(start);
        getdateThread();
    }
    private Handler mHandler = new Handler(){
      @Override
        public void handleMessage(Message msg){
          switch (msg.what){
              case 1:
                  view2.setText("向右"+change);
                  view4.setText("current:"+var);
                  view3.setText("device:"+deviceVar);
                  break;
              case 2:
                  view2.setText("向左"+change);
                  view4.setText("current:"+var);
                  view3.setText("device:"+deviceVar);
                  break;
              case 3:
                  view2.setText("向前"+change);
                  view4.setText("current:"+var);
                  view3.setText("device:"+deviceVar);
                  break;
          }
      }
    };
    private Button.OnClickListener start = new Button.OnClickListener() {
        public void onClick(View v) {
           // blue.scanLeDevice(true);
            check++;
            if(check%2 == 1) {
                onPause();
            }
            else {
                getdateThread();
                onResume();
            }
        }
    };
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

    protected void getdateThread(){
        new Thread(){
            public void run(){
                while(check%2 != 1) {
                    try {
                        Thread.sleep(800);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if ((deviceVar - var)>180){
                        change = deviceVar-360-var;
                    }else if((deviceVar- var)<-180){
                        change = deviceVar+360-var;
                    }else{
                        change = deviceVar- var;
                    }

                    if ((change) > 45 ) {
                        Message msg = new Message();
                        msg.what = 1;
                        mHandler.sendMessage(msg);
                    } else if((change) < -45){
                        Message msg = new Message();
                        msg.what = 2;
                        mHandler.sendMessage(msg);
                    }else if((change) > -45 && change < 45){
                        Message msg = new Message();
                        msg.what = 3;
                        mHandler.sendMessage(msg);
                    }
                    String url="http://140.134.26.143/ican/php/GetActionCode.php?id=3&action="+var;
                    if (blue.found == true)
                    {
                        int rssi, major, minor;
                        rssi = blue.rssiValue;
                        major = blue.major;
                        minor = blue.minor;
                        count++;
                        String url2= "http://140.134.26.143/ican/ican/Aanalysis.php?id="+count+"&rssi=" + -rssi + "&major="+major+"&minor="+minor;
                        OkHttpClient mOkHttpClient = new OkHttpClient();
                        final Request request = new Request.Builder().url(url2).build();
                        Call call = mOkHttpClient.newCall(request);
                        call.enqueue(new Callback() {
                            @Override
                            public void onFailure (Request request, IOException e){
                            }
                            @Override
                            public void onResponse (Response response) throws IOException {
                                String json = response.body().string();
                                Log.d("json",json);
                            }
                        });
                    }
                    OkHttpClient mOkHttpClient = new OkHttpClient();
                    final Request request = new Request.Builder().url(url).build();
                    Call call = mOkHttpClient.newCall(request);
                    call.enqueue(new Callback() {
                        @Override
                        public void onFailure (Request request, IOException e){
                        }
                        @Override
                        public void onResponse (Response response) throws IOException {
                            String json = response.body().string();
                            Log.d("json",json);
                            deviceVar = Integer.parseInt(json);
                            Log.d("json","D"+deviceVar);
                        }
                    });
                }
            }
        }.start();
    }
    //************************drawer*****************************
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout3);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
