package com.example.weiting.devicefollow;

import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.Message;
import android.os.StrictMode;
import android.view.View;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    SensorManager  mySensorManager;
    int var = 0;
    int userValue = 0;
    int check = 0;
    int change = 0;
    Button button2;
    TextView textview;
    TextView textview2;
    TextView textview3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textview = (TextView) findViewById(R.id.textView);
        textview2 = (TextView)findViewById(R.id.textView2);
        textview3 = (TextView)findViewById(R.id.textView3);
        button2 = (Button) findViewById(R.id.button);
        mySensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        button2.setOnClickListener(start);
        getdateThread();
    }
    private Button.OnClickListener start = new Button.OnClickListener() {
        public void onClick(View v) {
            check++;
            if(check%2 == 1) {
                onPause();
            }
            else {
                onResume();
                getdateThread();
            }
        }
    };
    private SensorListener mySensorListener = new SensorListener(){



        @Override
        public void onAccuracyChanged(int sensor, int accuracy) {}
        @Override
        public void onSensorChanged(int sensor, float[] values) {
            if(sensor == SensorManager.SENSOR_ORIENTATION){
                var = (int)values[0];
                textview.setText("current:"+var);
                textview2.setText("uservalue"+userValue);
                if ((userValue - var)>180){
                    change = userValue-360-var;
                }else if((userValue - var)<-180){
                    change = userValue+360-var;
                }else{
                    change = userValue - var;
                }

                if ((change) > 45 ) {
                    textview3.setText("向右"+change);

                } else if((change) < -45){
                    textview3.setText("向左"+change);

                }else if((change) > -45 && change < 45){
                    textview3.setText("向前"+change);
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
                        Thread.sleep(500);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    OkHttpClient mOkHttpClient = new OkHttpClient();
                    final Request request = new Request.Builder().url("http://140.134.26.143/ican/php/GetActionCode.php?id=1").build();
                    Call call = mOkHttpClient.newCall(request);
                    call.enqueue(new Callback() {
                        @Override
                        public void onFailure (Request request, IOException e){

                        }

                        @Override
                        public void onResponse (Response response) throws IOException {
                            String json = response.body().string();
                            userValue = Integer.parseInt(json);
                        }
                    });

                }

            }
        }.start();

    }

}
