package com.jiangtao.shuzicai.model.user;

import android.util.Log;

import com.jiangtao.shuzicai.R;
import com.jiangtao.shuzicai.basic.base.BaseActivityWithToolBar;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class RegisterSetInfoActivity extends BaseActivityWithToolBar {

    @Override
    public int setLayoutId() {
        return R.layout.activity_register_set_info;
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
