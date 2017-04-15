package com.jiangtao.shuzicai.model.home.view;

import com.jiangtao.shuzicai.common.view.billboard_view.model.BillboardMessage;
import com.jiangtao.shuzicai.common.view.trend_view.model.TrendModel;
import com.jiangtao.shuzicai.model.home.entry.StockIndex;

import java.util.List;

/**
 * Created by Nicky on 2017/1/17.
 */

public interface IHomeFragmentView {

    //获取指数数据
    void onUpdateIndexData(List<TrendModel> datas,String type);

    //绑定数据
    void bindIndexData(StockIndex indexData);

    //绑定公告数据
    void bindBillboardData(List<BillboardMessage> datas);
}
