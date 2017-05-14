package com.jiangtao.shuzicai.model.user;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.blankj.utilcode.utils.ToastUtils;
import com.jiangtao.shuzicai.Application;
import com.jiangtao.shuzicai.R;
import com.jiangtao.shuzicai.basic.adpter.base_adapter_helper_listview.BaseAdapterHelper;
import com.jiangtao.shuzicai.basic.adpter.base_adapter_helper_listview.QuickAdapter;
import com.jiangtao.shuzicai.basic.base.BaseActivityWithToolBar;
import com.jiangtao.shuzicai.model.user.entry.WealthDetail;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class WealthDetailActivity extends BaseActivityWithToolBar
        implements SwipeRefreshLayout.OnRefreshListener {

    //交易记录Listview
    @BindView(R.id.wealthDetailRecordListView)
    ListView wealthDetailRecordListView;
    //刷新
    @BindView(R.id.wealth_detail_record_refresh_widget)
    SwipeRefreshLayout wealthDetailSwipe;
    //适配器
    private QuickAdapter<WealthDetail> adapter;

    @BindView(R.id.tishi_empty)
    TextView tishi_empty;

    @Override
    public int setLayoutId() {
        return R.layout.activity_wealth_detail;
    }

    @Override
    protected void onInitialize() {
        initTitleBar();
        initSwipeRefresh();
        initListView();
        getData();
    }

    @Override
    public void initPresenter() {

    }

    //初始化swipe
    private void initSwipeRefresh() {
        wealthDetailSwipe.setColorSchemeResources(
                android.R.color.holo_blue_light,
                android.R.color.holo_green_light,
                android.R.color.holo_red_light,
                android.R.color.holo_orange_light);
        wealthDetailSwipe.setOnRefreshListener(this);
    }

    //初始化ListView
    private void initListView() {
        //初始化适配器
        adapter = new QuickAdapter<WealthDetail>(this, R.layout.item_wealth_record_listview,
                new ArrayList<WealthDetail>()) {
            @Override
            protected void convert(BaseAdapterHelper helper, WealthDetail item) {
                helper.setText(R.id.wealth_detail_time, item.getCreatedAt());
                String type = "充值";
                switch (item.getOperationType()) {
                    case WealthDetail.Operation_Type_Recharge:
                        type = "充值";
                        break;

                    case WealthDetail.Operation_Type_Good_Exchange:
                        type = "兑换礼品";
                        break;

                    case WealthDetail.Operation_Type_Forecast_Reward:
                    case WealthDetail.Operation_Type_Mantisssa_Reward:
                    case WealthDetail.Operation_Type_Whole_Reward:
                        type = "获奖";
                        break;

                    case WealthDetail.Operation_Type_Game_Forecast:
                    case WealthDetail.Operation_Type_Game_Mantisssa:
                    case WealthDetail.Operation_Type_Game_Whole:
                        type = "游戏消耗";
                        break;

                    case WealthDetail.Operation_Type_Wealth_Exchange:
                        type = "兑换成银元";
                        break;

                }
                helper.setText(R.id.wealth_detail_type, type);
                String valueType = item.getCurrencyType() == 0 ? "金币" : "银元";
                helper.setText(R.id.wealth_detail_value, item.getOperationValue() + ":" + valueType);
            }
        };
        wealthDetailRecordListView.setAdapter(adapter);
    }


    //初始化标头栏
    private void initTitleBar() {
        //右键
        setLeftImage(R.mipmap.ic_arrow_back_white_24dp, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        setCenterTitle("财富明细");
    }

    //获取数据
    private void getData() {
        if (null == Application.userInstance) {
            Intent intent = new Intent(this, LoginActivity.class);
            ToastUtils.showShortToast("请先进行登录");
            startActivity(intent);
            return;
        }

        showProgress("正在加载数据");
        BmobQuery<WealthDetail> query = new BmobQuery<WealthDetail>();
        query.addWhereEqualTo("userId", Application.userInstance.getObjectId());
        query.setLimit(20);
        query.order("-createdAt");
        query.findObjects(new FindListener<WealthDetail>() {
            @Override
            public void done(List<WealthDetail> list, BmobException e) {
                wealthDetailSwipe.setRefreshing(false);
                hideProgress();
                if (e == null) {
                    if (null != list && list.size() > 0) {
                        tishi_empty.setVisibility(View.GONE);
                        adapter.clear();
                        adapter.addAll(list);
                    } else {
                        tishi_empty.setVisibility(View.VISIBLE);
                        ToastUtils.showShortToast("没有数据记录");
                    }
                } else {
                    ToastUtils.showShortToast("获取数据失败");
                }
            }
        });

    }

    @Override
    public void onRefresh() {
        getData();
    }
}
