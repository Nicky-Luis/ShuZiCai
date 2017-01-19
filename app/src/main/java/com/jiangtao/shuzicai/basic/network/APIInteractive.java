package com.jiangtao.shuzicai.basic.network;


import com.jiangtao.shuzicai.model.user.entry.RegisterBean;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * Created by nicky on 2017/1/4.
 * 所有的api请求
 */

public class APIInteractive {

    //请求对象
    private static APICollections request = null;

    //初始化
    public static void initRetrofit() {
        if (null == request) {
            request = NetworkRequest.getRetrofitClient(APICollections.class);
        }
    }

    /**
     * 获取所有APP的信息
     *
     * @param callback
     */
    public static void getAllAPPInfos(final INetworkResponse callback) {
        if (null == request) {
            initRetrofit();
        }
        Call<ResponseBody> call = request.getApps();
        NetworkRequest.netRequest(call, callback);
    }


    /**
     * 获取指数信息
     *
     * @param where
     * @param callback
     */
    public static void getIndexData(String where, final INetworkResponse callback) {
        if (null == request) {
            initRetrofit();
        }

        Call<ResponseBody> call = request.getIndexDate(where);
        NetworkRequest.netRequest(call, callback);
    }

    /**
     * 获取指数信息
     *
     * @param callback
     */
    public static void getBillboardData(final INetworkResponse callback) {
        if (null == request) {
            initRetrofit();
        }

        Call<ResponseBody> call = request.getBillboardData(10, 0);
        NetworkRequest.netRequest(call, callback);
    }


    /**
     * 请求验证码
     *
     * @param body
     * @param callback
     */
    public static void requestSmsCode(RequestBody body, final INetworkResponse callback) {
        if (null == request) {
            initRetrofit();
        }

        Call<ResponseBody> call = request.requestSmsCode(body);
        NetworkRequest.netRequest(call, callback);
    }

    /**
     * 验证验证码
     *
     * @param code
     * @param phone
     * @param callback
     */
    public static void verifySmsCode(String code, String phone, final INetworkResponse callback) {
        if (null == request) {
            initRetrofit();
        }

        Call<ResponseBody> call = request.verifySmsCode(code, phone);
        NetworkRequest.netRequest(call, callback);
    }

    /**
     * 查询验证码
     *
     * @param ssid
     * @param callback
     */
    public static void querySms(String ssid, final INetworkResponse callback) {
        if (null == request) {
            initRetrofit();
        }

        Call<ResponseBody> call = request.querySms(ssid);
        NetworkRequest.netRequest(call, callback);
    }

    /**
     * 注册
     *
     * @param bean
     * @param callback
     */
    public static void register(RegisterBean bean, final INetworkResponse callback) {
        if (null == request) {
            initRetrofit();
        }

        Call<ResponseBody> call = request.register(bean);
        NetworkRequest.netRequest(call, callback);
    }

}
