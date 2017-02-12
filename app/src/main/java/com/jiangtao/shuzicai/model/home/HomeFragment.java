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
import com.jiangtao.shuzicai.model.home.interfaces.IHomeFragmentPresenter;
import com.jiangtao.shuzicai.model.home.presenter.HomeFragmentPresenter;
import com.jiangtao.shuzicai.model.home.view.IHomeFragmentView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;
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
    private IHomeFragmentPresenter presenter;
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
    public void onUpdateIndexData(List<TrendModel> datas) {
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
        homeIndexTime.setText(indexData.getDate().getIso().substring(0, 9));
        //构造方法的字符格式这里如果小数不足2位,会以0补足.
        DecimalFormat decimalFormat = new DecimalFormat(".00");
        String date = decimalFormat.format(indexData.getStock_value());
        homeIndexMainData.setText(date);

        //成交量
        String volume = "成交额(万元):" + decimalFormat.format(indexData.getTurnover_volume());
        homeIndexTransaction.setText(volume);
        //成交笔数
        String count = "成交量(万手):" + decimalFormat.format(indexData.getTurnover_count());
        homeIndexTotal.setText(count);

        //成交笔数
        String changeValue = decimalFormat.format(indexData.getChange_value());
        String changePercent = decimalFormat.format(indexData.getChange_percent()) + "%";
        homeIndexChange.setText(changeValue + "   " + changePercent);
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
        homeIndexMainData.setText("--");
        homeIndexTransaction.setText("成交额(万元)--");
        homeIndexTotal.setText("成交量(万手)--");
        homeIndexChange.setText("--");
    }

    //绑定财富数据
    private void bindWealth() {
        //绑定财富数据
        String gold = "金币：0";
        String silver = "银币：0";
        if (null != Application.wealthValue) {
            gold = "金币：" + String.valueOf((int) Application.wealthValue.getGoldValue());
            silver = "银币：" + String.valueOf((int) Application.wealthValue.getSilverValue());
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