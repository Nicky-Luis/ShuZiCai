package com.jiangtao.shuzicai.model.game;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.jiangtao.shuzicai.R;
import com.jiangtao.shuzicai.basic.base.BaseFragment;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/***
 * 首页fragment
 */
public class GameFragment extends BaseFragment {
    //参数
    public static final String ARGS_PAGE = "args_page";
    //页数
    private int mPage;
    //binding对象

    //对象实例化
    public static GameFragment newInstance(int page) {
        Bundle args = new Bundle();

        args.putInt(ARGS_PAGE, page);
        GameFragment fragment = new GameFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onGetArgument() {
        mPage = getArguments().getInt(ARGS_PAGE);
    }

    @Override
    public int setLayoutId() {
        return R.layout.fragment_game;
    }

    @Override
    public void loadLayout(View rootView) {
       // textView.setText("第" + mPage + "页");
    }

    /**
     * 主线程处理接收到的数据
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEventMainThread(String event) {
        Log.e("event MainThread", "消息： " + event + "  thread: " + Thread.currentThread().getName());
    }

}