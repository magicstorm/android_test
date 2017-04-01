package com.lansun.tests.utils;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;




public class ToastSingle {
    private static Toast toast = null;
    private static Context mContext;
    private static TextView textView;

    public static void showToast(Context context, String s, int duration) {
        if (toast == null) {
            toast = Toast.makeText(context, s, duration);
            toast.show();
        } else {
            toast.setText(s);
            toast.show();
        }
    }

    public static void showToastCenter(Context context, String s) {
        Toast zgpToast = Toast.makeText(context, s, Toast.LENGTH_SHORT);
        zgpToast.setGravity(Gravity.CENTER, 0, 0);
        zgpToast.show();
    }

    private static Toast centerToast = null;



}