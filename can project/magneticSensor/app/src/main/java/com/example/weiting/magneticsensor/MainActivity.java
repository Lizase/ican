package com.example.weiting.magneticsensor;

import android.app.Activity;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Message;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;


public class MainActivity extends Activity {

    //gen
    TextView myTextView2;
    //x
    TextView myTextView3;
    //y
    TextView myTextView4;
//z

    //acc
    TextView myTextView5;
    //x
    TextView myTextView6;
    //y
    TextView myTextView7;
//z

    //ori
    TextView myTextView8;
    //x
    TextView myTextView9;
    //y
    TextView myTextView10;
//z
    Button button2;
    int check = 0;
    int var = 0;
    int var2 = 0;
    int change = 0;
    SensorManager  mySensorManager;
//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()
                .penaltyLog()
                .build());


        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .penaltyLog()
                .penaltyDeath()
                .build());
        myTextView2 = (TextView) findViewById(R.id.myTextView2);
        myTextView3 = (TextView) findViewById(R.id.myTextView3);
        myTextView4 = (TextView) findViewById(R.id.myTextView4);
        myTextView5 = (TextView) findViewById(R.id.myTextView5);
        myTextView6 = (TextView) findViewById(R.id.myTextView6);
        myTextView7 = (TextView) findViewById(R.id.myTextView7);
        myTextView8 = (TextView) findViewById(R.id.myTextView8);
        myTextView9 = (TextView) findViewById(R.id.myTextView9);
        myTextView10 = (TextView) findViewById(R.id.myTextView10);
        button2 = (Button) findViewById(R.id.button2);
        mySensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        button2.setOnClickListener(start);
        storeThread();

    }
    private Button.OnClickListener start = new Button.OnClickListener() {
        public void onClick(View v) {
            check++;
            if(check%2 == 1)
                onPause();
            else
                onResume();

        }
    };
    private SensorListener mySensorListener = new SensorListener(){



        @Override
        public void onAccuracyChanged(int sensor, int accuracy) {}
        @Override
        public void onSensorChanged(int sensor, float[] values) {


             if(sensor == SensorManager.SENSOR_MAGNETIC_FIELD){
                myTextView2.setText("Current Magnetic x："+(int)values[0]);
                myTextView3.setText("Current Magnetic y："+(int)values[1]);
                myTextView4.setText("Current Magnetic z："+(int)values[2]);
            }else if(sensor == SensorManager.SENSOR_ACCELEROMETER){
                myTextView5.setText("Current Accelero x："+(int)values[0]);
                myTextView6.setText("Current Accelero y："+(int)values[1]);
                myTextView7.setText("Current Accelero z："+(int)values[2]);
            }else if(sensor == SensorManager.SENSOR_ORIENTATION){
                 var = (int)values[0];
                myTextView8.setText("Current Oraenttation x："+var);
                myTextView9.setText("Current Oraenttation y："+var2);
                myTextView10.setText("Current Oraenttation z："+(int)values[2]);


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


    private Handler mHandler = new Handler(){
        @Override
         public void handleMessage(Message msg){
            switch (msg.what){
                 case 1:
                    //DBConnector.update("UPDATE car_follow SET Direction ='R' WHERE Car_ID ='phone'");
                     Toast.makeText(MainActivity.this,"向右!!"+change,Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    //DBConnector.update("UPDATE car_follow SET Direction ='L'WHERE Car_ID ='phone'");
                    Toast.makeText(MainActivity.this,"向左!!"+change,Toast.LENGTH_SHORT).show();
                    break;

            }
        }
    };
    protected void storeThread(){
        var2 = var;
        new Thread(){
            public void run(){
               while(true) {
                   try {
                       Thread.sleep(300);

                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }
                   Message msg = new Message();
                   if ((var2 - var)>180){
                          change = var2-360-var;
                   }else if((var2 - var)<-180){
                          change = var2+360-var;
                   }else{
                          change = var2 - var;
                   }

                   if ((change) > 35 ) {
                       var2 = var;
                       msg.what = 2;
                       mHandler.sendMessage(msg);
                   } else if((change) < -35 ){
                       var2 = var;
                       msg.what = 1;
                       mHandler.sendMessage(msg);
                   }
               }

            }
        }.start();

    }



}
