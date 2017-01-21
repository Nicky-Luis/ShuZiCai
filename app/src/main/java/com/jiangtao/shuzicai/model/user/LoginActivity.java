package com.jiangtao.shuzicai.model.user;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.jiangtao.shuzicai.R;
import com.jiangtao.shuzicai.basic.base.BaseActivityWithToolBar;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.OnClick;

public class LoginActivity extends BaseActivityWithToolBar {


    //设置点击事件
    @OnClick({R.id.loginToRegisterTxt})
    public void OnClick(View view) {
        switch (view.getId()) {

            case R.id.loginToRegisterTxt:
                Intent intent = new Intent(LoginActivity.this, RegisterSetInfoActivity.class);
                startActivity(intent);
                break;

        }
    }

    @Override
    public int setLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void onInitialize() {

    }

    @Override
    public void initPresenter() {

    }

    /**
     * 主线程处理接收到的数据
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEventMainThread(String event) {
        Log.e("event MainThread", "消息： " + event + "  thread: " + Thread.currentThread().getName());
    }
}
