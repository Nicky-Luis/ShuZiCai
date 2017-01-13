package com.jiangtao.shuzicai.basic.network;

import android.util.Log;

import com.jiangtao.shuzicai.Application;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


/**
 * Created by nicky on 2016/12/29.
 * 网络请求
 */

public class NetworkRequest {

    //BASE_URL
    private static final String TAG = "NetworkRequest";
    //超时5秒
    private static final int DEFAULT_TIMEOUT = 5;

    //构造方法私有
    private NetworkRequest() {
    }

    /**
     * 创建 RetrofitManage 服务
     */
    public static <T> T getRetrofitClient(final Class<T> clss) {
        String baseURL = Application.getUserURl() + "/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(getOkHttpClient())//使用自己创建的OkHttp
                .addConverterFactory(ScalarsConverterFactory.create()) //增加返回值为String的支持
                .addConverterFactory(GsonConverterFactory.create())//增加返回值为Gson的支持(以实体类返回)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()) //增加返回值为Oservable<T>的支持
                .build();

        return retrofit.create(clss);
    }


    /**
     * 通用的网络请求模块
     *
     * @param call     请求call
     * @param callback 回调
     */
    public static void netRequest(Call<ResponseBody> call, final INetworkResponse callback) {
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (null == response || response.body() == null) {
                    Log.e(TAG, "data is null");
                    callback.onFailure(INetworkResponse.ERR_ANALYSIS_DATA);
                } else {
                    try {
                        String result = response.body().string();
                        callback.onSucceed(result);
                    } catch (IOException e) {
                        e.printStackTrace();
                        callback.onFailure(INetworkResponse.ERR_ANALYSIS_DATA);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.w(TAG, "Net Request Failure," + t.getMessage());
                callback.onFailure(INetworkResponse.ERR_RESULT_FAILURE);
            }
        });
    }

    ////////////////////////////////////private method/////////////////////////////////////

    /**
     * 获取okhttp对象,在这里进行相关的配置
     *
     * @return OkHttpClient
     */
    private static OkHttpClient getOkHttpClient() {
        //日志显示级别
        HttpLoggingInterceptor.Level level = HttpLoggingInterceptor.Level.BODY;
        //新建log拦截器
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.d("OkHttpClient", "Message:" + message);
            }
        });

        //新建header
        Interceptor headInterceptor = new Interceptor() {

            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request request = chain.request()
                        .newBuilder()
                        .addHeader("X-Bmob-Email", "m13539420022@163.com")
                        .addHeader("X-Bmob-Password", "1234554321")
                        .addHeader("X-Bmob-Application-Id", "cd89b563ca70dfd60befb89fa9ad6e42")
                        .addHeader("X-Bmob-REST-API-Key", "f21c0ff7f6aa0405e9f97c30fc9a414f")
                        .build();
                return chain.proceed(request);
            }
        };

        loggingInterceptor.setLevel(level);
        //定制OkHttp
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        //OkHttp进行添加拦截器loggingInterceptor
        httpClientBuilder.addInterceptor(loggingInterceptor);
        //添加header
        httpClientBuilder.addInterceptor(headInterceptor);
        return httpClientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .build();
    }

}
