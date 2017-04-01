package com.lansun.tests.caches;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * Created by ly on 8/16/16.
 */
public class LruBitmapCache extends LruCache<String, Bitmap> {
    /**
     * @param maxSize for caches that do not override {@link #sizeOf}, this is
     *                the maximum number of entries in the cache. For all other caches,
     *                this is the maximum sum of the sizes of the entries in this cache.
     */
    public LruBitmapCache(int maxSize) {
        super(maxSize);
    }

    public LruBitmapCache(Context context){
        this(getCacheSize(context));

    }

    @Override
    protected int sizeOf(String key, Bitmap value) {
        return value.getRowBytes() * value.getHeight();
    }

    public void putBitmap(String url, Bitmap bitmap){
        put(url, bitmap);
    }

    public Bitmap getBitmap(String url){
        return get(url);
    }

    public static int getCacheSize(Context context){
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        int availMemBytes = am.getMemoryClass();
        return availMemBytes/8;
    }
}
