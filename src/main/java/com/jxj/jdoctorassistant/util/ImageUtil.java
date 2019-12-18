package com.jxj.jdoctorassistant.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Build;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

/**
 * Created by Administrator on 2016/6/16.
 */
public class ImageUtil {
    // 将解密字符串转化为bitmap
    public static Bitmap getBitmap(String bitmapString) {
        // 将字符串转换成Bitmap类型
        Bitmap bitmap = null;
        try {
            byte[] bitmapArray;
            bitmapArray = Base64.decode(bitmapString, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0,
                    bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    // 得到图像数据流base64加密字符串
    public static String getStream(Bitmap bitmap) {
        long l1=getBitmapsize(bitmap);
        System.out.println("图片原尺寸:"+l1);
        double w=bitmap.getWidth();
        double h=bitmap.getHeight();
        if(l1>8000000){
            bitmap=zoomImage(bitmap,w/2,h/2);
        }
//        Bitmap bitmap=zoomImage(bmp,w/2,h/2);
        // 将Bitmap转换成字符串
        long l2=getBitmapsize(bitmap);
        System.out.println("图片新尺寸:"+l2);
        String string = null;
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bStream);
        byte[] bytes = bStream.toByteArray();
        double size=bytes.length/1024;
        System.out.println("图片尺寸:"+size+" KB");
        string = Base64.encodeToString(bytes, Base64.DEFAULT);
        return string;
    }

    public static long getBitmapsize(Bitmap bitmap){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
            return bitmap.getByteCount();
        }
        // Pre HC-MR1
        return bitmap.getRowBytes() * bitmap.getHeight();
    }

    /***
     * 图片的缩放方法
     *
     * @param bgimage
     *            ：源图片资源
     * @param newWidth
     *            ：缩放后宽度
     * @param newHeight
     *            ：缩放后高度
     * @return
     */
    public static Bitmap zoomImage(Bitmap bgimage, double newWidth,
                                   double newHeight) {
        // 获取这个图片的宽和高
        float width = bgimage.getWidth();
        float height = bgimage.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width,
                (int) height, matrix, true);
        return bitmap;
    }

}
