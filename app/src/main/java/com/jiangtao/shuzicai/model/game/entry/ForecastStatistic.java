package com.jiangtao.shuzicai.model.game.entry;

import cn.bmob.v3.BmobObject;

/**
 * Created by Nicky on 2017/2/16.
 * 涨跌预测记录
 */
public class ForecastStatistic extends BmobObject {

    //实际结果
    private float price;
    //押注期数
    private int periodNum;
    //实际结果，0:跌，1：涨
    private int result;
    //赢钱总数
    private float rewardSum;
    //输钱总数
    private int loserSum;

    public ForecastStatistic(float price, int periodNum, int result, float rewardSum, int loserSum) {
        this.price = price;
        this.periodNum = periodNum;
        this.result = result;
        this.rewardSum = rewardSum;
        this.loserSum = loserSum;
    }

    public float getPrice() {
        return price;
    }

    public int getPeriodNum() {
        return periodNum;
    }

    public int getResult() {
        return result;
    }

    public float getRewardSum() {
        return rewardSum;
    }

    public int getLoserSum() {
        return loserSum;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setPeriodNum(int periodNum) {
        this.periodNum = periodNum;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public void setRewardSum(float rewardSum) {
        this.rewardSum = rewardSum;
    }

    public void setLoserSum(int loserSum) {
        this.loserSum = loserSum;
    }
}
