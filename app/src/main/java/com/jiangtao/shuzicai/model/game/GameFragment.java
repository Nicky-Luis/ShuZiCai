package com.jiangtao.shuzicai.model.game;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.utils.ToastUtils;
import com.jiangtao.shuzicai.AppHandlerService;
import com.jiangtao.shuzicai.R;
import com.jiangtao.shuzicai.basic.base.BaseFragment;
import com.jiangtao.shuzicai.common.view.billboard_view.BillboardView;
import com.jiangtao.shuzicai.common.view.billboard_view.model.BillboardMessage;
import com.jiangtao.shuzicai.model.game.interfaces.IGamePresenter;
import com.jiangtao.shuzicai.model.game.presenter.GamePresenter;
import com.jiangtao.shuzicai.model.game.view.IGameView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/***
 * 首页fragment
 */
public class GameFragment extends BaseFragment implements IGameView {

    //参数
    public static final String ARGS_PAGE = "args_page";
    //页数
    private int mPage;
    //binding对象
    List<BillboardMessage> billboardMessages;
    //公告栏
    @BindView(R.id.gameBillboardView)
    BillboardView gameBillboardView;
    //全数
    @BindView(R.id.guess_title_quanshu_txt)
    TextView guess_title_quanshu_txt;
    //尾数
    @BindView(R.id.guess_title_weishu_txt)
    TextView guess_title_weishu_txt;
    //涨跌
    @BindView(R.id.guess_title_zhangdie_txt)
    TextView guess_title_zhangdie_txt;
    //主指数
    @BindView(R.id.gameIndexMainData)
    TextView gameIndexMainData;
    //改变量
    @BindView(R.id.gameIndexChange)
    TextView gameIndexChange;
    //百分比
    @BindView(R.id.gameIndexChangePercent)
    TextView gameIndexChangePercent;
    //presenter
    private IGamePresenter presenter;

    //对象实例化
    public static GameFragment newInstance(int page) {
        Bundle args = new Bundle();

        args.putInt(ARGS_PAGE, page);
        GameFragment fragment = new GameFragment();
        fragment.setArguments(args);
        return fragment;
    }

    //设置点击事件
    @OnClick({R.id.priceForecastLy, R.id.mantissaForecastLy, R.id.wholeForecastLy})
    public void OnClick(View view) {
        switch (view.getId()) {

            case R.id.priceForecastLy: {
                Intent intent = new Intent(getActivity(), GuessForecastActivity.class);
                startActivity(intent);
            }
            break;

            case R.id.mantissaForecastLy: {
                Intent intent = new Intent(getActivity(), GuessMantissaActivity.class);
                startActivity(intent);
            }
            break;

            case R.id.wholeForecastLy: {
                Intent intent = new Intent(getActivity(), GuessWholeActivity.class);
                startActivity(intent);
            }
            break;
        }
    }

    @Override
    public void onGetArgument() {
        mPage = getArguments().getInt(ARGS_PAGE);
    }

    @Override
    public void initPresenter() {
        presenter = new GamePresenter(getContext(), this);
    }

    @Override
    public int setLayoutId() {
        return R.layout.fragment_game;
    }

    @Override
    public void loadLayout(View rootView) {
        // textView.setText("第" + mPage + "页");
        presenter.getBillboardData();
        getData();
    }

    //获取数据


    /**
     * 主线程处理接收到的数据
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEventMainThread(String event) {
        Log.e("event MainThread", "消息： " + event + "  thread: " + Thread.currentThread().getName());
        if (event.equals("11")) {
            ToastUtils.showLongToast("获取数据失败");
        }
    }

    @Override
    public void bindBillboardData(List<BillboardMessage> datas) {
        //广播数据
        billboardMessages = datas;
        //绑定数据
        gameBillboardView.setScrollDataList(datas).startScrollView();
    }

    @Override
    public void getDataFail() {
        EventBus.getDefault().post("11");
    }


    //获取数据
    private void getData() {
        AppHandlerService.getLondonData(new AppHandlerService.DataCallBack() {

            @Override
            public void onGetGoldLondon(float price, String change, String limit) {
                //构造方法的字符格式这里如果小数不足2位,会以0补足.
                DecimalFormat decimalFormat = new DecimalFormat(".00");
                String resultPrice = decimalFormat.format(price);
                gameIndexMainData.setText(resultPrice);
                //变化值
                gameIndexChange.setText(change);
                //涨跌比率
                String changePercent = limit + "%";
                gameIndexChangePercent.setText(changePercent);
            }

            @Override
            public void onGetPeriodsCount(int periodsCount,boolean isTread) {
                guess_title_quanshu_txt.setText("第" + periodsCount + "期");
                guess_title_weishu_txt.setText("第" + periodsCount + "期");
                guess_title_zhangdie_txt.setText("第" + periodsCount + "期");
            }

            @Override
            public void onGetDataFail() {
                ToastUtils.showShortToast("获取数据失败");
            }
        });
    }
}