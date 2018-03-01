package com.xinri;

import android.content.Intent;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NLService extends NotificationListenerService {
    public String TAG="NLService";
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
//        super.onNotificationPosted(sbn);
        //这里只是获取了包名和通知提示信息，其他数据可根据需求取，注意空指针就行
        String pkg = sbn.getPackageName();
        long when=sbn.getNotification().when;
        Date date=new Date(when);
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String s=format.format(date);
        Log.d(TAG,s);
        if("com.tencent.mobileqq".equals(pkg)){
            CharSequence tickerText = sbn.getNotification().tickerText;
            sendBroadcast(String.format("显示通知\npkg:%s\ntickerText:%s", pkg, TextUtils.isEmpty(tickerText) ? "null" : tickerText+"\n时间:"+s));
        }
    }
    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
//        super.onNotificationRemoved(sbn);
        String pkg = sbn.getPackageName();
        CharSequence tickerText = sbn.getNotification().tickerText;
        sendBroadcast(String.format("移除通知\npkg:%s\ntickerText:%s", pkg, TextUtils.isEmpty(tickerText) ? "null" : tickerText));
    }
    private void sendBroadcast(String msg) {
        Intent intent = new Intent(getPackageName());
        intent.putExtra("text", msg);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
