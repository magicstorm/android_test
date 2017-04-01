package com.lansun.tests;

import android.app.Activity;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by ly on 12/2/16.
 */

public class AdvancedRecyclerViewTestActivity extends Activity{

    private RecyclerView rec;
    private RecyclerView.Adapter<GridViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_recyclerview);
        findViews();
        initViews();
    }

    private void findViews(){
        rec = (RecyclerView)findViewById(R.id.adv_rec);
    }

    class GridViewHolder extends RecyclerView.ViewHolder{
        public ImageView itemIv;
        public TextView itemTv;
        public GridViewHolder(View itemView) {
            super(itemView);
            itemIv = (ImageView)itemView.findViewById(R.id.item_iv);
            itemTv = (TextView)itemView.findViewById(R.id.item_tv);
        }
    }

    private void initViews(){
        adapter = new RecyclerView.Adapter<GridViewHolder>(){
            @Override
            public GridViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View v = View.inflate(AdvancedRecyclerViewTestActivity.this, R.layout.adv_recycler_view_item, null);
                GridViewHolder vh = new GridViewHolder(v);
                return vh;
            }

            @Override
            public void onBindViewHolder(GridViewHolder holder, int position) {
                holder.itemTv.setText(String.valueOf(position));
            }

            @Override
            public int getItemCount() {
                return 10;
            }
        };

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this,4);
        rec.setLayoutManager(layoutManager);
        rec.setAdapter(adapter);
    }

}
