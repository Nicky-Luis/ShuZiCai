package com.jiangtao.shuzicai.model.game;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bartoszlipinski.recyclerviewheader.RecyclerViewHeader;
import com.blankj.utilcode.utils.LogUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.jiangtao.shuzicai.Application;
import com.jiangtao.shuzicai.R;
import com.jiangtao.shuzicai.basic.adpter.base_adapter_helper_recyclerview.BaseAdapterHelper;
import com.jiangtao.shuzicai.basic.adpter.base_adapter_helper_recyclerview.QuickAdapter;
import com.jiangtao.shuzicai.basic.base.BaseActivityWithToolBar;
import com.jiangtao.shuzicai.model.game.entry.ForecastRecord;
import com.jiangtao.shuzicai.model.home.entry.StockIndex;
import com.jiangtao.shuzicai.model.mall.helper.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class GuessMantissaActivity extends BaseActivityWithToolBar
        implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.mantissaRecyclerView)
    RecyclerView mantissaRecyclerView;
    //
    @BindView(R.id.mantissaHeader)
    RecyclerViewHeader mantissaHeader;
    //
    @BindView(R.id.mantissa_refresh_widget)
    SwipeRefreshLayout mantissaRefreshWidget;
    //公告栏
    @BindView(R.id.mantissaRadioGroup)
    RadioGroup mantissaRadioGroup;
    //指数
    @BindView(R.id.hushenMainIndex)
    TextView hushenMainIndex;
    //主指数改变
    @BindView(R.id.hushenChange)
    TextView hushenChange;
    //百分比
    @BindView(R.id.hushenChangePercent)
    TextView hushenChangePercent;
    //首位
    @BindView(R.id.hushenIndexFirstNum)
    TextView hushenIndexFirstNum;
    //次位
    @BindView(R.id.hushenIndexSecondNum)
    TextView hushenIndexSecondNum;
    //三位
    @BindView(R.id.hushenIndexThirdNum)
    TextView hushenIndexThirdNum;
    //四位
    @BindView(R.id.hushenIndexFourthNum)
    TextView hushenIndexFourthNum;
    //首输入框
    @BindView(R.id.hushenIndexFirstEdt)
    EditText hushenIndexFirstEdt;
    //次输入框
    @BindView(R.id.hushenIndexSecondEdt)
    EditText hushenIndexSecondEdt;
    //提交
    @BindView(R.id.hushenSubmitBtn)
    TextView hushenSubmitBtn;
    //----------------------------------
    //指数
    @BindView(R.id.goldForecastMainIndex)
    TextView goldForecastMainIndex;
    //主指数改变
    @BindView(R.id.goldIndexChange)
    TextView goldIndexChange;
    //百分比
    @BindView(R.id.goldIndexChangePercent)
    TextView goldIndexChangePercent;
    //首位
    @BindView(R.id.goldIndexFirstNum)
    TextView goldIndexFirstNum;
    //次位
    @BindView(R.id.goldIndexSecondNum)
    TextView goldIndexSecondNum;
    //三位
    @BindView(R.id.goldIndexThirdNum)
    TextView goldIndexThirdNum;
    //四位
    @BindView(R.id.goldIndexFourthNum)
    TextView goldIndexFourthNum;
    //首输入框
    @BindView(R.id.goldIndexFirstEdt)
    EditText goldIndexFirstEdt;
    //次输入框
    @BindView(R.id.goldIndexSecondEdt)
    EditText goldIndexSecondEdt;
    //提交
    @BindView(R.id.goldSubmitBtn)
    TextView goldSubmitBtn;
    //预测时间
    @BindView(R.id.mantissaTime)
    TextView mantissaTime;
    //预测时间
    @BindView(R.id.mantissaResult)
    TextView mantissaResult;

    //适配器
    private QuickAdapter<ForecastRecord> forecastAdapter;

    @Override
    public int setLayoutId() {
        return R.layout.activity_guess_mantissa;
    }


    @Override
    protected void onInitialize() {
        initTitleBar();
        initSwipeRefresh();
        initRecyclerView();
        getDate();
    }

    @Override
    public void initPresenter() {

    }


    @Override
    public void onRefresh() {
        getDate();
    }

    //////////////////////////////////////////////////////////////

    //初始化title
    private void initTitleBar() {
        //右键
        setLeftImage(R.mipmap.ic_arrow_back_white_24dp, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        setCenterTitle("尾数预测");
    }


    //初始化swipe
    private void initSwipeRefresh() {
        mantissaRefreshWidget.setColorSchemeResources(
                android.R.color.holo_blue_light,
                android.R.color.holo_green_light,
                android.R.color.holo_red_light,
                android.R.color.holo_orange_light);
        mantissaRefreshWidget.setOnRefreshListener(this);
    }

    //初始化
    private void initRecyclerView() {
        //两列
        mantissaRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1,
                StaggeredGridLayoutManager.VERTICAL));
        //添加头部布局
        mantissaHeader.attachTo(mantissaRecyclerView, true);
        SpacesItemDecoration decoration = new SpacesItemDecoration(2);
        mantissaRecyclerView.addItemDecoration(decoration);

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
        mantissaRecyclerView.setAdapter(forecastAdapter);
    }

    //获取数据
    private void getDate() {
        getNewestGoldIndex();
        getNewest300Index();
    }

    //设置view的值
    private void setViewValue() {
        //点击事件
        mantissaRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId) {
                    case R.id.mantissaFirstBtn:
                        break;

                    case R.id.mantissaSecondBtn:
                        break;

                    case R.id.mantissaThirdBtn:
                        break;
                }
            }
        });
    }

    //获取最新的黄金信息
    private void getNewestGoldIndex() {

        if (null == Application.userInstance) {
            return;
        }
        BmobQuery<StockIndex> query = new BmobQuery<>();
        //查询playerName叫“比目”的数据
        query.addWhereEqualTo("stock_type", StockIndex.Type_chuangGold);
        // 根据createAt字段降序显示数据
        query.order("-date");
        //返回10条数据，如果不加上这条语句，默认返回10条数据
        query.setLimit(10);
        //执行查询方法
        query.findObjects(new FindListener<StockIndex>() {
            @Override
            public void done(List<StockIndex> stockIndices, BmobException e) {
                mantissaRefreshWidget.setRefreshing(false);
                if (e == null) {
                    //获取到最后的是10条黄金信息
                    if (null != stockIndices) {
                        StockIndex stockIndex = stockIndices.get(0);
                        bindGoldValue(stockIndex);
                    }
                    // for (StockIndex stockIndex : stockIndices) {
                    //      LogUtils.i("黄金数据：" + stockIndex.toString());
                    // }
                } else {
                    ToastUtils.showShortToast("获取数据失败");
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }

    //绑定黄金数据
    private void bindGoldValue(StockIndex stockIndex) {
        float indexValue = stockIndex.getStock_value();
        goldForecastMainIndex.setText(String.valueOf(indexValue));
        goldIndexChange.setText(String.valueOf(stockIndex.getChange_value()));
        goldIndexChangePercent.setText(String.valueOf(stockIndex.getChange_percent()) + "%");

        String aS = String.valueOf(indexValue);
        char[] asC = aS.toCharArray();
        for (char st : asC) {
            LogUtils.i("-------" + st);
        }
        goldIndexFirstNum.setText(String.valueOf(asC[0]));
        goldIndexSecondNum.setText(String.valueOf(asC[1]));
        goldIndexThirdNum.setText(String.valueOf(asC[2]));
        goldIndexFourthNum.setText(String.valueOf(asC[3]));
    }

    //获取最新的黄金信息
    private void getNewest300Index() {

        if (null == Application.userInstance) {
            return;
        }
        BmobQuery<StockIndex> query = new BmobQuery<>();
        //查询playerName叫“比目”的数据
        query.addWhereEqualTo("stock_type", StockIndex.Type_HuShen);
        // 根据createAt字段降序显示数据
        query.order("-date");
        //返回10条数据，如果不加上这条语句，默认返回10条数据
        query.setLimit(10);
        //执行查询方法
        query.findObjects(new FindListener<StockIndex>() {
            @Override
            public void done(List<StockIndex> stockIndices, BmobException e) {
                mantissaRefreshWidget.setRefreshing(false);
                if (e == null) {
                    //获取到最后的是10条黄金信息
                    if (null != stockIndices) {
                        StockIndex stockIndex = stockIndices.get(0);
                        bindIndex300Value(stockIndex);
                    }
                    // for (StockIndex stockIndex : stockIndices) {
                    //      LogUtils.i("黄金数据：" + stockIndex.toString());
                    // }
                } else {
                    ToastUtils.showShortToast("获取数据失败");
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }

    //绑定黄金数据
    private void bindIndex300Value(StockIndex stockIndex) {
        float indexValue = stockIndex.getStock_value();
        hushenMainIndex.setText(String.valueOf(indexValue));
        hushenChange.setText(String.valueOf(stockIndex.getChange_value()));
        hushenChangePercent.setText(String.valueOf(stockIndex.getChange_percent()) + "%");

        String aS = String.valueOf(indexValue);
        char[] asC = aS.toCharArray();
        for (char st : asC) {
            LogUtils.i("-------" + st);
        }
        hushenIndexFirstNum.setText(String.valueOf(asC[0]));
        hushenIndexSecondNum.setText(String.valueOf(asC[1]));
        hushenIndexThirdNum.setText(String.valueOf(asC[2]));
        hushenIndexFourthNum.setText(String.valueOf(asC[3]));
    }
}
