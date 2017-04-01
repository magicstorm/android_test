package com.lansun.tests;

import android.graphics.Bitmap;

/**
 * Created by ly on 10/11/16.
 */

public class Banner {


    int index;
    String bannerUrl;
    String targetUrl;
    Bitmap bannerImage;

    @Override
    public int hashCode() {
        return index;
    }

    public boolean equals(Object o){
        return index==((Banner)o).getIndex();
    }


    /**
     * Getters and Setters
     */
        public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getBannerUrl() {
        return bannerUrl;
    }

    public void setBannerUrl(String bannerUrl) {
        this.bannerUrl = bannerUrl;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    public Bitmap getBannerImage() {
        return bannerImage;
    }

    public void setBannerImage(Bitmap bannerImage) {
        this.bannerImage = bannerImage;
    }
}
