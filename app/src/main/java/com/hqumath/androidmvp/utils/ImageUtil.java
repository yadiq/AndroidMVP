package com.hqumath.androidmvp.utils;

import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;

import androidx.core.content.FileProvider;

import com.hqumath.androidmvp.BuildConfig;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 * ****************************************************************
 * 文件名称: ImageUtil
 * 作    者: Created by gyd
 * 创建时间: 2019/3/1 14:35
 * 文件描述: 图片管理
 * 注意事项:
 * 版权声明:
 * ****************************************************************
 */
public class ImageUtil {

    public static String imageToBase64(Bitmap bitmap, int quality) {
        ByteArrayOutputStream bas = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, quality, bas);//图片压缩质量（0,100]
        byte[] bytes = bas.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    public static Bitmap base64ToImage(String base64Str) {
        byte[] data = Base64.decode(base64Str, Base64.NO_WRAP);//略去加密字符串最后的“=”
        for (int i = 0; i < data.length; i++) {
            if (data[i] < 0) {
                //调整异常数据
                data[i] += 256;
            }
        }
        return BitmapFactory.decodeByteArray(data, 0, data.length);
    }

    /**
     * 图片压缩到指定大小
     *
     * @param bitmap  原图片
     * @param maxSize 图片大小KB
     * @return
     */
    public static File compressImage(Bitmap bitmap, int maxSize) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > maxSize) {  //循环判断如果压缩后图片是否大于 maxSize kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            options -= 10;//每次都减少10
            if (options == 0) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 5, baos);//这里压缩options%，把压缩后的数据存放到baos中
                break;
            } else {
                bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            }
        }
        //存储文件
        File compressFile = FileUtil.getExternalCacheFile("compress" + System.currentTimeMillis() + ".jpg");
        try {
            FileOutputStream fos = new FileOutputStream(compressFile);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        bitmap.recycle();
        return compressFile;
    }


    /**
     * 根据file生成uri
     *
     * @param file   文件
     * @param isCrop 是否调用系统裁剪（不支持FileProvider.getUriForFile）
     *               Android7.0以上手机调用系统裁剪提示“无法保存经过裁剪的图片”
     * @return 生成的uri
     */
    public static Uri getUriFromFile(File file, boolean isCrop) {
        Uri uri = null;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {//android10 分区存储
                ContentValues values = new ContentValues(2);
                values.put(MediaStore.Images.Media.DISPLAY_NAME, file.getName());
                values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {//SD卡是否可用
                    uri = CommonUtil.getContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                } else {
                    uri = CommonUtil.getContext().getContentResolver().insert(MediaStore.Images.Media.INTERNAL_CONTENT_URI, values);
                }
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && !isCrop) {//android7 私有文件安全性
                uri = FileProvider.getUriForFile(CommonUtil.getContext(), BuildConfig.APPLICATION_ID + ".FileProvider", file);
            } else {
                uri = Uri.fromFile(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return uri;
    }
}
