package com.jiangtao.shuzicai.model.user;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
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
import com.jiangtao.shuzicai.basic.utils.EditTextUtils;
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
import c.b.BP;
import c.b.PListener;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

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
        APIInteractive.authRecharge("0.01", "金币充值", "数字连连猜金币充值",
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
                        LogUtils.i("充值额为：" + msg.arg1);
                        onPaySucceed(100);
                        startCalculateInvite(100);
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
        LayoutInflater inflater = (LayoutInflater)
                getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.dialog_exchange_layout, null);
        final EditText convertEdt = (EditText) layout.findViewById(R.id.convertEdt);
        convertEdt.setHint("输入充值金币数");

        new AlertDialog.Builder(getActivity()).setTitle("1元可兑换1个金币").setView(
                layout).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (EditTextUtils.isEmpty(convertEdt)) {
                    ToastUtils.showShortToast("金币数不能空");
                } else {
                    int value = Integer.valueOf(EditTextUtils.getContent(convertEdt));
                    if (value <= 0) {
                        ToastUtils.showShortToast("请输入正确的金币值");
                    } else {
                        startAlibabaPay(value);
                    }
                }
            }
        }).setNegativeButton("取消", null).show();
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

    /***
     * 选择支付方式
     */
    private void selectPayType(final int value) {
        final List<String> datas = new ArrayList<>();
        datas.add("支付宝支付");
        datas.add("微信支付");
        final CustomListViewDialog dialog = new CustomListViewDialog(getActivity(), datas);
        dialog.setClickCallBack(new CustomListViewDialog.IClickCallBack() {
            @Override
            public void Onclick(View view, int which) {
                startPay(0 == which, value);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    /**
     * 检查某包名应用是否已经安装
     *
     * @param packageName 包名
     * @param browserUrl  如果没有应用市场，去官网下载
     * @return
     */
    private boolean checkPackageInstalled(String packageName, String browserUrl) {
        try {
            // 检查是否有支付宝客户端
            getActivity().getPackageManager().getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            // 没有安装支付宝，跳转到应用市场
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("market://details?id=" + packageName));
                startActivity(intent);
            } catch (Exception ee) {
                // 连应用市场都没有，用浏览器去支付宝官网下载
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(browserUrl));
                    startActivity(intent);
                } catch (Exception eee) {
                    ToastUtils.showShortToast("请先安装支付宝/微信吧");
                }
            }
        }
        return false;
    }


    private void installApk(String s) {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            //申请权限
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUESTPERMISSION);
        } else {
            installBmobPayPlugin(s);
        }
    }

    /**
     * 比目支付调用支付
     *
     * @param alipayOrWechatPay 支付类型，true为支付宝支付,false为微信支付
     */
    void startPay(final boolean alipayOrWechatPay, final int value) {
        if (alipayOrWechatPay) {
            if (!checkPackageInstalled("com.eg.android.AlipayGphone", "https://www.alipay.com")) {
                // 支付宝支付要求用户已经安装支付宝客户端
                ToastUtils.showShortToast("请安装支付宝客户端");
                return;
            }
        } else {
            if (checkPackageInstalled("com.tencent.mm", "http://weixin.qq.com")) {
                // 需要用微信支付时，要安装微信客户端，然后需要插件
                // 有微信客户端，看看有无微信支付插件
                int pluginVersion = BP.getPluginVersion(getActivity());
                if (pluginVersion < PLUGINVERSION) {
                    // 为0说明未安装支付插件,
                    // 否则就是支付插件的版本低于官方最新版
                    ToastUtils.showLongToast(
                            pluginVersion == 0 ? "监测到本机尚未安装支付插件,无法进行支付,请先安装插件(无流量消耗)"
                                    : "监测到本机的支付插件不是最新版,最好进行更新,请先更新插件(无流量消耗)"
                    );
                    installApk("bp.db");
                    return;
                }
            } else {
                // 没有安装微信
                ToastUtils.showShortToast("请安装微信客户端");
                return;
            }
        }
        showDialog("正在获取订单...\nSDK版本号:" + BP.getPaySdkVersion());
        try {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            ComponentName cn = new ComponentName("com.bmob.app.sport",
                    "com.bmob.app.sport.wxapi.BmobActivity");
            intent.setComponent(cn);
            this.startActivity(intent);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        BP.pay("金币充值", "金币充值", value, alipayOrWechatPay, new PListener() {

            // 因为网络等原因,支付结果未知(小概率事件),出于保险起见稍后手动查询
            @Override
            public void unknow() {
                ToastUtils.showShortToast("支付结果未知,请稍后手动查询");
                hideDialog();
            }

            // 支付成功,如果金额较大请手动查询确认
            @Override
            public void succeed() {
                onPaySucceed(value);
            }

            // 无论成功与否,返回订单号
            @Override
            public void orderId(String orderId) {
                // 此处应该保存订单号,比如保存进数据库等,以便以后查询
                LogUtils.i("订单号：" + orderId);
                showDialog("获取订单成功!请等待跳转到支付页面~");
            }

            // 支付失败,原因可能是用户中断支付操作,也可能是网络原因
            @Override
            public void fail(int code, String reason) {
                // 当code为-2,意味着用户中断了操作
                // code为-3意味着没有安装BmobPlugin插件
                if (code == -3) {
                    ToastUtils.showShortToast("监测到你尚未安装支付插件,无法进行支付," +
                            "请先安装插件(已打包在本地,无流量消耗),安装结束后重新支付");
                    installApk("bp.db");
                } else {
                    ToastUtils.showShortToast("支付中断!");
                }
                hideDialog();
            }
        });
    }

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

    void showDialog(String message) {
        try {
            if (dialog == null) {
                dialog = new ProgressDialog(getActivity());
                dialog.setCancelable(true);
            }
            dialog.setMessage(message);
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
            // 在其他线程调用dialog会报错
        }
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