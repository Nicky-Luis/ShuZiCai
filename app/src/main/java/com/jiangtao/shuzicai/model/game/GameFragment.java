package com.jiangtao.shuzicai.model.game;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.utils.LogUtils;
import com.jiangtao.shuzicai.R;
import com.jiangtao.shuzicai.basic.base.BaseFragment;
import com.jiangtao.shuzicai.common.view.billboard_view.BillboardView;
import com.jiangtao.shuzicai.common.view.billboard_view.model.BillboardMessage;
import com.jiangtao.shuzicai.model.game.entry.GameInfo;
import com.jiangtao.shuzicai.model.game.interfaces.IGamePresenter;
import com.jiangtao.shuzicai.model.game.presenter.GamePresenter;
import com.jiangtao.shuzicai.model.game.view.IGameView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

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
        getPeriodsCount();
    }

    /***
     * 获取期数
     */
    private void getPeriodsCount() {
        BmobQuery<GameInfo> query = new BmobQuery<GameInfo>();
        query.findObjects(new FindListener<GameInfo>() {
            @Override
            public void done(List<GameInfo> list, BmobException e) {
                if (e == null) {
                    if (list != null && list.size() > 0) {
                        for (GameInfo info : list) {
                            if (GameInfo.type_quanshu == info.getGameType()) {
                                guess_title_quanshu_txt.setText("第" + info.getNewestNum() + "期");
                            } else if (GameInfo.type_weishu == info.getGameType()) {
                                guess_title_weishu_txt.setText("第" + info.getNewestNum() + "期");
                            } else if (GameInfo.type_zhangdie == info.getGameType()) {
                                guess_title_zhangdie_txt.setText("第" + info.getNewestNum() + "期");
                            }
                        }
                    }
                }
            }
        });
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

    @Override
    public void bindBillboardData(List<BillboardMessage> datas) {
        LogUtils.i("---------绑定公告数据：" + datas.size());
        //广播数据
        billboardMessages = datas;
        //绑定数据
        gameBillboardView.setScrollDataList(datas).startScrollView();
    }
}