package com.jiangtao.shuzicai;

import android.widget.Button;
import android.widget.TextView;

import com.blankj.utilcode.utils.LogUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.jiangtao.shuzicai.basic.base.BaseActivityWithToolBar;
import com.jiangtao.shuzicai.basic.network.APIInteractive;
import com.jiangtao.shuzicai.basic.network.INetworkResponse;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivityWithToolBar {

    @BindView(R.id.button1)
    public Button button1;

    @OnClick({R.id.button1})
    public void ViewClick(TextView button) {
        switch (button.getId()) {
            case R.id.button1:
                LogUtils.i("点击了");
                APIInteractive.getAllAPPInfos(new INetworkResponse() {
                    @Override
                    public void onFailure(int code) {
                        ToastUtils.showShortToast("失败了");
                    }

                    @Override
                    public void onSucceed(String result) {
                        ToastUtils.showShortToast("成功了");
                    }
                });
                break;
            default:
                break;
        }
    }

    //////////////////////////////////////////////////
    @Override
    public int setLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onInitialize() {

    }

    @Override
    public void initPresenter() {

    }
}
