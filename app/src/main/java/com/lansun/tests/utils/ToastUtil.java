package com.lansun.tests.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by felix.fan on 2016/9/2.
 */
public class ToastUtil {
    private static Toast toast;
    public static void showToast(Context context, String msg) {
        if (toast == null) {
            toast = Toast.makeText(context.getApplicationContext(), msg, Toast.LENGTH_SHORT);
        }
        toast.setText(msg);
        toast.show();
    }
}
