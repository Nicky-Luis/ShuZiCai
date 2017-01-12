package com.jiangtao.shuzicai.basic.network;

/**
 * Created by nicky on 2016/12/29.
 * 通用的网络请求回调
 */

public interface INetworkResponse {

    //操作失败
    int ERR_RESULT_FAILURE = -1;
    //操作成功
    int ECODE_RESULT_SUCCEED = 1;
    //数据解析失败
    int ERR_ANALYSIS_DATA = 2;

    void onFailure(int code);

    void onSucceed(String result);
}
