package com.jiangtao.shuzicai.common.view.trend_view.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

/**
 * Created by Nicky on 2017/1/15.
 * 工具
 */

public class DataTools {

    /**
     * 获取最小的值
     *
     * @param datas
     * @return
     */
    public static float getMinData(List<TrendModel> datas) {
        if (null == datas) {
            return 1000f;
        }
        List<Float> nums = new ArrayList<>();
        for (TrendModel data : datas) {
            nums.add(data.getYValue());
        }
        return Collections.min(nums) - 10;
    }

    /**
     * 获取最大的值
     *
     * @param datas
     * @return
     */
    public static float getMaxData(List<TrendModel> datas) {
        if (null == datas) {
            return 2000f;
        }
        List<Float> nums = new ArrayList<>();
        for (TrendModel data : datas) {
            nums.add(data.getYValue());
        }
        return Collections.max(nums) + 10;
    }


    /**
     * 获取X轴的lable
     *
     * @param datas
     * @return
     */
    public static String getDataLable(List<TrendModel> datas, float value) {
        String result = "1月1日";
        if (null == datas) {
            return result;
        }
        for (TrendModel data : datas) {
            if (data.getXValue() == value) {
                return data.getXValueLabel();
            }
        }
        return result;
    }

    //测试数据数据
    public static List<TrendModel> getTrendDatas() {
        List<TrendModel> trendDatas = new ArrayList<>();
        Calendar date1 = Calendar.getInstance();
        date1.set(2017, 1, 1, 0, 0, 0);
        TrendModel model1 = new TrendModel(date1, TrendModel.Mon, 3366.852f);
        trendDatas.add(model1);

        Calendar date2 = Calendar.getInstance();
        date2.set(2017, 1, 2, 0, 0, 0);
        TrendModel model2 = new TrendModel(date2, TrendModel.Tues, 3345.748f);
        trendDatas.add(model2);

        Calendar date3 = Calendar.getInstance();
        date3.set(2017, 1, 3, 0, 0, 0);
        TrendModel model3 = new TrendModel(date3, TrendModel.Wed, 3361.639f);
        trendDatas.add(model3);

        Calendar date4 = Calendar.getInstance();
        date4.set(2017, 1, 4, 0, 0, 0);
        TrendModel model4 = new TrendModel(date4, TrendModel.Thur, 3355.801f);
        trendDatas.add(model4);

        Calendar date5 = Calendar.getInstance();
        date5.set(2017, 1, 5, 0, 0, 0);
        TrendModel model5 = new TrendModel(date5, TrendModel.Fri, 3332.686f);
        trendDatas.add(model5);

        return trendDatas;
    }
}
