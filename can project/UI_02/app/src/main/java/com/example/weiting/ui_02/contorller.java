package com.example.weiting.ui_02;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
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

import com.example.weiting.ui_02.following.follow;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

public class contorller extends AppCompatActivity {
    String result = "";
    int check = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_main2);
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
        myAdapter adapter = new myAdapter(menuData,R.layout.menu_list,contorller.this);
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
        contentData[0].name = "Can follow";
        contentData[1].name = "Voice control";

        contentData[0].photo = R.drawable.follow;
        contentData[1].photo = R.drawable.voice;

        GridView gridView = (GridView) findViewById(R.id.gridview);
        myAdapter menuAdapter = new myAdapter(contentData,R.layout.mode_page_bar,contorller.this);
        gridView.setNumColumns(2);
        gridView.setAdapter(menuAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    Intent intent = new Intent();
                    intent.setClass(contorller.this,follow.class);
                    startActivity(intent);
                }
                else if(position == 1){
                    Intent intent = new Intent();
                    intent.setClass(contorller.this,voice.class);
                    startActivity(intent);
                }
            }
        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout2);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        final TextView command = (TextView)findViewById(R.id.textView4);
        Button open = (Button) findViewById(R.id.on);
        Button off  = (Button) findViewById(R.id.off);
        Button top = (Button) findViewById(R.id.top);
        Button left = (Button) findViewById(R.id.left);
        Button right = (Button) findViewById(R.id.right);
        Button down = (Button) findViewById(R.id.down);
        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update("http://140.134.26.143/ican/php/UploadActionCode.php?id=1&action=open");
                command.setText("開蓋!!");
            }
        });
        off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                command.setText("關蓋!!");
                update("http://140.134.26.143/ican/php/UploadActionCode.php?id=1&action=close");

            }
        });
        top.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP){
                    update("http://140.134.26.143/ican/php/UploadActionCode.php?id=1&action=stop");
                    command.setText("停止!!");
                    return true;
                }
                else if(event.getAction() == MotionEvent.ACTION_DOWN){
                    update("http://140.134.26.143/ican/php/UploadActionCode.php?id=1&action=ahead");
                    command.setText("ahead!!");
                    return true;
                }
                return false;
            }
        });
        left.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP){
                    update("http://140.134.26.143/ican/php/UploadActionCode.php?id=1&action=stop");
                    command.setText("停止!!");
                    return true;
                }
                else if(event.getAction() == MotionEvent.ACTION_DOWN){
                    update("http://140.134.26.143/ican/php/UploadActionCode.php?id=1&action=left");
                    command.setText("left!!");
                    return true;
                }
                return false;
            }
        });
        right.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP){
                    update("http://140.134.26.143/ican/php/UploadActionCode.php?id=1&action=stop");
                    command.setText("停止!!");
                    return true;
                }
                else if(event.getAction() == MotionEvent.ACTION_DOWN){
                    update("http://140.134.26.143/ican/php/UploadActionCode.php?id=1&action=right");
                    command.setText("right!!");
                    return true;
                }
                return false;
            }
        });
        down.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP){
                    update("http://140.134.26.143/ican/php/UploadActionCode.php?id=1&action=stop");
                    command.setText("停止!!");
                    return true;
                }
                else if(event.getAction() == MotionEvent.ACTION_DOWN){
                    update("http://140.134.26.143/ican/php/UploadActionCode.php?id=1&action=back");
                    command.setText("back!!");
                    return true;
                }
                return false;
            }
        });
    }

    //*******************************drawer**********************************
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout2);
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
    //************上傳到資料庫*****************
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
                //Toast.makeText(contorller.this,result,Toast.LENGTH_SHORT).show();
            }
        }.execute();
    }

}
