package com.example.weiting.userfollow;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.Message;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import android.os.Handler;
import android.widget.TextView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    SensorManager  mySensorManager;
    int var = 0;
    int check = 0;
    TextView view;
    Button button2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button2 = (Button) findViewById(R.id.button);
        view = (TextView) findViewById(R.id.textView);
        mySensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        button2.setOnClickListener(start);
        updateThread();
    }
    private Button.OnClickListener start = new Button.OnClickListener() {
        public void onClick(View v) {
            check++;
            if(check%2 == 1) {
                onPause();
            }
            else {
                updateThread();
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
                var = (int)values[0];
                 view.setText("current:"+var);

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

    protected void updateThread(){
        new Thread(){
            public void run(){
                while(check%2 != 1) {
                    try {
                        Thread.sleep(500);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    String url = "http://140.134.26.143/ican/php/UploadActionCode.php?id=1&action="+var;
                    OkHttpClient mOkHttpClient = new OkHttpClient();
                    final Request request = new Request.Builder().url(url).build();

                    Call call = mOkHttpClient.newCall(request);
                    call.enqueue(new Callback() {
                        @Override
                        public void onFailure(Request request, IOException e) {

                        }

                        @Override
                        public void onResponse(Response response) throws IOException {
                            String json = response.body().toString();
                        }
                    });

                }

            }
        }.start();

    }
}
