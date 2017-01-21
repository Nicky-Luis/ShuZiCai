package com.jiangtao.shuzicai.basic.network;


import com.google.gson.JsonObject;
import com.jiangtao.shuzicai.model.user.entry.RegisterBean;
import com.jiangtao.shuzicai.model.user.entry.SmsCodeVerifyBean;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
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
   Call<JsonObject> getFirmwareVersion(@Body String body);

    /**
     * 获取所有APP的信息
     */
    @GET("apps")
   Call<JsonObject> getApps();


    /**
     * 获取指数数据
     *
     * @param where 条件
     * @return
     */
    @GET("classes/StockIndex")
   Call<JsonObject> getIndexDate(@Query("where") String where);

    /**
     * 获取公告数据
     *
     * @param limit 数量
     * @param skip  开始位置
     * @return
     */
    @GET("classes/BillboardMessage")
   Call<JsonObject> getBillboardData(@Query("limit") int limit,
                                        @Query("skip") int skip);

    /**
     * 请求短信验证码
     *
     * @param mobilePhoneNumber
     * @return
     */
    @POST("requestSmsCode")
   Call<JsonObject> requestSmsCode(@Body RequestBody mobilePhoneNumber);


    /**
     * 验证短信验证码
     *
     * @param mobilePhoneNumber
     * @return
     */
    @POST("verifySmsCode/{smsCode}")
   Call<JsonObject> verifySmsCode(@Path("smsCode") String smsCode,
                                     @Body SmsCodeVerifyBean mobilePhoneNumber);

    /**
     * 查询短信状态
     *
     * @return
     */
    @GET("querySms/{smsId}")
   Call<JsonObject> querySms(@Path("smsId") String smsId);


    /**
     * 用户注册
     *
     * @param bean
     * @return
     */
    @POST("users")
   Call<JsonObject> register(@Body RegisterBean bean);

    /**
     * 判断邀请码是否存在
     *
     * @param where
     * @return
     */
    @GET("users")
    Call<JsonObject> isInvitationCodeExist(@Query("where") String where);

}
