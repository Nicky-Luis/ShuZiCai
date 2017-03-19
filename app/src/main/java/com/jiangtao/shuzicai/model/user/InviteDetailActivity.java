package com.jiangtao.shuzicai.model.user;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.blankj.utilcode.utils.LogUtils;
import com.jiangtao.shuzicai.Application;
import com.jiangtao.shuzicai.R;
import com.jiangtao.shuzicai.basic.adpter.base_adapter_helper_listview.BaseAdapterHelper;
import com.jiangtao.shuzicai.basic.adpter.base_adapter_helper_listview.QuickAdapter;
import com.jiangtao.shuzicai.basic.base.BaseActivityWithToolBar;
import com.jiangtao.shuzicai.model.user.entry.InviteRecord;
import com.jiangtao.shuzicai.model.user.entry.WealthDetail;
import com.jiangtao.shuzicai.model.user.entry._User;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;


public class InviteDetailActivity extends BaseActivityWithToolBar implements SwipeRefreshLayout.OnRefreshListener {

    //下拉刷新
    @BindView(R.id.invite_swipe_refresh_widget)
    SwipeRefreshLayout inviteSwipeRefreshWidget;
    //列表
    //交易记录Listview
    @BindView(R.id.inviteRecordListView)
    ListView inviteListView;
    @BindView(R.id.InvitationCode)
    TextView InvitationCode;
    //所有用户
    private List<_User> allUsers = new ArrayList<>();
    //第一层用户信息
    private List<_User> firstUsers = new ArrayList<>();
    //第二层用户信息
    private List<_User> secondUsers = new ArrayList<>();
    //第一层用户信息
    private List<_User> thirdUsers = new ArrayList<>();
    //适配器
    private QuickAdapter<InviteRecord> inviteAdapter;
    //数据
    private List<InviteRecord> inviteRecords = new ArrayList<>();


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
                helper.setText(R.id.inviteUserName, item.getUserName());
                helper.setText(R.id.rechargeCount, "充值了：" + item.getRechargeValue() + "金币");
                helper.setText(R.id.rewardValue, "+" + item.getRewardValue() + "金币");
            }
        };
        inviteListView.setAdapter(inviteAdapter);
    }


    //初始化数据
    private void initData() {
        allUsers.clear();
        inviteRecords.clear();
        inviteAdapter.clear();
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
        BmobQuery<_User> query = new BmobQuery<_User>();
        query.addWhereEqualTo("InviteeCode", Application.userInstance.getInvitationCode());
        query.findObjects(new FindListener<_User>() {
            @Override
            public void done(List<_User> list, BmobException e) {
                if (e == null) {
                    firstUsers.clear();
                    firstUsers.addAll(list);
                    if (null != firstUsers && firstUsers.size() > 0) {
                        getWealthRecord(firstUsers, 0.08f);
                        getSecondInvitee(firstUsers);
                    }
                } else {
                    inviteSwipeRefreshWidget.setRefreshing(false);
                    LogUtils.e("更新数据失败");
                }
            }
        });
    }

    /**
     * 二级邀请用户
     */
    private void getSecondInvitee(List<_User> userModels) {
        secondUsers.clear();
        List<String> inviteeCodes = new ArrayList<>();
        for (_User userModel : userModels) {
            inviteeCodes.add(userModel.getInvitationCode());
        }
        //查询二级用户
        BmobQuery<_User> query = new BmobQuery<_User>();
        query.addWhereContainedIn("InviteeCode", inviteeCodes);
        query.findObjects(new FindListener<_User>() {
            @Override
            public void done(List<_User> list, BmobException e) {
                if (e == null) {
                    secondUsers.addAll(list);
                    if (null != secondUsers && secondUsers.size() > 0) {
                        getWealthRecord(secondUsers, 0.04f);
                        getThirdInvitee(secondUsers);
                    }
                } else {
                    inviteSwipeRefreshWidget.setRefreshing(false);
                    LogUtils.e("更新数据失败");
                }
            }
        });
    }

    /**
     * 三级邀请用户
     */
    private void getThirdInvitee(List<_User> userModels) {
        thirdUsers.clear();
        List<String> inviteeCodes = new ArrayList<>();
        for (_User userModel : userModels) {
            inviteeCodes.add(userModel.getInvitationCode());
        }
        //查询三级用户
        BmobQuery<_User> query = new BmobQuery<_User>();
        query.addWhereContainedIn("InviteeCode", inviteeCodes);
        query.findObjects(new FindListener<_User>() {
            @Override
            public void done(List<_User> list, BmobException e) {
                if (e == null) {
                    thirdUsers.addAll(list);
                    getWealthRecord(thirdUsers, 0.02f);
                } else {
                    inviteSwipeRefreshWidget.setRefreshing(false);
                    LogUtils.e("更新数据失败");
                }
            }
        });
    }

    //获取第一级财富值
    private void getWealthRecord(List<_User> userModels, final float type) {
        if (userModels == null) {
            inviteSwipeRefreshWidget.setRefreshing(false);
            return;
        }
        allUsers.addAll(userModels);
        List<String> userIds = new ArrayList<>();
        for (_User user : userModels) {
            userIds.add(user.getObjectId());
        }
        BmobQuery<WealthDetail> query = new BmobQuery<WealthDetail>();
        query.addWhereContainedIn("userId", userIds);
        query.findObjects(new FindListener<WealthDetail>() {
            @Override
            public void done(List<WealthDetail> list, BmobException e) {
                for (WealthDetail wealth : list) {
                    if (wealth.getOperationType() == 0) {
                        for (_User user : allUsers) {
                            if (wealth.getUserId().equals(user.getObjectId())) {
                                inviteAdapter.add(new InviteRecord(wealth.getCreatedAt(),
                                        user.getNickName(), wealth.getOperationValue(),
                                        wealth.getOperationValue() * type));
                                break;
                            }
                        }
                    }
                }
            }
        });
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
        InvitationCode.setText(Application.userInstance.getInvitationCode());
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
