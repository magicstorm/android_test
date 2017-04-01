package com.lansun.tests.utils;

/**
 * Created by felix.fan on 2016/9/18.
 */
public class FastDoubleClick {
    private static long lastClickTime;
    public static boolean isFastDoubleClick() {
        long currentTime =System.currentTimeMillis();
        long timeD =currentTime -lastClickTime;
        if (timeD > 0 && timeD < 1000) {
            return true;
        }
        lastClickTime = currentTime;
        return  false;
    }
}
