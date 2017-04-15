package com.jiangtao.shuzicai.model.game.entry;

import cn.bmob.v3.BmobObject;

/**
 * Created by Nicky on 2017/3/25.
 */
public class LondonGold extends BmobObject {

    private int periodsNum;//期数
    private String type;// 黄金美元
    private String price;// 1243.80
    private String changepercent;// -0.15%
    private String changequantity;// -1.5
    private String openingprice;// 1245.61
    private String maxprice;// 1251.86
    private String minprice;// 1240.35
    private String lastclosingprice;// 1245.30
    private String amplitude;// .93
    private String buyprice;// 1244.55
    private String sellprice;// 1243.80
    private String updatetime;// 2017-03-25 4:59:00 

    public LondonGold(int newestNum, String type, String price, String changepercent, String changequantity, String
            openingprice, String maxprice, String minprice, String lastclosingprice, String amplitude, String
                              buyprice, String sellprice, String updatetime) {
        this.periodsNum = newestNum;
        this.type = type;
        this.price = price;
        this.changepercent = changepercent;
        this.changequantity = changequantity;
        this.openingprice = openingprice;
        this.maxprice = maxprice;
        this.minprice = minprice;
        this.lastclosingprice = lastclosingprice;
        this.amplitude = amplitude;
        this.buyprice = buyprice;
        this.sellprice = sellprice;
        this.updatetime = updatetime;
    }

    public LondonGold() {
    }

    public int getPeriodsNum() {
        return periodsNum;
    }

    public String getType() {
        return type;
    }

    public String getPrice() {
        return price;
    }

    public String getChangepercent() {
        return changepercent;
    }

    public String getChangequantity() {
        return changequantity;
    }

    public String getOpeningprice() {
        return openingprice;
    }

    public String getMaxprice() {
        return maxprice;
    }

    public String getMinprice() {
        return minprice;
    }

    public String getLastclosingprice() {
        return lastclosingprice;
    }

    public String getAmplitude() {
        return amplitude;
    }

    public String getBuyprice() {
        return buyprice;
    }

    public String getSellprice() {
        return sellprice;
    }

    public String getUpdatetime() {
        return updatetime;
    }


    public void setPeriodsNum(int periodsNum) {
        this.periodsNum = periodsNum;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setChangepercent(String changepercent) {
        this.changepercent = changepercent;
    }

    public void setChangequantity(String changequantity) {
        this.changequantity = changequantity;
    }

    public void setOpeningprice(String openingprice) {
        this.openingprice = openingprice;
    }

    public void setMaxprice(String maxprice) {
        this.maxprice = maxprice;
    }

    public void setMinprice(String minprice) {
        this.minprice = minprice;
    }

    public void setLastclosingprice(String lastclosingprice) {
        this.lastclosingprice = lastclosingprice;
    }

    public void setAmplitude(String amplitude) {
        this.amplitude = amplitude;
    }

    public void setBuyprice(String buyprice) {
        this.buyprice = buyprice;
    }

    public void setSellprice(String sellprice) {
        this.sellprice = sellprice;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }
}
