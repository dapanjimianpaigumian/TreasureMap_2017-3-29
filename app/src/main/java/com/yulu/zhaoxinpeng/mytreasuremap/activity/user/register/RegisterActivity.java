package com.yulu.zhaoxinpeng.mytreasuremap.activity.user.register;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.RelativeLayout;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
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

// 注册页面
public class RegisterActivity extends AppCompatActivity implements RegisterView {

    @BindView(R.id.et_Username)
    EditText metUsername;
    @BindView(R.id.et_Password)
    EditText metPassword;
    @BindView(R.id.et_Confirm)
    EditText metConfirm;
    @BindView(R.id.btn_Register)
    Button btnRegister;
    @BindView(R.id.activity_register)
    RelativeLayout activityRegister;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private ActivityUtils mActivityUtils;
    private Unbinder unbinder;
    private String mUsername;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private String mPassword;
    private String mComfirm;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();

        unbinder = ButterKnife.bind(this);

        mActivityUtils = new ActivityUtils(this);
        // toolbar展示
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            // 显示返回箭头
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            // 显示标题
            getSupportActionBar().setTitle(R.string.register);

        }
        //给三个输入框添加监听
        metUsername.addTextChangedListener(textWatcher);
        metPassword.addTextChangedListener(textWatcher);
        metConfirm.addTextChangedListener(textWatcher);
    }

    // 文本变化的监听
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            // 根据文本的输入处理按钮的是否可以点击
            mUsername = metUsername.getText().toString();
            mPassword = metPassword.getText().toString();
            mComfirm = metConfirm.getText().toString();
            // 三个输入框都不为空并且密码和确认密码相同的时候
            boolean canRegiste = !(TextUtils.isEmpty(mUsername) || TextUtils.isEmpty(mPassword) || TextUtils.isEmpty(mComfirm)) && (mPassword.equals(mComfirm));

            btnRegister.setEnabled(canRegiste);
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // 处理Actionbar上返回的箭头
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.btn_Register)
    public void onViewClicked() {
        // 账号和密码是否符合规范
        if (RegexUtils.verifyUsername(mUsername) != RegexUtils.VERIFY_SUCCESS) {

            DialogFragmentUtils.getInstance(getString(R.string.username_error), getString(R.string.password_error)).show(getSupportFragmentManager(), "usernameError");
            return;
        }

        if (RegexUtils.verifyPassword(mPassword) != RegexUtils.VERIFY_SUCCESS) {
            // 显示一个对话框提示
            DialogFragmentUtils.getInstance(getString(R.string.password_error), getString(R.string.password_rules)).show(getSupportFragmentManager(), "passowrdError");
            return;
        }

        // 进行注册的业务
        new RegisterPresenter(this).register(new User(mUsername,mPassword));

    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Register Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    //---------------------------注册过程中涉及到的视图--------------------------
    // 显示进度条
    @Override
    public void showProgress() {
        mProgressDialog = ProgressDialog.show(this, "注册", "正在注册中，请稍候~");
    }

    // 隐藏进度条
    @Override
    public void hideProgress() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    // 显示信息
    @Override
    public void showToast(String msg) {
        mActivityUtils.showToast(msg);
    }

    // 跳转到Home页面
    @Override
    public void navigateToHome() {
        mActivityUtils.startActivity(HomeActivity.class);
        finish();
        // Main页面关闭
        Intent intent = new Intent(MainActivity.MAI_ACTION);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
