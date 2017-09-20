package utils.autoLogin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

/**@params当前类用作保存用户账号、密码到文件，并且删除指定账号**/
public class HWFileUtils {

	private static String TAG = "HWFileUtils";
	public static final String FUNCELL = "/funcell";// 文件夹名
	private static List<String> keyList = new ArrayList<>();// 键名

	// 多个账号的键名
	public static List<String> getKey() {
		return keyList;
	}

	// 读取登录、注册成功的所有账号
	public static Map<String, List<String>> readUserInfo() {

		HWUtils.logError(TAG, "****读取用户信息");
		File file = new File(rootPath() + FUNCELL + "/" + "info" + "/userInfo.txt");

		FileInputStream fis;
		try {
			fis = new FileInputStream(file);
			// 处理可能出现的中文乱码问题
			BufferedReader br = new BufferedReader(new InputStreamReader(fis, "UTF-8"));

			// 整行读，一行为一个账号数据

			// 读完全部行

			String content = "";// 读取的每行数据
			String[] contents = null;
			Map<String, List<String>> map = new LinkedHashMap<>();
			while ((content = br.readLine()) != null) {
				HWUtils.logError(TAG, "@@@@读取的每行数据是:" + content);
				List<String> list = new ArrayList<>();
				contents = content.split("##");
				keyList.add(contents[0]);// 键名保存在list中
				list.add(contents[1]);
				list.add(contents[2]);
				map.put(contents[0], list);// 值

			}

			HWUtils.logError(TAG, "@@@@读取值:" + map.toString() + ",keyList:" + keyList.toString());
			// 关闭流对象
			fis.close();
			br.close();
			return map;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	// 保存所有登录成功、注册成功账号
	public static void saveUserInfo(String userAccount, String refreshToken, String accessToken) {
		HWUtils.logError(TAG, "****保存用户信息");
		FileWriter file;
		try {
			file = new FileWriter(rootPath() + FUNCELL + "/" + "info" + "/userInfo.txt", true);// 设置为true为继续追加内容,普通file不能追加
			file.write(userAccount);
			// 换行
			file.write("##");
			// 继续追加
			file.write(refreshToken);
			file.write("##");
			file.write(accessToken);
			file.write("\r\n");
			// 刷新IO内存流
			file.flush();
			// 关闭
			file.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// 快速登录账号文件，只保存一个
	public static void saveQuick(String userAccount, String refreshToken, String token) {
		HWUtils.logError(TAG, "****保存用户信息-saveQuick");
		try {
			File file = new File(rootPath() + FUNCELL + "/" + "info" + "/quick.txt");
			FileOutputStream fos = new FileOutputStream(file);
			fos.write((userAccount + "##" + refreshToken + "##" + token).getBytes());
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// 读取登录成功、注册成功账号，只有一个
	public static Map readQuickAccount() {
		HWUtils.logError(TAG, "****读取用户信息-readQuickAccount");
		try {
			File file = new File(rootPath() + FUNCELL + "/" + "info" + "/quick.txt");
			FileInputStream fis = new FileInputStream(file);
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			String content = br.readLine();
			if (TextUtils.isEmpty(content)) {
				return null;
			}
			Map<String, List<String>> map = new LinkedHashMap<>();// 有序map
			String[] contents = content.split("##");// 0,1,2

			List<String> list = new ArrayList<>();
			list.add(contents[1]);
			list.add(contents[2]);
			map.put(contents[0], list);// 值

			HWUtils.logError(TAG, "****读取到的信息是:" + map.toString());
			fis.close();
			br.close();
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 在sd卡上创建文件-xinlai（也是缓存文件的根目录）
	 **/
	private static String getFuncellDir() {
		if (isSDcard()) {
			/** 获取sd卡的绝对路径 **/
			String SDPath = rootPath();
			File sdFile = new File(SDPath + FUNCELL + "/" + "info");
			if (!sdFile.exists()) {
				HWUtils.logError(TAG, sdFile.getAbsolutePath());
				sdFile.mkdirs();// 创建文件夹
				return sdFile.getAbsolutePath();
			}
		}
		return null;
	}

	private static String rootPath() {
		return Environment.getExternalStorageDirectory().getAbsolutePath();
	}

	/**
	 * 检查sd卡是否挂载
	 **/
	private static boolean isSDcard() {
		return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
	}

	// 创建总文件夹
	public static void createHwAccountFile() {
		String filePath = getFuncellDir();
		if (null == filePath) {
			HWUtils.logError(TAG, "****userInfo文件已创建");
			return;
		}
		File newFile = new File(filePath + "/" + "userInfo.txt");
		File quickFile = new File(filePath + "/" + "quick.txt");

		if (!newFile.exists()) {
			HWUtils.logError(TAG, "****创建file:" + newFile.getAbsolutePath());
			try {
				newFile.createNewFile();// 创建文件
				quickFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	private static void clear() {
		FileWriter file;
		try {
			file = new FileWriter(rootPath() + FUNCELL + "/" + "info" + "/userInfo.txt", false);// 设置为false--不再文件后面进行追加--清空文件
			file.write("");
			// 刷新IO内存流
			file.flush();
			file.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	// 将经过用户修改的登录信息重新写回文件

	public static void saveChangeInfo(Map<String, List<String>> map) {
		HWUtils.logError(TAG, "****保存修改后的用户信息map:" + map.toString());
		clear();
		FileWriter file;
		Iterator<Entry<String, List<String>>> entries = map.entrySet().iterator();
		try {
			file = new FileWriter(rootPath() + FUNCELL + "/" + "info" + "/userInfo.txt", true);
			while (entries.hasNext()) {

				Entry<String, List<String>> entry = entries.next();

				String userAccount = entry.getKey();

				List<String> list = entry.getValue();
				file.write(userAccount);
				// 换行
				file.write("##");
				// 继续追加
				file.write(list.get(0));
				file.write("##");
				file.write(list.get(1));
				file.write("\r\n");
				// 刷新IO内存流
				file.flush();
			}
			// map中没有元素时关闭文件
			file.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // 设置为true为继续追加内容,普通file不能追加

	}
}
