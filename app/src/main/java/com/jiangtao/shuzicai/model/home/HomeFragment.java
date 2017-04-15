package com.jiangtao.shuzicai.model.home;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.blankj.utilcode.utils.LogUtils;
import com.jiangtao.shuzicai.Application;
import com.jiangtao.shuzicai.R;
import com.jiangtao.shuzicai.basic.base.BaseFragment;
import com.jiangtao.shuzicai.common.event_message.WealthChangeMsg;
import com.jiangtao.shuzicai.common.view.billboard_view.BillboardView;
import com.jiangtao.shuzicai.common.view.billboard_view.model.BillboardMessage;
import com.jiangtao.shuzicai.common.view.trend_view.TrendView;
import com.jiangtao.shuzicai.common.view.trend_view.model.TrendModel;
import com.jiangtao.shuzicai.model.home.entry.StockIndex;
import com.jiangtao.shuzicai.model.home.presenter.HomeFragmentPresenter;
import com.jiangtao.shuzicai.model.home.view.IHomeFragmentView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;

/***
 * 首页fragment
 */
public class HomeFragment extends BaseFragment implements IHomeFragmentView {
    //参数
    public static final String ARGS_PAGE = "args_page";
    //页数
    private int mPage;
    //binding对象
    List<BillboardMessage> billboardMessages;
    //页数
    private HomeFragmentPresenter presenter;
    //趋势图
    @BindView(R.id.mainTrendView)
    TrendView mainTrendView;
    //公告栏
    @BindView(R.id.mainBillboardView)
    BillboardView mainBillboardView;
    //公告栏
    @BindView(R.id.indexRadioGroup)
    RadioGroup indexRadioGroup;
    //时间
    @BindView(R.id.homeIndexTime)
    TextView homeIndexTime;
    //指数值
    @BindView(R.id.homeIndexMainData)
    TextView homeIndexMainData;
    //成交
    @BindView(R.id.homeIndexTransaction)
    TextView homeIndexTransaction;
    //变化
    @BindView(R.id.homeIndexChange)
    TextView homeIndexChange;
    //总计
    @BindView(R.id.homeIndexTotal)
    TextView homeIndexTotal;
    //财富
    @BindView(R.id.homeMyGold)
    TextView homeMyGold;
    //财富
    @BindView(R.id.homeMySilver)
    TextView homeMySilver;


    //对象实例化
    public static HomeFragment newInstance(int page) {
        Bundle args = new Bundle();

        args.putInt(ARGS_PAGE, page);
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onGetArgument() {
        mPage = getArguments().getInt(ARGS_PAGE);
    }

    @Override
    public void initPresenter() {
        presenter = new HomeFragmentPresenter(getContext(), this);
        //查询数据
        presenter.getIndexData();
        presenter.getBillboardData();
    }

    @Override
    public int setLayoutId() {
        return R.layout.fragment_home;
    }


    @Override
    public void loadLayout(View rootView) {
        //点击事件
        indexRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId) {
                    case R.id.homeIndexSSEBtn:
                        presenter.setIndexType(StockIndex.Type_ShangZheng);
                        break;

                    case R.id.homeIndexCISBtn:
                        presenter.setIndexType(StockIndex.Type_HuShen);
                        break;

                    case R.id.homeIndexSCIBtn:
                        presenter.setIndexType(StockIndex.Type_ShenZheng);
                        break;

                    case R.id.homeIndexGEIBtn:
                        presenter.setIndexType(StockIndex.Type_chuangYe);
                        break;
                }
            }
        });
    }

    /**
     * 显示隐藏
     *
     * @param isVisibleToUser
     */
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            LogUtils.i("---------显示----------");
            if (null != mainBillboardView) {
                //   mainBillboardView.setScrollDataList(billboardMessages).startScrollView();
            }
        } else {
            LogUtils.i("---------隐藏----------");
            if (null != mainBillboardView) {
                //  mainBillboardView.stopScrollView();
            }
        }
    }

    /**
     * 主线程处理接收到的数据
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEventMainThread(String event) {
        Log.e("event MainThread", "消息： " + event + "  thread: " + Thread.currentThread().getName());
    }

    @Override
    public void onUpdateIndexData(List<TrendModel> datas, String type) {
        //设置名称
        switch (type) {
            case StockIndex.Type_ShangZheng:
                mainTrendView.setName("上证指数");
                break;

            case StockIndex.Type_HuShen:
                mainTrendView.setName("沪深300");
                break;

            case StockIndex.Type_ShenZheng:
                mainTrendView.setName("深证成指");
                break;

            case StockIndex.Type_chuangYe:
                mainTrendView.setName("创业板指");
                break;
        }
        //交易数据
        mainTrendView.bindTrendData(datas);
    }

    @Override
    public void bindIndexData(StockIndex indexData) {
        resetIndexViewValue();
        bindWealth();
        if (null == indexData) {
            return;
        }
        homeIndexTime.setText(indexData.getTime().substring(0, 10));
        //指数值
        float price = Float.valueOf(indexData.getNowPrice());
        float resultPrice = new BigDecimal(price).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
        homeIndexMainData.setText(String.valueOf(resultPrice));
        //成交量
        float amount = Float.valueOf(indexData.getTradeAmount()) / (10000 * 10000);
        float resultAmount = new BigDecimal(amount).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
        String volume = "成交额:" + resultAmount + "亿元";
        homeIndexTransaction.setText(volume);
        //成交笔数
        float count = Float.valueOf(indexData.getTradeNum()) / (10000);
        float resultCount = new BigDecimal(count).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
        String countStr = "成交量:" + resultCount + "万手";
        homeIndexTotal.setText(countStr);
        //涨跌比率
        String changePercent = indexData.getDiff_rate() + "%";
        homeIndexChange.setText(indexData.getDiff_money() + "   " + changePercent);
    }

    @Override
    public void bindBillboardData(List<BillboardMessage> datas) {
        LogUtils.i("---------绑定公告数据：" + datas.size());
        //广播数据
        billboardMessages = datas;
        //绑定数据
        mainBillboardView.setScrollDataList(datas).startScrollView();
    }

    //清除数据
    private void resetIndexViewValue() {
        Calendar calendar = Calendar.getInstance();
        homeIndexTime.setText(calendar.get(Calendar.YEAR) + "-" +
                (calendar.get(Calendar.MONTH) + 1) + "-" +
                calendar.get(Calendar.DAY_OF_MONTH));
        homeIndexMainData.setText("");
        homeIndexTransaction.setText("成交额");
        homeIndexTotal.setText("成交量");
        homeIndexChange.setText("");
    }

    //绑定财富数据
    private void bindWealth() {
        //绑定财富数据
        String gold = "金币：0";
        String silver = "银币：0";
        if (null != Application.userInstance) {
            gold = "金币：" + String.valueOf((int) Application.userInstance.getGoldValue());
            silver = "银币：" + String.valueOf((int) Application.userInstance.getSilverValue());
        }
        homeMyGold.setText(gold);
        homeMySilver.setText(silver);
    }

    /**
     * 财富发生了变化
     *
     * @param msg
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEventMainThread(WealthChangeMsg msg) {
        bindWealth();
    }
}