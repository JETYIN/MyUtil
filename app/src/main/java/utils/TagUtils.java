package utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by Administrator on 2016/12/28.
 */
public class TagUtils {

    private static final String TAG = TagUtils.class.getSimpleName();

    /**
     * 返回每个activity TAG
     **/
    public static String getLogTAG(Class cls) {

        return cls.getSimpleName();
    }

    public static int getCPUNum() {
        return Runtime.getRuntime().availableProcessors();
    }

    /**
     * 获取版本号
     **/
    public static PackageInfo getPackageInfo(Context context) {
        PackageInfo sPackageInfo = null;
        if (sPackageInfo == null) {
            String pkgName = context.getPackageName();
            try {
                sPackageInfo = context.getPackageManager().getPackageInfo(pkgName, 0);
            } catch (PackageManager.NameNotFoundException e) {
            }
        }
        return sPackageInfo;
    }
}
