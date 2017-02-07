package com.jiangtao.shuzicai.basic.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;


public abstract class BaseFragment extends Fragment implements IBaseView {
    private BaseActivity mActivity;
    private int viewId;
    private View mRootView;

    /**
     * 初始化布局
     */
    public abstract void onGetArgument();

    public abstract void initPresenter();
    /**
     * 获取layout的id
     *
     * @return
     */
    public abstract int setLayoutId();

    /**
     * loadLayout
     *
     * @param viewDataBinding
     */
    public abstract void loadLayout(View viewDataBinding);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onGetArgument();
        // Register
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewId = setLayoutId();
        if (0 == viewId) {
            new Exception("Please return the layout id in setLayoutId method,as simple as R" +
                    ".layout.cr_news_fragment_layout").printStackTrace();
            return super.onCreateView(inflater, container, savedInstanceState);
        } else {
            if (mRootView == null) {
                mRootView = inflater.inflate(viewId, null);
                ButterKnife.bind(this,mRootView);//绑定framgent
                //检测是否有内存泄露
                // RefWatcher refWatcher = BasicApp.getRefWatcher(getActivity());
                // refWatcher.watch(this);
                initPresenter();
                loadLayout(mRootView);
            }
            ViewGroup parent = (ViewGroup) mRootView.getParent();
            if (parent != null) {
                parent.removeView(mRootView);
            }
            return mRootView;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Unregister
        EventBus.getDefault().unregister(this);
    }

    /**
     * 获取当前Fragment状态
     *
     * @return true为正常 false为未加载或正在删除
     */
    private boolean getStatus() {
        return (isAdded() && !isRemoving());
    }

    /**
     * 获取Activity
     *
     * @return
     */
    public BaseActivity getBaseActivity() {
        if (mActivity == null) {
            mActivity = (BaseActivity) getActivity();
        }
        return mActivity;
    }

    @Override
    public void showProgress(boolean flag, String message) {
        if (getStatus()) {
            BaseActivity activity = getBaseActivity();
            if (activity != null) {
                activity.showProgress(flag, message);
            }
        }
    }

    @Override
    public void showProgress(String message) {
        showProgress(true, message);
    }

    @Override
    public void showProgress() {
        showProgress(true);
    }

    @Override
    public void showProgress(boolean flag) {
        showProgress(flag, "");
    }

    @Override
    public void hideProgress() {
        if (getStatus()) {
            BaseActivity activity = getBaseActivity();
            if (activity != null) {
                activity.hideProgress();
            }
        }
    }

    @Override
    public void showToast(int resId) {
        if (getStatus()) {
            BaseActivity activity = getBaseActivity();
            if (activity != null) {
                activity.showToast(resId);
            }
        }
    }

    @Override
    public void showToast(String msg) {
        if (getStatus()) {
            BaseActivity activity = getBaseActivity();
            if (activity != null) {
                activity.showToast(msg);
            }
        }
    }

    @Override
    public Context getContext() {
        return getActivity();
    }

    @Override
    public void close() {
    }
}
