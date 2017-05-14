package com.jiangtao.shuzicai.model.user;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.blankj.utilcode.utils.LogUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jiangtao.shuzicai.AppHandlerService;
import com.jiangtao.shuzicai.Application;
import com.jiangtao.shuzicai.R;
import com.jiangtao.shuzicai.basic.base.BaseFragment;
import com.jiangtao.shuzicai.basic.network.APIInteractive;
import com.jiangtao.shuzicai.basic.network.INetworkResponse;
import com.jiangtao.shuzicai.basic.widget.CustomListViewDialog;
import com.jiangtao.shuzicai.common.event_message.EditUserInfoMsg;
import com.jiangtao.shuzicai.common.event_message.LoginMsg;
import com.jiangtao.shuzicai.common.event_message.LogoutMsg;
import com.jiangtao.shuzicai.common.event_message.WealthChangeMsg;
import com.jiangtao.shuzicai.model.pay.PayResult;
import com.jiangtao.shuzicai.model.user.entry.WealthDetail;
import com.jiangtao.shuzicai.model.user.entry._User;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/***
 * 个人中心fragment
 */
public class PersonFragment extends BaseFragment {

    // 此为微信支付插件的官方最新版本号,请在更新时留意更新说明
    int PLUGINVERSION = 7;
    //安装插件权限
    private static final int REQUESTPERMISSION = 101;
    //参数
    public static final String ARGS_PAGE = "args_page";
    //页数
    private int mPage;
    //未登录
    @BindView(R.id.loginViewLayout)
    LinearLayout loginLayout;
    //已经登录
    @BindView(R.id.userInfoLayout)
    LinearLayout userInfoLayout;
    //头像
    @BindView(R.id.userHeadImg)
    SimpleDraweeView userHeadImg;
    //用户名
    @BindView(R.id.userNameTxt)
    TextView userNameTxt;
    //地址
    @BindView(R.id.userLocation)
    TextView userLocation;
    //性别
    @BindView(R.id.userSexTxt)
    ImageView userSexImg;
    //金币
    @BindView(R.id.goldLayout)
    LinearLayout goldLayout;
    //银元
    @BindView(R.id.silverLayout)
    LinearLayout silverLayout;
    //金币
    @BindView(R.id.goldCountTxt)
    TextView goldCountTxt;
    //银元
    @BindView(R.id.silverCountTxt)
    TextView silverCountTxt;
    //财富明细
    @BindView(R.id.wealthDetailTxt)
    TextView wealthDetailsTxt;
    //游戏记录
    @BindView(R.id.gameRcordTxt)
    TextView gameRcordTxt;
    //邀请
    @BindView(R.id.myInviteTxt)
    TextView myInviteTxt;
    //充值按钮
    @BindView(R.id.rechargeBtn)
    Button rechargeBtn;
    ProgressDialog dialog;
    //handler
    private static final int SDK_PAY_FLAG = 1;

    //设置点击事件
    @OnClick({R.id.loginViewLayout, R.id.userInfoLayout, R.id.rechargeBtn,
            R.id.wealthDetailTxt, R.id.gameRcordTxt, R.id.myInviteTxt})
    public void OnClick(View view) {
        switch (view.getId()) {

            case R.id.loginViewLayout: {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
            break;

            case R.id.userInfoLayout: {
                Intent intent = new Intent(getActivity(), UserInfoActivity.class);
                startActivity(intent);
            }
            break;

            case R.id.wealthDetailTxt: {
                Intent intent = new Intent(getActivity(), WealthDetailActivity.class);
                startActivity(intent);
            }
            break;

            case R.id.gameRcordTxt: {
                Intent intent = new Intent(getActivity(), GameRecordsActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.myInviteTxt: {
                Intent intent = new Intent(getActivity(), InviteDetailActivity.class);
                startActivity(intent);
            }
            break;

            case R.id.rechargeBtn: {
                LogUtils.i("开始充值");
                rechargeInput();
            }
            break;
        }
    }

    /**
     * 开始支付宝充值
     *
     * @param value
     */
    public void startAlibabaPay(final int value) {
        APIInteractive.authRecharge(String.valueOf(value), "shu zi cai", "数字连连猜金币充值",
                new INetworkResponse() {
                    @Override
                    public void onFailure(int code) {
                        ToastUtils.showLongToast("支付失败" + code);
                    }

                    @Override
                    public void onSucceed(JSONObject result) {
                        final String order = result.optString("order");
                        LogUtils.i("order:" + order);
                        new Thread(new Runnable() {

                            @Override
                            public void run() {
                                PayTask alipay = new PayTask(getActivity());
                                Map<String, String> result = alipay.payV2(order, true);
                                Log.i("msp", result.toString());
                                Message msg = new Message();
                                msg.what = SDK_PAY_FLAG;
                                msg.obj = result;
                                msg.arg1 = value;
                                mHandler.sendMessage(msg);
                            }
                        }).start();
                    }
                });
    }


    //对象实例化
    public static PersonFragment newInstance(int page) {
        Bundle args = new Bundle();

        args.putInt(ARGS_PAGE, page);
        PersonFragment fragment = new PersonFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    // 同步返回需要验证的信息
                    String resultInfo = payResult.getResult();
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        int value = msg.arg1 * 10;//一元等于10金币
                        LogUtils.i("充值额为：" + msg.arg1);
                        onPaySucceed(value);
                        startCalculateInvite(value);
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        ToastUtils.showLongToast("支付失败");
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };


    @Override
    public void onGetArgument() {
        mPage = getArguments().getInt(ARGS_PAGE);
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public int setLayoutId() {
        return R.layout.fragment_person;
    }

    @Override
    public void loadLayout(View rootView) {
        //初始化时
        resetView();
        // 同步数据
        updateUserInfoDate();
        //绑定财富信息
        upDateWealthValue();
    }

    /**
     * 设置页面状态
     */
    private void resetView() {
        if (null == loginLayout || null == userInfoLayout || null == goldLayout ||
                null == silverLayout || null == wealthDetailsTxt ||
                null == gameRcordTxt || null == myInviteTxt) {
            return;
        }
        int loginVisible;
        if (Application.userInstance == null) {
            loginLayout.setVisibility(View.VISIBLE);
            loginVisible = View.GONE;
        } else {
            loginLayout.setVisibility(View.GONE);
            loginVisible = View.VISIBLE;
        }

        //设置可见状态
        userInfoLayout.setVisibility(loginVisible);
        goldLayout.setVisibility(loginVisible);
        silverLayout.setVisibility(loginVisible);
        wealthDetailsTxt.setVisibility(loginVisible);
        gameRcordTxt.setVisibility(View.GONE);
        myInviteTxt.setVisibility(loginVisible);
    }

    /**
     * 更新用户信息数据
     */
    private void updateUserInfoDate() {
        //绑定信息
        if (null == Application.userInstance) {
            return;
        }
        //设置头像
        String imageUrl = Application.userInstance.getHeadImageUrl();
        if (null != imageUrl && !imageUrl.equals("") && URLUtil.isNetworkUrl(imageUrl)) {
            Uri uri = Uri.parse(imageUrl);
            userHeadImg.setImageURI(uri);
        } else {
            Uri uri = Uri.parse("res:///" + R.mipmap.add_head);
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setUri(uri)
                    .setTapToRetryEnabled(true)
                    .build();
            userHeadImg.setController(controller);
        }

        //设置用户名
        String name = Application.userInstance.getNickName();
        userNameTxt.setText(name);
        //设置地址
        String address = Application.userInstance.getAddress();
        if (address != null && address.length() > 3) {
            userLocation.setText(address.split("-")[1]);
        }
        //设置性别
        int gender = Application.userInstance.getGender();
        if (gender == 0) {
            userSexImg.setImageResource(R.mipmap.man);
        } else {
            userSexImg.setImageResource(R.mipmap.woman);
        }
    }

    //更新用户财富数据
    private void upDateWealthValue() {
        if (null == Application.userInstance) {
            return;
        }
        goldCountTxt.setText(String.valueOf(Application.userInstance.getGoldValue()));
        silverCountTxt.setText(String.valueOf(Application.userInstance.getSilverValue()));
    }

    /**
     * 用户登录状态处理
     *
     * @param msg
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEventMainThread(LoginMsg msg) {
        resetView();
        updateUserInfoDate();
        upDateWealthValue();
    }


    /**
     * 用户编辑信息处理
     *
     * @param msg
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEventMainThread(EditUserInfoMsg msg) {
        resetView();
        updateUserInfoDate();
        upDateWealthValue();
    }

    /**
     * 退出登录
     *
     * @param msg
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEventMainThread(LogoutMsg msg) {
        resetView();
        updateUserInfoDate();
    }

    /**
     * 财富发生了变化
     *
     * @param msg
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEventMainThread(WealthChangeMsg msg) {
        upDateWealthValue();
        updateUserInfoDate();
    }

    //充值的金额
    private void rechargeInput() {

        final List<String> datas = new ArrayList<>();
        datas.add("100金币");
        datas.add("200金币");
        datas.add("300金币");
        datas.add("500金币");
        datas.add("800金币");
        datas.add("1000金币");
        final CustomListViewDialog dialog = new CustomListViewDialog(getActivity(),
                datas, "选择充值数额(1元=10金币)");
        dialog.setClickCallBack(new CustomListViewDialog.IClickCallBack() {
            @Override
            public void Onclick(View view, int which) {
                int value = 10;
                if (1 == which) {
                    value = 20;
                } else if (2 == which) {
                    value = 30;
                } else if (3 == which) {
                    value = 50;
                } else if (4 == which) {
                    value = 80;
                } else if (5 == which) {
                    value = 100;
                }
                startAlibabaPay(value);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    /**
     * 开始计算邀请者奖励
     *
     * @param value
     */
    private void startCalculateInvite(final int value) {
        BmobQuery<_User> query = new BmobQuery<_User>();
        query.addWhereEqualTo("InvitationCode", Application.userInstance.getInviteeCode());
        query.findObjects(new FindListener<_User>() {
            @Override
            public void done(List<_User> list, BmobException e) {
                if (e == null) {
                    if (list != null && list.size() > 0) {
                        setFirstInvite(list.get(0), value);
                    } else {
                        LogUtils.e("没有找到初级邀请者");
                    }
                } else {
                    LogUtils.e("找寻初级邀请者异常" + e);
                }
            }
        });
    }


    /**
     * 获取第一级财富值
     *
     * @param firstUser
     * @param value
     */
    private void setFirstInvite(final _User firstUser, final int value) {
        if (firstUser == null) {
            return;
        }
        int first = firstUser.getGoldValue();
        int operateValue = (int) (value * 0.08f);
        int after = first + operateValue;
        if (operateValue <= 0) {
            return;
        }
        setSecondInvitee(firstUser, value);
        WealthDetail wealthDetail = new WealthDetail(
                firstUser.getObjectId(),
                first, after,
                WealthDetail.Currency_Type_Gold,
                WealthDetail.Operation_Type_Invite_First,
                operateValue, 0);
        saveWealthRecord(wealthDetail);
    }

    /**
     * 更新次级邀请者
     *
     * @param firstUser
     * @param value
     */
    private void setSecondInvitee(_User firstUser, final int value) {
        BmobQuery<_User> query = new BmobQuery<_User>();
        query.addWhereEqualTo("InvitationCode", firstUser.getInviteeCode());
        query.findObjects(new FindListener<_User>() {
            @Override
            public void done(List<_User> list, BmobException e) {
                if (e == null) {
                    if (list != null && list.size() > 0) {
                        final _User secondUser = list.get(0);
                        int first = secondUser.getGoldValue();
                        int operateValue = (int) (value * 0.04f);
                        int after = first + operateValue;
                        if (operateValue <= 0) {
                            return;
                        }
                        setThirdInvitee(secondUser, value);
                        WealthDetail wealthDetail = new WealthDetail(
                                secondUser.getObjectId(),
                                first, after,
                                WealthDetail.Currency_Type_Gold,
                                WealthDetail.Operation_Type_Invite_Second,
                                operateValue, 0);
                        saveWealthRecord(wealthDetail);
                    } else {
                        LogUtils.i("没有找到次级邀请者");
                    }
                } else {
                    LogUtils.e("寻找次级邀请者异常" + e);
                }
            }
        });
    }

    /**
     * 更新三级邀请者
     *
     * @param secondUser
     * @param value
     */
    private void setThirdInvitee(_User secondUser, final int value) {
        BmobQuery<_User> query = new BmobQuery<_User>();
        query.addWhereEqualTo("InvitationCode", secondUser.getInviteeCode());
        query.findObjects(new FindListener<_User>() {
            @Override
            public void done(List<_User> list, BmobException e) {
                if (e == null) {
                    if (list != null && list.size() > 0) {
                        _User thirdUser = list.get(0);
                        int first = thirdUser.getGoldValue();
                        int operateValue = (int) (value * 0.02f);
                        int after = first + operateValue;
                        if (operateValue <= 0) {
                            return;
                        }
                        WealthDetail wealthDetail = new WealthDetail(
                                thirdUser.getObjectId(),
                                first, after,
                                WealthDetail.Currency_Type_Gold,
                                WealthDetail.Operation_Type_Invite_Third,
                                operateValue, 0);
                        saveWealthRecord(wealthDetail);
                    } else {
                        LogUtils.i("没有找到三级邀请者");
                    }
                } else {
                    LogUtils.e("寻找三级邀请者异常" + e);
                }
            }
        });
    }
    //////////////////////////////////比目支付//////////////////////////////////////

    /**
     * 支付成功后的操作
     *
     * @param value
     */
    public void onPaySucceed(int value) {
        final int beforeValue = Application.userInstance.getGoldValue();
        final int afterValue = beforeValue + value;
        //记录金币数
        Application.userInstance.setGoldValue(afterValue);
        AppHandlerService.updateWealth();

        //记录充值的数据
        WealthDetail wealthDetail = new WealthDetail(
                Application.userInstance.getObjectId(),
                beforeValue,
                afterValue,
                WealthDetail.Currency_Type_Gold,
                WealthDetail.Operation_Type_Recharge,
                value, 1);
        saveWealthRecord(wealthDetail);

        ToastUtils.showShortToast("支付成功!");
        hideDialog();
    }

    /***
     * 记录金币的操作状态
     *
     * @param wealthDetail
     */
    public void saveWealthRecord(WealthDetail wealthDetail) {
        //记录金币的操作状态
        wealthDetail.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {

                } else {
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }


    void hideDialog() {
        if (dialog != null && dialog.isShowing())
            try {
                dialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    /**
     * 安装assets里的apk文件
     *
     * @param fileName
     */
    void installBmobPayPlugin(String fileName) {
        try {
            InputStream is = getContext().getAssets().open(fileName);
            File file = new File(Environment.getExternalStorageDirectory()
                    + File.separator + fileName + ".apk");
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            byte[] temp = new byte[1024];
            int i = 0;
            while ((i = is.read(temp)) > 0) {
                fos.write(temp, 0, i);
            }
            fos.close();
            is.close();

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.parse("file://" + file),
                    "application/vnd.android.package-archive");
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUESTPERMISSION) {
            if (permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    installBmobPayPlugin("bp.db");
                } else {
                    //提示没有权限，安装不了
                    ToastUtils.showShortToast("您拒绝了权限，这样无法安装支付插件");
                }
            }
        }
    }
}