package com.yulu.zhaoxinpeng.mytreasuremap.treasure.detail;

import com.yulu.zhaoxinpeng.mytreasuremap.net.NetClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2017/4/6.
 * 宝藏详情的业务类
 */

public class TreasureDetailPresenter {

    private TreasureDetailView mTreasureDetailView;

    public TreasureDetailPresenter(TreasureDetailView mTreasureDetailView) {
        this.mTreasureDetailView = mTreasureDetailView;
    }

    public void getTreasureDetail(TreasureDetail treasureDetail){
        Call<List<TreasureDetailResult>> detailCall = NetClient.getInstance().getTreasureApi().getTreasureDetail(treasureDetail);
        detailCall.enqueue(mCallback);
    }

    private Callback<List<TreasureDetailResult>> mCallback=new Callback<List<TreasureDetailResult>>() {
        @Override
        public void onResponse(Call<List<TreasureDetailResult>> call, Response<List<TreasureDetailResult>> response) {
            if (response.isSuccessful()) {
                List<TreasureDetailResult> resultList = response.body();
                if (resultList==null) {
                    mTreasureDetailView.showMessage("未获取到宝藏详情数据");
                }
                //获取到数据后，将数据设置给视图
                mTreasureDetailView.setDetailData(resultList);
            }
        }

        @Override
        public void onFailure(Call<List<TreasureDetailResult>> call, Throwable t) {
            mTreasureDetailView.showMessage("请求失败"+t.getMessage());
        }
    };
}
