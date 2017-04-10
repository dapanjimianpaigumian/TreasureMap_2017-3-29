package com.yulu.zhaoxinpeng.mytreasuremap.net;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.yulu.zhaoxinpeng.mytreasuremap.activity.user.User;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2017/3/29.
 */
// 网络的客户端类
//封装 OkHttpClient
public class NetClient {

    private static NetClient mNetClient;
    private OkHttpClient mOkHttpClient;
    public static String BaseUrl = "http://admin.syfeicuiedu.com";
    private final Gson mGson;
    private final Retrofit mRetrofit;
    private TreasureApi mTreasureApi;


    //私有的构造方法
    public NetClient() {

        mGson  = new GsonBuilder()
                .setLenient()// 设置GSON的非严格模式setLenient()
                .create();

        //创建一个日志拦截器
        HttpLoggingInterceptor mInterceptor = new HttpLoggingInterceptor();

        //设置打印日志的级别 （Body 意为全部）
        mInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        //okHttpClient 的单例化
        mOkHttpClient = new OkHttpClient.Builder()
                .addInterceptor(mInterceptor)
                .build();

        // Retrofit的创建
        mRetrofit = new Retrofit.Builder()
                .baseUrl(BaseUrl)// 必须要加的BASEURL
                //将 OkHttpClient 的属性附加给 Retrofit
                //能这样做的原因可能在于：Retrofit 是对 OkHttpClient 的再封装
                .client(mOkHttpClient)
                .addConverterFactory(GsonConverterFactory.create(mGson))//添加格式转换工厂
                .build();
    }

    public static NetClient getInstance() {
        if (mNetClient == null) {
            mNetClient = new NetClient();
        }
        return mNetClient;
    }

    // 将TreasureApi怎么对外提供处理：提供一个方法getTreasureApi()
    //通过此方法可以调用 TreasureApi 接口中的接口
    public TreasureApi getTreasureApi(){
        if (mTreasureApi==null) {
            mTreasureApi = mRetrofit.create(TreasureApi.class);

        }
        return mTreasureApi;
    }

    // 将每一个请求都单独的放置到一个方法里面
    public Call getData() {
        // 构建请求
        final Request request = new Request.Builder()
                .get()// 请求的方式
                .url("http://www.baidu.com")// 请求的地址
                .addHeader("content-type", "html")// 添加请求头信息
                .addHeader("context-length", "1024")
                // Get请求不需要添加请求体
                .build();
        // 根据请求进行建模Call
        return mOkHttpClient.newCall(request);
    }

    //Post 形式的请求构建
    public Call postData() {

        /**
         * OkHttpClient 的 Post请求方式
         * 1. 当需要上传的数据是键值对的形式的时候
         *  username = “”；
         *  password = “”；
         *  json = “{username=“”，password = “”}”
         *  一般以表单的形式进行提交
         *
         * 2. 当上传的数据是多个部分的时候
         *  多部分提交
         */
        // 表单形式请求体的构建
//        RequestBody formBody = new FormBody.Builder()
//                .add("username","123456")
//                .add("password","123456")
//                .build();
//
//        // 多部分请求体的构建
//        RequestBody multBody = new MultipartBody.Builder()
//                .addFormDataPart("photo","abc.png",RequestBody.create(null,"abc.png"))
//                .addFormDataPart("name","123456")
//                .build();

        // 需要上传的请求体:字符串、文件、数组等
        RequestBody requestbody = RequestBody.create(null, "{\n" +
                "\"UserName\":\"qjd\",\n" +
                "\"Password\":\"654321\"\n" +
                "}\n");
        Request request = new Request.Builder()
                .post(requestbody)
                .url(BaseUrl + "/Handler/UserHandler.ashx?action=login")
                .build();

        return mOkHttpClient.newCall(request);
    }

    //Retrofit的调用，实现登录
    public Call login(User user){

        RequestBody requestBody=RequestBody.create(null,mGson.toJson(user));

        Request request = new Request.Builder()
                .post(requestBody)
                .url(BaseUrl + "/Handler/UserHandler.ashx?action=login")
                .build();

        return mOkHttpClient.newCall(request);
    }
}
