package com.jiangtao.shuzicai.model.game.view;

import com.jiangtao.shuzicai.common.view.billboard_view.model.BillboardMessage;

import java.util.List;

/**
 * Created by Nicky on 2017/2/10.
 */

public interface IGameView {

    //绑定公告数据
    void bindBillboardData(List<BillboardMessage> datas);

    void getDataFail();
}
