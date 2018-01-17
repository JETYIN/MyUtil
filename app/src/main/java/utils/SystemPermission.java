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

    private void checkPermission(Activity context) {
        if (!Settings.System.canWrite(context)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS, Uri.parse("package:" + context.getPackageName()));
            context.startActivityForResult(intent, 100);
        }
    }
}
