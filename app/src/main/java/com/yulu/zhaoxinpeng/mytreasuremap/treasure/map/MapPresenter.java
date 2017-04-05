package com.yulu.zhaoxinpeng.mytreasuremap.treasure.map;

import com.yulu.zhaoxinpeng.mytreasuremap.net.NetClient;
import com.yulu.zhaoxinpeng.mytreasuremap.treasure.Area;
import com.yulu.zhaoxinpeng.mytreasuremap.treasure.Treasure;
import com.yulu.zhaoxinpeng.mytreasuremap.treasure.TreasureRepo;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2017/4/5.
 * 获取宝藏数据的业务类
 */

public class MapPresenter {

    private MapMvpView mMapMvpView;
    private Area mArea;

    public MapPresenter(MapMvpView mMapMvpView) {
        this.mMapMvpView = mMapMvpView;
    }

    //获取宝藏数据
    public void getTreasure(Area area){
        // 当前区域已经缓存过，就不再去请求
        if (TreasureRepo.getInstance().isCached(area)){
            return;
        }
        mArea = area;
        Call<List<Treasure>> listCall = NetClient.getInstance().getTreasureApi().getTreasureInArea(area);
        listCall.enqueue(mListCallback);
    }
    private Callback<List<Treasure>> mListCallback=new Callback<List<Treasure>>() {
        @Override
        public void onResponse(Call<List<Treasure>> call, Response<List<Treasure>> response) {
            if (response.isSuccessful()) {
                List<Treasure> treasureList = response.body();

                if (treasureList==null) {

                    mMapMvpView.showMessage("未获取到宝藏数据");
                    return;
                }

                // 做一个缓存:缓存请求的数据和区域
                TreasureRepo.getInstance().addTreasure(treasureList);
                TreasureRepo.getInstance().cache(mArea);

                //拿到数据：给MapFragment设置上，然后在地图上显示
                mMapMvpView.setTreasureData(treasureList);
            }
        }

        @Override
        public void onFailure(Call<List<Treasure>> call, Throwable t) {
            mMapMvpView.showMessage("请求失败哦"+t.getMessage());
        }
    };
}
