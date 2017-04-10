package com.yulu.zhaoxinpeng.mytreasuremap.custom;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;

import com.yulu.zhaoxinpeng.mytreasuremap.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2017/4/10.
 * 用户头像点击弹出的视图(视图：从相册、相机、取消)
 */

public class IconSelectWindow extends PopupWindow {
    @BindView(R.id.btn_gallery)
    Button mBtnGallery;
    @BindView(R.id.btn_camera)
    Button mBtnCamera;
    @BindView(R.id.btn_cancel)
    Button mBtnCancel;
    private final Unbinder unbinder;
    private IconSelectWindowListener mIconSelectWindowListener;
    private Activity mActivity;

    //调用父类的构造方法：通过参数实现视图填充
    public IconSelectWindow(Activity activity,IconSelectWindowListener iconSelectWindowListener) {
        super(activity.getLayoutInflater().inflate(R.layout.window_select_icon, null),
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        unbinder = ButterKnife.bind(this, getContentView());

        mIconSelectWindowListener=iconSelectWindowListener;

        mActivity=activity;

        setFocusable(true);//获得焦点

        setBackgroundDrawable(new BitmapDrawable());//设置背景
    }

    // 展示的方法
    public void show(){
        showAtLocation(mActivity.getWindow().getDecorView(), Gravity.BOTTOM,0,0);
    }

    @OnClick({R.id.btn_gallery, R.id.btn_camera, R.id.btn_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_gallery:
                mIconSelectWindowListener.navigatetoGallery();
                break;
            case R.id.btn_camera:
                mIconSelectWindowListener.navigatetoCamera();
                break;
            case R.id.btn_cancel:
                break;

        }
        dismiss();
    }

    //跳转的接口
    public interface IconSelectWindowListener{
        void navigatetoGallery();// 到相册
        void navigatetoCamera();// 到相机
    }
}
