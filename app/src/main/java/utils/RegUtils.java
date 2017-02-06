package utils;

import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/11/22.
 */
public class RegUtils {
    /**
     * ? 0次或1次
     * + 1次或1次以上
     * * 0次或多次
     **/
    public static final Pattern WIFI_NAME_PATTERN = Pattern.compile("^(xinlai)[0-9]*$");
    /**
     * 短信匹配验证码
     **/
    public static final Pattern SMS_CODE_PATTERN = Pattern.compile("^d{4}$");
    public static final String COMMA_DELIMITER = ",";


}
