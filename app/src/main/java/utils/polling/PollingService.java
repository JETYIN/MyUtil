package utils.polling;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.administrator.util.MainActivity;

/**
 * Created by Administrator on 2017/8/16.此类用于实现轮训与心跳连接，采用后台开服务，使用芯片RTC
 */

public class PollingService extends Service {
    int count = 0;
    private String TAG = "PollingService";
    private NotificationManager notificationManager;//提示框管理类
    private Notification notification;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "****create pollingService");
        initNotification();

    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

        Log.e(TAG, "****start pollingService");
        //启动线程
        new PollingThread().start();
    }
    //实例化消息通知栏

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    //初始化消息通知栏
    private void initNotification() {
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        int icon = R.drawable.ic_launcher;//r文件暂无法使用
        notification = new Notification();
        notification.icon = icon;
        notification.tickerText = "New Message";
        notification.defaults |= Notification.DEFAULT_SOUND;
        notification.flags = Notification.FLAG_AUTO_CANCEL;

    }

    //显示消息通知栏

    private void showNotification() {

        notification.when = System.currentTimeMillis();
        //Navigator to the new activity when click the notification title
        Intent i = new Intent(this, MainActivity.class);//此处的activity设置为主activity
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, Intent.FLAG_ACTIVITY_NEW_TASK);
        notification.setLatestEventInfo(this,
                getResources().getString(R.string.app_name), "You have new message!", pendingIntent);
        notificationManager.notify(0, notification);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "****pollingService destroy");
    }

    //设置线程，RTC定时调用产生心跳信息
    class PollingThread extends Thread {

        @Override
        public void run() {
            Log.e(TAG, "****start pollingThread");
            count++;

            if (count % 5 == 0) {

                //消息轮训次数被5整除时通知展示消息
                showNotification();
                Log.e(TAG, "****polling message arraived");
            }

        }
    }
}
