package com.jiangtao.shuzicai.model.mall.entry;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * Created by Nicky on 2017/1/26.
 * 商品表
 */
public class Goods extends BmobObject implements Serializable {

    private static final long serialVersionUID = -7060210544600464481L;
    //名称
    private String goodsName;
    //详细描述
    private String goodsDetail;
    //价格
    private int goodsPrice;
    //图片地址
    private String goodsImgUrl;
    //库存数量
    private int inventory;
    //商品类型：0:实物商品，1：手机充值卡，2：Q币充值卡
    private int type;
    //状态是否已经被下架，0：已下架，1：在线上
    private int isOnline;
    //总销售量
    private int salesVolume;

    public Goods() {
        this.setTableName("Goods");
    }

    public Goods(String goodsName, String goodsDetail, int goodsPrice, String goodsImgUrl, int inventory, int type,
                 int isOnline) {
        this.goodsName = goodsName;
        this.goodsDetail = goodsDetail;
        this.goodsPrice = goodsPrice;
        this.goodsImgUrl = goodsImgUrl;
        this.inventory = inventory;
        this.type = type;
        this.isOnline = isOnline;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int getSalesVolume() {
        return salesVolume;
    }

    public void setSalesVolume(int salesVolume) {
        this.salesVolume = salesVolume;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public String getGoodsDetail() {
        return goodsDetail;
    }

    public int getGoodsPrice() {
        return goodsPrice;
    }

    public String getGoodsImgUrl() {
        return goodsImgUrl;
    }

    public int getInventory() {
        return inventory;
    }

    public int getType() {
        return type;
    }

    public int getIsOnline() {
        return isOnline;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public void setGoodsDetail(String goodsDetail) {
        this.goodsDetail = goodsDetail;
    }

    public void setGoodsPrice(int goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public void setGoodsImgUrl(String goodsImgUrl) {
        this.goodsImgUrl = goodsImgUrl;
    }

    public void setInventory(int inventory) {
        this.inventory = inventory;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setIsOnline(int isOnline) {
        this.isOnline = isOnline;
    }
}
