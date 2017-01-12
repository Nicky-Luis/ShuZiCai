package com.jiangtao.shuzicai.basic.app;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.DisplayMetrics;

import java.util.Locale;

/**
 * 自定义应用入口
 *
 * @author Ht
 */
public abstract class BasicApp extends Application {

    //app对象
    protected static BasicApp mInstance;
    //检测内存泄露
   // private RefWatcher refWatcher;
    // 屏幕宽度
    public static int screenWidth;
    //屏幕高度
    public static int screenHeight;
    // 屏幕密度
    public static float screenDensity;

    public BasicApp() {
    }

    //获取程序的名字
    public abstract String getApplicationName();
    //程序初始化
    public abstract void onApplicationInit();

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        onApplicationInit();
        //检测内存泄露
        //refWatcher = LeakCanary.install(this);
        initScreenSize();
    }

    //获取实例
    public static BasicApp getInstance() {
        return mInstance;
    }

    /**
     * 在自己的Application中添加如下代码
     */
   /* public static RefWatcher getRefWatcher(Context context) {
        return mInstance.refWatcher;
    }*/

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public static String getVersion() {
        try {
            PackageManager manager = mInstance.getPackageManager();
            PackageInfo info = manager.getPackageInfo(mInstance.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获取app的名称
     *
     * @return 名称
     */
    public static String getAppName() {
        return mInstance.getApplicationName();
    }


    /**
     * 获取当前系统语言
     *
     * @return 当前系统语言
     */
    public static String getLanguage() {
        Locale locale = mInstance.getResources().getConfiguration().locale;
        String language = Locale.getDefault().toString();
        return language;
    }

    /**
     * 初始化当前设备屏幕宽高
     */
    private void initScreenSize() {
        DisplayMetrics curMetrics = getApplicationContext().getResources()
                .getDisplayMetrics();
        screenWidth = curMetrics.widthPixels;
        screenHeight = curMetrics.heightPixels;
        screenDensity = curMetrics.density;
    }

}
