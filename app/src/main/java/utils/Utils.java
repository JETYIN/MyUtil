package utils;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import org.json.JSONArray;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;

import component.ExitDialog;

/**
 * Created by Administrator on 2016/12/28.
 */
public class Utils {
    private static final long KB = 1024L;
    private static final long MB = 1024L * 1024L;
    public static final String TAG = TagUtils.getLogTAG(Utils.class);

    public static void actionCall(Context context, String data) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        if (!TextUtils.isEmpty(data)) {
            intent.setData(data.startsWith("tel:") ? Uri.parse(data) : Uri.parse("tel:" + data));
        }
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        context.startActivity(intent);
    }

    public static void actionSendTo(Context context, String data) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        if (!TextUtils.isEmpty(data)) {
            intent.setData(data.startsWith("mailto:") ? Uri.parse(data) : Uri.parse("mailto:" + data));
        }
        //String title = context.getString("发送邮件");
        context.startActivity(Intent.createChooser(intent, "发送邮件"));
    }

    public static void actionView(Context context, String data, String type, int flags) {
        Uri uri = TextUtils.isEmpty(data) ? null : Uri.parse(data);
        actionView(context, uri, type, flags);
    }

    public static void actionView(Context context, Uri uri, String type, int flags) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, type);
        if (flags > 0) {
            intent.setFlags(flags);
        }
        context.startActivity(Intent.createChooser(intent, null));
    }



    public static HashMap<String, String> getDictFromPlist(InputStream is) {
        HashMap<String, String> dict = null;
        String key = null, value = null;
        XmlPullParser parser = Xml.newPullParser();

        try {
            parser.setInput(is, "UTF-8");
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        dict = new HashMap<String, String>();
                        break;

                    case XmlPullParser.START_TAG:
                        if ("key".equals(parser.getName())) {
                            eventType = parser.next();
                            key = parser.getText();
                        } else if ("string".equals(parser.getName())) {
                            eventType = parser.next();
                            value = parser.getText();
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        if ("string".equals(parser.getName())) {
                            dict.put(key, value);
                        }
                        break;

                    default:
                        break;
                }
                eventType = parser.next();
            }
        } catch (XmlPullParserException e) {
        } catch (IOException e) {
        }

        return dict;
    }

    public static String getAttSizeString(Context context, long size) {
        if (size < KB) {
            return context.getString(R.string.size_bytes, size);
        } else if (size < MB) {
            return context.getString(R.string.size_kilobytes, size / (float) KB);
        } else {
            return context.getString(R.string.size_megabytes, size / (float) MB);
        }
    }

    /**
     * @param src source string to be encoded
     * @return utf-8 encoded string
     */
    public static String urlEncode(String src) {
        if (!TextUtils.isEmpty(src)) {
            try {
                return URLEncoder.encode(src, "UTF-8");
            } catch (UnsupportedEncodingException e) {
            }
        }
        return "";
    }

    /**
     * @param src source string to be decoded
     * @return utf-8 decoded string
     */
    public static String urlDecode(String src) {
        if (!TextUtils.isEmpty(src)) {
            try {
                return URLDecoder.decode(src, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                Logger.e(TAG, e.getMessage());
            } catch (IllegalArgumentException e) {
                Logger.e(TAG, e.getMessage());
            }
        }
        return "";
    }

    public static String join(CharSequence delimiter, JSONArray ja) {
        StringBuilder sb = new StringBuilder();
        if (ja != null) {
            boolean firstTime = true;
            for (int i = 0; i < ja.length(); i++) {
                if (firstTime) {
                    firstTime = false;
                } else {
                    sb.append(delimiter);
                }
                sb.append(ja.optString(i));
            }
        }
        return sb.toString();
    }

    public static String join(CharSequence delimiter, int[] values) {
        StringBuilder sb = new StringBuilder();
        if (values != null) {
            boolean firstTime = true;
            for (int i = 0; i < values.length; i++) {
                if (firstTime) {
                    firstTime = false;
                } else {
                    sb.append(delimiter);
                }
                sb.append(values[i]);
            }
        }
        return sb.toString();
    }

    /**
     * 显示软键盘
     **/
    public static void showInput(EditText etView) {
        if (etView != null) {
            if (!etView.hasFocus()) {
                etView.requestFocus();
            }

            InputMethodManager imm = (InputMethodManager) etView.getContext().getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            if (imm.isAcceptingText()) {
                imm.showSoftInput(etView, 0);
            }
        }
    }

    /**
     * 隐藏软键盘
     **/
    public static void hideInput(EditText etView) {
        if (etView != null) {
            InputMethodManager imm = (InputMethodManager) etView.getContext().getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(etView.getWindowToken(), 0);
        }
    }

    /**
     * 极光推送检查appkey
     **/
  /*  public static String getAppKey(Context context) {
        Bundle metaData = null;
        String appKey = null;
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            if (null != ai)
                metaData = ai.metaData;
            if (null != metaData) {
                appKey = metaData.getString(KEY_APP_KEY);
                if ((null == appKey) || appKey.length() != 24) {
                    appKey = null;
                }
            }
        } catch (PackageManager.NameNotFoundException e) {

        }
        return appKey;
    }*/

    /**
     * 获取手机的imei编号
     **/
    public static String getImei(Context context, String imei) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            imei = telephonyManager.getDeviceId();
        } catch (Exception e) {
            Log.e(TagUtils.getLogTAG(DateUtils.class), e.getMessage());
        }
        return imei;
    }

  /*  *//**
     * 获取手机deviceid
     **//*
    public static String getDeviceId(Context context) {
        String deviceId = JPushInterface.getUdid(context);
        return deviceId;
    }*/



}
