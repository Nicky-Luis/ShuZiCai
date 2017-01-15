package com.jiangtao.shuzicai.common.view.billboard_view.model;

import com.jiangtao.shuzicai.R;

import java.util.Date;

/**
 * Created by Nicky on 2017/1/14.
 */

public class ScrollMessage {

    //1、注册充值类型，2、中奖类型，3、投注类型
    public final static int Type_Join = 0;
    public final static int Type_Awards = 1;
    public final static int Type_Bet = 2;
    //1、银元，2、金币
    private final static int Unit_Silver = 0;
    private final static int Unit_Gold = 1;

    //面板人物
    private String personName;
    //类型
    private int messageType;
    //时间
    private Date data;
    //数值
    private float value;
    //操作单位
    private int valueUnit;

    /**
     * 构造函数
     *
     * @param value       操作值
     * @param personName  用户名
     * @param messageType 数据类型
     * @param data        时间
     */
    public ScrollMessage(float value, String personName, int messageType, Date data) {
        this.value = value;
        this.personName = personName;
        this.messageType = messageType;
        this.data = data;
    }

    /**
     * 获取图片资源
     *
     * @return
     */
    public int getMessageImageRes() {
        int res = R.mipmap.view_broadcastscrollview_notice;
        switch (this.messageType) {
            case Type_Join:
                res = R.mipmap.view_broadcastscrollview_notice;
                break;

            case Type_Awards:
                res = R.mipmap.view_broadcastscrollview_notice;
                break;

            case Type_Bet:
                res = R.mipmap.view_broadcastscrollview_suprise;
                break;
        }
        return res;
    }

    /**
     * 获取通知内容
     *
     * @return
     */
    public String getMessageContent() {
        String res = "";
        String unit = Unit_Silver == valueUnit ? "银元" : "金币";
        switch (this.messageType) {
            case Type_Join:
                res = "欢迎用户" + personName + "加入";
                break;

            case Type_Awards:
                res = "恭喜用户" + personName + "赢得了" + value + unit;
                break;

            case Type_Bet:
                res = "用户" + personName + "投注了" + value + unit;
                break;
        }
      //  LogUtils.i("数据=" + res);
        return res;
    }
}
