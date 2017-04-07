package com.yulu.zhaoxinpeng.mytreasuremap.treasure.hide;

/**
 * Created by Administrator on 2017/4/7.
 * 埋藏宝藏的视图接口
 */

public interface HideTreasureView {

    void showToast(String msg);
    void navigateToHome();
    void showProgress();
    void hideProgress();
}
