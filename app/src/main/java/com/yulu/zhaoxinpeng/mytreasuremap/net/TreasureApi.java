package com.yulu.zhaoxinpeng.mytreasuremap.net;

/**
 * Created by Administrator on 2017/3/30.
 */

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

/**
 * Retrofit框架中的对应服务器接口
 */
public interface TreasureApi {

    @GET("http://www.baidu.com")
    @Headers({"content-type:html"})
    Call<ResponseBody> getData();
}
