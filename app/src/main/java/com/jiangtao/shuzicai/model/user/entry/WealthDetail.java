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
    public final static int Operation_Type_Recharge = 0;
    public final static int Operation_Type_Exchange = 1;
    public final static int Operation_Type_Reward = 2;
    public final static int Operation_Type_Game = 3;
    public final static int Operation_Type_Conversion = 4;
    //用户id
    private String userId;
    //变化之前的值
    private float beforeValue;
    //变化之后的值
    private float afterValue;
    //操作的货币类型,0:金币，1：银币
    private int currencyType;
    //操作的类型，0：充值，1：兑换消耗，2，中奖，3：游戏消耗，4：转换
    private int operationType;
    //操作的数值
    private float operationValue;

    public WealthDetail(float beforeValue, float afterValue, int currencyType, int operationType, float
            operationValue, String userId) {
        this.beforeValue = beforeValue;
        this.afterValue = afterValue;
        this.currencyType = currencyType;
        this.operationType = operationType;
        this.operationValue = operationValue;
        this.userId = userId;
    }

    public WealthDetail() {
    }

    public float getBeforeValue() {
        return beforeValue;
    }

    public float getAfterValue() {
        return afterValue;
    }

    public int getCurrencyType() {
        return currencyType;
    }

    public int getOperationType() {
        return operationType;
    }

    public float getOperationValue() {
        return operationValue;
    }

    public String getUserId() {
        return userId;
    }

    public void setBeforeValue(float beforeValue) {
        this.beforeValue = beforeValue;
    }

    public void setAfterValue(float afterValue) {
        this.afterValue = afterValue;
    }

    public void setCurrencyType(int currencyType) {
        this.currencyType = currencyType;
    }

    public void setOperationType(int operationType) {
        this.operationType = operationType;
    }

    public void setOperationValue(float operationValue) {
        this.operationValue = operationValue;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
