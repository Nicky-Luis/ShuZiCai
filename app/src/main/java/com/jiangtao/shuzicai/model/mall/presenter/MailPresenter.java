package com.jiangtao.shuzicai.model.mall.presenter;

import android.content.Context;

import com.blankj.utilcode.utils.LogUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jiangtao.shuzicai.Application;
import com.jiangtao.shuzicai.basic.network.APIInteractive;
import com.jiangtao.shuzicai.basic.network.INetworkResponse;
import com.jiangtao.shuzicai.model.mall.entry.Goods;
import com.jiangtao.shuzicai.model.mall.interfaces.IMailPresenter;
import com.jiangtao.shuzicai.model.mall.view.IMailView;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by Nicky on 2017/2/2.
 */

public class MailPresenter implements IMailPresenter {

    //每页的数量
    private int limit = 100;
    //页数
    private int pageCount = 0;
    //view对象
    private IMailView mailView;
    //上下文
    private Context mContext;

    public MailPresenter(IMailView mailView, Context mContext) {
        this.mailView = mailView;
        this.mContext = mContext;
    }

    @Override
    public void getFirstPageGoods() {
        pageCount = 0;
        APIInteractive.getGoods(limit, pageCount * limit, new INetworkResponse() {
            @Override
            public void onFailure(int code) {
                LogUtils.e("更新数据失败");
            }

            @Override
            public void onSucceed(JSONObject result) {
                try {
                    String jArray = result.optString("results");
                    List<Goods> goodsList = new Gson().fromJson(jArray, new TypeToken<List<Goods>>() {
                    }.getType());
                    //更新数据
                    mailView.onGetGoods(goodsList);
                    pageCount++;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void getMoreGoods() {
        APIInteractive.getGoods(limit, pageCount * limit, new INetworkResponse() {
            @Override
            public void onFailure(int code) {
                LogUtils.e("更新数据失败");
            }

            @Override
            public void onSucceed(JSONObject result) {
                pageCount++;
                //更新数据
                Application.userInstance.setModelData(result);

            }
        });
    }
}
