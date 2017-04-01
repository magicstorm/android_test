package com.lansun.tests;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.lansun.tests.R;

/**
 * Created by ly on 10/03/2017.
 */

public class ParcelReceiverActivity extends AppCompatActivity{

    private TextView tv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parcel_receiver);

        Intent startupIntent = getIntent();
        ParcelSenderActivity.Message msg = (ParcelSenderActivity.Message)AutoParcel.unwrap(startupIntent.getParcelableExtra("message"));

        tv = (TextView)findViewById(R.id.output);
        tv.setText(msg.getText());

    }

}
