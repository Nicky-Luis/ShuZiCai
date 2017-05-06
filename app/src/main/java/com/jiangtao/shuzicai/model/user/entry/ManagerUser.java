package com.jiangtao.shuzicai.model.user.entry;

import cn.bmob.v3.BmobObject;

/**
 * Created by Nicky on 2017/3/18.
 * 管理类用户，包括管理员与虚拟用户
 */

public class ManagerUser extends BmobObject {
    //用户名
    private String name;
    //账户
    private String account;
    //密码
    private String password;

    public ManagerUser(String name, String account, String password) {
        this.name = name;
        this.account = account;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getAccount() {
        return account;
    }

    public String getPassword() {
        return password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
