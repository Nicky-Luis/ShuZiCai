package com.jiangtao.shuzicai.model.main;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiangtao.shuzicai.R;
import com.jiangtao.shuzicai.basic.base.BaseFragment;


/**
 * Created by Nicky on 2016/11/12.
 * 主页的fragment manager
 */

public class MainFragmentPagerAdapter extends FragmentPagerAdapter {
    //main tab
    private String[] titles;
    //标头
    private int[] titleImages;
    //上下文
    private Context context;
    //fragmentManager
    private FragmentManager fragmentManager;
    //各个fragment
    private BaseFragment[] mContentFragments;

    public MainFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.fragmentManager = fm;
        this.context = context;
        //获取title资源
        titles = context.getResources().getStringArray(R.array.main_tab_titles);
        //获取图片资源
        getResId();
        if (titles.length > 0) {
            mContentFragments = new BaseFragment[titles.length];
        }
    }

    @Override
    public Fragment getItem(int position) {
        if (null == mContentFragments[position]) {
            mContentFragments[position] = BlankFragment.newInstance(position + 1);
        }
        return mContentFragments[position];
    }

    @Override
    public int getCount() {
        return titles.length;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    /**
     * 获取图片tab资源
     */
    private void getResId() {
        TypedArray ar = context.getResources().obtainTypedArray(R.array.main_tab_image);
        int len = ar.length();
        titleImages = new int[len];
        for (int i = 0; i < len; i++) {
            titleImages[i] = ar.getResourceId(i, 0);
        }
        ar.recycle();
    }

    /**
     * @param position
     * @return
     */
    public View getTabView(int position) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_main_tabs, null);
        TextView tv = (TextView) v.findViewById(R.id.tab_main_txt);
        tv.setText(titles[position]);
        //设置图片
        ImageView img = (ImageView) v.findViewById(R.id.tab_main_img);
        img.setImageResource(titleImages[position]);
        return v;
    }
}
