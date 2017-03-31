package com.yulu.zhaoxinpeng.mytreasuremap.treasure;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.yulu.zhaoxinpeng.mytreasuremap.R;
import com.yulu.zhaoxinpeng.mytreasuremap.activity.user.UserPrefs;
import com.yulu.zhaoxinpeng.mytreasuremap.commons.ActivityUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2017/3/28.
 */

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.navigation_view)
    NavigationView mNavigationView;
    @BindView(R.id.drawerLayout)
    DrawerLayout mDrawerLayout;
    private Unbinder unbinder;
    private ImageView mUserIcon;
    private ActivityUtils mActivityUtils;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        unbinder = ButterKnife.bind(this);

        mActivityUtils = new ActivityUtils(this);

        //设置ToolBar
       /* setSupportActionBar(mToolbar);
        // 不显示默认的标题，而是显示布局中自己加的TextView
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }*/

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                mDrawerLayout,
                mToolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        toggle.syncState();//同步状态
        mDrawerLayout.addDrawerListener(toggle);

        //设置侧滑菜单item的选择监听
        mNavigationView.setNavigationItemSelectedListener(this);

        //找到侧滑菜单中的头布局的头像
        mUserIcon = (ImageView) mNavigationView.getHeaderView(0).findViewById(R.id.iv_usericon);
        mUserIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivityUtils.showToast("此乃圆形头像");
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        // 更新头像信息
        String photo = UserPrefs.getInstance().getPhoto();
        if (photo != null) {
            //采用Picasso 加载头像
            Picasso.with(this)
                    .load(photo)
                    .into(mUserIcon);
        }
    }

    //设置侧滑菜单Navigation中的item 每一项被选择时的点击事件
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_hide:
                mActivityUtils.showToast("开始埋藏宝藏吧");
                break;
            case R.id.menu_logout:
                mActivityUtils.showToast("退出喽");
                break;
        }

        //菜单退出时，按照原来滑出的方向返回
        mDrawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }
}
