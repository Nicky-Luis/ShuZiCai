package com.jiangtao.shuzicai.model.game.presenter;

import android.content.Context;

import com.blankj.utilcode.utils.LogUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jiangtao.shuzicai.basic.network.APIInteractive;
import com.jiangtao.shuzicai.basic.network.INetworkResponse;
import com.jiangtao.shuzicai.common.view.billboard_view.model.BillboardMessage;
import com.jiangtao.shuzicai.model.game.interfaces.IGamePresenter;
import com.jiangtao.shuzicai.model.game.view.IGameView;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by Nicky on 2017/2/10.
 */

public class GamePresenter implements IGamePresenter {

    //上下文
    private Context mContext;
    //view对象
    private IGameView gameView;
    //公告数据
    private List<BillboardMessage> billboardMessages;

    public GamePresenter(Context mContext, IGameView view) {
        this.gameView = view;
        this.mContext = mContext;
    }

    @Override
    public void getBillboardData() {
        //查询
        APIInteractive.getBillboardData(new INetworkResponse() {
            @Override
            public void onFailure(int code) {
                LogUtils.i("code=" + code);
                gameView.getDataFail();
            }

            @Override
            public void onSucceed(JSONObject result) {
                LogUtils.i("result = " + result);
                try {
                    String jArray = result.optString("results");
                    billboardMessages = new Gson().fromJson(jArray, new TypeToken<List<BillboardMessage>>() {
                    }.getType());
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
                //公告数据
                gameView.bindBillboardData(billboardMessages);
            }
        });
    }
}
