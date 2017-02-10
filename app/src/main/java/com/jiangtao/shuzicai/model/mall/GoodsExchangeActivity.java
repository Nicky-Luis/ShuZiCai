package com.jiangtao.shuzicai.model.mall;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.utils.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.hss01248.lib.MyDialogListener;
import com.hss01248.lib.StytledDialog;
import com.jiangtao.shuzicai.Application;
import com.jiangtao.shuzicai.R;
import com.jiangtao.shuzicai.basic.base.BaseActivityWithToolBar;
import com.jiangtao.shuzicai.basic.network.APIInteractive;
import com.jiangtao.shuzicai.basic.network.INetworkResponse;
import com.jiangtao.shuzicai.basic.utils.EditTextUtils;
import com.jiangtao.shuzicai.common.entity.BmobDate;
import com.jiangtao.shuzicai.common.entity.BmobPointer;
import com.jiangtao.shuzicai.common.view.city_selecter.ChangeAddressPopWindow;
import com.jiangtao.shuzicai.model.mall.entry.Goods;
import com.jiangtao.shuzicai.model.mall.entry.Order;
import com.jiangtao.shuzicai.model.user.LoginActivity;

import org.json.JSONObject;

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
                confirmDilog();
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

    //弹出确认框
    private void confirmDilog() {
        if (null == Application.userInstance) {
            Intent intent = new Intent(this, LoginActivity.class);
            ToastUtils.showShortToast("请先进行登录");
            startActivity(intent);
            return;
        }

        final Order order = new Order();
        order.setGoods(new BmobPointer("Goods", goods.getObjectId()));
        order.setUserId(Application.userInstance.getObjectId());
        if (EditTextUtils.isEmpty(phoneEdt)) {
            ToastUtils.showShortToast("电话不能为空");
            return;
        }
        String phone = EditTextUtils.getContent(phoneEdt);
        order.setReceivingPhone(phone);
        //地址
        if (EditTextUtils.isEmpty(addressEdt)) {
            ToastUtils.showShortToast("地址不能为空");
            return;
        }
        String address = EditTextUtils.getContent(phoneEdt);
        order.setAddress(addressPre + address);
        //联系人
        if (EditTextUtils.isEmpty(contactsEdt)) {
            ToastUtils.showShortToast("联系人不能为空");
            return;
        }
        String contact = EditTextUtils.getContent(phoneEdt);
        order.setContacts(contact);


        //状态
        order.setOrderStatus(0);

        StytledDialog.showMdAlert(this, "系统提示", "提交订单将消费" + goods.getGoodsPrice() +
                        "金币，是否确认？",
                "确认", "取消", null, false, true, new MyDialogListener() {

                    @Override
                    public void onFirst(DialogInterface dialogInterface) {
                        submitOrder(order);
                    }

                    @Override
                    public void onSecond(DialogInterface dialogInterface) {

                    }
                }).show();
    }

    //提交订单
    private void submitOrder(final Order order) {
        showProgress("提交订单中...");
        APIInteractive.getServerTime(new INetworkResponse() {
            @Override
            public void onFailure(int code) {
                hideProgress();
                ToastUtils.showShortToast("订单提交失败");
            }

            @Override
            public void onSucceed(JSONObject result) {
                try {
                    String time = result.optString("datetime");
                    order.setOrderTime(new BmobDate("Date", time));
                    submit();
                } catch (Exception e) {
                    e.printStackTrace();
                    ToastUtils.showShortToast("订单提交失败");
                }
            }

            //提交订单
            private void submit() {
                APIInteractive.submitOrder(order, new INetworkResponse() {
                    @Override
                    public void onFailure(int code) {
                        hideProgress();
                        ToastUtils.showShortToast("订单提交失败");
                    }

                    @Override
                    public void onSucceed(JSONObject result) {
                        hideProgress();
                        ToastUtils.showShortToast("订单提交成功");
                    }
                });
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
