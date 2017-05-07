package com.jiangtao.shuzicai.model.setting;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.blankj.utilcode.utils.ToastUtils;
import com.hss01248.lib.MyDialogListener;
import com.hss01248.lib.StytledDialog;
import com.jiangtao.shuzicai.AppConfigure;
import com.jiangtao.shuzicai.Application;
import com.jiangtao.shuzicai.R;
import com.jiangtao.shuzicai.basic.base.BaseActivityWithToolBar;
import com.jiangtao.shuzicai.common.event_message.LogoutMsg;
import com.jiangtao.shuzicai.common.event_message.WealthChangeMsg;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;

public class SettingActivity extends BaseActivityWithToolBar {

    //退出登录
    @BindView(R.id.logoutBtn)
    Button logoutBtn;
    //修改密码
    @BindView(R.id.modifyPasswordTxt)
    TextView modifyPasswordTxt;
    //找回密码
    @BindView(R.id.findPasswordTxt)
    TextView findPasswordTxt;
    //帮助
    @BindView(R.id.helpTxt)
    TextView helpTxt;
    //反馈
    @BindView(R.id.feedbackTxt)
    TextView feedbackTxt;
    //关于
    @BindView(R.id.aboutTxt)
    TextView aboutTxt;


    //退出登录
    @OnClick({R.id.logoutBtn, R.id.feedbackTxt, R.id.helpTxt,
            R.id.aboutTxt, R.id.findPasswordTxt, R.id.modifyPasswordTxt})
    public void OnClick(View view) {
        switch (view.getId()) {

            case R.id.findPasswordTxt: {
                Intent intent = new Intent(SettingActivity.this, FindPasswordActivity.class);
                startActivity(intent);
            }
            break;

            case R.id.modifyPasswordTxt: {
                Intent intent = new Intent(SettingActivity.this, ModifyPasswordActivity.class);
                startActivity(intent);
            }
            break;

            case R.id.logoutBtn:
                startLogout();
                break;

            case R.id.feedbackTxt: {
                Intent intent = new Intent(SettingActivity.this, FeedbackActivity.class);
                startActivity(intent);
            }
            break;

            case R.id.helpTxt: {
                Intent intent = new Intent(SettingActivity.this, HelpActivity.class);
                startActivity(intent);
            }
            break;

            case R.id.aboutTxt: {
                Intent intent = new Intent(SettingActivity.this, AboutActivity.class);
                startActivity(intent);
            }
            break;
        }
    }

    //退出登录
    private void startLogout() {
        Dialog gloablDialog = StytledDialog.showMdAlert(this, "系统提示", "确认退出？",
                "退出", "取消", null, false, true, new MyDialogListener() {

                    @Override
                    public void onFirst(DialogInterface dialogInterface) {
                        //清除缓存用户对象
                        BmobUser.logOut();
                        if (Application.userInstance != null) {
                            Application.userInstance = null;
                            AppConfigure.saveLoginStatue(false);
                            AppConfigure.saveUserName("");
                            AppConfigure.saveUserPassword("");
                        }
                        ToastUtils.showShortToast("退出成功");
                        EventBus.getDefault().post(new LogoutMsg());
                        EventBus.getDefault().post(new WealthChangeMsg());
                        finish();
                    }

                    @Override
                    public void onSecond(DialogInterface dialogInterface) {

                    }
                });
        gloablDialog.show();
    }


    //设置view的状态
    private void setViewStatus() {
        if (Application.userInstance == null) {
            logoutBtn.setVisibility(View.GONE);
            modifyPasswordTxt.setVisibility(View.GONE);
            findPasswordTxt.setVisibility(View.GONE);
        } else {
            logoutBtn.setVisibility(View.VISIBLE);
            modifyPasswordTxt.setVisibility(View.VISIBLE);
            findPasswordTxt.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int setLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void onInitialize() {
        initTitleBar();
        setViewStatus();
    }

    @Override
    public void initPresenter() {

    }

    /**
     * 初始化标题栏
     */
    private void initTitleBar() {
        //返回
        setLeftImage(R.mipmap.ic_arrow_back_white_24dp, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        setCenterTitle("设置");
    }

    /**
     * 主线程处理接收到的数据
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEventMainThread(String event) {
        Log.e("event MainThread", "消息： " + event + "  thread: " + Thread.currentThread().getName());
    }
}
