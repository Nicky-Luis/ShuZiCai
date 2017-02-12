package com.jiangtao.shuzicai.common.event_message;

import com.jiangtao.shuzicai.model.user.entry.WealthValue;

/**
 * Created by Nicky on 2017/2/12.
 */

public class WealthChangeMsg {
    private WealthValue wealthValue;

    public WealthChangeMsg(WealthValue wealthValue) {
        this.wealthValue = wealthValue;
    }

    public WealthValue getWealthValue() {
        return wealthValue;
    }

    public void setWealthValue(WealthValue wealthValue) {
        this.wealthValue = wealthValue;
    }
}
