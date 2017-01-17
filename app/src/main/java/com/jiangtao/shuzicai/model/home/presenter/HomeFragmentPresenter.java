package com.jiangtao.shuzicai.model.home.presenter;


import android.content.Context;

import com.blankj.utilcode.utils.LogUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jiangtao.shuzicai.basic.network.APIInteractive;
import com.jiangtao.shuzicai.basic.network.BmobQueryUtils;
import com.jiangtao.shuzicai.basic.network.INetworkResponse;
import com.jiangtao.shuzicai.common.view.trend_view.model.DataTools;
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
    private List<StockIndex> retList;

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

        String where = utils.setValue("date").GreaterThanEqual(BmobQueryUtils.getBmobDate(calendar)).buildUrlString();

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
                    retList = new Gson().fromJson(jArray, new TypeToken<List<StockIndex>>() {
                    }.getType());
                } catch (JSONException e) {
                    e.printStackTrace();
                    return;
                }

                //获取数据返回,显示上证指数
                homeFragmentView.onUpdateIndexData(DataTools.getTrendDatas(
                        StockIndex.Type_ShangZheng, retList));
            }
        });
    }

    @Override
    public void setIndexType(int type) {
        //获取数据返回,显示上证指数
        homeFragmentView.onUpdateIndexData(DataTools.getTrendDatas(type, retList));
    }
}
