package com.jiangtao.shuzicai.model.user;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.jiangtao.shuzicai.R;
import com.jiangtao.shuzicai.basic.base.BaseFragment;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

/***
 * 个人中心fragment
 */
public class PersonFragment extends BaseFragment {
    //参数
    public static final String ARGS_PAGE = "args_page";
    //页数
    private int mPage;
    //binding对象
    @BindView(R.id.loginViewLayout)
    LinearLayout loginLayout;

    //设置点击事件
    @OnClick({R.id.loginViewLayout})
    public void OnClick(View view) {
        switch (view.getId()) {

            case R.id.loginViewLayout:
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                break;

        }
    }

    //对象实例化
    public static PersonFragment newInstance(int page) {
        Bundle args = new Bundle();

        args.putInt(ARGS_PAGE, page);
        PersonFragment fragment = new PersonFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onGetArgument() {
        mPage = getArguments().getInt(ARGS_PAGE);
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public int setLayoutId() {
        return R.layout.fragment_person;
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