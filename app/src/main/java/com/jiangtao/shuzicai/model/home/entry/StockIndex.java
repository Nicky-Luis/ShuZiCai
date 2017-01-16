package com.jiangtao.shuzicai.model.home.entry;

import java.util.Date;

/**
 * Created by Nicky on 2017/1/16.
 * 指数model
 */

public class StockIndex {

    //指数类型
    public final static int Type_ShangZheng = 1;
    public final static int Type_ShenZheng = 2;
    public final static int Type_HuShen = 3;
    public final static int Type_chuangYe = 4;

    //指数名称
    private String stockName;
    //指数值
    private float stockValue;
    //类型
    private int stockType;
    //日期
    private Date date;
    //成交笔数
    private int turnoverCount;
    //成交量
    private float turnoverVolume;
    //变化量
    private float changeValue;
    //变化百分比
    private float changePercent;

    public StockIndex(String stockName, float stockValue, int stockType, Date date, int turnoverCount, float
            turnoverVolume, float changeValue, float changePercent) {
        this.stockName = stockName;
        this.stockValue = stockValue;
        this.stockType = stockType;
        this.date = date;
        this.turnoverCount = turnoverCount;
        this.turnoverVolume = turnoverVolume;
        this.changeValue = changeValue;
        this.changePercent = changePercent;
    }

    public String getStockName() {
        return stockName;
    }

    public float getStockValue() {
        return stockValue;
    }

    public int getStockType() {
        return stockType;
    }

    public Date getDate() {
        return date;
    }

    public int getTurnoverCount() {
        return turnoverCount;
    }

    public float getTurnoverVolume() {
        return turnoverVolume;
    }

    public float getChangeValue() {
        return changeValue;
    }

    public float getChangePercent() {
        return changePercent;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public void setStockValue(float stockValue) {
        this.stockValue = stockValue;
    }

    public void setStockType(int stockType) {
        this.stockType = stockType;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setTurnoverCount(int turnoverCount) {
        this.turnoverCount = turnoverCount;
    }

    public void setTurnoverVolume(float turnoverVolume) {
        this.turnoverVolume = turnoverVolume;
    }

    public void setChangeValue(float changeValue) {
        this.changeValue = changeValue;
    }

    public void setChangePercent(float changePercent) {
        this.changePercent = changePercent;
    }

    @Override
    public String toString() {
        return "StockIndex{" +
                "stockName='" + stockName + '\'' +
                ", stockValue=" + stockValue +
                ", stockType=" + stockType +
                ", date=" + date +
                ", turnoverCount=" + turnoverCount +
                ", turnoverVolume=" + turnoverVolume +
                ", changeValue=" + changeValue +
                ", changePercent=" + changePercent +
                '}';
    }
}
