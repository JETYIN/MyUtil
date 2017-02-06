package utils;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/11/28.
 */
public class MyStringUtils {

    /**
     * 判断一个字符串的位数
     *
     * @param str
     * @param length
     * @return
     */
    public static boolean isMatchLength(String str, int length) {
        if (str.isEmpty()) {
            return false;
        } else {
            return str.length() == length ? true : false;
        }
    }

    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobileNums) {
        /*
         * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
         * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
         * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
         */
        String telRegex = "[1][358]\\d{9}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobileNums))
            return false;
        else
            return mobileNums.matches(telRegex);
    }

    /**
     * 截取字符串-后两位
     **/
    public static String getLastTwoString(String targetString) {
        if (targetString.length() == 1) {
            return targetString;
        }
        if (targetString.length() >= 2) {
            return targetString.substring(targetString.length() - 2, targetString.length());
        }
        return null;
    }


    public static CharSequence getColoredText(CharSequence src, Pattern pattern, int color) {
        if (TextUtils.isEmpty(src) || pattern == null || !pattern.matcher(src).find()) {
            return src;
        }
        SpannableString ss = SpannableString.valueOf(src);
        Matcher matcher = pattern.matcher(src);
        while (matcher.find()) {
            ss.setSpan(new ForegroundColorSpan(color), matcher.start(), matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return ss;
    }

    public static CharSequence getColoredText(CharSequence src, final int color, int start, int end) {
        return getSpannedText(src, new SpanCallback() {
            @Override
            public Object createSpan() {
                return new ForegroundColorSpan(color);
            }
        }, start, end);
    }

    public static CharSequence getUnderlinedText(CharSequence src, int start, int end) {
        return getSpannedText(src, new SpanCallback() {
            @Override
            public Object createSpan() {
                return new UnderlineSpan();
            }
        }, start, end);
    }

    public static CharSequence getSpannedText(CharSequence src, SpanCallback callback, int start, int end) {
        if (TextUtils.isEmpty(src)) {
            return src;
        }

        int len = src.length();
        if (end < start || end < 0 || start > len) {
            return src;
        }

        if (start < 0) {
            start = 0;
        }
        if (end > len) {
            end = len;
        }
        SpannableString ss = SpannableString.valueOf(src);
        ss.setSpan(callback.createSpan(), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }

    public static CharSequence getSpannedText(CharSequence text, CharSequence key, SpanCallback callback, boolean repeat) {
        if (TextUtils.isEmpty(text) || TextUtils.isEmpty(key)) {
            return text;
        }

        SpannableString ss = SpannableString.valueOf(text);
        String upperText = text.toString().toLowerCase();
        int txtLen = upperText.length();
        String upperKey = key.toString().toLowerCase();
        int keyLen = upperKey.length();
        int start = upperText.indexOf(upperKey);

        while (start >= 0 && start < txtLen) {
            int end = start + keyLen;
            ss.setSpan(callback.createSpan(), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            start = repeat ? upperText.indexOf(upperKey, end) : txtLen;
        }

        return ss;
    }

    public static CharSequence getColoredText(CharSequence text, CharSequence key, final int color) {
        return getColoredText(text, key, color, true);
    }

    public static CharSequence getColoredText(CharSequence text, CharSequence key, final int color, boolean repeat) {
        return getSpannedText(text, key, new SpanCallback() {
            @Override
            public Object createSpan() {
                return new ForegroundColorSpan(color);
            }
        }, repeat);
    }

    public static CharSequence getStyledText(CharSequence text, CharSequence key, final int style, boolean repeat) {
        return getSpannedText(text, key, new SpanCallback() {
            @Override
            public Object createSpan() {
                return new StyleSpan(style);
            }
        }, repeat);
    }

    public static CharSequence getSizedText(CharSequence text, CharSequence key, final int size, boolean repeat) {
        return getSpannedText(text, key, new SpanCallback() {
            @Override
            public Object createSpan() {
                return new AbsoluteSizeSpan(size);
            }
        }, repeat);
    }

    public interface SpanCallback {
        public Object createSpan();
    }
}
