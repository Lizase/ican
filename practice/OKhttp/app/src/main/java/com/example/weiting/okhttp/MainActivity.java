package com.example.weiting.okhttp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    class Data{
        Reslut result;
        class Reslut{
            Results[] results;
            class Results{
                String id;
                String action;
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
         });

    }

    BroadcastReceiver actionReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String actionJson = intent.getExtras().getString("json");
            Gson gson = new Gson();
            Data data = gson.fromJson(actionJson,Data.class);
            String[] list_item=new String[data.result.results.length];
            for(int i=0;i<data.result.results.length;i++){
                list_item[i] = new String();
                list_item[i] = "\nID:+:"+data.result.results[i].id;
                list_item[i] = "\nAction"+data.result.results[i].action;
            }
            AlertDialog.Builder dialog_list = new AlertDialog.Builder(MainActivity.this);
            dialog_list.setTitle("行駛方向");
            dialog_list.setItems(list_item,null);
            dialog_list.show();
        }
    };
    IntentFilter intentFilter = new IntentFilter("Action");
    registerReceiver(actionReceiver,intentFilter);

}
