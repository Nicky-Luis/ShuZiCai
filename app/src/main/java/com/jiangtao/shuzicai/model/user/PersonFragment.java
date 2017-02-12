package com.jiangtao.shuzicai.model.user;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.LogUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jiangtao.shuzicai.Application;
import com.jiangtao.shuzicai.R;
import com.jiangtao.shuzicai.basic.base.BaseFragment;
import com.jiangtao.shuzicai.basic.network.APIInteractive;
import com.jiangtao.shuzicai.basic.network.BmobQueryUtils;
import com.jiangtao.shuzicai.basic.network.INetworkResponse;
import com.jiangtao.shuzicai.common.event_message.EditUserInfoMsg;
import com.jiangtao.shuzicai.common.event_message.LoginMsg;
import com.jiangtao.shuzicai.common.event_message.LogoutMsg;
import com.jiangtao.shuzicai.common.event_message.WealthChangeMsg;
import com.jiangtao.shuzicai.model.user.entry.WealthValue;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/***
 * 个人中心fragment
 */
public class PersonFragment extends BaseFragment {
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
    @BindView(R.id.wealthDetailsTxt)
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


    //设置点击事件
    @OnClick({R.id.loginViewLayout, R.id.userInfoLayout,
            R.id.wealthDetailsTxt, R.id.gameRcordTxt, R.id.myInviteTxt})
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

            case R.id.wealthDetailsTxt: {
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
        }
    }


    //对象实例化
    public static PersonFragment newInstance(int page) {
        Bundle args = new Bundle();

        args.putInt(ARGS_PAGE, page);
        PersonFragment fragment = new PersonFragment();
        fragment.setArguments(args);
        return fragment;
    }

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
        synchronizeDate();
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
        gameRcordTxt.setVisibility(loginVisible);
        myInviteTxt.setVisibility(loginVisible);
    }

    /**
     * 更新用户信息数据
     */
    private void updateUserInfoDate() {
        //绑定信息
        if (null != Application.userInstance) {
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

            //设置用户名、
            String name = Application.userInstance.getNickName();
            userNameTxt.setText(name);
            //设置地址
            String address = Application.userInstance.getAddress();
            userLocation.setText(address.split("-")[1]);
            //设置性别
            int gender = Application.userInstance.getGender();
            if (gender == 0) {
                userSexImg.setImageResource(R.mipmap.man);
            } else {
                userSexImg.setImageResource(R.mipmap.woman);
            }
        }
    }

    //更新用户财富数据
    private void upDateWealthValue(WealthValue wealthValue) {

        goldCountTxt.setText(String.valueOf((int) wealthValue.getGoldValue()));
        silverCountTxt.setText(String.valueOf((int) wealthValue.getSilverValue()));
    }

    //更新信息
    private void synchronizeDate() {
        if (null == Application.userInstance) {
            return;
        }

        String userId = Application.userInstance.getObjectId();
        //获取当前用户的信息
        APIInteractive.getCurrentUser(userId, new INetworkResponse() {
            @Override
            public void onFailure(int code) {
                LogUtils.e("更新数据失败");
            }

            @Override
            public void onSucceed(JSONObject result) {
                //更新数据
                Application.userInstance.setModelData(result);
                updateUserInfoDate();
                //获取用户的财富
                getWealthValue();
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

    /**
     * 用户登录状态处理
     *
     * @param msg
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEventMainThread(LoginMsg msg) {
        resetView();
        // 同步数据
        synchronizeDate();
    }


    /**
     * 用户编辑信息处理
     *
     * @param msg
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEventMainThread(EditUserInfoMsg msg) {
        resetView();
        // 同步数据
        synchronizeDate();
    }


    /**
     * 退出登录
     *
     * @param msg
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEventMainThread(LogoutMsg msg) {
        resetView();
    }

    /**
     * 财富发生了变化
     *
     * @param msg
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEventMainThread(WealthChangeMsg msg) {
        upDateWealthValue(msg.getWealthValue());
    }
}