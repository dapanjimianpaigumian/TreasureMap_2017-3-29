package com.yulu.zhaoxinpeng.mytreasuremap.activity.user.login;

import android.os.Handler;
import android.os.Looper;

import com.yulu.zhaoxinpeng.mytreasuremap.activity.user.MultiUser;
import com.yulu.zhaoxinpeng.mytreasuremap.activity.user.User;
import com.yulu.zhaoxinpeng.mytreasuremap.net.NetClient;

import okhttp3.Call;
import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2017/3/28.
 */
// 登录的业务类：帮View去做业务请求

/**
 * 用户名  123456
 * 密码   123456
 */
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
    private Call mCall;

    private Handler mHandler = new Handler(Looper.getMainLooper());

    public LoginPresenter(LoginView loginView) {
        this.mLoginView = loginView;
    }

    // 登录的业务
    public void Login(User user) {

        //Call模型的取消
        //mCall.cancel();




        mLoginView.showProgress();

        NetClient.getInstance().getTreasureApi().login(user).enqueue(new Callback<LoginResult>() {
            @Override
            public void onResponse(retrofit2.Call<LoginResult> call, Response<LoginResult> response) {
                mLoginView.hideProgress();

                if (response.isSuccessful()) {

                    if (response.body()==null) {
                        mLoginView.showToast("未知错误！");
                        return;
                    }

                    if (response.body().getCode()==1) {
                        mLoginView.navigateToHome();

                        mLoginView.showToast(response.body().getMsg());
                    }

                }
            }

            @Override
            public void onFailure(retrofit2.Call<LoginResult> call, Throwable t) {
                mLoginView.hideProgress();
                mLoginView.showToast("请求失败！"+t.getMessage());
            }
        });

        /*//表单形式的方法
        NetClient.getInstance().getTreasureApi().getFormData("123456", "123456").enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, Response<ResponseBody> response) {
                mLoginView.showToast("请求成功  " + response.code());
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                mLoginView.showToast("请求失败！" + t.getMessage());
            }
        });*/

        //多部分形式的方法
        /*MultiUser user1 = new MultiUser("yt59856b15cf394e7b84a7d48447d16098",
                "xc62",
                "555",
                "123456",
                "0F8EC12223174657B2E842076D54C361");
        NetClient.getInstance().getTreasureApi().getMultData(user1).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, Response<ResponseBody> response) {
                mLoginView.showToast("请求成功  "+response.code());
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                mLoginView.showToast("请求失败！");
            }
        });*/



        /*RequestBody body=RequestBody.create(null,new Gson().toJson(user));
        NetClient.getInstance().getTreasureApi().login(body).enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                mLoginView.showToast("132");
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                mLoginView.showToast("200");
            }
        });*/




       /* NetClient.getInstance().getTreasureApi().getData().enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                mLoginView.showToast("请求成功  "+response.code());
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                mLoginView.showToast("请求失败！");
            }
        });*/


        //用 Retrofit 实现登录藏宝地图客户端
       /* NetClient.getInstance().login(user).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mLoginView.showToast("请求失败了！");
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (response.isSuccessful()) {

                            try {
                                String json = response.body().string();

                                UserResult userResult = new Gson().fromJson(json, UserResult.class);

                                if (userResult==null) {
                                    mLoginView.showToast("未知错误！");
                                    return;
                                }

                                if (userResult.getCode()==1) {
                                    mLoginView.navigateToHome();
                                }

                                mLoginView.showToast(userResult.getMsg());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        });*/


        //OkHttpClient 的Post用法
        /*mCall = NetClient.getInstance().postData();

        mCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //Log.e("okhttp","onfailure");
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {

                        mLoginView.showToast("啊哦，请求失败了！");

                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                //Log.e("okhttp","onresponse"+response.code());

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (response.isSuccessful()) {

                            ResponseBody body = response.body();

                            try {
                                String json = body.string();
                                // GSON解析
                                mLoginView.showToast("请求成功" + response.code());

                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                            // Log.e("响应成功","响应体数据"+response.body().string());
                        }
                    }
                });

            }
        });*/


        //同步方式
       /* try {
            Response execute = okHttpClient.newCall(request).execute();

        } catch (Exception e) {
            e.printStackTrace();
        }*/

        //异步方式
       /* okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("okhttp","onfailure");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e("okhttp","onresponse"+response.code());

                if (response.isSuccessful()) {
                    Log.e("响应成功","响应体数据"+response.body().string());
                }
            }
        });*/
    }

}
