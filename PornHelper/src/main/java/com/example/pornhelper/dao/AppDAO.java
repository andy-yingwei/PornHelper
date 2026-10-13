package com.example.pornhelper.dao;

import android.database.sqlite.SQLiteDatabase;

import org.litepal.LitePal;

import com.example.pornhelper.table.App;
import com.example.pornhelper.helper.DebugLog;

/**
 * App 表 DAO
 * ======================
 * 日志规则：
 * ======================
 * 1. DebugLog 全局开关：DebugLog.enableLog()
 * 2. AppDAO 文件级开关：AppDAO.enableLog(tag)
 * 3. 两者都为 true 才打印
 * 4. 所有日志都走 DebugLog 的方法（d / e / w ...）
 * ======================
 * 完整使用范例：
 * ======================
 * ① 在 Application（仅 DEBUG 环境）开全局总闸：
 *     public class MyApplication extends Application {
 *         @Override
 *         public void onCreate() {
 *             super.onCreate();
 *             if (BuildConfig.DEBUG) {
 *                 DebugLog.enableLog();
 *             }
 *         }
 *     }

 * ② 在需要调试的地方开 AppDAO 日志（可指定 tag）：
 *     AppDAO.enableLog("AppDAO");
 *     AppDAO.enableLog("DB_App");

 * ③ 业务代码里正常调用（不需要关心日志）：
 *     String url = AppDAO.getUrl();
 *     boolean dynamicTheme = AppDAO.getDynamicTheme();
 *     AppDAO.incrementLoginCount();

 * ④ Logcat 输出示例：
 *     D/AppDAO: App数据表  主页地址=Https://example.com

 * ⑤ 异常时输出示例：
 *     E/AppDAO: App数据表  查询主页地址异常

 * ⑥ 不想打日志了：
 *     AppDAO.disableLog();

 * ⑦ 上线 / Release 包（推荐写法）：
 *     if (BuildConfig.DEBUG) {
 *         DebugLog.enableLog();
 *         AppDAO.enableLog("AppDAO");
 *     } else {
 *         DebugLog.disableLog();
 *     }

 * ======================
 * 打印条件（必须同时满足）：
 * ======================
 * - DebugLog.isLogEnabled() == true
 * - AppDAO.enabled == true
 * - 业务方法中调用了 debugLog.d(...) / debugLog.e(...)
 */
public class AppDAO {

    // ==================== 日志控制 ====================

    /** 默认 tag */
    private static final String DEFAULT_TAG = "AppDAO";

    /** DebugLog 实例 */
    private static DebugLog debugLog = new DebugLog(DEFAULT_TAG);

    /** AppDAO 文件级开关，默认关闭 */
    private static boolean enabled = false;

    /**
     * 开启 AppDAO 日志
     * @param tag 自定义 tag；为 null 或空时自动使用 "AppDAO"
     */
    public static void enableLog(String tag) {
        enabled = true;
        debugLog = new DebugLog((tag !=
                null && !tag.trim().isEmpty()) ? tag : DEFAULT_TAG);
    }

    /**
     * 关闭 AppDAO 日志（不影响 DebugLog 全局开关）
     */
    public static void disableLog() {
        enabled = false;
    }

    /**
     * 判断 FavoriteActorDAO 日志是否允许打印
     * 需同时满足：
     * - DebugLog 全局开关
     * - FavoriteActorDAO 文件级开关
     */
    public static boolean isLogEnabled() {
        return enabled && DebugLog.isLogEnabled();
    }

    // ==================== 查询 ====================

    /**
     * 判断 App 表中是否存在至少一条记录
     *
     * @return true = 存在；false = 不存在或查询异常
     * 日志：
     * - 成功：App数据表  存在
     * - 异常：App数据表  不存在（附带异常）
     */
    public static boolean exists() {
        try {
            boolean ok = LitePal.findFirst(App.class) != null;
            if (isLogEnabled()) debugLog.d("App数据表  存在");
            return ok;
        } catch (Exception e) {
            if (isLogEnabled()) debugLog.e("App数据表  不存在", e);
            return false;
        }
    }

    /**
     * 获取 App 配置中的主页地址
     *
     * @return 主页 URL；无数据或异常时返回空字符串
     * 日志：
     * - App数据表  主页地址=xxx
     * - 异常：App数据表  查询主页地址异常
     */
    public static String getUrl() {
        try {
            App app = LitePal.findFirst(App.class);
            String url = app != null ? app.getUrl() : "";
            if (isLogEnabled()) debugLog.d("App数据表  主页地址=" + url);
            return url;
        } catch (Exception e) {
            if (isLogEnabled()) debugLog.e("App数据表  查询主页地址异常", e);
            return "";
        }
    }

    /**
     * 查询是否启用「点击变色」功能
     *
     * @return true = 启用；false = 未启用或异常
     * 日志：
     * - App数据表  是否启用点击变色=true/false
     */
    public static boolean getColorEnabled() {
        try {
            App app = LitePal.findFirst(App.class);
            boolean v = app != null && app.getColorEnabled();
            if (isLogEnabled()) debugLog.d("App数据表  是否启用点击变色=" + v);
            return v;
        } catch (Exception e) {
            if (isLogEnabled()) debugLog.e("App数据表  查询是否启用点击变色异常", e);
            return false;
        }
    }

    /**
     * 获取累计登录次数
     *
     * @return 登录次数（无记录返回 0）
     * 日志：
     * - App数据表  登录次数=x
     */
    public static int getLoginCount() {
        try {
            App app = LitePal.findFirst(App.class);
            int v = app != null ? app.getLoginCount() : 0;
            if (isLogEnabled()) debugLog.d("App数据表  登录次数=" + v);
            return v;
        } catch (Exception e) {
            if (isLogEnabled()) debugLog.e("App数据表  查询登录次数异常", e);
            return 0;
        }
    }

    /**
     * 查询是否启用「动态主题（跟随系统时间切换）」
     *
     * @return true = 启用动态主题
     * 日志：
     * - App数据表  主题颜色是否跟随手机时间自动切换=true/false
     */
    public static boolean getDynamicTheme() {
        try {
            App app = LitePal.findFirst(App.class);
            boolean v = app != null && app.getDynamicTheme();
            if (isLogEnabled()) debugLog.d("App数据表  主题颜色是否跟随手机时间自动切换=" + v);
            return v;
        } catch (Exception e) {
            if (isLogEnabled()) debugLog.e("App数据表  查询主题颜色是否跟随手机时间自动切换异常", e);
            return false;
        }
    }

    // ==================== 插入 ====================

    /**
     * 如果 App 表为空，则插入一条默认配置
     *
     * @param url          主页地址
     * @param colorEnabled 是否点击变色
     * @param loginCount   登录次数
     * @param dynamicTheme 是否动态主题
     * @return true = 插入成功；false = 已存在或异常
     * 日志：
     * - 已存在：App数据表  已存在记录，跳过插入
     * - 成功：App数据表  记录插入成功
     * - 异常：App数据表  记录插入失败异常
     */
    public static boolean insertIfNotExists(String url, boolean colorEnabled,
                                            int loginCount, boolean dynamicTheme) {
        if (exists()) {
            if (isLogEnabled()) debugLog.d("App数据表  已存在记录，跳过插入");
            return false;
        }
        try {
            App app = new App(url, colorEnabled, loginCount, dynamicTheme);
            boolean ok = app.save();
            if (enabled && ok) debugLog.d("App数据表  记录插入成功");
            return ok;
        } catch (Exception e) {
            if (isLogEnabled()) debugLog.e("App数据表  记录插入失败异常", e);
            return false;
        }
    }

    // ==================== 更新 ====================

    /**
     * 更新主页地址
     *
     * @param url 新地址
     * @return true = 更新成功
     * 日志：
     * - App数据表  更新主页地址成功 url=xxx
     * - 异常：App数据表  更新主页地址异常
     */
    public static boolean updateUrl(String url) {
        try {
            App app = LitePal.findFirst(App.class);
            if (app == null) return false;
            app.setUrl(url);
            boolean ok = app.save();
            if (isLogEnabled()) debugLog.d("App数据表  更新主页地址成功  url=" + url);
            return ok;
        } catch (Exception e) {
            if (isLogEnabled()) debugLog.e("App数据表  更新主页地址异常", e);
            return false;
        }
    }

    /**
     * 切换「点击变色」开关（取反）
     *
     * @return true = 更新成功
     * 日志：
     * - App数据表  更新是否启用点击变色成功
     */
    public static boolean updateColorEnabled() {
        try {
            App app = LitePal.findFirst(App.class);
            if (app == null) return false;
            app.setColorEnabled(!app.getColorEnabled());
            boolean ok = app.save();
            if (isLogEnabled()) debugLog.d("App数据表  更新是否启用点击变色成功");
            return ok;
        } catch (Exception e) {
            if (isLogEnabled()) debugLog.e("App数据表  更新是否启用点击变色异常", e);
            return false;
        }
    }

    /**
     * 登录次数 +1（SQLite 原子自增，避免并发问题）
     *
     * @return true = 自增成功；无记录返回 false
     * 日志：
     * - App数据表  登录次数原子自增成功
     * - 异常：App数据表  登录次数原子自增异常
     */
    public static boolean incrementLoginCount() {
        try {
            if (LitePal.findFirst(App.class) == null) return false;
            SQLiteDatabase db = LitePal.getDatabase();
            db.execSQL("UPDATE App SET loginCount = loginCount + 1");
            if (isLogEnabled()) debugLog.d("App数据表  登录次数原子自增成功");
            return true;
        } catch (Exception e) {
            if (isLogEnabled()) debugLog.e("App数据表  登录次数原子自增异常", e);
            return false;
        }
    }

    /**
     * 切换「动态主题」开关（取反）
     *
     * @return true = 更新成功
     * 日志：
     * - App数据表  主题颜色是否跟随手机时间自动切换成功
     */
    public static boolean updateDynamicTheme() {
        try {
            App app = LitePal.findFirst(App.class);
            if (app == null) return false;
            app.setDynamicTheme(!app.getDynamicTheme());
            boolean ok = app.save();
            if (isLogEnabled()) debugLog.d("App数据表  主题颜色是否跟随手机时间自动切换成功");
            return ok;
        } catch (Exception e) {
            if (isLogEnabled()) debugLog.e("App数据表  主题颜色是否跟随手机时间自动切换异常", e);
            return false;
        }
    }
}

