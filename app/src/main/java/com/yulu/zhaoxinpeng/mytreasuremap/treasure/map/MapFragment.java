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
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.yulu.zhaoxinpeng.mytreasuremap.R;
import com.yulu.zhaoxinpeng.mytreasuremap.commons.ActivityUtils;
import com.yulu.zhaoxinpeng.mytreasuremap.custom.TreasureView;
import com.yulu.zhaoxinpeng.mytreasuremap.treasure.Area;
import com.yulu.zhaoxinpeng.mytreasuremap.treasure.Treasure;
import com.yulu.zhaoxinpeng.mytreasuremap.treasure.TreasureRepo;
import com.yulu.zhaoxinpeng.mytreasuremap.treasure.detail.TreasureDetailActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2017/3/31.
 * 地图和宝藏的展示
 */

public class MapFragment extends Fragment implements MapMvpView {

    private static final int LOCATION_REQUEST_CODE = 200;
    @BindView(R.id.center)
    Space center;
    @BindView(R.id.iv_located)
    ImageView ivLocated;
    @BindView(R.id.btn_HideHere)
    Button mBtnHideHere;
    @BindView(R.id.centerLayout)
    RelativeLayout mCenterLayout;
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
    FrameLayout mLayoutBottom;
    @BindView(R.id.map_frame)
    FrameLayout mMapFrame;
    @BindView(R.id.treasureView)
    TreasureView mTreasureView;
    @BindView(R.id.hide_treasure)
    RelativeLayout mHideTreasure;

    Unbinder unbinder;
    private MapView mMapView;
    private BaiduMap mBaidumap;
    private LocationClient mLocationClient;
    private static LatLng mCurrentLocation;
    private static LatLng first_CurrentLocation;
    private String mCurrentAddr;
    private MapStatusUpdate mStatusUpdate;
    private Boolean isFirst = true;
    private MapPresenter mMapPresenter;
    private ActivityUtils mAcitivityUtils;
    private Marker mCurrentMarker;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container);
        // 检测权限有没有授权成功
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            //如果没有成功获取，就需要向用户申请
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_REQUEST_CODE);
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        unbinder = ButterKnife.bind(this, view);

        mAcitivityUtils = new ActivityUtils(getActivity());

        mMapPresenter = new MapPresenter((MapMvpView) this);

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
            first_CurrentLocation=mCurrentLocation;
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
                isFirst = false;
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

        // 设置地图状态的监听
        mBaidumap.setOnMapStatusChangeListener(mStatusChangeListener);

        // 设置地图上的覆盖物的点击监听
        mBaidumap.setOnMarkerClickListener(mMarkerClickListener);
    }

    // 地图状态的监听
    private BaiduMap.OnMapStatusChangeListener mStatusChangeListener = new BaiduMap.OnMapStatusChangeListener() {
        @Override
        public void onMapStatusChangeStart(MapStatus mapStatus) {

        }

        @Override
        public void onMapStatusChange(MapStatus mapStatus) {

        }

        //当地图状态不再改变
        @Override
        public void onMapStatusChangeFinish(MapStatus mapStatus) {

            // 拿到当前移动后的地图状态所在的位置
            LatLng target = mapStatus.target;

            // 地图状态确实发生变化了
            if (target != MapFragment.this.mCurrentLocation) {
                // 根据当前的地图的状态来获取当前的区域内的宝藏数据
                updateMapArea();
                // 当前地图的位置
                MapFragment.this.mCurrentLocation = target;
            }
        }
    };


    //-------------------------------宝藏数据的获取及展示------------------------------
    // 覆盖物的点击监听
    private BaiduMap.OnMarkerClickListener mMarkerClickListener = new BaiduMap.OnMarkerClickListener() {

        // 点击Marker会触发：marker当前点击的
        @Override
        public boolean onMarkerClick(Marker marker) {

            // 1. 创建InfoWindow
            InfoWindow infoWindow = new InfoWindow(dot_expand, marker.getPosition(), 0, new InfoWindow.OnInfoWindowClickListener() {

                // InfoWindow的点击监听
                @Override
                public void onInfoWindowClick() {
                    // 切换回普通的视图(点图)
                    changeUIMode(UI_MODE_NORMAL);
                }
            });

            // 当前点击的Marker先管理判断
            if (mCurrentMarker!=null){
                if (mCurrentMarker!=marker){
                    mCurrentMarker.setVisible(true);// 点击了其他的，把之前的显示出来
                }
            }
            mCurrentMarker = marker;
            // 点击展示InfoWindow，当前的覆盖物不可见
            mCurrentMarker.setVisible(false);

            // 宝藏的信息取出
            int id = marker.getExtraInfo().getInt("id");

            Treasure treasure = TreasureRepo.getInstance().getTreasure(id);

            mTreasureView.bindTreasure(treasure);

            // 2. 地图上展示
            mBaidumap.showInfoWindow(infoWindow);

            // 切换成宝藏选中的视图
            changeUIMode(UI_MODE_SELECT);

            return false;
        }
    };

    /**
     * 视图的切换方法：根据各个控件的显示和隐藏来实现视图的切换
     * 普通的视图
     * 宝藏选中的视图
     * 埋藏宝藏的视图
     */
    private static final int UI_MODE_NORMAL = 0;// 普通视图
    private static final int UI_MODE_SELECT = 1;// 宝藏选中视图
    private static final int UI_MODE_HIDE = 2;// 埋藏宝藏视图

    private static int mUIMode = UI_MODE_NORMAL;// 当前的视图

    public void changeUIMode(int uiMode) {
        if (mUIMode == uiMode) return;
        mUIMode = uiMode;

        switch (uiMode) {
            // 切换为普通视图
            case UI_MODE_NORMAL:
                if (mCurrentMarker != null) {
                    mCurrentMarker.setVisible(true);
                }
                mBaidumap.hideInfoWindow();
                mLayoutBottom.setVisibility(View.GONE);
                mCenterLayout.setVisibility(View.GONE);
                break;
            // 切换为选中视图(展示宝藏信息卡片)
            case UI_MODE_SELECT:
                mLayoutBottom.setVisibility(View.VISIBLE);
                //mTreasureView.setVisibility(View.VISIBLE);
                mCenterLayout.setVisibility(View.GONE);
                mHideTreasure.setVisibility(View.GONE);
                break;

            // 切换为埋藏宝藏
            case UI_MODE_HIDE:
                if (mCurrentMarker != null) {
                    mCurrentMarker.setVisible(true);
                }
                mBaidumap.hideInfoWindow();
                mCenterLayout.setVisibility(View.VISIBLE);
                mLayoutBottom.setVisibility(View.GONE);
                mBtnHideHere.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mLayoutBottom.setVisibility(View.VISIBLE);
                        mTreasureView.setVisibility(View.GONE);
                        mHideTreasure.setVisibility(View.VISIBLE);
                    }
                });
                break;
        }
    }

    // 区域的确定和宝藏数据的获取
    private void updateMapArea() {

        // 拿到当前的地图状态
        MapStatus mapStatus = mBaidumap.getMapStatus();

        // 从中拿到当前地图的经纬度
        double longitude = mapStatus.target.longitude;
        double latitude = mapStatus.target.latitude;

        // 根据当前的经纬度来确定区域
        Area area = new Area();

        // 根据当前经纬度向上和向下取整得到的区域
        area.setMaxLat(Math.ceil(latitude));
        area.setMaxLng(Math.ceil(longitude));
        area.setMinLat(Math.floor(latitude));
        area.setMinLat(Math.floor(latitude));

        // 要根据当前的区域来获取了：进行网络请求
        mMapPresenter.getTreasure(area);
    }


    //-----------------------视图的具体实现------------------------------------
    @Override
    public void showMessage(String msg) {
        mAcitivityUtils.showToast(msg);
    }

    @Override
    public void setTreasureData(List<Treasure> list) {
        for (Treasure treasure : list) {

            // 拿到每一个宝藏数据、将宝藏信息以覆盖物的形式添加到地图上
            LatLng latLng = new LatLng(treasure.getLatitude(), treasure.getLongitude());
            addMarker(latLng, treasure.getId());
        }

    }

    // 覆盖物图标
    private BitmapDescriptor dot = BitmapDescriptorFactory.fromResource(R.mipmap.treasure_dot);
    private BitmapDescriptor dot_expand = BitmapDescriptorFactory.fromResource(R.mipmap.treasure_expanded);

    // 将定位的位置返回出去，供其它调用
    public static LatLng getMyLocation() {
        return first_CurrentLocation;
    }

    // 添加覆盖物的方法
    private void addMarker(LatLng latLng, int id) {

        MarkerOptions options = new MarkerOptions()
                .position(latLng)// 覆盖物添加的位置
                .icon(dot)// 覆盖物的图标
                .anchor(0.5f, 0.5f);// 锚点位置：居中

        // 将宝藏的id信息也一并存到覆盖物里面
        Bundle bundle = new Bundle();
        bundle.putInt("id", id);
        options.extraInfo(bundle);

        mBaidumap.addOverlay(options);
    }

    //--------------------------------------------------------------------------------------------
    @OnClick({R.id.iv_scaleUp, R.id.iv_scaleDown, R.id.tv_satellite, R.id.tv_compass,R.id.treasureView})
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
            case R.id.treasureView:
                int id = mCurrentMarker.getExtraInfo().getInt("id");
                Treasure treasure = TreasureRepo.getInstance().getTreasure(id);
                TreasureDetailActivity.open(getActivity(),treasure);
                break;
        }
    }

    // 点击定位按钮：移动到定位的位置
    @OnClick(R.id.tv_located)
    public void moveToLocation() {
        MapStatus mapStatus = new MapStatus.Builder()
                .target(first_CurrentLocation)
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

        switch (requestCode) {
            case LOCATION_REQUEST_CODE:
                // 用户授权成功了
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 定位了
                    mLocationClient.requestLocation();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()).setMessage("打开WIFI和GPS提升定位准确度，以\\n\\n便更准确地找到您的位置。现在开启？")
                            .setNegativeButton("取消", null)
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // 清空缓存的数据
        TreasureRepo.getInstance().clear();
        unbinder.unbind();
    }
}
