package com.yulu.zhaoxinpeng.mytreasuremap.treasure.list;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.yulu.zhaoxinpeng.mytreasuremap.custom.TreasureView;
import com.yulu.zhaoxinpeng.mytreasuremap.treasure.Treasure;
import com.yulu.zhaoxinpeng.mytreasuremap.treasure.detail.TreasureDetailActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/6.
 * RecyclerView的适配器
 */
public class TreasureListAdapter extends RecyclerView.Adapter<TreasureListAdapter.MyViewHolder>{

    private List<Treasure> data=new ArrayList<>();

    // 添加数据的方法
    public void addItemData(List<Treasure> list){
        data.clear();
        data.addAll(list);
        notifyDataSetChanged();
    }

    // 创建ViewHolder
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TreasureView treasureView = new TreasureView(parent.getContext());
        return new MyViewHolder(treasureView);
    }

    // 数据的绑定
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Treasure treasure = data.get(position);
        holder.mTreasureView.bindTreasure(treasure);

        // 点击事件
        holder.mTreasureView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 点击卡片的时候直接跳转到宝藏详情页
                TreasureDetailActivity.open(v.getContext(),treasure);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        private TreasureView mTreasureView;

        public MyViewHolder(TreasureView itemView) {
            super(itemView);
            this.mTreasureView=itemView;
        }
    }

}
