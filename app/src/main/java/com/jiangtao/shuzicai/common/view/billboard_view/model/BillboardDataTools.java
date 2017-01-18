package com.jiangtao.shuzicai.common.view.billboard_view.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nicky on 2017/1/15.
 * 工具
 */

public class BillboardDataTools {


    //测试数据数据
    public static List<BillboardMessage> getMessageDatas() {
        List<BillboardMessage> scrollDataList= new ArrayList<>();
       /* for (int index = 0; index < 10; index++) {
            BillboardMessage msg = new BillboardMessage(100, "kkkk" + index, BillboardMessage.Type_Join, new Date());
            scrollDataList.add(msg);
            BillboardMessage msg2 = new BillboardMessage(100, "啦啦啦啦" + index, BillboardMessage.Type_Awards, new Date());
            scrollDataList.add(msg2);
            BillboardMessage msg3 = new BillboardMessage(100, "么么么么" + index, BillboardMessage.Type_Bet, new Date());
            scrollDataList.add(msg3);
        }*/
        return scrollDataList;
    }
}
