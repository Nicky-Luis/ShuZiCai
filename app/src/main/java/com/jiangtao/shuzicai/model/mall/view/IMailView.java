package com.jiangtao.shuzicai.model.mall.view;

import com.jiangtao.shuzicai.model.mall.entry.Goods;

import java.util.List;

/**
 * Created by Nicky on 2017/2/2.
 */

public interface IMailView {

    void onGetGoods(List<Goods> goodses);

    void onGetGoodsFailed();
}
