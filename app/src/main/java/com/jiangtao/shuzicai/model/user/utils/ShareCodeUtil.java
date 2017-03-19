package com.jiangtao.shuzicai.model.user.utils;

import com.jiangtao.shuzicai.model.user.entry._User;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

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
        BmobQuery<_User> query = new BmobQuery<_User>();
        query.addWhereEqualTo("InvitationCode", code);
        query.findObjects(new FindListener<_User>() {
            @Override
            public void done(List<_User> list, BmobException e) {
                if (e == null) {
                    if (null != list) {
                        if (list.size() > 0) {
                            callBack.shareCodeExist();
                        } else {
                            callBack.getShareCode(code);
                        }
                    }else {
                        callBack.shareCodeExist();
                    }
                } else {
                    callBack.shareCodeExist();
                }
            }
        });
    }

    public interface IShareCallBack {
        void shareCodeExist();

        void getShareCode(String code);
    }
}