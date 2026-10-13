package com.example.pornhelper.dao;

import org.litepal.LitePal;

import com.example.pornhelper.table.FavoriteStudio;
import com.example.pornhelper.helper.DebugLog;

import java.util.List;
import java.util.ArrayList;

/**
 * FavoriteStudio 表 DAO
 * ======================
 * 日志规则：
 * ======================
 * 1. DebugLog 全局开关：DebugLog.enableLog()
 * 2. FavoriteStudioDAO 文件级开关：FavoriteStudioDAO.enableLog(tag)
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

 * ② 在需要调试的地方开 FavoriteStudioDAO 日志（可指定 tag）：
 *     FavoriteStudioDAO.enableLog("FavoriteStudioDAO");
 *     FavoriteStudioDAO.enableLog("DB_Studio");

 * ③ 业务代码里正常调用（不需要关心日志）：
 *     boolean exists = FavoriteStudioDAO.existsByName("片商A");
 *     List<FavoriteStudio> list = FavoriteStudioDAO.findAll();
 *     FavoriteStudioDAO.insertIfNameNotExists(studio);

 * ④ Logcat 输出示例：
 *     D/FavoriteStudioDAO: FavoriteStudio数据表  查询全部数量=3

 * ⑤ 异常时输出示例：
 *     E/FavoriteStudioDAO: FavoriteStudio数据表  查询所有记录异常

 * ⑥ 不想打日志了：
 *     FavoriteStudioDAO.disableLog();

 * ⑦ 上线 / Release 包（推荐写法）：
 *     if (BuildConfig.DEBUG) {
 *         DebugLog.enableLog();
 *         FavoriteStudioDAO.enableLog("FavoriteStudioDAO");
 *     } else {
 *         DebugLog.disableLog();
 *     }

 * ======================
 * 打印条件（必须同时满足）：
 * ======================
 * - DebugLog.isLogEnabled() == true
 * - FavoriteStudioDAO.enabled == true
 * - 业务方法中调用了 debugLog.d(...) / debugLog.e(...)

 * ======================
 * 返回约定：
 * ======================
 * boolean -> true=命中/成功, false=未命中/已存在/失败/异常
 * List    -> 查到的结果列表；异常或查不到返回空 ArrayList（不返回 null）
 * int     -> 实际删除行数；异常返回 0
 */
public class FavoriteStudioDAO {

    // ==================== 日志控制 ====================

    /** 默认 tag */
    private static final String DEFAULT_TAG = "FavoriteStudioDAO";

    /** DebugLog 实例 */
    private static DebugLog debugLog = new DebugLog(DEFAULT_TAG);

    /** FavoriteStudioDAO 文件级开关，默认关闭 */
    private static boolean enabled = false;

    /**
     * 开启 FavoriteStudioDAO 日志
     * @param tag 自定义 tag；为 null 或空时自动使用 "FavoriteStudioDAO"
     */
    public static void enableLog(String tag) {
        enabled = true;
        debugLog = new DebugLog(
                (tag != null && !tag.trim().isEmpty()) ? tag : DEFAULT_TAG
        );
    }

    /**
     * 关闭 FavoriteStudioDAO 日志（不影响 DebugLog 全局开关）
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

    // ==================== 是否存在 ====================

    /**
     * 是否存在指定 url
     * @param url 片商主页地址
     * @return true=存在, false=不存在 / 参数为空 / 异常
     * 日志：
     * - FavoriteStudio数据表  url存在 url=xxx
     * - FavoriteStudio数据表  url不存在 url=xxx
     * - 异常：FavoriteStudio数据表  按url判断存在性异常
     */
    public static boolean existsByUrl(String url) {
        try {
            FavoriteStudio s = LitePal.where("url = ?", url).findFirst(FavoriteStudio.class);
            if (s != null) {
                if (isLogEnabled()) debugLog.d("FavoriteStudio数据表  url存在 url=" + url);
                return true;
            }
            if (isLogEnabled()) debugLog.d("FavoriteStudio数据表  url不存在 url=" + url);
            return false;
        } catch (Exception e) {
            if (isLogEnabled()) debugLog.e("FavoriteStudio数据表  按url判断存在性异常", e);
            return false;
        }
    }

    /**
     * 是否存在指定姓名（片商名称）
     * @param name 片商名称
     * @return true=存在, false=不存在 / 参数为空 / 异常
     * 日志：
     * - FavoriteStudio数据表  姓名存在 name=xxx
     * - FavoriteStudio数据表  姓名不存在 name=xxx
     * - 异常：FavoriteStudio数据表  按姓名判断存在性异常
     */
    public static boolean existsByName(String name) {
        try {
            FavoriteStudio s = LitePal.where("name = ?", name).findFirst(FavoriteStudio.class);
            if (s != null) {
                if (isLogEnabled()) debugLog.d("FavoriteStudio数据表  姓名存在 name=" + name);
                return true;
            }
            if (isLogEnabled()) debugLog.d("FavoriteStudio数据表  姓名不存在 name=" + name);
            return false;
        } catch (Exception e) {
            if (isLogEnabled()) debugLog.e("FavoriteStudio数据表  按姓名判断存在性异常", e);
            return false;
        }
    }

    // ==================== 查询所有 ====================

    /**
     * 查询所有收藏片商
     * @return 全部记录列表；空表或异常返回空 ArrayList（绝不返回 null）
     * 日志：
     * - FavoriteStudio数据表  查询全部数量=x
     * - 异常：FavoriteStudio数据表  查询所有记录异常
     */
    public static List<FavoriteStudio> findAll() {
        try {
            List<FavoriteStudio> list = LitePal.findAll(FavoriteStudio.class);
            if (isLogEnabled()) {
                debugLog.d("FavoriteStudio数据表  查询全部数量="
                        + (list != null ? list.size() : 0));
            }
            return list != null ? list : new ArrayList<>();
        } catch (Exception e) {
            if (isLogEnabled()) debugLog.e("FavoriteStudio数据表  查询所有记录异常", e);
            return new ArrayList<>();
        }
    }

    // ==================== 模糊查询 ====================

    /**
     * 按片商名称模糊查询
     * @param keyword 名称关键字（前后拼 %）
     * @return 命中列表；无命中或异常返回空 ArrayList
     * 日志：
     * - FavoriteStudio数据表  模糊查询姓名 命中数量=x keyword=xxx
     * - 异常：FavoriteStudio数据表  模糊查询异常
     */
    public static List<FavoriteStudio> findByNameLike(String keyword) {
        try {
            List<FavoriteStudio> list =
                    LitePal.where("name like ?", "%" + keyword + "%").find(FavoriteStudio.class);
            if (isLogEnabled()) {
                debugLog.d("FavoriteStudio数据表  模糊查询姓名 命中数量="
                        + (list != null ? list.size() : 0) + " keyword=" + keyword);
            }
            return list != null ? list : new ArrayList<>();
        } catch (Exception e) {
            if (isLogEnabled()) debugLog.e("FavoriteStudio数据表  模糊查询异常", e);
            return new ArrayList<>();
        }
    }

    // ==================== 插入（存在则跳过） ====================

    /**
     * 插入片商（姓名已存在则跳过）
     * @param studio FavoriteStudio 对象（非 null）
     * @return true=首次插入成功, false=姓名已存在/studio为null/失败/异常
     * 日志：
     * - FavoriteStudio数据表  姓名已存在跳过插入 name=xxx
     * - FavoriteStudio数据表  插入成功 name=xxx
     * - FavoriteStudio数据表  插入失败
     * - 异常：FavoriteStudio数据表  插入(按姓名去重)异常
     */
    public static boolean insertIfNameNotExists(FavoriteStudio studio) {
        if (studio == null) return false;
        if (existsByName(studio.getName())) {
            if (isLogEnabled()) {
                debugLog.d("FavoriteStudio数据表  姓名已存在跳过插入 name=" + studio.getName());
            }
            return false;
        }
        try {
            boolean ok = studio.save();
            if (isLogEnabled()) {
                if (ok) {
                    debugLog.d("FavoriteStudio数据表  插入成功 name=" + studio.getName());
                } else {
                    debugLog.e("FavoriteStudio数据表  插入失败 name=" + studio.getName(), null);
                }
            }
            return ok;
        } catch (Exception e) {
            if (isLogEnabled()) debugLog.e("FavoriteStudio数据表  插入(按姓名去重)异常", e);
            return false;
        }
    }

    /**
     * 插入片商（url 已存在则跳过）
     * @param studio FavoriteStudio 对象（非 null）
     * @return true=首次插入成功, false=url已存在/studio为null/失败/异常
     * 日志：
     * - FavoriteStudio数据表  url已存在跳过插入 url=xxx
     * - FavoriteStudio数据表  插入成功 url=xxx
     * - FavoriteStudio数据表  插入失败
     * - 异常：FavoriteStudio数据表  插入(按url去重)异常
     */
    public static boolean insertIfUrlNotExists(FavoriteStudio studio) {
        if (studio == null) return false;
        if (existsByUrl(studio.getUrl())) {
            if (isLogEnabled()) {
                debugLog.d("FavoriteStudio数据表  url已存在跳过插入 url=" + studio.getUrl());
            }
            return false;
        }
        try {
            boolean ok = studio.save();
            if (isLogEnabled()) {
                if (ok) {
                    debugLog.d("FavoriteStudio数据表  插入成功 url=" + studio.getUrl());
                } else {
                    debugLog.e("FavoriteStudio数据表  插入失败 url=" + studio.getUrl(), null);
                }
            }
            return ok;
        } catch (Exception e) {
            if (isLogEnabled()) debugLog.e("FavoriteStudio数据表  插入(按url去重)异常", e);
            return false;
        }
    }

    // ==================== 删除 ====================

    /**
     * 按姓名删除
     * @param name 片商名称（精确匹配）
     */
    public static void deleteByName(String name) {
        try {
            int rows = LitePal.deleteAll(FavoriteStudio.class, "name = ?", name);
            if (isLogEnabled()) {
                if (rows == 0) {
                    debugLog.e("FavoriteStudio数据表删除失败  未找到name=" + name+"对应记录 " ,null);
                } else {
                    debugLog.d("FavoriteStudio数据表删除成功  按姓名="+name+"删除成功 行数=" + rows);
                }
            }
        } catch (Exception e) {
            if (isLogEnabled()) debugLog.e("FavoriteStudio数据表  按姓名删除异常", e);
        }
    }

    /**
     * 按 url 删除
     * @param url url（精确匹配）
     */
    public static void deleteByUrl(String url) {
        try {
            int rows = LitePal.deleteAll(FavoriteStudio.class, "url = ?", url);
            if (isLogEnabled()) {
                if (rows == 0) {
                    debugLog.e("FavoriteStudio数据表删除失败  未找到url=" + url+"对应记录 " ,null);
                } else {
                    debugLog.d("FavoriteStudio数据表删除成功  按url="+url+"删除成功 行数=" + rows);
                }
            }
        } catch (Exception e) {
            if (isLogEnabled()) debugLog.e("FavoriteStudio数据表  按url删除异常", e);
        }
    }
}