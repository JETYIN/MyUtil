package utils.app;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

/**
 * Created by Administrator on 2017/8/7.当前类主要用于对manifest的操作
 */

public class ManifestUtils {

    /**notice manifest中的meta-data不能动态修改
     * **/

    /**
     * @params key manifest中设置的meta-data的android:name
     * 读取manifest中节点数据
     **/


    public static String readMetaData(Context context, String key) {

        ApplicationInfo applicationInfo;
        Bundle bundle = null;
        try {
            applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            bundle = applicationInfo.metaData;//通过applicationinfo获取到对应的bundle实例

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null == bundle ? null : bundle.get(key).toString();//根据在manifest中设置的android:name作为key将获得的值解析出来

    }
}
