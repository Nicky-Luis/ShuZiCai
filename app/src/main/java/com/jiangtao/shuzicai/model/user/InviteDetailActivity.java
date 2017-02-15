package com.jiangtao.shuzicai.model.user;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ListView;

import com.blankj.utilcode.utils.LogUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jiangtao.shuzicai.Application;
import com.jiangtao.shuzicai.R;
import com.jiangtao.shuzicai.basic.adpter.base_adapter_helper_listview.BaseAdapterHelper;
import com.jiangtao.shuzicai.basic.adpter.base_adapter_helper_listview.QuickAdapter;
import com.jiangtao.shuzicai.basic.base.BaseActivityWithToolBar;
import com.jiangtao.shuzicai.basic.network.APIInteractive;
import com.jiangtao.shuzicai.basic.network.BmobQueryUtils;
import com.jiangtao.shuzicai.basic.network.INetworkResponse;
import com.jiangtao.shuzicai.model.user.entry.InviteRecord;
import com.jiangtao.shuzicai.model.user.entry.UserModel;
import com.jiangtao.shuzicai.model.user.entry.WealthDetail;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


public class InviteDetailActivity extends BaseActivityWithToolBar implements SwipeRefreshLayout.OnRefreshListener {

    //下拉刷新
    @BindView(R.id.invite_swipe_refresh_widget)
    SwipeRefreshLayout inviteSwipeRefreshWidget;
    //列表
    //交易记录Listview
    @BindView(R.id.inviteRecordListView)
    ListView inviteListView;
    //第一层用户信息
    private List<UserModel> firstUsers;
    //第二层用户信息
    private List<UserModel> secondUsers = new ArrayList<>();
    //第一层用户信息
    private List<UserModel> thirdUsers = new ArrayList<>();

    //适配器
    private QuickAdapter<InviteRecord> inviteAdapter;


    //设置点击事件
    @OnClick({R.id.inviteTxt})
    public void OnClick(View view) {
        switch (view.getId()) {

            //邀请
            case R.id.inviteTxt: {
            }
            break;
        }
    }

    //初始化swipe
    private void initSwipeRefresh() {
        inviteSwipeRefreshWidget.setColorSchemeResources(
                android.R.color.holo_blue_light,
                android.R.color.holo_green_light,
                android.R.color.holo_red_light,
                android.R.color.holo_orange_light);
        inviteSwipeRefreshWidget.setOnRefreshListener(this);
    }


    @Override
    public void onRefresh() {
        //下拉刷新
        initData();
    }

    //初始化ListView
    private void initListView() {
        //初始化适配器
        inviteAdapter = new QuickAdapter<InviteRecord>(this,
                R.layout.item_invite_record_listview,

                new ArrayList<InviteRecord>()) {
            @Override
            protected void convert(BaseAdapterHelper helper, InviteRecord item) {
                helper.setText(R.id.rechargeTime, item.getTime());
                helper.setText(R.id.inviteUserName, item.getInviteeUser());
                helper.setText(R.id.rechargeCount, "充值了：" + item.getRechargeValue() + "金币");
                helper.setText(R.id.rewardValue, "+" + item.getRewardValue() + "金币");
            }
        };
        inviteListView.setAdapter(inviteAdapter);
    }


    //初始化数据
    private void initData() {
        //先获取我邀请的用户
        getMyInvitee();
    }

    //////////////////////////////////////////////////////////////////

    /**
     * 首先获取我的邀请者
     */
    private void getMyInvitee() {
        if (null == Application.userInstance) {
            inviteSwipeRefreshWidget.setRefreshing(false);
            return;
        }
        String userId = Application.userInstance.getInvitationCode();

        BmobQueryUtils utils = BmobQueryUtils.newInstance();
        String where = utils.setValue("InviteeCode").equal(userId);

        //获取用户的信息
        APIInteractive.getUserInfo(where, new INetworkResponse() {
            @Override
            public void onFailure(int code) {
                inviteSwipeRefreshWidget.setRefreshing(false);
                LogUtils.e("更新数据失败");
            }

            @Override
            public void onSucceed(JSONObject result) {
                //更新数据
                try {
                    String jArray = result.optString("results");
                    firstUsers = new Gson().fromJson(jArray, new TypeToken<List<UserModel>>() {
                    }.getType());
                    if (null != firstUsers && firstUsers.size() > 0) {
                        getSecondInvitee(firstUsers);
                    }
                } catch (Exception e) {
                    inviteSwipeRefreshWidget.setRefreshing(false);
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 二级邀请用户
     */
    private void getSecondInvitee(List<UserModel> userModels) {
        final int size = userModels.size();
        for (UserModel userModel : userModels) {
            BmobQueryUtils utils = BmobQueryUtils.newInstance();
            String where = utils.setValue("InviteeCode").equal(userModel.getInvitationCode());

            //获取用户的信息
            APIInteractive.getUserInfo(where, new INetworkResponse() {
                int count = 0;

                @Override
                public void onFailure(int code) {
                    inviteSwipeRefreshWidget.setRefreshing(false);
                    LogUtils.e("更新数据失败");
                }

                @Override
                public void onSucceed(JSONObject result) {
                    //更新数据
                    try {
                        String jArray = result.optString("results");
                        List<UserModel> users = new Gson().fromJson(jArray, new TypeToken<List<UserModel>>() {
                        }.getType());
                        if (null != users && users.size() > 0) {
                            secondUsers.addAll(users);
                        }
                        //查询到最后一个时
                        count++;
                        if (count >= size && null != secondUsers && secondUsers.size() > 0) {
                            getThirdInvitee(secondUsers);
                        }
                    } catch (Exception e) {
                        inviteSwipeRefreshWidget.setRefreshing(false);
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    /**
     * 三级邀请用户
     */
    private void getThirdInvitee(List<UserModel> userModels) {
        final int size = userModels.size();
        for (UserModel userModel : userModels) {
            LogUtils.i("用户名："+userModel.getNickName());
            BmobQueryUtils utils = BmobQueryUtils.newInstance();
            String where = utils.setValue("InviteeCode").equal(userModel.getInvitationCode());

            //获取用户的信息
            APIInteractive.getUserInfo(where, new INetworkResponse() {
                int count = 0;

                @Override
                public void onFailure(int code) {
                    LogUtils.e("更新数据失败");
                    inviteSwipeRefreshWidget.setRefreshing(false);
                }

                @Override
                public void onSucceed(JSONObject result) {
                    //更新数据
                    try {
                        String jArray = result.optString("results");
                        List<UserModel> users = new Gson().fromJson(jArray, new TypeToken<List<UserModel>>() {
                        }.getType());
                        if (null != users && users.size() > 0) {
                            thirdUsers.addAll(users);
                        }
                        //查询到最后一个时
                        count++;
                        if (count >= size) {
                            getWealthRecord(firstUsers, 0.08f);
                            getWealthRecord(secondUsers, 0.04f);
                            getWealthRecord(thirdUsers, 0.02f);
                        }
                    } catch (Exception e) {
                        inviteSwipeRefreshWidget.setRefreshing(false);
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    //获取第一级财富值
    private void getWealthRecord(List<UserModel> userModels, final float type) {
        if (userModels == null) {
            inviteSwipeRefreshWidget.setRefreshing(false);
            return;
        }
        for (final UserModel userModel : userModels) {
            BmobQueryUtils utils = BmobQueryUtils.newInstance();
            String where = utils.setValue("userId").equal(userModel.getObjectId());

            //获取用户的信息
            APIInteractive.getWealthDetailRecord(where, new INetworkResponse() {
                @Override
                public void onFailure(int code) {
                    inviteSwipeRefreshWidget.setRefreshing(false);
                    LogUtils.e("更新数据失败");
                }

                @Override
                public void onSucceed(JSONObject result) {
                    inviteSwipeRefreshWidget.setRefreshing(false);
                    //更新数据
                    try {
                        String jArray = result.optString("results");
                        List<WealthDetail> wealthDetails = new Gson().fromJson(jArray, new
                                TypeToken<List<WealthDetail>>() {
                                }.getType());
                        for (WealthDetail wealth : wealthDetails) {
                            if (wealth.getOperationType() == 0) {
                                inviteAdapter.add(new InviteRecord(wealth.getCreateAt(),
                                        userModel.getNickName(), wealth.getOperationValue(),
                                        wealth.getOperationValue() * type));
                            }
                        }
                    } catch (Exception e) {
                        inviteSwipeRefreshWidget.setRefreshing(false);
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    ////////////////////////////////////////////////////////////////////
    @Override
    public int setLayoutId() {
        return R.layout.activity_invite_detail;
    }

    @Override
    protected void onInitialize() {
        initTitleBar();
        initSwipeRefresh();
        initListView();
        initData();
    }

    @Override
    public void initPresenter() {

    }

    private void initTitleBar() {
        //右键
        setLeftImage(R.mipmap.ic_arrow_back_white_24dp, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        setCenterTitle("我的邀请");
    }
}
