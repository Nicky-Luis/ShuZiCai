package com.jiangtao.shuzicai.model.user.interfaces;

/**
 * Created by Nicky on 2017/1/21.
 */

public interface IRegisterSetInfoView {

    void onHeadImgPostSucceed(String imageUrl);

    void onHeadImgPostFailed();

    void onUpdateInfoFailed();

    void onUpdateInfoSucceed();
}
