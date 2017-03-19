package com.jiangtao.shuzicai.model.game.entry;

import cn.bmob.v3.BmobObject;

/**
 * Created by Nicky on 2017/3/9.
 * d
 */
public class GameInfo extends BmobObject {

    public final static int type_zhangdie = 0;
    public final static int type_weishu = 1;
    public final static int type_quanshu = 2;

    //最新的期数
    private int newestNum;
    //游戏类型,游戏类型0:涨跌，1猜尾数，2：猜全数
    private int gameType;

    public GameInfo() {
        this.setTableName("GameInfo");
    }

    public GameInfo(int newestNum, int gameType) {
        this.newestNum = newestNum;
        this.gameType = gameType;
    }

    public int getNewestNum() {
        return newestNum;
    }

    public int getGameType() {
        return gameType;
    }

    public void setNewestNum(int newestNum) {
        this.newestNum = newestNum;
    }

    public void setGameType(int gameType) {
        this.gameType = gameType;
    }
}
