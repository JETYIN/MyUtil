package utils.secure;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by Administrator on 2017/3/10.
 */

public class EncodeUtils {

    public static String getEncodeUTF(String str){
        try {
            return URLEncoder.encode("str","UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
