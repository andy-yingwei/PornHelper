package com.example.pornhelper.dao;

import org.litepal.LitePal;

import com.example.pornhelper.table.FavoriteVideo;
import com.example.pornhelper.helper.DebugLog;

import java.util.List;
import java.util.ArrayList;

/**
 * FavoriteVideo 表 DAO
 * ======================
 * 日志规则：
 * ======================
 * 1. DebugLog 全局开关：DebugLog.enableLog()
 * 2. FavoriteVideoDAO 文件级开关：FavoriteVideoDAO.enableLog(tag)
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

 * ② 在需要调试的地方开 FavoriteVideoDAO 日志（可指定 tag）：
 *     FavoriteVideoDAO.enableLog("FavoriteVideoDAO");
 *     FavoriteVideoDAO.enableLog("DB_FavVideo");

 * ③ 业务代码里正常调用（不需要关心日志）：
 *     boolean exists = FavoriteVideoDAO.existsByTitle("视频A");
 *     List<FavoriteVideo> list = FavoriteVideoDAO.findAll();
 *     FavoriteVideoDAO.insertIfTitleNotExists(video);

 * ④ Logcat 输出示例：
 *     D/FavoriteVideoDAO: FavoriteVideo数据表  查询全部数量=10

 * ⑤ 异常时输出示例：
 *     E/FavoriteVideoDAO: FavoriteVideo数据表  查询所有记录异常

 * ⑥ 不想打日志了：
 *     FavoriteVideoDAO.disableLog();

 * ⑦ 上线 / Release 包（推荐写法）：
 *     if (BuildConfig.DEBUG) {
 *         DebugLog.enableLog();
 *         FavoriteVideoDAO.enableLog("FavoriteVideoDAO");
 *     } else {
 *         DebugLog.disableLog();
 *     }

 * ======================
 * 打印条件（必须同时满足）：
 * ======================
 * - DebugLog.isLogEnabled() == true
 * - FavoriteVideoDAO.enabled == true
 * - 业务方法中调用了 debugLog.d(...) / debugLog.e(...)

 * ======================
 * 返回约定：
 * ======================
 * boolean -> true=命中/成功, false=未命中/已存在/失败/异常
 * List    -> 查到的结果列表；异常或查不到返回空 ArrayList（不返回 null）
 * int     -> 实际删除行数；异常返回 0
 */
public class FavoriteVideoDAO {

    // ==================== 日志控制 ====================

    /** 默认 tag */
    private static final String DEFAULT_TAG = "FavoriteVideoDAO";

    /** DebugLog 实例 */
    private static DebugLog debugLog = new DebugLog(DEFAULT_TAG);

    /** FavoriteVideoDAO 文件级开关，默认关闭 */
    private static boolean enabled = false;

    /**
     * 开启 FavoriteVideoDAO 日志
     * @param tag 自定义 tag；为 null 或空时自动使用 "FavoriteVideoDAO"
     */
    public static void enableLog(String tag) {
        enabled = true;
        debugLog = new DebugLog(
                (tag != null && !tag.trim().isEmpty()) ? tag : DEFAULT_TAG
        );
    }

    /**
     * 关闭 FavoriteVideoDAO 日志（不影响 DebugLog 全局开关）
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
     * @param url 视频地址
     * @return true=存在, false=不存在 / 参数为空 / 异常
     * 日志：
     * - FavoriteVideo数据表  url存在 url=xxx
     * - FavoriteVideo数据表  url不存在 url=xxx
     * - 异常：FavoriteVideo数据表  按url判断存在性异常
     */
    public static boolean existsByUrl(String url) {
        try {
            FavoriteVideo v = LitePal.where("url = ?", url).findFirst(FavoriteVideo.class);
            if (v != null) {
                if (isLogEnabled()) debugLog.d("FavoriteVideo数据表  url存在 url=" + url);
                return true;
            }
            if (isLogEnabled()) debugLog.d("FavoriteVideo数据表  url不存在 url=" + url);
            return false;
        } catch (Exception e) {
            if (isLogEnabled()) debugLog.e("FavoriteVideo数据表  按url判断存在性异常", e);
            return false;
        }
    }

    /**
     * 是否存在指定标题
     * @param title 视频标题
     * @return true=存在, false=不存在 / 参数为空 / 异常
     * 日志：
     * - FavoriteVideo数据表  标题存在 title=xxx
     * - FavoriteVideo数据表  标题不存在 title=xxx
     * - 异常：FavoriteVideo数据表  按标题判断存在性异常
     */
    public static boolean existsByTitle(String title) {
        try {
            FavoriteVideo v = LitePal.where("title = ?", title).findFirst(FavoriteVideo.class);
            if (v != null) {
                if (isLogEnabled()) debugLog.d("FavoriteVideo数据表  标题存在 title=" + title);
                return true;
            }
            if (isLogEnabled()) debugLog.d("FavoriteVideo数据表  标题不存在 title=" + title);
            return false;
        } catch (Exception e) {
            if (isLogEnabled()) debugLog.e("FavoriteVideo数据表  按标题判断存在性异常", e);
            return false;
        }
    }

    // ==================== 查询所有 ====================

    /**
     * 查询所有收藏视频
     * @return 全部记录列表；空表或异常返回空 ArrayList（绝不返回 null）
     * 日志：
     * - FavoriteVideo数据表  查询全部数量=x
     * - 异常：FavoriteVideo数据表  查询所有记录异常
     */
    public static List<FavoriteVideo> findAll() {
        try {
            List<FavoriteVideo> list = LitePal.findAll(FavoriteVideo.class);
            if (isLogEnabled()) {
                debugLog.d("FavoriteVideo数据表  查询全部数量="
                        + (list != null ? list.size() : 0));
            }
            return list != null ? list : new ArrayList<>();
        } catch (Exception e) {
            if (isLogEnabled()) debugLog.e("FavoriteVideo数据表  查询所有记录异常", e);
            return new ArrayList<>();
        }
    }

    // ==================== 模糊查询 ====================

    /**
     * 按标题模糊查询
     * @param keyword 标题关键字（前后拼 %）
     * @return 命中列表；无命中或异常返回空 ArrayList
     * 日志：
     * - FavoriteVideo数据表  模糊查询标题 命中数量=x keyword=xxx
     * - 异常：FavoriteVideo数据表  模糊查询异常
     */
    public static List<FavoriteVideo> findByTitleLike(String keyword) {
        try {
            List<FavoriteVideo> list =
                    LitePal.where("title like ?", "%" + keyword + "%").find(FavoriteVideo.class);
            if (isLogEnabled()) {
                debugLog.d("FavoriteVideo数据表  模糊查询标题 命中数量="
                        + (list != null ? list.size() : 0) + " keyword=" + keyword);
            }
            return list != null ? list : new ArrayList<>();
        } catch (Exception e) {
            if (isLogEnabled()) debugLog.e("FavoriteVideo数据表  模糊查询异常", e);
            return new ArrayList<>();
        }
    }

    // ==================== 插入（存在则跳过） ====================

    /**
     * 插入收藏视频（标题已存在则跳过）
     * @param video FavoriteVideo 对象（非 null）
     * @return true=首次插入成功, false=标题已存在/video为null/失败/异常
     * 日志：
     * - FavoriteVideo数据表  标题已存在跳过插入 title=xxx
     * - FavoriteVideo数据表  插入成功 title=xxx
     * - FavoriteVideo数据表  插入失败
     * - 异常：FavoriteVideo数据表  插入(按标题去重)异常
     */
    public static boolean insertIfTitleNotExists(FavoriteVideo video) {
        if (video == null) return false;
        if (existsByTitle(video.getTitle())) {
            if (isLogEnabled()) {
                debugLog.d("FavoriteVideo数据表  标题已存在跳过插入 title=" + video.getTitle());
            }
            return false;
        }
        try {
            boolean ok = video.save();
            if (isLogEnabled()) {
                if (ok) {
                    debugLog.d("FavoriteVideo数据表  插入成功 title=" + video.getTitle());
                } else {
                    debugLog.e("FavoriteVideo数据表  插入失败 title=" + video.getTitle(), null);
                }
            }
            return ok;
        } catch (Exception e) {
            if (isLogEnabled()) debugLog.e("FavoriteVideo数据表  插入(按标题去重)异常", e);
            return false;
        }
    }

    /**
     * 插入收藏视频（url 已存在则跳过）
     * @param video FavoriteVideo 对象（非 null）
     * @return true=首次插入成功, false=url已存在/video为null/失败/异常
     * 日志：
     * - FavoriteVideo数据表  url已存在跳过插入 url=xxx
     * - FavoriteVideo数据表  插入成功 url=xxx
     * - FavoriteVideo数据表  插入失败
     * - 异常：FavoriteVideo数据表  插入(按url去重)异常
     */
    public static boolean insertIfUrlNotExists(FavoriteVideo video) {
        if (video == null) return false;
        if (existsByUrl(video.getUrl())) {
            if (isLogEnabled()) {
                debugLog.d("FavoriteVideo数据表  url已存在跳过插入 url=" + video.getUrl());
            }
            return false;
        }
        try {
            boolean ok = video.save();
            if (isLogEnabled()) {
                if (ok) {
                    debugLog.d("FavoriteVideo数据表  插入成功 url=" + video.getUrl());
                } else {
                    debugLog.e("FavoriteVideo数据表  插入失败 url=" + video.getUrl(), null);
                }
            }
            return ok;
        } catch (Exception e) {
            if (isLogEnabled()) debugLog.e("FavoriteVideo数据表  插入(按url去重)异常", e);
            return false;
        }
    }

    // ==================== 删除 ====================

    /**
     * 按标题删除
     * @param title 视频标题（精确匹配）
     */
    public static void deleteByTitle(String title) {
        try {
            int rows = LitePal.deleteAll(FavoriteVideo.class, "title = ?", title);
            if (isLogEnabled()) {
                if (rows == 0) {
                    debugLog.e("FavoriteVideo数据表删除失败  未找到title=" + title+"对应记录 " ,null);
                } else {
                    debugLog.d("FavoriteVideo数据表删除成功  按title="+title+"删除成功 行数=" + rows);
                }
            }
        } catch (Exception e) {
            if (isLogEnabled()) debugLog.e("FavoriteVideo数据表  按标题删除异常", e);
        }
    }

    /**
     * 按 url 删除
     * @param url url（精确匹配）
     */
    public static void deleteByUrl(String url) {
        try {
            int rows = LitePal.deleteAll(FavoriteVideo.class, "url = ?", url);
            if (isLogEnabled()) {
                if (rows == 0) {
                    debugLog.e("FavoriteVideo数据表删除失败  未找到url=" + url+"对应记录 " ,null);
                } else {
                    debugLog.d("FavoriteVideo数据表删除成功  按url="+url+"删除成功 行数=" + rows);
                }
            }
        } catch (Exception e) {
            if (isLogEnabled()) debugLog.e("FavoriteVideo数据表  按url删除异常", e);
        }
    }
}