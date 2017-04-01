package com.lansun.tests;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lansun.tests.utils.ToastSingle;

import static com.lansun.tests.LanguageTest.test.ass;

/**
 * Created by ly on 09/03/2017.
 */

public class LanguageTest extends Activity{

    private LanguageTest ma;
    private TextView fuckt;

    public  enum test{
        fuck, you, ass, hole
    }

    private test test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_test);

        fuckt = (TextView)findViewById(R.id.fuck);

        ma = this;

        this.test = ass;

        System.out.println("" + this.test.ordinal());


        fuckt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastSingle.showToast(LanguageTest.this, "fuck", Toast.LENGTH_SHORT);
            }
        });

        new Handler(getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                ToastSingle.showToast(LanguageTest.this, "fuck", Toast.LENGTH_SHORT);
            }
        }, 2000);
    }


}
