package com.yulu.zhaoxinpeng.mytreasuremap.treasure.map;

import com.yulu.zhaoxinpeng.mytreasuremap.commons.LogUtils;
import com.yulu.zhaoxinpeng.mytreasuremap.net.NetClient;
import com.yulu.zhaoxinpeng.mytreasuremap.treasure.Area;
import com.yulu.zhaoxinpeng.mytreasuremap.treasure.Treasure;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2017/4/5.
 */

public class MapPresenter {
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
                    return;
                }

                LogUtils.e("请求数据"+treasureList.size());
            }
        }

        @Override
        public void onFailure(Call<List<Treasure>> call, Throwable t) {
            LogUtils.e("请求失败"+t.getMessage());
        }
    };
}
