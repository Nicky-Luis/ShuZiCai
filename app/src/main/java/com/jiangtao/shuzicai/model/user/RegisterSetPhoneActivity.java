package com.jiangtao.shuzicai.model.user;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.blankj.utilcode.utils.ToastUtils;
import com.jiangtao.shuzicai.R;
import com.jiangtao.shuzicai.basic.base.BaseActivityWithToolBar;
import com.jiangtao.shuzicai.basic.utils.EditTextUtils;
import com.jiangtao.shuzicai.basic.utils.PhoneUtils;
import com.jiangtao.shuzicai.common.tools.CountDownTimer;
import com.jiangtao.shuzicai.model.user.interfaces.IRegisterSetPhoneView;
import com.jiangtao.shuzicai.model.user.interfaces.IRegisterSetPresenter;
import com.jiangtao.shuzicai.model.user.presenter.RegisterSetPhonePresenter;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

public class RegisterSetPhoneActivity extends BaseActivityWithToolBar implements IRegisterSetPhoneView {

    //获取验证码
    @BindView(R.id.VerifyCodeBtn)
    Button VerifyCodeBtn;
    //手机
    @BindView(R.id.phoneNumberEdt)
    EditText phoneNumberEdt;
    //验证码
    @BindView(R.id.verifyCodeEdt)
    EditText verifyCodeEdt;
    //密码
    @BindView(R.id.passwordEdt)
    EditText passwordEdt;
    //邀请码
    @BindView(R.id.invitationCodeEdt)
    EditText invitationCodeEdt;

    //手机号
    private String mPhoneNumber;
    //验证码
    private String mVerifyCode;
    //密码
    private String mPassword;
    //邀请码
    private String mInviteeCode;

    //设置点击事件
    @OnClick({R.id.VerifyCodeBtn, R.id.registerBtn})
    public void OnClick(View view) {
        switch (view.getId()) {

            //获取验证码
            case R.id.VerifyCodeBtn:
                getVerifyCode();
                break;

            //注册按钮
            case R.id.registerBtn:
                startRegister();
                break;
        }
    }

    //注册
    private IRegisterSetPresenter registerPresenter;

    @Override
    public int setLayoutId() {
        return R.layout.activity_register_set_phone;
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
        //标头
        setCenterTitle("注册");
    }

    @Override
    public void initPresenter() {
        this.registerPresenter = new RegisterSetPhonePresenter(this, this);
    }

    /**
     * 获取验证码
     */
    private void getVerifyCode() {
        if (EditTextUtils.isEmpty(phoneNumberEdt)) {
            ToastUtils.showLongToast("手机号码不能为空");
            return;
        }

        mPhoneNumber = EditTextUtils.getContent(phoneNumberEdt);
        PhoneUtils phoneUtils = new PhoneUtils(mPhoneNumber);
        if (!phoneUtils.isLawful()) {
            ToastUtils.showLongToast("手机号码无效");
        } else {
            registerPresenter.getVerifyCode(mPhoneNumber);
            //开始倒计时
            new CountDownTimer(1000 * 60, 1000) {

                @Override
                public void onTick(long millisUntilFinished) {
                    if (VerifyCodeBtn.isEnabled()) {
                        VerifyCodeBtn.setEnabled(false);
                        VerifyCodeBtn.setBackgroundColor(getResources().getColor(R.color.gray));
                    }
                    String still = millisUntilFinished / 1000 + "秒";
                    VerifyCodeBtn.setText(still);
                }

                @Override
                public void onFinish() {
                    VerifyCodeBtn.setEnabled(true);
                    VerifyCodeBtn.setBackgroundColor(getResources().getColor(R.color.main_orange));
                    VerifyCodeBtn.setText("发送验证码");
                }
            }.start();
        }
    }

    /**
     * 开始注册
     */
    private void startRegister() {
        if (EditTextUtils.isEmpty(phoneNumberEdt)) {
            ToastUtils.showLongToast("手机号码不能为空");
            return;
        } else if (!EditTextUtils.isLengthMatch(phoneNumberEdt, 11)) {
            ToastUtils.showLongToast("手机号码产长度不正确");
            return;
        } else if (EditTextUtils.isEmpty(verifyCodeEdt)) {
            ToastUtils.showLongToast("验证码不能为空");
            return;
        } else if (!EditTextUtils.isLengthMatch(verifyCodeEdt, 6)) {
            ToastUtils.showLongToast("验证码长度不正确");
            return;
        } else if (EditTextUtils.isEmpty(passwordEdt)) {
            ToastUtils.showLongToast("密码不能为空");
            return;
        } else if (EditTextUtils.isEmpty(invitationCodeEdt)) {
            ToastUtils.showLongToast("邀请码不能为空");
            return;
        }

        //获取信息
        mVerifyCode = EditTextUtils.getContent(verifyCodeEdt);
        mPhoneNumber = EditTextUtils.getContent(phoneNumberEdt);
        mPassword = EditTextUtils.getContent(passwordEdt);
        mInviteeCode = EditTextUtils.getContent(invitationCodeEdt);

        showProgress("注册中...");
        registerPresenter.startRegister(mPhoneNumber, mVerifyCode, mPassword, mInviteeCode);
    }

    @Override
    public void onGetVeryCode(String ssid) {
        //获取验证码状态
        registerPresenter.getSmsStatue(ssid);
    }

    @Override
    public void onRegisterSucceed() {
        hideProgress();
        ToastUtils.showLongToast("注册成功，请完善个人信息");
        //跳转到设置密码页面
        Intent intent = new Intent(RegisterSetPhoneActivity.this, UserInfoActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onRegisterFailed() {
        hideProgress();
        ToastUtils.showLongToast("注册失败，请重新注册");
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
