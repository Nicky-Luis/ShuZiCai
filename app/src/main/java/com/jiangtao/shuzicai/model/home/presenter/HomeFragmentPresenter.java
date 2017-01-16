package com.jiangtao.shuzicai.model.home.presenter;


import com.blankj.utilcode.utils.LogUtils;
import com.jiangtao.shuzicai.basic.network.APIInteractive;
import com.jiangtao.shuzicai.basic.network.INetworkResponse;
import com.jiangtao.shuzicai.model.home.interfaces.IHomeFragmentPresenter;

/**
 * Created by Nicky on 2017/1/16.
 */

public class HomeFragmentPresenter implements IHomeFragmentPresenter {


    public HomeFragmentPresenter() {

    }

    //获取数据
    @Override
    public void getIndexData() {
        APIInteractive.getIndexData(new INetworkResponse() {
            @Override
            public void onFailure(int code) {
                LogUtils.i("code=" + code);
            }

            @Override
            public void onSucceed(String result) {
                LogUtils.i("result = " + result);
            }
        });
    }
}
