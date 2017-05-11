package com.jiangtao.shuzicai.common.view.trend_view.model;

import com.github.mikephil.charting.data.Entry;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Nicky on 2017/1/15.
 * 趋势数据
 */

public class TrendModel {

    //日期列表
    private String time = "";
    //数据
    private Entry value;
    //期数
    private int periods = 1;
    //类型
    private String type = "index";


    public TrendModel() {
    }

    //星期
    public TrendModel(String time, int count, float yValue) {
        this.time = time;
        this.value = new Entry(count, yValue);
    }

    public TrendModel(int num, float yValue) {
        this.periods = num;
        this.value = new Entry(num, yValue);
        this.type = "london";
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
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Calendar calendar = Calendar.getInstance();
            Date date = null;
            try {
                date = sdf.parse(time);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            calendar.setTime(date);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            //得到月，因为从0开始的，所以要加1
            int month = calendar.get(Calendar.MONTH) + 1;
            //得到天
            //LogUtils.i("------" + month + "月" + day + "日------");
            return month + "月" + day + "日";
        } else {
            return "第" + periods + "期";
        }
    }

}
