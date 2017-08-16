package utils.polling;

/**
 * Created by Administrator on 2017/8/16.
 */

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;
import android.util.Log;

/**
 * 当前辅助类用于开启轮训,AlarmManager的定时任务与芯片时钟有关,比Thread+handler稳定
 **/

public class PollingUtils {
    private String TAG = "PollingUtils";


    public static void startPollingService(Context context,
                                           int seconds, Class<?> cls, String action) {
        //获取AlarmManager系统服务
        AlarmManager manager = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);

        //包装需要执行Service的Intent
        Intent intent = new Intent(context, cls);
        intent.setAction(action);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        //触发服务的起始时间,该时间表示从开机到当前的总时间，就算当前处于待机息屏、省点模式状态下也不会关闭
        long triggerAtTime = SystemClock.elapsedRealtime();

        //使用AlarmManger的setRepeating方法设置定期执行的时间间隔（seconds秒）和需要执行的Service

        /**@param 闹钟类型、首次执行时间（当前）、闹钟执行间隔时间、闹钟响应动作**/
        /**当api等级大于19，setRepeating方法将不会生效，需要使用setWindow方法**/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //参数2是开始时间、参数3是允许系统延迟的时间
            manager.setWindow(AlarmManager.RTC, triggerAtTime, seconds * 1000, pendingIntent);
        } else {
            manager.setRepeating(AlarmManager.RTC, triggerAtTime, seconds * 1000, pendingIntent);
        }
        Log.e("result", "it will start weigthing 3...");
    }

    //停止轮询服务
    public static void stopPollingService(Context context, Class<?> cls, String action) {
        AlarmManager manager = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, cls);
        intent.setAction(action);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        //取消正在执行的服务
        manager.cancel(pendingIntent);
    }
}
