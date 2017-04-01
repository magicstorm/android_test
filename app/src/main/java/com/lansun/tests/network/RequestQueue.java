package com.lansun.tests.network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by ly on 9/5/16.
 */
public class RequestQueue {
    public ArrayList<LinkedList> queues ;
    //0 priority queue
    //always add to last get from first
    public LinkedList<Request> priorityQueue;
    //1 default queue
    //always add to last get from first
    public LinkedList<Request> defaultQueue;

    //request holder for efficient find and cancel request by requestId
    private HashMap<Object , Request>requestHolder;

    //queue id to priority map
    HashMap<Integer, Integer> priToQueMap = new HashMap<>();

    //interfaces
    private OnAddNewRequestListener onAddNewRequestListener;
    public interface OnAddNewRequestListener{
        void onAddNewRequest();
    }



    //requestId start from 0 for cancel request
    private long requestId = 0;

    private long unhandledRequestCount = 0;

    private void setDefaultParams(){
        setDefaultPriToQueMap();

    }

    private void setDefaultPriToQueMap(){
        priToQueMap.put(5, 0);
        priToQueMap.put(6, 0);
        priToQueMap.put(7, 0);
        priToQueMap.put(0, 1);
        priToQueMap.put(1, 1);
        priToQueMap.put(2, 1);
        priToQueMap.put(3, 1);
        priToQueMap.put(4, 1);
    }

    public RequestQueue(){
        queues = new ArrayList<>();
        priorityQueue = new LinkedList<>();
        defaultQueue = new LinkedList<>();
        queues.add(priorityQueue);
        queues.add(defaultQueue);

        requestHolder = new HashMap<>();

        setDefaultParams();
    }


    //public methods

    //add request to queue and get requestId

    public long addRequest(Request request, String tag){
        int pri = request.getPriority();
        int queId = priToQueMap.get(pri);

        requestId = getRequestId();
        if(tag==null){
            request.setRequestId(requestId);
            requestHolder.put(requestId, request);
        }
        else{
            request.setRequestId(tag);
            requestHolder.put(tag, request);
        }

        request.setQueId(queId);
        queues.get(queId).addLast(request);

        increaseUnhandledRequestCount();

        if(onAddNewRequestListener!=null){
            onAddNewRequestListener.onAddNewRequest();
        }



        //add request to queue
        return requestId;
    }

    private synchronized void increaseUnhandledRequestCount(){
        unhandledRequestCount += 1;
    }

    public synchronized void decreaseUnhandledRequestCount() {
        unhandledRequestCount -= 1;
    }


    //cancel request
    public <T> boolean cancelRequest(T tagOrId){
        Request req = requestHolder.get(tagOrId);
        if(req!=null){
            int qid = req.getQueId();
            return queues.get(qid).remove(req);
        }
        else{
            //return false means request has been sent or request does not exist
            return false;
        }
    }




    //generateRequestId
    private long getRequestId() {
        synchronized ("requestIdLock"){
            requestId += 1;
            return requestId;
        }
    }

    /**
     * Setters & Getters
     */


    public OnAddNewRequestListener getOnAddNewRequestListener() {
        return onAddNewRequestListener;
    }

    public void setOnAddNewRequestListener(OnAddNewRequestListener onAddNewRequestListener) {
        this.onAddNewRequestListener = onAddNewRequestListener;
    }

    public synchronized long getUnhandledRequestCount() {
        return unhandledRequestCount;
    }



}
