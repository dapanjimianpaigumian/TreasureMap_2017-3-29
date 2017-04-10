package com.yulu.zhaoxinpeng.mytreasuremap.activity.user.account;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/4/8.
 */

public class UploadResult {

    /**
     * errcode : 文件系统上传成功！
     * urlcount : 1
     * imgUrl : /UpLoad/HeadPic/f683f88dc9d14b648ad5fcba6c6bc840_0.png
     * smallImgUrl : /UpLoad/HeadPic/f683f88dc9d14b648ad5fcba6c6bc840_0_1.png
     */

    @SerializedName("errcode")
    private String msg;

    @SerializedName("urlcount")
    private int count;

    @SerializedName("smallImgUrl")
    private String url;

    public String getMsg() {
        return msg;
    }

    public int getCount() {
        return count;
    }

    public String getUrl() {
        return url;
    }
}
