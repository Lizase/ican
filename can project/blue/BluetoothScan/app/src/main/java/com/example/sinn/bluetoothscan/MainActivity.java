package com.example.sinn.bluetoothscan;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    blue bluetoothScanner;
    blue blue;
    Button buttonScan;
    TextView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView)findViewById(R.id.listViewBle);
        view = (TextView)findViewById(R.id.textView);
        //bluetoothScanner = new BluetoothScanner(MainActivity.this, listView);
        bluetoothScanner = new blue(MainActivity.this, listView,view);
        buttonScan = (Button)findViewById(R.id.buttonScan);
        buttonScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // bluetoothScanner.scanLeDevice(true);
                bluetoothScanner.scanLeDevice(true);
            }
        });
    }
}
