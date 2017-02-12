package com.jiangtao.shuzicai.model.mall;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.utils.LogUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hss01248.lib.MyDialogListener;
import com.hss01248.lib.StytledDialog;
import com.jiangtao.shuzicai.Application;
import com.jiangtao.shuzicai.R;
import com.jiangtao.shuzicai.basic.base.BaseActivityWithToolBar;
import com.jiangtao.shuzicai.basic.network.APIInteractive;
import com.jiangtao.shuzicai.basic.network.BmobQueryUtils;
import com.jiangtao.shuzicai.basic.network.INetworkResponse;
import com.jiangtao.shuzicai.basic.utils.EditTextUtils;
import com.jiangtao.shuzicai.common.entity.BmobBatch;
import com.jiangtao.shuzicai.common.entity.BmobDate;
import com.jiangtao.shuzicai.common.entity.BmobPointer;
import com.jiangtao.shuzicai.common.event_message.WealthChangeMsg;
import com.jiangtao.shuzicai.common.view.city_selecter.ChangeAddressPopWindow;
import com.jiangtao.shuzicai.model.mall.entry.Goods;
import com.jiangtao.shuzicai.model.mall.entry.Order;
import com.jiangtao.shuzicai.model.user.LoginActivity;
import com.jiangtao.shuzicai.model.user.entry.WealthDetail;
import com.jiangtao.shuzicai.model.user.entry.WealthValue;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 商品交换
 */
public class GoodsExchangeActivity extends BaseActivityWithToolBar {

    public final static String Intent_Key = "goods";
    private Goods goods;
    //
    @BindView(R.id.goods_img)
    SimpleDraweeView goodsImg;
    //
    @BindView(R.id.goods_title)
    TextView goodsName;
    //
    @BindView(R.id.goods_price)
    TextView goodsPrice;
    //详情
    @BindView(R.id.goods_detail)
    TextView goodsDetail;

    @BindView(R.id.ship_address_title)
    TextView addressTitle;
    //地址详情
    @BindView(R.id.ship_address_detail_edt)
    EditText addressEdt;
    //地址详情
    @BindView(R.id.contacts_edt)
    EditText contactsEdt;

    //电话
    @BindView(R.id.phone_edt)
    EditText phoneEdt;
    //
    private String addressPre = "广东-广州-番禺";


    //设置点击事件
    @OnClick({R.id.ship_address_title, R.id.confirmBtn})
    public void OnClick(View view) {
        switch (view.getId()) {

            case R.id.ship_address_title:
                selectCity();
                break;

            case R.id.confirmBtn:
                confirmDialog();
                break;
        }
    }

    @Override
    public int setLayoutId() {
        return R.layout.activity_goods_exchange;
    }

    @Override
    protected void onInitialize() {
        initTitleBar();
        goods = (Goods) getIntent().getSerializableExtra(Intent_Key);
        setViewData();
    }

    @Override
    public void initPresenter() {

    }

    //初始化标头
    private void initTitleBar() {
        //右键
        setLeftImage(R.mipmap.ic_arrow_back_white_24dp, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        setCenterTitle("商品详情");
    }

    //获取订单信息
    private Order getOrderObj() {
        Order order = new Order();
        order.setGoods(new BmobPointer("Goods", goods.getObjectId()));
        order.setUserId(Application.userInstance.getObjectId());
        if (EditTextUtils.isEmpty(phoneEdt)) {
            ToastUtils.showShortToast("电话不能为空");
            return null;
        }
        String phone = EditTextUtils.getContent(phoneEdt);
        order.setReceivingPhone(phone);
        //地址
        if (EditTextUtils.isEmpty(addressEdt)) {
            ToastUtils.showShortToast("地址不能为空");
            return null;
        }
        String address = EditTextUtils.getContent(phoneEdt);
        order.setAddress(addressPre + address);
        //联系人
        if (EditTextUtils.isEmpty(contactsEdt)) {
            ToastUtils.showShortToast("联系人不能为空");
            return null;
        }
        String contact = EditTextUtils.getContent(contactsEdt);
        order.setContacts(contact);
        //状态
        order.setOrderStatus(0);
        return order;
    }

    //弹出确认框
    private void confirmDialog() {
        if (null == Application.userInstance) {
            Intent intent = new Intent(this, LoginActivity.class);
            ToastUtils.showShortToast("请先进行登录");
            startActivity(intent);
            return;
        }

        if (goods.getGoodsPrice() > Application.wealthValue.getGoldValue()) {
            StytledDialog.showMdAlert(this, "系统提示", "剩余的金币不足以兑换该商品",
                    "确认", "取消", null, false, true, new MyDialogListener() {

                        @Override
                        public void onFirst(DialogInterface dialogInterface) {
                        }

                        @Override
                        public void onSecond(DialogInterface dialogInterface) {
                        }
                    }).show();
        } else {
            StytledDialog.showMdAlert(this, "系统提示", "提交订单将消费" + goods.getGoodsPrice() +
                            "金币，是否确认？",
                    "确认", "取消", null, false, true, new MyDialogListener() {

                        @Override
                        public void onFirst(DialogInterface dialogInterface) {
                            startSubmitOrder();
                        }

                        @Override
                        public void onSecond(DialogInterface dialogInterface) {

                        }
                    }).show();
        }
    }

    //提交订单
    private void submitOrder(Order order) {
        List<BmobBatch> batches = new ArrayList<>();
        float afterValue = Application.wealthValue.getGoldValue() - goods.getGoodsPrice();
        //记录操作状态
        WealthDetail wealthDetail = new WealthDetail(Application.wealthValue.getGoldValue(),
                afterValue, WealthDetail.Currency_Type_Gold, WealthDetail.Operation_Type_Exchange, goods
                .getGoodsPrice(), Application.userInstance.getObjectId());
        BmobBatch recordBatch = new BmobBatch("POST", "/1/classes/WealthDetail", wealthDetail);
        batches.add(recordBatch);
        //添加订单信息
        BmobBatch orderBatch = new BmobBatch("POST", "/1/classes/goodsOrder", order);
        batches.add(orderBatch);
        //修改财富数据
        WealthValue wealthValue = new WealthValue();
        Application.wealthValue.setGoldValue(afterValue);
        wealthValue.setGoldValue(Application.wealthValue.getGoldValue());
        wealthValue.setSilverValue(Application.wealthValue.getSilverValue());
        BmobBatch wealthBatch = new BmobBatch("PUT", "/1/classes/WealthValue/" + Application.wealthValue.getObjectId
                (), wealthValue);
        batches.add(wealthBatch);
        //获取最终封装好的batch
        Map<String, List> batchBean = BmobBatch.getBatchCmd(batches);

        APIInteractive.bmobBatch(batchBean, new INetworkResponse() {
            @Override
            public void onFailure(int code) {
                hideProgress();
                ToastUtils.showShortToast("订单提交失败");
            }

            @Override
            public void onSucceed(JSONObject result) {
                hideProgress();
                int resultCount = 0;
                try {
                    JSONArray jsonArray = result.optJSONArray("results");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = (JSONObject) jsonArray.get(i);
                        if (object.has("success")) {
                            resultCount++;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //只有两个值都成功时才提示成功
                if (3 == resultCount) {
                    ToastUtils.showShortToast("订单提交成功");
                    getWealthValue();
                    finish();
                } else {
                    ToastUtils.showShortToast("订单提交异常");
                }
            }
        });
    }

    /**
     * 获取财富
     */
    private void getWealthValue() {
        if (null == Application.userInstance) {
            return;
        }
        String userId = Application.userInstance.getObjectId();

        BmobQueryUtils utils = BmobQueryUtils.newInstance();
        String where = utils.setValue("userId").equal(userId);

        //获取当前用户的信息
        APIInteractive.getWealthValue(where, new INetworkResponse() {
            @Override
            public void onFailure(int code) {
                LogUtils.e("更新数据失败");
            }

            @Override
            public void onSucceed(JSONObject result) {
                //更新数据
                try {
                    String jArray = result.optString("results");
                    List<WealthValue> wealthValues = new Gson().fromJson(jArray, new TypeToken<List<WealthValue>>() {
                    }.getType());
                    if (wealthValues.size() > 0) {
                        Application.wealthValue = wealthValues.get(0);
                        EventBus.getDefault().post(new WealthChangeMsg(wealthValues.get(0)));
                    } else {
                        ToastUtils.showShortToast("财富数据更新失败");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //提交订单
    private void startSubmitOrder() {
        showProgress("提交订单中...");
        APIInteractive.getServerTime(new INetworkResponse() {
            @Override
            public void onFailure(int code) {
                hideProgress();
                ToastUtils.showShortToast("订单提交失败");
            }

            @Override
            public void onSucceed(JSONObject result) {
                Order order = getOrderObj();
                if (null != order) {
                    try {
                        String time = result.optString("datetime");
                        order.setOrderTime(new BmobDate("Date", time));
                        submitOrder(order);
                    } catch (Exception e) {
                        e.printStackTrace();
                        ToastUtils.showShortToast("订单提交失败");
                    }
                }
            }
        });
    }

    //设置数据
    private void setViewData() {
        Uri uri = Uri.parse(goods.getGoodsImgUrl());
        goodsImg.setImageURI(uri);

        addressTitle.setText(addressPre);
        goodsName.setText(goods.getGoodsName());
        goodsPrice.setText(goods.getGoodsPrice() + "金币");
        goodsDetail.setText("说明：" + goods.getGoodsDetail());
    }

    /**
     * 选择收货城市
     */
    private void selectCity() {
        ChangeAddressPopWindow mChangeAddressPopwindow = new ChangeAddressPopWindow(GoodsExchangeActivity.this);
        mChangeAddressPopwindow.setAddress("广东", "广州", "天河区");
        mChangeAddressPopwindow.showAtLocation(addressTitle, Gravity.BOTTOM, 0, 0);
        mChangeAddressPopwindow
                .setAddresskListener(new ChangeAddressPopWindow.OnAddressCListener() {

                    @Override
                    public void onClick(String province, String city, String area) {
                        addressPre = province + "-" + city + "-" + area;
                        addressTitle.setText(addressPre);
                    }
                });
    }
}
