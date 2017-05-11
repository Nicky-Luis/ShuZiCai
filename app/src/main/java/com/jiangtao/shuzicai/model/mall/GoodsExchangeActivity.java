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
import com.hss01248.lib.MyDialogListener;
import com.hss01248.lib.StytledDialog;
import com.jiangtao.shuzicai.AppHandlerService;
import com.jiangtao.shuzicai.Application;
import com.jiangtao.shuzicai.R;
import com.jiangtao.shuzicai.basic.base.BaseActivityWithToolBar;
import com.jiangtao.shuzicai.basic.utils.EditTextUtils;
import com.jiangtao.shuzicai.common.view.city_selecter.ChangeAddressPopWindow;
import com.jiangtao.shuzicai.model.mall.entry.Goods;
import com.jiangtao.shuzicai.model.mall.entry.GoodsOrder;
import com.jiangtao.shuzicai.model.user.LoginActivity;
import com.jiangtao.shuzicai.model.user.entry.WealthDetail;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 商品兑换换
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
    private GoodsOrder getOrderObj() {
        GoodsOrder order = new GoodsOrder();
        order.setUserId(Application.userInstance.getObjectId());
        order.setUser(Application.userInstance);
        order.setGoodObj(goods);
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
        String address = EditTextUtils.getContent(addressEdt);
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

        if (goods.getGoodsPrice() > Application.userInstance.getGoldValue()) {
            ToastUtils.showShortToast("剩余的金币不足以兑换该商品,请充值");
            return;
        }

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

    //提交订单
    private void saveWealthDetailOrder(final GoodsOrder order) {
        final int afterValue = Application.userInstance.getGoldValue() - goods.getGoodsPrice();
        if (afterValue <= 0) {
            ToastUtils.showShortToast("金币不足，请充值");
            return;
        }
        //记录操作状态
        final WealthDetail wealthDetail = new WealthDetail(
                Application.userInstance.getObjectId(),
                Application.userInstance.getGoldValue(),
                afterValue,
                WealthDetail.Currency_Type_Gold,
                WealthDetail.Operation_Type_Good_Exchange,
                goods.getGoodsPrice(),
                1);
        //先保存财富详情数据
        wealthDetail.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    saveOrderValue(order, wealthDetail, afterValue);
                } else {
                    hideProgress();
                    ToastUtils.showShortToast("订单提交失败");
                    LogUtils.e("提交订单失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }

    /**
     * 保存订单数据
     *
     * @param order
     * @param wealthDetail
     */
    private void saveOrderValue(GoodsOrder order, WealthDetail wealthDetail,
                                final int afterValue) {
        order.setWealthDetail(wealthDetail);
        order.setOptUser(null);
        //保存订单
        order.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    updateWealthValue(afterValue);
                } else {
                    ToastUtils.showShortToast("订单提交失败");
                    LogUtils.e("提交订单失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }

    //更新商品信息
    public void startUpdateGoods() {
        goods.setInventory(goods.getInventory() - 1);
        goods.setSalesVolume(goods.getSalesVolume() + 1);
        if (goods.getInventory() <= 0) {
            goods.setIsOnline(0);
        }
        goods.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    ToastUtils.showShortToast("订单提交成功");
                    hideProgress();
                    finish();
                } else {
                    ToastUtils.showShortToast("数据异常");
                }
            }
        });
    }

    /**
     * 更新财富数据
     *
     * @param afterValue
     */
    private void updateWealthValue(final int afterValue) {

        Application.userInstance.setGoldValue(afterValue);
        AppHandlerService.updateWealth();
        startUpdateGoods();
    }

    //提交订单
    private void startSubmitOrder() {
        showProgress("提交订单中...");
        GoodsOrder order = getOrderObj();
        if (null != order) {
            saveWealthDetailOrder(order);
        } else {
            hideProgress();
        }
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
