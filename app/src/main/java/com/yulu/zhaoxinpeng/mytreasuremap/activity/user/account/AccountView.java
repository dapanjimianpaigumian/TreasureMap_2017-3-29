package com.yulu.zhaoxinpeng.mytreasuremap.activity.user.account;

/**
 * Created by Administrator on 2017/4/10.
 * 头像的视图接口
 */

public interface AccountView {

    void showProgress();

    void hideProgress();

    void showToast(String msg);

    void updatePhoto(String photoUrl);
}
