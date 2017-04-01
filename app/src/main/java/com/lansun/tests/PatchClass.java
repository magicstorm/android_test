package com.lansun.tests;

import android.widget.Toast;

/**
 * Created by ly on 31/03/2017.
 */

public class PatchClass {
    public static void helloWorld(){
        Toast.makeText(TestsApplication.getContext(), "Hello, I'm coming from http", Toast.LENGTH_LONG);
    }
}
