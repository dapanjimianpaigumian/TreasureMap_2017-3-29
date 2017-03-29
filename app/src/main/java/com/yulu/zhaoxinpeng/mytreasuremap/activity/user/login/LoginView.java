package com.yulu.zhaoxinpeng.mytreasuremap.activity.user.login;

/**
 * Created by Administrator on 2017/3/28.
 */
//登录视图的接口
public interface LoginView {
    void showProgress();// 显示进度
    void hideProgress();// 隐藏进度
    void showToast(String msg);// 显示吐司
    void navigateToHome();// 跳转页面
}
