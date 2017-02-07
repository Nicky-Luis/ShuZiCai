package com.jiangtao.shuzicai.model.mall.entry;

import com.jiangtao.shuzicai.common.entity.BmobDate;

/**
 * Created by Nicky on 2017/2/7.
 * 订单
 */
public class Order {

    //商品id
    private String goodsId;
    //用户Id
    private String userId;
    //电话
    private String receivingPhone;
    //收货地址
    private String address;
    //下单时间
    private BmobDate orderTime;

    public void setOrderTime(BmobDate orderTime) {
        this.orderTime = orderTime;
    }

    public BmobDate getOrderTime() {
        return orderTime;
    }

    //订单的状态,0：未处理，1:已处理
    private int orderStatus;

    public Order(String goodsId, String userId, String receivingPhone, String address, BmobDate orderTime, int
            orderStatus) {
        this.goodsId = goodsId;
        this.userId = userId;
        this.receivingPhone = receivingPhone;
        this.address = address;
        this.orderTime = orderTime;
        this.orderStatus = orderStatus;
    }

    public Order() {
    }

    public String getGoodsId() {
        return goodsId;
    }

    public String getUserId() {
        return userId;
    }

    public String getReceivingPhone() {
        return receivingPhone;
    }

    public String getAddress() {
        return address;
    }


    public int getOrderStatus() {
        return orderStatus;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setReceivingPhone(String receivingPhone) {
        this.receivingPhone = receivingPhone;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }
}
