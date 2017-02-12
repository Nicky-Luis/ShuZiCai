package com.jiangtao.shuzicai.common.event_message;

/**
 * Created by Nicky on 2017/1/23.
 */

public class LoginMsg {
    private boolean result;

    public LoginMsg(boolean result) {
        this.result = result;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }
}
