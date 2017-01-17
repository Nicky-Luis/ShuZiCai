package com.jiangtao.shuzicai.model.home.view;

import com.jiangtao.shuzicai.common.view.trend_view.model.TrendModel;

import java.util.List;

/**
 * Created by Nicky on 2017/1/17.
 *
 */

public interface IHomeFragmentView {

    //获取指数数据
    void onUpdateIndexData(List<TrendModel> datas);
}
