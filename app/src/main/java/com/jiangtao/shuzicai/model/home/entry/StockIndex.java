package com.jiangtao.shuzicai.model.home.entry;

import com.jiangtao.shuzicai.common.BmobDate;

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
    private String stock_name;
    //指数值
    private float stock_value;
    //类型
    private int stock_type;
    //日期
    private BmobDate date;
    //成交笔数
    private int turnover_count;
    //成交量
    private float turnover_volume;
    //变化量
    private float change_value;
    //变化百分比
    private float change_percent;

    public StockIndex(String stock_name, float stock_value, int stock_type, BmobDate date, int turnover_count, float
            turnover_volume, float change_value, float change_percent) {
        this.stock_name = stock_name;
        this.stock_value = stock_value;
        this.stock_type = stock_type;
        this.date = date;
        this.turnover_count = turnover_count;
        this.turnover_volume = turnover_volume;
        this.change_value = change_value;
        this.change_percent = change_percent;
    }

    public StockIndex() {
    }

    public String getStock_name() {
        return stock_name;
    }

    public float getStock_value() {
        return stock_value;
    }

    public int getStock_type() {
        return stock_type;
    }

    public BmobDate getDate() {
        return date;
    }

    public int getTurnover_count() {
        return turnover_count;
    }

    public float getTurnover_volume() {
        return turnover_volume;
    }

    public float getChange_value() {
        return change_value;
    }

    public float getChange_percent() {
        return change_percent;
    }

    public void setStock_name(String stock_name) {
        this.stock_name = stock_name;
    }

    public void setStock_value(float stock_value) {
        this.stock_value = stock_value;
    }

    public void setStock_type(int stock_type) {
        this.stock_type = stock_type;
    }

    public void setDate(BmobDate date) {
        this.date = date;
    }

    public void setTurnover_count(int turnover_count) {
        this.turnover_count = turnover_count;
    }

    public void setTurnover_volume(float turnover_volume) {
        this.turnover_volume = turnover_volume;
    }

    public void setChange_value(float change_value) {
        this.change_value = change_value;
    }

    public void setChange_percent(float change_percent) {
        this.change_percent = change_percent;
    }

    @Override
    public String toString() {
        return "StockIndex{" +
                "stock_name='" + stock_name + '\'' +
                ", stock_value=" + stock_value +
                ", stock_type=" + stock_type +
                ", date=" + date.toString() +
                ", turnover_count=" + turnover_count +
                ", turnover_volume=" + turnover_volume +
                ", change_value=" + change_value +
                ", change_percent=" + change_percent +
                '}';
    }
}
