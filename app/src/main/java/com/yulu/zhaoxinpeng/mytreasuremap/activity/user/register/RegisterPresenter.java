package com.yulu.zhaoxinpeng.mytreasuremap.activity.user.register;

import android.os.AsyncTask;

import com.yulu.zhaoxinpeng.mytreasuremap.activity.user.User;
import com.yulu.zhaoxinpeng.mytreasuremap.activity.user.UserPrefs;
import com.yulu.zhaoxinpeng.mytreasuremap.net.NetClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2017/3/28.
 */

public class RegisterPresenter {
    private RegisterView mRegisterView;

    public RegisterPresenter(RegisterView registerView){
        this.mRegisterView=registerView;
    }

    public void register(User user){
        // 显示进度条
        mRegisterView.showProgress();

        NetClient.getInstance().getTreasureApi().register(user).enqueue(new Callback<RegisterResult>() {

            // 请求成功
            @Override
            public void onResponse(Call<RegisterResult> call, Response<RegisterResult> response) {
                mRegisterView.hideProgress();// 隐藏进度
                // 响应成功
                if (response.isSuccessful()){
                    RegisterResult registerResult = response.body();

                    // 响应体是不是为null
                    if (registerResult==null){
                        mRegisterView.showToast("未知的错误");
                        return;
                    }
                    if(registerResult.getCode()==1){

                        // 真正的注册成功了
                        // 保存Tokenid
                        UserPrefs.getInstance().setTokenid(registerResult.getTokenId());
                        mRegisterView.navigateToHome();
                    }
                    mRegisterView.showToast(registerResult.getMsg());
                }
            }

            // 请求失败
            @Override
            public void onFailure(Call<RegisterResult> call, Throwable t) {
                mRegisterView.hideProgress();// 隐藏进度
                mRegisterView.showToast("请求失败："+t.getMessage());
            }
        });
    }
}
