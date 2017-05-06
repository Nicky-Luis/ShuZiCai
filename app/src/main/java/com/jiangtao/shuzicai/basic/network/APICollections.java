package com.jiangtao.shuzicai.basic.network;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.jiangtao.shuzicai.model.mall.entry.GoodsOrder;
import com.jiangtao.shuzicai.model.user.entry.RegisterBean;
import com.jiangtao.shuzicai.model.user.entry.SmsCodeVerifyBean;
import com.jiangtao.shuzicai.model.user.entry.UpdateInfoBean;

import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by nicky on 2016/12/29.
 * 接口
 */

public interface APICollections {

    /**
     * 获取所有APP的信息
     */
    @GET("1/apps")
    Call<JsonObject> getApps();


    /**
     * 获取指数数据
     *
     * @param where 条件
     * @return
     */
    @GET("1/classes/StockIndex")
    Call<JsonObject> getIndexDate(@Query("where") String where);

    /**
     * 获取公告数据
     *
     * @param limit 数量
     * @param skip  开始位置
     * @return
     */
    @GET("1/classes/BillboardMessage")
    Call<JsonObject> getBillboardData(@Query("limit") int limit,
                                      @Query("skip") int skip);

    /**
     * 请求短信验证码
     *
     * @param mobilePhoneNumber
     * @return
     */
    @POST("1/requestSmsCode")
    Call<JsonObject> requestSmsCode(@Body RequestBody mobilePhoneNumber);


    /**
     * 验证短信验证码
     *
     * @param mobilePhoneNumber
     * @return
     */
    @POST("1/verifySmsCode/{smsCode}")
    Call<JsonObject> verifySmsCode(@Path("smsCode") String smsCode,
                                   @Body SmsCodeVerifyBean mobilePhoneNumber);

    /**
     * 查询短信状态
     *
     * @return
     */
    @GET("1/querySms/{smsId}")
    Call<JsonObject> querySms(@Path("smsId") String smsId);


    /**
     * 用户注册
     *
     * @param bean
     * @return
     */
    @POST("1/users")
    Call<JsonObject> register(@Body RegisterBean bean);

    /**
     * 判断邀请码是否存在
     *
     * @param where
     * @return
     */
    @GET("1/users")
    Call<JsonObject> isInvitationCodeExist(@Query("where") String where);

    /**
     * 修改用户信息
     *
     * @param objectID
     * @param bean
     * @return
     */
    @PUT("1/users/{objectID}")
    Call<JsonObject> updateUserInfo(@Header("X-Bmob-Session-Token") String userToken,
                                    @Path("objectID") String objectID,
                                    @Body UpdateInfoBean bean);


    /**
     * 登录
     *
     * @param username
     * @param password
     * @return
     */
    @GET("1/login")
    Call<JsonObject> login(@Query("username") String username,
                           @Query("password") String password);

    /**
     * 获取当前用户信息
     *
     * @param objectID
     * @return
     */
    @GET("1/users/{objectID}")
    Call<JsonObject> getCurrentUser(@Path("objectID") String objectID);


    /**
     * 获取当前用户的财富
     *
     * @param where
     * @return
     */
    @GET("1/classes/WealthValue")
    Call<JsonObject> getWealthValue(@Query("where") String where);

    /**
     * 获取商品信息
     *
     * @param limit
     * @param skip
     * @return
     */
    @GET("1/classes/Goods")
    Call<JsonObject> getGoods(@Query("limit") int limit,
                              @Query("skip") int skip);

    /**
     * 提交订单
     *
     * @param bean
     * @return
     */
    @POST("1/classes/goodsOrder")
    Call<JsonObject> submitOrder(@Body GoodsOrder bean);


    /**
     * 获取服务器时间
     *
     * @return
     */
    @GET("1/timestamp")
    Call<JsonObject> getServerTime();

    /**
     * 获取兑换交易记录
     *
     * @param where
     * @return
     */
    @GET("1/classes/goodsOrder")
    Call<JsonObject> getExchangeRecord(@Query("where") String where,
                                       @Query("include") String include);

    /**
     * 获取交易记录
     *
     * @param where
     * @return
     */
    @GET("1/classes/WealthDetail")
    Call<JsonObject> getWealthDetailRecord(@Query("where") String where);


    /**
     * 批量操作
     *
     * @param bean
     * @return
     */
    @POST("1/batch")
    Call<JsonArray> bmobBatch(@Body Map<String, List> bean);

    /**
     * 查询用户信息
     *
     * @param where
     * @return
     */
    @GET("1/classes/_User")
    Call<JsonObject> getUserInfo(@Query("where") String where);


    /**
     * 验证订单信息
     *
     * @param total_amount
     * @param body
     * @param subject
     * @return
     */
    @GET("pay")
    Call<JsonObject> authRecharge(@Query("total_amount") String total_amount,
                                  @Query("body") String body,
                                  @Query("subject") String subject);
}
