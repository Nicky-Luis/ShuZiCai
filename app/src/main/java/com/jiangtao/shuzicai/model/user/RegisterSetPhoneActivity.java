package com.jiangtao.shuzicai.model.user;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.blankj.utilcode.utils.ToastUtils;
import com.jiangtao.shuzicai.R;
import com.jiangtao.shuzicai.basic.base.BaseActivityWithToolBar;
import com.jiangtao.shuzicai.common.tools.CountDownTimer;
import com.jiangtao.shuzicai.model.user.interfaces.IRegisterPhoneView;
import com.jiangtao.shuzicai.model.user.interfaces.IRegisterPresenter;
import com.jiangtao.shuzicai.model.user.presenter.RegisterPresenter;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

public class RegisterSetPhoneActivity extends BaseActivityWithToolBar implements IRegisterPhoneView {

    //获取验证码
    @BindView(R.id.VerifyCodeBtn)
    Button VerifyCodeBtn;
    //获取验证码
    @BindView(R.id.phoneNumberEdt)
    EditText phoneNumberEdt;
    //获取验证码
    @BindView(R.id.verifyCodeEdt)
    EditText verifyCodeEdt;


    //设置点击事件
    @OnClick({R.id.VerifyCodeBtn})
    public void OnClick(View view) {
        switch (view.getId()) {

            //获取验证码
            case R.id.VerifyCodeBtn:
                getVerifyCode();
                break;

        }
    }

    //注册
    private IRegisterPresenter registerPresenter;

    @Override
    public int setLayoutId() {
        return R.layout.activity_register_set_phone;
    }

    @Override
    protected void onInitialize() {

    }

    @Override
    public void initPresenter() {
        this.registerPresenter = new RegisterPresenter(this, this);
    }

    /**
     * 获取验证码
     */
    private void getVerifyCode() {
        registerPresenter.getVerifyCode(phoneNumberEdt.getText().toString().trim());
        CountDownTimer timer = new CountDownTimer(1000 * 60, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                if (!VerifyCodeBtn.isEnabled()) {
                    VerifyCodeBtn.setEnabled(false);
                }
                String still = millisUntilFinished / 1000 + "秒";
                VerifyCodeBtn.setText(still);
            }

            @Override
            public void onFinish() {
                VerifyCodeBtn.setEnabled(true);
                VerifyCodeBtn.setText("发送验证码");
            }
        };
    }

    @Override
    public void onPhoneErr() {
        ToastUtils.showLongToast("号码无效");
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
