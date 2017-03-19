package com.jiangtao.shuzicai.model.user;

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
import com.jiangtao.shuzicai.model.user.entry._User;
import com.jiangtao.shuzicai.model.user.utils.ShareCodeUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.datatype.BmobSmsState;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;

public class RegisterSetPhoneActivity extends BaseActivityWithToolBar {

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

            // registerPresenter.getVerifyCode(mPhoneNumber);
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
                    } else {
                        ToastUtils.showLongToast("验证码已经发送");
                    }
                }
            }
        });
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

        BmobQuery<_User> query = new BmobQuery<_User>();
        query.addWhereEqualTo("mobilePhoneNumber", mPhoneNumber);
        query.count(_User.class, new CountListener() {
            @Override
            public void done(Integer integer, BmobException e) {
                if (integer > 0) {
                    ToastUtils.showShortToast("用户已存在请直接登录");
                } else {
                    isInvitationExist();
                }
            }
        });
    }

    /**
     * 判断邀请码是否存在
     */
    private void isInvitationExist() {
        BmobQuery<_User> query = new BmobQuery<_User>();
        query.addWhereEqualTo("InviteeCode", mInviteeCode);
        query.count(_User.class, new CountListener() {
            @Override
            public void done(Integer integer, BmobException e) {
                if (integer > 0) {
                    register();
                } else {
                    ToastUtils.showShortToast("邀请码不存在");
                }
            }
        });
    }

    //注册
    private void register() {
        showProgress("注册中...");
        final _User user = new _User();
        user.setMobilePhoneNumber(mPhoneNumber);
        user.setUsername(mPhoneNumber);
        user.setPassword(mPassword);
        user.setInviteeCode(mInviteeCode);//邀请码
        user.setGender(0);//性别
        user.setAddress("广东-深圳-福田");
        user.setGoldValue(0);
        user.setSilverValue(1000);
        //默认头像
        user.setHeadImageUrl("http://bmob-cdn-8867.b0.upaiyun.com/2017/03/19/b57b5f7982114ed8a4867f4d7094fbd4.jpg");

        //生成邀请码
        ShareCodeUtil.generateRandomStr(6, new ShareCodeUtil.IShareCallBack() {

            @Override
            public void shareCodeExist() {
                //注册失败
                ToastUtils.showLongToast("注册失败,请重新注册");
            }

            @Override
            public void getShareCode(String code) {
                user.setInvitationCode(code);
                user.setNickName("用户" + code);
                user.signOrLogin(mVerifyCode, new SaveListener<_User>() {

                    @Override
                    public void done(_User user, BmobException e) {
                        hideProgress();
                        if (e == null) {
                            ToastUtils.showLongToast("注册成功，请登录");
                            finish();
                        } else {
                            ToastUtils.showLongToast("注册失败");
                        }
                    }
                });
            }
        });
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
