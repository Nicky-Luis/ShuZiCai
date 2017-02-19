package com.jiangtao.shuzicai.model.game.entry;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;

/**
 * Created by Nicky on 2017/2/19.
 * 全数猜测记录
 */

public class GuessWholeRecord extends BmobObject {

    //用户id
    private String userId;
    //押注金币数值
    private float goldValue;
    //押注时间
    private BmobDate time;
    //押注期数
    private int periodNum;
    //押注的数据
    private float guessValue;
    //实际的指数
    private float indexResult;
    //是否中奖
    private boolean isReward;
    //获取的奖励数量
    private float rewardCount;

    public GuessWholeRecord(String userId, float goldValue, BmobDate time, int periodNum, float guessValue, float
            indexResult, boolean isReward, float rewardCount) {
        this.userId = userId;
        this.goldValue = goldValue;
        this.time = time;
        this.periodNum = periodNum;
        this.guessValue = guessValue;
        this.indexResult = indexResult;
        this.isReward = isReward;
        this.rewardCount = rewardCount;
    }

    public GuessWholeRecord() {
    }

    public String getUserId() {
        return userId;
    }

    public float getGoldValue() {
        return goldValue;
    }

    public BmobDate getTime() {
        return time;
    }

    public int getPeriodNum() {
        return periodNum;
    }

    public float getGuessValue() {
        return guessValue;
    }

    public float getIndexResult() {
        return indexResult;
    }

    public boolean isReward() {
        return isReward;
    }

    public float getRewardCount() {
        return rewardCount;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setGoldValue(float goldValue) {
        this.goldValue = goldValue;
    }

    public void setTime(BmobDate time) {
        this.time = time;
    }

    public void setPeriodNum(int periodNum) {
        this.periodNum = periodNum;
    }

    public void setGuessValue(float guessValue) {
        this.guessValue = guessValue;
    }

    public void setIndexResult(float indexResult) {
        this.indexResult = indexResult;
    }

    public void setReward(boolean reward) {
        isReward = reward;
    }

    public void setRewardCount(float rewardCount) {
        this.rewardCount = rewardCount;
    }
}
