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
import com.jiangtao.shuzicai.model.share.onekeyshare.OnekeyShare;
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
    //第一层用户信息
    private List<_User> firstUsers = new ArrayList<>();
    //第二层用户信息
    private List<_User> secondUsers = new ArrayList<>();
    //第三层用户信息
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
                showShare();
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
        inviteRecords.clear();
        inviteAdapter.clear();
        //先获取我邀请的用户
        getMyInvitee();
    }

    //////////////////////////////////////////////////////////////////

    private void showShare() {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网、QQ和QQ空间使用
        oks.setTitle("标题");
        // titleUrl是标题的网络链接，仅在Linked-in,QQ和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite("ShareSDK");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

// 启动分享GUI
        oks.show(this);
    }

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
                    if (null != list && list.size() > 0) {
                        LogUtils.i("找到第一级用户数量：" + list.size());
                        firstUsers.clear();
                        firstUsers.addAll(list);
                        if (null != firstUsers && firstUsers.size() > 0) {
                            getWealthRecord(firstUsers, WealthDetail.Operation_Type_Invite_First);
                            getSecondInvitee(firstUsers);
                        }
                    } else {
                        inviteSwipeRefreshWidget.setRefreshing(false);
                        LogUtils.e("没有找到第一级用户信息");
                    }
                } else {
                    inviteSwipeRefreshWidget.setRefreshing(false);
                    LogUtils.e("查找第一级用户信息异常" + e);
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
                    if (null != list && list.size() > 0) {
                        LogUtils.i("找到第二级用户数量：" + list.size());
                        secondUsers.clear();
                        secondUsers.addAll(list);
                        if (null != secondUsers && secondUsers.size() > 0) {
                            getWealthRecord(secondUsers, WealthDetail.Operation_Type_Invite_Second);
                            getThirdInvitee(secondUsers);
                        }
                    } else {
                        inviteSwipeRefreshWidget.setRefreshing(false);
                        LogUtils.e("没有找到第二级用户信息");
                    }
                } else {
                    inviteSwipeRefreshWidget.setRefreshing(false);
                    LogUtils.e("查找第二级用户信息异常" + e);

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
                    if (null != list && list.size() > 0) {
                        LogUtils.i("找到第二级用户数量：" + list.size());
                        thirdUsers.clear();
                        thirdUsers.addAll(list);
                        getWealthRecord(thirdUsers, WealthDetail.Operation_Type_Invite_Third);
                    } else {
                        inviteSwipeRefreshWidget.setRefreshing(false);
                        LogUtils.e("没有找到第三级用户信息");
                    }
                } else {
                    inviteSwipeRefreshWidget.setRefreshing(false);
                    LogUtils.e("查找第三级用户信息异常" + e);
                }
            }
        });
    }

    //获取财富值
    private void getWealthRecord(List<_User> userModels, final int type) {
        if (userModels == null) {
            inviteSwipeRefreshWidget.setRefreshing(false);
            return;
        }
        List<String> userIds = new ArrayList<>();
        for (_User user : userModels) {
            userIds.add(user.getObjectId());
        }
        BmobQuery<WealthDetail> query = new BmobQuery<WealthDetail>();
        query.addWhereContainedIn("userId", userIds);
        query.addWhereEqualTo("operationType", type);
        query.findObjects(
                new FindListener<WealthDetail>() {
                    @Override
                    public void done(List<WealthDetail> list, BmobException e) {
                        if (e == null) {
                            if (null != list && list.size() > 0) {
                                LogUtils.i("查找邀请用户的财富信息成功");
                                setWealthView(list, type);
                            } else {
                                LogUtils.e("没有找到这一级的财富" + type);
                            }
                        } else {
                            LogUtils.e("查找这一级邀请用户的财富信息失败" + e);
                        }
                    }
                }
        );
    }

    /**
     * 查找成功后设置view
     *
     * @param list
     * @param type
     */
    public void setWealthView(List<WealthDetail> list, int type) {
        List<_User> users = new ArrayList<>();
        float rate = 0.08f;
        if (type == WealthDetail.Operation_Type_Invite_First) {
            rate = 0.08f;
            users.addAll(firstUsers);
        } else if (type == WealthDetail.Operation_Type_Invite_Second) {
            rate = 0.04f;
            users.addAll(secondUsers);
        } else if (type == WealthDetail.Operation_Type_Invite_Third) {
            rate = 0.02f;
            users.addAll(thirdUsers);
        }

        for (WealthDetail wealth : list) {
            for (_User user : users) {
                if (wealth.getUserId().equals(user.getObjectId())) {
                    InviteRecord inviteRecord = new InviteRecord(
                            wealth.getCreatedAt(),
                            user.getNickName(),
                            wealth.getOperationValue(),
                            (int) (wealth.getOperationValue() * rate));
                    inviteAdapter.add(inviteRecord);
                    break;
                }
            }
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
