package utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

import java.io.File;
import java.math.BigDecimal;

/**
 * Created by Administrator on 2018/6/13.
 */
//FIXME 此类用于计算app中缓存大小，以作显示或清除缓存操作
public class CacheUtils {

    //FIXME 返回当前app内部缓存大小，以作ui显示
    private String getCacheSizeString(Context context) {
        long size = getFolderSize(new File(context.getCacheDir().getAbsolutePath()));
        return formatFileSize(size);
    }

    //FIXME 清除app内部缓存
    private boolean clearCache(Context context) {
        return deleteDir(new File(context.getCacheDir().getAbsolutePath()));
    }

    private long getFolderSize(File file) {
        if (!file.exists()) {
            return 0;
        }

        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (File aFileList : fileList) {
                if (aFileList.isDirectory()) {
                    size = size + getFolderSize(aFileList);
                } else {
                    size = size + aFileList.length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    private String formatFileSize(double size) {
        double kByte = size / 1024;
        if (kByte < 1) {
            return size + "B";
        }
        double mByte = kByte / 1024;
        if (mByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB";
        }
        double gByte = mByte / 1024;
        if (gByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(mByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB";
        }
        double tByte = gByte / 1024;
        if (tByte < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(tByte);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB";
    }

    private boolean deleteDir(File dir) {
        if (dir == null || !dir.exists())
            return false;

        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (String aChildren : children) {
                boolean success = deleteDir(new File(dir, aChildren));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    //FIXME 复制文字到剪切板
    public static void copy(Context context) {
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("content", s2);//FIXME S2为将要复制的文字内容
        cmb.setPrimaryClip(clipData);
        //FIXME 提示已将文字复制到剪切板
    }
}
