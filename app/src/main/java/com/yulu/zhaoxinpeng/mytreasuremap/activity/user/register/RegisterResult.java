package com.yulu.zhaoxinpeng.mytreasuremap.activity.user.register;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/3/31.
 * 注册的响应结果实体
 */

public class RegisterResult {

    @SerializedName("tokenid")
    private int tokenId;

    @SerializedName("errcode")
    private int code;

    @SerializedName("errmsg")
    private String msg;

    public int getTokenId() {
        return tokenId;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
