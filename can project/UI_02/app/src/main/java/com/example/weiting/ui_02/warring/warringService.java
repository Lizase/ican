package com.example.weiting.ui_02.warring;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.app.AlarmManager;
import android.app.PendingIntent;

import com.example.weiting.ui_02.Data;
import com.example.weiting.ui_02.R;
import com.example.weiting.ui_02.canDBconnect;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

public class warringService extends Service {
    public String result="";
    public Boolean warring = true;
    public void onCreate(){

    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    @Override
    public int onStartCommand(Intent intent,int flags,int startId){
        super.onStartCommand(intent,flags,startId);

        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = "";
                url = "http://140.134.26.143/ican/ican/Ashow.php?id=caps9129";
                result = canDBconnect.updateData(url);
                //Log.d("warring", result);
                try{
                    JSONArray jsonArray = new JSONArray(result);
                    Log.d("warring", result);
                    for(int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonData = jsonArray.getJSONObject(i);
                        Log.d("warring", jsonData.getString("battery"));
                        if(Integer.parseInt(jsonData.getString("battery")) <= 60){
                            Intent intent = new Intent(warringService.this,warningDialog.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            warringService.this.startActivity(intent);

                            AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
                            //读者可以修改此处的Minutes从而改变提醒间隔时间
                            //此处是设置每隔90分钟启动一次
                            //这是90分钟的毫秒数
                            int Minutes = 5*1000;
                            //SystemClock.elapsedRealtime()表示1970年1月1日0点至今所经历的时间
                            long triggerAtTime = SystemClock.elapsedRealtime() + Minutes;
                            //此处设置开启AlarmReceiver这个Service
                            Intent j = new Intent(warringService.this, AlarmReceiver.class);
                            PendingIntent pi = PendingIntent.getBroadcast(warringService.this, 0, j, 0);
                            //ELAPSED_REALTIME_WAKEUP表示让定时任务的出发时间从系统开机算起，并且会唤醒CPU。
                            manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
                        }
                    }
                }catch (Exception e){}
            }
        }).start();

        return super.onStartCommand(intent, flags, startId);
        //return START_NOT_STICKY;
    }
    public void onDestroy() {
        //System.out.println("MyService onDestroy()：Called by the system to notify a Service that it is no longer used and is being removed. ");
        super.onDestroy();
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent i = new Intent(this, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
        manager.cancel(pi);
    }
    @Override
    public boolean onUnbind(Intent intent) {
        System.out.println("MyService onUnbind()：Called when all clients have disconnected from a particular interface published by the service.");
        return super.onUnbind(intent);
    }

    /**
     * Created by Weiting on 2017/10/9.
     */

    public static class deviceAdapter extends BaseAdapter {
        public Data[] data;
        public int view;
        public Context context;
        public deviceAdapter(Data[] data,int view,Context context){
            this.data = data;
            this.view = view;
            this.context = context;
        }
        @Override
        public  int getCount(){
            return data.length;
        }
        @Override
        public Data getItem(int position){
            return data[position];
        }
        @Override
        public long getItemId(int position){
            return 0;
        }
        @Override
        public View getView(int position, View rowView, ViewGroup parent){
            LayoutInflater inflater = LayoutInflater.from(context);
            rowView = inflater.inflate(view,parent,false);
            TextView name = (TextView) rowView.findViewById(R.id.deviceName);
            ImageView imageView = (ImageView) rowView.findViewById(R.id.deviceImage);
            name.setText(data[position].name);
            imageView.setImageResource(data[position].photo);
            return rowView;
        }
    }
}
