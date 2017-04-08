package com.yulu.zhaoxinpeng.mytreasuremap.treasure.hide;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.baidu.mapapi.model.LatLng;
import com.yulu.zhaoxinpeng.mytreasuremap.R;
import com.yulu.zhaoxinpeng.mytreasuremap.activity.user.UserPrefs;
import com.yulu.zhaoxinpeng.mytreasuremap.commons.ActivityUtils;
import com.yulu.zhaoxinpeng.mytreasuremap.treasure.TreasureRepo;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class HideTreasureAcitvity extends AppCompatActivity implements HideTreasureView {

    @BindView(R.id.hide_send)
    ImageView mHideSend;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.et_description)
    EditText mEtDescription;
    @BindView(R.id.treasure_size_linearlayout)
    LinearLayout mTreasureSizeLinearlayout;
    private Unbinder unbinder;
    private static final String KEY_TITLE = "key_title";
    private static final String KEY_ADDRESS = "key_address";
    private static final String KEY_LATLNG = "key_latlng";
    private static final String KEY_ALTITUDE = "key_altitude";
    private ActivityUtils mActivityUtils;
    private ProgressDialog mProgressDialog;
    private int Size;

    public static void open(Context context, String title, String address, LatLng latLng, double altitude) {
        Intent intent = new Intent(context, HideTreasureAcitvity.class);
        intent.putExtra(KEY_TITLE, title);
        intent.putExtra(KEY_ADDRESS, address);
        intent.putExtra(KEY_LATLNG, latLng);
        intent.putExtra(KEY_ALTITUDE, altitude);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hide_treasure);
        unbinder = ButterKnife.bind(this);

        mActivityUtils = new ActivityUtils(this);

        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getIntent().getStringExtra(KEY_TITLE));
        }

        registerForContextMenu(mTreasureSizeLinearlayout);
    }

    //创建 ContextMenu
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_hide_treasure_size, menu);
    }

    //ContextMenu 监听事件
    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_size_mini:
                mActivityUtils.showToast("小型宝物");
                Size = 0;
                break;
            case R.id.action_size_medium:
                mActivityUtils.showToast("中等宝物");
                Size = 1;
                break;
            case R.id.action_size_large:
                mActivityUtils.showToast("大型宝物");
                Size = 2;
                break;
        }
        return super.onContextItemSelected(item);
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

    @OnClick(R.id.hide_send)
    public void onViewClicked() {

        // 取出传递的数据
        Intent intent = getIntent();
        String titile = intent.getStringExtra(KEY_TITLE);
        String address = intent.getStringExtra(KEY_ADDRESS);
        LatLng latlng = intent.getParcelableExtra(KEY_LATLNG);
        double altitude = intent.getDoubleExtra(KEY_ALTITUDE, 0);

        //用户的TokenId
        int tokenid = UserPrefs.getInstance().getTokenid();

        //拿到描述信息
        String desc = mEtDescription.getText().toString();

        //需要上传的请求体数据
        HideTreasure hideTreasure = new HideTreasure();
        hideTreasure.setTitle(titile);
        hideTreasure.setAltitude(altitude);
        hideTreasure.setDescription(desc);
        hideTreasure.setLatitude(latlng.latitude);
        hideTreasure.setLongitude(latlng.longitude);
        hideTreasure.setLocation(address);
        hideTreasure.setTokenId(tokenid);
        hideTreasure.setSize(Size);

        new HideTreasurePresenter(this).hideTreasure(hideTreasure);
    }

    @Override
    public void showToast(String msg) {
        mActivityUtils.showToast(msg);
    }

    @Override
    public void navigateToHome() {
        finish();

        // 清除缓存 : 为了返回到之前的页面重新去请求数据
        TreasureRepo.getInstance().clear();
    }

    @Override
    public void showProgress() {
        mProgressDialog = ProgressDialog.show(this, "上传宝藏", "宝藏上传中，请稍候~");
    }

    @Override
    public void hideProgress() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }
}
