package com.jiangtao.shuzicai.common.view.billboard_view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.blankj.utilcode.utils.LogUtils;
import com.jiangtao.shuzicai.R;
import com.jiangtao.shuzicai.basic.adpter.base_adapter_helper_listview.BaseAdapterHelper;
import com.jiangtao.shuzicai.basic.adpter.base_adapter_helper_listview.QuickAdapter;
import com.jiangtao.shuzicai.common.view.billboard_view.model.BillboardMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nicky on 2017/1/14.
 * 广播滚动view
 */

public class BillboardView extends RelativeLayout {

    //handler key
    private final static int Handler_Key = 0;
    //滚动时间2s
    private final static int Scroll_Time = 1000 /** 2*/;
    //滚动条数
    private final static int Scroll_Count = 3;
    //列表
    private ListView mListView;
    //适配器
    private QuickAdapter<BillboardMessage> adapter;
    //mContext
    private Context mContext;
    //所有的数据源
    private List<BillboardMessage> scrollDataList;
    //周期值
    private int periodCount;
    //滚动状态值
    private boolean scrollDataEnable = false;
    //滚动状态值
    private boolean isStart = false;

    public BillboardView(Context context) {
        super(context);
        constructView(context);
    }

    public BillboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        constructView(context);
    }

    public BillboardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        constructView(context);
    }

    private void constructView(Context context) {
        this.mContext = context;
        this.periodCount = 0;
        // 导入布局
        View rootView = LayoutInflater.from(context).inflate(R.layout.view_billboard_layout, this, true);
        initView(rootView);
    }

    /**
     * 滚动view
     */
    private void setScrollData() {
        //LogUtils.i("---------setScrollData ：" + periodCount);
        if (adapter.getCount() >= Scroll_Count) {
            adapter.remove(0);
        }
        //添加下一个
        BillboardMessage message =scrollDataList.get(periodCount);
        //LogUtils.i("---------绑定的 ：" + message.toString());
        adapter.add(message);
        periodCount++;
        if (periodCount >= scrollDataList.size()) {
            periodCount = 0;
        }
    }

    /**
     * 初始化
     */
    private void initView(View rootView) {
        //listView
        mListView = (ListView) rootView.findViewById(R.id.billboardListView);
        //初始化适配器
        adapter = new QuickAdapter<BillboardMessage>(mContext,
                R.layout.item_billboardview_listview, new ArrayList<BillboardMessage>()) {
            @Override
            protected void convert(BaseAdapterHelper helper, BillboardMessage item) {
                helper.setImageResource(R.id.view_billboard_img, item.getMessageImageRes());
                helper.setText(R.id.view_billboard_content, item.getMessageContent());
            }
        };
        mListView.setAdapter(adapter);
    }

    //handler对象
    private Handler mHandler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case Handler_Key:
                    setScrollData();
                    //延时发送
                    mHandler.sendEmptyMessageDelayed(Handler_Key, Scroll_Time);
                    break;
            }
            return false;
        }
    });

    /////////////////////////////public method//////////////////////////////


    /**
     * 设置滚动数据
     *
     * @param scrollDataList 数据源
     */
    public BillboardView setScrollDataList(List<BillboardMessage> scrollDataList) {
        if (null != scrollDataList && scrollDataList.size() < Scroll_Count) {
            LogUtils.e("滚动数据源错误");
            scrollDataEnable = false;
            isStart = false;
            return this;
        }
        scrollDataEnable = true;
        this.scrollDataList = scrollDataList;
        return this;
    }

    /**
     * 开始滚动view
     */
    public void startScrollView() {
        if (!scrollDataEnable) {
            return;
        }
        if (!isStart) {
            periodCount = 0;
            isStart = true;
            mHandler.sendEmptyMessage(Handler_Key);
        }else {
            LogUtils.w("已经在运行啦..");
        }
    }

    /**
     * 停止滚动
     */
    public void stopScrollView() {
        if (!scrollDataEnable) {
            return;
        }
        if (isStart) {
            periodCount = 0;
            isStart = false;
            adapter.replaceAll(new ArrayList<BillboardMessage>());
            mHandler.removeMessages(Handler_Key);
        }else {
            LogUtils.w("没有运行哦..");
        }
    }
}
