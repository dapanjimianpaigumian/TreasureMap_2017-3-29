package com.yulu.zhaoxinpeng.mytreasuremap.activity.user.account;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.pkmmte.view.CircularImageView;
import com.squareup.picasso.Picasso;
import com.yulu.zhaoxinpeng.mytreasuremap.R;
import com.yulu.zhaoxinpeng.mytreasuremap.activity.user.UserPrefs;
import com.yulu.zhaoxinpeng.mytreasuremap.commons.ActivityUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

//个人信息界面
public class AccountActivity extends AppCompatActivity {

    @BindView(R.id.account_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.iv_usericon)
    CircularImageView mIvUsericon;
    private Unbinder unbinder;
    private ActivityUtils mActivityUtils;

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

    @OnClick(R.id.iv_usericon)
    public void onViewClicked() {
        mActivityUtils.showToast("这是头像");
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
        super.onDestroy();
        unbinder.unbind();
    }
}
