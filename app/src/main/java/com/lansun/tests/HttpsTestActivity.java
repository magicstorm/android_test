package com.lansun.tests;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.lansun.tests.network.*;
import com.lansun.tests.network.Mapi;

import org.json.JSONObject;

/**
 * Created by ly on 12/6/16.
 */

public class HttpsTestActivity extends Activity{

    private TextView tv;
    private Mapi mapi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_https_test);
        findViews();
        download();
    }

    private void findViews(){
        tv = (TextView)findViewById(R.id.tv);
    }

    private void download(){
        mapi = com.lansun.tests.network.Mapi.getInstance();
        mapi.exec("https://www.baidu.com", null, Request.Method.GET, new Mapi.JsonRequestCallBack() {
            @Override
            public void onReceiveInternalResponse(InternalJsonResponse response) {
                System.out.println("fuck");
            }

            @Override
            public void onError(RequestError error) {

            }

            @Override
            public void onReceiveThirdpartyResponse(JSONObject response) {

            }
        });

    }
}
