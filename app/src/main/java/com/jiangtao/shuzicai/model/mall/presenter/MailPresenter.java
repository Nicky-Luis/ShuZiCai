package com.jiangtao.shuzicai.model.mall.presenter;

import android.content.Context;

import com.blankj.utilcode.utils.LogUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jiangtao.shuzicai.Application;
import com.jiangtao.shuzicai.basic.network.APIInteractive;
import com.jiangtao.shuzicai.basic.network.BmobQueryUtils;
import com.jiangtao.shuzicai.basic.network.INetworkResponse;
import com.jiangtao.shuzicai.common.entity.BmobBatch;
import com.jiangtao.shuzicai.common.event_message.WealthChangeMsg;
import com.jiangtao.shuzicai.model.mall.entry.Goods;
import com.jiangtao.shuzicai.model.mall.interfaces.IMailPresenter;
import com.jiangtao.shuzicai.model.mall.view.IMailView;
import com.jiangtao.shuzicai.model.user.entry.WealthDetail;
import com.jiangtao.shuzicai.model.user.entry.WealthValue;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    //提交兑换
    @Override
    public void submitExchange(float goldNumber) {
        List<BmobBatch> batches = new ArrayList<>();
        float afterGold = Application.wealthValue.getGoldValue() - goldNumber;
        float afterSilver = Application.wealthValue.getSilverValue() + goldNumber * 1000;
        //记录金币的操作状态
        WealthDetail goldDetail = new WealthDetail(Application.wealthValue.getGoldValue(),
                afterGold, WealthDetail.Currency_Type_Gold, WealthDetail.Operation_Type_Exchange,
                goldNumber, Application.userInstance.getObjectId());
        BmobBatch goldRecordBatch = new BmobBatch("POST", "/1/classes/WealthDetail", goldDetail);
        batches.add(goldRecordBatch);
        //记录银币的操作状态
        WealthDetail wealthDetail = new WealthDetail(Application.wealthValue.getSilverValue(),
                afterSilver, WealthDetail.Currency_Type_Silver, WealthDetail.Operation_Type_Exchange,
                goldNumber * 1000, Application.userInstance.getObjectId());
        BmobBatch silverRecordBatch = new BmobBatch("POST", "/1/classes/WealthDetail", wealthDetail);
        batches.add(silverRecordBatch);

        //修改财富数据
        WealthValue wealthValue = new WealthValue();
        Application.wealthValue.setGoldValue(afterGold);
        wealthValue.setGoldValue(afterGold);
        wealthValue.setSilverValue(afterSilver);
        BmobBatch wealthBatch = new BmobBatch("PUT", "/1/classes/WealthValue/" + Application.wealthValue.getObjectId
                (), wealthValue);
        batches.add(wealthBatch);
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

        //获取当前用户的信息
        APIInteractive.getWealthValue(where, new INetworkResponse() {
            @Override
            public void onFailure(int code) {
                LogUtils.e("更新数据失败");
            }

            @Override
            public void onSucceed(JSONObject result) {
                //更新数据
                try {
                    String jArray = result.optString("results");
                    List<WealthValue> wealthValues = new Gson().fromJson(jArray, new TypeToken<List<WealthValue>>() {
                    }.getType());
                    if (wealthValues.size() > 0) {
                        Application.wealthValue = wealthValues.get(0);
                        EventBus.getDefault().post(new WealthChangeMsg(wealthValues.get(0)));
                    } else {
                        ToastUtils.showShortToast("财富数据更新失败");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
