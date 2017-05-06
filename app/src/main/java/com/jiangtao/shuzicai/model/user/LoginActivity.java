package com.jiangtao.shuzicai.model.user;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.blankj.utilcode.utils.ToastUtils;
import com.jiangtao.shuzicai.AppConfigure;
import com.jiangtao.shuzicai.AppHandlerService;
import com.jiangtao.shuzicai.R;
import com.jiangtao.shuzicai.basic.base.BaseActivityWithToolBar;
import com.jiangtao.shuzicai.basic.utils.EditTextUtils;
import com.jiangtao.shuzicai.common.event_message.LoginMsg;
import com.jiangtao.shuzicai.common.event_message.WealthChangeMsg;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BaseActivityWithToolBar {

    @BindView(R.id.loginBtn)
    Button loginBtn;
    //账户
    @BindView(R.id.loginPhoneEdt)
    EditText loginPhoneEdt;
    //密码
    @BindView(R.id.loginPasswordEdt)
    EditText loginPasswordEdt;
    //presenter
    //用户名
    private String account;
    //密码
    private String password;

    //设置点击事件
    @OnClick({R.id.loginToRegisterTxt, R.id.loginBtn})
    public void OnClick(View view) {
        switch (view.getId()) {

            case R.id.loginToRegisterTxt:
                Intent intent = new Intent(LoginActivity.this, RegisterSetPhoneActivity.class);
                startActivity(intent);
                break;

            case R.id.loginBtn:
                startLogin();
                break;

        }
    }

    @Override
    public int setLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void onInitialize() {
        //返回
        setLeftImage(R.mipmap.ic_arrow_back_white_24dp, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        setCenterTitle("登录");
    }

    @Override
    public void initPresenter() {
    }

    /**
     * 开始登录
     */
    private void startLogin() {
        account = EditTextUtils.getContent(loginPhoneEdt);
        password = EditTextUtils.getContent(loginPasswordEdt);
        if (EditTextUtils.isEmpty(loginPhoneEdt)) {
            ToastUtils.showShortToast("账户不能为空");
        } else if (EditTextUtils.isEmpty(loginPasswordEdt)) {
            ToastUtils.showShortToast("密码不能为空不能为空");
        }
        showProgress("登录中...");
        AppHandlerService.startLogin(account,password);
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

    /**
     * 用户登录状态处理
     *
     * @param result
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEventMainThread(LoginMsg result) {
        hideProgress();
        if (result.isSucceed()){
            ToastUtils.showShortToast("登录成功");
            //保存登录状态
            AppConfigure.saveLoginStatue(true);
            AppConfigure.saveUserName(account);
            AppConfigure.saveUserPassword(password);
            finish();
        }
        EventBus.getDefault().post(new WealthChangeMsg());
    }

}
