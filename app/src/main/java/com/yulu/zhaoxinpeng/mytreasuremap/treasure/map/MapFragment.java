package com.yulu.zhaoxinpeng.mytreasuremap.treasure.map;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yulu.zhaoxinpeng.mytreasuremap.R;

/**
 * Created by Administrator on 2017/3/31.
 */
// 地图和宝藏的展示
public class MapFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container);
        return view;
    }
}
