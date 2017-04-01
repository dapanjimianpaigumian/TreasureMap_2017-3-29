package com.yulu.zhaoxinpeng.mytreasuremap.treasure.map;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
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

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
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
    Unbinder unbinder1;
    private MapView mMapView;
    private BaiduMap mBaidumap;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container);

        unbinder1 = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        unbinder = ButterKnife.bind(this, view);

        initMapView();
    }

    private void initMapView() {
        MapStatus mMapStatus = new MapStatus.Builder()
                .rotate(0)
                .zoom(13)
                .overlook(0)
                .build();

        BaiduMapOptions mMapOptions = new BaiduMapOptions()
                .mapStatus(mMapStatus)
                .compassEnabled(true)
                .zoomGesturesEnabled(true)
                .scaleControlEnabled(false)
                .zoomControlsEnabled(false);

        mMapView = new MapView(getActivity(), mMapOptions);

        mMapFrame.addView(mMapView, 0);

        mBaidumap = mMapView.getMap();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.iv_scaleUp, R.id.iv_scaleDown, R.id.tv_satellite, R.id.tv_compass})
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
                mapType=(mapType==BaiduMap.MAP_TYPE_NORMAL)?BaiduMap.MAP_TYPE_SATELLITE:BaiduMap.MAP_TYPE_NORMAL;

                String msg=(mapType==BaiduMap.MAP_TYPE_NORMAL)?"卫星":"普通";
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
}
