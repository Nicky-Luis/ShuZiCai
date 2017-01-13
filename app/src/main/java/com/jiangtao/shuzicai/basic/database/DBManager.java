package com.jiangtao.shuzicai.basic.database;


import com.jiangtao.shuzicai.Application;
import com.jiangtao.shuzicai.basic.app.BaseGlobal;
import com.litesuits.orm.LiteOrm;

import java.util.List;

/**
 * Created by Nicky on 2016/11/27.
 * database
 */

public class DBManager {

    private static DBManager instance = new DBManager();
    //LiteOrm对象
    private static LiteOrm liteOrm;


    private DBManager() {
        if (liteOrm == null) {
            liteOrm = LiteOrm.newSingleInstance(Application.getApp(), BaseGlobal
                    .DATABASE_NAME);
        }
        // open the log
        liteOrm.setDebugged(true);
    }

    //获取实例
    public static DBManager getInstance(String dbName) {
        return instance;
    }

    /**
     * 插入一条记录
     *
     * @param t
     */
    public <T> long insert(T t) {
        return liteOrm.save(t);
    }

    /**
     * 插入所有记录
     *
     * @param list
     */
    public <T> void insertAll(List<T> list) {
        liteOrm.save(list);
    }

    /**
     * 查询所有
     *
     * @param cla
     * @return
     */
    public <T> List<T> getQueryAll(Class<T> cla) {
        return liteOrm.query(cla);
    }

    /**
     * 删除一个数据
     *
     * @param t
     * @param <T>
     */
    public <T> void delete(T t) {
        liteOrm.delete(t);
    }

    /**
     * 删除一个表
     *
     * @param cla
     * @param <T>
     */
    public <T> void delete(Class<T> cla) {
        liteOrm.delete(cla);
    }

    /**
     * 删除集合中的数据
     *
     * @param list
     * @param <T>
     */
    public <T> void deleteList(List<T> list) {
        liteOrm.delete(list);
    }

    /**
     * 删除数据库
     */
    public void deleteDatabase() {
        liteOrm.deleteDatabase();
    }

}
