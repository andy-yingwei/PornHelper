package com.example.pornhelper.table;

import org.litepal.crud.LitePalSupport;

/**
 * ****************************
 * 描述：App系统表
 * ****************************
 */
public class App extends LitePalSupport {

    /** 主页地址 */
    private String url;
    /** item是否启用点击变色 */
    private boolean colorEnabled;
    /** 登录次数统计 */
    private int loginCount;
    /** 主题颜色是否跟随手机时间自动切换 */
    private boolean dynamicTheme;

    // ==================== 构造方法 ====================

    /** 无参构造 */
    public App() {}

    /** 有参构造 */
    public App(String url, boolean colorEnabled, int loginCount, boolean dynamicTheme) {
        this.url = url;
        this.colorEnabled = colorEnabled;
        this.loginCount = loginCount;
        this.dynamicTheme = dynamicTheme;
    }

    // ==================== Getter / Setter ====================

    /**
     * 获取网站主页地址
     * @return 网站主页地址
     */
    public String getUrl() {
        return url;
    }

    /**
     * 设置网站主页地址
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * 获取item是否启用点击变色
     * @return item是否启用点击变色
     */
    public boolean getColorEnabled() {
        return colorEnabled;
    }

    /**
     * 设置item是否启用点击变色
     */
    public void setColorEnabled(boolean colorEnabled) {
        this.colorEnabled = colorEnabled;
    }

    /**
     * 获取登录次数统计
     * @return 登录次数统计
     */
    public int getLoginCount() {
        return loginCount;
    }

    /**
     * 设置登录次数统计
     */
    public void setLoginCount(int loginCount) {
        this.loginCount = loginCount;
    }

    /**
     * 获取主题颜色是否跟随手机时间自动切换
     * @return 主题颜色是否跟随手机时间自动切换
     */
    public boolean getDynamicTheme() {
        return dynamicTheme;
    }

    /**
     * 设置主题颜色是否跟随手机时间自动切换
     */
    public void setDynamicTheme(boolean dynamicTheme) {
        this.dynamicTheme = dynamicTheme;
    }

    // ==================== 返回类对象数据 ====================

    /**
     * 返回当前类对象的字段信息字符串
     * @return 字段数据字符串
     */
    public String toLogString() {
        return "App{url='" + url
                + "', colorEnabled=" + colorEnabled
                + ", loginCount=" + loginCount
                + ", dynamicTheme=" + dynamicTheme + "}";
    }

}
