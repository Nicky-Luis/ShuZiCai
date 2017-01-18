package com.jiangtao.shuzicai.model.home.presenter;


import android.content.Context;

import com.blankj.utilcode.utils.LogUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jiangtao.shuzicai.basic.network.APIInteractive;
import com.jiangtao.shuzicai.basic.network.BmobQueryUtils;
import com.jiangtao.shuzicai.basic.network.INetworkResponse;
import com.jiangtao.shuzicai.common.view.billboard_view.model.BillboardMessage;
import com.jiangtao.shuzicai.common.view.trend_view.model.TrendDataTools;
import com.jiangtao.shuzicai.common.view.trend_view.model.TrendModel;
import com.jiangtao.shuzicai.model.home.entry.StockIndex;
import com.jiangtao.shuzicai.model.home.interfaces.IHomeFragmentPresenter;
import com.jiangtao.shuzicai.model.home.view.IHomeFragmentView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Nicky on 2017/1/16.
 */

public class HomeFragmentPresenter implements IHomeFragmentPresenter {

    //上下文
    private Context mContext;
    //view对象
    private IHomeFragmentView homeFragmentView;
    //五日指数集合
    private List<StockIndex> stockIndices;
    //公告数据
    private List<BillboardMessage> billboardMessages;

    public HomeFragmentPresenter(Context mContext, IHomeFragmentView view) {
        this.homeFragmentView = view;
        this.mContext = mContext;
    }

    //获取数据
    @Override
    public void getIndexData() {

        Calendar calendar = Calendar.getInstance();
        calendar.set(2017, 0, 15, 0, 0, 0);
        BmobQueryUtils utils = BmobQueryUtils.newInstance();

        String where = utils.setValue("date").GreaterThanEqual(
                BmobQueryUtils.getBmobDate(calendar)).buildUrlString();

        //查询
        APIInteractive.getIndexData(where, new INetworkResponse() {
            @Override
            public void onFailure(int code) {
                LogUtils.i("code=" + code);
                ToastUtils.showLongToast("获取数据失败");
            }

            @Override
            public void onSucceed(String result) {
                //LogUtils.i("result = " + result);
                try {
                    JSONObject myJsonObject = new JSONObject(result);
                    String jArray = myJsonObject.optString("results");
                    stockIndices = new Gson().fromJson(jArray, new TypeToken<List<StockIndex>>() {
                    }.getType());
                } catch (JSONException e) {
                    e.printStackTrace();
                    return;
                }

                //获取数据返回,显示上证指数
                homeFragmentView.onUpdateIndexData(TrendDataTools.getTrendDatas(
                        StockIndex.Type_ShangZheng, stockIndices));
                //获取今日的数据
                homeFragmentView.bindIndexData(TrendDataTools.getNewestDatas(
                        StockIndex.Type_ShangZheng, stockIndices));
            }
        });
    }

    /**
     * 获取公告数据
     */
    @Override
    public void getBillboardData() {
        //查询
        APIInteractive.getBillboardData(new INetworkResponse() {
            @Override
            public void onFailure(int code) {
                LogUtils.i("code=" + code);
                ToastUtils.showLongToast("获取数据失败");
            }

            @Override
            public void onSucceed(String result) {
                LogUtils.i("result = " + result);
                try {
                    JSONObject myJsonObject = new JSONObject(result);
                    String jArray = myJsonObject.optString("results");
                    billboardMessages = new Gson().fromJson(jArray, new TypeToken<List<BillboardMessage>>() {
                    }.getType());
                } catch (JSONException e) {
                    e.printStackTrace();
                    return;
                }
                //公告数据
                homeFragmentView.bindBillboardData(billboardMessages);
            }
        });
    }

    /**
     * 获取资产数据
     */
    @Override
    public void getAssetsData() {

    }

    @Override
    public void setIndexType(int type) {
        //获取数据返回,显示上证指数
        List<TrendModel> trendModels = TrendDataTools.getTrendDatas(type, stockIndices);
        homeFragmentView.onUpdateIndexData(trendModels);
        //获取今日的股票数据
        homeFragmentView.bindIndexData(TrendDataTools.getNewestDatas(type, stockIndices));
    }
}