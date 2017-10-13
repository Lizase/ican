package com.example.weiting.bscan;

/**
 * Created by Weiting on 2017/7/27.
 */

public class scan {
    package com.example.a4789.s_parking;

import com.google.zxing.integration.android.*;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadFactory;

    public class InsideNavigation extends AppCompatActivity
            implements NavigationView.OnNavigationItemSelectedListener {

        private DrawerLayout drawerLayout_Menu;
        private NavigationView navigationView_Menu;
        private Toolbar toolbar_InsideNavigation;
        private Bundle insideNavigationBundle;
        private TextView textView_navigationViewName,textView_navigationViewEmail;
        private Intent intoMain = new Intent(),intoAlt = new Intent(), intoResData=new Intent(),intoOutsiteNav=new Intent(),intoFree=new Intent();

        private Beacon beacon01, beacon02, beacon03, beacon04, beacon05, beacon06, beacon07, beacon08;
        private Beacon[] beaconList = {beacon01, beacon02, beacon03, beacon04, beacon05, beacon06, beacon07, beacon08};
        private BluetoothAdapter bluetoothAdapter;
        private ImageView imageView_Map;
        private ImageView[] imageView_car = new ImageView[43];

        private Button buttonChoiceParkingSpace[] = new Button[5];

        private int startByte, major, minor,connectState, userParkingSpaceState, carParkNumber;
        private boolean isFound;
        private byte[] uuidBytes = new byte[16];
        private String uuidString, mac;

        private Bitmap bitmap;
        private Boolean stopActivity;
        private Canvas canvas;
        private Paint paint;
        private int width, height,start,nowAt, i, tempStart, lastStart;
        private ParkingPoint[] trun=new ParkingPoint[7];
        private ParkingPoint[] park=new ParkingPoint[44];
        private int[] iBeaconComparison=new int[7];
        private JSONObject jsonObject, parkingSpacejsonObject, userParkingSpaceObject;
        private JSONArray jsonArray;
        private DetermineReserved determineReserved;
        private GetSpaceData getSpaceData;
        private ScanParkSpace scanParkSpace;
        private boolean checkFinish =true;

        private Handler handler, scanSpacehandler, parkingSpaceHandler;
        private Thread thread = null, scanSpaceThread = null, parkingSpaceThread = null;

        private Connect connect = new Connect();
        private String parkingSpaceState, parkNumber = "parkNumber=1", scanContent, scanFormat, userParkingSpace, userParkingSpaceData;
        private String parkingSpaceURL = "http://140.134.26.143:9427/sparking/SpaceState.php",
                userParkingSpaceURL = "http://140.134.26.143:9427/sparking/alterUserParkingSpace.php";

        private static final int DRAWNAVIGATIONLINE = 1 , FINISH = 0, DRAWCARONPARK = 1, SCANQRCODE = 2, SHOWUSERDATA = 1;

        AlertDialog.Builder builder;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_inside_navigation);
            stopActivity=false;
            InterfaceInit();

        /*sensoriBeacon();
        //SetButtonParkingChoice();
        scanParkingSpace();
        //SensorParkingSpace();

        thread.start();
        scanSpaceThread.start();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ConfigChoiceNavigation();*/

        }

        /*private void SetButtonParkingChoice() {
            buttonChoiceParkingSpace[0].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (imageView_car[2].getVisibility() == View.INVISIBLE){
                        nowAt = 3;
                    }
                }
            });
            buttonChoiceParkingSpace[1].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (imageView_car[7].getVisibility() == View.INVISIBLE) {
                        nowAt = 8;
                    }
                }
            });
            buttonChoiceParkingSpace[2].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (imageView_car[12].getVisibility() == View.INVISIBLE) {
                        nowAt = 13;
                    }
                }
            });
            buttonChoiceParkingSpace[3].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (imageView_car[30].getVisibility() == View.INVISIBLE) {
                        nowAt = 31;
                    }
                }
            });
            buttonChoiceParkingSpace[4].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (imageView_car[35].getVisibility() == View.INVISIBLE) {
                        nowAt = 36;
                    }
                }
            });

        }*/
        private void ConfigChoiceNavigation() {
            if (imageView_car[2].getVisibility() == View.INVISIBLE) {
                nowAt = 3;
            }else if (imageView_car[1].getVisibility() == View.INVISIBLE) {
                nowAt = 2;
            }else if (imageView_car[0].getVisibility() == View.INVISIBLE) {
                nowAt = 1;
            }
        /*
        if (imageView_car[12].getVisibility() == View.INVISIBLE) {
            nowAt = 13;
        }else if (imageView_car[7].getVisibility() == View.INVISIBLE) {
            nowAt = 8;
        }else if (imageView_car[30].getVisibility() == View.INVISIBLE) {
            nowAt = 31;
        }else if (imageView_car[35].getVisibility() == View.INVISIBLE) {
            nowAt = 36;
        }else if (imageView_car[2].getVisibility() == View.INVISIBLE){
            nowAt = 3;
        }*/
        }

        private void InterfaceInit() {
            insideNavigationBundle=this.getIntent().getExtras();
            toolbar_InsideNavigation = (Toolbar) findViewById(R.id.Toolbar_InsideNavigation);
            drawerLayout_Menu = (DrawerLayout) findViewById(R.id.DrawerLayout_Menu);
            navigationView_Menu = (NavigationView) findViewById(R.id.NavigationView_Menu);


            intoMain.setClass(this, MainActivity.class);
            intoAlt.setClass(this, AlterUserData.class);
            intoOutsiteNav.setClass(this, OutsideNavigation.class);
            intoResData.setClass(this, ReservationDataActivity.class);
            intoFree.setClass(this, ParkingfeeActivity.class);

            setSupportActionBar(toolbar_InsideNavigation);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawerLayout_Menu, toolbar_InsideNavigation, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

            drawerLayout_Menu.setDrawerListener(toggle);
            navigationView_Menu.setNavigationItemSelectedListener(this);

            toggle.syncState();
            width = getWindowManager().getDefaultDisplay().getWidth();
            height = getWindowManager().getDefaultDisplay().getHeight();
            Log.i("size ", "+++++ " + width + "+++++" + height);

            imageView_Map = (ImageView) findViewById(R.id.ImageView_InDoorGPSMap);
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            canvas = new Canvas(bitmap);
            imageView_Map.setImageBitmap(bitmap);
            determineReserved=new DetermineReserved();
            getSpaceData=new GetSpaceData();
            scanParkSpace=new ScanParkSpace();

            paint = new Paint();
            paint.setColor(Color.BLUE);
            paint.setStrokeWidth(15);
            start = 0;
            tempStart = 0;
            lastStart = 0;

            if(navigationView_Menu.getHeaderCount() > 0) {
                View menuHeader = navigationView_Menu.getHeaderView(0);
                textView_navigationViewName =(TextView)menuHeader.findViewById(R.id.TextView_Name);
                textView_navigationViewEmail=(TextView)menuHeader.findViewById(R.id.TextView_Email);
                textView_navigationViewName.setText(insideNavigationBundle.getString("username"));
                textView_navigationViewEmail.setText(insideNavigationBundle.getString("email"));
            }

            imageView_car[0] = (ImageView) findViewById(R.id.ImageView_car1);
            imageView_car[1] = (ImageView) findViewById(R.id.ImageView_car2);
            imageView_car[2] = (ImageView) findViewById(R.id.ImageView_car3);

            imageView_car[3] = (ImageView) findViewById(R.id.ImageView_car4);
            imageView_car[4] = (ImageView) findViewById(R.id.ImageView_car5);
            imageView_car[5] = (ImageView) findViewById(R.id.ImageView_car6);
            imageView_car[6] = (ImageView) findViewById(R.id.ImageView_car7);
            imageView_car[7] = (ImageView) findViewById(R.id.ImageView_car8);
            imageView_car[8] = (ImageView) findViewById(R.id.ImageView_car9);
            imageView_car[9] = (ImageView) findViewById(R.id.ImageView_car10);
            imageView_car[10] = (ImageView) findViewById(R.id.ImageView_car11);
            imageView_car[11] = (ImageView) findViewById(R.id.ImageView_car12);
            imageView_car[12] = (ImageView) findViewById(R.id.ImageView_car13);
            imageView_car[13] = (ImageView) findViewById(R.id.ImageView_car14);
            imageView_car[14] = (ImageView) findViewById(R.id.ImageView_car15);
            imageView_car[15] = (ImageView) findViewById(R.id.ImageView_car16);
            imageView_car[16] = (ImageView) findViewById(R.id.ImageView_car17);
            imageView_car[17] = (ImageView) findViewById(R.id.ImageView_car18);
            imageView_car[18] = (ImageView) findViewById(R.id.ImageView_car19);
            imageView_car[19] = (ImageView) findViewById(R.id.ImageView_car20);
            imageView_car[20] = (ImageView) findViewById(R.id.ImageView_car21);
            imageView_car[21] = (ImageView) findViewById(R.id.ImageView_car22);
            imageView_car[22] = (ImageView) findViewById(R.id.ImageView_car23);
            imageView_car[23] = (ImageView) findViewById(R.id.ImageView_car24);
            imageView_car[24] = (ImageView) findViewById(R.id.ImageView_car25);
            imageView_car[25] = (ImageView) findViewById(R.id.ImageView_car26);
            imageView_car[26] = (ImageView) findViewById(R.id.ImageView_car27);
            imageView_car[27] = (ImageView) findViewById(R.id.ImageView_car28);
            imageView_car[28] = (ImageView) findViewById(R.id.ImageView_car29);
            imageView_car[29] = (ImageView) findViewById(R.id.ImageView_car30);
            imageView_car[30] = (ImageView) findViewById(R.id.ImageView_car31);
            imageView_car[31] = (ImageView) findViewById(R.id.ImageView_car32);
            imageView_car[32] = (ImageView) findViewById(R.id.ImageView_car33);
            imageView_car[33] = (ImageView) findViewById(R.id.ImageView_car34);
            imageView_car[34] = (ImageView) findViewById(R.id.ImageView_car35);
            imageView_car[35] = (ImageView) findViewById(R.id.ImageView_car36);
            imageView_car[36] = (ImageView) findViewById(R.id.ImageView_car37);
            imageView_car[37] = (ImageView) findViewById(R.id.ImageView_car38);
            imageView_car[38] = (ImageView) findViewById(R.id.ImageView_car39);
            imageView_car[39] = (ImageView) findViewById(R.id.ImageView_car40);
            imageView_car[40] = (ImageView) findViewById(R.id.ImageView_car41);
            imageView_car[41] = (ImageView) findViewById(R.id.ImageView_car42);
            imageView_car[42] = (ImageView) findViewById(R.id.ImageView_car43);

        /*buttonChoiceParkingSpace[0] = (Button)findViewById(R.id.ButtonChoiceParkingSpace3);
        buttonChoiceParkingSpace[1] = (Button)findViewById(R.id.ButtonChoiceParkingSpace8);
        buttonChoiceParkingSpace[2] = (Button)findViewById(R.id.ButtonChoiceParkingSpace13);
        buttonChoiceParkingSpace[3] = (Button)findViewById(R.id.ButtonChoiceParkingSpace31);
        buttonChoiceParkingSpace[4] = (Button)findViewById(R.id.ButtonChoiceParkingSpace36);*/

            trun[0] = new ParkingPoint(width*0.23f, height*0.135f);
            trun[1] = new ParkingPoint(width*0.72f, height*0.135f);
            trun[2] = new ParkingPoint(width*0.72f, height*0.30f);
            trun[3] = new ParkingPoint(width*0.72f, height*0.45f);
            trun[4] = new ParkingPoint(width*0.72f, height*0.60f);
            trun[5] = new ParkingPoint(width*0.72f, height*0.75f);
            trun[6] = new ParkingPoint(width*0.72f, height*0.86f);

            park[1] = new ParkingPoint(width*0.85f, height*0.855f,5,0);//1
            park[2] = new ParkingPoint(width*0.85f, height*0.815f,5,0);//2
            park[3] = new ParkingPoint(width*0.85f, height*0.775f,5,0);//3
            park[4] = new ParkingPoint(width*0.85f, height*0.86f,3,0);//4
            park[5] = new ParkingPoint(width*0.85f, height*0.86f,3,0);//5
            park[6] = new ParkingPoint(width*0.85f, height*0.86f,3,0);//6
            park[7] = new ParkingPoint(width*0.85f, height*0.86f,3,0);//7
            park[8] = new ParkingPoint(width*0.85f, height*0.52f,3,0);//8
            park[9] = new ParkingPoint(width*0.85f, height*0.86f,3,0);//9
            park[10] = new ParkingPoint(width*0.85f, height*0.86f,1,0);//10
            park[11] = new ParkingPoint(width*0.85f, height*0.86f,1,0);//11
            park[12] = new ParkingPoint(width*0.85f, height*0.86f,1,0);//12
            park[13] = new ParkingPoint(width*0.85f, height*0.26f,1,0);//13
            park[14] = new ParkingPoint(width*0.85f, height*0.86f,1,0);//14
            park[15] = new ParkingPoint(width*0.85f, height*0.86f,1,0);//15
            park[16] = new ParkingPoint(width*0.85f, height*0.86f,1,0);//16
            park[17] = new ParkingPoint(width*0.85f, height*0.86f,1,0);//17
            park[18] = new ParkingPoint(width*0.85f, height*0.86f,1,0);//18
            park[19] = new ParkingPoint(width*0.52f, height*0.04f,0,1);//19
            park[20] = new ParkingPoint(width*0.77f, height*0.04f,0,1);//20
            park[21] = new ParkingPoint(width*0.77f, height*0.04f,0,1);//21
            park[22] = new ParkingPoint(width*0.77f, height*0.04f,0,1);//22
            park[23] = new ParkingPoint(width*0.77f, height*0.04f,0,1);//23
            park[24] = new ParkingPoint(width*0.77f, height*0.86f,1,0);//24
            park[25] = new ParkingPoint(width*0.52f, height*0.04f,1,0);//25
            park[26] = new ParkingPoint(width*0.52f, height*0.04f,1,0);//26
            park[27] = new ParkingPoint(width*0.47f, height*0.04f,1,0);//27
            park[28] = new ParkingPoint(width*0.47f, height*0.37f,1,0);//28
            park[29] = new ParkingPoint(width*0.47f, height*0.04f,1,0);//29
            park[30] = new ParkingPoint(width*0.52f, height*0.04f,3,0);//30
            park[31] = new ParkingPoint(width*0.52f, height*0.52f,3,0);//31
            park[32] = new ParkingPoint(width*0.47f, height*0.04f,3,0);//32
            park[33] = new ParkingPoint(width*0.47f, height*0.04f,3,0);//33
            park[34] = new ParkingPoint(width*0.47f, height*0.04f,3,0);//34
            park[35] = new ParkingPoint(width*0.47f, height*0.04f,3,0);//35
            park[36] = new ParkingPoint(width*0.52f, height*0.71f,3,0);//36
            park[37] = new ParkingPoint(width*0.47f, height*0.04f,3,0);//37
            park[38] = new ParkingPoint(width*0.47f, height*0.04f,3,0);//38
            park[39] = new ParkingPoint(width*0.47f, height*0.04f,3,0);//39
            park[40] = new ParkingPoint(width*0.52f, height*0.04f,5,0);//40
            park[41] = new ParkingPoint(width*0.52f, height*0.04f,6,1);//41
            park[42] = new ParkingPoint(width*0.52f, height*0.04f,6,1);//42
            park[43] = new ParkingPoint(width*0.52f, height*0.04f,6,1);//43



        /*iBeaconComparison[0]=0;
        iBeaconComparison[1]=0;
        iBeaconComparison[2]=1;
        iBeaconComparison[3]=3;
        iBeaconComparison[4]=5;
        iBeaconComparison[5]=6;*/

            handler = new Handler();
            scanSpacehandler = new Handler();
            //handler.post(runnable);
            //beaconCount = 0;

            builder = new AlertDialog.Builder(InsideNavigation.this);

            for(int i = 0;i < beaconList.length;i++) {
                beaconList[i] = new Beacon(i+1);
            }

            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if(bluetoothAdapter == null || (!bluetoothAdapter.isEnabled())) {
                Toast.makeText(getApplicationContext(), "please open BlueTooth !", Toast.LENGTH_SHORT).show();
                Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBluetooth, 1);
            }
            else {
                Action();
                sensoriBeacon();
                //SetButtonParkingChoice();
                scanParkingSpace();
                //SensorParkingSpace();

                thread.start();
                scanSpaceThread.start();

            /*try {
                Thread.sleep(500);
                alterConditionValue = "park_number=1&section=1";
                jsonObject = connectwebserver.connect(alterConditionValue, alterConditionURL);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }*/
                ConfigChoiceNavigation();
            }
            try {
                jsonObject=determineReserved.doDetermine(insideNavigationBundle.getString("email"));
                jsonObject=getSpaceData.doDetermine(insideNavigationBundle.getString("email"), jsonObject.getInt("park_number"));
                connectState=jsonObject.getInt("state");
                if(connectState==0)
                {
                    //nowAt=jsonObject.getInt("park_space");
                    nowAt = 3;
                }
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public void scanParkingSpace(){
            scanSpaceThread = new Thread(){
                public void run(){
                    while(checkFinish) {
                        try {
                            parkingSpaceState = connect.doPost(parkingSpaceURL, parkNumber, null, null, "UTF-8");
                            System.out.println(parkingSpaceState);

                            parkingSpacejsonObject = new JSONObject(parkingSpaceState);
                            jsonArray = parkingSpacejsonObject.getJSONArray("full_space");
                            for (i = 0; i < jsonArray.length(); i++) {
                                System.out.println(jsonArray.getInt(i));
                            }

                            DrawCarOnPark();
                            Thread.sleep(1000);
                            ConfigChoiceNavigation();

                            Thread.sleep(2000);
                            Message message = new Message();
                            message.what = FINISH;
                            uiMessageParkHandler.sendMessage(message);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
        }
        private void DrawCarOnPark() throws InterruptedException{
            Message msg = new Message();
            msg.what = DRAWCARONPARK;
            uiMessageParkHandler.sendMessage(msg);
        }

        Handler uiMessageParkHandler = new Handler(){
            @Override
            public void handleMessage(Message message) {
                switch (message.what){
                    case DRAWNAVIGATIONLINE:
                        for(i = 0;i<43;i++){
                            imageView_car[i].setVisibility(View.INVISIBLE);
                        }
                        try {
                            for(i = 0; i< jsonArray.length();i++){
                                imageView_car[jsonArray.getInt(i)-1].setVisibility(View.VISIBLE);
                            }
                            jsonObject = scanParkSpace.doDetermine(3, 1);
                            System.out.println(jsonObject);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    case FINISH:

                        thread.interrupt();
                        break;
                }
                super.handleMessage(message);
            }
        };

        public void sensoriBeacon(){
            thread = new Thread(){
                public void run(){
                    while(checkFinish) {
                        try {
                            bluetoothAdapter.stopLeScan(leScanCallback);
                            Thread.sleep(200);
                            bluetoothAdapter.startLeScan(leScanCallback);
                            //Thread.sleep(200);


                            DrawNavigationLine();
                            Thread.sleep(800);
                            Message message = new Message();
                            message.what = FINISH;
                            uiMessageHandler.sendMessage(message);
                            paint = new Paint();
                            paint.setColor(Color.BLUE);
                            paint.setStrokeWidth(15);
                            //Thread.sleep(600);
                        } catch (InterruptedException e) {
                            e.printStackTrace();

                        }
                        if(start==park[nowAt].getTurnpaint()){
                            Message message = new Message();
                            message.what = SCANQRCODE;
                            uiMessageHandler.sendMessage(message);
                            checkFinish=false;
                        }
                    }
                    if(checkFinish==false){
                        bluetoothAdapter.stopLeScan(leScanCallback);
                        canvas.drawColor(0, PorterDuff.Mode.CLEAR);
                        thread.interrupt();
                        scanSpaceThread.interrupt();

                    }
                }
            };
        }

        private void DrawNavigationLine() throws InterruptedException{
            Message msg = new Message();
            msg.what = DRAWNAVIGATIONLINE;
            uiMessageHandler.sendMessage(msg);
        }

        Handler uiMessageHandler = new Handler(){
            @Override
            public void handleMessage(Message message){

                imageView_Map.setImageBitmap(bitmap);

                switch (message.what){
                    case DRAWNAVIGATIONLINE:
                        canvas.drawColor(0, PorterDuff.Mode.CLEAR);

                        Beacon tempBeacon;
                        for(int i = 0;i < beaconList.length - 1;i++) {
                            for(int j = 0;j < beaconList.length - 1 - i;j++) {
                                if(beaconList[j].getRssi() < beaconList[j+1].getRssi()) {
                                    tempBeacon = beaconList[j];
                                    beaconList[j] = beaconList[j+1];
                                    beaconList[j+1] = tempBeacon;
                                }
                            }
                        }

                        int counter=0;
                        while (counter < beaconList.length && beaconList[counter].getRssi()==0){
                            Log.i("beacons :", "" + beaconList[counter].getMinor()+"****"+beaconList[counter].getRssi()+"****"+beaconList[counter].getAvgRssi());
                            counter++;
                        }
                        //start = iBeaconComparison[beaconList[counter].getMinor()];
                        //bluetoothAdapter.stopLeScan(leScanCallback);
                        if(counter == 8){
                            start = 0;
                        }
                        else{
                            tempStart = beaconList[counter].getMinor() - 1;
                            if(tempStart == lastStart){
                                start = beaconList[counter].getMinor() - 1;
                            }
                            else{
                                lastStart = tempStart;
                            }
                        }

                        Log.i("beacon1 ", "iiiiii ");
                        canvas.drawCircle(trun[start].getX(), trun[start].getY(), 25, paint);
                        try {
                            jsonObject = scanParkSpace.doDetermine(3, 1);
                            System.out.println(jsonObject);
                            if(jsonObject.getInt("state") == 1) {

                                //canvas.drawCircle(width*0.77f, height*0.78f, 25, paint);
                            }else{

                                thread.interrupt();
                            }
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        int i = start;//   ;i<park[nowAt].getTurnpaint();i++
                        while(i != park[nowAt].getTurnpaint() && i != (park[nowAt].getTurnpaint()+1))
                        {
                            if(i<park[nowAt].getTurnpaint()) {
                                canvas.drawLine(trun[i].getX(), trun[i].getY(), trun[i + 1].getX(), trun[i + 1].getY(), paint);
                                i++;
                            }
                            else if(i>(park[nowAt].getTurnpaint()+1)){
                                canvas.drawLine(trun[i].getX(), trun[i].getY(), trun[i - 1].getX(), trun[i - 1].getY(), paint);
                                i--;
                            }
                        }
                        Log.i("i==== ", ""+i);
                        if(park[nowAt].getCheckDirection()==1) {
                            canvas.drawLine(trun[i].getX(), trun[i].getY(), park[nowAt].getX(), trun[i].getY(), paint);
                            canvas.drawLine(park[nowAt].getX(), trun[i].getY(), park[nowAt].getX(), park[nowAt].getY(), paint);
                        }
                        else if(park[nowAt].getCheckDirection()==0) {
                            canvas.drawLine(trun[i].getX(), trun[i].getY(), trun[i].getX(), park[nowAt].getY(), paint);
                            canvas.drawLine(trun[i].getX(), park[nowAt].getY(), park[nowAt].getX(), park[nowAt].getY(), paint);
                        }


                        Log.i("beacon1 ", "jjjjj ");

                        break;
                    case FINISH:

                        thread.interrupt();
                        break;
                    case SCANQRCODE:
                        if(scanFormat == null && scanContent == null){
                            builder.setTitle("QRCode");
                            builder.setMessage("Do you want to scan QRCode?");
                            builder.setCancelable(false);

                            builder.setPositiveButton("Scan", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    IntentIntegrator scanIntegrator = new IntentIntegrator(InsideNavigation.this);
                                    scanIntegrator.initiateScan();
                                    //Toast.makeText(InsideNavigation.this, "scan", Toast.LENGTH_SHORT).show();
                                }
                            });

                            builder.setNegativeButton("SKIP", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //Toast.makeText(InsideNavigation.this, "SKIP", Toast.LENGTH_SHORT).show();
                                }
                            });
                            AlertDialog alert = builder.create();
                            alert.show();
                        }
                        else{
                            alterUserParkingSpace();
                            parkingSpaceThread.start();
                        }
                        break;
                }
                super.handleMessage(message);
            }
        };

        public void onActivityResult(int requestCode, int resultCode, Intent intent){
            IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
            if(scanningResult!=null){
                scanContent = scanningResult.getContents();
                scanFormat = scanningResult.getFormatName();
                //Toast.makeText(getApplicationContext(), "Format:" + scanFormat + "\nContent:" + scanContent, Toast.LENGTH_SHORT).show();
            }else{
                //Toast.makeText(getApplicationContext(),"nothing",Toast.LENGTH_SHORT).show();
            }
        }

        public void alterUserParkingSpace(){
            parkingSpaceThread = new Thread(){
                public void run() {
                    try{
                        userParkingSpace = "email=" + insideNavigationBundle.getString("email") + "&" + scanContent;
                        userParkingSpaceData = connect.doPost(userParkingSpaceURL, userParkingSpace, null, null, "UTF-8");
                        userParkingSpaceObject = new JSONObject(userParkingSpaceData);
                        userParkingSpaceState = userParkingSpaceObject.getInt("state");

                        showUserData();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Message message = new Message();
                    message.what = FINISH;
                    userParkingMessageHandler.sendMessage(message);
                }
            };
        }

        private void showUserData() throws InterruptedException{
            Message msg = new Message();
            msg.what = SHOWUSERDATA;
            userParkingMessageHandler.sendMessage(msg);
        }

        Handler userParkingMessageHandler = new Handler() {
            @Override
            public void handleMessage(Message message) {
                switch (message.what){
                    case SHOWUSERDATA:
                        if(userParkingSpaceState == 0){
                            carParkNumber = Integer.parseInt(scanContent.substring(35));
                            builder.setTitle("Welcome");
                            builder.setMessage("Hello!" + insideNavigationBundle.getString("name") + "\nYour car at NO." + carParkNumber);
                            builder.setCancelable(false);

                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //Toast.makeText(InsideNavigation.this, "scan", Toast.LENGTH_SHORT).show();
                                }
                            });
                            AlertDialog alert = builder.create();
                            alert.show();
                            //Toast.makeText(getApplicationContext(),"sucess", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            builder.setTitle("Error");
                            builder.setMessage("Please scan QRCode, again!");
                            builder.setCancelable(false);

                            builder.setPositiveButton("Scan", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    scanContent = null;
                                    scanFormat = null;
                                    Message message = new Message();
                                    message.what = SCANQRCODE;
                                    uiMessageHandler.sendMessage(message);
                                }
                            });
                            //Toast.makeText(getApplicationContext(),"fail", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case FINISH:

                        break;
                }
            }
        };


        private void Action() {
            bluetoothAdapter.startLeScan(leScanCallback);
        }

    ***private  BluetoothAdapter.LeScanCallback leScanCallback = new BluetoothAdapter.LeScanCallback() {
            @Override
            public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
                startByte = 2;
                isFound = false;

                for(int i = 0;i<beaconList.length;i++){
                    beaconList[i].allClear();
                }

                while(startByte <= 5) {
                    if(((int) scanRecord[startByte + 2] & 0xff) == 0x02 && ((int) scanRecord[startByte + 3] & 0xff) == 0x15) {
                        isFound = true;
                        break;
                    }
                    startByte++;
                }

                if(isFound) {
                    mac = device.getAddress();
                    System.arraycopy(scanRecord, startByte + 4, uuidBytes, 0, 16);
                    uuidString = bytesToHex(uuidBytes);
                    uuidString = uuidString.substring(0, 8) + "-"
                            + uuidString.substring(8, 12)+ "-"
                            + uuidString.substring(12, 16) + "-"
                            + uuidString.substring(16, 20) + "-"
                            + uuidString.substring(20, 32);

                    major = (scanRecord[startByte + 20] & 0xff) * 0x100 + (scanRecord[startByte + 21] & 0xff);
                    minor = (scanRecord[startByte + 22] & 0xff) * 0x100 + (scanRecord[startByte + 23] & 0xff);

                    for(int i = 0;i < beaconList.length; i++) {
                        if(beaconList[i].getMinor() == minor) {
                            beaconList[i].setBeacon(device.getName(), mac, uuidString, major, rssi);
                            break;
                        }
                    }
                    System.out.println(mac + minor + rssi);
                /*Beacon tempBeacon;
                for(int i = 0;i < beaconList.length - 1;i++) {
                    for(int j = 0;j < beaconList.length - 1 - i;j++) {
                        if(beaconList[j].getRssi() < beaconList[j+1].getRssi()) {
                            tempBeacon = beaconList[j];
                            beaconList[j] = beaconList[j+1];
                            beaconList[j+1] = tempBeacon;
                        }
                    }
                }

                int counter=0;
                while (beaconList[counter].getRssi()==0 && counter<beaconList.length){
                    Log.i("beacons :", "" + beaconList[counter].getMinor()+"****"+beaconList[counter].getRssi()+"****"+beaconList[counter].getAvgRssi());
                    counter++;
                }
                //start = iBeaconComparison[beaconList[counter].getMinor()];
                //bluetoothAdapter.stopLeScan(leScanCallback);
                start = beaconList[counter].getMinor() - 1;*/
                /*tempStart[nowAtCounter] = beaconList[counter].getMinor() - 1;
                nowAtCounter++;
                if(nowAtCounter == 2){
                    if(tempStart[0] == tempStart[1] && tempStart[1] == tempStart[2])start = tempStart[0];
                    nowAtCounter = 0;
                }*/
                }
            }

        };

        private String bytesToHex(byte[] bytes) {
            char[] hexArray = "0123456789ABCDEF".toCharArray();

            char[] hexChars = new char[bytes.length * 2];
            for (int j = 0; j < bytes.length; j++) {
                int v = bytes[j] & 0xFF;
                hexChars[j * 2] = hexArray[v >>> 4];
                hexChars[j * 2 + 1] = hexArray[v & 0x0F];
            }
            return new String(hexChars);
        }

        @Override
        public void onBackPressed() {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.DrawerLayout_Menu);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.inside_navigation, menu);
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

        @SuppressWarnings("StatementWithEmptyBody")
        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            // Handle navigation view item clicks here.
            int id = item.getItemId();

            if (id == R.id.NavigationView_navigation) {
                intoOutsiteNav.putExtras(insideNavigationBundle);
                startActivity(intoOutsiteNav);
            }  else if (id == R.id.NavigationView_reservation) {
                intoResData.putExtras(insideNavigationBundle);
                startActivity(intoResData);
            } else if (id == R.id.NavigationView_AlterUserData) {
                intoAlt.putExtras(insideNavigationBundle);
                startActivity(intoAlt);
            } else if (id == R.id.NavigationView_AboutUs) {

            } else if (id == R.id.NavigationView_SignOut) {
                startActivity(intoMain);
            }else if (id == R.id.NavigationView_ParkingFee) {
                startActivity(intoFree);
            }

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.DrawerLayout_Menu);
            drawer.closeDrawer(GravityCompat.START);
            return true;
        }
        @Override
        public  void onDestroy(){
            super.onDestroy();
            bluetoothAdapter.stopLeScan(leScanCallback);
        /*Message message = new Message();
        message.what = FINISH;
        uiMessageParkHandler.sendMessage(message);
        handler.sendMessage(message);*/
            checkFinish=false;
            thread.interrupt();
            scanSpaceThread.interrupt();



        }
        public  void onResume(){
            super.onResume();
            if(!stopActivity){
                stopActivity=true;
            }
            else {
                //bluetoothAdapter.startLeScan(leScanCallback);
                checkFinish=true;
                sensoriBeacon();
                //SetButtonParkingChoice();
                scanParkingSpace();
                thread.start();
                scanSpaceThread.start();
                //System.out.println("11111111111");
            }

        }

        public  void onStop(){
            super.onStop();
            bluetoothAdapter.stopLeScan(leScanCallback);
        /*Message message = new Message();
        message.what = FINISH;
        uiMessageParkHandler.sendMessage(message);
        handler.sendMessage(message);*/
            checkFinish=false;
            thread.interrupt();
            scanSpaceThread.interrupt();

        }

    }

}
