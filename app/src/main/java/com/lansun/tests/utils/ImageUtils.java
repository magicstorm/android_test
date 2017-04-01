package com.lansun.tests.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;

/**
 * Created by ly on 10/12/16.
 */

public class ImageUtils {
    public enum RESIZE_MODE{
        FIT_WIDTH, FIT_HEIGHT
    }

    public static Bitmap readImageFromResource(Context context, int resourceId, int width, int height, RESIZE_MODE resizeMode){

        //get original size:w
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(context.getResources(), resourceId, opt);

        //calculate scale
        int scale = calculateScale(opt.outWidth, opt.outHeight, width, height, resizeMode);

        opt.inJustDecodeBounds = false;
        opt.inSampleSize = scale;

        return BitmapFactory.decodeResource(context.getResources(), resourceId, opt);
    }

    public static Bitmap readImageFromInputStream(Context context, InputStream inputStream, int width, int height, RESIZE_MODE resizeMode){

        //get original size:w
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(inputStream, null, opt);

        //calculate scale
        int scale = calculateScale(opt.outWidth, opt.outHeight, width, height, resizeMode);

        opt.inJustDecodeBounds = false;
        opt.inSampleSize = scale;

        return BitmapFactory.decodeStream(inputStream, null, opt);
    }

    private static int calculateScale(int srcWidth, int srcHeight, int destWidth, int destHeight, RESIZE_MODE resizeMode){
        int scale = 1;

        float mscale=1.0f;

        if(resizeMode== RESIZE_MODE.FIT_WIDTH){
            mscale = (float)srcWidth/(float)destWidth;
        }

//        int heightScale = originalHeight/height;
//        int scale = widthScale>heightScale?widthScale:heightScale;
        scale = (int)Math.ceil(mscale);
        return scale<1?1:scale;
    }


}
