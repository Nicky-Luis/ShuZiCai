package com.jiangtao.shuzicai.common.view.billboard_view.model;

import com.jiangtao.shuzicai.R;
import com.jiangtao.shuzicai.common.BmobDate;

/**
 * Created by Nicky on 2017/1/14.
 */

public class BillboardMessage {

    //1、注册充值类型，2、奖励金币，3、奖励银元，4、投注金币，5投注银元
    public final static int Type_Join = 1;
    public final static int Type_Awards_Gold = 2;
    public final static int Type_Awards_Silver = 3;
    public final static int Type_Bet_Gold = 4;
    public final static int Type_Bet_Silver = 5;


    //面板人物
    private String UserName;
    //面板人物
    private String UserID;
    //时间
    private BmobDate Date;
    //数值
    private float BillboardValue;
    //操作类型
    private int BillboardType;

    public BillboardMessage(String userName, String userID, BmobDate date, float billboardValue, int billboardType) {
        UserName = userName;
        UserID = userID;
        Date = date;
        BillboardValue = billboardValue;
        BillboardType = billboardType;
    }

    public BillboardMessage() {
    }

    public String getUserName() {
        return UserName;
    }

    public String getUserID() {
        return UserID;
    }

    public BmobDate getDate() {
        return Date;
    }

    public float getBillboardValue() {
        return BillboardValue;
    }

    public int getBillboardType() {
        return BillboardType;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public void setDate(BmobDate date) {
        Date = date;
    }

    public void setBillboardValue(float billboardValue) {
        BillboardValue = billboardValue;
    }

    public void setBillboardType(int billboardType) {
        BillboardType = billboardType;
    }

    ////////////////////////////////////////////////////////////////////

    /**
     * 获取图片资源
     *
     * @return
     */
    public int getMessageImageRes() {
        int res = R.mipmap.view_broadcastscrollview_notice;
        switch (BillboardType) {
            case Type_Join:
                res = R.mipmap.view_broadcastscrollview_notice;
                break;

            case Type_Awards_Gold:
                res = R.mipmap.view_broadcastscrollview_notice;
                break;

            case Type_Awards_Silver:
                res = R.mipmap.view_broadcastscrollview_notice;
                break;

            case Type_Bet_Gold:
                res = R.mipmap.view_broadcastscrollview_suprise;

            case Type_Bet_Silver:
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
        String content = "";
        switch (BillboardType) {
            case Type_Join:
                content = "欢迎用户" + UserName + "加入";
                break;

            case Type_Awards_Gold:
                content = "恭喜用户" + UserName + "赢得了" + BillboardValue + "金币";
                break;

            case Type_Awards_Silver:
                content = "恭喜用户" + UserName + "赢得了" + BillboardValue + "银元";
                break;

            case Type_Bet_Gold:
                content = "用户" + UserName + "投注了" + BillboardValue + "金币";
                break;

            case Type_Bet_Silver:
                content = "用户" + UserName + "投注了" + BillboardValue + "银元";
                break;
        }
        // LogUtils.i("数据=" + content);
        return content;
    }
}
