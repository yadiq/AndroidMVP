package com.hqumath.androidmvp.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

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
}
