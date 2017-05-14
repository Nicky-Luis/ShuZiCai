package com.jiangtao.shuzicai.model.game;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.jiangtao.shuzicai.model.game.entry.GuessWholeRecord;
import com.jiangtao.shuzicai.model.game.entry.LondonGold;
import com.jiangtao.shuzicai.model.mall.helper.SpacesItemDecoration;
import com.jiangtao.shuzicai.model.user.LoginActivity;
import com.jiangtao.shuzicai.model.user.entry.WealthDetail;

import java.math.BigDecimal;
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

public class GuessWholeActivity extends BaseActivityWithToolBar
        implements SwipeRefreshLayout.OnRefreshListener {


    //
    @BindView(R.id.guessWholeRecyclerView)
    RecyclerView guessWholeRecyclerView;
    //
    @BindView(R.id.guessWholeHeader)
    RecyclerViewHeader guessWholeHeader;
    //
    @BindView(R.id.guessWhole_refresh_widget)
    SwipeRefreshLayout guessWholeRefreshWidget;
    //指数
    @BindView(R.id.guessWholeMainIndex)
    TextView guessWholeMainIndex;
    //主指数改变
    @BindView(R.id.guessWholeChange)
    TextView guessWholeChange;
    //百分比
    @BindView(R.id.guessWholeChangePercent)
    TextView guessWholeChangePercent;
    //首位
    @BindView(R.id.guessWholeFirstEdt)
    EditText guessWholeFirstEdt;
    //次位
    @BindView(R.id.guessWholeSecondEdt)
    EditText guessWholeSecondEdt;
    //三位
    @BindView(R.id.guessWholeThirdEdt)
    EditText guessWholeThirdEdt;
    //四位
    @BindView(R.id.guessWholeFourthEdt)
    EditText guessWholeFourthEdt;
    //五位
    @BindView(R.id.guessWholeFifthEdt)
    EditText guessWholeFifthEdt;
    //六位
    @BindView(R.id.guessWholeSixthEdt)
    EditText guessWholeSixthEdt;
    //开奖时间
    @BindView(R.id.guessWholeResultTime)
    TextView guessWholeResultTime;
    //押注的期数
    private int NewestNum = -1;
    //是否允许交易
    private boolean isTread = false;
    //适配器
    private QuickAdapter<GuessWholeRecord> recordAdapter;
    //记录
    @BindView(R.id.record_layout)
    LinearLayout record_layout;

    //设置点击事件
    @OnClick({R.id.guessWholeSubmitBtn})
    public void OnClick(View view) {
        switch (view.getId()) {

            //提交
            case R.id.guessWholeSubmitBtn: {
                startBet();
            }
            break;
        }
    }

    @Override
    public int setLayoutId() {
        return R.layout.activity_guess_whole_activity;
    }


    @Override
    protected void onInitialize() {
        initTitleBar();
        initSwipeRefresh();
        initRecyclerView();
        initEditViewListener();
        getData();
    }

    @Override
    public void initPresenter() {

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
        setCenterTitle("全数预测");
        setRightTitle("说明", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(GuessWholeActivity.this, GuessWholeDetailActivity.class);
                startActivity(intent);
            }
        });
    }

    //初始化swipe
    private void initSwipeRefresh() {
        guessWholeRefreshWidget.setColorSchemeResources(
                android.R.color.holo_blue_light,
                android.R.color.holo_green_light,
                android.R.color.holo_red_light,
                android.R.color.holo_orange_light);
        guessWholeRefreshWidget.setOnRefreshListener(this);
    }

    //序号
    int current = 0;

    //监听
    private void initEditViewListener() {
        final EditText[] editTexts = {guessWholeFirstEdt, guessWholeSecondEdt,
                guessWholeThirdEdt, guessWholeFourthEdt,
                guessWholeFifthEdt, guessWholeSixthEdt};
        for (final EditText editText : editTexts) {
            editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    int index = 0;
                    for (final EditText edt : editTexts) {
                        if (view.getId() == edt.getId()) {
                            current = index;
                            break;
                        }
                        index++;
                    }
                }
            });
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (charSequence.length() > 0) {
                        current++;
                    }
                    if (charSequence.length() <= 0) {
                        current--;
                    }
                    if (current < 0) {
                        current = 0;
                    }
                    if (current > 5) {
                        current = 5;
                    }
                    LogUtils.i("current：" + current);
                    editTexts[current].requestFocus();
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
        }
    }

    //初始化
    private void initRecyclerView() {
        //两列
        guessWholeRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1,
                StaggeredGridLayoutManager.VERTICAL));
        //添加头部布局
        guessWholeHeader.attachTo(guessWholeRecyclerView, true);
        SpacesItemDecoration decoration = new SpacesItemDecoration(2);
        guessWholeRecyclerView.addItemDecoration(decoration);

        //adapter初始化
        recordAdapter = new QuickAdapter<GuessWholeRecord>(getContext(),
                R.layout.item_guess_whole_recoder_recyclerview,
                new ArrayList<GuessWholeRecord>()) {
            @Override
            protected void convert(BaseAdapterHelper helper, final GuessWholeRecord item) {
                helper.setText(R.id.guess_whole_period_count, "第" + item.getPeriodNum() + "期");
                if (item.getBetStatus() != 1) {
                    float price = item.getGuessValue();
                    DecimalFormat decimalFormat = new DecimalFormat(".00");
                    String resultPrice = decimalFormat.format(price);
                    helper.setText(R.id.guessWholeItemResult, "预测：" + resultPrice);

                    if (item.getHandlerFlag() == 0) {
                        helper.setText(R.id.guessWholeActualResults, "结果: 待定");
                    } else {
                        float price1 = item.getIndexResult();
                        DecimalFormat decimalFormat1 = new DecimalFormat(".00");
                        String resultPrice1 = decimalFormat1.format(price1);
                        helper.setTextColor(R.id.guessWholeActualResults, R.color.green);
                        helper.setText(R.id.guessWholeActualResults, "结果：" + resultPrice1);
                    }
                } else {
                    helper.setTextColor(R.id.guessWholeItemResult, R.color.main_red);
                    helper.setText(R.id.guessWholeItemResult, "已中奖");
                    helper.setText(R.id.guessWholeActualResults, "奖励" + item.getRewardCount() + "金币");
                }
            }
        };
        guessWholeRecyclerView.setAdapter(recordAdapter);
    }

    @Override
    public void onRefresh() {
        getData();
    }

    //获取数据
    private void getData() {
        showProgress("正在加载数据");
        AppHandlerService.getLondonData(new AppHandlerService.DataCallBack() {

            @Override
            public void onGetGoldLondon(float price, String change, String limit) {

                DecimalFormat decimalFormat = new DecimalFormat(".00");
                String resultPrice = decimalFormat.format(price);
                guessWholeMainIndex.setText(resultPrice);
                guessWholeChange.setText(change);
                //涨跌比率
                String changePercent = limit + "%";
                guessWholeChangePercent.setText(changePercent);
                getGameRecord();
            }

            @Override
            public void onGetPeriodsCount(int periodsCount, boolean tread) {
                NewestNum = periodsCount;
                isTread = tread;
                setCenterTitle("全数预测(" + NewestNum + "期)");
            }

            @Override
            public void onGetDataFail() {
                hideProgress();
                ToastUtils.showShortToast("获取数据失败");
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
        submitValue();
    }

    //提交猜测的数据
    private void submitValue() {
        if (EditTextUtils.isEmpty(guessWholeFirstEdt) || EditTextUtils.isEmpty(guessWholeSecondEdt)
                || EditTextUtils.isEmpty(guessWholeThirdEdt) || EditTextUtils.isEmpty(guessWholeFourthEdt) ||
                EditTextUtils.isEmpty(guessWholeFifthEdt) || EditTextUtils.isEmpty(guessWholeSixthEdt)) {
            ToastUtils.showShortToast("请先输入预测的数据");
            return;
        }

        int firstValue = Integer.valueOf(EditTextUtils.getContent(guessWholeFirstEdt));
        int secondValue = Integer.valueOf(EditTextUtils.getContent(guessWholeSecondEdt));
        int thirdValue = Integer.valueOf(EditTextUtils.getContent(guessWholeThirdEdt));
        int fourthValue = Integer.valueOf(EditTextUtils.getContent(guessWholeFourthEdt));
        int fifthValue = Integer.valueOf(EditTextUtils.getContent(guessWholeFifthEdt));
        int sixthValue = Integer.valueOf(EditTextUtils.getContent(guessWholeSixthEdt));

        //记录值
        final GuessWholeRecord record = new GuessWholeRecord();
        record.setUserId(Application.userInstance.getObjectId());
        record.setBetStatus(0);
        record.setPeriodNum(NewestNum);
        float value = firstValue * 1000 + secondValue * 100 +
                thirdValue * 10 + fourthValue + fifthValue / 10f + sixthValue / 100f;
        LogUtils.i("数据：" + value);
        record.setGuessValue(new BigDecimal(value).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());

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
                        ToastUtils.showShortToast("请输入正确的金币数");
                    } else if (value > Application.userInstance.getGoldValue()) {
                        ToastUtils.showShortToast("金币不够，请充值");
                    } else if (value % 20 == 0) {
                        record.setGoldValue(value);
                        record.save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                if (e == null) {
                                    ToastUtils.showShortToast("操作成功");
                                    guessWholeFirstEdt.setText("");
                                    guessWholeSecondEdt.setText("");
                                    guessWholeThirdEdt.setText("");
                                    guessWholeFourthEdt.setText("");
                                    guessWholeFifthEdt.setText("");
                                    guessWholeSixthEdt.setText("");
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
    }

    //获取操作记录
    private void getGameRecord() {
        if (null == Application.userInstance) {
            hideProgress();
            return;
        }
        BmobQuery<GuessWholeRecord> query = new BmobQuery<>();
        //查询playerName叫“比目”的数据
        query.addWhereEqualTo("userId", Application.userInstance.getObjectId());
        query.order("-createdAt"); //倒序
        query.setLimit(20); //最多10条
        //执行查询方法
        query.findObjects(new FindListener<GuessWholeRecord>() {
            @Override
            public void done(List<GuessWholeRecord> list, BmobException e) {
                hideProgress();
                guessWholeRefreshWidget.setRefreshing(false);
                if (e == null) {
                    if (null != list && list.size() > 0) {
                        record_layout.setVisibility(View.VISIBLE);
                        guessWholeResultTime.setText("");
                        List<Integer> numList = new ArrayList<>();
                        for (GuessWholeRecord forecastRecord : list) {
                            if (forecastRecord.getHandlerFlag() == 0) {
                                numList.add(forecastRecord.getPeriodNum());
                            }
                        }
                        List<Integer> integerList = removeDuplicate(numList);
                        gameResultHandler(list, integerList);
                    }else {
                        recordAdapter.clear();
                        record_layout.setVisibility(View.GONE);
                        guessWholeResultTime.setText("请输入你想要预测的指数信息");
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
    private void gameResultHandler(final List<GuessWholeRecord> recordList, List<Integer> periodNums) {
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
                    class ComparatorUser implements Comparator<GuessWholeRecord> {
                        @Override
                        public int compare(GuessWholeRecord u1, GuessWholeRecord u2) {
                            return u2.getCreatedAt().compareTo(u1.getCreatedAt());
                        }
                    }
                    Comparator<GuessWholeRecord> cmp = new ComparatorUser();
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
     * 获取更新的数据
     *
     * @param list
     * @param recordList
     * @return
     */
    public List<BmobObject> getGameBmobBatch(List<LondonGold> list, List<GuessWholeRecord> recordList) {
        LogUtils.e("伦敦金数据：" + list.size());
        LogUtils.e("记录数据：" + recordList.size());
        final List<BmobObject> datas = new ArrayList<BmobObject>();
        for (LondonGold gold : list) {
            for (GuessWholeRecord wholeRecord : recordList) {
                if (wholeRecord.getHandlerFlag() == 0 && wholeRecord.getPeriodNum() == gold.getPeriodsNum()) {
                    float currentPrice = Float.valueOf(gold.getLatestpri());
                    //记录结果
                    wholeRecord.setRewardFlag(1);//标记为已经同步奖励
                    wholeRecord.setHandlerFlag(1);//标记为已经处理了结果
                    wholeRecord.setIndexResult(currentPrice);//中奖的实际结果
                    boolean result = wholeRecord.getGuessValue() == currentPrice;
                    wholeRecord.setBetStatus(result ? 1 : 2);
                    wholeRecord.setRewardCount(result ? 2000 : 0);
                    datas.add(wholeRecord);
                    LogUtils.e("中奖结果：" + (result ? "中奖" : "未中奖"));
                    //更新财富
                    saveWealthValue(result ? 2000 : 0);
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
                WealthDetail.Operation_Type_Whole_Reward,
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
                WealthDetail.Operation_Type_Game_Whole,
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
