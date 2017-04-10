package com.yulu.zhaoxinpeng.mytreasuremap.activity.user.account;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.pkmmte.view.CircularImageView;
import com.squareup.picasso.Picasso;
import com.yulu.zhaoxinpeng.mytreasuremap.R;
import com.yulu.zhaoxinpeng.mytreasuremap.activity.user.UserPrefs;
import com.yulu.zhaoxinpeng.mytreasuremap.commons.ActivityUtils;
import com.yulu.zhaoxinpeng.mytreasuremap.custom.IconSelectWindow;

import org.hybridsquad.android.library.CropHandler;
import org.hybridsquad.android.library.CropHelper;
import org.hybridsquad.android.library.CropParams;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

//个人信息界面
public class AccountActivity extends AppCompatActivity implements AccountView {

    @BindView(R.id.account_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.iv_usericon)
    CircularImageView mIvUsericon;
    private Unbinder unbinder;
    private ActivityUtils mActivityUtils;
    private IconSelectWindow mSelectWindow;
    private ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        unbinder = ButterKnife.bind(this);

        mActivityUtils = new ActivityUtils(this);

        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.account_msg);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        String photo = UserPrefs.getInstance().getPhoto();
        if (photo != null) {
            Picasso.with(this)
                    .load(photo)
                    .into(mIvUsericon);
        }
    }

    //点击头像：弹出一个弹窗(自定义一个PopupWindow)
    @OnClick(R.id.iv_usericon)
    public void onViewClicked() {
        if (mSelectWindow == null) {
            mSelectWindow = new IconSelectWindow(this, selectWindowListener);
        }
        if (mSelectWindow.isShowing()) {
            mSelectWindow.dismiss();
            return;
        }
        mSelectWindow.show();
    }

    //跳转的监听
    private IconSelectWindow.IconSelectWindowListener selectWindowListener = new IconSelectWindow.IconSelectWindowListener() {

        //跳转到相册
        @Override
        public void navigatetoGallery() {

            // 清除缓存
            CropHelper.clearCachedCropFile(mCropHandler.getCropParams().uri);

            Intent intent = CropHelper.buildCropFromGalleryIntent(mCropHandler.getCropParams());
            startActivityForResult(intent, CropHelper.REQUEST_CROP);
        }

        //跳转到相机
        @Override
        public void navigatetoCamera() {

            // 清除之前剪切的图片的缓存
            CropHelper.clearCachedCropFile(mCropHandler.getCropParams().uri);

            // 跳转
            Intent intent = CropHelper.buildCaptureIntent(mCropHandler.getCropParams().uri);
            startActivityForResult(intent, CropHelper.REQUEST_CAMERA);
        }
    };

    //图片处理
    private CropHandler mCropHandler = new CropHandler() {
        //图片剪切之后：参数Uri代表剪切后的图片
        @Override
        public void onPhotoCropped(Uri uri) {
            // 拿到剪切之后的图片
            File file = new File(uri.getPath());
            //进行网络请求将图片上传
            new AccountPresenter(AccountActivity.this).uploadPhoto(file);
        }

        //取消剪切
        @Override
        public void onCropCancel() {
            mActivityUtils.showToast("取消剪切");
        }

        //剪切失败
        @Override
        public void onCropFailed(String message) {
            mActivityUtils.showToast(message);
        }

        //剪切的参数设置：Uri(图片剪切之后保存的路径)
        @Override
        public CropParams getCropParams() {

            //默认剪切设置
            CropParams cropParams = new CropParams();
            return cropParams;
        }

        // 上下文
        @Override
        public Activity getContext() {
            return AccountActivity.this;
        }
    };

    //处理图片剪切的结果
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        CropHelper.handleResult(mCropHandler, requestCode, resultCode, data);
    }

    //处理返回箭头
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        if (mCropHandler.getCropParams() != null)
            CropHelper.clearCachedCropFile(mCropHandler.getCropParams().uri);
        super.onDestroy();
    }


    //----------------------------头像的视图方法---------------------------------------
    @Override
    public void showProgress() {
        mProgressDialog = ProgressDialog.show(this, "上传头像", "头像正在上传中~");
    }

    @Override
    public void hideProgress() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void showToast(String msg) {
        mActivityUtils.showToast(msg);
    }

    @Override
    public void updatePhoto(String photoUrl) {

        if (photoUrl != null) {

            Picasso.with(this)
                    .load(photoUrl)
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.user_icon)
                    .into(mIvUsericon);
        }
    }
}
