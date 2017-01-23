package com.jiangtao.shuzicai.model.user;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.LogUtils;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jiangtao.shuzicai.Application;
import com.jiangtao.shuzicai.R;
import com.jiangtao.shuzicai.basic.base.BaseFragment;
import com.jiangtao.shuzicai.basic.network.APIInteractive;
import com.jiangtao.shuzicai.basic.network.INetworkResponse;
import com.jiangtao.shuzicai.common.message.EditUserInfoMsg;
import com.jiangtao.shuzicai.common.message.LoginMsg;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

import static com.jiangtao.shuzicai.R.id.userSexTxt;

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
    @BindView(userSexTxt)
    ImageView userSexImg;


    //设置点击事件
    @OnClick({R.id.loginViewLayout, R.id.userInfoLayout})
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
        if (null == loginLayout || null == userInfoLayout) {
            return;
        }
        if (Application.userInstance == null) {
            loginLayout.setVisibility(View.VISIBLE);
            userInfoLayout.setVisibility(View.GONE);
        } else {
            loginLayout.setVisibility(View.GONE);
            userInfoLayout.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 更新数据
     */
    private void updateViewDate() {
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

    //更新信息
    public void synchronizeDate() {
        if (null == Application.userInstance) {
            return;
        }
        APIInteractive.getCurrentUser(Application.userInstance.getObjectId(),
                new INetworkResponse() {
                    @Override
                    public void onFailure(int code) {
                        LogUtils.e("更新数据失败");
                    }

                    @Override
                    public void onSucceed(JSONObject result) {
                        //更新数据
                        Application.userInstance.setModelData(result);
                        updateViewDate();
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

}