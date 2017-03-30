package com.yulu.zhaoxinpeng.mytreasuremap.activity.user;

/**
 * Created by Administrator on 2017/3/30.
 */
/** 用户类
 * GsonFormat创建实体类：设置里面下载插件
 */
public class User {

    /**
     * UserName : qjd
     * Password : 654321
     */

    private String UserName;
    private String Password;



    public User(String userName, String password) {
        this.UserName = userName;
        this.Password = password;
    }
}
