package com.jiangtao.shuzicai.basic.base;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiangtao.shuzicai.R;
import com.jiangtao.shuzicai.model.main.MainActivity;


public abstract class BaseActivityWithToolBar extends BaseActivity {
    //toolbar
    private Toolbar mToolbar;

    @Override
    protected void loadLayout(View view) {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            setLeftTitle(getResources().getString(R.string.app_name), null);
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        getSupportActionBar().setTitle(title);
        super.setTitle(title);
    }

    /**
     * 重置
     */
    public void resetToolView() {
        TextView leftTitle = (TextView) mToolbar.findViewById(R.id.toolBarLeftTxt);
        ImageView leftImage = (ImageView) mToolbar.findViewById(R.id.toolBarLeftImg);
        TextView rightTitle = (TextView) mToolbar.findViewById(R.id.toolBarRightTxt);
        ImageView rightImage = (ImageView) mToolbar.findViewById(R.id.toolBarRightImg);
        TextView title = (TextView) mToolbar.findViewById(R.id.toolBarCenterTxt);

        leftTitle.setText("");
        title.setText("");
        rightTitle.setText("");

        leftTitle.setVisibility(View.GONE);
        leftImage.setVisibility(View.GONE);
        rightTitle.setVisibility(View.GONE);
        rightImage.setVisibility(View.GONE);
        title.setVisibility(View.GONE);
    }

    /**
     * 左边标题
     *
     * @param content
     */
    public void setLeftTitle(String content) {
        TextView leftTitle = (TextView) mToolbar.findViewById(R.id.toolBarLeftTxt);
        leftTitle.setVisibility(View.VISIBLE);
        leftTitle.setText(content);
        ImageView leftImage = (ImageView) mToolbar.findViewById(R.id.toolBarLeftImg);
        leftImage.setVisibility(View.GONE);
    }

    /**
     * 左边标题
     *
     * @param content
     */
    public void setLeftTitle(String content, View.OnClickListener listner) {
        TextView leftTitle = (TextView) mToolbar.findViewById(R.id.toolBarLeftTxt);
        leftTitle.setVisibility(View.VISIBLE);
        leftTitle.setText(content);
        if (null != listner) {
            leftTitle.setOnClickListener(listner);
        }
        ImageView leftImage = (ImageView) mToolbar.findViewById(R.id.toolBarLeftImg);
        leftImage.setVisibility(View.GONE);
    }

    /**
     * 左边图标
     *
     * @param res
     */
    public void setLeftImage(int res, View.OnClickListener listner) {
        ImageView leftImage = (ImageView) mToolbar.findViewById(R.id.toolBarLeftImg);
        leftImage.setVisibility(View.VISIBLE);
        leftImage.setImageResource(res);
        if (null != listner) {
            leftImage.setOnClickListener(listner);
        }
        TextView leftTitle = (TextView) mToolbar.findViewById(R.id.toolBarLeftTxt);
        leftTitle.setVisibility(View.GONE);
    }

    /**
     * 左边标题
     *
     * @param content
     */
    public void setRightTitle(String content, View.OnClickListener listner) {
        TextView rightTitle = (TextView) mToolbar.findViewById(R.id.toolBarRightTxt);
        rightTitle.setVisibility(View.VISIBLE);
        rightTitle.setText(content);
        if (null != listner) {
            rightTitle.setOnClickListener(listner);
        }
        ImageView rightImage = (ImageView) mToolbar.findViewById(R.id.toolBarRightImg);
        rightImage.setVisibility(View.GONE);
    }

    /**
     * 左边图标
     *
     * @param res
     */
    public void setRightImage(int res, View.OnClickListener listner) {
        ImageView rightImage = (ImageView) mToolbar.findViewById(R.id.toolBarRightImg);
        rightImage.setVisibility(View.VISIBLE);
        rightImage.setImageResource(res);
        if (null != listner) {
            rightImage.setOnClickListener(listner);
        }
        TextView rightTitle = (TextView) mToolbar.findViewById(R.id.toolBarRightTxt);
        rightTitle.setVisibility(View.GONE);
    }

    /**
     * 标题
     *
     * @param content
     */
    public void setCenterTitle(String content) {
        TextView title = (TextView) mToolbar.findViewById(R.id.toolBarCenterTxt);
        title.setVisibility(View.VISIBLE);
        title.setText(content);
    }


    //toolbar的菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 為了讓 Toolbar 的 Menu 有作用，這邊的程式不可以拿掉
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * 跳转到首页
     */
    public void gotoMainPage() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    /**
     * 获取titlebar
     */
    public Toolbar getToolBar() {
        return mToolbar;
    }

}
