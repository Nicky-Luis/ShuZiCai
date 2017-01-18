package com.jiangtao.shuzicai.common.view.trend_view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.jiangtao.shuzicai.R;
import com.jiangtao.shuzicai.common.view.trend_view.model.TrendDataTools;
import com.jiangtao.shuzicai.common.view.trend_view.model.TrendModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Nicky on 2017/1/15.
 * 趋势图
 */

public class TrendView extends RelativeLayout {

    //mContext
    private Context mContext;
    //所有的数据源
    private List<TrendModel> trendModels;
    //曲线图对象
    private LineChart trendView;
    //表的名字
    private String name;

    public TrendView(Context context) {
        super(context);
        constructView(context);
    }

    public TrendView(Context context, AttributeSet attrs) {
        super(context, attrs);
        constructView(context);
    }

    public TrendView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        constructView(context);
    }

    /**
     * 构造view
     *
     * @param context
     */
    private void constructView(Context context) {
        this.mContext = context;
        this.name = "沪深300";
        // 导入布局
        View rootView = LayoutInflater.from(context).inflate(R.layout.view_trendview_layout, this, true);
        initView(rootView);
    }

    /**
     * 初始化
     */
    private void initView(View rootView) {
        //listView
        trendView = (LineChart) rootView.findViewById(R.id.trend_line_chart);
        trendView.setBackgroundResource(R.color.wheat);
        trendView.setDescription(null);//描述
        trendView.setTouchEnabled(true); //启用/禁用与图表的所有可能的触摸交互。
        trendView.setDragEnabled(false); //是否允许拖拽图表
        trendView.setScaleEnabled(false);//是否允许缩放图表
        trendView.setScaleXEnabled(false);//是否允许缩放X轴比例
        trendView.setScaleYEnabled(false); //是否允许缩放Y轴比例
    }

    /**
     * X轴样式设置
     */
    private void initXAxisLine() {
        XAxis xAxis = trendView.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // 设置X轴的位置
        xAxis.setEnabled(true);// 设置轴启用或禁用
        xAxis.setDrawGridLines(false);//是否绘制网格
        xAxis.setDrawAxisLine(false);//是否绘制轴线
        xAxis.setTextColor(Color.BLACK);//X轴上字体的颜色
        xAxis.setAxisLineColor(Color.BLACK);//X轴的颜色
        xAxis.setDrawLabels(true);//X轴的坐标标签
        xAxis.setAxisLineWidth(1f);//X轴的轴宽
        xAxis.setAxisMinimum(TrendDataTools.getEarliestData(trendModels));
        xAxis.setLabelCount(trendModels.size(), true);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return TrendDataTools.getDataLable(trendModels, value);
            }
        });
    }

    /**
     * y轴样式设置
     */
    private void initYAxisLine() {
        YAxis leftAxis = trendView.getAxisLeft();
        leftAxis.setAxisMinimum(TrendDataTools.getMinData(trendModels));
        leftAxis.setAxisMaximum(TrendDataTools.getMaxData(trendModels));
        leftAxis.setSpaceTop(10f);
        leftAxis.setLabelCount(5, true);
        leftAxis.setDrawZeroLine(false);//不画轴
        leftAxis.setDrawAxisLine(false);//是否绘制轴线

        //隐藏右侧轴
        trendView.getAxisRight().setEnabled(false);
    }


    /**
     * 设置数据
     *
     * @param values
     */
    private void initData(List<Entry> values) {
        trendView.clear();
        if (null == values || values.size() <= 0) {
            return;
        }
        LineDataSet set1 = new LineDataSet(values, name);
        set1.enableDashedHighlightLine(10f, 5f, 0f);  //点击某个点时显示的线条
        set1.setHighLightColor(Color.RED);  //点击某个点时显示的线条的颜色
        set1.setColor(mContext.getResources().getColor(R.color.darkorange));  //线的颜色
        set1.setValueTextColor(Color.BLACK);//字体的颜色
        set1.setCircleColor(Color.RED);//圆圈的颜色
        set1.setDrawValues(true);//画出具体的值
        set1.setLineWidth(2f);
        set1.setCircleRadius(3f);//关键点的半径
        set1.setDrawCircleHole(false);//空心的孔
        set1.setValueTextSize(9f);//字体大小

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        LineData data = new LineData(dataSets);
        // set data
        trendView.setData(data);
    }

    //初始化设置
    private void setData(List<Entry> values) {
        initXAxisLine();
        initYAxisLine();
        initData(values);
    }

    ///////////////////////////////////////////////

    /**
     * 绑定数据
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 绑定数据
     *
     * @param values
     */
    public void bindTrendData(List<TrendModel> values) {
        this.trendModels = values;

        List<Entry> datas = new ArrayList<>();
        for (TrendModel data : values) {
            datas.add(data.getEntry());
        }
        Collections.sort(datas, new Comparator<Entry>() {
                    public int compare(Entry dataA, Entry DataB) {
                        return (int) (dataA.getX() - DataB.getX());
                    }
                }
        );
        setData(datas);
    }
}
