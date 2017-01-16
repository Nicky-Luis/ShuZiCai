package com.jiangtao.shuzicai.model.main;

import android.os.Bundle;
import android.view.View;

import com.jiangtao.shuzicai.R;
import com.jiangtao.shuzicai.basic.base.BaseFragment;


public class BlankFragment extends BaseFragment {
    //参数
    public static final String ARGS_PAGE = "args_page";
    //页数
    private int mPage;
    //binding对象

    //对象实例化
    public static BlankFragment newInstance(int page) {
        Bundle args = new Bundle();

        args.putInt(ARGS_PAGE, page);
        BlankFragment fragment = new BlankFragment();
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
        return R.layout.fragment_blank;
    }

    @Override
    public void loadLayout(View rootView) {
       // textView.setText("第" + mPage + "页");
    }


}