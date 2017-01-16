package com.jiangtao.shuzicai.model.home;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.blankj.utilcode.utils.LogUtils;
import com.jiangtao.shuzicai.R;
import com.jiangtao.shuzicai.basic.base.BaseFragment;
import com.jiangtao.shuzicai.common.view.billboard_view.BillboardView;
import com.jiangtao.shuzicai.common.view.billboard_view.model.MessageTools;
import com.jiangtao.shuzicai.common.view.billboard_view.model.ScrollMessage;
import com.jiangtao.shuzicai.common.view.trend_view.TrendView;
import com.jiangtao.shuzicai.common.view.trend_view.model.DataTools;
import com.jiangtao.shuzicai.model.home.interfaces.IHomeFragmentPresenter;
import com.jiangtao.shuzicai.model.home.presenter.HomeFragmentPresenter;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;

/***
 * 首页fragment
 */
public class HomeFragment extends BaseFragment {
    //参数
    public static final String ARGS_PAGE = "args_page";
    //页数
    private int mPage;
    //binding对象
    List<ScrollMessage> scrollDataList;
    //页数
    private IHomeFragmentPresenter presenter;
    //趋势图
    @BindView(R.id.mainTrendView)
    TrendView mainTrendView;
    //公告栏
    @BindView(R.id.mainBillboardView)
    BillboardView mainBillboardView;

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
        presenter = new HomeFragmentPresenter();
        presenter.getIndexData();
    }

    @Override
    public int setLayoutId() {
        return R.layout.fragment_home;
    }


    @Override
    public void loadLayout(View rootView) {
        scrollDataList = MessageTools.getMessageDatas();
        //绑定数据
        mainBillboardView.setScrollDataList(scrollDataList).startScrollView();
        //绑定数据/
        mainTrendView.bindTrendData(DataTools.getTrendDatas());
    }

    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            LogUtils.i("---------显示----------");
            if (null != mainBillboardView) {
                mainBillboardView.setScrollDataList(scrollDataList).startScrollView();
            }
        } else {
            LogUtils.i("---------隐藏----------");
            if (null != mainBillboardView) {
                mainBillboardView.stopScrollView();
            }
        }
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {

        } else {

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
}