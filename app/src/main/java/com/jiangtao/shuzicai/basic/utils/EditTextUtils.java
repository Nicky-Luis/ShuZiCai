package com.jiangtao.shuzicai.basic.utils;

import android.widget.EditText;

/**
 * Created by Nicky on 2017/1/21.
 * EditText 的工具类
 */

public class EditTextUtils {

    /**
     * 获取内容
     *
     * @param edt
     * @return
     */
    public static String getContent(EditText edt) {
        return edt.getText().toString().trim();
    }

    /**
     * 判断是否为空
     *
     * @param edt
     * @return
     */
    public static boolean isEmpty(EditText edt) {
        return getContent(edt).equals("");
    }

    /**
     * 长度是否相等
     *
     * @param edt
     * @param length
     * @return
     */
    public static boolean isLengthMatch(EditText edt, int length) {
        int size =getContent(edt).length();
        return  size== length;
    }
}
