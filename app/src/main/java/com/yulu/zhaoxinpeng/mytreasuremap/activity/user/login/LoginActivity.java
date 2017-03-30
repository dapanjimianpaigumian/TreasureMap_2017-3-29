package com.yulu.zhaoxinpeng.mytreasuremap.activity.user.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yulu.zhaoxinpeng.mytreasuremap.HomeActivity;
import com.yulu.zhaoxinpeng.mytreasuremap.MainActivity;
import com.yulu.zhaoxinpeng.mytreasuremap.R;
import com.yulu.zhaoxinpeng.mytreasuremap.activity.user.User;
import com.yulu.zhaoxinpeng.mytreasuremap.commons.ActivityUtils;
import com.yulu.zhaoxinpeng.mytreasuremap.commons.DialogFragmentUtils;
import com.yulu.zhaoxinpeng.mytreasuremap.commons.RegexUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.R.id.message;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

public class LoginActivity extends AppCompatActivity implements LoginView {

    @BindView(R.id.btn_Login)
    Button btnLogin;
    @BindView(R.id.et_Username)
    EditText metUsername;
    @BindView(R.id.et_Password)
    EditText metPassword;
    @BindView(R.id.tv_forgetPassword)
    TextView tvForgetPassword;
    @BindView(R.id.activity_login)
    LinearLayout activityLogin;
    private Unbinder unbinder;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    private String mPassword;
    private String mUsername;
    private ProgressDialog mProgressDialog;
    private ActivityUtils mActivityUtils;
    private LoginView mLoginView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    //相当于initview
    @Override
    public void onContentChanged() {
        super.onContentChanged();

        unbinder = ButterKnife.bind(this);

        mActivityUtils=new ActivityUtils(this);
        // toolbar
        // Toolbar作为ActionBar展示
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            // 设置返回的箭头,内部是选项菜单来处理的，而且Android内部已经给他设置好了id
            // android.R.id.home
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            //设置标题
            getSupportActionBar().setTitle(R.string.login);
        }
        //给两个输入框添加监听
        metUsername.addTextChangedListener(textWatcher);
        metPassword.addTextChangedListener(textWatcher);
    }

    //监听文本的变化
    private TextWatcher textWatcher = new TextWatcher() {
        //文本变化前
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        //文本变化时
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        //文本变化后
        @Override
        public void afterTextChanged(Editable s) {
            mUsername = metUsername.getText().toString();
            mPassword = metPassword.getText().toString();

            // 判断用户名和密码都不为空，按钮才可以点击
            boolean canlogin = !(TextUtils.isEmpty(mUsername) || TextUtils.isEmpty(mPassword));
            //当用户名和密码都不为空时，登陆按钮才可以点击
            btnLogin.setEnabled(canlogin);
        }
    };

    // 重写选项菜单的选中监听

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.btn_Login)
    public void onViewClicked() {
        // 账号不符合规则
        if (RegexUtils.verifyUsername(mUsername) != RegexUtils.VERIFY_SUCCESS) {

            //弹出提示框
            DialogFragmentUtils.getInstance(getString(R.string.username_error),
                    getString(R.string.username_rules))
                    .show(getSupportFragmentManager(), "usernameError");
            return;
        }
        // 密码不符合规范
        if (RegexUtils.verifyPassword(mPassword) != RegexUtils.VERIFY_SUCCESS) {

            //弹出提示框
            DialogFragmentUtils.getInstance(getString(R.string.password_error),
                    getString(R.string.password_rules))
                    .show(getSupportFragmentManager(), "passwordError");
            return;
        }

        // 要去做登录的业务逻辑,模拟用户登录的场景，异步任务来模拟
      new LoginPresenter(this).Login(new User(mUsername,mPassword));

    }
    //--------------------登录过程中的视图处理-----------------------
    @Override
    public void showProgress() {
        mProgressDialog = ProgressDialog.show(this, "登录", "正在登录中，请稍候~");

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
    public void navigateToHome() {
        mActivityUtils.startActivity(HomeActivity.class);
        finish();

        // MainActivity也需要关闭：发个本地广播的形式关闭
        Intent intent = new Intent(MainActivity.MAI_ACTION);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
