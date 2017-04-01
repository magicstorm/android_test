package com.lansun.tests.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

/**
 * Created by felix.fan on 2016/9/2.
 */
public class SpUtil {
    public static final String sp_name = "config";
    private static  SharedPreferences mSp;
    private static String previousFile ="";
    private static SharedPreferences sp;


    //获取boolean值
    public static boolean getBoolean(Context context,String key,boolean defValue){

        if (mSp == null) {
            mSp = context.getSharedPreferences(sp_name, Context.MODE_PRIVATE);
        }
        boolean aBoolean = mSp.getBoolean(key, defValue);
        return aBoolean;
    }
    //保存boolean值
    public static void setBoolean(Context context,String key ,boolean value) {
        if (mSp == null) {
            mSp = context.getSharedPreferences(sp_name, Context.MODE_PRIVATE);
        }
        mSp.edit().putBoolean(key, value).commit();
    }
    //获取String值
    public static String getString(Context context,String key,String defValue) {
        if (mSp == null) {
            mSp = context.getSharedPreferences(sp_name, Context.MODE_PRIVATE);
        }
        String string = mSp.getString(key, defValue);
        return string;
    }
    //保存String值
    public static void setString(Context context,String key ,String value) {
        if (mSp == null) {
            mSp = context.getSharedPreferences(sp_name, Context.MODE_PRIVATE);
        }
        mSp.edit().putString(key, value).commit();
    }

    //动态设置保存的文件名

    //获取boolean值
    public static boolean getBoolean(Context context,String fileName,String key,boolean defValue){
        if (!TextUtils.equals(previousFile, fileName) || sp == null) {
            previousFile = fileName;
            sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        }
        boolean aBoolean = sp.getBoolean(key, defValue);
        return aBoolean;
    }
    //保存boolean值
    public static void setBoolean(Context context,String fileName,String key ,boolean value) {
        if (!TextUtils.equals(previousFile, fileName) || sp == null) {
            previousFile = fileName;
            sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        }

        sp.edit().putBoolean(key, value).commit();
    }
    //获取String值
    public static String getString(Context context,String fileName,String key,String defValue) {
        if (!TextUtils.equals(previousFile, fileName) || sp == null) {
            previousFile = fileName;
            sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        }
        String string = sp.getString(key, defValue);
        return string;
    }
    //保存String值
    public static void setString(Context context,String fileName,String key ,String value) {
        if (!TextUtils.equals(previousFile, fileName) || sp == null) {
            previousFile = fileName;
            sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        }
        sp.edit().putString(key, value).commit();
    }

}
