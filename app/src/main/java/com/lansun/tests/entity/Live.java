package com.lansun.tests.entity;

import android.graphics.Bitmap;

/**
 * Created by ly on 11/29/16.
 */

public class Live extends AsyncModel {

    String anchor_id;
    String anchor_name;
    String anchor_type;
    String audit_accid;
    String chat_room_id;
    String cover_pic;
    String cover;
    String live_desc;
    String live_id;
    String live_name;
    String start_timestamp;
    String video_addr;
    String watching_count;
    Bitmap coverBitmap;

    public Live(){
        super();
    }

    private static String path = "";
    private static String key = "";
    private static final String topLiving5Path = "/home/toplivinglist";
    private static final String topToLive5Path = "/home/topcominglivelist";
    private static final String starLivePath = "/home/starlivelist";


    public static void getTopLiving5(CallBack callBack) {
        AsyncModel.getDatas(Live.class, null, callBack, key, topLiving5Path);
    }

    public static void getTopComing5(CallBack callBack) {
        AsyncModel.getDatas(Live.class, null, callBack, key, topToLive5Path);
    }


    public static void getStarLivesInRange(int start, int num, CallBack callBack) {
        Params params = new Params();
        params.put("index", String.valueOf(start));
        params.put("size", String.valueOf(num));
        AsyncModel.getDatas(Live.class, params, callBack, key, starLivePath, start);
    }


    @Override
    public String getImageUrl() {
        return cover_pic==null?cover:cover_pic;
    }

    @Override
    public void setImage(Bitmap image) {
        coverBitmap = image;
    }


    /**
     * Getters and Setters
     */

    public static String getPath() {
        return path;
    }

    public static void setPath(String path) {
        Live.path = path;
    }

    public static String getKey() {
        return key;
    }

    public static void setKey(String key) {
        Live.key = key;
    }

    public String getAnchor_id() {
        return anchor_id;
    }

    public void setAnchor_id(String anchor_id) {
        this.anchor_id = anchor_id;
    }

    public String getAnchor_type() {
        return anchor_type;
    }

    public void setAnchor_type(String anchor_type) {
        this.anchor_type = anchor_type;
    }

    public String getAudit_accid() {
        return audit_accid;
    }

    public void setAudit_accid(String audit_accid) {
        this.audit_accid = audit_accid;
    }

    public String getChat_room_id() {
        return chat_room_id;
    }

    public void setChat_room_id(String chat_room_id) {
        this.chat_room_id = chat_room_id;
    }

    public String getCover_pic() {
        return cover_pic;
    }

    public void setCover_pic(String cover_pic) {
        this.cover_pic = cover_pic;
    }

    public String getLive_id() {
        return live_id;
    }

    public void setLive_id(String live_id) {
        this.live_id = live_id;
    }

    public String getLive_name() {
        return live_name;
    }

    public void setLive_name(String live_name) {
        this.live_name = live_name;
    }

    public String getStart_timestamp() {
        return start_timestamp;
    }

    public void setStart_timestamp(String start_timestamp) {
        this.start_timestamp = start_timestamp;
    }

    public String getVideo_addr() {
        return video_addr;
    }

    public void setVideo_addr(String video_addr) {
        this.video_addr = video_addr;
    }

    public String getWatching_count() {
        return watching_count;
    }

    public void setWatching_count(String watching_count) {
        this.watching_count = watching_count;
    }

    public String getAnchor_name() {
        return anchor_name;
    }

    public void setAnchor_name(String anchor_name) {
        this.anchor_name = anchor_name;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getLive_desc() {
        return live_desc;
    }

    public void setLive_desc(String live_desc) {
        this.live_desc = live_desc;
    }

    public Bitmap getCoverBitmap() {
        return coverBitmap;
    }

    public void setCoverBitmap(Bitmap coverBitmap) {
        this.coverBitmap = coverBitmap;
    }

    public String getItemKey(){return getKey();}
    @Override
    public Bitmap getItemImage() {
        return coverBitmap;
    }
}
