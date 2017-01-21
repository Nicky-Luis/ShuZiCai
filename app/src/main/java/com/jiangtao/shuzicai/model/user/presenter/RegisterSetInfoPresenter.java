package com.jiangtao.shuzicai.model.user.presenter;

import android.content.Context;

import com.jiangtao.shuzicai.model.user.interfaces.IRegisterSetInfoPresenter;
import com.jiangtao.shuzicai.model.user.interfaces.IRegisterSetInfoView;

/**
 * Created by Nicky on 2017/1/21.
 */

public class RegisterSetInfoPresenter implements IRegisterSetInfoPresenter {

    //上下文
    private Context mContext;
    //view对象
    private IRegisterSetInfoView registerSetInfoView;

    public RegisterSetInfoPresenter(Context mContext, IRegisterSetInfoView registerSetInfoView) {
        this.mContext = mContext;
        this.registerSetInfoView = registerSetInfoView;
    }
}
