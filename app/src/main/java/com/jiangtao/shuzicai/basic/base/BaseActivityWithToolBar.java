package com.jiangtao.shuzicai.basic.base;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.jiangtao.shuzicai.model.main.MainActivity;
import com.jiangtao.shuzicai.R;


public abstract class BaseActivityWithToolBar extends BaseActivity {
    //toolbar
    private Toolbar mToolbar;
    //菜单
    private Menu menu;

    @Override
    protected void loadLayout(View view) {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            //在setSupportActionBar(toolbar);之后，不然就报错了
            getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
            // getSupportActionBar().setSubtitle("副标题");
            // getSupportActionBar().setLogo(R.drawable.ic_launcher);
            // 菜单回调
            mToolbar.setOnMenuItemClickListener(onMenuItemClickListener);
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        getSupportActionBar().setTitle(title);
        super.setTitle(title);
    }

    //监听
    private Toolbar.OnMenuItemClickListener onMenuItemClickListener = new Toolbar
            .OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {

                case R.id.action_more:
                    Snackbar.make(mToolbar, "Click More", Snackbar.LENGTH_SHORT).show();
                    break;
            }
            return true;
        }
    };


    //toolbar的菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 為了讓 Toolbar 的 Menu 有作用，這邊的程式不可以拿掉
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        this.menu = menu;
        return super.onPrepareOptionsMenu(menu);
    }


    //获取菜单
    public Menu getMenu() {
        return menu;
    }

    //获取item
    public MenuItem  getMenuItem(int res){
        return menu.findItem(res);
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
