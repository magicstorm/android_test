package com.lansun.tests.utils;

import android.content.Context;

/**
 * Created by ly on 8/11/16.
 */
public class Helpers {

    public static boolean inMainProcess(Context context) {
        String packageName = context.getPackageName();
        String processName = SystemUtil.getProcessName(context);
        return packageName.equals(processName);
    }


}
