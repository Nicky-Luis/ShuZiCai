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
import com.jiangtao.shuzicai.model.game.GameFragment;
import com.jiangtao.shuzicai.model.home.HomeFragment;
import com.jiangtao.shuzicai.model.mall.MallFragment;
import com.jiangtao.shuzicai.model.user.PersonFragment;

import java.lang.reflect.Method;


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
    //各个fragment 类
    private Class[] clas = {HomeFragment.class, GameFragment.class, MallFragment.class, PersonFragment.class};
    //工厂类
    private FragmentFactory fragmentFactory;
    //各个fragment
    private BaseFragment[] mContentFragments;

    /***
     * 构造
     *
     * @param fm
     * @param context
     */
    public MainFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.fragmentManager = fm;
        this.context = context;
        this.fragmentFactory = new FragmentFactory();
        //获取title资源
        this.titles = context.getResources().getStringArray(R.array.main_tab_titles);
        //获取图片资源
        getResId();
        if (titles.length > 0) {
            mContentFragments = new BaseFragment[titles.length];
        }
    }

    @Override
    public Fragment getItem(int position) {
        if (null == mContentFragments[position]) {
            mContentFragments[position] = fragmentFactory.createProduct(clas[position], position + 1);
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

    ///////////////////////////////////////////////
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

    /////////////////////////////////////////////////////////////////////
    // 抽象的工厂类，定义了其子类必须实现的createProduct()方法
    private abstract class Factory {
        //运用了Java 中的泛型和反射技术
        public abstract <T extends BaseFragment> T createProduct(Class<T> c, int args);
    }

    /**
     * fragment 工厂类
     */
    private class FragmentFactory extends Factory {
        public <T extends BaseFragment> T createProduct(Class<T> c, int args) {
            T product = null;
            try {
                //product = (T) Class.forName(c.getName()).newInstance();
                //反射获取
                Class<?> threadClazz = Class.forName(c.getName());
                Method method = threadClazz.getMethod("newInstance", int.class);
                product = (T) method.invoke(null, args);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return product;
        }
    }
}
