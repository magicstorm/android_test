package com.lansun.tests.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by ly on 10/9/16.
 */

public class ImageHelpers {

    public enum RESIZE_MODE{
        FIT_WIDTH, FIT_HEIGHT
    }

    public static Bitmap resizeToFit(Context context, int resourceId, int width, int height, RESIZE_MODE resizeMode){

        //get original size:w
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inJustDecodeBounds = true;
        Bitmap bitmapFrame = BitmapFactory.decodeResource(context.getResources(), resourceId, opt);
        int originalWidth = opt.outWidth;
        int originalHeight = opt.outHeight;




        //calculate scale
        int scale = 1;
//        int heightScale = originalHeight/height;

//        int scale = widthScale>heightScale?widthScale:heightScale;

        if(resizeMode== RESIZE_MODE.FIT_WIDTH){
            scale = originalWidth/width;
        }

        opt.inJustDecodeBounds = false;
        opt.inSampleSize = scale;


        return BitmapFactory.decodeResource(context.getResources(), resourceId, opt);

    }
}
