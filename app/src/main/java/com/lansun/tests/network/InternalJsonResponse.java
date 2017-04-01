package com.lansun.tests.network;

import org.json.JSONObject;

/**
 * Created by ly on 8/17/16.
 */
public class InternalJsonResponse {
    private String code;
    private String msg;
    private JSONObject data;
    public InternalJsonResponse(){}

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public JSONObject getData() {
        return data;
    }

}
