package utils;

import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import component.ExitDialog;

/**
 * Created by Administrator on 2016/12/14.
 */
public class FileUtils {
    /**
     * xinlai根目录
     **/
    public static final String XINLAI_BASE_PATH = getXinLaiDir().getAbsolutePath();
    public static final String XIN_LAI = "/XinLai";
    /**
     * 删除文件夹时删除根文件Xinlai以下所有文件
     **/
    private static final String TAG = "clearCache";

    /**
     * 组装文件路径element 为多个数量字符集
     **/
    private static String buildFileString(Object... element) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Object src : element) {
            stringBuilder.append(src);
        }
        return stringBuilder.toString();
    }

    /**
     * 在sd卡上创建文件-xinlai（也是缓存文件的根目录）
     **/
    public static File getXinLaiDir() {
        if (isSDcard()) {
            /**获取sd卡的绝对路径**/
            String SDPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            File sdFile = new File(SDPath + XIN_LAI);
            if (sdFile.mkdir() || sdFile.isDirectory()) {
                return sdFile;
            }
            Logger.e("SDPath", sdFile.getAbsolutePath());
        }
        return null;
    }

    /**
     * 检查sd卡是否挂载
     **/
    public static boolean isSDcard() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    /**
     * 清除webview缓存
     **/
    public static void clearWebViewCache(Context context) {
        //清理Webview缓存数据库
        try {
            context.deleteDatabase("webview.db");
            context.deleteDatabase("webviewCache.db");
        } catch (Exception e) {
            e.printStackTrace();
        }

        //WebView 缓存文件
        File appCacheDir = new File(context.getFilesDir().getAbsolutePath() + "");
        Logger.e(TAG, "appCacheDir path=" + appCacheDir.getAbsolutePath());

        File webviewCacheDir = new File(context.getCacheDir().getAbsolutePath() + "");
        Logger.e(TAG, "webviewCacheDir path=" + webviewCacheDir.getAbsolutePath());

        //删除webview 缓存目录
        if (webviewCacheDir.exists()) {
            deleteFile(webviewCacheDir);
        }
        //删除webview 缓存 缓存目录
        if (appCacheDir.exists()) {
            deleteFile(appCacheDir);
        }
    }

    /**
     * 递归删除文件-清楚缓存删除除根目录下的所有文件
     **/
    public static void deleteFile(File file) {
        Logger.i(TAG, "delete file path=" + file.getAbsolutePath());

        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                File files[] = file.listFiles();
                for (File list : files) {
                    deleteFile(list);
                }
            }
            file.delete();
        } else {
            Logger.e(TAG, "delete file no exists " + file.getAbsolutePath());
        }
    }

    /**
     * 计算文件大小
     **/
    private static long countFileSize(File file) {
        long size = 0;
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File list : files) {
                    size += countFileSize(list);
                }
            }
        } else {
            size += file.length();
        }
        //Formatter.formatFileSize(context, size)将long format成string
        return size;

    }


    public static boolean unzipFile(String zipPath, String destPath) {
        ZipFile zipFile = null;
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        byte[] buffer = new byte[4096];
        try {
            zipFile = new ZipFile(zipPath);
            for (Enumeration<? extends ZipEntry> e = zipFile.entries(); e.hasMoreElements(); ) {
                ZipEntry entry = e.nextElement();
                File file = new File(destPath, entry.getName());
                if (entry.isDirectory()) {
                    if (!file.exists() && !file.mkdirs()) {
                        return false;
                    }
                } else {
                    File parentFile = file.getParentFile();
                    if (!parentFile.exists() && !parentFile.mkdirs()) {
                        return false;
                    }
                    bis = new BufferedInputStream(zipFile.getInputStream(entry));
                    bos = new BufferedOutputStream(new FileOutputStream(file));
                    int readLen = 0;
                    while ((readLen = bis.read(buffer)) > 0) {
                        bos.write(buffer, 0, readLen);
                    }
                    bis.close();
                    bos.close();
                }
            }
        } catch (IOException e) {
            Logger.e(TAG, e.getMessage());
            return false;
        } finally {
            try {
                if (zipFile != null) {
                    zipFile.close();
                }
                if (bis != null) {
                    bis.close();
                }
                if (bos != null) {
                    bos.close();
                }
            } catch (IOException e) {
            }
        }

        return true;
    }
    /**删除指定文件名**/
    public static boolean deleteFile(String fileName) {
        if (!TextUtils.isEmpty(fileName)) {
            File file = new File(fileName);
            if (file.isFile()) {
                return file.delete();
            }
        }
        return false;
    }

    /**重命名文件**/
    public static boolean renameFile(String oldName, String newName) {
        File old = new File(oldName);
        if (old.exists()) {
            File nn = new File(newName);
            return old.renameTo(nn);
        }
        return false;
    }

    /**复制**/
    public static boolean copy(String srcName, String dstName) {
        File src = new File(srcName);
        File dst = new File(dstName);
        InputStream in = null;
        OutputStream out = null;
        try {
            in = new FileInputStream(src);
            out = new FileOutputStream(dst);

            // Transfer bytes from in to out
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            return true;
        } catch (FileNotFoundException e) {
            Logger.e(TAG, "copy error: " + e.getMessage());
        } catch (Exception e) {
            Logger.e(TAG, "copy error: " + e.getMessage());
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
        }
        return false;
    }
    /**
     * 下载
     **/
    public static void downloadFile(final Context context, final String url, final String fileName) {
        new ExitDialog(context).setTitleLd(R.string.att_download_dialog_title)
                .setMessage(R.string.att_download_dialog_message).setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
                        dm.enqueue(request);
                    }
                }).show();
    }


}

