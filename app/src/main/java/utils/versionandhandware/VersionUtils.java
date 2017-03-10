package utils.versionandhandware;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Process;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by Administrator on 2017/3/10.
 */

public class VersionUtils {


    /**
     * 获取当前App版本号
     **/
    public static String getVersionNumber(Context context) {

        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            String version = packageInfo.versionName;
            return version;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getVersion(Context context) {

        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();

        }
        return null;
    }

    /**
     * 获取当前进程名称----用于在application中，退出app，但是当前进程未被杀死，再进入app不会init第三方sdk
     **/

    /**
     * 获取当前进程名称--application中调用
     **/
    /*public boolean quickStart() {
        if (TextUtils.isEmpty(mCurentProcessName)) {
            mCurentProcessName = getCurProcessName(this);
        }
        if (contains(mCurentProcessName, ":mini")) {
            Log.d("loadDex", ":mini start!");
            return true;
        }
        return false;
    }*/
    public boolean contains(String str, String searchStr) {
        return str != null && searchStr != null ? str.indexOf(searchStr) >= 0 : false;
    }

    /**
     * 进程名称
     **/
    public static String getCurProcessName(Context context) {
        try {
            int pid = android.os.Process.myPid();
            ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager.getRunningAppProcesses()) {
                if (appProcess.pid == pid) {
                    return appProcess.processName;
                }
            }
        } catch (Exception e) {
            // ignore
        }
        return null;
    }
}
