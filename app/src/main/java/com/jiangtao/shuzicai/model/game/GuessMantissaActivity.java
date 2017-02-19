package com.jiangtao.shuzicai.model.game;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.jiangtao.shuzicai.basic.utils.EditTextUtils;
import com.jiangtao.shuzicai.model.game.entry.GuessMantissaRecord;
import com.jiangtao.shuzicai.model.home.entry.StockIndex;
import com.jiangtao.shuzicai.model.mall.helper.SpacesItemDecoration;
import com.jiangtao.shuzicai.model.user.LoginActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

import static com.jiangtao.shuzicai.model.game.entry.GuessMantissaRecord.Guess_Type_DoubleDirect;
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
    //指数数据
    private char[] hushenIndexArray;
    private char[] goldIndexArray;
    //当前的游戏类型
    private int currentGuessType = GuessMantissaRecord.Guess_Type_Percentile;
    //handler
    private Handler mHandler = new Handler();
    //适配器
    private QuickAdapter<GuessMantissaRecord> recordAdapter;

    //设置点击事件
    @OnClick({R.id.hushenSubmitBtn, R.id.goldSubmitBtn})
    public void OnClick(View view) {
        switch (view.getId()) {

            //提交
            case R.id.hushenSubmitBtn: {
                submitValue(GuessMantissaRecord.Index_Type_Hushen);
            }
            break;

            //提交
            case R.id.goldSubmitBtn: {
                submitValue(GuessMantissaRecord.Index_Type_Gold);
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
        setRightTitle("说明", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(GuessMantissaActivity.this, GuessMantissaDetailActivity.class);
                startActivity(intent);
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
                //指数类型
                if (item.getIndexType() == GuessMantissaRecord.Index_Type_Hushen) {
                    helper.setText(R.id.gussIndexType, "沪深300:");
                } else {
                    helper.setText(R.id.gussIndexType, "伦敦金:");
                }
                //数据类型
                if (item.getGuessType() == GuessMantissaRecord.Guess_Type_Percentile) {
                    helper.setText(R.id.gussType, "百分位直选");
                } else if (item.getGuessType() == GuessMantissaRecord.Guess_Type_DoubleDirect) {
                    helper.setText(R.id.gussType, "双数直选");
                } else {
                    helper.setText(R.id.gussType, "双数组选");
                }

                helper.setText(R.id.guss_mantissa_period_count, "第" + item.getPeriodNum() + "期");
                helper.setText(R.id.actualResults, "实际结果:" + item.getIndexResult());
                helper.setText(R.id.gussMantissaResult, "预测结果：" + item.getGuessValue());
            }
        };
        mantissaRecyclerView.setAdapter(recordAdapter);
    }

    //获取数据
    private void getDate() {
        getNewestGoldIndex();
        getNewest300Index();
        getMantissaRecord();
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
                        currentGuessType = GuessMantissaRecord.Guess_Type_DoubleGroup;
                        break;
                }
                setGuessType();
            }
        });
    }

    //设置游戏类型
    private void setGuessType() {
        //设置沪深300的
        if (currentGuessType == GuessMantissaRecord.Guess_Type_Percentile
                && hushenIndexArray != null && hushenIndexArray.length > 5) {
            hushenIndexFirstEdt.setText(String.valueOf(hushenIndexArray[5]));
            hushenIndexFirstEdt.setBackgroundColor(getResources().getColor(R.color.white));
            hushenIndexFirstEdt.setEnabled(false);
        } else {
            hushenIndexFirstEdt.setText("");
            hushenIndexFirstEdt.setBackgroundColor(getResources().getColor(R.color.gray));
            hushenIndexFirstEdt.setEnabled(true);
        }
        //设置黄金的
        if (currentGuessType == GuessMantissaRecord.Guess_Type_Percentile
                && goldIndexArray != null && goldIndexArray.length > 5) {
            goldIndexFirstEdt.setText(String.valueOf(goldIndexArray[5]));
            goldIndexFirstEdt.setBackgroundColor(getResources().getColor(R.color.white));
            goldIndexFirstEdt.setEnabled(false);
        } else {
            goldIndexFirstEdt.setText("");
            goldIndexFirstEdt.setBackgroundColor(getResources().getColor(R.color.gray));
            goldIndexFirstEdt.setEnabled(true);
        }
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
        goldIndexArray = aS.toCharArray();
        goldIndexFirstNum.setText(String.valueOf(goldIndexArray[0]));
        goldIndexSecondNum.setText(String.valueOf(goldIndexArray[1]));
        goldIndexThirdNum.setText(String.valueOf(goldIndexArray[2]));
        goldIndexFourthNum.setText(String.valueOf(goldIndexArray[3]));

        LogUtils.i("黄金数据：" + stockIndex.toString());
        //根据类型再去绑定
        setGuessType();
    }

    //获取最新的沪深300信息
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

    //绑定300数据
    private void bindIndex300Value(StockIndex stockIndex) {
        float indexValue = stockIndex.getStock_value();
        hushenMainIndex.setText(String.valueOf(indexValue));
        hushenChange.setText(String.valueOf(stockIndex.getChange_value()));
        hushenChangePercent.setText(String.valueOf(stockIndex.getChange_percent()) + "%");

        String aS = String.valueOf(indexValue);
        hushenIndexArray = aS.toCharArray();

        hushenIndexFirstNum.setText(String.valueOf(hushenIndexArray[0]));
        hushenIndexSecondNum.setText(String.valueOf(hushenIndexArray[1]));
        hushenIndexThirdNum.setText(String.valueOf(hushenIndexArray[2]));
        hushenIndexFourthNum.setText(String.valueOf(hushenIndexArray[3]));

        LogUtils.i("300数据：" + stockIndex.toString());
        //根据类型再去绑定
        setGuessType();
    }

    //获取操作记录
    private void getMantissaRecord() {
        BmobQuery<GuessMantissaRecord> query = new BmobQuery<>();
        //查询playerName叫“比目”的数据
        query.addWhereEqualTo("userId", Application.userInstance.getObjectId());
        query.order("-time");
        //最多100条
        query.setLimit(100);
        //执行查询方法
        query.findObjects(new FindListener<GuessMantissaRecord>() {
            @Override
            public void done(List<GuessMantissaRecord> guessMantissaRecords, BmobException e) {
                Log.i("bmob", "返回：" + guessMantissaRecords.size());
                mantissaRefreshWidget.setRefreshing(false);
                if (e == null) {
                    recordAdapter.clear();
                    recordAdapter.addAll(guessMantissaRecords);
                } else {
                    ToastUtils.showShortToast("获取数据失败");
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }

    //提交沪深300数据
    private void submitValue(int indexType) {
        if (null == Application.userInstance) {
            Intent intent = new Intent(this, LoginActivity.class);
            ToastUtils.showShortToast("请先进行登录");
            startActivity(intent);
            return;
        }

        EditText firstEdt = hushenIndexFirstEdt;
        EditText secondEdt = hushenIndexSecondEdt;
        if (indexType == GuessMantissaRecord.Index_Type_Gold) {
            firstEdt = goldIndexFirstEdt;
            secondEdt = goldIndexSecondEdt;
        }

        if (EditTextUtils.isEmpty(firstEdt)) {
            ToastUtils.showShortToast("请先输入预测的数据");
            return;
        }
        if (currentGuessType != GuessMantissaRecord.Guess_Type_Percentile) {
            if (EditTextUtils.isEmpty(secondEdt)) {
                ToastUtils.showShortToast("请先输入预测的数据");
                return;
            }
        }

        int firstValue = Integer.valueOf(EditTextUtils.getContent(firstEdt));
        int secondValue = Integer.valueOf(EditTextUtils.getContent(secondEdt));

        //记录值
        final GuessMantissaRecord record = new GuessMantissaRecord();
        record.setUserId(Application.userInstance.getObjectId());
        record.setGuessType(currentGuessType);
        record.setReward(false);
        record.setIndexType(indexType);
        record.setTime(new BmobDate(new Date(System.currentTimeMillis())));
        record.setIndexResult(0);
        record.setRewardCount(0);
        record.setPeriodNum(100);

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
                    float value = Float.parseFloat(EditTextUtils.getContent(convertEdt));
                    if (value % 20 == 0) {
                        record.setGoldValue(value);
                        record.save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                if (e == null) {
                                    ToastUtils.showShortToast("操作成功");
                                    //延时去执行
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

}
