package com.jiangtao.shuzicai.basic.network;


import com.blankj.utilcode.utils.LogUtils;
import com.google.gson.JsonObject;
import com.jiangtao.shuzicai.model.user.entry.RegisterBean;
import com.jiangtao.shuzicai.model.user.entry.SmsCodeVerifyBean;
import com.jiangtao.shuzicai.model.user.entry.UpdateInfoBean;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.RequestBody;
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
        Call<JsonObject> call = request.getApps();
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

        Call<JsonObject> call = request.getIndexDate(where);
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

        Call<JsonObject> call = request.getBillboardData(10, 0);
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

        Call<JsonObject> call = request.requestSmsCode(body);
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

        SmsCodeVerifyBean bean = new SmsCodeVerifyBean(phone);
        Call<JsonObject> call = request.verifySmsCode(code, bean);
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

        Call<JsonObject> call = request.querySms(ssid);
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

        Call<JsonObject> call = request.register(bean);
        NetworkRequest.netRequest(call, callback);
    }

    /**
     * 查询验证码是否存在
     *
     * @param where
     * @param callback
     */
    public static void getVerifyCodeExist(String where, final INetworkResponse callback) {
        if (null == request) {
            initRetrofit();
        }

        Call<JsonObject> call = request.isInvitationCodeExist(where);
        NetworkRequest.netRequest(call, callback);
    }

    /**
     * 修改用户信息
     *
     * @param objectId
     * @param bean
     * @param callback
     */
    public static void updateUserInfo(String token, String objectId, UpdateInfoBean bean, final INetworkResponse
            callback) {
        if (null == request) {
            initRetrofit();
        }

        Call<JsonObject> call = request.updateUserInfo(token, objectId, bean);
        NetworkRequest.netRequest(call, callback);
    }

    /**
     * 登录
     *
     * @param account
     * @param password
     * @param callback
     */
    public static void startLogin(String account, String password, final INetworkResponse callback) {
        if (null == request) {
            initRetrofit();
        }
        Call<JsonObject> call = request.login(account, password);
        NetworkRequest.netRequest(call, callback);
    }

    /**
     * 获取当前用户信息
     * @param objectID
     * @param callback
     */
    public static void getCurrentUser(String objectID,  final INetworkResponse callback) {
        if (null == request) {
            initRetrofit();
        }
        Call<JsonObject> call = request.getCurrentUser(objectID);
        NetworkRequest.netRequest(call, callback);
    }


    /**
     * 上传文件
     *
     * @param path
     * @param callback
     */
    public static void postFile(String path, final INetworkResponse callback) {
        LogUtils.i("开始上传文件：" + path);
        BmobUpload.initBmob("cd89b563ca70dfd60befb89fa9ad6e42",
                "f21c0ff7f6aa0405e9f97c30fc9a414f", 6 * 1000);
        BmobUpload.uploadFile(path, new BmobUpload.IUpLoadResult() {
            @Override
            public void onUpLoadResult(String result) {
                LogUtils.i("上传完成，result：" + result);
                if (!result.equals("Not Found") && !result.equals("file Not Found")
                        && !result.equals("Error") && !result.equals("Unregistered")
                        && !result.equals("")) {
                    try {
                        //将字符串转换成jsonObject对象
                        JSONObject myJsonObject = new JSONObject(result);
                        callback.onSucceed(myJsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        callback.onFailure(INetworkResponse.ERR_RESULT_FAILURE);
                    }
                } else {
                    callback.onFailure(INetworkResponse.ERR_RESULT_FAILURE);
                }
            }
        });

    }
}
