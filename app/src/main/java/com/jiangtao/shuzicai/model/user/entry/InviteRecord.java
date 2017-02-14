package com.jiangtao.shuzicai.model.user.entry;

import com.jiangtao.shuzicai.common.entity.BmobPointer;

/**
 * Created by Nicky on 2017/2/14.
 * 记录邀请相关的信息
 */

public class InviteRecord {
    //受邀者
    private BmobPointer  InviteeUser;
    //邀请发出者
    private BmobPointer  InviteUser;
    //操作类型 0：注册邀请，1：充值奖励
    private int OperateType;
    //金币类型 0：金币，1：银币
    private int CurrencyType;
}
