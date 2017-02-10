package com.jiangtao.shuzicai.model.main;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.jiangtao.shuzicai.R;
import com.jiangtao.shuzicai.basic.base.BaseActivityWithToolBar;
import com.jiangtao.shuzicai.basic.manager.ActivityManager;
import com.jiangtao.shuzicai.model.setting.SettingActivity;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;

public class MainActivity extends BaseActivityWithToolBar {

    //记录用户首次点击返回键的时间
    private long firstBackTime = 0;

    //viewpager
    @BindView(R.id.mainViewPager)
    ViewPager mainViewPager;
    //tabs
    @BindView(R.id.mainTabs)
    TabLayout mainTabs;

    //////////////////////////////////////////////////
    @Override
    public int setLayoutId() {
        return R.layout.activity_main;
    }


    @Override
    protected void onInitialize() {
        resetToolView();
        setLeftTitle("数字连连猜");

        MainFragmentPagerAdapter adapter = new MainFragmentPagerAdapter(
                getBaseFragmentManager(), getContext());
        mainViewPager.setAdapter(adapter);
        //监听滑动的页面
        mainViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // position :当前页面，及你点击滑动的页面；positionOffset:当前页面偏移的百分比；positionOffsetPixels:当前页面偏移的像素位置
            }

            @Override
            public void onPageSelected(int position) {
                resetToolView();
                // arg0是当前选中的页面的Position
                if (position == 0) {
                    setLeftTitle("数字连连猜");
                } else if (position == 1) {
                    setLeftTitle("游戏");
                } else if (position == 2) {
                    setLeftTitle("商城");
                } else if (position == 3) {
                    setLeftTitle("个人中心");
                    setRightTitle("设置", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //跳转至设置页面
                            startActivity(new Intent(MainActivity.this, SettingActivity.class));
                        }
                    });
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //state ==1的时表示正在滑动，state==2的时表示滑动完毕了，state==0的时表示什么都没做
            }
        });
        mainTabs.setupWithViewPager(mainViewPager);
        initTabView(adapter);
    }

    @Override
    public void initPresenter() {

    }

    /**
     * 初始化底部tabview
     *
     * @param adapter
     */
    private void initTabView(MainFragmentPagerAdapter adapter) {
        for (int i = 0; i < mainTabs.getTabCount(); i++) {
            TabLayout.Tab tab = mainTabs.getTabAt(i);
            if (tab != null) {
                tab.setCustomView(adapter.getTabView(i));
            }
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                long secondTime = System.currentTimeMillis();
                if (secondTime - firstBackTime > 2000) {
                    showToast(R.string.MainActivity_key_back_text);
                    firstBackTime = secondTime;
                    return true;
                } else {
                    ActivityManager.getAppManager().AppExit(this, true);
                }
                break;
        }
        return super.onKeyUp(keyCode, event);
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
