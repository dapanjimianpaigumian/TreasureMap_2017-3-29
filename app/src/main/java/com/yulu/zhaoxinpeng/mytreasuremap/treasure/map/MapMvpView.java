package com.yulu.zhaoxinpeng.mytreasuremap.treasure.map;

import com.yulu.zhaoxinpeng.mytreasuremap.treasure.Treasure;

import java.util.List;

/**
 * Created by Administrator on 2017/4/5.
 * 获取数据的视图接口
 */

public interface MapMvpView {
    void showMessage(String msg);//显示信息
    void setTreasureData(List<Treasure> list);//设置数据
}
