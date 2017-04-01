package com.lansun.tests.network;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by ly on 9/5/16.
 */
public class Request<T>{

    private String url;
    private Params params;
    public RequestCallBack callBack;

    private int priority = 0;

    public boolean isSending() {
        return sending;
    }

    public void setSending(boolean sending) {
        this.sending = sending;
    }

    private boolean sending = false;

    private Enum reqType = ReqType.INTERNAL;
    public enum ReqType{
        INTERNAL, IMAGE, NORMAL
    }

    private Enum method = Method.GET;
    public enum Method{
        POST, GET
    }



    private int queId;

    private T requestId;

    public interface RequestCallBack{
        void onReceiveResponse(JSONObject response);
        void onError(RequestError error);
    }

    public Request(String url, Method method, Params params, RequestCallBack callback){
        this.method = method;
        this.params = params;
        this.url = method== Method.GET?addParamsToUrl(params, url):url;
        this.callBack = callback;
    }

    public static class Params extends HashMap {
        public Params(){
            super();
        }
    }

    private String addParamsToUrl(Params params, String url) {
        String fullUrl = url;
        if(params!=null&&params.size()>0){
            Set<String> ks = params.keySet();
            Iterator<String> it = ks.iterator();
            int in = 0;
            while(it.hasNext()){
                fullUrl += in==0?"?":"&" ;
                String key = it.next();
                String val = (String)params.get(key);
                fullUrl += key+"="+val;
                in+=1;
            }
        }
        return fullUrl;
    }


    /**
     * Getters and Setters
     */

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getQueId() {
        return queId;
    }

    public void setQueId(int queId) {
        this.queId = queId;
    }

    public T getRequestId() {
        return requestId;
    }

    public void setRequestId(T requestId) {
        this.requestId = requestId;
    }

    public String getUrl() {
        return url;
    }

    public Params getParams() {
        return params;
    }

    public Enum getMethod() {
        return method;
    }

    public Enum getReqType() {
        return reqType;
    }

    public void setReqType(Enum reqType) {
        this.reqType = reqType;
    }

}
