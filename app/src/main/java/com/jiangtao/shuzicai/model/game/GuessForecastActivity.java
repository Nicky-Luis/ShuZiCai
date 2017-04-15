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
import com.jiangtao.shuzicai.model.game.entry.GameInfo;
import com.jiangtao.shuzicai.model.game.entry.GuessForecastRecord;
import com.jiangtao.shuzicai.model.game.entry.HuShenIndex;
import com.jiangtao.shuzicai.model.home.entry.StockIndex;
import com.jiangtao.shuzicai.model.mall.helper.SpacesItemDecoration;
import com.jiangtao.shuzicai.model.user.LoginActivity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

//涨跌预测
public class GuessForecastActivity extends BaseActivityWithToolBar
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
    //开奖时间
    @BindView(R.id.forecastResultTime)
    TextView forecastResultTime;
    //押注的期数
    private int NewestNum = 0;

    //适配器
    private QuickAdapter<GuessForecastRecord> forecastAdapter;


    //设置点击事件
    @OnClick({R.id.forecastUpBtn, R.id.forecastDownBtn})
    public void OnClick(View view) {
        switch (view.getId()) {

            //看涨
            case R.id.forecastUpBtn: {
                betValue(GuessForecastRecord.ForecastUp);
            }
            break;

            //看跌
            case R.id.forecastDownBtn: {
                betValue(GuessForecastRecord.ForecastDown);
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
        forecastAdapter = new QuickAdapter<GuessForecastRecord>(getContext(),
                R.layout.item_forecast_recoder_recyclerview, new ArrayList<GuessForecastRecord>()) {
            @Override
            protected void convert(BaseAdapterHelper helper, final GuessForecastRecord item) {
                helper.setText(R.id.forecast_period_count, "第" + item.getPeriodNum() + "期");
                if (item.getBetStatus() == 1) {
                    helper.setText(R.id.forecastResult, "预测结果：看涨");
                } else {
                    helper.setText(R.id.forecastResult, "预测结果：看跌");
                }
                if (item.getHandlerFlag() == 1) {
                    if (item.getBetResult() == 1) {
                        helper.setText(R.id.actualResults, "实际结果: 上涨");
                    } else {
                        helper.setText(R.id.actualResults, "实际结果: 下跌");
                    }
                } else {
                    helper.setText(R.id.actualResults, "实际结果: 待定");
                }
            }
        };
        forecastRecyclerView.setAdapter(forecastAdapter);
    }

    //绑定指数值
    private void bindIndexValue(HuShenIndex indexData) {
        if (null == indexData) {
            return;
        }
        //指数值
        float price = Float.valueOf(indexData.getNowPrice());
        float resultPrice = new BigDecimal(price).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
        forecastMainIndex.setText(String.valueOf(resultPrice));

        //涨跌比率
        indexChange.setText(indexData.getDiff_money());
        String changePercent = indexData.getDiff_rate() + "%";
        indexChangePercent.setText(changePercent);


        //这期开奖时间
        forecastResultTime.setText("");
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
        setCenterTitle("涨跌预测");
        setRightTitle("说明", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(GuessForecastActivity.this, GuessForecastDetailActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onRefresh() {
        initData();
    }

    private void initData() {
        getLastWeekData();
        getForecastRecord();
        getPeriodsCount();
    }

    //////////////////////////////////////////////////////

    /***
     * 获取期数
     */
    private void getPeriodsCount() {
        BmobQuery<GameInfo> query = new BmobQuery<GameInfo>();
        query.addWhereEqualTo("gameType", GameInfo.type_zhangdie);
        query.findObjects(new FindListener<GameInfo>() {
            @Override
            public void done(List<GameInfo> list, BmobException e) {
                if (e == null) {
                    if (list != null && list.size() > 0) {
                        NewestNum = list.get(0).getNewestNum();
                        setCenterTitle("涨跌预测(" + NewestNum + "期)");
                    }
                }
            }
        });
    }

    /**
     * 押注
     *
     * @param type
     */
    private void betValue(final int type) {
        if (null == Application.userInstance) {
            Intent intent = new Intent(this, LoginActivity.class);
            ToastUtils.showShortToast("请先进行登录");
            startActivity(intent);
            return;
        }

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
    private void submitForecastData(final float value, int type) {
        if (value > Application.userInstance.getSilverValue()) {
            ToastUtils.showShortToast("银币不够");
            return;
        }
        GuessForecastRecord forecastRecord = new GuessForecastRecord();
        forecastRecord.setUserId(Application.userInstance.getObjectId());
        forecastRecord.setBetSilverValue(value);
        forecastRecord.setBetStatus(type);
        forecastRecord.setPeriodNum(NewestNum);

        forecastRecord.save(new SaveListener<String>() {
            @Override
            public void done(String objectId, BmobException e) {
                if (e == null) {
                    getForecastRecord();
                    updateWealth(value);
                    ToastUtils.showShortToast("操作成功");
                } else {
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
                LogUtils.i("objectId = " + objectId);
            }
        });
    }

    //获取数据
    public void getIndexData(BmobDate date) {
        BmobQuery<HuShenIndex> query = new BmobQuery<HuShenIndex>();
        query.addWhereGreaterThan("createdAt", date);
        query.order("createdAt");
        query.setLimit(50);
        //执行查询方法
        query.findObjects(new FindListener<HuShenIndex>() {
            @Override
            public void done(List<HuShenIndex> stockIndices, BmobException e) {
                Log.i("bmob", "返回：" + stockIndices.size());
                mForecastRefreshWidget.setRefreshing(false);
                if (e == null) {
                    gameTrendView.bindTrendData(TrendDataTools.getTrendDatas2(
                            StockIndex.Type_HuShen, stockIndices));
                    if (stockIndices.size() > 0) {
                        bindIndexValue(stockIndices.get(0));
                    }
                } else {
                    ToastUtils.showShortToast("获取数据失败");
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }

    /**
     * 更新财务数据
     *
     * @param value
     */
    public void updateWealth(float value) {
        Application.userInstance.setSilverValue(Application.userInstance.getSilverValue() - value);
        Application.userInstance.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    LogUtils.i("更新数据成功");
                } else {
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
        BmobQuery<GuessForecastRecord> query = new BmobQuery<>();
        //查询playerName叫“比目”的数据
        query.addWhereEqualTo("userId", Application.userInstance.getObjectId());
        query.setLimit(10);
        //执行查询方法
        query.findObjects(new FindListener<GuessForecastRecord>() {
            @Override
            public void done(List<GuessForecastRecord> object, BmobException e) {
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

    /**
     * 获取7天前的时间
     *
     * @return
     */
    private void getLastWeekData() {
        gameTrendView.setName("沪深300");
        //一个礼拜前的数据
        Bmob.getServerTime(new QueryListener<Long>() {
            @Override
            public void done(Long aLong, BmobException e) {
                if (e == null) {
                    int dayCount = 7;
                    BmobDate date = new BmobDate(new Date(aLong - (dayCount * 24 * 60 * 60)));
                    getIndexData(date);
                    Log.i("bmob", "当前服务器时间为:" + aLong);
                } else {
                    Log.i("bmob", "获取服务器时间失败:" + e.getMessage());
                }
            }
        });
    }
}
