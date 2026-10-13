package com.example.mypornhelper;

import android.app.Activity;
import android.app.Application;

import com.example.glidehelper.LoadExecutor;
import com.example.pornhelper.helper.DebugLog;

import org.litepal.LitePal;

import java.util.LinkedHashMap;
import java.util.Stack;

/**
 * 当android程序启动时，会自动创建一个Application对象存储系统的一些信息
 * 如果要创建自己Application，需要继承Application，并在AndroidManifest.xml文件application标签中注册增加name属性
 * 例：<application
 *        android:name="com.example.a18mhjingmantiantang.util.MyApplication"
 *    </application>
 */
public class MyApplication extends Application {

    public static Stack<Activity> activityStack;
    public static LinkedHashMap<String, String> cookieMap;

    // Application 实例创建时调用。Android系统的入口是Application类的 onCreate（），默认为空实现
    //作用
    //初始化 应用程序级别 的资源，如全局对象、环境配置变量、图片资源初始化、推送服务的注册等
    //注：请不要执行耗时操作，否则会拖慢应用程序启动速度
    //数据共享、数据缓存 设置全局共享数据，如全局共享变量、方法等
    //注：这些共享数据只在应用程序的生命周期内有效，当该应用程序被杀死，这些数据也会被清空，所以 只能存储一些具备 临时性的共享数据
    @Override
    public void onCreate() {
        super.onCreate();
        activityStack = new Stack<>();
        // 启动时清理 7 天前的 AES 解密缓存文件，防止磁盘无限膨胀
        LoadExecutor.clearExpiredAesCache(this);
        //初始化LitePal
        LitePal.initialize(this);
        // 日志打印总开关
        DebugLog.enableLog();
    }

}

