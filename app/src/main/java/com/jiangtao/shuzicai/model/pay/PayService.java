package com.jiangtao.shuzicai.model.pay;

import android.app.Activity;
import android.util.Log;

public class PayService {


    /**
     * 支付宝支付业务
     *
     * @param activity
     * @param orderInfo
     */
    public static void payV2(final Activity activity, final String orderInfo) {
        Log.i("", "数据：" + orderInfo);

    }
}
