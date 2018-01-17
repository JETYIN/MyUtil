package utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

/**
 * Created by Administrator on 2018/1/17.
 */
//用于解决修改系统级别权限，如申请write_settings,动态申请此权限无用，需跳转到系统设置界面打开对应设置，打开授权
public class SystemPermission {

    private void init(Activity context) {
        if (Build.VERSION.SDK_INT >= 23) {
            checkPermission(context);
        }
    }

    /**
     * WRITE_SETTINGS系统设置权限，有两个权限动态申请权限无用。需要跳转至系统设置界面打开系统权限开关
     * @param context
     */
    private void checkPermission(Activity context) {
        if (!Settings.System.canWrite(context)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS, Uri.parse("package:" + context.getPackageName()));
            context.startActivityForResult(intent, 100);
        }
    }

    /**
     * SYSTEM_ALERT_WINDOW悬浮窗权限
     * @param context
     */
    private  void requestAlertWindowPermission(Activity context) {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
        intent.setData(Uri.parse("package:" + context.getPackageName()));
        context.startActivityForResult(intent, 200);
    }
}
