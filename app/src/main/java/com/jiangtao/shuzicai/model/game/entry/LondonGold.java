package com.jiangtao.shuzicai.model.game.entry;

import cn.bmob.v3.BmobObject;

/**
 * Created by Nicky on 2017/3/25.
 * 伦敦金
 */
public class LondonGold extends BmobObject {

    private int periodsNum;//期数
    private String name;//现货黄金 ,
    private String latestpri;//1228.4最新价
    private String change;//-9.41 ,
    private String limit;//-0.77 ,
    private String buypri;//1228.4 ,
    private String sellpri;//1229.2 ,
    private String openpri;//1237.88 ,
    private String yespri;//1237.81 ,
    private String maxpri;//1241.27 ,
    private String minpri;//1228.4 ,
    private String time;//2017-05-04 23:34:57

    public LondonGold(int periodsNum, String name, String latestpri, String change, String limit, String buypri,
                      String sellpri, String openpri, String yespri, String maxpri, String minpri, String time) {
        this.periodsNum = periodsNum;
        this.name = name;
        this.latestpri = latestpri;
        this.change = change;
        this.limit = limit;
        this.buypri = buypri;
        this.sellpri = sellpri;
        this.openpri = openpri;
        this.yespri = yespri;
        this.maxpri = maxpri;
        this.minpri = minpri;
        this.time = time;
    }

    public int getPeriodsNum() {
        return periodsNum;
    }

    public String getName() {
        return name;
    }

    public String getLatestpri() {
        return latestpri;
    }

    public String getChange() {
        return change;
    }

    public String getLimit() {
        return limit;
    }

    public String getBuypri() {
        return buypri;
    }

    public String getSellpri() {
        return sellpri;
    }

    public String getOpenpri() {
        return openpri;
    }

    public String getYespri() {
        return yespri;
    }

    public String getMaxpri() {
        return maxpri;
    }

    public String getMinpri() {
        return minpri;
    }

    public String getTime() {
        return time;
    }

    public void setPeriodsNum(int periodsNum) {
        this.periodsNum = periodsNum;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLatestpri(String latestpri) {
        this.latestpri = latestpri;
    }

    public void setChange(String change) {
        this.change = change;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public void setBuypri(String buypri) {
        this.buypri = buypri;
    }

    public void setSellpri(String sellpri) {
        this.sellpri = sellpri;
    }

    public void setOpenpri(String openpri) {
        this.openpri = openpri;
    }

    public void setYespri(String yespri) {
        this.yespri = yespri;
    }

    public void setMaxpri(String maxpri) {
        this.maxpri = maxpri;
    }

    public void setMinpri(String minpri) {
        this.minpri = minpri;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
