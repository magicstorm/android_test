package com.lansun.tests.entity;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.lansun.tests.network.InternalJsonResponse;
import com.lansun.tests.network.Mapi;
import com.lansun.tests.network.Request;
import com.lansun.tests.network.RequestError;
import com.lansun.tests.utils.HelperFunctions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by ly on 11/29/16.
 */

public abstract class AsyncModel implements AsyncImageItem{

    private String id;

    public ImageStatus imageStatus = ImageStatus.No_Url;
    private com.lansun.tests.Mapi mapi;

    public ImageStatus getImageStatus() {
        return imageStatus;
    }

    protected void setImageStatus(ImageStatus imageStatus) {
        this.imageStatus = imageStatus;
    }

    public AsyncModel(){
        mapi = com.lansun.tests.Mapi.getInstance();
    }


    public enum ImageStatus{
        Downloaded, Downloading, No_Url, Url_Ready, Failed
    }

    public interface ImageObserver{
        void onImageDownloadSuccess(Bitmap bm, String id);
        void onImageDownloadFailed();
    }


    public void getImage(final ImageObserver io) {
        if(getImageUrl()==null)return;
        setImageStatus(ImageStatus.Downloading);
        mapi.getImageLoader().get(this.getImageUrl(), new ImageListener(io));
    }
    public void getImage(final ImageObserver io, int width, int height) {
        if(getImageUrl()==null)return;
        setImageStatus(ImageStatus.Downloading);
        mapi.getImageLoader().get(this.getImageUrl(), new ImageListener(io), width, height);
    }

    public void getImage(final ImageObserver io, int width, int height, ImageView.ScaleType scaleType ) {
        if(getImageUrl()==null)return;
        setImageStatus(ImageStatus.Downloading);
        mapi.getImageLoader().get(this.getImageUrl(), new ImageListener(io), width, height , scaleType);
    }




    public void getImageAndNotify(final Adapter adapter) {
        getImage(new NotifyImageObserver(adapter));
    }

    public void getImageAndNotify(final Adapter adapter, int width, int height) {
        getImage(new NotifyImageObserver(adapter), width, height);
    }

    public void getImageAndNotify(final Adapter adapter, int width, int height, ImageView.ScaleType scaleType) {
        getImage(new NotifyImageObserver(adapter), width, height, scaleType);
    }

    static class Params extends Request.Params{
        public Params(){
            super();
        }
    }


    public static <T> void getDatas(Class<T> clazz, Params params, AsyncModel.CallBack callback, String key, String path){
        getDatas(clazz, params, callback, key, path, 0);
    }

    public static <T> void getDatas(final Class<T> clazz, Params params, final AsyncModel.CallBack<T> callback, final String key, String path, final int startId){
        if(callback==null){
            return;
        }

        Mapi.getInstance().exec(path, params, Request.Method.GET, new Mapi.JsonRequestCallBack() {
            @Override
            public void onReceiveInternalResponse(InternalJsonResponse response) {
                HashMap<String, T> items = new HashMap<String, T>();
                if(response.getCode().equals("200")){
                    try {
                        JSONArray jsonArray = response.getData().getJSONArray("list");
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject categoryObject = jsonArray.getJSONObject(i);
                            AsyncModel item = (AsyncModel)clazz.newInstance();
                            HelperFunctions.jsonObjToCustomObj(categoryObject, item);
                            item.setId(hasKey(categoryObject, key)?categoryObject.getString(key):String.valueOf(startId+i));

                            items.put(item.getId(), (T)item);
                            item.setImageStatus(item.getImageUrl()!=null? ImageStatus.Url_Ready: ImageStatus.No_Url);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }


                    if(items.size()>0){
                        callback.onSuccess(items);
                    }
                    else{
                        callback.onFail(new Error("无数据"));
                    }
                }
                else{
                    AsyncModel.Error error = new Error(response.getMsg());
                    error.setErrorCode(response.getCode());
                    callback.onFail(error);
                }
            }

            @Override
            public void onError(RequestError error) {
                callback.onFail(new Error(error.toString()));
            }

            @Override
            public void onReceiveThirdpartyResponse(JSONObject response) {

            }
        });
    }

    private static boolean hasKey(JSONObject categoryObject, String key) {
        return !TextUtils.isEmpty(key)&&categoryObject.has("key");
    }

    public static <T> void getImagesForMap(HashMap<String, T> datas, AsyncModel.ImageObserver observer) {
        getImagesForMap(datas, observer, 0, 0, null);
    }

    public static <T> void getImagesForMap(HashMap<String, T> datas, AsyncModel.ImageObserver observer, int width, int height) {
        getImagesForMap(datas, observer, width, height, null);
    }

    public static <T> void getImagesForMap(HashMap<String, T> datas, AsyncModel.ImageObserver observer, int width, int height, ImageView.ScaleType scaleType) {
        Set ks = datas.keySet();
        Iterator<String> it = ks.iterator();
        while(it.hasNext()){
            String key = it.next();
            AsyncModel val = (AsyncModel)datas.get(key);
//            AsyncModel.ImageStatus status = val.getImageStatus();
            if(width==0&&height==0){
                val.getImage(observer);
            }
            else if(scaleType==null){
                val.getImage(observer, width, height);
            }
            else{
                val.getImage(observer, width, height, scaleType);
            }
        }
    }





    class ImageListener implements ImageLoader.ImageListener {
        ImageObserver io;
        public ImageListener(ImageObserver io){
            this.io = io;
        }
        @Override
        public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
            AsyncModel.this.setImage(response.getBitmap());
            setImageStatus(ImageStatus.Downloaded);
            if(io!=null){
                io.onImageDownloadSuccess(AsyncModel.this.getItemImage(), AsyncModel.this.getId());
            }
        }

        @Override
        public void onErrorResponse(VolleyError error) {
            setImageStatus(ImageStatus.Failed);
            if(io!=null){
                io.onImageDownloadFailed();
            }
        }
    }

    public interface Adapter{
        void notifyDataSetChanges();
    }


    class NotifyImageObserver implements ImageObserver{
        Adapter adapter;
        public NotifyImageObserver(Adapter adapter){
            this.adapter = adapter;

        }
        @Override
        public void onImageDownloadSuccess(Bitmap bm, String key) {
            if(adapter!=null){
                adapter.notifyDataSetChanges();
            }
        }

        @Override
        public void onImageDownloadFailed() {

        }
    }

    public interface CallBack<T>{
        void onSuccess(HashMap<String, T> datas);
        void onFail(Error error);
    }


    public static class Error{
        String errorCode;
        String errorMessage;
        public Error(String errorMessage){
            this.errorMessage = errorMessage;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }

        public String getErrorCode() {
            return errorCode;
        }

        public void setErrorCode(String errorCode) {
            this.errorCode = errorCode;
        }

    }


    public static class DataErrorException extends Exception{
        public DataErrorException(){
            super();
        }

        public DataErrorException(String msg){
            super(msg);
        }

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
