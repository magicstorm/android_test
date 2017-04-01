package com.lansun.tests;

import android.app.Activity;
import android.os.Bundle;

import com.lansun.tests.widgets.SnackBar;

/**
 * Created by ly on 22/03/2017.
 */

public class SnackBarTest extends Activity{

    private SnackBar snackBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.snack_bar_test);

        snackBar = (SnackBar)findViewById(R.id.snackBar);
    }


}
