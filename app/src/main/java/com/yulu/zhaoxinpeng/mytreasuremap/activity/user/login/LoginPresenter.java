package com.yulu.zhaoxinpeng.mytreasuremap.activity.user.login;

import android.os.AsyncTask;

/**
 * Created by Administrator on 2017/3/28.
 */
// 登录的业务类：帮View去做业务请求
public class LoginPresenter {
    /**
     * 业务类中间涉及到的视图怎么处理？
     * 1. 创建LoginActivity，不能采用这种方式
     * 2. 接口回调的方式
     * A 接口  里面有一个a()
     * AImpl是A接口的实现类  实现a()
     * 使用：A a = new Aimpl();
     * this.a = a;
     * a.a();
     * <p>
     * 接口创建好了，怎么初始化？
     * Activity实现视图接口
     */
    private LoginView mLoginView;

    public LoginPresenter(LoginView loginView) {
        this.mLoginView = loginView;
    }

    // 登录的业务
    public void Login() {
        /**
         * 1. 参数：请求的地址、上传的数据等类型，可以为空Void
         * 2. 进度：一般是Integer(int的包装类)，可以为空Void
         * 3. 结果：比如String、可以为空Void
         */
        new AsyncTask<Void, Integer, Void>() {
            // 请求之前的视图处理：比如进度条的显示
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mLoginView.showProgress();
            }

            // 后台线程：耗时的操作
            @Override
            protected Void doInBackground(Void... params) {
                 // 模拟：休眠3秒钟
                try {
                    Thread.sleep(3000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            // 拿到请求的数据，处理UI：进度条隐藏、跳转页面等
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                mLoginView.hideProgress();
                mLoginView.showToast("登录成功");
                mLoginView.navigateToHome();

            }
        }.execute();
    }

}
