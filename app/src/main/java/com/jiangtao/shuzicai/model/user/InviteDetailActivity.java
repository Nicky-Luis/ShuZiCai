package com.jiangtao.shuzicai.model.user;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ListView;

import com.blankj.utilcode.utils.LogUtils;
import com.jiangtao.shuzicai.R;
import com.jiangtao.shuzicai.basic.adpter.base_adapter_helper_listview.BaseAdapterHelper;
import com.jiangtao.shuzicai.basic.adpter.base_adapter_helper_listview.QuickAdapter;
import com.jiangtao.shuzicai.basic.base.BaseActivityWithToolBar;
import com.jiangtao.shuzicai.model.user.entry.WealthDetail;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;


public class InviteDetailActivity extends BaseActivityWithToolBar implements SwipeRefreshLayout.OnRefreshListener {

    //下拉刷新
    @BindView(R.id.invite_swipe_refresh_widget)
    SwipeRefreshLayout inviteSwipeRefreshWidget;
    //列表
    //交易记录Listview
    @BindView(R.id.inviteRecordListView)
    ListView inviteListView;

    //适配器
    private QuickAdapter<WealthDetail> inviteAdapter;


    //设置点击事件
    @OnClick({R.id.inviteTxt})
    public void OnClick(View view) {
        switch (view.getId()) {

            //邀请
            case R.id.inviteTxt: {
            }
            break;
        }
    }

    //初始化swipe
    private void initSwipeRefresh() {
        inviteSwipeRefreshWidget.setColorSchemeResources(
                android.R.color.holo_blue_light,
                android.R.color.holo_green_light,
                android.R.color.holo_red_light,
                android.R.color.holo_orange_light);
        inviteSwipeRefreshWidget.setOnRefreshListener(this);
    }


    @Override
    public void onRefresh() {
        LogUtils.i("下拉刷新");
        //下拉刷新
        initData();
    }

    //初始化ListView
    private void initListView() {
        //初始化适配器
        inviteAdapter = new QuickAdapter<WealthDetail>(this,
                R.layout.item_exchange_record_listview,

                new ArrayList<WealthDetail>()) {
            @Override
            protected void convert(BaseAdapterHelper helper, WealthDetail item) {
            }
        };
        inviteListView.setAdapter(inviteAdapter);
    }


    //初始化数据
    private void initData() {
    }

    @Override
    public int setLayoutId() {
        return R.layout.activity_invite_detail;
    }

    @Override
    protected void onInitialize() {
        initTitleBar();
        initSwipeRefresh();
        initListView();
    }

    @Override
    public void initPresenter() {

    }

    private void initTitleBar() {
        //右键
        setLeftImage(R.mipmap.ic_arrow_back_white_24dp, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        setCenterTitle("我的邀请");
    }
}
