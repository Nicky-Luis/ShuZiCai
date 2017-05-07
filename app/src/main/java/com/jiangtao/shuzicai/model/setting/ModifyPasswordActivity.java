package com.jiangtao.shuzicai.model.setting;

import android.view.View;
import android.widget.EditText;

import com.blankj.utilcode.utils.LogUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.jiangtao.shuzicai.R;
import com.jiangtao.shuzicai.basic.base.BaseActivityWithToolBar;
import com.jiangtao.shuzicai.basic.utils.EditTextUtils;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class ModifyPasswordActivity extends BaseActivityWithToolBar {

    @BindView(R.id.newPassword)
    EditText newPassword;
    @BindView(R.id.oldPassword)
    EditText oldPassword;

    @OnClick({R.id.confirmBtn})
    public void OnClick(View view) {
        switch (view.getId()) {

            case R.id.confirmBtn: {
                modifyPassword();
            }
            break;
        }
    }

    /**
     * 修改密码
     */
    public void modifyPassword() {
        if (EditTextUtils.isEmpty(oldPassword)) {
            ToastUtils.showShortToast("旧密码不能为空");
            return;
        } else if (EditTextUtils.isEmpty(newPassword)) {
            ToastUtils.showShortToast("新密码不能为空");
            return;
        }
        String oldPwd = EditTextUtils.getContent(oldPassword);
        String newPwd = EditTextUtils.getContent(newPassword);
        showProgress("修改中...");
        BmobUser.updateCurrentUserPassword(oldPwd, newPwd, new UpdateListener() {

            @Override
            public void done(BmobException e) {
                hideProgress();
                if (e == null) {
                    ToastUtils.showShortToast("密码修改成功");
                    finish();
                } else {
                    LogUtils.e("密码修改失败:" + e.getMessage());
                    ToastUtils.showShortToast("密码修改失败"+e.getMessage());
                }
            }
        });
    }


    @Override
    public int setLayoutId() {
        return R.layout.activity_modify_password;
    }

    @Override
    protected void onInitialize() {
        initTitleBar();
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

        setCenterTitle("修改密码");
    }

}
