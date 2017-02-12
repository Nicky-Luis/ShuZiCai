package com.jiangtao.shuzicai.model.mall;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bartoszlipinski.recyclerviewheader.RecyclerViewHeader;
import com.blankj.utilcode.utils.LogUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.jiangtao.shuzicai.Application;
import com.jiangtao.shuzicai.R;
import com.jiangtao.shuzicai.basic.adpter.base_adapter_helper_recyclerview.BaseAdapterHelper;
import com.jiangtao.shuzicai.basic.adpter.base_adapter_helper_recyclerview.QuickAdapter;
import com.jiangtao.shuzicai.basic.base.BaseFragment;
import com.jiangtao.shuzicai.common.event_message.WealthChangeMsg;
import com.jiangtao.shuzicai.model.mall.entry.Goods;
import com.jiangtao.shuzicai.model.mall.helper.SpacesItemDecoration;
import com.jiangtao.shuzicai.model.mall.interfaces.IMailPresenter;
import com.jiangtao.shuzicai.model.mall.presenter.MailPresenter;
import com.jiangtao.shuzicai.model.mall.view.IMailView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/***
 * 首页fragment
 */
public class MallFragment extends BaseFragment implements IMailView, SwipeRefreshLayout.OnRefreshListener {

    //
    @BindView(R.id.mallRecyclerView)
    RecyclerView mallRecyclerView;
    //
    @BindView(R.id.header)
    RecyclerViewHeader header;
    //
    @BindView(R.id.goods_swipe_refresh_widget)
    SwipeRefreshLayout mSwipeRefreshWidget;
    //金币
    @BindView(R.id.mallMyGold)
    TextView mallMyGold;
    //金币
    @BindView(R.id.mallMySilver)
    TextView mallMySilver;
    //兑换按钮
    @BindView(R.id.rechargeBtn)
    Button rechargeBtn;
    //
    //参数
    public static final String ARGS_PAGE = "args_page";
    //页数
    private int mPage;
    //适配器
    private QuickAdapter<Goods> mailAdapter;
    //presenter对象
    private IMailPresenter mailPresenter;

    //设置点击事件
    @OnClick({R.id.wealthDetailsTxt, R.id.rechargeBtn})
    public void OnClick(View view) {
        switch (view.getId()) {

            //财富详细
            case R.id.wealthDetailsTxt: {
                Intent intent = new Intent(getActivity(), ExchangeRecordActivity.class);
                startActivity(intent);
            }
            break;

            //兑换
            case R.id.rechargeBtn: {
                wealthExchange();
            }
            break;
        }
    }

    //对象实例化
    public static MallFragment newInstance(int page) {
        Bundle args = new Bundle();

        args.putInt(ARGS_PAGE, page);
        MallFragment fragment = new MallFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onGetArgument() {
        mPage = getArguments().getInt(ARGS_PAGE);
    }

    @Override
    public void initPresenter() {
        mailPresenter = new MailPresenter(this, getActivity());
    }

    @Override
    public int setLayoutId() {
        return R.layout.fragment_mall;
    }

    @Override
    public void loadLayout(View rootView) {
        initRecyclerView();
        initSwipeRefresh();
        initData();
    }

    //初始化数据
    private void initData() {
        mailAdapter.clear();
        mailPresenter.getFirstPageGoods();
        bindWealth();
    }

    //兑换框
    private void wealthExchange() {
        EditText convertEdt = new EditText(getActivity());
        convertEdt.setPadding(5,0,5,0);
        convertEdt.setInputType(InputType.TYPE_CLASS_NUMBER);
        convertEdt.setHint("输入要兑换的金额...");
        convertEdt.setBackgroundColor(getResources().getColor(R.color.main_thin_white));

        new AlertDialog.Builder(getActivity()).setTitle("1金币可兑换1000银币").setView(
                convertEdt).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).setNegativeButton("取消", null).show();
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
        mallMyGold.setText(gold);
        mallMySilver.setText(silver);
    }

    //初始化swipe
    private void initSwipeRefresh() {
        mSwipeRefreshWidget.setColorSchemeResources(
                android.R.color.holo_blue_light,
                android.R.color.holo_green_light,
                android.R.color.holo_red_light,
                android.R.color.holo_orange_light);
        mSwipeRefreshWidget.setOnRefreshListener(this);
    }

    //初始化
    private void initRecyclerView() {
        //两列
        mallRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL));
        //添加头部布局
        header.attachTo(mallRecyclerView, true);
        SpacesItemDecoration decoration = new SpacesItemDecoration(16);
        mallRecyclerView.addItemDecoration(decoration);

        //adapter初始化
        mailAdapter = new QuickAdapter<Goods>(getContext(), R.layout.item_mall_recyclerview,
                new ArrayList<Goods>()) {
            @Override
            protected void convert(BaseAdapterHelper helper, final Goods item) {
                helper.setImageUrl(R.id.mall_item_img, item.getGoodsImgUrl());
                helper.setText(R.id.mall_item_title, item.getGoodsName());
                helper.setText(R.id.mall_item_price, item.getGoodsPrice() + " 金币");
                //点击事件
                helper.setOnClickListener(R.id.goods_item_ly, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), GoodsExchangeActivity.class);
                        Bundle bd = new Bundle();
                        bd.putSerializable(GoodsExchangeActivity.Intent_Key, item);
                        intent.putExtras(bd);
                        startActivity(intent);
                    }
                });
            }
        };
        mallRecyclerView.setAdapter(mailAdapter);
    }

    /**
     * 主线程处理接收到的数据
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEventMainThread(String event) {
        Log.e("event MainThread", "消息： " + event + "  thread: " +
                Thread.currentThread().getName());
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

    @Override
    public void onGetGoods(List<Goods> goodses) {
        mSwipeRefreshWidget.setRefreshing(false);
        mailAdapter.addAll(goodses);
    }

    @Override
    public void onGetGoodsFailed() {
        mSwipeRefreshWidget.setRefreshing(false);
        ToastUtils.showLongToast("获取数据失败");
    }

    @Override
    public void onRefresh() {
        LogUtils.i("下拉刷新");
        //下拉刷新
        initData();
    }
}