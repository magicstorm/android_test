package com.lansun.tests;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.telephony.TelephonyManager;

import com.lansun.tests.network.Mapi;
import com.lansun.tests.network.Request;
import com.lansun.tests.utils.HelperFunctions;
import com.lansun.tests.utils.ViewHelpers;

import org.json.JSONObject;

//import com.lansun.tests.jpush.JPushHelpers;


/**
 * Created by ly on 8/16/16.
 */
public class Config {
    private boolean updated = false;
    private static Config config;

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    private String protocol = "http";

    public static final String PROTOCOL_HTTP = "http";
    public static final String PROTOCOL_HTTPS = "https";


    public static final String MAPI_TEST = "10.0.1.154";
    public static final String MAPI_ONLINE = "139.196.238.172";
    public static final String CONFIG_TEST = "10.0.1.154";
    public static final String CONFIG_ONLINE = "139.196.238.172";


    private String config_domain = CONFIG_TEST;

//    private String mapi_domain = "180.166.161.106:31800";
    private String mapi_domain = MAPI_TEST;


//    10.0.1.154

    //    private String mapi_domain = "192.168.31.194:8000";
    private String appVersion = "2.7.0";

    private String osType = "android";
    private String osVersion;
    private String IMEI = "";

    private String min_version = "2.6.0";
    private String min_version_desc = "更更更健康～";
    private String curr_version = "2.7.0";
    private String curr_version_desc = "男人就要新～";
    private String log_domain = "10.0.1.154";
    //    private String log_domain = "139.196.238.172";
    private String root_domain = "/";
    private String android_download_url = "http://www.baidu.com";



    //for jpush
    private String register_id;


    private Config() {
    }


    public static synchronized Config getInstance() {
        if (config == null) {
            config = new Config();
        }
        return config;
    }

    /**
     * config update callback
     */

    public interface OnConfigurationUpdatedListener {
        void onConfigurationUpdated();
    }

    public void updateConfig(final OnConfigurationUpdatedListener onConfigurationUpdatedListener) {
        com.lansun.tests.network.Mapi mapi = Mapi.getInstance();
        Request.Params params = new Request.Params();
        mapi.exec("/biz/configservice/config", params, Request.Method.GET, new Mapi.JsonRequestCallBack() {
            @Override
            public void onReceiveInternalResponse(com.lansun.tests.network.InternalJsonResponse response) {
                if (response.getCode().equals("200")) {
                    HelperFunctions.jsonObjToCustomObj(response.getData(), Config.config);
                    onConfigurationUpdatedListener.onConfigurationUpdated();
                    setUpdated(true);
                }
            }

            @Override
            public void onError(com.lansun.tests.network.RequestError error) {

            }

            @Override
            public void onReceiveThirdpartyResponse(JSONObject response) {
            }
        });
    }

    private boolean versionValidate() {
        return getVersionInt(config.getAppVersion()) >= getVersionInt(config.getMin_version());
//        return getVersionInt(config.getAppVersion())<getVersionInt(config.getMin_version());
    }

    private boolean isNewestVersion() {
        return getVersionInt(config.getAppVersion()) >= getVersionInt(config.getCurr_version());
        //用来测试
//        return getVersionInt(config.getAppVersion())<getVersionInt(config.getCurr_version());

    }

    private Integer getVersionInt(String version) {
        return Integer.valueOf(version.replace(".", ""));
    }


//    public boolean promptToUpdate(final Context context) {
//        if (!isNewestVersion()) {
//            ViewHelpers.showDialog(context, getStringResrouce(R.string.config_service_update_title, context), Config.getInstance().curr_version_desc,
//                    getStringResrouce(R.string.config_service_update_no, context), new ViewHelpers.OnNegativeBtnClickListener() {
//                        @Override
//                        public void onNegativeBtnClick(DialogInterface dialog, int which) {
//
//                        }
//                    }, getStringResrouce(R.string.config_service_update_ok, context), new ViewHelpers.OnPositiveBtnClickListener() {
//                        @Override
//                        public void onPositiveBtnClickListener(DialogInterface dialog, int which) {
//                            openBrowserForUrl(android_download_url, context, false);
//                        }
//                    }, false);
//            return true;
//        }
//        return false;
//    }


//    private void openBrowserForUrl(String url, Context context, boolean closeApp) {
//
//        Intent intent = null;
//        if (closeApp) {
//            intent = new Intent(context, AppDownloadActivity.class);
//            intent.putExtra("url", url);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//        } else {
//            intent = new Intent();
//            intent.setAction(Intent.ACTION_VIEW);
//            intent.setData(Uri.parse(url));
//        }
//        context.startActivity(intent);
//    }
//
//    private String getStringResrouce(int id, Context context) {
//        return context.getResources().getString(id);
//    }
//
//    public boolean forceToupdate(final Context context) {
//        if (!versionValidate()) {
//            ViewHelpers.showDialog(context, getStringResrouce(R.string.config_service_update_title, context), Config.getInstance().curr_version_desc,
//                    null, null, getStringResrouce(R.string.config_service_update_ok, context), new ViewHelpers.OnPositiveBtnClickListener() {
//                        @Override
//                        public void onPositiveBtnClickListener(DialogInterface dialog, int which) {
//                            openBrowserForUrl(android_download_url, context, true);
//                        }
//                    }, true);
//
//            return true;
//        }
//        return false;
//    }

    /**
     * Getters and Setters
     */

    public String getMapi_domain() {
        return mapi_domain;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }


    public String getOsType() {
        return osType;
    }

    public void setOsType(String osType) {
        this.osType = osType;
    }

    public String getMin_version() {
        return min_version;
    }

    public String getMin_version_desc() {
        return min_version_desc;
    }

    public String getCurr_version() {
        return curr_version;
    }

    public String getCurr_version_desc() {
        return curr_version_desc;
    }

    public String getLog_domain() {
        return log_domain;
    }

    public String getRoot_domain() {
        return root_domain;
    }

    public String getAndroid_download_url() {
        return android_download_url;
    }


    public String getConfig_domain() {
        return config_domain;
    }

    public void setConfig_domain(String config_domain) {
        this.config_domain = config_domain;
    }



    public String getOsVersion() {
        osVersion = Build.VERSION.RELEASE;
        return osVersion;
    }

    //这是设备id
    public String getIMEI(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        IMEI = tm.getDeviceId();
        return IMEI;
    }

    public boolean isUpdated() {
        return updated;
    }

    public void setUpdated(boolean updated) {
        this.updated = updated;
    }

    public void setMapi_domain(String mapi_domain) {
        this.mapi_domain = mapi_domain;
    }
}
