package com.jiangtao.shuzicai.model.game;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.bartoszlipinski.recyclerviewheader.RecyclerViewHeader;
import com.jiangtao.shuzicai.R;
import com.jiangtao.shuzicai.basic.adpter.base_adapter_helper_recyclerview.BaseAdapterHelper;
import com.jiangtao.shuzicai.basic.adpter.base_adapter_helper_recyclerview.QuickAdapter;
import com.jiangtao.shuzicai.basic.base.BaseActivityWithToolBar;
import com.jiangtao.shuzicai.model.game.entry.ForecastRecord;
import com.jiangtao.shuzicai.model.mall.helper.SpacesItemDecoration;

import java.util.ArrayList;

import butterknife.BindView;

public class GuessMantissaActivity extends BaseActivityWithToolBar
        implements SwipeRefreshLayout.OnRefreshListener{

    @BindView(R.id.mantissaRecyclerView)
    RecyclerView mantissaRecyclerView;
    //
    @BindView(R.id.mantissaHeader)
    RecyclerViewHeader mantissaHeader;
    //
    @BindView(R.id.mantissa_refresh_widget)
    SwipeRefreshLayout mantissaRefreshWidget;
    //适配器
    private QuickAdapter<ForecastRecord> forecastAdapter;

    @Override
    public int setLayoutId() {
        return R.layout.activity_guess_mantissa;
    }


    @Override
    protected void onInitialize() {
        initTitleBar();
        initSwipeRefresh();
        initRecyclerView();
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
        setCenterTitle("尾数预测");
    }


    //初始化swipe
    private void initSwipeRefresh() {
        mantissaRefreshWidget.setColorSchemeResources(
                android.R.color.holo_blue_light,
                android.R.color.holo_green_light,
                android.R.color.holo_red_light,
                android.R.color.holo_orange_light);
        mantissaRefreshWidget.setOnRefreshListener(this);
    }

    //初始化
    private void initRecyclerView() {
        //两列
        mantissaRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1,
                StaggeredGridLayoutManager.VERTICAL));
        //添加头部布局
        mantissaHeader.attachTo(mantissaRecyclerView, true);
        SpacesItemDecoration decoration = new SpacesItemDecoration(2);
        mantissaRecyclerView.addItemDecoration(decoration);

        //adapter初始化
        forecastAdapter = new QuickAdapter<ForecastRecord>(getContext(),
                R.layout.item_forecast_recoder_recyclerview, new ArrayList<ForecastRecord>()) {
            @Override
            protected void convert(BaseAdapterHelper helper, final ForecastRecord item) {
                helper.setText(R.id.forecast_period_count, "第" + item.getPeriodCount() + "期");
                helper.setText(R.id.actualResults, "实际结果:" + item.getPeriodResult());
                if (item.getPeriodValue() == 1) {
                    helper.setText(R.id.forecastResult, "预测结果：看涨");
                } else {
                    helper.setText(R.id.forecastResult, "预测结果：看跌");
                }
            }
        };
        mantissaRecyclerView.setAdapter(forecastAdapter);
    }

    @Override
    public void onRefresh() {

    }
}
