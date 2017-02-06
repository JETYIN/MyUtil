package utils;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2016/12/28.
 */
public class DateUtils {

    private static final String TAG = TagUtils.getLogTAG(DateUtils.class);
    private static final int DAY_MILLS = 24 * 60 * 60 * 1000;

    //TIMER 9:30
    public static boolean compareTime(String TIMER, int TYPE) {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        long hour = -1, minute = -1;
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        try {
            //设置打卡时间
            Date StandardTimer = sdf.parse(TIMER);
            //获取系统当前时间
            Date NowTimer = new Date();
            //获取时间差，毫秒数
            long diff = NowTimer.getTime() - StandardTimer.getTime();
            hour = diff % nd / nh;
            // 计算差多少分钟
            minute = diff % nd % nh / nm;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (TYPE == 0) {
            return getWorkTimerCompare(hour, minute);
        }
        if (TYPE == 1) {
            return getWorkOutTimerCompare(hour, minute);
        }
        return false;
    }

    /**
     * 下班时间
     **/
    private static boolean getWorkOutTimerCompare(long hour, long minute) {
        boolean isFlag = false;
        //17;20
        if (hour == 0 && minute >= 0) {
            isFlag = true;
        }
        if (hour > 0 && hour < 7) {
            isFlag = true;
        }
        if (hour >= 8) {
            isFlag = false;
        }
        return isFlag;
    }

    /**
     * 判断打卡时间是否超时
     **/
    private static boolean getWorkTimerCompare(long hour, long minute) {
        boolean isFlag = false;
        //9:25
        if (hour == 0 && minute <= 0) {
            isFlag = true;
        }
        if (hour > 0 || (hour == 0 && minute > 0)) {
            isFlag = false;
        }
        return isFlag;
    }

    public static String getWifi(String s) {
        int length = s.length();
        String str = s.substring(1, length - 1);
        return str;
    }

    /**
     * hh：返回12小时制，HH：返回24小时制
     **/
    public static String getNowDate() {
        SimpleDateFormat sp = new SimpleDateFormat("HH:mm:ss");
        return sp.format(new Date());
    }

    public static String getNowDateWithoutSD() {
        SimpleDateFormat sp = new SimpleDateFormat("HH:mm");
        return sp.format(new Date());
    }

    public static void isCompareTime() {

    }
    /**定时器**/
    private void initTime(final Handler handler) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = 0;
                handler.sendMessage(message);
            }
        }, 0, 1000);
    }

    /**
     * 时间选择器
     **/
    public static void datePick(Context context, final TextView textView) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int monthOfYear = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                textView.setText(new StringBuilder()
                        .append(year)
                        .append("-")
                        .append((month + 1) < 10 ? "0" + (month + 1)
                                : (month + 1)).append("-")
                        .append((day < 10) ? "0" + day : day));
            }
        }, year, monthOfYear, dayOfMonth).show();
    }

    /**
     * 进入设置时间
     **/
    public static void setDataPickTime(TextView textView) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int monthOfYear = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        textView.setText(new StringBuilder()
                .append(year)
                .append("-")
                .append((monthOfYear + 1) < 10 ? "0" + (monthOfYear + 1)
                        : (monthOfYear + 1)).append("-")
                .append((dayOfMonth < 10) ? "0" + dayOfMonth : dayOfMonth));

    }

/**是否是同一天,System.currentTimeMillis()获取的是从1970.1.1到当前时间的毫秒数，除以每天的毫秒数
 * today是从1970.1.1到今天的天数**/
    public static boolean isToday(long time) {
        long day = time / DAY_MILLS;
        long today = System.currentTimeMillis() / DAY_MILLS;

        return day == today;
    }

    public static boolean isSameDay(long milliseconds) {
        Calendar compar = Calendar.getInstance();
        Calendar current = Calendar.getInstance();
        compar.setTimeInMillis(milliseconds);
        int comparDay = compar.get(Calendar.DAY_OF_MONTH);
        int currentDay = current.get(Calendar.DAY_OF_MONTH);
        if (comparDay == currentDay) {

            return true;
        }
        return false;
    }

    /**根据传入的两个long类型的时间类型进行比较**/
    public static boolean isSameDay(long millisecondsL, long millisecondsR) {
        Calendar calendarL = Calendar.getInstance();
        Calendar calendarR = Calendar.getInstance();
        /**将日期设置成毫秒的形式进行比较时候在当前月份的同一天**/
        calendarL.setTimeInMillis(millisecondsL);
        calendarR.setTimeInMillis(millisecondsR);
        int lDay = calendarL.get(Calendar.DAY_OF_MONTH);
        int rDay = calendarR.get(Calendar.DAY_OF_MONTH);
        if (lDay == rDay) {
            return true;
        }
        return false;
    }
    @SuppressLint("SimpleDateFormat")
    public static String getTimeString(int type, long milliseonds) {
        SimpleDateFormat sdf;
        switch (type) {
            case 1:
                sdf = new SimpleDateFormat("HH:mm");
                break;
            case 2:
                sdf = new SimpleDateFormat("yyyy-MM-dd");
                break;
            case 3:
                sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                break;
            case 4:
                sdf = new SimpleDateFormat("MM-dd HH:mm");
                break;
            case 5:
                sdf = new SimpleDateFormat("yyyy年M月d日");
                break;
            case 6:
                sdf = new SimpleDateFormat("yyyy年M月d日 HH:mm");
                break;
            case 7:
                sdf = new SimpleDateFormat("MM.dd");
                break;
            case 8:
                sdf = new SimpleDateFormat("MM-dd");
                break;
            case 9:
                sdf = new SimpleDateFormat("yyyy.MM.dd");
                break;
            case 10:
                sdf = new SimpleDateFormat("HH:mm:ss");
                break;
            default:
                sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                break;
        }
        return sdf.format(new Date(milliseonds));
    }
    public static int getYear() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR);
    }

    public static int getMonth() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.MONTH) + 1;
    }

    public static int getDate() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DATE);
    }

    public static boolean saveBitmap(Bitmap bitmap, String fileName) {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(fileName);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, out);
            return true;
        } catch (IOException e) {
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
        }
        return false;
    }

    public static boolean isHasAt(String content) {
        if (content == null || content.length() == 0) {
            return false;
        }
        return content.contains("@");
    }

    public static boolean isHasTopic(String content) {
        if (content == null || content.length() == 0) {
            return false;
        }
        return content.contains("#");

    }  public static <T> ArrayList<T> copy(ArrayList<T> datas) {
        ArrayList<T> copyData = new ArrayList<>();
        for (T t : datas) {
            copyData.add(t);
        }
        return copyData;
    }
}
