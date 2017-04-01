package com.lansun.tests.network;


import android.app.ActivityManager;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;

import com.lansun.tests.Config;
import com.lansun.tests.utils.HelperFunctions;

import org.json.JSONObject;

import java.io.BufferedInputStream;
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

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;


public class Mapi {


    private Config config;
    private String baseUrl;
    private String imageBaseUrl;
    private Handler handler = new Handler(Looper.getMainLooper());

    //singleTon
    private static Mapi mMapi = null;
    private static RequestQueue requestQueue = null;
    private static DefaultScheduler scheduler = null;

//    private static ImageLoader mImageLoader = null;


    //id for request tag
    private static long requestId = 0;
    private static String certFilePath = "";


    /* Constructor */
    private Mapi(){
        setConfig();
        setBaseUrl();
//        setImageBaseUrl();
    }

    private enum Dest{
        INTERNAL, THIRDPARTY
    }




    /* apis for upper layer */
    public static synchronized Mapi getInstance(){
        if(mMapi == null){
            mMapi = new Mapi();
        }
        setRequestQueue();
        scheduler = new DefaultScheduler(requestQueue);
//        setImageLoader();
        return mMapi;
    }


    public interface JsonRequestCallBack{
        void onReceiveInternalResponse(InternalJsonResponse response);
        void onError(RequestError error);
        void onReceiveThirdpartyResponse(JSONObject response);
    }

    /**
     * @param uri home/api
     * @param params Params extends from Hashmap, value can be String or another JSONObject
     * @param method enum Mapi.RequestType
     * @param callBack
     * @param tag for cancel requests that have same tag
     * @return requestId for cancel request
     */


    public long exec(String uri, Request.Params params, Request.Method method, JsonRequestCallBack callBack, String tag){
        return exec(uri, params, method, callBack, tag, false);
    }

    public long exec(String uri, Request.Params params, Request.Method method, JsonRequestCallBack callBack){
        return exec(uri, params, method, callBack, null, false);
    }

    public long exec(String uri, Request.Params params, Request.Method method, JsonRequestCallBack callBack, String tag, boolean defaultParams){
        if(!uri.equals("/biz/configservice/config")&&!config.isUpdated()){
            config.updateConfig(new Config.OnConfigurationUpdatedListener() {
                @Override
                public void onConfigurationUpdated() {
//                    ToastSingle.showToast(JupiterUserApplication.getContext(), "configuration updated", Toast.LENGTH_SHORT);
                }
            });
        }
        return sendJsonRequest(composeRequestUrl(uri), params, method, callBack, tag, Dest.INTERNAL, defaultParams);
    }

    public long exec(String uri, Request.Params params, Request.Method method, JsonRequestCallBack callBack, boolean defaultParams){
        return exec(uri, params, method, callBack, null, defaultParams);
    }


    public long execThirdParty(String url, Request.Params params, Request.Method method, JsonRequestCallBack callBack){
        return execThirdParty(url, params, method, callBack, null);
    }

    public long execThirdParty(String url, Request.Params params, Request.Method method, JsonRequestCallBack callBack, String tag){
        return sendJsonRequest(url, params, method, callBack, tag, Dest.THIRDPARTY, false);
    }

    private long sendJsonRequest(String url, Request.Params params,
                                 Request.Method method, final JsonRequestCallBack callback,
                                 String tag, final Dest dest, boolean defaultParams){

        if(params==null){
            params = new Request.Params();
        }

//        if(defaultParams){
//            addDefaultParams(params);
//        }

//        Config.promptToUpdate();

        Request request = new Request(url, method, params, new Request.RequestCallBack() {
            @Override
            public void onReceiveResponse(final JSONObject response) {
                if(dest== Dest.INTERNAL){
                    final InternalJsonResponse internalJsonResponse = new InternalJsonResponse();
                    HelperFunctions.jsonObjToCustomObj(response, internalJsonResponse);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onReceiveInternalResponse(internalJsonResponse);
                        }
                    });
                }else if(dest== Dest.THIRDPARTY){
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onReceiveThirdpartyResponse(response);
                        }
                    });
                }
            }
            @Override
            public void onError(final RequestError error) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onError(error);
                    }
                });
            }
        });
        return requestQueue.addRequest(request, tag);
    }


//    /* Helpers */
//    private void addDefaultParams(Request.Params params) {
//        params.put("app_v", config.getAppVersion());
//        params.put("os_v", config.getOsVersion());
//        params.put("device_id", config.getIMEI());
//        params.put("os_t", config.getOsType());
//        //jpush registration id
////        params.put("register_id", config.getRegister_id()==null?"":config.getRegister_id());
//    }

    private static void setRequestQueue() {
        if(requestQueue == null){
            requestQueue = new RequestQueue();
        }
    }

    private static SSLSocketFactory generateSSLSocketFactory(String certFilePath) throws Exception {
        boolean contextGenerated = true;
        InputStream caInput = null;
        SSLContext context = null;
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

    private String getSuffix(){
        return ".mx";
    }

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
        baseUrl = config.getProtocol() + "://" + config.getMapi_domain() +
                (!isEmptyPath(config.getRoot_domain())?config.getRoot_domain():"");
    }

    private boolean isEmptyPath(String path){
        return path.replace("/", "").equals("");
    }

    private void setImageBaseUrl(){
        imageBaseUrl = baseUrl;
    };


    public interface ImageListener{
        void onImageDownloadSuccess(Bitmap image);
        void onImageDownloadFailed();
    }
    public void getImage(String url, ImageListener imageListener, int width, int Height, boolean diskcache){

    }

}



