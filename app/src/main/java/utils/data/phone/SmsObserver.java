package utils.data.phone;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;

import com.example.administrator.util.MainActivity;

import java.util.regex.Matcher;

import utils.Logger;
import utils.RegUtils;

/**
 * Created by Administrator on 2016/12/9.观察者莫斯-内容观察者
 */
public class SmsObserver extends ContentObserver {
    /**
     * Creates a content observer.
     *
     * @param handler The handler to run {@link #onChange} on, or null if none.
     */
    private Context mContext;
    private Handler mHandler;

    public SmsObserver(Context context, Handler handler) {
        super(handler);
        this.mContext = context;
        this.mHandler = handler;
    }

    /**
     * 状态改变时调用-接收短信,动态获取短信内容
     **/
    @Override
    public void onChange(boolean selfChange, Uri uri) {
        super.onChange(selfChange, uri);
        Logger.e("SMS_LOADING", "sms has changed");
        Logger.e("SMS_URI", uri.toString());

        if (uri.toString().equals("content://sms/raw")) {
            return;
        }

        Uri inboxUri = Uri.parse("content://sms/inbox");
        Cursor cursor = mContext.getContentResolver().query(inboxUri, null,
                null, null, "date desc");

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                String address = cursor.getString(cursor
                        .getColumnIndex("address"));
                String body = cursor.getString(cursor.getColumnIndex("body"));
                Logger.e("SMS_USER", "发件人为：" + address + " " + "短信内容为：" + body);

                Matcher matcher = RegUtils.SMS_CODE_PATTERN.matcher(body);

               /* Pattern pattern = Pattern.compile("(\\d{4})");//正则表达式-4位验证码
                Matcher matcher = pattern.matcher(body);*/
                if (matcher.find()) {
                    String code = matcher.group(0);
                    Logger.e("SMS_CODE", "code is" + code);
/**目标activity中传入的mHandler将匹配到的信息发回到mainactivity中**/
                    mHandler.obtainMessage(MainActivity.SMS_CODE, code)
                            .sendToTarget();
                }
            }
        }
    }
   /* //注册观察者-在目标activity中编写此代码
    private void rigisterOS() {
        smsObserver = new SmsObserver(this, handler);
        Uri uri = Uri.parse("content://sms");
        getContentResolver().registerContentObserver(uri, true, smsObserver);
    }*/
}
