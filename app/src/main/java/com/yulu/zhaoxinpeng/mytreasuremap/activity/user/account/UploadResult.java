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
    private String code;
    @SerializedName("urlcount")
    private int count;
    @SerializedName("smallImgUrl")
    private String url;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
