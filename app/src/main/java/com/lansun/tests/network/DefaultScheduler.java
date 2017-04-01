package com.lansun.tests.network;

/**
 * Created by ly on 9/5/16.
 */
public class DefaultScheduler extends Scheduler{



    public DefaultScheduler(RequestQueue queue){
        super(queue);
    }

//    public void start(){
//        Runnable worker = new Runnable() {
//            @Override
//            public void run() {
//                while(true) {
//                    if (priorityQueue.size() > 0) {
//                        sendRequest(priorityQueue);
//                        continue;
//                    } else if (defaultQueue.size() > 0) {
//                        if (sendRequest(defaultQueue))
//                            continue;
//                    }
//                    else{
//                    }
//
//                    //for test, prevent none stop loop
//                    try {
//                        Thread.sleep(1000l);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        };
//
//        Thread workerThread = new Thread(worker);
//        workerThread.start();
//
//    }




}
