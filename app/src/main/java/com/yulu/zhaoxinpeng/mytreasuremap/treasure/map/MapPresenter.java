package com.yulu.zhaoxinpeng.mytreasuremap.treasure.map;

import com.yulu.zhaoxinpeng.mytreasuremap.net.NetClient;
import com.yulu.zhaoxinpeng.mytreasuremap.treasure.Area;
import com.yulu.zhaoxinpeng.mytreasuremap.treasure.Treasure;

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

    public MapPresenter(MapMvpView mMapMvpView) {
        this.mMapMvpView = mMapMvpView;
    }

    //获取宝藏数据
    public void getTreasure(Area area){
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
