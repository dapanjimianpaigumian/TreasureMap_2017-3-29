package com.yulu.zhaoxinpeng.mytreasuremap;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.yulu.zhaoxinpeng.mytreasuremap.treasure.HomeActivity;

/**
 * Created by Administrator on 2017/4/9.
 */

public class SplashAcitvity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
