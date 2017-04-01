package com.lansun.tests.models;

import android.content.Context;
import android.widget.Toast;

import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.lansun.tests.utils.ToastSingle;

import java.util.HashMap;
import java.util.List;

/**
 * Created by ly on 06/02/2017.
 */

public class Map {


    private final Context mContext;

    public Map(Context mContext){
        this.mContext = mContext;
    }

    public static void getCoodinatesByAdd(final Context context, String address, String cityCode) {
        GeocodeSearch geocoderSearch = new GeocodeSearch(context);
        geocoderSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {

            }

            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
                if(i==1000){
                    List<GeocodeAddress> addresses = geocodeResult.getGeocodeAddressList();
                    String resultText = "";
                    int index = 0;
                    for(GeocodeAddress address:addresses){
                        resultText += "add" + String.valueOf(index) + ": (" + String.valueOf(address.getLatLonPoint().getLatitude()) + "," +
                                String.valueOf(address.getLatLonPoint().getLongitude()) + ")\n";
                        index += 1;
                    }


                    ToastSingle.showToast(context, resultText, Toast.LENGTH_SHORT);
                }
                else{
                    ToastSingle.showToast(context, "获取坐标失败, code: " + String.valueOf(i), Toast.LENGTH_SHORT);
                }
            }
        });

        GeocodeQuery query = new GeocodeQuery(address, cityCode);
        geocoderSearch.getFromLocationNameAsyn(query);


    }

}
