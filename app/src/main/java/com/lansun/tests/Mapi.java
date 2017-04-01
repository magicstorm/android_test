package com.lansun.tests;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.lansun.tests.caches.LruBitmapCache;
import com.lansun.tests.utils.HelperFunctions;
import com.lansun.tests.network.InternalJsonResponse;
import com.lansun.tests.network.RequestError;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

public class Mapi {


    private Config config;
    private String baseUrl;
    private String imageBaseUrl;

    //singleTon
    private static Mapi mMapi = null;
    private static RequestQueue requestQueue = null;
    private static ImageLoader mImageLoader = null;


    //id for request tag
    private static long requestId = 0;
    private static String certFilePath = "";

    public enum RequestType{
        POST, GET,
    }

    /* Constructor */
    private Mapi(){
        setConfig();
        setBaseUrl();
        setImageBaseUrl();
    }




    /* public interfaces */

    //callback interface
    public interface JsonRequestCallBack{
        void onReceiveInternalResponse(InternalJsonResponse response);
        void onError(RequestError error);
        void onReceiveThirdpartyResponse(JSONObject response);
    }

    //request params
    public static class Params extends HashMap{
        public Params(){
            super();
        }
    }

    /* apis for upper layer */

    public static synchronized Mapi getInstance(){
        if(mMapi == null){
            mMapi = new Mapi();
        }
        setRequestQueue();
        setImageLoader();
        return mMapi;
    }

    //get image url by path
    public  String getImageUrl(String path){
        return imageBaseUrl + path;
    }


    // get ImageLoader for NetworkImageView
    public ImageLoader getImageLoader(){
        return mImageLoader;
    }
    /**
     * @param tag string or int tag
     * @param <T>
     */
    public <T> void cancelRequest(T tag){
        cancelVolleyRequest(tag);
    }

     /**
     * @param uri home/api
     * @param params Params extends from Hashmap, value can be String or another JSONObject
     * @param requestType enum Mapi.RequestType
     * @param callBack
     * @param tag for cancel requests that have same tag
     * @return requestId for cancel request
     */
    public long exec(String uri, Params params, Enum requestType, JsonRequestCallBack callBack, String tag){
        return sendJsonRequest(composeRequestUrl(uri), params, requestType, callBack, tag, "internal");
    }

    public long exec(String uri, Params params, Enum requestType, JsonRequestCallBack callBack){
        return exec(uri, params, requestType, callBack, null);
    }

    public long execThirdParty(String url, Params params, Enum requestType, JsonRequestCallBack callBack, String tag){
        return sendJsonRequest(url, params, requestType, callBack, tag, "third_party");
    }


    /* Helpers */
    private String getSuffix(){
        return ".mx";
    }

    private static synchronized long getRequestId() {
        requestId += 1;
        return requestId;
    }

    private <T> void cancelVolleyRequest(T tag) {
        if(requestQueue !=null){
            requestQueue.cancelAll(tag);
        }
    }

    private long sendVolleyJsonRequest(String url, final Params params, Enum requestType, final JsonRequestCallBack callBack,
                                       String tag, final String dest) {
        int volleyRequestType = requestType== RequestType.GET? Request.Method.GET:Request.Method.POST;
//        if(dest=="internal"){
//            addDefaultParams(params);
//        }

        String fullUrl = url;
        if(volleyRequestType == Request.Method.GET){
            fullUrl = addParamsToUrl(params, url);
        }
        //get convert params to JSONObject
        final JSONObject jsonObject = params==null?null:new JSONObject(params);

        System.out.println("url: " + url);

//        volleyRequestType == Request.Method.GET?null:jsonObject.toString(),
        StringRequest stringRequest = new StringRequest(
                volleyRequestType,
                volleyRequestType == Request.Method.GET ? fullUrl : url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        returnJsonResponse(response, callBack, dest);
//                        ToastSingle.showToast(TestsApplication.getContext(), response, Toast.LENGTH_LONG);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        returnRequestError(error, callBack);
//                        ToastSingle.showToast(TestsApplication.getContext(), error.toString(), Toast.LENGTH_LONG);
                    }
                }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };

        long requestId = getRequestId();
        stringRequest.setTag(tag==null?requestId:tag);
        stringRequest.setShouldCache(false);
        requestQueue.add(stringRequest);
        return requestId;
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

    private void returnRequestError(VolleyError error, JsonRequestCallBack callBack) {
        RequestError re = new RequestError();
        re.setErrorMessage(error.toString());
        callBack.onError(re);
    }


    private void returnJsonResponse(String response, JsonRequestCallBack callBack, String dest) {
        JSONObject resp=null;
        try {
            resp = new JSONObject(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(dest=="internal"){
            InternalJsonResponse jsResponse = new InternalJsonResponse();
            HelperFunctions.jsonObjToCustomObj(resp, jsResponse);

            callBack.onReceiveInternalResponse(jsResponse);
        }
        else if(dest=="third_party"){
            callBack.onReceiveThirdpartyResponse(resp);
        }
    }

    //for jsonobject request
//    private void returnJsonResponse(JSONObject response, JsonRequestCallBack callBack, String dest) {
//        if(dest=="internal"){
//            InternalJsonResponse jsResponse = new InternalJsonResponse();
//            HelperFunctions.jsonObjToCustomObj(response, jsResponse);
//            callBack.onReceiveInternalResponse(jsResponse);
//        }
//        else if(dest=="third_party"){
//            callBack.onReceiveThirdpartyResponse(response);
//        }
//    }

//    private JSONObject params2JSONObject(Params params) {
//        JSONObject requestObject = new JSONObject();
//        Set<String> ks = params.keySet();
//        Iterator<String> it = ks.iterator();
//        while(it.hasNext()){
//            String key = it.next();
//            String val = (String)params.get(key);
//
//            try {
//                requestObject.put(key, val);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//
//        return requestObject;
//    }

    private void addDefaultParams(Params params) {
        params.put("app_v", config.getAppVersion());
        params.put("os_v", config.getOsVersion());
        params.put("device_id", config.getIMEI(TestsApplication.getContext()));
        params.put("os_t", config.getOsType());
    }


    /* mapi methods */

    private static void setImageLoader() {
        if(mImageLoader == null){
            mImageLoader = new ImageLoader(requestQueue, new ImageLoader.ImageCache(){
                private final LruBitmapCache cache = new LruBitmapCache(TestsApplication.getContext());

                @Override
                public Bitmap getBitmap(String url) {
                    return cache.get(url);
                }

                @Override
                public void putBitmap(String url, Bitmap bitmap) {
                    cache.put(url, bitmap);
                }
            });
        }
    }

    private static void setRequestQueue() {
        if(requestQueue == null){
            initRequestQueue();
        }

    }

    private static void initRequestQueue() {
        Cache cache = new DiskBasedCache(TestsApplication.getContext().getCacheDir(),
                1024 * 1024); // 1MB cap

        HurlStack hs = null;
        try {
            hs = new File(certFilePath).exists()?new HurlStack(null, generateSSLSocketFactory(certFilePath)):new HurlStack();
        } catch (Exception e) {
            e.printStackTrace();
            hs = new HurlStack();
        }

        Network network = new BasicNetwork(hs);

        requestQueue = new RequestQueue(cache, network);

        requestQueue.start();

//            requestQueue = Volley.newRequestQueue(TestsApplication.getContext());
    }

    private static SSLSocketFactory generateSSLSocketFactory(String certFilePath) throws Exception {
        boolean contextGenerated = true;
        InputStream caInput = null;
        SSLContext  context = null;
        try {

            // Load CAs from an InputStream
            // (could be from a resource or ByteArrayInputStream or ...)
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            caInput = new BufferedInputStream(new FileInputStream(certFilePath));

            // From https://www.washington.edu/itconnect/security/ca/load-der.crt
            Certificate ca = cf.generateCertificate(caInput);
            System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());


            // Create a KeyStore containing our trusted CAs
            String keyStoreType = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);


            // Create a TrustManager that trusts the CAs in our KeyStore
            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);


            // Create an SSLContext that uses our TrustManager
            context = SSLContext.getInstance("TLS");
            context.init(null, tmf.getTrustManagers(), null);

        } catch (CertificateException e) {
            contextGenerated = false;
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            contextGenerated = false;
            e.printStackTrace();
        }
        catch (NoSuchAlgorithmException e) {
            contextGenerated = false;
            e.printStackTrace();
        } catch (KeyStoreException e) {
            contextGenerated = false;
            e.printStackTrace();
        } catch (IOException e) {
            contextGenerated = false;
            e.printStackTrace();
        } catch (KeyManagementException e) {
            contextGenerated = false;
            e.printStackTrace();
        } finally {
            try {
                caInput.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(contextGenerated){
            return context.getSocketFactory();
        }
        else {
            throw new Exception();
        }
    }



    private long sendJsonRequest(String url, Params params, Enum requestType,
                                 final JsonRequestCallBack callBack, String tag, String dest){
        return sendVolleyJsonRequest(url, params, requestType, callBack, tag, dest);
    }

    @NonNull
    private String composeRequestUrl(String uri) {
        return baseUrl + uri + getSuffix();
    }


    /**
     * Getters & Setters
     */

    private void setConfig() {
        config = Config.getInstance();
    }

    private void setBaseUrl(){
        baseUrl = config.getProtocol() + "://" + config.getMapi_domain();
    }
    private void setImageBaseUrl(){
        imageBaseUrl = baseUrl;
    };
}
