package com.jiangtao.shuzicai;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;

import com.jiangtao.shuzicai.model.main.MainActivity;

/**
 * this is splash activity
 */
public class SplashActivity extends AppCompatActivity {

    //启动的时间
    private final static int LunchTime = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SystemClock.sleep(LunchTime);
        //跳转
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
