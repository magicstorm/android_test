package com.lansun.tests;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

import com.lansun.tests.network.InternalJsonResponse;
import com.lansun.tests.network.Scheduler;
import com.lansun.tests.retrofit.RetrofitTestActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Url;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;
import rx.subjects.PublishSubject;
import rx.subjects.ReplaySubject;
import rx.subjects.Subject;

/**
 * Created by ly on 22/02/2017.
 */

public class RxJavaTestActivity extends Activity {

    private Observable<Integer> ob2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        helloworld();

        combineObservableTest();


    }


    class Image{

        Data data;
        String msg;
        String code;
    }

    class Data{
        List<Img> list;
        String size;
    }

    class Img{
        String index;
        String target_url;
        String url;
    }

    public interface TestService{
        @GET
        Observable<Image> getImages(@Url String path);
    }


    private void combineObservableTest(){
        final ReplaySubject<Integer> behaviorSubject = ReplaySubject.create();


//        final Observer<Integer> observer = new Observer<Integer>() {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//
//            }
//
//            @Override
//            public void onNext(Integer integer) {
//                System.out.println("observer: " + String.valueOf(integer) + "\n");
//            }
//        };


        Observable<Integer> ob1 = Observable.create(new Observable.OnSubscribe<Integer>() {
            public int count = 0;
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                count += 1;
                for(int i=0;i<5;i++){
                    if(subscriber.isUnsubscribed())continue;
                    subscriber.onNext(i);
//                    observer.onNext(i);
//                    behaviorSubject.onNext(i);
                }
                if(!subscriber.isUnsubscribed())subscriber.onCompleted();
//                behaviorSubject.onCompleted();
//                    observer.onCompleted();

                System.out.println("count: " + String.valueOf(count));
            }
        });



        ob1.subscribe(behaviorSubject);

        ob2 = Observable.create(new Observable.OnSubscribe<Integer>() {
            int count2 = 0;
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                for(int i=6;i<11;i++){
                    if(subscriber.isUnsubscribed())continue;
                    subscriber.onNext(i);
                    if(i==8&&count2==0){
                        subscriber.onError(new Throwable("fuck"));
                    }
                }
                if(!subscriber.isUnsubscribed())subscriber.onCompleted();
                count2+=1;
                System.out.println("count2: " + String.valueOf(count2));
            }
        });


        Observable<String> ob3 = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                String[] ss = new String[]{"a", "b", "c", "d", "e"};
                for(String s: ss){
                    subscriber.onNext(s);
                }
                subscriber.onCompleted();
            }
        });


        Observable obMerge = Observable.merge(ob1, ob2);

        obMerge.subscribe(new Observer() {
            @Override
            public void onCompleted() {
                System.out.println("completed...");
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("error...");
                ob2.retry();
            }

            @Override
            public void onNext(Object o) {

            }
        });


//
//
//        Observable<Object> ob = Observable.concat(ob3.cast(Object.class), behaviorSubject.cast(Object.class));
//        ob.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).
//                subscribe(new Observer<Object>() {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//
//            }
//
//            @Override
//            public void onNext(Object o) {
//                System.out.println(o instanceof String? o : String.valueOf(o) + "\n");
//            }
//        });
//
//        behaviorSubject.subscribe(new Observer<Integer>() {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//
//            }
//
//            @Override
//            public void onNext(Integer integer) {
//                System.out.println("fuck: " + String.valueOf(integer) + "\n");
//            }
//        });


//        Observable<Integer> mergedOb = Observable.merge(ob1, ob2);

//        behaviorSubject.subscribe(new Observer<Integer>() {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//
//            }
//
//            @Override
//            public void onNext(Integer integer) {
//                System.out.println("subject: " +
//                        String.valueOf(integer)+"\n");
//            }
//        });

//        mergedOb.subscribe(new Observer<Integer>() {
//            @Override
//            public void onCompleted() {
//            }
//
//            @Override
//            public void onError(Throwable e) {
//
//            }
//
//            @Override
//            public void onNext(Integer integer) {
//                System.out.println("merged ob: " +
//                        String.valueOf(integer)+"\n");
//            }
//        });


//        ob1.subscribe(new Observer<Integer>() {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//
//            }
//
//            @Override
//            public void onNext(Integer integer) {
//                System.out.println("ob1: " + String.valueOf(integer)+"\n");
//            }
//        });
    }


    private void helloworld(){
        /**
         * create observer
         */
//        Observable<Integer> observable = Observable.create(new Observable.OnSubscribe<Integer>() {
//            @Override
//            public void call(Subscriber<? super Integer> subscriber) {
//                for(int i=0;i<5;i++){
//                    subscriber.onNext(i);
//                }
//
//                subscriber.onCompleted();
//            }
//        });

        /**
         * from
         * no premitive types, need wrapper class
         */

//        Integer[] arr = new Integer[]{1, 2, 3};
//        ArrayList<Integer> arl = new ArrayList<>();
//        arl.add(1);
//        arl.add(2);
//        arl.add(3);
//        arl.add(4);
//        Observable<Integer> observable = Observable.from(arr);

        /**
         * future
         */

//        Observable<Integer> observable = Observable.just(fuck(), fuck());
//
//
//        Observable.create(new Observable.OnSubscribe<Object>() {
//            @Override
//            public void call(Subscriber<? super Object> subscriber) {
//
//            }
//        });


        /**
         * With Retrofit
         */
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.1.154/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();



        TestService testService = retrofit.create(TestService.class);

        Subscription subscription = testService.getImages("home/popbannerlist.mx")
                .subscribeOn(Schedulers.io())
                .distinct()
                .subscribe(new Observer<Image>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Image image) {
                        System.out.println("fuck");
                    }
                });






//        Subscription subscription = observable
//                .filter(new Func1<Integer, Boolean>() {
//                    @Override
//                    public Boolean call(Integer integer) {
//                        return integer == 1;
//                    }
//                }).subscribe(new Observer<Integer>() {
//                    @Override
//                    public void onCompleted() {
//                        System.out.println("finished");
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//
//                    @Override
//                    public void onNext(Integer integer) {
//                        System.out.println("" + String.valueOf(integer));
//                    }
//                });


    }

    public Integer fuck(){
        try {
            LinkedHashMap<String, String> lhm = new LinkedHashMap<>();
            JSONObject jsonObject = new JSONObject("{fuck:'1', lis:'2'}");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 1;
    }




}
