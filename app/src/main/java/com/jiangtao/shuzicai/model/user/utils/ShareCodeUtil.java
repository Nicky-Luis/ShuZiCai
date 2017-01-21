package com.jiangtao.shuzicai.model.user.utils;

import com.blankj.utilcode.utils.LogUtils;
import com.jiangtao.shuzicai.basic.network.APIInteractive;
import com.jiangtao.shuzicai.basic.network.BmobQueryUtils;
import com.jiangtao.shuzicai.basic.network.INetworkResponse;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 邀请码生成器，算法原理：
 */
public class ShareCodeUtil {

    /**
     * 随机生成验证码（数字+字母）
     *
     * @param len 邀请码长度
     * @return string
     */
    public static void generateRandomStr(int len, IShareCallBack callBack) {
        //字符源，可以根据需要删减
        String generateSource = "qwe8as2dzx9c7p5ik3mjufr4vyltn6bgh";
        String rtnStr = "";
        for (int i = 0; i < len; i++) {
            //循环随机获得当次字符，并移走选出的字符
            String nowStr = String.valueOf(generateSource.charAt((int) Math.floor(Math.random() * generateSource
                    .length())));
            rtnStr += nowStr;
            generateSource = generateSource.replaceAll(nowStr, "");
        }
        //判断验证码
        isShareCodeExist(rtnStr, callBack);
    }

    /**
     * 查询是否有一家存在的数据
     *
     * @param code
     * @return
     */
    private static void isShareCodeExist(final String code, final IShareCallBack callBack) {
        BmobQueryUtils utils = BmobQueryUtils.newInstance();
        String where = utils.setValue("InvitationCode").equal(code);

        //查询是否有已经存在的
        APIInteractive.getVerifyCodeExist(where, new INetworkResponse() {

            @Override
            public void onFailure(int code) {
                LogUtils.i("查询邀请码成功，code:" + code);
                callBack.shareCodeExist();
            }

            @Override
            public void onSucceed(JSONObject result) {
                LogUtils.i("查询邀请码成功，result:" + result);
                JSONArray obj = result.optJSONArray("results");
                if (obj.length() > 0) {
                    callBack.shareCodeExist();
                } else {
                    callBack.getShareCode(code);
                }
            }
        });
    }

    public interface IShareCallBack {
        void shareCodeExist();

        void getShareCode(String code);
    }
}