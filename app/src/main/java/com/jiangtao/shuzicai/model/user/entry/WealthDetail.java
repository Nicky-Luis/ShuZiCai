package com.jiangtao.shuzicai.model.user.entry;

import cn.bmob.v3.BmobObject;

/**
 * Created by Nicky on 2017/2/12.
 */

public class WealthDetail extends BmobObject {

    //伙伴类型
    public final static int Currency_Type_Gold = 0;
    public final static int Currency_Type_Silver = 1;
    //操作类型
    public final static int Operation_Type_Recharge = 0;//充值
    public final static int Operation_Type_Wealth_Exchange = 1;//兑换成银元
    public final static int Operation_Type_Good_Exchange = 2;//兑换成商品
    public final static int Operation_Type_Forecast_Reward = 3;//涨跌中奖
    public final static int Operation_Type_Mantisssa_Reward = 4;//尾数中奖
    public final static int Operation_Type_Whole_Reward = 5;//全数中奖
    public final static int Operation_Type_Game_Forecast = 6;//涨跌消耗
    public final static int Operation_Type_Game_Mantisssa = 7;//尾数消耗
    public final static int Operation_Type_Game_Whole = 8;//全数消耗
    public final static int Operation_Type_Invite_First = 9;//初级奖励
    public final static int Operation_Type_Invite_Second = 10;//次级奖励
    public final static int Operation_Type_Invite_Third = 11;//三级奖励

    //用户id
    private String userId;
    //变化之前的值
    private int beforeValue;
    //变化之后的值
    private int afterValue;
    //操作的货币类型,0:金币，1：银币
    private int currencyType;
    //操作的类型，0：充值，1：兑换消耗，2，中奖，3：游戏消耗，4：转换
    private int operationType;
    //操作的数值
    private int operationValue;
    //用户是否已经同步更新
    private int flag;

    public WealthDetail(String userId, int beforeValue, int afterValue, int currencyType, int operationType, int
            operationValue, int flag) {
        this.userId = userId;
        this.beforeValue = beforeValue;
        this.afterValue = afterValue;
        this.currencyType = currencyType;
        this.operationType = operationType;
        this.operationValue = operationValue;
        this.flag = flag;
    }

    public WealthDetail() {
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public int getBeforeValue() {
        return beforeValue;
    }

    public int getAfterValue() {
        return afterValue;
    }

    public int getCurrencyType() {
        return currencyType;
    }

    public int getOperationType() {
        return operationType;
    }

    public int getOperationValue() {
        return operationValue;
    }

    public String getUserId() {
        return userId;
    }

    public void setBeforeValue(int beforeValue) {
        this.beforeValue = beforeValue;
    }

    public void setAfterValue(int afterValue) {
        this.afterValue = afterValue;
    }

    public void setCurrencyType(int currencyType) {
        this.currencyType = currencyType;
    }

    public void setOperationType(int operationType) {
        this.operationType = operationType;
    }

    public void setOperationValue(int operationValue) {
        this.operationValue = operationValue;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
