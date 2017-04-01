package com.lansun.tests.network;

import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

/**
 * Created by ly on 9/5/16.
 */
public class Scheduler {
    protected RequestQueue queue;
    protected LinkedList<Request> priorityQueue;
    protected LinkedList<Request> defaultQueue;



    protected int maxThread = 10;


    protected int threadCount = 0;
    protected HashMap<Object, Request> sendingRequests = new HashMap<>();

    public Scheduler(RequestQueue queue){
        this.queue = queue;
        this.priorityQueue = queue.queues.get(0);
        this.defaultQueue = queue.queues.get(1);
        queue.setOnAddNewRequestListener(new RequestQueue.OnAddNewRequestListener() {
            @Override
            public void onAddNewRequest() {
                handleRequest();
            }
        });
    }

    protected synchronized boolean handleRequest(){
        if(threadCount<=maxThread){
            while(queue.getUnhandledRequestCount()>0){
                if(priorityQueue.size()>0){
                    sendAll(queue.priorityQueue);
                }
                else if(defaultQueue.size()>0){
                    sendNextRequest(defaultQueue);
                }
            }
            return true;
        }
        else{
            return false;
        }
    }

    private void sendAll(LinkedList<Request> queue) {
        while(queue.size()>0){
            sendNextRequest(queue);
        }
    }

    private void sendNextRequest(LinkedList<Request> queue) {
        Request request = queue.getFirst();
        sendRequest(request);
        sendingRequests.put(request.getRequestId(), request);
        queue.removeFirst();
        this.queue.decreaseUnhandledRequestCount();
    }


     Thread sendRequest(final Request request){
        //send request and reduce threadCount
        synchronized ("threadCountLock"){
            threadCount+=1;
        }
        Thread r =  new Thread(){
            @Override
            public void run() {

                System.out.println("enter thread: " + threadCount);

                String requestType = (request.getMethod()== Request.Method.POST)?"POST":"GET";
                System.out.println(requestType);


                InputStream stream=null;
                try {
                    URL url = new URL(request.getUrl());
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod(requestType);
                    conn.setConnectTimeout(5000);
                    conn.setReadTimeout(5000);

                    if(requestType.equals("GET")){
                        conn.connect();
                    }
                    else if(requestType.equals("POST")){
                        String stringParams = getParamsString(request);
                        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");//or application/json
                        conn.setRequestProperty("Content-Length", String.valueOf(stringParams.getBytes("UTF8").length));
                        conn.setDoOutput(true);
                        OutputStream os = conn.getOutputStream();
                        os.write(stringParams.getBytes());
                    }

                    int responseCode = conn.getResponseCode();
                    stream = conn.getInputStream();
                    String stringResponse = getStringFromInputStream(stream);
                    if(responseCode==200){
                        //convert response to InternalJsonResponse
                        JSONObject jsonResponse = new JSONObject(stringResponse);


                        request.callBack.onReceiveResponse(jsonResponse);
                    }
                    else{
                        handleFailedRequest(responseCode, stringResponse, request);
                    }

                } catch (MalformedURLException e) {
                    handleConnectonFail(e.getMessage(), request);
                    e.printStackTrace();
                } catch (IOException e) {
                    handleConnectonFail(e.toString(), request);
                    e.printStackTrace();
                } catch (JSONException e) {
                    handleConnectonFail(e.getMessage(), request);
                    e.printStackTrace();
                } finally{
                    try {
                        if(stream!=null){
                            stream.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    synchronized ("threadCountLock"){
                        threadCount-=1;
                        if(threadCount>=maxThread-1){
                            handleRequest();
                        }
                    }

                }
            }
        };
        r.start();
        return r;
    }



    private void handleConnectonFail(String errorMessage, Request request) {
        RequestError error = new RequestError();
        error.setErrorMessage(errorMessage);
        request.callBack.onError(error);
    }

    public void handleFailedRequest(int responseCode, String stringResponse, Request request) {
        RequestError error = new RequestError();
        error.setErrorCode(String.valueOf(responseCode));
        error.setErrorMessage("连接失败, 错误信息: " + stringResponse);
        request.callBack.onError(error);
    }

    @NonNull
    private String getParamsString(Request request) {
        String stringParams = "";
        Request.Params requestParams = request.getParams();
        Set<String> ks = requestParams.keySet();
        Iterator<String> it = ks.iterator();
        while (it.hasNext()){
            String key = it.next();
            if(requestParams.get(key) instanceof JSONArray){
                JSONArray jsonArray = (JSONArray)requestParams.get(key);
                String jsonArrayString = jsonArray.toString();
                stringParams += key+"="+ jsonArrayString +'&';
                System.out.println(jsonArrayString);
            }
            else{
                stringParams += key+"="+requestParams.get(key)+'&';
            }
        }

        return stringParams.substring(0, stringParams.length()-1);
    }

    private String getStringFromInputStream(InputStream stream){
//        StringBuilder sb = new StringBuilder(Math.max(16, stream.available()));
//        char[] tmp = new char[4096];
//        InputStreamReader reader = new InputStreamReader(stream, "UTF-8");
//        int len;
//        while((len=reader.read(tmp))>0){
//            sb.append(tmp, 0, len);
//        }
//        return sb.toString();
        byte[] b = new byte[8192];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            while((len=stream.read(b))!=-1){
                bos.write(b, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            String stringResult = new String(bos.toByteArray(), "utf-8");
            return stringResult;
//            new String(bos.toByteArray(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Getters and Setters
     */

    public int getMaxThread() {
        return maxThread;
    }

    public void setMaxThread(int maxThread) {
        this.maxThread = maxThread;
    }

}
