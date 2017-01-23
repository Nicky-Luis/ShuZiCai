package com.jiangtao.shuzicai;


import com.jiangtao.shuzicai.basic.app.BaseSetting;
import com.jiangtao.shuzicai.basic.utils.EncryptUtils;

/**
 * Created by Nicky on 2016/11/26.
 * 保存APP的配置信息
 */

public class AppConfigure {
    //用户登录信息
    private final static String USER_LOGIN_STATUS_SP = "user_login_status";
    private final static String IS_USER_LOGIN = "is_user_login";
    private final static String USER_LOGIN_NAME = "user_name";
    private final static String USER_LOGIN_PASSWORD = "user_password";

    /***
     * 判断用户是否已经登录
     *
     * @return boolean 结果
     */
    public static boolean userIsLogin() {
        BaseSetting setting = new BaseSetting(Application.APPContext, USER_LOGIN_STATUS_SP);
        return setting.loadBoolean(IS_USER_LOGIN);
    }

    /***
     * 更新用户的登录状态
     *
     * @param isLogin 状态
     */
    public static void saveLoginStatue(Boolean isLogin) {
        BaseSetting setting = new BaseSetting(Application.APPContext, USER_LOGIN_STATUS_SP);
        setting.saveBoolean(IS_USER_LOGIN, isLogin);
    }

    /***
     * 保存用户的用户名
     *
     * @param name 用户名
     * @return boolean 结果
     */
    public static boolean saveUserName(String name) {
        BaseSetting setting = new BaseSetting(Application.APPContext,
                USER_LOGIN_STATUS_SP);
        try {
            String secretName = EncryptUtils.encoderJson(name);
            setting.saveString(USER_LOGIN_NAME, secretName);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /***
     * 保存用户的密码
     *
     * @param password 用户名
     * @return boolean 结果
     */
    public static boolean saveUserPassword(String password) {
        BaseSetting setting = new BaseSetting(Application.APPContext,
                USER_LOGIN_STATUS_SP);
        try {
            String secretPassword =  EncryptUtils.encoderJson(password);
            setting.saveString(USER_LOGIN_PASSWORD, secretPassword);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /***
     * 获取用户的用户名
     *
     * @return String 结果
     */
    public static String getUserName() {
        BaseSetting setting = new BaseSetting(Application.APPContext, USER_LOGIN_STATUS_SP);
        try {
            String secretName = setting.loadString(USER_LOGIN_NAME);
            return EncryptUtils.decoderJson(secretName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /***
     * 获取用户的密码
     *
     * @return String 结果
     */
    public static String getUserPassword() {
        BaseSetting setting = new BaseSetting(Application.APPContext
                .getApplicationContext(), USER_LOGIN_STATUS_SP);
        try {
            String secretPassword = setting.loadString(USER_LOGIN_PASSWORD);
            return EncryptUtils.decoderJson(secretPassword);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}
