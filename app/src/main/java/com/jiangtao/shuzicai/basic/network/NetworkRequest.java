package com.jiangtao.shuzicai.basic.network;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
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
    //
    private static Retrofit retrofit;

    //构造方法私有
    private NetworkRequest() {
    }

    /**
     * 创建 RetrofitManage 服务
     */
    public static <T> T getRetrofitClient(String baseURL,final Class<T> clss) {
        retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(getOkHttpClient())//使用自己创建的OkHttp
                .addConverterFactory(ScalarsConverterFactory.create()) //增加返回值为String的支持
                .addConverterFactory(GsonConverterFactory.create())//增加返回值为Gson的支持(以实体类返回)
               // .addCallAdapterFactory(RxJavaCallAdapterFactory.create()) //增加返回值为Oservable<T>的支持
                .build();

        return retrofit.create(clss);
    }


    public static void test(Call<JsonObject> call, final INetworkResponse callback) {
        //请求
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    Log.v(TAG, " response.isSuccessful()：" + response.isSuccessful());
                    Log.v(TAG, " response.body()：" + response.body());
                    Log.v(TAG, " response.message()：" + response.message());
                    Log.v(TAG, " response.errorBody()：" + response.errorBody());
                    Log.v(TAG, " response.raw()：" + response.raw().code());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                //网络问题会走该回调
                if (t instanceof SocketTimeoutException) {
                    Log.w(TAG, "请求超时");
                } else if (t instanceof ConnectException) {
                    Log.w(TAG, "连接错误");
                } else if (t instanceof RuntimeException) {
                    Log.w(TAG, "错误");
                }
                Log.w(TAG, "Net Request Failure," + t.getMessage());
            }
        });
    }
    /**
     * 通用的网络请求模块
     *
     * @param call     请求call
     * @param callback 回调
     */
    public static void netRequest2(Call<JsonArray> call, final INetworkResponse callback) {
        //请求
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.body() == null) {
                    Log.e(TAG, "data is null");
                    callback.onFailure(INetworkResponse.ERR_ANALYSIS_DATA);
                } else {
                    try {
                        JSONArray jsonArray =  new JSONArray(response.body().getAsJsonArray().toString());
                        JSONObject object =new JSONObject();
                        object.put("results",jsonArray);
                        callback.onSucceed(object);
                    } catch (Exception e) {
                        e.printStackTrace();
                        callback.onFailure(INetworkResponse.ERR_ANALYSIS_DATA);
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                //网络问题会走该回调
                if (t instanceof SocketTimeoutException) {
                    Log.w(TAG, "请求超时");
                } else if (t instanceof ConnectException) {
                    Log.w(TAG, "连接错误");
                } else if (t instanceof RuntimeException) {
                    Log.w(TAG, "错误");
                }
                Log.w(TAG, "Net Request Failure," + t.getMessage());
                callback.onFailure(INetworkResponse.ERR_RESULT_FAILURE);
            }
        });
    }

    /**
     * 通用的网络请求模块
     *
     * @param call     请求call
     * @param callback 回调
     */
    public static void netRequest(Call<JsonObject> call, final INetworkResponse callback) {
        //请求
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body() == null) {
                    Log.e(TAG, "data is null");
                    callback.onFailure(INetworkResponse.ERR_ANALYSIS_DATA);
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        callback.onSucceed(jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        callback.onFailure(INetworkResponse.ERR_ANALYSIS_DATA);
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                //网络问题会走该回调
                if (t instanceof SocketTimeoutException) {
                    Log.w(TAG, "请求超时");
                } else if (t instanceof ConnectException) {
                    Log.w(TAG, "连接错误");
                } else if (t instanceof RuntimeException) {
                    Log.w(TAG, "错误");
                }
                Log.w(TAG, "Net Request Failure," + t.getMessage());
                callback.onFailure(INetworkResponse.ERR_RESULT_FAILURE);
            }
        });
    }

    /**
     * 创建bean对象
     *
     * @param bean
     * @return
     */
    public static RequestBody createJsonString(Object bean) {
        Gson gson = new Gson();
        //通过Gson将Bean转化为Json字符串形式
        String route = gson.toJson(bean);
        return RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), route);
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
                //设置Content-Type
//                request.newBuilder().post(new RequestBody() {
//
//                    @Override
//                    public MediaType contentType() {
//                        return MediaType.parse("image/jpeg");
//                    }
//
//                    @Override
//                    public void writeTo(BufferedSink sink) throws IOException {
//                    }
//                });
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
