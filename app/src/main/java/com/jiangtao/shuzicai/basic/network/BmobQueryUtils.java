package com.jiangtao.shuzicai.basic.network;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Nicky on 2017/1/16.
 * 专为比目设置的查询where工具
 */

public class BmobQueryUtils {

    // 小于
    public final static String Include = "$in";
    //   不包含在数组中
    public final static String NotInclude = "$nin";
    //    这个 Key    有值
    public final static String Exists = " $exists";
    //   匹配另一个查询的返回值
    public final static String Select = "$select";
    //   排除另一个查询的返回
    public final static String DontSelect = "$dontSelect";
    //   包括所有给定的值
    public final static String All = "$all";
    //   匹配PCRE表达式
    public final static String Regex = "$regex";

    //str
    private String kayValue = "";
    //条件
    private JSONObject conditionObject;

    private BmobQueryUtils() {
        conditionObject = new JSONObject();
    }

    public static BmobQueryUtils newInstance() {
        return new BmobQueryUtils();
    }

    public BmobQueryUtils setValue(String value) {
        this.kayValue = value;
        return this;
    }

    /**
     * 大于
     *
     * @param value
     * @return
     */
    public BmobQueryUtils LessThan(int value) {
        try {
            conditionObject.put("$lt", value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * 大于
     *
     * @param value
     * @return
     */
    public BmobQueryUtils LessThanEqual(int value) {
        try {
            conditionObject.put("$lte", value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * 大于
     *
     * @param value
     * @return
     */
    public BmobQueryUtils GreaterThan(int value) {
        try {
            conditionObject.put("$gt", value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * 大于
     *
     * @param value
     * @return
     */
    public BmobQueryUtils GreaterThanEqual(int value) {
        try {
            conditionObject.put("$gte", value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * 不等于
     *
     * @param value
     * @return
     */
    public BmobQueryUtils NotEqual(int value) {
        try {
            conditionObject.put("$ne", value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * 获取编码后的值
     *
     * @return
     */
    public String buildUrlString() {
        JSONObject resultObject = new JSONObject();
        try {
            resultObject.put(this.kayValue, conditionObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultObject.toString();
    }

}
