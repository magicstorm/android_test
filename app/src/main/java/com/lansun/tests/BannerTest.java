package com.lansun.tests;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.lansun.tests.network.*;
import com.lansun.tests.network.Mapi;
import com.lansun.tests.utils.ViewHelpers;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by ly on 11/28/16.
 */

public class BannerTest extends Activity{

    private String userId = "47654529";
    private String bannerPath = "/biz/bizhome/bannerlist";

    private HashMap<Integer, Banner> banners = new HashMap<>();
    private PagerAdapter bannerVpAdapter;

    //switches
    private boolean isAutoSlide=false;
    private boolean continueSlide = true;


    private int bannerDownloadRetryCount = 0;
    private Mapi mapi;
    private View bannerView;
    private ViewPager bannerVp;
    private DisplayMetrics dm;
    private LinearLayout dotLayout;
    private float density;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner_test);
        findViews();

    }

    @Override
    protected void onResume() {
        dm = getResources().getDisplayMetrics();
        density = dm.density;
        bannerDownloadRetryCount = 0;
        mapi = com.lansun.tests.network.Mapi.getInstance();
//        getBannerImageUrls();
        super.onResume();
    }


    private void findViews(){
        bannerView = View.inflate(this, R.layout.banner_layout, null);
        bannerVp = (ViewPager) bannerView.findViewById(R.id.homepage_banner_vp);
        dotLayout = (LinearLayout) bannerView.findViewById(R.id.dot_layout_ll);
    }


//    private void getBannerImageUrls(){
//        Request.Params bannerUrlParams = new Request.Params();
//        mapi.exec(bannerPath, bannerUrlParams, Request.Method.GET, new Mapi.JsonRequestCallBack() {
//            @Override
//            public void onReceiveInternalResponse(final InternalJsonResponse response) {
//
//                if(response.getCode().equals("200")){
//                    try {
//                        for(int i=0;i<Integer.valueOf(response.getData().getString("size"));i++){
//                            Banner tmpBanner = new Banner();
//                            if(!banners.containsKey(i)) {
//                                String bannerUrl = response.getData().getJSONArray("list").getJSONObject(i).getString("url");
//                                tmpBanner.setBannerUrl(bannerUrl);
//                                tmpBanner.setBannerImage(null);
//                                banners.put(i, tmpBanner);
//                            }
//                            final int j = i;
//                            if(banners != null && banners.containsKey(i)) {
//                                com.lansun.tests.Mapi.getInstance().getImageLoader().get(banners.get(i).getBannerUrl(), new ImageLoader.ImageListener() {
//                                    @Override
//                                    public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
//                                        if (response.getImage() != null && banners != null && banners.containsKey(j)) {
//                                            banners.get(j).setBannerImage(response.getImage());
//                                            refreshCurrentPage(j, response);
//                                        }
//                                    }
//
//                                    @Override
//                                    public void onErrorResponse(VolleyError error) {
//
//                                    }
//                                }, dm.widthPixels/2, bannerVp.getMeasuredHeight()/2);
//                            }
//                        }
//                        if(banners.size()>0){
//                            setupBanner();
//                            resetBanner();
//                            initDots();
//                        }
//                        else if(banners.size()>0){
//                            bannerVpAdapter.notifyDataSetChanged();
//                        }
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    //consider retry strategy according to return code
//                }
//
//                if(banners.size()==0&&bannerDownloadRetryCount<3){
//                    bannerDownloadRetryCount += 1;
//                    getBannerImageUrls();
//                }
//
//            }
//             @Override
//            public void onError(RequestError error) {
//                if(banners.size()==0){
//                    getBannerImageUrls();
//                }
//            }
//
//            @Override
//            public void onReceiveThirdpartyResponse(JSONObject response) {
//                if(banners.size()==0){
//                    getBannerImageUrls();
//                }
//            }
//        });
//    }

    private void setupBanner() {
        bannerVpAdapter = new PagerAdapter() {
            @Override
            public int getCount() {

                return Integer.MAX_VALUE;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view==object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, final int position) {
                View view = View.inflate(BannerTest.this, R.layout.banner_cell, null);
                ImageView iv = (ImageView) view.findViewById(R.id.homepage_vpimage_iv);
                if(banners.size()>0) {
                    int realPosition = position % banners.size();
                    if (banners.size() > realPosition) {
                        if (banners.get(realPosition).getBannerImage() == null) {
                        } else {
                            iv.setImageBitmap(banners.get(realPosition).getBannerImage());
                        }
                    }
                    container.addView(view);
                    view.setTag(position);
                }

                return view;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View)object);
            }
        };

        bannerVp.setAdapter(bannerVpAdapter);

        bannerVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(dotLayout.getChildCount()>0){
                    updateDot();
                }
                else{
                    if(banners.size()>0){
                        initDots();
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    private void resetBanner() {
        if(banners.size()>0){
//            int itemNum = Integer.MAX_VALUE/2-((Integer.MAX_VALUE/2)%banners.size());
            bannerVp.setCurrentItem(30);
            if(!isAutoSlide()){
                setAutoSlide(true);
                autoSlideBanner();
            }
        }
    }

    private void initDots(){
        if(banners!=null&&banners.size()>1){
            for(int i=0;i<banners.size();i++){
                if(dotLayout.getChildAt(i)==null){
                    View dotView = new View(this);
                    LinearLayout.LayoutParams dotParams = new LinearLayout.LayoutParams(ViewHelpers.dpToPixels(7, density), ViewHelpers.dpToPixels(7, density));
                    if(i!=0){
                        dotParams.leftMargin = ViewHelpers.dpToPixels(5, density);
                    }
                    dotView.setBackgroundResource(R.drawable.selector_dot);
                    dotView.setLayoutParams(dotParams);
                    dotLayout.addView(dotView);
                }
            }
            updateDot();
        }
    }

    private void updateDot(){
        if(banners.size()>1){
            int currentIndex = bannerVp.getCurrentItem()%banners.size();
            for(int i=0; i<banners.size();i++){
                dotLayout.getChildAt(i).setEnabled(currentIndex==i);
            }
        }
    }

    private void autoSlideBanner(){
        if(banners.size()>1){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    bannerVp.setCurrentItem(bannerVp.getCurrentItem() + 1, true);
                    if (isContinueSlide()) {
                        autoSlideBanner();
                    } else {
                        setAutoSlide(false);
                    }
                }
            }, 3000);
        }
    }

    private void refreshCurrentPage(int num, ImageLoader.ImageContainer response) {
        if(bannerVpAdapter!=null&& bannerVp !=null&&banners!=null&&banners.size()>0){
            int currentItem = bannerVp.getCurrentItem();
            for(int i=(currentItem-1); i<(currentItem+2);i++){
                if((i%banners.size())==num){
                    View v = bannerVp.findViewWithTag(i);
                    if(v!=null){
                        ImageView iv = (ImageView)v.findViewById(R.id.homepage_vpimage_iv);
                        banners.get(i%banners.size()).setBannerImage(response.getBitmap());
                        iv.setImageBitmap(response.getBitmap());
                    }
                }
            }
        }
    }


    /**
     * Getters and Setters
     */
    public synchronized boolean isAutoSlide() {
        return isAutoSlide;
    }

    public synchronized void setAutoSlide(boolean autoSlide) {
        isAutoSlide = autoSlide;
    }


    public synchronized boolean isContinueSlide() {
        return continueSlide;
    }

    public synchronized void setContinueSlide(boolean continueSlide) {
        this.continueSlide = continueSlide;
    }

}
