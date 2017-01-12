package com.jiangtao.shuzicai;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;

/**
 * this is splash activity
 */
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SystemClock.sleep(2000);
        //跳转
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
