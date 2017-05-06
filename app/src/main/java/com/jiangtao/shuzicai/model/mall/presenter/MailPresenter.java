package com.jiangtao.shuzicai.model.mall.presenter;

import android.content.Context;

import com.blankj.utilcode.utils.LogUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.jiangtao.shuzicai.AppHandlerService;
import com.jiangtao.shuzicai.Application;
import com.jiangtao.shuzicai.basic.network.APIInteractive;
import com.jiangtao.shuzicai.basic.network.BmobQueryUtils;
import com.jiangtao.shuzicai.basic.network.INetworkResponse;
import com.jiangtao.shuzicai.model.mall.entry.Goods;
import com.jiangtao.shuzicai.model.mall.view.IMailView;
import com.jiangtao.shuzicai.model.user.entry.WealthDetail;

import org.json.JSONObject;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Nicky on 2017/2/2.
 */

public class MailPresenter {

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

    public void getFirstPageGoods() {
        pageCount = 0;
        BmobQuery<Goods> query = new BmobQuery<Goods>();
        query.addWhereEqualTo("isOnline",1);
        query.addWhereGreaterThan("inventory",0);
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
    public void submitExchange(final int goldNumber) {
        final int afterGold = Application.userInstance.getGoldValue() - goldNumber;
        final int afterSilver = Application.userInstance.getSilverValue() + goldNumber * 1000;
        //记录金币的操作状态
        WealthDetail goldDetail = new WealthDetail(
                Application.userInstance.getObjectId(),
                Application.userInstance.getGoldValue(),
                afterGold,
                WealthDetail.Currency_Type_Gold,
                WealthDetail.Operation_Type_Wealth_Exchange,
                goldNumber,1);
        goldDetail.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    //记录银币的操作状态
                    WealthDetail wealthDetail = new WealthDetail(
                            Application.userInstance.getObjectId(),
                            Application.userInstance.getSilverValue(),
                            afterSilver,
                            WealthDetail.Currency_Type_Silver,
                            WealthDetail.Operation_Type_Wealth_Exchange,
                            goldNumber * 1000, 1);
                    wealthDetail.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if (e == null) {
                                //修改财富数据
                                Application.userInstance.setGoldValue(afterGold);
                                Application.userInstance.setSilverValue(afterSilver);
                                ToastUtils.showShortToast("兑换成功");
                                AppHandlerService.updateWealth();
                            } else {
                                mailView.onExchangeResult(false);
                                ToastUtils.showShortToast("兑换失败");
                            }
                        }
                    });
                } else {
                    mailView.onExchangeResult(false);
                    ToastUtils.showShortToast("兑换失败");
                }
            }
        });
    }

    /**
     * 获取财富
     */
    public void getWealthValue() {
        if (null == Application.userInstance) {
            return;
        }
        String userId = Application.userInstance.getObjectId();

        BmobQueryUtils utils = BmobQueryUtils.newInstance();
        String where = utils.setValue("userId").equal(userId);
    }
}
