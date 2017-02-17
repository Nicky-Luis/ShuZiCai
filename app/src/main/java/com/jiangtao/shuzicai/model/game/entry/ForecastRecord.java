package com.jiangtao.shuzicai.model.game.entry;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;

/**
 * Created by Nicky on 2017/2/16.
 * 涨跌预测记录
 */
public class ForecastRecord extends BmobObject {

    //涨
    public final static int ForecastDown = 0;
    public final static int ForecastUp = 1;
    //用户id
    private String userId;
    //押注银元数值
    private float silverValue;
    //押注时间
    private BmobDate time;
    //涨跌情况，0:跌，1：涨
    private int periodValue;
    //押注期数
    private int periodCount;
    //实际结果
    private float periodResult;
    //实际的指数
    private float indexResult;
    //获取的奖励数量
    private float rewardCount;

    public ForecastRecord(String userId, float silverValue, BmobDate time, int periodValue, int periodCount, float
            periodResult, float indexResult, float rewardCount) {
        this.userId = userId;
        this.silverValue = silverValue;
        this.time = time;
        this.periodValue = periodValue;
        this.periodCount = periodCount;
        this.periodResult = periodResult;
        this.indexResult = indexResult;
        this.rewardCount = rewardCount;
    }

    public ForecastRecord() {
        this.setTableName("ForecastRecord");
    }

    public String getUserId() {
        return userId;
    }

    public float getSilverValue() {
        return silverValue;
    }

    public BmobDate getTime() {
        return time;
    }

    public int getPeriodValue() {
        return periodValue;
    }

    public int getPeriodCount() {
        return periodCount;
    }

    public float getPeriodResult() {
        return periodResult;
    }

    public float getIndexResult() {
        return indexResult;
    }

    public float getRewardCount() {
        return rewardCount;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setSilverValue(float silverValue) {
        this.silverValue = silverValue;
    }

    public void setTime(BmobDate time) {
        this.time = time;
    }

    public void setPeriodValue(int periodValue) {
        this.periodValue = periodValue;
    }

    public void setPeriodCount(int periodCount) {
        this.periodCount = periodCount;
    }

    public void setPeriodResult(float periodResult) {
        this.periodResult = periodResult;
    }

    public void setIndexResult(float indexResult) {
        this.indexResult = indexResult;
    }

    public void setRewardCount(float rewardCount) {
        this.rewardCount = rewardCount;
    }
}
