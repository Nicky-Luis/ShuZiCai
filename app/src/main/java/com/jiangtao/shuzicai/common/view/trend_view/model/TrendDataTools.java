package com.jiangtao.shuzicai.common.view.trend_view.model;

import com.jiangtao.shuzicai.model.game.entry.LondonGold;
import com.jiangtao.shuzicai.model.home.entry.StockIndex;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by Nicky on 2017/1/15.
 * 工具
 */

public class TrendDataTools {

    /**
     * 获取最小的值
     *
     * @param datas
     * @return
     */
    public static float getMinData(List<TrendModel> datas,int amplitude) {
        if (null == datas || datas.size() <= 0) {
            return 1000f;
        }

        List<Float> nums = new ArrayList<>();
        for (TrendModel data : datas) {
            //LogUtils.i("-----:" + data.getYValue());
            nums.add(data.getYValue());
        }
        return Collections.min(nums) - amplitude;
    }

    /**
     * 获取最大的值
     *
     * @param datas
     * @return
     */
    public static float getMaxData(List<TrendModel> datas,int amplitude) {
        if (null == datas || datas.size() <= 0) {
            return 2000f;
        }
        List<Float> nums = new ArrayList<>();
        for (TrendModel data : datas) {
            nums.add(data.getYValue());
        }
        return Collections.max(nums) + amplitude;
    }

    /**
     * 获取最早的日期
     *
     * @param datas
     * @return
     */
    public static float getEarliestData(List<TrendModel> datas) {
        if (null == datas || datas.size() <= 0) {
            return 2000f;
        }
        List<Float> nums = new ArrayList<>();
        for (TrendModel data : datas) {
            nums.add(data.getXValue());
        }
        return Collections.min(nums);
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
            //LogUtils.i("data.getXValue()=" + data.getXValue());
            // LogUtils.i("value=" + value);
            if (data.getXValue() == value) {
                return data.getXValueLabel();
            }
        }
        return result;
    }

    /**
     * 数据解析，将股票数据转化为图表数据
     *
     * @param type
     * @param retList
     * @return
     */
    public static List<TrendModel> getTrendDatas(String type, List<StockIndex> retList) {
        //交易数据
        List<TrendModel> trendModels = new ArrayList<>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            if (null != retList) {
                //解析数据
                for (StockIndex data : retList) {
                    if (data.getCode().equals(type)) {
                        Calendar calendar = Calendar.getInstance();
                        Date date = sdf.parse(data.getTime());
                        calendar.setTime(date);
                        float day = calendar.get(Calendar.DAY_OF_MONTH);
                        float price = Float.valueOf(data.getNowPrice());
                        float resultPrice = new BigDecimal(price).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
                        TrendModel model = new TrendModel(calendar, day, resultPrice);
                        trendModels.add(model);
                    }
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return trendModels;
    }


    /**
     * 数据解析，将股票数据转化为图表数据 显示为伦敦金
     *
     * @param newestNum
     * @param retList
     * @return
     */
    public static List<TrendModel> getTrendDatas2(int newestNum, List<LondonGold> retList) {
        //交易数据
        List<TrendModel> trendModels = new ArrayList<>();
        //解析数据
        for (LondonGold data : retList) {
            float price = Float.valueOf(data.getLatestpri());
            TrendModel model = new TrendModel(newestNum--, price);
            trendModels.add(model);
        }
        return trendModels;
    }


    /**
     * 数据解析，将股票数据转化为图表数据
     *
     * @param type
     * @param retList
     * @return
     */
    public static StockIndex getNewestDatas(String type, List<StockIndex> retList) {
        if (null == retList || retList.size() == 0) {
            return null;
        }
        for (int index = retList.size() - 1; index >= 0; index--) {
            if (retList.get(index).getCode().equals(type)) {
                return retList.get(index);
            }
        }
        return null;
    }


    //测试数据数据
    public static List<TrendModel> getTrendDatas() {

        List<TrendModel> trendDatas = new ArrayList<>();
       /* Calendar date1 = Calendar.getInstance();
        date1.set(2017, 1, 1, 0, 0, 0);
        TrendModel model1 = new TrendModel(date1, 3366.852f);
        trendDatas.add(model1);

        Calendar date2 = Calendar.getInstance();
        date2.set(2017, 1, 2, 0, 0, 0);
        TrendModel model2 = new TrendModel(date2, 3345.748f);
        trendDatas.add(model2);

        Calendar date3 = Calendar.getInstance();
        date3.set(2017, 1, 3, 0, 0, 0);
        TrendModel model3 = new TrendModel(date3, 3361.639f);
        trendDatas.add(model3);

        Calendar date4 = Calendar.getInstance();
        date4.set(2017, 1, 4, 0, 0, 0);
        TrendModel model4 = new TrendModel(date4, 3355.801f);
        trendDatas.add(model4);

        Calendar date5 = Calendar.getInstance();
        date5.set(2017, 1, 5, 0, 0, 0);
        TrendModel model5 = new TrendModel(date5, 3332.686f);
        trendDatas.add(model5);
*/
        return trendDatas;
    }
}
