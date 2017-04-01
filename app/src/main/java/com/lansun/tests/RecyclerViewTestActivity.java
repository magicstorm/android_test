package com.lansun.tests;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.*;
import android.util.DisplayMetrics;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lansun.tests.utils.ToastSingle;
import com.lansun.tests.utils.ViewHelpers;
import com.lansun.tests.widgets.OuterFrameRecyclerView;

import java.util.ArrayList;

/**
 * Created by ly on 11/28/16.
 */

public class RecyclerViewTestActivity extends Activity {

    private OuterFrameRecyclerView recView;
    private RecyclerView.Adapter<ViewHolder> adapter;
    ArrayList<String> listText = new ArrayList<>();
    private DisplayMetrics dm;
    private float density;
    private static final int VIEW_TYPE_REC = 1;
    private static final int VIEW_TYPE_NORMAL = 0;

    private static final int LIST_POSITION_SUBREC1 = 5;
    private RecyclerView rec1;
    private RecyclerView.Adapter<ViewHolder1> adapter1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview_test);
        initDatas();
        findViews();
        initViews();
    }

    private void initDatas(){
        for(int i=0;i<100;i++){
            listText.add("fuck" + String.valueOf(i));
        }
        dm = getResources().getDisplayMetrics();
        density = dm.density;
    }

    private void findViews(){
        recView = (OuterFrameRecyclerView)findViewById(R.id.rec_list);
        rec1 = (RecyclerView)View.inflate(this, R.layout.rec1, null);
    }

    class ViewHolder1 extends RecyclerView.ViewHolder{
        public TextView tv1;
        public TextView tv2;
        public ViewHolder1(View itemView) {
            super(itemView);

            tv1 = (TextView)itemView.findViewById(R.id.fuck1);
            tv2 = (TextView)itemView.findViewById(R.id.fuck2);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }


    class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tv1;
        public TextView tv2;
        public ViewHolder(View itemView) {
            super(itemView);


            if(!(itemView instanceof RecyclerView)){
                tv1 = (TextView)itemView.findViewById(R.id.fuck1);
                tv2 = (TextView)itemView.findViewById(R.id.fuck2);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }

        }
    }



    private void initViews(){
        /**
         * Rec1
         */
        adapter1 = new RecyclerView.Adapter<ViewHolder1>(){

            @Override
            public ViewHolder1 onCreateViewHolder(ViewGroup parent, int viewType) {

                View v = View.inflate(RecyclerViewTestActivity.this, R.layout.rec_cell, null);
                v.setMinimumHeight(100);
                return new ViewHolder1(v);
            }

            @Override
            public void onBindViewHolder(ViewHolder1 holder, int position) {
                holder.tv1.setText(listText.get(position));
            }

            @Override
            public int getItemCount() {
                return listText.size();
            }
        };


        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rec1.setAdapter(adapter1);
        rec1.setLayoutManager(layoutManager1);

        /**
         * Main Rec
         */

        adapter = new RecyclerView.Adapter<ViewHolder>(){

            @Override
            public int getItemViewType(int position) {
                /**
                 * 这里根据位置返回该位置view的类型
                 */
                switch (position){
                    case LIST_POSITION_SUBREC1:
                        return VIEW_TYPE_REC;
                    default:
                        return VIEW_TYPE_NORMAL;
                }
            }

            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View v;
                switch (viewType){
                    case VIEW_TYPE_REC:
                        v = rec1;
                        break;
                    default:
                        v = View.inflate(RecyclerViewTestActivity.this, R.layout.rec_cell, null);
                        v.setMinimumHeight(100);
                        break;
                }

//                ((ViewGroup)v).addView(genSplitLine());
                return new ViewHolder(v);
            }

            @Override
            public void onBindViewHolder(ViewHolder holder, int position) {
                if(position==LIST_POSITION_SUBREC1)return;
                if(position<LIST_POSITION_SUBREC1){
                    holder.tv1.setText(listText.get(position));
                }
                else{
                    holder.tv1.setText(listText.get(position-1));
                }
            }

            @Override
            public int getItemCount() {
                return listText.size()+1;
            }
        };



        /**
         * divider
         */
        android.support.v7.widget.DividerItemDecoration did = new android.support.v7.widget.DividerItemDecoration(recView.getContext(), LinearLayoutManager.VERTICAL);
        did.setDrawable(ContextCompat.getDrawable(RecyclerViewTestActivity.this, R.drawable.red_dvd));
        recView.addItemDecoration(did);


        recView.setHasFixedSize(true);
        recView.setItemAnimator(new DefaultItemAnimator());

        /**
         * essential
         */
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recView.setAdapter(adapter);
        recView.setLayoutManager(layoutManager);


//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                /**
//                 * remove view
//                 */
//                listText.remove(3);
//                adapter.notifyItemRemoved(3);
//
//                /**
//                 * add view
//                 */
//
////                listText.add(2, "新加的");
////                adapter.notifyItemInserted(2);
//
//                ToastSingle.showToast(RecyclerViewTestActivity.this, "移除", Toast.LENGTH_SHORT);
//            }
//        }, 10000);



    }

    private View genSplitLine() {
        View splitLine = new View(RecyclerViewTestActivity.this);
        splitLine.setBackgroundColor(Color.parseColor("#e6e6e6"));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewHelpers.dpToPixels(1, density));
        splitLine.setLayoutParams(params);
        return splitLine;
    }
}
