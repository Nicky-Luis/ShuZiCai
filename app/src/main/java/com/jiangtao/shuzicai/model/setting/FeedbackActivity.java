package com.jiangtao.shuzicai.model.setting;

import android.view.View;

import com.jiangtao.shuzicai.R;
import com.jiangtao.shuzicai.basic.base.BaseActivityWithToolBar;

public class FeedbackActivity extends BaseActivityWithToolBar {


    @Override
    public int setLayoutId() {
        return R.layout.activity_feedback;
    }

    @Override
    protected void onInitialize() {
        initTitleBar();
    }

    @Override
    public void initPresenter() {

    }

    /**
     * 初始化标题栏
     */
    private void initTitleBar() {
        //返回
        setLeftImage(R.mipmap.ic_arrow_back_white_24dp, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        setCenterTitle("意见反馈");
    }
}
