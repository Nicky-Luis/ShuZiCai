package com.jiangtao.shuzicai.model.mall.presenter;

import android.content.Context;

import com.blankj.utilcode.utils.LogUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.jiangtao.shuzicai.Application;
import com.jiangtao.shuzicai.basic.network.APIInteractive;
import com.jiangtao.shuzicai.basic.network.BmobQueryUtils;
import com.jiangtao.shuzicai.basic.network.INetworkResponse;
import com.jiangtao.shuzicai.common.entity.BmobBatch;
import com.jiangtao.shuzicai.model.mall.entry.Goods;
import com.jiangtao.shuzicai.model.mall.interfaces.IMailPresenter;
import com.jiangtao.shuzicai.model.mall.view.IMailView;
import com.jiangtao.shuzicai.model.user.entry.WealthDetail;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

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
        BmobQuery<Goods> query = new BmobQuery<Goods>();
        query.setLimit(100);
        query.findObjects(new FindListener<Goods>() {
            @Override
            public void done(List<Goods> goodsList, BmobException e) {
                if (e == null) {
                    mailView.onGetGoods(goodsList);
                    pageCount++;
                } else {
                    ToastUtils.showShortToast("更新数据失败");
                    LogUtils.e("更新数据失败");
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
               // Application.userInstance.setModelData(result);

            }
        });
    }

    //提交兑换
    @Override
    public void submitExchange(float goldNumber) {
        List<BmobBatch> batches = new ArrayList<>();
        float afterGold = Application.userInstance.getGoldValue() - goldNumber;
        float afterSilver = Application.userInstance.getSilverValue() + goldNumber * 1000;
        //记录金币的操作状态
        WealthDetail goldDetail = new WealthDetail(Application.userInstance.getGoldValue(),
                afterGold, WealthDetail.Currency_Type_Gold, WealthDetail.Operation_Type_Exchange,
                goldNumber, Application.userInstance.getObjectId());
        BmobBatch goldRecordBatch = new BmobBatch("POST", "/1/classes/WealthDetail", goldDetail);
        batches.add(goldRecordBatch);
        //记录银币的操作状态
        WealthDetail wealthDetail = new WealthDetail(Application.userInstance.getSilverValue(),
                afterSilver, WealthDetail.Currency_Type_Silver, WealthDetail.Operation_Type_Exchange,
                goldNumber * 1000, Application.userInstance.getObjectId());
        BmobBatch silverRecordBatch = new BmobBatch("POST", "/1/classes/WealthDetail", wealthDetail);
        batches.add(silverRecordBatch);

        //修改财富数据
        Application.userInstance.setGoldValue(afterGold);
        //获取最终封装好的batch
        Map<String, List> batchBean = BmobBatch.getBatchCmd(batches);

        APIInteractive.bmobBatch(batchBean, new INetworkResponse() {
            @Override
            public void onFailure(int code) {
                mailView.onExchangeResult(false);
                ToastUtils.showShortToast("兑换失败");
            }

            @Override
            public void onSucceed(JSONObject result) {
                int resultCount = 0;
                try {
                    JSONArray jsonArray = result.optJSONArray("results");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = (JSONObject) jsonArray.get(i);
                        if (object.has("success")) {
                            resultCount++;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //只有三个值都成功时才提示成功
                if (3 == resultCount) {
                    mailView.onExchangeResult(true);
                    ToastUtils.showShortToast("兑换成功");
                } else {
                    mailView.onExchangeResult(false);
                    ToastUtils.showShortToast("兑换提交异常");
                }
            }
        });
    }

    /**
     * 获取财富
     */
    @Override
    public void getWealthValue() {
        if (null == Application.userInstance) {
            return;
        }
        String userId = Application.userInstance.getObjectId();

        BmobQueryUtils utils = BmobQueryUtils.newInstance();
        String where = utils.setValue("userId").equal(userId);
    }
}
