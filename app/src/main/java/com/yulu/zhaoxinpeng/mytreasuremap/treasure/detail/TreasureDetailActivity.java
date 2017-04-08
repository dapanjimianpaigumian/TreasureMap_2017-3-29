package com.yulu.zhaoxinpeng.mytreasuremap.treasure.detail;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.navi.BaiduMapNavigation;
import com.baidu.mapapi.navi.NaviParaOption;
import com.baidu.mapapi.utils.OpenClientUtil;
import com.yulu.zhaoxinpeng.mytreasuremap.R;
import com.yulu.zhaoxinpeng.mytreasuremap.commons.ActivityUtils;
import com.yulu.zhaoxinpeng.mytreasuremap.custom.TreasureView;
import com.yulu.zhaoxinpeng.mytreasuremap.treasure.Treasure;
import com.yulu.zhaoxinpeng.mytreasuremap.treasure.map.MapFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

// 宝藏的详情页
public class TreasureDetailActivity extends AppCompatActivity implements TreasureDetailView {

    private static final String KEY_TREASURE = "key_treasure";
    @BindView(R.id.iv_navigation)
    ImageView mNavigation;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.frameLayout)
    FrameLayout mFrameLayout;
    @BindView(R.id.detail_treasure)
    TreasureView mTreasureView;
    @BindView(R.id.tv_detail_description)
    TextView mTvDetail;
    @BindView(R.id.tv_detail_size)
    TextView mTvDetailSize;
    private Unbinder unbinder;
    private Treasure mTreasure;
    private ActivityUtils mActivityUtils;
    private TreasureDetailPresenter mDetailPresenter;

    /**
     * 对外提供一个方法，跳转到本页面
     * 规范一下传递的数据：需要什么参数就必须要传入
     */
    public static void open(Context context, Treasure treasure) {
        Intent intent = new Intent(context, TreasureDetailActivity.class);
        intent.putExtra(KEY_TREASURE, treasure);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treasure_detail);
        unbinder = ButterKnife.bind(this);

        mActivityUtils = new ActivityUtils(this);

        mDetailPresenter = new TreasureDetailPresenter(this);

        // 取到传递过来的数据
        mTreasure = (Treasure) getIntent().getSerializableExtra(KEY_TREASURE);

        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            //设置标题和返回箭头
            getSupportActionBar().setTitle(mTreasure.getTitle());
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        ininMapView();

        //宝藏信息卡片的展示
        mTreasureView.bindTreasure(mTreasure);

        //网络获取宝藏的详情数据
        TreasureDetail treasureDetail = new TreasureDetail(mTreasure.getId());
        mDetailPresenter.getTreasureDetail(treasureDetail);
    }

    // 地图的展示
    private void ininMapView() {

        //宝藏经纬度
        LatLng mLatLng = new LatLng(mTreasure.getLatitude(), mTreasure.getLongitude());

        MapStatus mMapStatus = new MapStatus.Builder()
                .target(mLatLng)
                .zoom(18)
                .rotate(0)
                .overlook(-20)
                .build();

        // 地图只是用于展示，没有任何操作
        BaiduMapOptions mBaiduMapOptions = new BaiduMapOptions()
                .mapStatus(mMapStatus)
                .compassEnabled(false)
                .scrollGesturesEnabled(false)
                .scaleControlEnabled(false)
                .zoomControlsEnabled(false)
                .zoomGesturesEnabled(false)
                .rotateGesturesEnabled(false);

        // 创建的地图控件
        MapView mMapView = new MapView(this, mBaiduMapOptions);

        //添加到布局中
        mFrameLayout.addView(mMapView);

        //拿到地图的操作类
        BaiduMap mMap = mMapView.getMap();

        BitmapDescriptor mBitmapDescriptor = BitmapDescriptorFactory.fromResource(R.mipmap.treasure_expanded);

        MarkerOptions mMarkerOptions = new MarkerOptions()
                .position(mLatLng)
                .icon(mBitmapDescriptor)
                .anchor(0.5f, 0.5f);

        // 添加一个覆盖物
        mMap.addOverlay(mMarkerOptions);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            // 处理返回箭头的事件
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    //导航按钮的点击事件
    @OnClick(R.id.iv_navigation)
    public void onViewClicked(View view) {
        // 展示出来一个PopupMenu
        /**
         * 1. 创建一个弹出式菜单
         * 2. 菜单项的填充(布局)
         * 3. 设置菜单项的点击监听
         * 4. 显示
         */
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.inflate(R.menu.menu_navigation);
        popupMenu.setOnMenuItemClickListener(mMenuItemClickListener);
        popupMenu.show();
    }

    // 菜单项的点击监听
    private PopupMenu.OnMenuItemClickListener mMenuItemClickListener = new PopupMenu.OnMenuItemClickListener() {

        // 点击菜单项会触发：具体根据item的id来判断
        @Override
        public boolean onMenuItemClick(MenuItem item) {

            // 不管进行骑行还是步行，都需要起点和终点：坐标和地址
            // 起点：我们定位的位置和地址
            LatLng start = MapFragment.getMyLocation();
            String startAddr = MapFragment.getMyAddr();

            // 终点：宝藏的位置和地址
            LatLng end = new LatLng(mTreasure.getLatitude(), mTreasure.getLongitude());
            String endAddr = mTreasure.getLocation();

            switch (item.getItemId()) {
                case R.id.walking_navi:
                    //开始步行导航
                    startWalkingNavi(start, startAddr, end, endAddr);
                    break;
                case R.id.biking_navi:
                    //开始骑行导航
                    startBikingNavi(start, startAddr, end, endAddr);
            }
            return false;
        }
    };

    //骑行导航方法
    private void startBikingNavi(LatLng start, String startAddr, LatLng end, String endAddr) {

        //起点、终点的设置
        NaviParaOption option = new NaviParaOption()
                .startPoint(start)
                .startName(startAddr)
                .endPoint(end)
                .endName(endAddr);

        //打开骑行导航
        boolean bikeNavi = BaiduMapNavigation.openBaiduMapWalkNavi(option, this);

        //无法成功开启百度地图，那么开启网页导航
        if (bikeNavi) {
            //startWebNavi(start,startAddr,end,endAddr);

            showDialog();
        }
    }

    // 显示一个对话框提示：没有安装，是否去下载
    private void showDialog() {
        new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("您未安装百度地图客户端或版本过低，要不要安装？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 打开最新版的客户端下载页面
                        OpenClientUtil.getLatestBaiduMapApp(TreasureDetailActivity.this);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create().show();
    }

    //步行导航方法
    private void startWalkingNavi(LatLng start, String startAddr, LatLng end, String endAddr) {
        //起点、终点的设置
        NaviParaOption option = new NaviParaOption()
                .startPoint(start)
                .startName(startAddr)
                .endPoint(end)
                .endName(endAddr);

        //开启步行导航
        boolean walkNavi = BaiduMapNavigation.openBaiduMapWalkNavi(option, this);

        //无法成功开启百度地图，那么开启网页导航
        if (walkNavi) {
            startWebNavi(start, startAddr, end, endAddr);
        }
    }

    //打开网页进行导航
    private void startWebNavi(LatLng start, String startAddr, LatLng end, String endAddr) {
        //起点和终点的设置
        NaviParaOption option = new NaviParaOption()
                .startPoint(start)
                .startName(startAddr)
                .endPoint(end)
                .endName(endAddr);

        BaiduMapNavigation.openBaiduMapWalkNavi(option, this);
    }

    //-----------------------------------详情的视图实现---------------------------------------
    @Override
    public void showMessage(String msg) {
        mActivityUtils.showToast(msg);
    }

    @Override
    public void setDetailData(List<TreasureDetailResult> list) {

        //展示请求到的数据
        if (list.size() >= 1) {
            TreasureDetailResult result = list.get(0);
            mTvDetail.setText(result.description);

            if (result.size==0) {
                mTvDetailSize.setText("小型");
            }else if(result.size==1){
                mTvDetailSize.setText("中等");
            }else if(result.size==2){
                mTvDetailSize.setText("大型");
            }
            return;
        }
        mTvDetail.setText("当前宝藏没有详细信息");
    }
}

