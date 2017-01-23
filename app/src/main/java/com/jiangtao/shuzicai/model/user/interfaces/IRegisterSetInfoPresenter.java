package com.jiangtao.shuzicai.model.user.interfaces;

import com.jiangtao.shuzicai.model.user.entry.UpdateInfoBean;

/**
 * Created by Nicky on 2017/1/21.
 */

public interface IRegisterSetInfoPresenter {

    //上传文件
    void startPostHeadImg(String filePath);

    //设置用户信息
    void startSetUserInfo(String token, String onjectID,UpdateInfoBean bean);
}
