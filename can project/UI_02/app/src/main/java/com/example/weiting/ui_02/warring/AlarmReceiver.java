package com.example.weiting.ui_02.warring;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.example.weiting.ui_02.R;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        //throw new UnsupportedOperationException("Not yet implemented");

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent appIntent = PendingIntent.getActivity(context, 0,intent, 0);

        Notification notification = new Notification.Builder(context)
                .setContentIntent(appIntent)
                .setSmallIcon(R.drawable.logoc) // 設置狀態列裡面的圖示（小圖示）　　
               // .setLargeIcon(BitmapFactory.decodeResource(MainActivity.this.getResources(), R.drawable.ic_launcher)) // 下拉下拉清單裡面的圖示（大圖示）
                .setTicker("notification on status bar.") // 設置狀態列的顯示的資訊
                .setWhen(System.currentTimeMillis())// 設置時間發生時間
                .setAutoCancel(false) // 設置通知被使用者點擊後是否清除  //notification.flags = Notification.FLAG_AUTO_CANCEL;
                .setContentTitle("提醒") // 設置下拉清單裡的標題
                .setContentText("電量過低!!")// 設置上下文內容
                .setOngoing(true)      //true使notification变为ongoing，用户不能手动清除  // notification.flags = Notification.FLAG_ONGOING_EVENT; notification.flags = Notification.FLAG_NO_CLEAR;
                .setDefaults(Notification.DEFAULT_ALL) //使用所有默認值，比如聲音，震動，閃屏等等
// .setDefaults(Notification.DEFAULT_VIBRATE) //使用默認手機震動提示
// .setDefaults(Notification.DEFAULT_SOUND) //使用默認聲音提示
// .setDefaults(Notification.DEFAULT_LIGHTS) //使用默認閃光提示
// .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND) //使用默認閃光提示 與 默認聲音提示

// .setVibrate(vibrate) //自訂震動長度
// .setSound(uri) //自訂鈴聲
// .setLights(0xff00ff00, 300, 1000) //自訂燈光閃爍 (ledARGB, ledOnMS, ledOffMS)
                .build();

//把指定ID的通知持久的發送到狀態條上
        manager.notify(0, notification);


        //再次开启LongRunningService这个服务，从而可以
        Intent i = new Intent(context,warringService.class);
        context.startService(i);
    }

}
