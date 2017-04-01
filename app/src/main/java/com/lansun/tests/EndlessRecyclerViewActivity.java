package com.lansun.tests;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.lansun.tests.entity.AsyncModel;
import com.lansun.tests.entity.Live;
import com.lansun.tests.utils.ViewHelpers;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.TreeMap;

/**
 * Created by ly on 12/5/16.
 */

public class EndlessRecyclerViewActivity extends Activity{

    private RecyclerView rec;
    private RecyclerView.Adapter<ViewHolder> adapter;
    private float density;
    private TreeMap<String, Live> starLives = new TreeMap<>(new Comparator<String>() {
        @Override
        public int compare(String o1, String o2) {
            int n1 = Integer.valueOf(o1);
            int n2 = Integer.valueOf(o2);
            return n1>n2?1:((n1==n2)?0:-1);
        }
    });
    private LinearLayoutManager layoutManager;
    private LinkedList<Integer> loadingPositions = new LinkedList<>();
    private PtrClassicFrameLayout frame;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_endless_recyclerview);
        initDatas();
        findViews();
        initView();
    }

    private void findViews(){
        frame = (com.chanven.lib.cptr.PtrClassicFrameLayout)findViewById(R.id.test_list_view_frame);
        rec = (RecyclerView)findViewById(R.id.endless_rec);
    }


    private void initDatas(){
        loadPositions(0, 6);
    }

    private synchronized void loadPositions(int start, int num){
        //这里判断是否需要加载，之后进行加载
        int startPos = start;
        int size = num;
        boolean find = false;
        for(int i=start; i<start+num;i++){
            if(loadingPositions.contains(i)){
                continue;
            }
            if(!find){
                startPos = i;
                size = num - (i-start);
                find = true;
            }
            loadingPositions.add(i);
        }
        if(size>0){
            getItemsAndNotify(startPos, size);
        }
    }

    private synchronized void removedLoadingPositions(int start, int size){
        //下载完成后从正在加载列表移除
        for(int i=start; i<start+size; i++){
            loadingPositions.remove(Integer.valueOf(i));
        }
    }

    private void getItemsAndNotify(final int start, final int size) {
        Live.getStarLivesInRange(start, size, new AsyncModel.CallBack() {
            @Override
            public void onSuccess(HashMap datas) {
                starLives.putAll(datas);
                removedLoadingPositions(start, size);

                Live.getImagesForMap(datas, new AsyncModel.ImageObserver() {
                    @Override
                    public void onImageDownloadSuccess(Bitmap bm, String id) {
                        if(adapter!=null&&bm!=null){
                            starLives.get(id).setImage(bm);
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onImageDownloadFailed() {

                    }
                });
            }

            @Override
            public void onFail(AsyncModel.Error error) {

            }
        });
    }


    class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView iv;
        public ViewHolder(View itemView) {
            super(itemView);
            iv = (ImageView)itemView.findViewById(R.id.endless_rec_item_iv);
            System.out.println("test");
        }
    }

    private void initView(){

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        density = displayMetrics.density;

        adapter = new RecyclerView.Adapter<ViewHolder>(){
            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View v = View.inflate(EndlessRecyclerViewActivity.this, R.layout.endless_recyclerview_item, null);
                v.setMinimumHeight(ViewHelpers.dpToPixels(200, density));
                return new ViewHolder(v);
            }

            @Override
            public void onBindViewHolder(ViewHolder holder, int position) {
                    Live live = starLives.get(String.valueOf(position));
                    if(live.getItemImage()!=null){
                        holder.iv.setImageBitmap(live.getItemImage());
                    }
            }

            @Override
            public int getItemCount() {
                return starLives.size();
            }
        };

        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        rec.setAdapter(adapter);
        rec.setLayoutManager(layoutManager);
        rec.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int highestKey = Integer.valueOf(starLives.lastKey());
                int lastPos = layoutManager.findLastVisibleItemPosition();
                if(lastPos+4>highestKey){
                    loadPositions(highestKey+1, 4);
                };
            }
        });


        frame.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                HashMap<String, Live> newLives = new HashMap<String, Live>();
                for(int i=0;i<5;i++){
                    Live live = new Live();
                    live.setLive_desc("test" + i);
                    newLives.put("100"+i, live);
                }
                starLives.putAll(newLives);
                adapter.notifyItemRangeInserted(0, 5);

                frame.refreshComplete();

            }
        });

    }

}
