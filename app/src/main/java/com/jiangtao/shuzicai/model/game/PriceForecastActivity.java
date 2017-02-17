package com.jiangtao.shuzicai.model.game;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bartoszlipinski.recyclerviewheader.RecyclerViewHeader;
import com.blankj.utilcode.utils.LogUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.jiangtao.shuzicai.Application;
import com.jiangtao.shuzicai.R;
import com.jiangtao.shuzicai.basic.adpter.base_adapter_helper_recyclerview.BaseAdapterHelper;
import com.jiangtao.shuzicai.basic.adpter.base_adapter_helper_recyclerview.QuickAdapter;
import com.jiangtao.shuzicai.basic.base.BaseActivityWithToolBar;
import com.jiangtao.shuzicai.basic.utils.EditTextUtils;
import com.jiangtao.shuzicai.common.view.trend_view.TrendView;
import com.jiangtao.shuzicai.common.view.trend_view.model.TrendDataTools;
import com.jiangtao.shuzicai.model.game.entry.ForecastRecord;
import com.jiangtao.shuzicai.model.home.entry.StockIndex;
import com.jiangtao.shuzicai.model.mall.PriceForecastDescriptionActivity;
import com.jiangtao.shuzicai.model.mall.helper.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

//涨跌预测
public class PriceForecastActivity extends BaseActivityWithToolBar
        implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.forecastRecyclerView)
    RecyclerView forecastRecyclerView;
    //
    @BindView(R.id.forecastHeader)
    RecyclerViewHeader forecastHeader;
    //
    @BindView(R.id.forecast_refresh_widget)
    SwipeRefreshLayout mForecastRefreshWidget;
    //交易图
    @BindView(R.id.gameTrendView)
    TrendView gameTrendView;
    //主指数
    @BindView(R.id.forecastMainIndex)
    TextView forecastMainIndex;
    //变化量
    @BindView(R.id.indexChange)
    TextView indexChange;
    //变化值
    @BindView(R.id.indexChangePercent)
    TextView indexChangePercent;

    //适配器
    private QuickAdapter<ForecastRecord> forecastAdapter;


    //设置点击事件
    @OnClick({R.id.forecastUpBtn, R.id.forecastDownBtn})
    public void OnClick(View view) {
        switch (view.getId()) {

            //看涨
            case R.id.forecastUpBtn: {
                betValue(ForecastRecord.ForecastUp);
            }
            break;

            //看跌
            case R.id.forecastDownBtn: {
                betValue(ForecastRecord.ForecastDown);
            }
            break;
        }
    }

    @Override
    public int setLayoutId() {
        return R.layout.activity_price_forecast;
    }

    @Override
    protected void onInitialize() {
        initTitleBar();
    }

    @Override
    public void initPresenter() {
        initSwipeRefresh();
        initRecyclerView();
        initData();
    }

    //初始化swipe
    private void initSwipeRefresh() {
        mForecastRefreshWidget.setColorSchemeResources(
                android.R.color.holo_blue_light,
                android.R.color.holo_green_light,
                android.R.color.holo_red_light,
                android.R.color.holo_orange_light);
        mForecastRefreshWidget.setOnRefreshListener(this);
    }

    //初始化
    private void initRecyclerView() {
        //两列
        forecastRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1,
                StaggeredGridLayoutManager.VERTICAL));
        //添加头部布局
        forecastHeader.attachTo(forecastRecyclerView, true);
        SpacesItemDecoration decoration = new SpacesItemDecoration(2);
        forecastRecyclerView.addItemDecoration(decoration);

        //adapter初始化
        forecastAdapter = new QuickAdapter<ForecastRecord>(getContext(),
                R.layout.item_forecast_recoder_recyclerview, new ArrayList<ForecastRecord>()) {
            @Override
            protected void convert(BaseAdapterHelper helper, final ForecastRecord item) {
                helper.setText(R.id.forecast_period_count, "第" + item.getPeriodCount() + "期");
                helper.setText(R.id.actualResults, "实际结果:" + item.getPeriodResult());
                if (item.getPeriodValue() == 1) {
                    helper.setText(R.id.forecastResult, "预测结果：看涨");
                } else {
                    helper.setText(R.id.forecastResult, "预测结果：看跌");
                }
            }
        };
        forecastRecyclerView.setAdapter(forecastAdapter);
    }

    //绑定指数值
    private void bindIndexValue() {
        forecastMainIndex.setText("3151.11");
        indexChange.setText("+2.53");
        indexChangePercent.setText("2.35%");
    }

    //初始化title
    private void initTitleBar() {
        //右键
        setLeftImage(R.mipmap.ic_arrow_back_white_24dp, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        setCenterTitle("涨跌预测(325期)");
        setRightTitle("说明", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(PriceForecastActivity.this, PriceForecastDescriptionActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onRefresh() {
        initData();
    }

    private void initData() {
        getIndexData();
        getForecastRecord();
        bindIndexValue();
    }

    //////////////////////////////////////////////////////

    /**
     * 押注
     *
     * @param type
     */
    private void betValue(final int type) {
        LayoutInflater inflater = (LayoutInflater)
                this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.dialog_exchange_layout, null);
        final EditText convertEdt = (EditText) layout.findViewById(R.id.convertEdt);
        convertEdt.setHint("输入押注的银币数");

        new AlertDialog.Builder(this).setTitle("输入银币数").setView(
                layout).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (EditTextUtils.isEmpty(convertEdt)) {
                    ToastUtils.showShortToast("银币数数不能空");
                } else {
                    float value = Float.parseFloat(EditTextUtils.getContent(convertEdt));
                    submitForecastData(value, type);
                }
            }
        }).setNegativeButton("取消", null).show();
    }

    /**
     * 提交预测的数据
     *
     * @param value
     * @param type
     */
    private void submitForecastData(float value, int type) {
        ForecastRecord forecastRecord = new ForecastRecord();
        forecastRecord.setUserId(Application.userInstance.getObjectId());
        forecastRecord.setSilverValue(value);
        forecastRecord.setTime(new BmobDate(new Date(System.currentTimeMillis())));
        forecastRecord.setPeriodValue(type);
        forecastRecord.setPeriodCount(123);

        forecastRecord.save(new SaveListener<String>() {
            @Override
            public void done(String objectId, BmobException e) {
                if (e == null) {
                    getForecastRecord();
                    ToastUtils.showShortToast("操作成功");
                } else {
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
                LogUtils.i("objectId = " + objectId);
            }
        });
    }

    //获取数据
    public void getIndexData() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2017, 0, 15, 0, 0, 0);
        Date date = calendar.getTime();

        BmobQuery<StockIndex> query = new BmobQuery<>();
        //查询playerName叫“比目”的数据
        query.addWhereGreaterThanOrEqualTo("date", new BmobDate(date));
        //执行查询方法
        query.findObjects(new FindListener<StockIndex>() {
            @Override
            public void done(List<StockIndex> stockIndices, BmobException e) {
                Log.i("bmob", "返回：" + stockIndices.size());
                mForecastRefreshWidget.setRefreshing(false);
                if (e == null) {
                    gameTrendView.bindTrendData(TrendDataTools.getTrendDatas(
                            StockIndex.Type_HuShen, stockIndices));
                } else {
                    ToastUtils.showShortToast("获取数据失败");
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }


    //获取预测的记录
    private void getForecastRecord() {
        if (null == Application.userInstance) {
            return;
        }
        BmobQuery<ForecastRecord> query = new BmobQuery<>();
        //查询playerName叫“比目”的数据
        query.addWhereEqualTo("userId", Application.userInstance.getObjectId());
        //返回50条数据，如果不加上这条语句，默认返回10条数据
        query.setLimit(500);
        //执行查询方法
        query.findObjects(new FindListener<ForecastRecord>() {
            @Override
            public void done(List<ForecastRecord> object, BmobException e) {
                mForecastRefreshWidget.setRefreshing(false);
                if (e == null) {
                    forecastAdapter.clear();
                    forecastAdapter.addAll(object);
                } else {
                    ToastUtils.showShortToast("获取数据失败");
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }
}
