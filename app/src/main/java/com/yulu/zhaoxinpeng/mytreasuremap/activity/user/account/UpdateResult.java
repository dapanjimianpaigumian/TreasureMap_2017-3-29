package com.yulu.zhaoxinpeng.mytreasuremap.activity.user.account;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/4/8.
 */

public class UpdateResult {

    /**
     * errcode : 1
     * errmsg : 修改成功!
     */

    @SerializedName("errcode")
    private int code;

    @SerializedName("errmsg")
    private String msg;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
