package com.lansun.tests.utils;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by felix.fan on 2016/9/13.
 */
public class LoadBigPicture {
    //文件路径
    public static  Bitmap loadBigPicture(String imageFilePath, int width, int height)  {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imageFilePath, options);

        int outHeight = options.outHeight;
        int outWidth = options.outWidth;

        int widthSample = outWidth/width;
        int heightSample = outHeight/height;

        int sample = widthSample>heightSample?widthSample:heightSample;

        options.inSampleSize =sample;
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(imageFilePath, options);
        return bitmap;
    }
    public static  Bitmap loadBigPicture(Bitmap bitmap, int width, int height)  {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(bais, null, options);

        int outHeight = options.outHeight;
        int outWidth = options.outWidth;

        int widthSample = outWidth/width;
        int heightSample = outHeight/height;

        int sample = widthSample>heightSample?widthSample:heightSample;

        options.inSampleSize =sample;
        options.inJustDecodeBounds = false;
        bais = new ByteArrayInputStream(baos.toByteArray());
        Bitmap newBitmap = BitmapFactory.decodeStream(bais, null, options);
        return newBitmap;
    }
    public static  Bitmap bitmapCompress(Bitmap bitmap, int width, int height)  {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        if (baos.toByteArray().length / 1024 > 1024) {
            baos.reset();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        }
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        BitmapFactory.decodeStream(bais, null, options);
        options.inJustDecodeBounds =false;

        int newBitmapWidth = options.outWidth;
        int newBitmapHeight = options.outHeight;
        int be =1;
        if (newBitmapWidth > width && newBitmapWidth > newBitmapHeight) {
            be = newBitmapWidth / width;
        } else if (newBitmapHeight > newBitmapWidth && newBitmapHeight > height) {
            be = newBitmapHeight / height;
        }
        if (be <= 0) {
            be=1;
        }
        options.inSampleSize =be;
        bais = new ByteArrayInputStream(baos.toByteArray());
        Bitmap newBitmap = BitmapFactory.decodeStream(bais, null, options);
        bitmap.recycle();
        baos.reset();
        return newBitmap;
    }
    //bitmap 不压缩
    public static Bitmap loadBigPictureNoCompress(ContentResolver contentResolver,Uri uri, int width, int height) throws IOException {
        InputStream inputStream = contentResolver.openInputStream(uri);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inDither= true;
        options.inPreferredConfig =Bitmap.Config.ARGB_8888;
        BitmapFactory.decodeStream(inputStream, null, options);
        inputStream.close();

        int outHeight = options.outHeight;
        int outWidth = options.outWidth;

        if (outHeight == -1 || outWidth == -1) {
            return null;
        }

        if (width <=0 || height <=0) {
            return null;
        }

        int widthSample = outWidth / width;
        int heightSample = outHeight / height;

        int sample = widthSample > heightSample ? widthSample : heightSample;

        options.inSampleSize = sample;
        options.inJustDecodeBounds = false;
        inputStream =  contentResolver.openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, options);
        inputStream.close();
        //质量压缩
        return bitmap;
    }
    //bitmap 压缩
    public static Bitmap loadBigPicture(ContentResolver contentResolver,Uri uri, int width, int height) throws FileNotFoundException, IOException  {
            InputStream inputStream = contentResolver.openInputStream(uri);

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            options.inDither= true;
            options.inPreferredConfig =Bitmap.Config.ARGB_8888;
            BitmapFactory.decodeStream(inputStream, null, options);
            inputStream.close();

            int outHeight = options.outHeight;
            int outWidth = options.outWidth;

            if (outHeight == -1 || outWidth == -1) {
                return null;
            }

            if (width <=0 || height <=0) {
                return null;
            }

            int widthSample = outWidth / width;
            int heightSample = outHeight / height;

            int sample = widthSample > heightSample ? widthSample : heightSample;

            options.inSampleSize = sample;
            options.inJustDecodeBounds = false;
            inputStream =  contentResolver.openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, options);
            inputStream.close();
            //质量压缩
            return compressImage(bitmap);
    }

    private static Bitmap compressImage(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        int options = 100;
        //循环压缩，知道小于100k
        while (baos.toByteArray().length/1024>100) {
            //情空，重新写入
            baos.reset();
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
            options -=10;//每次都减少10
        }
        //把压缩后的字节写入
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        Bitmap newBitmap = BitmapFactory.decodeStream(bais, null, null);
        return newBitmap;
    }

}
