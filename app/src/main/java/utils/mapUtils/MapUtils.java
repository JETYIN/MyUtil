package utils.mapUtils;

import android.text.TextUtils;
import android.util.Log;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import static utils.Utils.TAG;


/**
 * Created by Administrator on 2017/10/26.针对map的操作
 */

public class MapUtils {


   private void init(){
       Map<String, String> map = new TreeMap<String, String>();
       map.put("credential_type", "credential_type");
       map.put("zoneid", "zoneid");
       map.put("pf", "pf");
       map.put("character_id", "");
       map.put("access_token", "");
       map.put("pfkey", "");
       map.put("openid", "");
       map.put("server", "");
       map.put("pay_token", "");
   }

    //treemap是有序map，键值按字符升序排序
    public String  getConnect(Map<String,String> map){

        StringBuilder sb = new StringBuilder();
        for (String key : map.keySet()) {
            sb.append(key + "=").append(map.get(key) + "&");
        }
        String target = sb.toString();

		/*
		 * for (Iterator<String> it = map.keySet().iterator(); it.hasNext();) {
		 * String person = map.get(it.next()); sb.append(it + "=").append(person
		 * + "&"); }
		 */
        Log.e(TAG, target.substring(0, target.length() - 1));// 截取字符串从0到length-1位
        Log.e(TAG, target.substring(0, target.length() - 1) + "#" + "appkeyssss");
return null;
    }

    //删除map中的value空值,使用迭代器删除

    public Map deleteNullValue(Map<String,String> map){

        // 删除map中指定键名--
        Iterator<String> iterator = map.keySet().iterator();

        // 去掉map中value为空的键
        while (iterator.hasNext()) {
            String key = iterator.next();
            if (TextUtils.isEmpty(map.get(key))) {
                iterator.remove();
            }

            return map;
    }


}
}
