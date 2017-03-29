package com.yulu.zhaoxinpeng.mytreasuremap.activity.user.register;

import android.os.AsyncTask;

/**
 * Created by Administrator on 2017/3/28.
 */

public class RegisterPresenter {
    private RegisterView mRegisterView;

    public RegisterPresenter(RegisterView registerView){
        this.mRegisterView=registerView;
    }

    public void Register(){
        new AsyncTask<Void, Integer, Void>() {
            // 请求之前的视图处理：比如进度条的显示
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mRegisterView.showProgress();
            }

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Thread.sleep(3000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                mRegisterView.hideProgress();
                mRegisterView.showToast("注册成功");
                mRegisterView.navigateToHome();
            }
        }.execute();
    }
}
