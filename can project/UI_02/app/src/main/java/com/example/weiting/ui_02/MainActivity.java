package com.example.weiting.ui_02;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.weiting.ui_02.fingerprint.fingerprint;
import com.example.weiting.ui_02.following.follow;


public class MainActivity extends AppCompatActivity{

    SQLiteDatabase dbrw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyDBHelper dbhelper = new MyDBHelper(this);
        dbrw = dbhelper.getWritableDatabase();
        String[] colum ={"userID","icanID","usedTime","battery","temperature","latitude","longitude","state","lastUsedTime"};
        Cursor c;
        c = dbrw.query("myTable",colum,null,null,null,null,null);
        Log.d("sql",c.getCount()+"\n");
        if(c.getCount()>0){
            c.moveToFirst();
            for(int i = 0;i<c.getCount();i++){
                Log.d("sql",c.getString(0)+"\n");
                Log.d("sql",c.getString(1)+"\n");
                Log.d("sql",c.getString(2)+"\n");
                Log.d("sql",c.getString(3)+"\n");
                Log.d("sql",c.getString(4)+"\n");
                Log.d("sql",c.getString(5)+"\n");
                Log.d("sql",c.getString(6)+"\n");
                Log.d("sql",c.getString(7)+"\n");
                Log.d("sql",c.getString(8)+"\n");
                c.moveToNext();
            }
        }
        Data[] menuData = new Data[3];
        for(int i = 0;i<3;i++){
            menuData[i] = new Data();
        }
        menuData[0].name = "首頁";
        menuData[0].photo = R.drawable.home;
        menuData[1].name = "訊息通知";
        menuData[1].photo = R.drawable.mail;
        menuData[2].name = "設定";
        menuData[2].photo = R.drawable.settings;
        myAdapter adapter = new myAdapter(menuData,R.layout.menu_list,MainActivity.this);
        TextView slogan = (TextView)findViewById(R.id.textView8);
        Typeface face = Typeface.createFromAsset(this.getAssets(),"fonts/Arbeka.ttf");//設定字型
        slogan.setTypeface(face);
        ListView listView = (ListView) findViewById(R.id.listview);
        //listView.setDivider(new ColorDrawable(ContextUtil.getColor(R.color.divider)));
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent,View view,int position,long id){  //第幾個item被點選
                if(position == 0){

                }
                else if(position== 1){
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this,feeback.class);
                    startActivity(intent);
                }
                else if(position== 2){
                    Intent intent = new Intent();
                    //Intent warring_intent = new Intent();
                    intent.setClass(MainActivity.this,Setting.class);
                    //warring_intent.setClass(MainActivity.this,warringService.class);
                   // startService(warring_intent);
                    startActivity(intent);
                }
            }
        });

        Data[] contentData = new Data[3];
        for(int i = 0;i<3;i++){
            contentData[i] = new Data();
        }
        contentData[0].name = "Controller";
        contentData[1].name = "Can follow";
        contentData[2].name = "Voice control";


        contentData[0].photo = R.drawable.controller;
        contentData[1].photo = R.drawable.follow;
        contentData[2].photo = R.drawable.voice;



        GridView gridView = (GridView) findViewById(R.id.gridview);
        myAdapter menuAdapter = new myAdapter(contentData,R.layout.home_page_bar,MainActivity.this);
        gridView.setNumColumns(3);
        gridView.setAdapter(menuAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this,contorller.class);
                    startActivity(intent);
                }
                else if(position == 1){
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this,follow.class);
                    startActivity(intent);

                }
                else if(position== 2){
                        Intent intent = new Intent();
                    intent.setClass(MainActivity.this,voice.class);
                    startActivity(intent);
                }


            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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
