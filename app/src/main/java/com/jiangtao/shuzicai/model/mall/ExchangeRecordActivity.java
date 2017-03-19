package com.jiangtao.shuzicai.model.mall;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ListView;

import com.blankj.utilcode.utils.LogUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.google.gson.Gson;
import com.jiangtao.shuzicai.Application;
import com.jiangtao.shuzicai.R;
import com.jiangtao.shuzicai.basic.adpter.base_adapter_helper_listview.BaseAdapterHelper;
import com.jiangtao.shuzicai.basic.adpter.base_adapter_helper_listview.QuickAdapter;
import com.jiangtao.shuzicai.basic.base.BaseActivityWithToolBar;
import com.jiangtao.shuzicai.basic.network.APIInteractive;
import com.jiangtao.shuzicai.basic.network.BmobQueryUtils;
import com.jiangtao.shuzicai.basic.network.INetworkResponse;
import com.jiangtao.shuzicai.model.mall.entry.Goods;
import com.jiangtao.shuzicai.model.mall.entry.GoodsOrder;
import com.jiangtao.shuzicai.model.user.LoginActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ExchangeRecordActivity extends BaseActivityWithToolBar implements SwipeRefreshLayout.OnRefreshListener {


    //交易记录Listview
    @BindView(R.id.exchangeRecordListView)
    ListView exchangeRecordListView;
    //刷新
    @BindView(R.id.exchange_record_refresh_widget)
    SwipeRefreshLayout mSwipeRefreshWidget;
    //适配器
    private QuickAdapter<GoodsOrder> adapter;


    @Override
    public int setLayoutId() {
        return R.layout.activity_exchange_record;
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

    private void initTitleBar() {
        //右键
        setLeftImage(R.mipmap.ic_arrow_back_white_24dp, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        setCenterTitle("兑换记录");
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

    //初始化ListView
    private void initListView() {
        //初始化适配器
        adapter = new QuickAdapter<GoodsOrder>(this, R.layout.item_exchange_record_listview,
                new ArrayList<GoodsOrder>()) {
            @Override
            protected void convert(BaseAdapterHelper helper, GoodsOrder item) {
                helper.setText(R.id.view_orders_time,item.getOrderTime().getIso());
                helper.setText(R.id.view_orders_address,item.getAddress());
                helper.setText(R.id.view_orders_phone,item.getReceivingPhone());
                helper.setText(R.id.view_orders_people,item.getContacts());
                helper.setImageUrl(R.id.orders_goods_img,item.getGoodObj().getGoodsImgUrl());
                helper.setText(R.id.orders_goods_name_txt,item.getGoodObj().getGoodsName());
                helper.setText(R.id.orders_goods_price_txt,item.getGoodObj().getGoodsPrice()+"金币");
            }
        };
        exchangeRecordListView.setAdapter(adapter);
    }

    //获取数据
    private void getData() {
        if (null == Application.userInstance) {
            Intent intent = new Intent(this, LoginActivity.class);
            ToastUtils.showShortToast("请先进行登录");
            startActivity(intent);
            return;
        }

        BmobQueryUtils utils = BmobQueryUtils.newInstance();
        String where = utils.setValue("userId").equal(Application.userInstance.getObjectId());

        APIInteractive.getExchangeRecord(where,"goods", new INetworkResponse() {
            @Override
            public void onFailure(int code) {
                mSwipeRefreshWidget.setRefreshing(false);
                ToastUtils.showShortToast("获取数据失败");
            }

            @Override
            public void onSucceed(JSONObject result) {
                mSwipeRefreshWidget.setRefreshing(false);
                LogUtils.i("result = " + result);
                List<GoodsOrder> orders =new ArrayList<>();
                try {
                    JSONArray jArray = result.optJSONArray("results");
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject object = (JSONObject) jArray.get(i);
                        Gson gson = new Gson();
                        GoodsOrder order= gson.fromJson(object.toString(),GoodsOrder.class);

                        JSONObject goodsObject = object.optJSONObject("goods");
                        Gson goodsGson = new Gson();
                        Goods good = goodsGson.fromJson(goodsObject.toString(),Goods.class);
                        order.setGoodObj(good);
                        orders.add(order);
                    }
                    adapter.clear();
                    adapter.addAll(orders);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        getData();
    }
}
