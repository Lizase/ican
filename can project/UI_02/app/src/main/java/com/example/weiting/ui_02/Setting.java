package com.example.weiting.ui_02;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;
import com.example.weiting.ui_02.warring.warringService;

import org.json.JSONArray;
import org.json.JSONObject;

public class Setting extends AppCompatActivity {
    SQLiteDatabase dbrw;
    Data[] menuData;
    JSONArray jsonArray;
    ListView listView;
    myAdapter adapter;
    public ImageView imageView;
    public Switch toggleButton;
    private TextView mode;
    String result = " ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notify);

        //MyDBHelper dbhelper = new MyDBHelper(this);
        //dbrw = dbhelper.getWritableDatabase();
        toggleButton = (Switch) findViewById(R.id.switch1);
        imageView = (ImageView) findViewById(R.id.imageView3);
        mode = (TextView) findViewById(R.id.mode);
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                toggleButton.setChecked(isChecked);
                //使用三目运算符来响应按钮变换的事件
                Log.d("isCheck", "a" + isChecked);
                if (isChecked) {
                    Intent warring_intent = new Intent();
                    warring_intent.setClass(Setting.this, warringService.class);
                    startService(warring_intent);
                } else {
                    Intent warring_intent = new Intent();
                    warring_intent.setClass(Setting.this, warringService.class);
                    stopService(warring_intent);
                }
                imageView.setImageResource(isChecked ? R.drawable.notify : R.drawable.notifyoff);
                mode.setText(isChecked ? "開啟" : "關閉");

            }
        });

       /* String[] colum ={"icanID"};
        Cursor c;
        c = dbrw.query("myTable",colum,null,null,null,null,null);
        if(c.getCount()>0){
            c.moveToFirst();
            for(int i = 0;i<c.getCount();i++){
                Log.d("sql",c.getString(i)+"\n");
                c.moveToNext();
            }
        }*/
        getDevice();

        adapter = new myAdapter(menuData, R.layout.menu_list, Setting.this);
        listView = (ListView) findViewById(R.id.deviceList);



    }

    private void getDevice() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = "";
                url = "http://140.134.26.143/ican/ican/Ashow.php?id=caps9129";
                result = canDBconnect.updateData(url);
                try {
                    jsonArray = new JSONArray(result);
                    Data[] menuData = new Data[jsonArray.length()];
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonData = jsonArray.getJSONObject(i);
                        menuData[i] = new Data();
                        Log.d("setting", jsonData.getString("ican_ID"));
                        menuData[i].name = jsonData.getString("ican_ID");
                        menuData[i].photo = R.drawable.deviceimage;
                    }
                    final myAdapter adapter = new myAdapter(menuData, R.layout.menu_list, Setting.this);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            listView.setAdapter(adapter);
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                                @Override
                                public void onItemClick(AdapterView<?> parent,View view,int position,long id){  //第幾個item被點選
                                    setContentView(R.layout.device_inform);
                                }
                            });
                        }
                    });

                } catch (Exception e) {

                }
            }
        }).start();
    }
}