package com.yulu.zhaoxinpeng.mytreasuremap.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.yulu.zhaoxinpeng.mytreasuremap.R;
import com.yulu.zhaoxinpeng.mytreasuremap.treasure.Treasure;
import com.yulu.zhaoxinpeng.mytreasuremap.treasure.map.MapFragment;

import java.text.DecimalFormat;

/**
 * Created by Administrator on 2017/4/5.
 * 宝藏信息的卡片
 */

public class TreasureView extends RelativeLayout {

    private View view;
    private TextView mTvTreasureLocation;
    private TextView mTvDistance;
    private TextView mTvTreasureTitle;

    // 代码
    public TreasureView(Context context) {
        super(context);
        init();
    }

    // 布局和代码中使用
    public TreasureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    // 布局代码使用，设置style样式
    public TreasureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    // 视图的填充
    private void init() {
        // 视图的初始化
        view = LayoutInflater.from(getContext().getApplicationContext()).inflate(R.layout.view_treasure, this, true);
        mTvTreasureTitle = (TextView) view.findViewById(R.id.tv_treasureTitle);
        mTvDistance = (TextView) view.findViewById(R.id.tv_distance);
        mTvTreasureLocation = (TextView) view.findViewById(R.id.tv_treasureLocation);

    }

    // 可以对外提供一个方法：根据宝藏信息，填充布局中的数据
    public void bindTreasure(Treasure treasure) {

        // 标题和地址
        mTvTreasureTitle.setText(treasure.getTitle());
        mTvTreasureLocation.setText(treasure.getLocation());

        // 距离：宝藏距离我们有多远

        double distance = 0.00d;// 距离

        // 宝藏的位置
        LatLng latLng = new LatLng(treasure.getLatitude(), treasure.getLongitude());

        // 定位的位置
        LatLng myLocation = MapFragment.getMyLocation();

        if (myLocation == null) {
            distance = 0.00d;
        }

        // 利用百度地图提供的计算工具
        distance = DistanceUtil.getDistance(latLng, myLocation);

        // 规范下一显示的样式
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        String text = decimalFormat.format(distance / 1000) + "km";
        mTvDistance.setText(text);
    }

}
