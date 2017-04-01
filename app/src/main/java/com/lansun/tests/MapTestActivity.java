package com.lansun.tests;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.lansun.tests.models.Map;
import com.lansun.tests.utils.ImageHelpers;
import com.lansun.tests.utils.ToastSingle;
import com.lansun.tests.utils.ViewHelpers;

/**
 * Created by ly on 06/02/2017.
 */

public class MapTestActivity extends Activity implements View.OnClickListener{

    private EditText addressEt;
    private MapView map;
    private AMap aMap;
    private LocationSource.OnLocationChangedListener mListener;
    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationOpt;
    private float density;
    private MarkerOptions centerMarkerOption;
    private Marker centerMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_test);

        initDatas();
        findViews();
        map.onCreate(savedInstanceState);
        initViews();
        initAMap();
    }

    private void initDatas(){
        DisplayMetrics dm = getResources().getDisplayMetrics();
        density = dm.density;

//        Intent startIntent = getIntent();
//        centerLat = startIntent.getDoubleExtra("centerLat", 34.341568);
//        centerLng = startIntent.getDoubleExtra("centerLng", 108.940174);
//        centerTitle = startIntent.getStringExtra("centerTitle");
//        centerSnippet = startIntent.getStringExtra("snippet");
//        centerDrawableId = startIntent.getIntExtra("drawableId", 0);
    }


    private void initAMap(){
        if(aMap ==null){
            aMap = map.getMap();
        }
//        UiSettings uiSettings = aMap.getUiSettings();
//        uiSettings.setScrollGesturesEnabled(true);


//        LatLng latLng = new LatLng(31.19, 121.70);



//        aMap.invalidate();

        aMap.setOnMarkerDragListener(new AMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
                ToastSingle.showToast(MapTestActivity.this, "拖marker", Toast.LENGTH_SHORT);
            }

            @Override
            public void onMarkerDrag(Marker marker) {
                Double lat = marker.getPosition().latitude;
                Double lng = marker.getPosition().longitude;
                marker.setSnippet("坐标: (" + String.valueOf(lat) + "," + String.valueOf(lng) + ")" );
                marker.showInfoWindow();
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                ToastSingle.showToast(MapTestActivity.this, "拖marker结束", Toast.LENGTH_SHORT);

            }
        });

        aMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                ToastSingle.showToast(MapTestActivity.this, "点击了marker", Toast.LENGTH_SHORT);
                return false;
            }
        });



        aMap.setLocationSource(new LocationSource() {
            @Override
            public void activate(OnLocationChangedListener onLocationChangedListener) {
                mListener = onLocationChangedListener;
                if(mLocationClient ==null){
                    mLocationClient = new AMapLocationClient(MapTestActivity.this);
                    mLocationOpt = new AMapLocationClientOption();
                    mLocationClient.setLocationListener(new AMapLocationListener() {
                        @Override
                        public void onLocationChanged(AMapLocation aMapLocation) {
                            mListener.onLocationChanged(aMapLocation);
                            LatLng latLng1 = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());

                            if(centerMarker==null){
                                aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng1, 14));
                                String title = "好地方";
                                centerMarkerOption = new MarkerOptions().position(latLng1).title(title).snippet("一个好地方").draggable(true).icon(
                                        BitmapDescriptorFactory.fromBitmap(
                                                ImageHelpers.resizeToFit(
                                                        MapTestActivity.this,
                                                        R.drawable.qq,
                                                        ViewHelpers.dpToPixels(8, density),
                                                        ViewHelpers.dpToPixels(8, density),
                                                        ImageHelpers.RESIZE_MODE.FIT_WIDTH)
                                        )
                                );
                                centerMarker = aMap.addMarker(centerMarkerOption);
                            }

                        }
                    });
                    mLocationOpt.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
                    mLocationClient.setLocationOption(mLocationOpt);
                    mLocationClient.startLocation();
                }


            }

            @Override
            public void deactivate() {
                mListener = null;
                if(mLocationClient!=null){
                    mLocationClient.stopLocation();
                    mLocationClient.onDestroy();
                }
                mLocationClient = null;
            }


        });



        aMap.setMyLocationEnabled(true);



    }

    @Override
    protected void onResume() {
        super.onResume();
        map.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        map.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        map.onDestroy();
        if(null != mLocationClient){
            mLocationClient.onDestroy();
        }
    }

    private void findViews(){
        addressEt = (EditText)findViewById(R.id.map_address_et);
        map = (MapView)findViewById(R.id.map);
    }

    private void initViews(){
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        map.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.map_address_to_coordinates_btn:
                String address = addressEt.getText().toString();
                if(!TextUtils.isEmpty(address)){
                    Map.getCoodinatesByAdd(MapTestActivity.this, address, "021");
                }
                break;
        }
    }
}
