package com.jiangtao.shuzicai.basic.network;

import com.google.gson.Gson;
import com.jiangtao.shuzicai.common.entity.BmobPointer;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Nicky on 2017/1/16.
 * 专为比目设置的查询where工具
 */

public class BmobQueryUtils {

    //条件
    private JSONObject conditionObject;
    //str
    private String kayValue = "";

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
     * 等于
     *
     * @param value
     * @return
     */
    public String equal(String value) {
        JSONObject resultObject = new JSONObject();
        try {
            resultObject.put(this.kayValue, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultObject.toString();
    }

    /**
     * 等于
     *
     * @param value
     * @return
     */
    public String equal(int value) {
        JSONObject resultObject = new JSONObject();
        try {
            resultObject.put(this.kayValue, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultObject.toString();
    }

    /**
     * 等于
     *
     * @param value
     * @return
     */
    public String equal(BmobPointer value) {
        JSONObject resultObject = new JSONObject();
        try {
            resultObject.put(this.kayValue, value.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultObject.toString();
    }


    /**
     * 小于
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
     * 小于
     *
     * @param value
     * @return
     */
    public BmobQueryUtils LessThan(JSONObject value) {
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
     * 小于等于
     *
     * @param value
     * @return
     */
    public BmobQueryUtils LessThanEqual(JSONObject value) {
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
    public BmobQueryUtils GreaterThan(JSONObject value) {
        try {
            conditionObject.put("$gt", value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this;
    }


    /**
     * 大于等于
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
     * 大于等于
     *
     * @param value
     * @return
     */
    public BmobQueryUtils GreaterThanEqual(JSONObject value) {
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
     * 包含在数组中
     *
     * @param values
     * @return
     */
    public String Include(List<String> values) {
        Map<String, List<String>> map = new HashMap<>();
        map.put("$in",values);
        Gson gson = new Gson();
        String str = gson.toJson(map);

        JSONObject resultObject = new JSONObject();
        try {
            resultObject.put(this.kayValue, str);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultObject.toString();
    }


    /**
     * 不等于
     *
     * @param value
     * @return
     */
    public BmobQueryUtils NotInclude(int value) {
        try {
            conditionObject.put("$nin", value);
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
    public BmobQueryUtils Exists(int value) {
        try {
            conditionObject.put("$exists", value);
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
    public BmobQueryUtils Select(int value) {
        try {
            conditionObject.put("$select", value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this;
    }


    /**
     * 排除另一个查询的返回
     *
     * @param value
     * @return
     */
    public BmobQueryUtils DontSelect(int value) {
        try {
            conditionObject.put("$dontSelect", value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this;
    }


    /**
     * 包括所有给定的值
     *
     * @param value
     * @return
     */
    public BmobQueryUtils All(int value) {
        try {
            conditionObject.put("$all", value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * 匹配PCRE表达式
     *
     * @param value
     * @return
     */
    public BmobQueryUtils Regex(int value) {
        try {
            conditionObject.put("$regex", value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * 与
     * @param where1
     * @param where2
     * @return
     */
    public String and(String where1,String where2) {
        List<String> list =new ArrayList<>();
        list.add(where1);
        list.add(where2);

        Map<String, List<String>> map = new HashMap<>();
        map.put("$and",list);
        Gson gson = new Gson();
        String str = gson.toJson(map);
        return str;
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

    /**
     * 获取编码后的值
     *
     * @return
     */
    public static JSONObject getBmobDate(Calendar calendar) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        String Date = format.format(calendar.getTime());

        JSONObject resultObject = new JSONObject();
        try {
            resultObject.put("__type", "Date");
            resultObject.put("iso", Date);
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            return resultObject;
        }
    }
}
