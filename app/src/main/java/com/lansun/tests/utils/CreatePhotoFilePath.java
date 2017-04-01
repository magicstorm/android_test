package com.lansun.tests.utils;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by felix.fan on 2016/10/12.
 */

public class CreatePhotoFilePath {
    public static File createTackPhotoFile() {
        File file = new File(Environment.getExternalStorageDirectory() + "/MuXing");
        if (!file.exists()) {
            file.mkdirs();
        }
        String photoFileName = createPhotoFileName();
        File photoFile = new File(file, photoFileName +".jpg");
        if (!photoFile.exists()) {
            try {
                photoFile.createNewFile();
                return photoFile;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return photoFile;
    }

    //设置照片名称
    public static String createPhotoFileName() {
        String fileName = null;
        SimpleDateFormat sdf = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
        Date date = new Date(System.currentTimeMillis());
        fileName = sdf.format(date);
        return fileName;
    }

    //获取图片的旋转角度
    public static int obtainBitmapDegree(String path) {
        int degree = 0;
        //获取图片的旋转信息
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree=90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree=180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree=270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return degree;
    }
    //旋转角度，矫正图片
    public static Bitmap rotatoBitmapByDegree(Bitmap bitmap, int degree) {
        Bitmap newBitmap = null;
        //根据选择角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        //生成新的图片
        matrix.postRotate(degree);
        newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        if (newBitmap == null) {
            newBitmap = bitmap;
        }
        if (newBitmap != bitmap) {
            bitmap.recycle();
        }
        return newBitmap;
    }
}
