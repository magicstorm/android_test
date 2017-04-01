package com.lansun.tests.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

/**
 * Created by felix.fan on 2016/9/13.
 */
public class FilePathToByte {
    public static  byte[] toByteArray(String path) {
        Bitmap bitmap= BitmapFactory.decodeFile(path);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 30, baos);
        byte[] byteArray = baos.toByteArray();
        return byteArray;
    }
    public static  byte[] toByteArrayNoCompress(String path) {
        Bitmap bitmap= BitmapFactory.decodeFile(path);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.WEBP, 100, baos);
        byte[] byteArray = baos.toByteArray();
        return byteArray;
    }
}
