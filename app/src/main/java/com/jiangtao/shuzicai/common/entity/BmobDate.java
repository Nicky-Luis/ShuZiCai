package com.jiangtao.shuzicai.common.entity;

/**
 * Created by Nicky on 2017/1/17.
 */

public class BmobDate {
    //类型
    private String __type;
    //时间值
    private String iso;

    public BmobDate(String __type, String iso) {
        this.__type = __type;
        this.iso = iso;
    }

    public BmobDate() {
    }

    public String get__type() {
        return __type;
    }

    public String getIso() {
        return iso;
    }

    public void set__type(String __type) {
        this.__type = __type;
    }

    public void setIso(String iso) {
        this.iso = iso;
    }

    @Override
    public String toString() {
        return "BmobDate{" +
                "__type='" + __type + '\'' +
                ", iso='" + iso + '\'' +
                '}';
    }
}
