package com.hqumath.androidmvp.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

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
}
