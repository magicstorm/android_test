package com.lansun.tests;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;

/**
 * Created by ly on 31/03/2017.
 */

public class TestActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_test);

        MClassLoader mClassLoader = new MClassLoader();
        mClassLoader.downloadPatch(Environment.getExternalStorageDirectory().getAbsolutePath()+"/PatchClass.java");

    }
}
