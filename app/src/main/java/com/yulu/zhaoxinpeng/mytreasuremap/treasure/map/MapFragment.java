package com.yulu.zhaoxinpeng.mytreasuremap.treasure.map;

import android.Manifest;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.yulu.zhaoxinpeng.mytreasuremap.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2017/3/31.
 */
// 地图和宝藏的展示
public class MapFragment extends Fragment {

    private static final int LOCATION_REQUEST_CODE = 200;
    @BindView(R.id.center)
    Space center;
    @BindView(R.id.iv_located)
    ImageView ivLocated;
    @BindView(R.id.btn_HideHere)
    Button btnHideHere;
    @BindView(R.id.centerLayout)
    RelativeLayout centerLayout;
    @BindView(R.id.iv_scaleUp)
    ImageView ivScaleUp;
    @BindView(R.id.iv_scaleDown)
    ImageView ivScaleDown;
    @BindView(R.id.tv_located)
    TextView tvLocated;
    @BindView(R.id.tv_satellite)
    TextView mtvSatellite;
    @BindView(R.id.tv_compass)
    TextView tvCompass;
    @BindView(R.id.ll_locationBar)
    LinearLayout llLocationBar;
    @BindView(R.id.tv_currentLocation)
    TextView tvCurrentLocation;
    @BindView(R.id.iv_toTreasureInfo)
    ImageView ivToTreasureInfo;
    @BindView(R.id.et_treasureTitle)
    EditText etTreasureTitle;
    @BindView(R.id.cardView)
    CardView cardView;
    @BindView(R.id.layout_bottom)
    FrameLayout layoutBottom;
    @BindView(R.id.map_frame)
    FrameLayout mMapFrame;
    Unbinder unbinder;
    private MapView mMapView;
    private BaiduMap mBaidumap;
    private LocationClient mLocationClient;
    private LatLng mCurrentLocation;
    private String mCurrentAddr;
    private MapStatusUpdate mStatusUpdate;
    private Boolean isFirst=true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container);
        // 检测权限有没有授权成功
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED) {

            //如果没有成功获取，就需要向用户申请
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},LOCATION_REQUEST_CODE);
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        unbinder = ButterKnife.bind(this, view);

        // 初始化百度地图
        initMapView();

        // 初始化定位相关
        initLocation();
    }

    // 初始化定位相关
    private void initLocation() {

        //前置：激活定位图层
        mBaidumap.setMyLocationEnabled(true);

        // 1. 第一步，初始化LocationClient类
        mLocationClient = new LocationClient(getActivity().getApplication().getApplicationContext());

        // 2. 第二步，配置定位SDK参数
        LocationClientOption mLocationClientOption = new LocationClientOption();
        mLocationClientOption.setOpenGps(true);// 打开GPS
        mLocationClientOption.setCoorType("bd091");// 设置坐标类型，默认gcj02，会有偏差，设置返回的定位结果坐标系
        mLocationClientOption.setIsNeedAddress(true);// 需要地址信息

        // 设置参数给LocationClient
        mLocationClient.setLocOption(mLocationClientOption);

        // 3. 第三步，实现BDLocationListener接口
        mLocationClient.registerLocationListener(mBdLocationListener);

        // 4. 第四步，开始定位
        mLocationClient.start();
    }
    // 定位监听
    private BDLocationListener mBdLocationListener = new BDLocationListener() {
        // 当获取到定位数据的时候会触发
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if (bdLocation == null) {
                // 没有拿到数据，可以重新进行请求
                mLocationClient.requestLocation();
                return;
            }

            // 拿到定位的经纬度
            double latitude = bdLocation.getLatitude();
            double longitude = bdLocation.getLongitude();

            // 定位的位置和地址
            mCurrentLocation = new LatLng(latitude, longitude);
            mCurrentAddr = bdLocation.getAddrStr();

            Log.i("TAG", "定位的位置：" + mCurrentAddr + "经纬度：" + latitude + "," + longitude);

            // 地图上设置定位数据
            MyLocationData locationData = new MyLocationData.Builder()
                    .latitude(latitude)// 设置定位的经纬度
                    .longitude(longitude)
                    .accuracy(100f)// 定位精度的大小
                    .build();

            mBaidumap.setMyLocationData(locationData);

            if (isFirst) {
                // 自动移动到定位处
                moveToLocation();
                isFirst=false;
            }

        }
    };
    // 初始化百度地图的操作
    private void initMapView() {

        // 地图的状态
        MapStatus mMapStatus = new MapStatus.Builder()
                .rotate(0)// 旋转的角度
                .zoom(13)// 默认是12，范围3-21
                .overlook(0)// 俯仰的角度
                .build();

        // 设置地图的信息
        BaiduMapOptions mMapOptions = new BaiduMapOptions()
                .mapStatus(mMapStatus)
                .compassEnabled(true)// 是否显示指南针，默认显示
                .zoomGesturesEnabled(true)// 是否允许缩放手势
                .scaleControlEnabled(false)// 不显示比例尺
                .zoomControlsEnabled(false);// 不显示缩放的控件

        // 创建地图控件
        mMapView = new MapView(getActivity(), mMapOptions);

        // 在布局中添加地图的控件：0，放置在第一位
        mMapFrame.addView(mMapView, 0);

        // 拿到地图的操作类(设置地图的视图、地图状态变化、添加覆盖物等)
        mBaidumap = mMapView.getMap();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.iv_scaleUp, R.id.iv_scaleDown, R.id.tv_satellite, R.id.tv_compass, R.id.tv_located})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_scaleUp:
                mBaidumap.setMapStatus(MapStatusUpdateFactory.zoomIn());
                break;
            case R.id.iv_scaleDown:
                mBaidumap.setMapStatus(MapStatusUpdateFactory.zoomOut());
                break;
            case R.id.tv_satellite:

                int mapType = mBaidumap.getMapType();
                mapType = (mapType == BaiduMap.MAP_TYPE_NORMAL) ? BaiduMap.MAP_TYPE_SATELLITE : BaiduMap.MAP_TYPE_NORMAL;

                String msg = (mapType == BaiduMap.MAP_TYPE_NORMAL) ? "卫星" : "普通";
                mBaidumap.setMapType(mapType);
                mtvSatellite.setText(msg);
                break;
            case R.id.tv_compass:
                //切换地图指南针的显示与否
                boolean enabled = mBaidumap.getUiSettings().isCompassEnabled();
                mBaidumap.getUiSettings().setCompassEnabled(!enabled);
                break;
        }
    }
    // 点击定位按钮：移动到定位的位置
    @OnClick(R.id.tv_located)
    public void moveToLocation() {
        MapStatus mapStatus = new MapStatus.Builder()
                .target(mCurrentLocation)
                .rotate(0)
                .overlook(0)
                .zoom(19)
                .build();

        mStatusUpdate = MapStatusUpdateFactory.newMapStatus(mapStatus);

        mBaidumap.animateMapStatus(mStatusUpdate);
    }

    // 处理权限的回调
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case LOCATION_REQUEST_CODE:
                // 用户授权成功了
                if (grantResults[0]== PackageManager.PERMISSION_GRANTED) {
                    // 定位了
                    mLocationClient.requestLocation();
                }else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()).setMessage("打开WIFI和GPS提升定位准确度，以\\n\\n便更准确地找到您的位置。现在开启？")
                            .setNegativeButton("取消",null)
                            .setPositiveButton("开启", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .setIcon(R.mipmap.map_address);
                    builder.show();

                }
        }
    }
}
