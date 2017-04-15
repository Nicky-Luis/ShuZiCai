package com.jiangtao.shuzicai.model.home.presenter;


import android.content.Context;
import android.util.Log;

import com.blankj.utilcode.utils.LogUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.jiangtao.shuzicai.common.view.billboard_view.model.BillboardMessage;
import com.jiangtao.shuzicai.common.view.trend_view.model.TrendDataTools;
import com.jiangtao.shuzicai.common.view.trend_view.model.TrendModel;
import com.jiangtao.shuzicai.model.home.entry.StockIndex;
import com.jiangtao.shuzicai.model.home.view.IHomeFragmentView;

import java.util.Date;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;

/**
 * Created by Nicky on 2017/1/16.
 * d
 */

public class HomeFragmentPresenter {

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
    public void getIndexData() {
        //获取服务器时间
        Bmob.getServerTime(new QueryListener<Long>() {
            @Override
            public void done(Long aLong, BmobException e) {
                if (e == null) {
                    int dayCount = 7;
                    BmobDate date = new BmobDate(new Date(aLong - (dayCount * 24 * 60 * 60)));

                    BmobQuery<StockIndex> query = new BmobQuery<StockIndex>();
                    query.addWhereGreaterThan("createdAt",date);
                    //返回50条数据
                    query.setLimit(50);
                    //执行查询方法
                    query.findObjects(new FindListener<StockIndex>() {
                        @Override
                        public void done(List<StockIndex> datas, BmobException e) {
                            if (e == null) {
                                stockIndices = datas;
                                String type = StockIndex.Type_ShangZheng;
                                //获取数据返回,显示上证指数
                                homeFragmentView.onUpdateIndexData(TrendDataTools.getTrendDatas(
                                        type, stockIndices), type);
                                //获取今日的数据
                                homeFragmentView.bindIndexData(TrendDataTools.getNewestDatas(
                                        type, stockIndices));
                            } else {
                                LogUtils.e("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                                ToastUtils.showLongToast("获取股票数据失败");
                            }
                        }
                    });
                    Log.i("bmob", "当前服务器时间为:" + aLong);
                } else {
                    Log.i("bmob", "获取服务器时间失败:" + e.getMessage());
                }
            }
        });
    }

    /**
     * 获取公告数据
     */
    public void getBillboardData() {
        BmobQuery<BillboardMessage> query = new BmobQuery<BillboardMessage>();
        query.setLimit(10);//只允许返回10条信息
        //执行查询方法
        query.findObjects(new FindListener<BillboardMessage>() {
            @Override
            public void done(List<BillboardMessage> datas, BmobException e) {
                if (e == null) {
                    billboardMessages = datas;
                    //公告数据
                    homeFragmentView.bindBillboardData(billboardMessages);
                } else {
                    LogUtils.e("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                    ToastUtils.showLongToast("获取数据失败");
                }
            }
        });
    }


    //筛选类型
    public void setIndexType(String type) {
        //获取数据返回
        List<TrendModel> trendModels = TrendDataTools.getTrendDatas(type, stockIndices);
        homeFragmentView.onUpdateIndexData(trendModels, type);
        //获取今日的股票数据
        homeFragmentView.bindIndexData(TrendDataTools.getNewestDatas(type, stockIndices));
    }

}