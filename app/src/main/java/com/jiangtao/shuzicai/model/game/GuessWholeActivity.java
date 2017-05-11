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
import com.jiangtao.shuzicai.model.game.entry.Config;
import com.jiangtao.shuzicai.model.game.entry.GuessWholeRecord;
import com.jiangtao.shuzicai.model.game.entry.LondonGold;
import com.jiangtao.shuzicai.model.mall.helper.SpacesItemDecoration;
import com.jiangtao.shuzicai.model.user.LoginActivity;
import com.jiangtao.shuzicai.model.user.entry.WealthDetail;

import java.math.BigDecimal;
import java.text.DecimalFormat;
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
                if (item.getHandlerFlag() == 0) {
                    helper.setText(R.id.guessWholeActualResults, "实际结果: 待定");
                } else {
                    helper.setText(R.id.guessWholeActualResults, "实际结果:" + item.getIndexResult());
                }

                helper.setText(R.id.guessWholeItemResult, "预测结果：" + item.getGuessValue());
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
        getPeriodsCount();
        guessWholeResultTime.setText("");
    }


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
                    setCenterTitle("全数预测(" + NewestNum + "期)");
                    //记录是否可以交易
                    isTread = gameInfo.isTread();
                    getLondonIndex(NewestNum - 1);
                } else {
                    ToastUtils.showShortToast("获取数据失败");
                }
            }
        });
    }

    //获取最新的沪深300信息
    private void getLondonIndex(int num) {
        if (null == Application.userInstance) {
            return;
        }
        BmobQuery<LondonGold> query = new BmobQuery<LondonGold>();
        query.addWhereEqualTo("periodsNum", num);
        //执行查询方法
        query.findObjects(new FindListener<LondonGold>() {
            @Override
            public void done(List<LondonGold> stockIndices, BmobException e) {
                guessWholeRefreshWidget.setRefreshing(false);
                if (e == null) {
                    //获取到最后的是10条黄金信息
                    if (null != stockIndices && stockIndices.size() > 0) {
                        bindIndexValue(stockIndices.get(0));
                    }
                    getMantissaRecord();
                } else {
                    ToastUtils.showShortToast("获取数据失败");
                    Log.e("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }

    //绑定300数据
    private void bindIndexValue(LondonGold indexData) {
        float price = Float.valueOf(indexData.getLatestpri());
        DecimalFormat decimalFormat = new DecimalFormat(".00");
        String resultPrice = decimalFormat.format(price);
        guessWholeMainIndex.setText(resultPrice);

        guessWholeChange.setText(indexData.getChange());
        //涨跌比率
        String changePercent = indexData.getLimit() + "%";
        guessWholeChangePercent.setText(changePercent);
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

        BmobQuery<GuessWholeRecord> query = new BmobQuery<GuessWholeRecord>();
        query.addWhereEqualTo("userId", Application.userInstance.getObjectId());
        query.addWhereEqualTo("periodNum", NewestNum);
        //查询数量
        query.count(GuessWholeRecord.class, new CountListener() {
            @Override
            public void done(Integer integer, BmobException e) {
                if (e == null) {
                    if (0 == integer) {
                        submitValue();
                    } else {
                        ToastUtils.showShortToast("不能重复参该期游戏");
                    }
                } else {
                    ToastUtils.showShortToast("操作失败，请重试");
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
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
                                    getMantissaRecord();
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
    private void getMantissaRecord() {
        if (null == Application.userInstance) {
            return;
        }
        BmobQuery<GuessWholeRecord> query = new BmobQuery<>();
        //查询playerName叫“比目”的数据
        query.addWhereEqualTo("userId", Application.userInstance.getObjectId());
        query.order("-createdAt"); //倒序
        query.setLimit(10); //最多10条
        //执行查询方法
        query.findObjects(new FindListener<GuessWholeRecord>() {
            @Override
            public void done(List<GuessWholeRecord> guessWholeRecords, BmobException e) {
                Log.i("bmob", "返回：" + guessWholeRecords.size());
                guessWholeRefreshWidget.setRefreshing(false);
                if (e == null) {
                    recordAdapter.clear();
                    recordAdapter.addAll(guessWholeRecords);
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
