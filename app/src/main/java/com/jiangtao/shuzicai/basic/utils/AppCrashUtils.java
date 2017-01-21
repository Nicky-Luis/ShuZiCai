package com.jiangtao.shuzicai.basic.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;

import com.blankj.utilcode.utils.AppUtils;
import com.blankj.utilcode.utils.CloseUtils;
import com.blankj.utilcode.utils.FileUtils;
import com.blankj.utilcode.utils.LogUtils;
import com.blankj.utilcode.utils.Utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Nicky on 2017/1/21.
 * desc  : 崩溃相关工具类
 * </pre>
 */
public class AppCrashUtils implements Thread.UncaughtExceptionHandler {

    private volatile static AppCrashUtils mInstance;

    private Thread.UncaughtExceptionHandler mHandler;
    private boolean mInitialized;
    private String crashDir;
    private String versionName;
    private int versionCode;

    private AppCrashUtils() {
    }

    /**
     * 获取单例
     * <p>在Application中初始化{@code CrashUtils.getInstance().init(this);}</p>
     *
     * @return 单例
     */
    public static AppCrashUtils getInstance() {
        if (mInstance == null) {
            synchronized (AppCrashUtils.class) {
                if (mInstance == null) {
                    mInstance = new AppCrashUtils();
                }
            }
        }
        return mInstance;
    }

    /**
     * 初始化
     *
     * @return {@code true}: 成功<br>{@code false}: 失败
     */
    public boolean init(Context context) {
        if (mInitialized) return true;
        String fileName = AppUtils.getAppName(context) + "/crash";
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            crashDir = Environment.getExternalStorageDirectory() + File.separator + fileName + File.separator;
        } else {
            crashDir = Utils.getContext().getCacheDir().getPath() + File.separator + fileName + File.separator;
        }
        LogUtils.i("crashDir is:" + crashDir);
        try {
            PackageInfo pi = Utils.getContext().getPackageManager().getPackageInfo(Utils.getContext().getPackageName
                    (), 0);
            versionName = pi.versionName;
            versionCode = pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        mHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
        return mInitialized = true;
    }

    @Override
    public void uncaughtException(Thread thread, final Throwable throwable) {
        String now = new SimpleDateFormat("yy-MM-dd", Locale.getDefault()).format(new Date());
        final String fullPath = crashDir + now + ".txt";
        if (!FileUtils.createOrExistsFile(fullPath)) return;
        new Thread(new Runnable() {
            @Override
            public void run() {
                PrintWriter pw = null;
                try {
                    pw = new PrintWriter(new FileWriter(fullPath, true));
                    pw.write(getCrashHead());
                    throwable.printStackTrace(pw);
                    Throwable cause = throwable.getCause();
                    while (cause != null) {
                        cause.printStackTrace(pw);
                        cause = cause.getCause();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    CloseUtils.closeIO(pw);
                }
                System.exit(0);
            }
        }).start();
        if (mHandler != null) {
            mHandler.uncaughtException(thread, throwable);
        }
    }

    /**
     * 获取崩溃头
     *
     * @return 崩溃头
     */
    private String getCrashHead() {
        String now = new SimpleDateFormat("yy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        return "\n\n\n************* Crash Log Head ****************" +
                "\nCrash Time: " + now +// 崩溃时间
                "\nDevice Manufacturer: " + Build.MANUFACTURER +// 设备厂商
                "\nDevice Model       : " + Build.MODEL +// 设备型号
                "\nAndroid Version    : " + Build.VERSION.RELEASE +// 系统版本
                "\nAndroid SDK        : " + Build.VERSION.SDK_INT +// SDK版本
                "\nApp VersionName    : " + versionName +
                "\nApp VersionCode    : " + versionCode +
                "\n************* Crash Log Head ****************\n\n";
    }
}
