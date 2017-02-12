package com.jiangtao.shuzicai.common.entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Nicky on 2017/2/10.
 * 比目批量操作
 */

public class BmobBatch {
    //方法
    private String method;
    //token
    private String token;
    //路径
    private String path;
    //对象
    private Object body;

    public BmobBatch() {
    }

    public BmobBatch(String method, String path, Object body) {
        this.method = method;
        this.token = null;
        this.path = path;
        this.body = body;
    }

    public BmobBatch(String method, String token, String path, Object body) {
        this.method = method;
        this.token = token;
        this.path = path;
        this.body = body;
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public Object getBody() {
        return body;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setBody(String body) {
        this.body = body;
    }

    /**
     * 封装batch
     *
     * @param batches
     * @return
     */
    public static Map<String, List> getBatchCmd(List<BmobBatch> batches) {
        Map<String, List> batchMap = new HashMap<>();
        batchMap.put("requests", batches);
        return batchMap;
    }
}
