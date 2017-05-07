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
import com.jiangtao.shuzicai.AppHandlerService;
import com.jiangtao.shuzicai.Application;
import com.jiangtao.shuzicai.R;
import com.jiangtao.shuzicai.basic.adpter.base_adapter_helper_recyclerview.BaseAdapterHelper;
import com.jiangtao.shuzicai.basic.adpter.base_adapter_helper_recyclerview.QuickAdapter;
import com.jiangtao.shuzicai.basic.base.BaseActivityWithToolBar;
import com.jiangtao.shuzicai.basic.utils.EditTextUtils;
import com.jiangtao.shuzicai.common.view.trend_view.TrendView;
import com.jiangtao.shuzicai.common.view.trend_view.model.TrendDataTools;
import com.jiangtao.shuzicai.common.view.trend_view.model.TrendModel;
import com.jiangtao.shuzicai.model.game.entry.Config;
import com.jiangtao.shuzicai.model.game.entry.GuessForecastRecord;
import com.jiangtao.shuzicai.model.game.entry.LondonGold;
import com.jiangtao.shuzicai.model.mall.helper.SpacesItemDecoration;
import com.jiangtao.shuzicai.model.user.LoginActivity;
import com.jiangtao.shuzicai.model.user.entry.WealthDetail;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;

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
    private int NewestNum = -1;
    //是否允许交易
    private boolean isTread = false;

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
    private void bindIndexValue(LondonGold indexData) {
        if (null == indexData) {
            return;
        }
        //指数值
        float price = Float.valueOf(indexData.getLatestpri());
        float resultPrice = new BigDecimal(price).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
        forecastMainIndex.setText(String.valueOf(resultPrice));

        //涨跌比率
        indexChange.setText(indexData.getChange());
        String changePercent = indexData.getLimit() + "%";
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
        getPeriodsCount();
        getForecastRecord();
    }

    //////////////////////////////////////////////////////

    /***
     * 获取期数
     */
    private void getPeriodsCount() {
        BmobQuery<Config> query = new BmobQuery<Config>();
        query.getObject(Config.objectId, new QueryListener<Config>() {
            @Override
            public void done(Config gameInfo, BmobException e) {
                if (e == null && gameInfo != null) {
                    NewestNum = gameInfo.getNewestNum();
                    setCenterTitle("涨跌预测(" + NewestNum + "期)");
                    //获取前面五期数据
                    getShowLondonData(NewestNum - 1);
                    //记录是否可以交易
                    isTread = gameInfo.isTread();
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

        if (!isTread) {
            ToastUtils.showShortToast("当前时间不能交易");
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
                    int value = Integer.parseInt(EditTextUtils.getContent(convertEdt));
                    if (value <= 0) {
                        ToastUtils.showShortToast("请输入正确的金币值");
                    } else {
                        getIfJoined(value, type);
                    }
                }
            }
        }).setNegativeButton("取消", null).show();
    }

    //判断是否已经参与了游戏
    public void getIfJoined(final int value, final int type) {
        if (-1 == NewestNum) {
            ToastUtils.showShortToast("操作失败，请重试");
            return;
        }
        BmobQuery<GuessForecastRecord> query = new BmobQuery<GuessForecastRecord>();
        query.addWhereEqualTo("userId", Application.userInstance.getObjectId());
        query.addWhereEqualTo("periodNum", NewestNum);
        query.count(GuessForecastRecord.class, new CountListener() {
            @Override
            public void done(Integer integer, BmobException e) {
                if (e == null) {
                    if (0 == integer) {
                        submitForecastData(value, type);
                    } else {
                        ToastUtils.showShortToast("不能重复参与同一期游戏");
                    }
                } else {
                    ToastUtils.showShortToast("操作失败，请重试");
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }

    /**
     * 提交预测的数据
     *
     * @param value
     * @param type
     */
    private void submitForecastData(final int value, int type) {
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
                    ToastUtils.showShortToast("操作成功,请等待开奖");
                } else {
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
                LogUtils.i("objectId = " + objectId);
            }
        });
    }

    //获取前面5期伦敦金的数据用于显示
    public void getShowLondonData(final int newestNum) {
        BmobQuery<LondonGold> query = new BmobQuery<LondonGold>();
        query.order("-createdAt");
        query.setLimit(5);
        //执行查询方法
        query.findObjects(new FindListener<LondonGold>() {
            @Override
            public void done(List<LondonGold> stockIndices, BmobException e) {
                mForecastRefreshWidget.setRefreshing(false);
                if (e == null && null != stockIndices) {
                    LogUtils.i("bmob", "返回：" + stockIndices.size());
                    List<TrendModel> modelList = TrendDataTools.getTrendLondonData(newestNum,
                            stockIndices);
                    gameTrendView.setName("伦敦金");
                    gameTrendView.bindTrendData(modelList, 1);
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
    public void updateWealth(int value) {
        final int afterValue = Application.userInstance.getSilverValue() - value;
        //记录金币的操作状态
        WealthDetail wealthDetail = new WealthDetail(
                Application.userInstance.getObjectId(),
                Application.userInstance.getSilverValue(),
                afterValue,
                WealthDetail.Currency_Type_Silver,
                WealthDetail.Operation_Type_Game_Forecast,
                value, 1);
        wealthDetail.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    Application.userInstance.setSilverValue(afterValue);
                    AppHandlerService.updateWealth();
                } else {
                    LogUtils.e("更新财务记录失败:" + e);
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
}
