package com.example.weiting.ui_02;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
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
import android.widget.Toast;
import java.util.ArrayList;
import android.support.v7.app.AppCompatActivity;

import com.example.weiting.ui_02.following.follow;

/**
 * Created by Weiting on 2017/7/24.
 */

public class voice extends AppCompatActivity {
    protected static final int RESULT_SPEECH = 1;
    private Button btnSpeak;
    private TextView voiceText;
    String result = "";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_main4);
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
        myAdapter adapter = new myAdapter(menuData,R.layout.menu_list,voice.this);
        ListView listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent,View view,int position,long id){  //第幾個item被點選
                if(position == 0){

                }
                else if(position == 1){
                    Intent intent = new Intent();
                    intent.setClass(voice.this,MainActivity.class);
                    startActivity(intent);
                }
                else if(position== 2){
                    Intent intent = new Intent();
                    intent.setClass(voice.this,feeback.class);
                    startActivity(intent);

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
        contentData[1].name = "Can follow";

        contentData[0].photo = R.drawable.controller;
        contentData[1].photo = R.drawable.follow;


        GridView gridView = (GridView) findViewById(R.id.gridview);
        myAdapter menuAdapter = new myAdapter(contentData,R.layout.mode_page_bar,voice.this);
        gridView.setNumColumns(2);
        gridView.setAdapter(menuAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    Intent intent = new Intent();
                    intent.setClass(voice.this,contorller.class);
                    startActivity(intent);
                }
                else if(position == 1){
                    Intent intent = new Intent();
                    intent.setClass(voice.this,follow.class);
                    startActivity(intent);

                }
            }
        });
        btnSpeak = (Button)findViewById(R.id.voice);
        voiceText = (TextView)findViewById(R.id.voicetext);
        btnSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");
                try {
                        startActivityForResult(intent, RESULT_SPEECH);
                        voiceText.setText("");
                } catch (ActivityNotFoundException a) {
                        Toast t = Toast.makeText(getApplicationContext(), "Opps! Your device doesn't support Speech to Text", Toast.LENGTH_SHORT);
                        t.show();
                }
            }
        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout4);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
    }
    //*****************drawer************************
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout4);
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

    @Override
	    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            switch (requestCode) {
                case RESULT_SPEECH: {
                    if (resultCode == RESULT_OK && null != data) {
                        ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        voiceText.setText(text.get(0));
                        textAnalysis();
                    }
                    break;
                }
            }
        }

    public void textAnalysis(){
        String text = "";
        Log.d("msg","center!!");
        text = voiceText.getText().toString();
        Log.d("msg",text);
        if(text.indexOf("前")>=0 || text.indexOf("forward")>=0 || text.indexOf("Pad")>=0 ){

            update("http://140.134.26.143/ican/php/UploadActionCode.php?id=1&action=ahead");
        }else if(text.indexOf("右")>=0 || text.indexOf("right")>=0){
            update("http://140.134.26.143/ican/php/UploadActionCode.php?id=1&action=right");
        }else if(text.indexOf("左")>=0 || text.indexOf("light")>=0 || text.indexOf("love")>=0 || text.indexOf("life")>=0){
            update("http://140.134.26.143/ican/php/UploadActionCode.php?id=1&action=left");
        }else if(text.indexOf("後")>=0 || (text.indexOf("back")>=0 && text.indexOf("feedback")<0) ){
            update("http://140.134.26.143/ican/php/UploadActionCode.php?id=1&action=back");
        }else if(text.indexOf("停")>=0 || text.indexOf("結束")>=0 || text.indexOf("stop")>=0){
            update("http://140.134.26.143/ican/php/UploadActionCode.php?id=1&action=stop");
        }else if(text.indexOf("首頁")>=0 || text.indexOf("home")>=0){
            Intent intent = new Intent();
            intent.setClass(voice.this,MainActivity.class);
            startActivity(intent);
            finish();
        }else if(text.indexOf("feedback")>=0 || text.indexOf("建議")>=0 || text.indexOf("advice")>=0 || text.indexOf("問題")>=0){
            Intent intent = new Intent();
            intent.setClass(voice.this,feeback.class);
            startActivity(intent);
            finish();

        }else if(text.indexOf("control")>=0 || text.indexOf("遙控")>=0){
            Intent intent = new Intent();
            intent.setClass(voice.this,contorller.class);
            startActivity(intent);
            finish();

        }
        else if(text.indexOf("設定")>=0 || text.indexOf("set")>=0){

        }

    }


    public void update(String url){
        final String myurl = url;
        new AsyncTask<Integer,Void,String>(){
            @Override
            protected void onPreExecute(){
                result = "";
                super.onPreExecute();
            }
            @Override
            protected String doInBackground(Integer...parms){
                do{

                    result = canDBconnect.updateData(myurl);
                    Log.d("aabb",result);
                }while(result == "");
                return result;
            }
            @Override
            protected void onProgressUpdate(Void...parms){

            }
            @Override
            protected void onPostExecute(String result){
                super.onPostExecute(result);
                Toast.makeText(voice.this,result,Toast.LENGTH_SHORT).show();
            }
        }.execute();
    }

}
