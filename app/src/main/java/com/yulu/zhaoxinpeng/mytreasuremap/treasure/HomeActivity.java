package com.yulu.zhaoxinpeng.mytreasuremap.treasure;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.yulu.zhaoxinpeng.mytreasuremap.MainActivity;
import com.yulu.zhaoxinpeng.mytreasuremap.R;
import com.yulu.zhaoxinpeng.mytreasuremap.activity.user.UserPrefs;
import com.yulu.zhaoxinpeng.mytreasuremap.commons.ActivityUtils;
import com.yulu.zhaoxinpeng.mytreasuremap.treasure.list.TreasureListFragment;
import com.yulu.zhaoxinpeng.mytreasuremap.treasure.map.MapFragment;

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

    private TreasureListFragment mListFragment;// 列表视图
    private FragmentManager mFragmentManager;
    private MapFragment mMapFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        unbinder = ButterKnife.bind(this);

        mFragmentManager = getSupportFragmentManager();
        mMapFragment = (MapFragment) mFragmentManager.findFragmentById(R.id.mapFragment);

        mActivityUtils = new ActivityUtils(this);

        //设置ToolBar
        setSupportActionBar(mToolbar);
        // 不显示默认的标题，而是显示布局中自己加的TextView
        //if (getSupportActionBar() != null) {
        //getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //}

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

    // 侧滑的Navigation的Item每一项被选择的时候会触发
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_hide:// 埋藏宝藏的时候
                mMapFragment.changeUIMode(2);// 切换到埋藏宝藏的视图
                break;
            case R.id.menu_logout:// 退出的时候
                UserPrefs.getInstance().clearUser();//清除本地用户信息
                mActivityUtils.startActivity(MainActivity.class);//返回动画界面
                finish();
                break;
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    // 准备工作：完成选项菜单的图标的切换等
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        // item的图标的变化处理
        MenuItem item = menu.findItem(R.id.action_toggle);
        // 根据显示的视图不一样，设置不一样的图标
        if (mListFragment != null && mListFragment.isAdded()) {
            item.setIcon(R.drawable.ic_map);
        } else {
            item.setIcon(R.drawable.ic_view_list);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    // 创建：选项菜单的布局填充
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 菜单的填充
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    // 某一个选项菜单被选择的时候(点击)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_toggle:

                // 切换视图：地图的视图和列表的视图进行切换
                showListFragment();

                // 更新选项菜单的视图：触发onPrepareOptionsMenu
                invalidateOptionsMenu();

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    // 显示或隐藏列表视图
    private void showListFragment() {

        //当展示 列表视图时，
        if (mListFragment != null && mListFragment.isAdded()) {

            // 将ListFragment弹出回退栈
            mFragmentManager.popBackStack();
            // 移除ListFragment
            mFragmentManager.beginTransaction().remove(mListFragment).commit();
            return;
        }

        mListFragment = new TreasureListFragment();

        mActivityUtils.showToast(mListFragment + "---------------------");
        // 在布局中展示(FrameLayout作为占位)
        mFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, mListFragment)
                // 添加回退栈
                .addToBackStack(null)
                .commit();
    }

    // 处理返回键
    @Override
    public void onBackPressed() {

        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (mMapFragment.clickBackPressed()) {
                super.onBackPressed();
            }
        }
    }
}
