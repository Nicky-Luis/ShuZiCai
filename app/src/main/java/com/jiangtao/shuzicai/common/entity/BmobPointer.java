package com.jiangtao.shuzicai.common.entity;

import com.google.gson.Gson;

/**
 * Created by Nicky on 2017/2/8.
 * 比目pointer对象
 */

public class BmobPointer {

    private String __type = "Pointer";
    //表名
    private String className;
    //对象ID
    private String objectId;

    public BmobPointer(String className, String objectId) {
        this.__type = "Pointer";
        this.className = className;
        this.objectId = objectId;
    }

    public BmobPointer() {
    }

    public String get__type() {
        return __type;
    }

    public String getClassName() {
        return className;
    }

    public String getObjectId() {
        return objectId;
    }

    public void set__type(String __type) {
        this.__type = __type;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
