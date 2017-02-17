package com.jiangtao.shuzicai.model.mall;

import android.view.View;

import com.jiangtao.shuzicai.R;
import com.jiangtao.shuzicai.basic.base.BaseActivityWithToolBar;

public class PriceForecastDescriptionActivity extends BaseActivityWithToolBar {

    @Override
    public int setLayoutId() {
        return R.layout.activity_price_forecast_description;
    }

    @Override
    protected void onInitialize() {
        initTitleBar();
    }

    @Override
    public void initPresenter() {

    }


    //初始化title
    private void initTitleBar() {
        //右键
        setLeftImage(R.mipmap.ic_arrow_back_white_24dp, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        setCenterTitle("涨跌预测规则说明");
    }
}
