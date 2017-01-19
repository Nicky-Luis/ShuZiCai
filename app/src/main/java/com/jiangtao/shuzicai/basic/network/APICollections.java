package com.jiangtao.shuzicai.basic.network;


import com.jiangtao.shuzicai.model.user.entry.RegisterBean;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
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
     * 获取指数数据
     *
     * @param where 条件
     * @return
     */
    @GET("classes/StockIndex")
    Call<ResponseBody> getIndexDate(@Query("where") String where);

    /**
     * 获取公告数据
     *
     * @param limit 数量
     * @param skip  开始位置
     * @return
     */
    @GET("classes/BillboardMessage")
    Call<ResponseBody> getBillboardData(@Query("limit") int limit,
                                        @Query("skip") int skip);

    /**
     * 请求短信验证码
     *
     * @param mobilePhoneNumber
     * @return
     */
    @POST("requestSmsCode")
    Call<ResponseBody> requestSmsCode(@Body RequestBody mobilePhoneNumber);


    /**
     * 验证短信验证码
     *
     * @param mobilePhoneNumber
     * @return
     */
    @POST("verifySmsCode/{smsCode}")
    Call<ResponseBody> verifySmsCode(@Path("smsCode") String smsCode,
                                     @Field("mobilePhoneNumber") String mobilePhoneNumber);

    /**
     * 查询短信状态
     *
     * @param smsId
     * @return
     */
    @POST("querySms/:{smsId}")
    Call<ResponseBody> querySms(@Path("smsId") String smsId);


    /**
     * 用户注册
     *
     * @param bean
     * @return
     */
    @POST("users")
    Call<ResponseBody> register(@Body RegisterBean bean);

}
