package com.yulu.zhaoxinpeng.mytreasuremap.activity.user.register;

/**
 * Created by Administrator on 2017/3/28.
 */

public interface RegisterView {
    void showProgress();
    void hideProgress();
    void showToast(String msg);
    void navigateToHome();
}
