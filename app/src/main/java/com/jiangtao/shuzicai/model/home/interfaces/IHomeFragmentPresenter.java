package com.jiangtao.shuzicai.model.home.interfaces;

/**
 * Created by Nicky on 2017/1/16.
 */

public interface IHomeFragmentPresenter {

    //获取指数
    void getIndexData();

    //获取公告数据
    void getBillboardData();

    //获取资产数据
    void getAssetsData();

    //设置指数类型
    void setIndexType(int type);
}
