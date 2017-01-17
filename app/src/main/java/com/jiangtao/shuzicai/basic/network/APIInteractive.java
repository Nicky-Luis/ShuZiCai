package com.jiangtao.shuzicai.basic.network;


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
}
