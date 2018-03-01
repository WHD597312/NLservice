package com.xinri;

import android.support.v7.app.AppCompatActivity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "MainActivity";
    private static final int NOTIFICATION = 100;
    private Button btnSend;
    private Button btnRemove;
    private TextView tv;
    private NotificationManager manager;
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String str = intent.getStringExtra("text");
            tv.setText(str);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        btnSend = (Button) findViewById(R.id.btnSend);
        btnSend.setOnClickListener(this);
        btnRemove = (Button) findViewById(R.id.btnRemove);
        btnRemove.setOnClickListener(this);
        tv = (TextView) findViewById(R.id.tv);
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        //注册本地广播监听
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(getPackageName());
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean isAuthor=isNotificationServiceEnable();
        if (!isAuthor){
            //直接跳转通知授权界面
            //android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS是API 22才加入到Settings里，这里直接写死
            startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 是否已授权
     *
     * @return
     */
    private boolean isNotificationServiceEnable() {
        return NotificationManagerCompat.getEnabledListenerPackages(this).contains(getPackageName());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSend:
                sendNotification();
                break;
            case R.id.btnRemove:
                removeNotification();
                break;
        }
    }

    /**
     * 发送通知
     */
    private void sendNotification() {
        Notification notification = new Notification.Builder(this)
                // 设置显示在状态栏的通知提示信息
                .setTicker("有新消息")
                .setSmallIcon(R.drawable.ic_launcher)
                // 设置通知内容的标题
                .setContentTitle("一条新通知")
                // 设置通知内容
                .setContentText("通知内容")
                .setWhen(System.currentTimeMillis())
                .build();
        notification.tickerText = "您有一条新的通知";
        notification.defaults = notification.DEFAULT_SOUND;
        manager.notify(NOTIFICATION, notification);
    }

    /**
     * 移除通知
     */
    private void removeNotification() {
        manager.cancel(NOTIFICATION);
    }


}
