package com.jiangtao.shuzicai.model.home.entry;

import cn.bmob.v3.BmobObject;

/**
 * Created by Nicky on 2017/1/16.
 * 指数model
 */

public class StockIndex extends BmobObject {

    //上证指数，深证成指,创业板指,沪深300
    public final static String Type_ShangZheng = "sh000001";
    public final static String Type_ShenZheng = "sz399001";
    public final static String Type_chuangYe = "sz399006";
    public final static String Type_HuShen = "sz399300";
    public final static String Type_London = "London";
    //sh000001,sz399001,sz399006,sz399300
    //昨日关盘日期
    private String yestodayClosePrice;
    private String todayMax;
    private String todayMin;
    private String max52;
    //变化值
    private String diff_money;
    //交易量
    private String tradeNum;
    //股票代码
    private String code;
    //最大价格
    private String maxPrice;
    //当前价格
    private String nowPrice;
    private String min52;
    private String time;
    private String name;
    //交易总额
    private String tradeAmount;
    //摇荡
    private String swing;
    private String todayOpenPrice;
    private String diff_rate;
    private String minPrice;

    public StockIndex() {
    }

    public StockIndex(String yestodayClosePrice, String todayMax, String todayMin, String max52,
                      String diff_money, String tradeNum, String code, String maxPrice, String nowPrice, String
                              min52, String time, String name, String tradeAmount, String swing, String
                              todayOpenPrice, String diff_rate, String minPrice) {
        this.yestodayClosePrice = yestodayClosePrice;
        this.todayMax = todayMax;
        this.todayMin = todayMin;
        this.max52 = max52;
        this.diff_money = diff_money;
        this.tradeNum = tradeNum;
        this.code = code;
        this.maxPrice = maxPrice;
        this.nowPrice = nowPrice;
        this.min52 = min52;
        this.time = time;
        this.name = name;
        this.tradeAmount = tradeAmount;
        this.swing = swing;
        this.todayOpenPrice = todayOpenPrice;
        this.diff_rate = diff_rate;
        this.minPrice = minPrice;
    }


    public String getYestodayClosePrice() {
        return yestodayClosePrice;
    }

    public String getTodayMax() {
        return todayMax;
    }

    public String getTodayMin() {
        return todayMin;
    }

    public String getMax52() {
        return max52;
    }

    public String getDiff_money() {
        return diff_money;
    }

    public String getTradeNum() {
        return tradeNum;
    }

    public String getCode() {
        return code;
    }

    public String getMaxPrice() {
        return maxPrice;
    }

    public String getNowPrice() {
        return nowPrice;
    }

    public String getMin52() {
        return min52;
    }

    public String getTime() {
        return time;
    }

    public String getName() {
        return name;
    }

    public String getTradeAmount() {
        return tradeAmount;
    }

    public String getSwing() {
        return swing;
    }

    public String getTodayOpenPrice() {
        return todayOpenPrice;
    }

    public String getDiff_rate() {
        return diff_rate;
    }

    public String getMinPrice() {
        return minPrice;
    }


    public void setYestodayClosePrice(String yestodayClosePrice) {
        this.yestodayClosePrice = yestodayClosePrice;
    }

    public void setTodayMax(String todayMax) {
        this.todayMax = todayMax;
    }

    public void setTodayMin(String todayMin) {
        this.todayMin = todayMin;
    }

    public void setMax52(String max52) {
        this.max52 = max52;
    }

    public void setDiff_money(String diff_money) {
        this.diff_money = diff_money;
    }

    public void setTradeNum(String tradeNum) {
        this.tradeNum = tradeNum;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setMaxPrice(String maxPrice) {
        this.maxPrice = maxPrice;
    }

    public void setNowPrice(String nowPrice) {
        this.nowPrice = nowPrice;
    }

    public void setMin52(String min52) {
        this.min52 = min52;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTradeAmount(String tradeAmount) {
        this.tradeAmount = tradeAmount;
    }

    public void setSwing(String swing) {
        this.swing = swing;
    }

    public void setTodayOpenPrice(String todayOpenPrice) {
        this.todayOpenPrice = todayOpenPrice;
    }

    public void setDiff_rate(String diff_rate) {
        this.diff_rate = diff_rate;
    }

    public void setMinPrice(String minPrice) {
        this.minPrice = minPrice;
    }
}
