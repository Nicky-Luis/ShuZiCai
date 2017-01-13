package com.jiangtao.shuzicai.model.main;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;

import com.jiangtao.shuzicai.R;
import com.jiangtao.shuzicai.basic.base.BaseActivityWithToolBar;
import com.jiangtao.shuzicai.basic.manager.ActivityManager;

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
        MainFragmentPagerAdapter adapter = new MainFragmentPagerAdapter(
                getBaseFragmentManager(), getContext());
        mainViewPager.setAdapter(adapter);
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
}
