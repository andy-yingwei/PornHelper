package com.example.pornhelper.video;

import android.content.Context;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.pornhelper.R;
import com.example.pornhelper.helper.Colors;

/**
 * ============================================================
 * VideoProfileConfig
 * ============================================================
 * Video 详情页（VideoProfileView）的纯 UI 外观配置类。
 *
 * <p><b>设计原则：</b>
 * <ol>
 *   <li>不支持 XML 自定义属性（无 attrs.xml / TypedArray）</li>
 *   <li>所有样式只能在 Java 中通过 Builder 配置</li>
 *   <li>单位约定：
 *     <ul>
 *       <li>TextSize → sp</li>
 *       <li>Height / Size / Radius → dp</li>
 *     </ul>
 *   </li>
 *   <li>颜色统一使用 {@code @ColorInt}（ARGB int 值）</li>
 *   <li>图片资源使用 {@code @DrawableRes}（R.drawable.xxx）</li>
 *   <li>Config 为不可变对象（final 字段 + Builder）</li>
 *   <li>本类不包含任何网络请求参数（cookies / headers / AES 等）
 *       鉴权参数通过 VideoProfileView 的 setter 注入</li>
 * </ol>
 *
 * <p><b>与 VideoProfileView 的关系：</b>
 * <ul>
 *   <li>VideoProfileConfig 只负责 UI 外观（颜色 / 尺寸 / 图标 / 圆角等）</li>
 *   <li>不包含业务状态（收藏状态、展开状态等由 View 自行管理）</li>
 *   <li>通过 {@code videoProfileView.setConfig(config)} 应用配置</li>
 * </ul>
 *
 * <p><b>完整示例：</b>
 * <pre>
 * VideoProfileConfig config =
 *         new VideoProfileConfig.Builder()
 *
 *         // 容器
 *         .background(Colors.of(ctx, R.color.black))
 *         .backgroundRadius(0)
 *
 *         // 封面
 *         .coverHeight(240)
 *         .coverRadius(0)
 *         .coverPlaceholder(R.drawable.placeholder_video)
 *         .coverError(R.drawable.placeholder_video)
 *         .skipMemoryCache(true)
 *         .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
 *
 *         // 标题
 *         .titleTextSize(16)
 *         .titleTextColor(Colors.of(ctx, R.color.white))
 *
 *         // 箭头
 *         .arrowSize(24)
 *         .arrowCollapsed(R.drawable.ic_arrow_down)
 *         .arrowExpanded(R.drawable.ic_arrow_up)
 *         .arrowColor(Colors.of(ctx, R.color.white))
 *
 *         // 收藏 / 下载按钮
 *         .iconButtonSize(35)
 *         .favoriteIconUnselected(R.drawable.ic_star)
 *         .favoriteIconSelected(R.drawable.ic_star_fill)
 *         .favoriteIconColor(Colors.of(ctx, R.color.red))
 *         .downloadIcon(R.drawable.ic_download)
 *         .downloadIconColor(Colors.of(ctx, R.color.blue))
 *
 *         // 简介
 *         .biographyTextSize(12)
 *         .biographyTextColor(Colors.of(ctx, R.color.white))
 *
 *         .build();
 *
 * videoProfileView.setConfig(config);
 * </pre>
 *
 * <p><b>基于已有配置创建变体：</b>
 * <pre>
 * VideoProfileConfig newConfig =
 *         new VideoProfileConfig.Builder()
 *         .copyFrom(existingConfig)
 *         .coverHeight(300)
 *         .favoriteIconColor(0xFFFF4081)
 *         .build();
 * </pre>
 */
public final class VideoProfileConfig {

    /* =========================================================
     * 容器
     * ========================================================= */
    /** 整个 View 的背景色（@ColorInt） */
    @ColorInt
    public final int backgroundColor;

    /** 整个 View 的圆角半径，单位 dp；0 = 不圆角 */
    public final int backgroundRadius;

    /* =========================================================
     * 封面
     * ========================================================= */
    /** 封面高度，单位 dp（宽度跟随父布局 match_parent） */
    public final int coverHeight;

    /** 封面圆角半径，单位 dp；0 = 不圆角 */
    public final int coverRadius;

    /** 封面加载前的占位图（R.drawable.xxx） */
    @DrawableRes
    public final int coverPlaceholder;

    /** 封面加载失败后的兜底图（R.drawable.xxx） */
    @DrawableRes
    public final int coverError;

    /** 是否跳过内存缓存（Glide: skipMemoryCache） */
    public final boolean skipMemoryCache;

    /** Glide 磁盘缓存策略 */
    public final DiskCacheStrategy diskCacheStrategy;

    /* =========================================================
     * 标题
     * ========================================================= */
    /** 视频标题字号，单位 sp */
    public final int titleTextSize;

    /** 视频标题颜色（@ColorInt） */
    @ColorInt
    public final int titleTextColor;

    /* =========================================================
     * 箭头
     * ========================================================= */
    /** 箭头图标大小，单位 dp（宽高一致） */
    public final int arrowSize;

    /** 折叠状态箭头（直接换图，不旋转） */
    @DrawableRes
    public final int arrowCollapsed;

    /** 展开状态箭头（直接换图，不旋转） */
    @DrawableRes
    public final int arrowExpanded;

    /** 箭头 tint 颜色（vector / 单色图有效） */
    @ColorInt
    public final int arrowColor;

    /* =========================================================
     * 收藏 / 下载 图标按钮
     * ========================================================= */
    /** 收藏 / 下载按钮图标大小，单位 dp（按钮本身 = 图标 + 内边距） */
    public final int iconButtonSize;

    /** 收藏按钮：未选中图标（R.drawable.xxx） */
    @DrawableRes
    public final int favoriteIconUnselected;

    /** 收藏按钮：选中图标（R.drawable.xxx） */
    @DrawableRes
    public final int favoriteIconSelected;

    /** 收藏按钮图标颜色（@ColorInt） */
    @ColorInt
    public final int favoriteIconColor;

    /** 下载按钮图标（选中 / 未选中共用） */
    @DrawableRes
    public final int downloadIcon;

    /** 下载按钮图标颜色（@ColorInt） */
    @ColorInt
    public final int downloadIconColor;

    /* =========================================================
     * 简介
     * ========================================================= */
    /** 简介文字字号，单位 sp */
    public final int biographyTextSize;

    /** 简介文字颜色（@ColorInt） */
    @ColorInt
    public final int biographyTextColor;

    private VideoProfileConfig(Builder b) {
        this.backgroundColor = b.backgroundColor;
        this.backgroundRadius = b.backgroundRadius;

        this.coverHeight = b.coverHeight;
        this.coverRadius = b.coverRadius;
        this.coverPlaceholder = b.coverPlaceholder;
        this.coverError = b.coverError;
        this.skipMemoryCache = b.skipMemoryCache;
        this.diskCacheStrategy = b.diskCacheStrategy;

        this.titleTextSize = b.titleTextSize;
        this.titleTextColor = b.titleTextColor;

        this.arrowSize = b.arrowSize;
        this.arrowCollapsed = b.arrowCollapsed;
        this.arrowExpanded = b.arrowExpanded;
        this.arrowColor = b.arrowColor;

        this.iconButtonSize = b.iconButtonSize;
        this.favoriteIconUnselected = b.favoriteIconUnselected;
        this.favoriteIconSelected = b.favoriteIconSelected;
        this.favoriteIconColor = b.favoriteIconColor;
        this.downloadIcon = b.downloadIcon;
        this.downloadIconColor = b.downloadIconColor;

        this.biographyTextSize = b.biographyTextSize;
        this.biographyTextColor = b.biographyTextColor;

    }

    /* =========================================================
     * 默认配置（暗色主题：black，cookies / headers = null）
     * ========================================================= */
    public static VideoProfileConfig defaultConfig(Context ctx) {
        return new Builder()
                .background(Colors.of(ctx, R.color.black))
                .backgroundRadius(0)

                .coverHeight(240)
                .coverRadius(0)
                .coverPlaceholder(R.drawable.placeholder_video)
                .coverError(R.drawable.placeholder_video)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)

                .titleTextSize(16)
                .titleTextColor(Colors.of(ctx, R.color.white))

                .arrowSize(24)
                .arrowCollapsed(R.drawable.ic_arrow_down)
                .arrowExpanded(R.drawable.ic_arrow_up)
                .arrowColor(Colors.of(ctx, R.color.white))

                .iconButtonSize(35)
                .favoriteIconUnselected(R.drawable.ic_star)
                .favoriteIconSelected(R.drawable.ic_star_fill)
                .favoriteIconColor(Colors.of(ctx, R.color.red))
                .downloadIcon(R.drawable.ic_download)
                .downloadIconColor(Colors.of(ctx, R.color.blue))

                .biographyTextSize(12)
                .biographyTextColor(Colors.of(ctx, R.color.white))

                // cookies / headers 默认 null
                .build();
    }

    /* =========================================================
     * Builder
     * ========================================================= */
    public static final class Builder {

        /* 容器 */
        private int backgroundColor = 0xFF000000;
        private int backgroundRadius = 0;    // dp，0 = 不圆角

        /* 封面 */
        private int coverHeight = 240;
        private int coverRadius = 0;
        private int coverPlaceholder = R.drawable.placeholder_video;
        private int coverError = R.drawable.placeholder_video;
        private boolean skipMemoryCache = true;
        private DiskCacheStrategy diskCacheStrategy = DiskCacheStrategy.AUTOMATIC;

        /* 标题 */
        private int titleTextSize = 16;
        private int titleTextColor = 0xFFFFFFFF;

        /* 箭头 */
        private int arrowSize = 24;
        private int arrowCollapsed = R.drawable.ic_arrow_down;
        private int arrowExpanded = R.drawable.ic_arrow_up;
        private int arrowColor = 0xFFFFFFFF;

        /* 收藏 / 下载 */
        private int iconButtonSize = 35;
        private int favoriteIconUnselected = R.drawable.ic_star;
        private int favoriteIconSelected = R.drawable.ic_star_fill;
        private int favoriteIconColor = 0xFFFF0000;
        private int downloadIcon = R.drawable.ic_download;
        private int downloadIconColor = 0xFF0000FF;

        /* 简介 */
        private int biographyTextSize = 12;
        private int biographyTextColor = 0xFFFFFFFF;


        /* -------- 容器 -------- */
        public Builder background(@ColorInt int c) {
            this.backgroundColor = c;
            return this;
        }

        public Builder backgroundRadius(int dp) {
            this.backgroundRadius = dp;
            return this;
        }

        /* -------- 封面 -------- */
        public Builder coverHeight(int dp) {
            this.coverHeight = dp;
            return this;
        }

        public Builder coverRadius(int dp) {
            this.coverRadius = dp;
            return this;
        }

        public Builder coverPlaceholder(@DrawableRes int res) {
            this.coverPlaceholder = res;
            return this;
        }

        public Builder coverError(@DrawableRes int res) {
            this.coverError = res;
            return this;
        }

        public Builder skipMemoryCache(boolean v) {
            this.skipMemoryCache = v;
            return this;
        }

        public Builder diskCacheStrategy(DiskCacheStrategy strategy) {
            this.diskCacheStrategy = strategy;
            return this;
        }

        /* -------- 标题 -------- */
        public Builder titleTextSize(int sp) {
            this.titleTextSize = sp;
            return this;
        }

        public Builder titleTextColor(@ColorInt int c) {
            this.titleTextColor = c;
            return this;
        }

        /* -------- 箭头 -------- */
        public Builder arrowSize(int dp) {
            this.arrowSize = dp;
            return this;
        }

        public Builder arrowCollapsed(@DrawableRes int res) {
            this.arrowCollapsed = res;
            return this;
        }

        public Builder arrowExpanded(@DrawableRes int res) {
            this.arrowExpanded = res;
            return this;
        }

        public Builder arrowColor(@ColorInt int c) {
            this.arrowColor = c;
            return this;
        }

        /* -------- 收藏 / 下载 -------- */
        public Builder iconButtonSize(int dp) {
            this.iconButtonSize = dp;
            return this;
        }

        public Builder favoriteIconUnselected(@DrawableRes int res) {
            this.favoriteIconUnselected = res;
            return this;
        }

        public Builder favoriteIconSelected(@DrawableRes int res) {
            this.favoriteIconSelected = res;
            return this;
        }

        public Builder favoriteIconColor(@ColorInt int c) {
            this.favoriteIconColor = c;
            return this;
        }

        public Builder downloadIcon(@DrawableRes int res) {
            this.downloadIcon = res;
            return this;
        }

        public Builder downloadIconColor(@ColorInt int c) {
            this.downloadIconColor = c;
            return this;
        }

        /* -------- 简介 -------- */
        public Builder biographyTextSize(int sp) {
            this.biographyTextSize = sp;
            return this;
        }

        public Builder biographyTextColor(@ColorInt int c) {
            this.biographyTextColor = c;
            return this;
        }

        /* -------- 拷贝 -------- */
        public Builder copyFrom(VideoProfileConfig old) {
            if (old == null) return this;
            this.backgroundColor = old.backgroundColor;
            this.backgroundRadius = old.backgroundRadius;
            this.coverHeight = old.coverHeight;
            this.coverRadius = old.coverRadius;
            this.coverPlaceholder = old.coverPlaceholder;
            this.coverError = old.coverError;
            this.skipMemoryCache = old.skipMemoryCache;
            this.diskCacheStrategy = old.diskCacheStrategy;
            this.titleTextSize = old.titleTextSize;
            this.titleTextColor = old.titleTextColor;
            this.arrowSize = old.arrowSize;
            this.arrowCollapsed = old.arrowCollapsed;
            this.arrowExpanded = old.arrowExpanded;
            this.arrowColor = old.arrowColor;
            this.iconButtonSize = old.iconButtonSize;
            this.favoriteIconUnselected = old.favoriteIconUnselected;
            this.favoriteIconSelected = old.favoriteIconSelected;
            this.favoriteIconColor = old.favoriteIconColor;
            this.downloadIcon = old.downloadIcon;
            this.downloadIconColor = old.downloadIconColor;
            this.biographyTextSize = old.biographyTextSize;
            this.biographyTextColor = old.biographyTextColor;
            return this;
        }

        /* -------- 构建 -------- */
        public VideoProfileConfig build() {
            return new VideoProfileConfig(this);
        }
    }
}