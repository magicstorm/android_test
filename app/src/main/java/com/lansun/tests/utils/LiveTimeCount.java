package com.lansun.tests.utils;

import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Sam.Zhu on 2016/8/18.
 */
public class LiveTimeCount extends CountDownTimer {
    private TextView tv;
    public LiveTimeCount(long millisInFuture, long countDownInterval, TextView tv) {
        super(millisInFuture, countDownInterval);
        this.tv=tv;
    }
    @Override
    public void onTick(long l) {
        tv.setText(l/1000+"");
    }

    @Override
    public void onFinish() {
        tv.setVisibility(View.GONE);
    }
}
