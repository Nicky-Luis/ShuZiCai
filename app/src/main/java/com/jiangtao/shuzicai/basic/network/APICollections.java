package com.jiangtao.shuzicai.basic.network;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by nicky on 2016/12/29.
 * 接口
 */

public interface APICollections {

    /**
     * 获取最新的固件版本号
     * 传递 FirmwareVersionBean 对象
     *
     * @param body 数据表单
     */
    @POST("g_version_match.php")
    Call<ResponseBody> getFirmwareVersion(@Body String body);

    /**
     * 获取所有APP的信息
     */
    @GET("apps")
    Call<ResponseBody> getApps();


    /**
     * 获取所有APP的信息
     */
    @GET("classes/StockIndex")
    Call<ResponseBody> getIndexDate(@Query("where") String where);

}
