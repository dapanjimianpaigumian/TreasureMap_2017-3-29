package com.yulu.zhaoxinpeng.mytreasuremap.treasure.detail;

import java.util.List;

/**
 * Created by Administrator on 2017/4/6.
 * 宝藏详情的视图接口
 */

public interface TreasureDetailView {
    void showMessage(String msg);//弹吐司
    void setDetailData(List<TreasureDetailResult> list);//设置数据
}
