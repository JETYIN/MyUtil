package utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;

import utils.data.db.DataHelper;


/**
 * Created by Administrator on 2016/11/24.
 */
public class GpsBorderCast extends BroadcastReceiver {
    /**临近预警发送广播，此处为广播接收器接受广播并且保存状态**/
    @Override
    public void onReceive(Context context, Intent intent) {
        //获取是否进入指定区域
        boolean isEnter = intent.getBooleanExtra(LocationManager.KEY_PROXIMITY_ENTERING, false);
        if (isEnter) {
            //进入了gps临近范围，保存当前状态到share
            SharedPreferences sharedPreferences = DataHelper.getSharedPreferences(context);
            sharedPreferences.edit().putInt("GPS", 1).commit();

        } else {

        }
    }
}
