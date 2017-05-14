package com.jiangtao.shuzicai.model.game;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
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
import com.jiangtao.shuzicai.model.game.entry.GuessMantissaRecord;
import com.jiangtao.shuzicai.model.game.entry.LondonGold;
import com.jiangtao.shuzicai.model.mall.helper.SpacesItemDecoration;
import com.jiangtao.shuzicai.model.user.LoginActivity;
import com.jiangtao.shuzicai.model.user.entry.WealthDetail;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BatchResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListListener;
import cn.bmob.v3.listener.SaveListener;

import static com.jiangtao.shuzicai.model.game.GuessForecastActivity.removeDuplicate;
import static com.jiangtao.shuzicai.model.game.entry.GuessMantissaRecord.Guess_Type_DoubleDirect;
import static com.jiangtao.shuzicai.model.game.entry.GuessMantissaRecord.Guess_Type_DoubleGroup;
import static com.jiangtao.shuzicai.model.game.entry.GuessMantissaRecord.Guess_Type_Percentile;

public class GuessMantissaActivity extends BaseActivityWithToolBar
        implements SwipeRefreshLayout.OnRefreshListener {


    //
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
    //开奖时间
    @BindView(R.id.mantissaResultTime)
    TextView mantissaResultTime;
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
    //首输入框
    @BindView(R.id.goldIndexFirstEdt)
    EditText goldIndexFirstEdt;
    //次输入框
    @BindView(R.id.goldIndexSecondEdt)
    EditText goldIndexSecondEdt;
    //提交
    @BindView(R.id.goldSubmitBtn)
    TextView goldSubmitBtn;
    //当前的游戏类型
    private int currentGuessType = GuessMantissaRecord.Guess_Type_Percentile;
    //handler
    private Handler mHandler = new Handler();
    //适配器
    private QuickAdapter<GuessMantissaRecord> recordAdapter;
    //押注的期数
    private int NewestNum = -1;
    //是否允许交易
    private boolean isTread = false;
    //记录
    @BindView(R.id.record_layout)
    LinearLayout record_layout;

    //设置点击事件
    @OnClick({R.id.goldSubmitBtn})
    public void OnClick(View view) {
        switch (view.getId()) {
            //提交
            case R.id.goldSubmitBtn: {
                startBet();
            }
            break;
        }
    }

    @Override
    public int setLayoutId() {
        return R.layout.activity_guess_mantissa;
    }

    @Override
    protected void onInitialize() {
        initTitleBar();
        initSwipeRefresh();
        initRecyclerView();
        setViewValue();
        getData();
        initView();
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void onRefresh() {
        getData();
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
        setRightTitle("说明", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(GuessMantissaActivity.this, GuessMantissaDetailActivity.class);
                startActivity(intent);
            }
        });
    }


    private void initView() {
        goldIndexFirstEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    goldIndexSecondEdt.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        goldIndexSecondEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() <= 0) {
                    goldIndexFirstEdt.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
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
        recordAdapter = new QuickAdapter<GuessMantissaRecord>(getContext(),
                R.layout.item_guess_mantissa_recoder_recyclerview,
                new ArrayList<GuessMantissaRecord>()) {
            @Override
            protected void convert(BaseAdapterHelper helper, final GuessMantissaRecord item) {
                helper.setText(R.id.guss_mantissa_period_count, "第" + item.getPeriodNum() + "期");
                //数据类型
                if (item.getGuessType() == GuessMantissaRecord.Guess_Type_Percentile) {
                    helper.setText(R.id.gussType, "百分位直选");
                } else if (item.getGuessType() == GuessMantissaRecord.Guess_Type_DoubleDirect) {
                    helper.setText(R.id.gussType, "双数直选");
                } else {
                    helper.setText(R.id.gussType, "双数组选");
                }

                if (item.getBetStatus() != 1) {
                    float price = item.getIndexResult();
                    String resultPrice;
                    if (item.getGuessType() == GuessMantissaRecord.Guess_Type_Percentile) {
                        DecimalFormat decimalFormat = new DecimalFormat(".0");
                        resultPrice = decimalFormat.format(price);
                    } else {
                        resultPrice = String.valueOf((int) price) + ".";
                    }
                    helper.setVisible(R.id.gussMantissaPre, true);
                    helper.setText(R.id.gussMantissaPre, "预测：" + (resultPrice.length() < 4 ? "" : resultPrice));
                    helper.setTextColor(R.id.gussMantissaResult, R.color.main_orange);

                    if (item.getGuessValue() < 10 && item.getGuessType() != GuessMantissaRecord.Guess_Type_Percentile) {
                        helper.setText(R.id.gussMantissaResult, "0" + String.valueOf(item.getGuessValue()));
                    } else {
                        helper.setText(R.id.gussMantissaResult, String.valueOf(item.getGuessValue()));
                    }

                    if (item.getHandlerFlag() == 0) {
                        helper.setText(R.id.actualResults, "结果: 待定");
                    } else {
                        DecimalFormat decimalFormat1 = new DecimalFormat(".00");
                        String resultPrice1 = decimalFormat1.format(price);
                        helper.setTextColor(R.id.actualResults, R.color.green);
                        helper.setText(R.id.actualResults, "结果：" + resultPrice1);
                    }
                } else {
                    helper.setTextColor(R.id.gussMantissaResult, R.color.main_red);
                    helper.setVisible(R.id.gussMantissaPre, false);
                    helper.setText(R.id.gussMantissaResult, "已中奖");
                    helper.setText(R.id.actualResults, "奖励" + item.getRewardCount() + "金币");
                }
            }
        };
        mantissaRecyclerView.setAdapter(recordAdapter);
    }

    //设置view的值
    private void setViewValue() {
        //点击事件
        mantissaRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId) {
                    case R.id.mantissaFirstBtn:
                        currentGuessType = Guess_Type_Percentile;
                        break;

                    case R.id.mantissaSecondBtn:
                        currentGuessType = Guess_Type_DoubleDirect;
                        break;

                    case R.id.mantissaThirdBtn:
                        currentGuessType = Guess_Type_DoubleGroup;
                        break;
                }
                setGuessType();
            }
        });
    }

    //设置游戏类型
    private void setGuessType() {
        //设置黄金的
        if (currentGuessType == GuessMantissaRecord.Guess_Type_Percentile) {
            goldIndexFirstEdt.setText("");
            goldIndexFirstEdt.setVisibility(View.GONE);
        } else {
            goldIndexFirstEdt.setVisibility(View.VISIBLE);
            goldIndexFirstEdt.setText("");
            goldIndexFirstEdt.setBackgroundColor(getResources().getColor(R.color.main_orange));
            goldIndexFirstEdt.setEnabled(true);
        }
    }

    //获取数据
    private void getData() {
        showProgress("正在加载数据");
        AppHandlerService.getLondonData(new AppHandlerService.DataCallBack() {

            @Override
            public void onGetGoldLondon(float price, String change, String limit) {
                DecimalFormat decimalFormat = new DecimalFormat(".00");
                String resultPrice = decimalFormat.format(price);
                goldForecastMainIndex.setText(resultPrice);

                goldIndexChange.setText(change);
                goldIndexChangePercent.setText(limit + "%");

                //根据类型再去绑定
                setGuessType();
            }

            @Override
            public void onGetPeriodsCount(int periodsCount, boolean tread) {
                NewestNum = periodsCount;
                isTread = tread;
                setCenterTitle("尾数预测(" + NewestNum + "期)");

                //获取游戏记录
                getGameRecord();
            }

            @Override
            public void onGetDataFail() {
                hideProgress();
                ToastUtils.showShortToast("获取数据失败");
            }
        });
    }


    //获取操作记录
    private void getGameRecord() {
        if (null == Application.userInstance) {
            hideProgress();
            return;
        }
        BmobQuery<GuessMantissaRecord> query = new BmobQuery<>();
        //查询playerName叫“比目”的数据
        query.addWhereEqualTo("userId", Application.userInstance.getObjectId());
        query.order("-createdAt");
        //最多20条
        query.setLimit(20);
        //执行查询方法
        query.findObjects(new FindListener<GuessMantissaRecord>() {
            @Override
            public void done(List<GuessMantissaRecord> list, BmobException e) {
                mantissaRefreshWidget.setRefreshing(false);
                hideProgress();
                if (e == null) {
                    if (null != list && list.size() > 0) {
                        record_layout.setVisibility(View.VISIBLE);
                        mantissaResultTime.setText("");
                        List<Integer> numList = new ArrayList<>();
                        for (GuessMantissaRecord forecastRecord : list) {
                            if (forecastRecord.getHandlerFlag() == 0) {
                                numList.add(forecastRecord.getPeriodNum());
                            }
                        }
                        List<Integer> integerList = removeDuplicate(numList);
                        gameResultHandler(list, integerList);
                    }else {
                        recordAdapter.clear();
                        record_layout.setVisibility(View.GONE);
                        mantissaResultTime.setText("输入你想要预测的指数信息");
                    }
                } else {
                    ToastUtils.showShortToast("获取数据失败");
                    LogUtils.e("获取游戏记录失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }

    /**
     * 预测结果处理
     */
    private void gameResultHandler(final List<GuessMantissaRecord> recordList, List<Integer> periodNums) {
        LogUtils.i("开始获取对应的期数信息");
        BmobQuery<LondonGold> query = new BmobQuery<LondonGold>();
        query.addWhereContainedIn("periodsNum", periodNums);
        //执行查询方法
        query.findObjects(new FindListener<LondonGold>() {
            @Override
            public void done(List<LondonGold> list, BmobException e) {
                if (e == null) {
                    final List<BmobObject> batchs = getGameBmobBatch(list, recordList);
                    startBmobBatchUpdate(batchs);
                    //按时间排序
                    class ComparatorUser implements Comparator<GuessMantissaRecord> {
                        @Override
                        public int compare(GuessMantissaRecord u1, GuessMantissaRecord u2) {
                            return u2.getCreatedAt().compareTo(u1.getCreatedAt());
                        }
                    }
                    Comparator<GuessMantissaRecord> cmp = new ComparatorUser();
                    Collections.sort(recordList, cmp);

                    recordAdapter.clear();
                    recordAdapter.addAll(recordList);
                } else {
                    ToastUtils.showShortToast("获取数据失败");
                    LogUtils.e("查询涨跌统计信息失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }

    /**
     * 批量更新
     *
     * @param datas
     * @return
     */
    public boolean startBmobBatchUpdate(List<BmobObject> datas) {
        if (datas.size() <= 0) {
            return true;
        }
        //批量更新中奖信息
        new BmobBatch().updateBatch(datas).doBatch(new QueryListListener<BatchResult>() {

            @Override
            public void done(List<BatchResult> list, BmobException e) {
                if (e == null) {
                    for (int i = 0; i < list.size(); i++) {
                        BatchResult result = list.get(i);
                        BmobException ex = result.getError();
                        if (ex == null) {
                            LogUtils.i("第" + i + "个数据批量更新成功：" + result.getUpdatedAt());
                        } else {
                            LogUtils.i("第" + i + "个数据批量更新失败：" + ex.getMessage() + "," + ex.getErrorCode());
                        }
                    }
                } else {
                    LogUtils.i("失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
        return false;
    }


    /**
     * 获取更新的数据
     *
     * @param list
     * @param recordList
     * @return
     */
    public List<BmobObject> getGameBmobBatch(List<LondonGold> list, List<GuessMantissaRecord> recordList) {
        LogUtils.e("伦敦金数据：" + list.size());
        LogUtils.e("记录数据：" + recordList.size());
        final List<BmobObject> datas = new ArrayList<BmobObject>();
        for (LondonGold gold : list) {
            for (GuessMantissaRecord mantissaRecord : recordList) {
                if (mantissaRecord.getHandlerFlag() == 0 && mantissaRecord.getPeriodNum() == gold.getPeriodsNum()) {
                    float currentPrice = Float.valueOf(gold.getLatestpri());
                    //获取小数点后两位数
                    String te = gold.getLatestpri().substring(5);
                    int re = Integer.parseInt(te);
                    int resultNum = re < 10 ? re * 10 : re;
                    LogUtils.e("最后两位数：" + resultNum);
                    //获取个位数
                    int secondNum = resultNum % 10;
                    //记录结果
                    mantissaRecord.setRewardFlag(1);//标记为已经同步奖励
                    mantissaRecord.setHandlerFlag(1);//标记为已经处理了结果
                    mantissaRecord.setIndexResult(currentPrice);//中奖的实际结果
                    //计算押注的结果
                    if (mantissaRecord.getGuessType() == GuessMantissaRecord.Guess_Type_Percentile) {
                        LogUtils.i("百分位直选");
                        boolean result = mantissaRecord.getGuessValue() == secondNum;
                        mantissaRecord.setBetStatus(result ? 1 : 2);
                        mantissaRecord.setRewardCount(result ? 100 : 0);
                        saveWealthValue(result ? 100 : 0);
                    } else if (mantissaRecord.getGuessType() == GuessMantissaRecord.Guess_Type_DoubleDirect) {
                        LogUtils.i("双数位直选");
                        boolean result = mantissaRecord.getGuessValue() == resultNum;
                        mantissaRecord.setBetStatus(result ? 1 : 2);
                        mantissaRecord.setRewardCount(result ? 1000 : 0);
                        saveWealthValue(result ? 1000 : 0);
                    } else if (mantissaRecord.getGuessType() == GuessMantissaRecord.Guess_Type_DoubleGroup) {
                        LogUtils.i("双数位组选");
                        int num = ((mantissaRecord.getGuessValue() % 10) * 10)
                                + (mantissaRecord.getGuessValue() / 10) % 10;
                        //结果
                        boolean result = (mantissaRecord.getGuessValue() == resultNum) || (num == resultNum);
                        mantissaRecord.setBetStatus(result ? 1 : 2);
                        mantissaRecord.setRewardCount(result ? 500 : 0);
                        saveWealthValue(result ? 500 : 0);
                    }
                    datas.add(mantissaRecord);
                }
            }
        }
        return datas;
    }


    /**
     * 获奖成功后的操作
     *
     * @param value
     */
    public void saveWealthValue(int value) {
        if (value <= 0) {
            return;
        }
        LogUtils.i("奖励的金币数为：" + value);
        final int beforeValue = Application.userInstance.getGoldValue();
        final int afterValue = beforeValue + value;
        //记录银币数
        Application.userInstance.setGoldValue(afterValue);
        AppHandlerService.updateWealth();

        //记录充值的数据
        WealthDetail wealthDetail = new WealthDetail(
                Application.userInstance.getObjectId(),
                beforeValue,
                afterValue,
                WealthDetail.Currency_Type_Gold,
                WealthDetail.Operation_Type_Mantisssa_Reward,
                value, 1);
        saveWealthRecord(wealthDetail);
    }

    /***
     * 记录金币的操作状态
     *
     * @param wealthDetail
     */
    public void saveWealthRecord(WealthDetail wealthDetail) {
        //记录金币的操作状态
        wealthDetail.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    LogUtils.i("奖励财富记录更新成功");
                } else {
                    LogUtils.e("奖励财富记录更新失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }


    //开始参与游戏
    public void startBet() {
        if (null == Application.userInstance) {
            Intent intent = new Intent(this, LoginActivity.class);
            ToastUtils.showShortToast("请先进行登录");
            startActivity(intent);
            return;
        }

        if (-1 == NewestNum) {
            ToastUtils.showShortToast("操作失败，请重试");
            return;
        }

        if (!isTread) {
            ToastUtils.showShortToast("当前时间不能交易");
            return;
        }

        submitValue(GuessMantissaRecord.Index_Type_Gold);
    }

    //提交沪深300数据
    private void submitValue(int indexType) {

        EditText firstEdt = goldIndexFirstEdt;
        EditText secondEdt = goldIndexSecondEdt;
        //百分位任何情况下都不能为空
        if (EditTextUtils.isEmpty(secondEdt)) {
            ToastUtils.showShortToast("请先输入预测的数据");
            return;
        }
        int firstValue = 0;
        int secondValue = Integer.valueOf(EditTextUtils.getContent(secondEdt));

        if (currentGuessType != GuessMantissaRecord.Guess_Type_Percentile) {
            if (EditTextUtils.isEmpty(firstEdt)) {
                ToastUtils.showShortToast("请先输入预测的数据");
                return;
            }
            firstValue = Integer.valueOf(EditTextUtils.getContent(firstEdt));
        }
        //记录值
        final GuessMantissaRecord record = new GuessMantissaRecord();
        record.setUserId(Application.userInstance.getObjectId());
        record.setGuessType(currentGuessType);
        record.setBetStatus(0);
        record.setIndexType(indexType);

        record.setIndexResult(0);
        record.setRewardCount(0);
        record.setHandlerFlag(0);
        record.setPeriodNum(NewestNum);

        if (GuessMantissaRecord.Guess_Type_Percentile == currentGuessType) {
            record.setGuessValue(secondValue);
        } else {
            record.setGuessValue(firstValue * 10 + secondValue);
        }

        //弹出提示框
        LayoutInflater inflater = (LayoutInflater)
                this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.dialog_exchange_layout, null);
        final EditText convertEdt = (EditText) layout.findViewById(R.id.convertEdt);
        convertEdt.setHint("输入押注的金币数");

        new AlertDialog.Builder(this).setTitle("输入金币数（必须为20的倍数）").setView(
                layout).setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (EditTextUtils.isEmpty(convertEdt)) {
                    ToastUtils.showShortToast("币数不能空");
                } else {
                    final int value = Integer.parseInt(EditTextUtils.getContent(convertEdt));
                    if (value <= 0) {
                        ToastUtils.showShortToast("请输入正确的金币值");
                    } else if (value > Application.userInstance.getGoldValue()) {
                        ToastUtils.showShortToast("金币不够，请充值");
                    } else if (value % 20 == 0) {
                        record.setGoldValue(value);
                        record.save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                if (e == null) {
                                    ToastUtils.showShortToast("操作成功");
                                    //更新用户的财富
                                    updateWealth(value);
                                    //更新游戏记录
                                    getGameRecord();
                                } else {
                                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                                }
                            }
                        });
                    } else {
                        ToastUtils.showShortToast("币数必须为20的倍数");
                    }
                }
            }
        }).setNegativeButton("取消", null).show();
        firstEdt.setText("");
        secondEdt.setText("");
    }

    /**
     * 更新财务数据
     *
     * @param value
     */
    public void updateWealth(int value) {
        final int afterValue = Application.userInstance.getGoldValue() - value;
        //记录金币的操作状态
        WealthDetail wealthDetail = new WealthDetail(
                Application.userInstance.getObjectId(),
                Application.userInstance.getGoldValue(),
                afterValue,
                WealthDetail.Currency_Type_Gold,
                WealthDetail.Operation_Type_Game_Mantisssa,
                value, 1);
        wealthDetail.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    Application.userInstance.setGoldValue(afterValue);
                    AppHandlerService.updateWealth();
                } else {
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }
}
