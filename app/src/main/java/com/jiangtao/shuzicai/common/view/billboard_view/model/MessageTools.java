package com.jiangtao.shuzicai.common.view.billboard_view.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Nicky on 2017/1/15.
 * 工具
 */

public class MessageTools {


    //测试数据数据
    public static List<ScrollMessage> getMessageDatas() {
        List<ScrollMessage> scrollDataList= new ArrayList<>();
        for (int index = 0; index < 10; index++) {
            ScrollMessage msg = new ScrollMessage(100, "kkkk" + index, ScrollMessage.Type_Join, new Date());
            scrollDataList.add(msg);
            ScrollMessage msg2 = new ScrollMessage(100, "啦啦啦啦" + index, ScrollMessage.Type_Awards, new Date());
            scrollDataList.add(msg2);
            ScrollMessage msg3 = new ScrollMessage(100, "么么么么" + index, ScrollMessage.Type_Bet, new Date());
            scrollDataList.add(msg3);
        }
        return scrollDataList;
    }
}
