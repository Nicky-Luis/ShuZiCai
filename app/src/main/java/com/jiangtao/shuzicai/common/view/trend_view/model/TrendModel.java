package com.jiangtao.shuzicai.common.view.trend_view.model;

import com.blankj.utilcode.utils.LogUtils;
import com.github.mikephil.charting.data.Entry;

import java.util.Calendar;

/**
 * Created by Nicky on 2017/1/15.
 * 趋势数据
 */

public class TrendModel {

    //日期列表
    private Calendar date;
    //数据
    private Entry value;
    //期数
    private int periods = 1;
    //类型
    private String type = "index";


    public TrendModel() {
    }

    //星期
    public TrendModel(Calendar date, float weekValue, float yValue) {
        LogUtils.i("weekValue:" + weekValue + " --- yValue:" + yValue);
        this.date = date;
        this.value = new Entry(weekValue, yValue);
    }

    public TrendModel(int num, float yValue) {
        this.periods = num;
        this.value = new Entry(num, yValue);
        this.type="london";
    }

    public float getYValue() {
        return value.getY();
    }

    public float getXValue() {
        return value.getX();
    }


    public Entry getEntry() {
        return value;
    }

    //获取lable
    public String getXValueLabel() {
        if (type.equals("index")) {
            //得到月，因为从0开始的，所以要加1
            int month = date.get(Calendar.MONTH) + 1;
            //得到天
            int day = date.get(Calendar.DAY_OF_MONTH);
            //LogUtils.i("------" + month + "月" + day + "日------");
            return month + "月" + day + "日";
        } else {
            return "第" + periods + "期";
        }
    }

}
