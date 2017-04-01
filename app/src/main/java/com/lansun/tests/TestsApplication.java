package com.lansun.tests;

import android.app.Application;
import android.content.Context;

/**
 * Created by ly on 11/28/16.
 */

public class TestsApplication extends Application{
    private static Context mContext;
    @Override
    public void onCreate() {
        super.onCreate();

        mContext = getApplicationContext();
    }

    public static Context getContext(){
        return mContext;
    }

}
