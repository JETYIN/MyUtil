package utils.versionandhandware;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.Collections;
import java.util.List;



import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.os.storage.StorageManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;


public class HandwareUtils {

	final public static int IMEI = 1;
	final public static int NET_TYPE = IMEI + 1;
	final public static int SDCARD_SUM = NET_TYPE + 1;
	final public static int DEFAUL_TSDCARD_FREESIZE = SDCARD_SUM + 1;
	final public static int CPU = DEFAUL_TSDCARD_FREESIZE + 1;
	final public static int MEM = CPU + 1;
	final public static int SCREEN_PIEXL = MEM + 1;
	final public static int SYSTEM_VERSION = SCREEN_PIEXL + 1;
	final public static int MOBILE_TYPE = SYSTEM_VERSION + 1;
	final public static int MOBILE_SERVICE_PROVIDER = MOBILE_TYPE + 1;
	final public static int SYSTEM_TYPE = MOBILE_SERVICE_PROVIDER + 1;

	public static long lastClickTime;

	/**
	 * android 6.0以上需要其他方法进行兼容，此方法不适用与6.0以上
	 * @param context
	 * @return
     */
	public static String getImei(Context context) {
		String imei = "";
		try {
			imei = ((TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
		} catch (Exception e) {
			// TODO: handle exception
		}

		return imei;
	}

	// 获取设备android id
	public static String getAndroid_ID(Context context) {
		return Settings.Secure.getString(context.getContentResolver(),
				Settings.Secure.ANDROID_ID);
	}

	/**
	 * 获取网络类型
	 * 
	 * @param context
	 * @return
	 */
	public static String getNetType(Context context) {
		String ret = "";
		ConnectivityManager mConnectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
		if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
			int type = mNetworkInfo.getType();
			if (ConnectivityManager.TYPE_WIFI == type) {
				ret = "wifi";
			} else if (ConnectivityManager.TYPE_MOBILE == type) {
				ret = "3g";
				int subtype = mNetworkInfo.getSubtype();
				switch (subtype) {
				case TelephonyManager.NETWORK_TYPE_GPRS:
				case TelephonyManager.NETWORK_TYPE_EDGE:
				case TelephonyManager.NETWORK_TYPE_CDMA:
				case TelephonyManager.NETWORK_TYPE_1xRTT:
				case TelephonyManager.NETWORK_TYPE_IDEN:
					ret = "2g";
					break;

				case TelephonyManager.NETWORK_TYPE_UMTS:
				case TelephonyManager.NETWORK_TYPE_EVDO_0:
				case TelephonyManager.NETWORK_TYPE_EVDO_A:
				case TelephonyManager.NETWORK_TYPE_HSDPA:
				case TelephonyManager.NETWORK_TYPE_HSUPA:
				case TelephonyManager.NETWORK_TYPE_HSPA:
				case TelephonyManager.NETWORK_TYPE_EVDO_B:
				case TelephonyManager.NETWORK_TYPE_EHRPD:
				case TelephonyManager.NETWORK_TYPE_HSPAP:
					ret = "3g";
					break;

				case TelephonyManager.NETWORK_TYPE_LTE:
					ret = "4g";
					break;

				default:
					ret = "未知";
					break;
				}

			} else {
				ret = "未知";
			}
		}
		return ret;
	}

	public static String getSdcardSum(Context context) {
		String ret = "";

		Method methodGetPaths;
		String[] paths = null;
		StorageManager storageManager = (StorageManager) context
				.getSystemService(Activity.STORAGE_SERVICE);

		try {
			methodGetPaths = storageManager.getClass().getMethod(
					"getVolumePaths");

			paths = (String[]) methodGetPaths.invoke(storageManager);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		int sum = 0;
		if (paths != null) {
			for (String stringTmp : paths) {
				sum++;
			}
		} else {
			boolean sdCardExist = Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED);
			if (sdCardExist) {
				sum = 1;
			}
		}

		ret = "" + sum;
		return ret;
	}

	public static String getCPU() {
		String ret = "";
		ret = Build.CPU_ABI;
		return ret;
	}

	public static String getMEM() {
		String ret = "";
		String str1 = "/proc/meminfo";
		String str2;
		Double memory = 0.0;

		try {

			FileReader r = new FileReader(str1);
			BufferedReader bufferedRead = new BufferedReader(r, 8192);
			str2 = bufferedRead.readLine();
			String str4 = str2.substring(str2.length() - 9, str2.length() - 3);
			memory = Double.parseDouble(str4) / 1000;

		} catch (Exception e) {
			Log.e("----", "getMEM error info:" + e.getMessage());
		}

		ret = "" + memory;
		return ret;
	}

	public static String getScreenPiexl(Context context) {
		String ret = "";

		DisplayMetrics metric = new DisplayMetrics();
		if (metric != null) {
			((Activity) context).getWindowManager().getDefaultDisplay()
					.getMetrics(metric);
			ret = "" + metric.widthPixels + "X" + metric.heightPixels;
		}
		return ret;
	}

	public static String getSystemVersion() {
		String ret = "";
		ret = "" + Build.VERSION.RELEASE;
		return ret;
	}

	public static String getMobileType() {
		String ret = "";
		ret = Build.MODEL;
		return ret;
	}

	public static String getMobileServiceProvider(Context context) {
		String ret = "";
		String IMSI = "";
		String ProvidersName = null;
		TelephonyManager telephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);

		// 返回唯一的用户ID;就是这张卡的编号神马的
		try {
			IMSI = telephonyManager.getSubscriberId();
			if (IMSI != null) {
				// IMSI号前面3位460是国家，紧接着后面2位00 02是中国移动，01是中国联通，03是中国电信。
				if (IMSI.startsWith("46000") || IMSI.startsWith("46002")) {
					ProvidersName = "中国移动";
				} else if (IMSI.startsWith("46001")) {
					ProvidersName = "中国联通";
				} else if (IMSI.startsWith("46003")) {
					ProvidersName = "中国电信";
				} else {
					ProvidersName = "其他";
				}
			} else {
				ProvidersName = "无";
			}
		} catch (Exception ex) {
			Log.e("error", ex.getMessage());
			ProvidersName = "无";
		}
		ret = ProvidersName;
		return ret;
	}

	public static String getSystemType() {
		String ret = "";
		ret = "android";
		return ret;
	}

	/****************************************************************************
	 * Progress UI
	 ****************************************************************************/
	static ProgressDialog sProgressDialog;

	public static void startProgress(final String msg, final Context context) {
		((Activity) context).runOnUiThread(new Runnable() {
			@Override
			public void run() {
				try {
					dimssProgress(context);
					if (PlatformInterface.getDomainHost().equals(
							PlatformInterface.DOMAIN_HOST_DEFAULT)) {
						sProgressDialog = ProgressDialog.show(context, "提示",
								msg, true);
					} else {
						sProgressDialog = ProgressDialog.show(
								context,
								context.getResources().getString(
										RUtils.string(context, "funcell_tip")),
								msg, true);
					}
					sProgressDialog.setCancelable(false);
				} catch (Exception ex) {
					Log.v("ddd", ex.getMessage());
				}
			}
		});
	}

	public static void dimssProgress(final Context context) {
		((Activity) context).runOnUiThread(new Runnable() {

			@Override
			public void run() {
				try {
					if (sProgressDialog != null && sProgressDialog.isShowing()) {
						sProgressDialog.dismiss();
					}
				} catch (Exception ex) {
					Log.v("ddd", ex.getMessage());
				}
			}
		});
	}

	public static void alert(final String mess, final Context context) {
		((Activity) context).runOnUiThread(new Runnable() {
			@Override
			public void run() {
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				if (PlatformInterface.getDomainHost().equals(
						PlatformInterface.DOMAIN_HOST_DEFAULT)) {
					builder.setTitle("提示");
				} else {
					builder.setTitle(context.getResources().getString(
							RUtils.string(context, "funcell_tip")));
				}

				// builder.setIcon(context.getResources().getDrawable(R.drawable.icon));
				builder.setMessage(mess);
				builder.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								dialog.dismiss();
							}
						});
				builder.create().show();
			}
		});
	}

	// 外部浏览器打开一个网址
	public static void openUrl(Context context, String Url) {
		try {
			new URL(Url);
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return;
		}
		try {
			Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse(Url));
			context.startActivity(it);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {

		}
	}

	/**
	 * 检测是否有网络
	 * 
	 * @param act
	 * @return
	 */
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		if (info != null && info.getState() == NetworkInfo.State.CONNECTED) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @param intervalTime
	 * @return
	 */
	public static boolean isFastDoubleClick(long intervalTime) {
		long time = System.currentTimeMillis();
		if (time - lastClickTime < intervalTime) {
			return true;
		}
		lastClickTime = time;
		return false;
	}

	/**
	 * 获取当前时间（以秒为单位的时间戳）
	 * 
	 * @return
	 */
	public static String getTimestamp() {
		long time = System.currentTimeMillis();
		String timestamp = String.valueOf(time);
		if (timestamp.length() >= 10) {
			timestamp = timestamp.substring(0, 10);
		}
		return timestamp;
	}

	/**
	 * 获取当前手机型号
	 * 
	 * @return
	 */
	public static String getPhoneModel() {
		return Build.MODEL;
	}

	/**
	 * 获取当前手机系统版本
	 * 
	 * @return
	 */
	public static String getPhoneVersion() {
		return Build.VERSION.RELEASE;
	}

	/**
	 * 获取当前手机高度
	 * 
	 * @return
	 */
	public static String getPhoneHeight(Activity context) {

		DisplayMetrics dm = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return String.valueOf(dm.heightPixels);
	}

	/**
	 * 获取当前手机宽度
	 * 
	 * @return
	 */
	public static String getPhoneWidht(Activity context) {

		DisplayMetrics dm = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return String.valueOf(dm.widthPixels);
	}

	/**
	 * 获取当前Ip地址
	 * 
	 * @return
	 */
	public static String getCurrentIp(Context context) {

		try {

			String ipv4;

			List nilist = Collections.list(NetworkInterface
					.getNetworkInterfaces());
			for (Object object : nilist) {
				NetworkInterface ni = (NetworkInterface) object;

				List ialist = Collections.list(ni.getInetAddresses());

				for (Object obj : ialist) {

					InetAddress address = (InetAddress) obj;

					if (!address.isLoopbackAddress()
							&& InetAddressUtils.isIPv4Address(ipv4 = address
									.getHostAddress())) {
						return ipv4;
					}
				}
			}

		} catch (SocketException ex) {

		}

		return null;
	}

	public String getLocalMacAddress(Context context) {
		WifiManager wifi = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifi.getConnectionInfo();
		return info.getMacAddress();
	}

	/**
	 * 获取可用运存大小,单位byte
	 * 
	 * @param context
	 * @return
	 */
	public static long getAvailMemory(Context context) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
		am.getMemoryInfo(mi);
		return mi.availMem;
	}

	/**
	 * 获取运存总大小,单位byte
	 * 
	 * @return
	 */
	public static long getTotalMemory() {
		String str1 = "/proc/meminfo";// 系统内存信息文件
		String str2;
		String[] arrayOfString;
		long initial_memory = 0;

		try {
			FileReader localFileReader = new FileReader(str1);
			BufferedReader localBufferedReader = new BufferedReader(
					localFileReader, 8192);
			str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小

			arrayOfString = str2.split("\\s+");
			for (String num : arrayOfString) {
				Log.i(str2, num + "\t");
			}

			initial_memory = Integer.valueOf(arrayOfString[1]).intValue() * 1024;// 获得系统总内存，单位是KB，乘以1024转换为Byte
			localBufferedReader.close();

		} catch (IOException e) {
		}
		return initial_memory;
	}

	public static String getRAM(Context mCon) {
		String str1 = "/proc/meminfo";// 系统内存信息文件
		String str2;
		String[] arrayOfString;
		long initial_memory = 0;

		try {
			FileReader localFileReader = new FileReader(str1);
			BufferedReader localBufferedReader = new BufferedReader(
					localFileReader, 8192);
			str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小

			arrayOfString = str2.split("\\s+");
			for (String num : arrayOfString) {
				Log.i(str2, num + "\t");
			}

			initial_memory = Integer.valueOf(arrayOfString[1]).intValue();
			localBufferedReader.close();

		} catch (IOException e) {
		}
		return initial_memory / (Double) 1024.00 + "";// Byte转换为KB或者MB，内存大小规格化
	}

	/**
	 * 获取可用内部存储大小,单位byte
	 * 
	 * @return
	 */
	public static long getAvailableInternalMemorySize() {
		try {
			File path = Environment.getDataDirectory();
			StatFs stat = new StatFs(path.getPath());
			long blockSize = stat.getBlockSize();
			long availableBlocks = stat.getAvailableBlocks();
			return availableBlocks * blockSize;
		} catch (Exception e) {

		}
		return -1;
	}

	/**
	 * 获取总共内部存储大小,单位byte
	 * 
	 * @return
	 */
	public static long getTotalInternalMemorySize() {
		try {
			File path = Environment.getDataDirectory();
			StatFs stat = new StatFs(path.getPath());
			long blockSize = stat.getBlockSize();
			long totalBlocks = stat.getBlockCount();
			return totalBlocks * blockSize;

		} catch (Exception e) {

		}
		return -1;
	}

	/**
	 * SDCard是否可用
	 * 
	 * @return
	 */
	public static boolean externalMemoryAvailable() {
		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
	}

	/**
	 * 获取可用外部存储大小,单位byte
	 * 
	 * @return
	 */
	public static long getAvailableExternalMemorySize() {
		try {
			if (externalMemoryAvailable()) {
				File path = Environment.getExternalStorageDirectory();
				StatFs stat = new StatFs(path.getPath());
				long blockSize = stat.getBlockSize();
				long availableBlocks = stat.getAvailableBlocks();
				return availableBlocks * blockSize;
			} else {
				return -1;
			}
		} catch (Exception e) {

		}
		return -1;
	}

	/**
	 * 获取总共外部存储大小,单位byte
	 * 
	 * @return
	 */
	public static long getTotalExternalMemorySize() {
		try {
			if (externalMemoryAvailable()) {
				File path = Environment.getExternalStorageDirectory();
				StatFs stat = new StatFs(path.getPath());
				long blockSize = stat.getBlockSize();
				long totalBlocks = stat.getBlockCount();
				return totalBlocks * blockSize;
			} else {
				return -1;
			}
		} catch (Exception e) {

		}
		return -1;
	}

	/**
	 * 对字符串进行md5 32位小写加密
	 * 
	 * @param s
	 * @return String
	 */
	public final static String stringTo32LowerCaseMD5(String s) {

		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			byte[] strTemp = s.getBytes();
			// 使用MD5创建MessageDigest对象
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(strTemp);
			byte[] md = mdTemp.digest();
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte b = md[i];
				// System.out.println((int)b);
				// 将没个数(int)b进行双字节加密
				str[k++] = hexDigits[b >> 4 & 0xf];
				str[k++] = hexDigits[b & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			return null;
		}
	}

	public static void cleanMemory(Context context) {
		long beforeCleanMemory = getAvailMemory(context) / (1024 * 1024);
		System.out.println("---> 清理前可用内存大小:" + beforeCleanMemory + "M");
		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> runningAppProcessInfoList = activityManager
				.getRunningAppProcesses();

		for (RunningAppProcessInfo runningAppProcessInfo : runningAppProcessInfoList) {
			String processName = runningAppProcessInfo.processName;
			if (RunningAppProcessInfo.IMPORTANCE_VISIBLE < runningAppProcessInfo.importance
					&& !processName.contains(context.getPackageName())
					&& processName.indexOf("tencent") == -1
					&& processName.indexOf("qq") == -1) {
				// 调用杀掉进程的方法
				for (String pkg : runningAppProcessInfo.pkgList) {
					System.out.println("---> 开始清理:"
							+ runningAppProcessInfo.importance + " , " + pkg);
					killBackgroundProcesses(context, pkg);
				}

			}
		}
		long afterCleanMemory = getAvailMemory(context) / (1024 * 1024);
		System.out.println("---> 清理后可用内存大小:" + afterCleanMemory + "M");
		System.out.println("---> 节约内存大小:"
				+ (afterCleanMemory - beforeCleanMemory) + "M");

	}

	// 利用activityManager.restartPackage()方法杀死进程
	// 该方法实际调用了activityManager.killBackgroundProcesses()方法
	public static void killBackgroundProcesses(Context context,
			String packageName) {
		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		activityManager.killBackgroundProcesses(packageName);
		System.gc();
	}
	
	//上传日志的编码
	
	public static String setLogToBiEncoder(String str){
		try {
			return URLEncoder.encode(str, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			Log.e("setLogToBiEncoder", "进入游戏初始化上传日志编码异常");
			e.printStackTrace();
		}
		return null;
	}
}
