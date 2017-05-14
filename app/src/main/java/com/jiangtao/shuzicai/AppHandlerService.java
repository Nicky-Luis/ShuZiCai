package com.jiangtao.shuzicai;

import com.blankj.utilcode.utils.LogUtils;
import com.jiangtao.shuzicai.common.event_message.LoginMsg;
import com.jiangtao.shuzicai.common.event_message.WealthChangeMsg;
import com.jiangtao.shuzicai.model.game.entry.Config;
import com.jiangtao.shuzicai.model.game.entry.LondonGold;
import com.jiangtao.shuzicai.model.user.entry.WealthDetail;
import com.jiangtao.shuzicai.model.user.entry._User;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BatchResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.QueryListListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Nicky on 2017/5/1.
 * 全局函数
 */

public class AppHandlerService {

    /**
     * 开始登录
     *
     * @param account
     * @param password
     */
    public static void startLogin(String account, String password) {
        BmobUser.loginByAccount(account, password, new LogInListener<_User>() {

            @Override
            public void done(_User user, BmobException e) {
                if (user != null) {
                    Application.userInstance = user;
                    EventBus.getDefault().post(new LoginMsg(true));
                    LogUtils.i("登录成功");
                    //同步财富数据
                    synchInviteWealth();
                } else {
                    Application.userInstance = null;
                    EventBus.getDefault().post(new LoginMsg(false));
                    LogUtils.e("登录失败：" + e);
                }
                EventBus.getDefault().post(new WealthChangeMsg());
            }
        });

    }

    //更新用户财务数据
    public static void updateWealth() {
        Application.userInstance.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    LogUtils.i("更新用户信息数据成功");
                    EventBus.getDefault().post(new WealthChangeMsg());
                } else {
                    LogUtils.i("更新用户信息数据失败:" + e);
                }
            }
        });
    }


    /**
     * 获取伦敦金数据
     *
     * @param callBack
     */
    public static void getLondonData(DataCallBack callBack) {
        getPeriodsCount(callBack);
        startGetLondon(callBack);
    }

    ////////////////////////////同步邀请财富数据///////////////////////////////
    //同步用户邀请奖励的财富数据
    public static void synchInviteWealth() {
        //邀请奖励的类型
        List<Integer> types = new ArrayList<>();
        types.add(WealthDetail.Operation_Type_Invite_First);
        types.add(WealthDetail.Operation_Type_Invite_Second);
        types.add(WealthDetail.Operation_Type_Invite_Third);
        //开始查询数据
        BmobQuery<WealthDetail> query = new BmobQuery<WealthDetail>();
        query.addWhereEqualTo("userId", Application.userInstance.getObjectId());
        query.addWhereContainedIn("operationType", types);
        query.findObjects(new FindListener<WealthDetail>() {
            @Override
            public void done(List<WealthDetail> list, BmobException e) {
                if (e == null) {
                    if (list != null && list.size() > 0) {
                        //如果上限对于50
                        int num = list.size() / 50;
                        for (int count = 0; count <= num; count++) {
                            int endNum = (count * 50) + 49;
                            int end = endNum > list.size() ? list.size() : endNum;
                            startInviteUpdate(list.subList(count * 50, end));
                        }
                    } else {
                        LogUtils.e("没有找到邀请奖励财富信息");
                    }
                } else {
                    LogUtils.e("找寻邀请奖励财富信息异常" + e);
                }
            }
        });
    }

    /**
     * 同步更新用户的邀请获取的财富
     *
     * @param list
     */
    public static void startInviteUpdate(List<WealthDetail> list) {
        int totalValue = 0;
        final List<BmobObject> datas = new ArrayList<BmobObject>();
        for (WealthDetail wealthDetail : list) {
            totalValue = totalValue + wealthDetail.getOperationValue();
            wealthDetail.setFlag(1);
            datas.add(wealthDetail);
        }
        //先更新用户的财务数据
        int afterValue = Application.userInstance.getGoldValue() + totalValue;
        Application.userInstance.setGoldValue(afterValue);
        Application.userInstance.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    LogUtils.i("更新用户信息数据成功");
                    EventBus.getDefault().post(new WealthChangeMsg());

                    //财务数据更新成功后再批量更新的方式
                    new BmobBatch().updateBatch(datas).doBatch(new QueryListListener<BatchResult>() {

                        @Override
                        public void done(List<BatchResult> list, BmobException e) {
                            if (e == null) {
                                for (int i = 0; i < list.size(); i++) {
                                    BatchResult result = list.get(i);
                                    BmobException ex = result.getError();
                                    if (ex == null) {
                                        LogUtils.i("第" + i + "个数据批量更新成功：" + result.getUpdatedAt());
                                    } else {
                                        LogUtils.i("第" + i + "个数据批量更新失败：" + ex.getMessage() + "," + ex.getErrorCode());
                                    }
                                }
                            } else {
                                LogUtils.i("失败：" + e.getMessage() + "," + e.getErrorCode());
                            }
                        }
                    });
                } else {
                    LogUtils.e("更新用户信息数据失败:" + e);
                }
            }
        });
    }


    /**
     * 获取最新的期数
     */
    private static void getPeriodsCount(final DataCallBack callBack) {
        if (null == callBack) {
            return;
        }
        BmobQuery<Config> query = new BmobQuery<Config>();
        query.getObject(Config.objectId, new QueryListener<Config>() {
            @Override
            public void done(Config gameInfo, BmobException e) {
                LogUtils.i("获取期数完成：");
                if (e == null && gameInfo != null) {
                    callBack.onGetPeriodsCount(gameInfo.getNewestNum(),gameInfo.isTread());
                } else {
                    callBack.onGetDataFail();
                    LogUtils.i("获取最新指数失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }

    /**
     * 获取最新的伦敦金数据
     */
    private static void startGetLondon(final DataCallBack callBack) {
        if (null == callBack) {
            return;
        }
        BmobQuery<LondonGold> query = new BmobQuery<LondonGold>();
        query.order("-createdAt");
        query.setLimit(1);
        //执行查询方法
        query.findObjects(new FindListener<LondonGold>() {
            @Override
            public void done(List<LondonGold> list, BmobException e) {
                if (e == null && null != list) {
                    if (list.size() > 0) {
                        //指数值
                        LondonGold indexData = list.get(0);
                        float price = Float.valueOf(indexData.getLatestpri());
                        callBack.onGetGoldLondon(price, indexData.getChange(), indexData.getLimit());
                    } else {
                        callBack.onGetDataFail();
                    }
                } else {
                    callBack.onGetDataFail();
                    LogUtils.i("获取最新指数失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }


    //回调
    public interface DataCallBack {
        void onGetGoldLondon(float price, String change, String limit);

        void onGetPeriodsCount(int periodsCount,boolean isTread);

        void onGetDataFail();
    }
}
