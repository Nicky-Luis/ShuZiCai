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
import com.jiangtao.shuzicai.model.game.entry.GameInfo;
import com.jiangtao.shuzicai.model.game.entry.GuessWholeRecord;
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
    //适配器
    private QuickAdapter<GuessWholeRecord> recordAdapter;

    //设置点击事件
    @OnClick({R.id.guessWholeSubmitBtn})
    public void OnClick(View view) {
        switch (view.getId()) {

            //提交
            case R.id.guessWholeSubmitBtn: {
                submitValue();
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
                helper.setText(R.id.guessWholeActualResults, "实际结果:" + item.getIndexResult());
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
    private void getData(){
        getNewest300Index();
        getMantissaRecord();
        getPeriodsCount();
    }


    /***
     * 获取期数
     */
    private void getPeriodsCount() {
        BmobQuery<GameInfo> query = new BmobQuery<GameInfo>();
        query.addWhereEqualTo("gameType", GameInfo.type_quanshu);
        query.findObjects(new FindListener<GameInfo>() {
            @Override
            public void done(List<GameInfo> list, BmobException e) {
                if (e == null) {
                    if (list != null && list.size() > 0) {
                        setCenterTitle("全数预测(" + list.get(0).getNewestNum() + "期)");
                    }
                }
            }
        });
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
                guessWholeRefreshWidget.setRefreshing(false);
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
        guessWholeMainIndex.setText(String.valueOf(indexValue));
        guessWholeChange.setText(String.valueOf(stockIndex.getChange_value()));
        guessWholeChangePercent.setText(String.valueOf(stockIndex.getChange_percent()) + "%");

        LogUtils.i("300数据：" + stockIndex.toString());
    }

    //提交猜测的数据
    private void submitValue() {
        if (null == Application.userInstance) {
            Intent intent = new Intent(this, LoginActivity.class);
            ToastUtils.showShortToast("请先进行登录");
            startActivity(intent);
            return;
        }

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
        record.setReward(false);
        record.setTime(new BmobDate(new Date(System.currentTimeMillis())));
        record.setIndexResult(0);
        record.setRewardCount(0);
        record.setPeriodNum(100);
        float value = firstValue * 1000 + secondValue * 100 +
                thirdValue * 10 + fourthValue + fifthValue / 10f + sixthValue / 100f;
        record.setGuessValue(value);


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

    //获取操作记录
    private void getMantissaRecord() {
        if (null==Application.userInstance){
            return;
        }
        BmobQuery<GuessWholeRecord> query = new BmobQuery<>();
        //查询playerName叫“比目”的数据
        query.addWhereEqualTo("userId", Application.userInstance.getObjectId());
        query.order("-time"); //倒序
        query.setLimit(100); //最多100条
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
}
