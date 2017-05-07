package com.jiangtao.shuzicai.model.setting;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.blankj.utilcode.utils.LogUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.jiangtao.shuzicai.R;
import com.jiangtao.shuzicai.basic.base.BaseActivityWithToolBar;
import com.jiangtao.shuzicai.basic.utils.EditTextUtils;
import com.jiangtao.shuzicai.basic.utils.PhoneUtils;
import com.jiangtao.shuzicai.common.tools.CountDownTimer;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobSmsState;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

//重置密码
public class FindPasswordActivity extends BaseActivityWithToolBar {

    //手机号
    @BindView(R.id.phoneNumberEdt)
    EditText phoneNumberEdt;
    //密码
    @BindView(R.id.passwordEdt)
    EditText passwordEdt;
    //验证码
    @BindView(R.id.verifyCodeEdt)
    EditText verifyCodeEdt;
    //获取验证码
    @BindView(R.id.getCodeBtn)
    Button verifyCodeBtn;

    //设置点击事件
    @OnClick({R.id.getCodeBtn, R.id.confirmBtn})
    public void OnClick(View view) {
        switch (view.getId()) {

            case R.id.getCodeBtn:
                startGetVerifyCode();
                break;

            case R.id.confirmBtn:
                startFindPassword();
                break;

        }
    }

    //找回密码
    public void startFindPassword() {
        if (EditTextUtils.isEmpty(verifyCodeEdt)) {
            ToastUtils.showLongToast("验证码不能为空");
            return;
        }
        String mVerifyCode = EditTextUtils.getContent(verifyCodeEdt);

        if (EditTextUtils.isEmpty(passwordEdt)) {
            ToastUtils.showLongToast("新密码不能为空");
            return;
        }
        String mPassword = EditTextUtils.getContent(passwordEdt);
        showProgress("重置中...");
        BmobUser.resetPasswordBySMSCode(mVerifyCode, mPassword, new UpdateListener() {

            @Override
            public void done(BmobException ex) {
                hideProgress();
                if (ex == null) {
                    ToastUtils.showShortToast("密码找回成功");
                    Log.i("smile", "密码重置成功");
                    finish();
                } else {
                    Log.i("smile", "重置失败：code =" + ex.getErrorCode() + ",msg = " + ex.getLocalizedMessage());
                    ToastUtils.showShortToast("密码找回失败");
                }
            }
        });
    }


    @Override
    public int setLayoutId() {
        return R.layout.activity_reset_password;
    }

    @Override
    protected void onInitialize() {
        initTitleBar();
    }

    @Override
    public void initPresenter() {

    }

    //获取验证码
    public void startGetVerifyCode() {
        if (EditTextUtils.isEmpty(phoneNumberEdt)) {
            ToastUtils.showLongToast("手机号码不能为空");
            return;
        }
        String mPhoneNumber = EditTextUtils.getContent(phoneNumberEdt);
        PhoneUtils phoneUtils = new PhoneUtils(mPhoneNumber);
        if (!phoneUtils.isLawful()) {
            ToastUtils.showLongToast("手机号码无效");
            return;
        }

        BmobSMS.requestSMSCode(mPhoneNumber, "shuzicai", new QueryListener<Integer>() {

            @Override
            public void done(Integer smsId, BmobException ex) {
                if (ex == null) {
                    querySmsStatue(smsId);
                    //用于查询本次短信发送详情
                    Log.i("smile", "短信id：" + smsId);
                } else {
                    ToastUtils.showLongToast("验证码发送失败");
                }
            }
        });

        //开始倒计时
        new CountDownTimer(1000 * 60, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                if (verifyCodeBtn.isEnabled()) {
                    verifyCodeBtn.setEnabled(false);
                    verifyCodeBtn.setBackgroundColor(getResources().getColor(R.color.gray));
                }
                String still = millisUntilFinished / 1000 + "秒";
                verifyCodeBtn.setText(still);
            }

            @Override
            public void onFinish() {
                verifyCodeBtn.setEnabled(true);
                verifyCodeBtn.setBackgroundColor(getResources().getColor(R.color.main_orange));
                verifyCodeBtn.setText("发送验证码");
            }
        }.start();
    }

    /**
     * 查询验证码状态
     *
     * @param smsId
     */
    private void querySmsStatue(Integer smsId) {
        BmobSMS.querySmsState(smsId, new QueryListener<BmobSmsState>() {

            @Override
            public void done(BmobSmsState state, BmobException ex) {
                if (ex == null) {
                    //:SUCCESS（发送成功）、FAIL（发送失败）、SENDING(发送中)
                    if (state.getSmsState().equals("SUCCESS")) {
                        //验证码发送成功
                        ToastUtils.showLongToast("验证码发送成功");
                    } else if (state.getSmsState().equals("FAIL")) {
                        ToastUtils.showLongToast("验证码发送失败");
                    } else if (state.getSmsState().equals("SENDING")) {
                        ToastUtils.showLongToast("验证码发送中");
                    } else {
                        LogUtils.e("验证码发送异常");
                    }
                }
            }
        });
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

        setCenterTitle("找回密码");
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
