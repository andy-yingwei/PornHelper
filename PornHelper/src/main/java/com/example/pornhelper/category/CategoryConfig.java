package com.example.pornhelper.category;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.pornhelper.R;

/**
 * =============================================================
 * CategoryCoverCookieHeaderConfig（Cookie / Header 请求 + 分类封面列表项配置）
 * =============================================================

 * 设计原则：
 * 1. 不支持 XML 自定义属性（没有 attrs.xml / TypedArray）
 * 2. 所有样式只能在 Java 里用 Builder 配置
 * 3. 单位约定写在字段注释里：
 *      - TextSize → sp
 *      - Height / Radius → dp
 * 4. 颜色统一用 @ColorInt（已经是 ARGB 的 int）
 *      .backgroundColor(0xFFFFFFFF)
 *      .titleTextColor(Colors.of(ctx, R.color.black_10))
 * 5. 图片资源用 @DrawableRes（R.drawable.xxx）
 * 6. Config 是不可变对象（final 字段 + Builder）

 * Cookie / Header 是“非样式配置”，但和样式放一起，避免 Adapter 参数爆炸。

 * 完整示例：
 * <pre>
 * CategoryCoverCookieHeaderConfig cfg =
 *         new CategoryCoverCookieHeaderConfig.Builder()
 *
 *         // 卡片
 *         .backgroundColor(Colors.of(ctx, R.color.white))
 *         .backgroundRadius(12)
 *
 *         // 封面
 *         .coverHeight(240)
 *         .coverRoundRadius(8)
 *         .coverPlaceholder(R.drawable.placeholder_category)
 *         .coverError(R.drawable.placeholder_category)
 *
 *         // 标题
 *         .titleTextSize(16)
 *         .titleTextColor(Colors.of(ctx, R.color.black_10))
 *
 *         // 信息
 *         .infoTextSize(12)
 *         .infoTextColor(Colors.of(ctx, R.color.black_10))
 *
 *         // Glide
 *         .skipMemoryCache(false)
 *         .DiskCacheStrategy(DiskCacheStrategy.NONE)
 *
 *
 *         .build();
 * </pre>
 */
public final class CategoryConfig {

    /* =========================================================
     * 卡片
     * ========================================================= */

    /** 卡片背景色（@ColorInt） */
    @ColorInt
    public final int backgroundColor;

    /** 卡片圆角，单位 dp */
    public final int backgroundRadius;

    /* =========================================================
     * 封面
     * ========================================================= */

    /** 封面高度，单位 dp */
    public final int coverHeight;

    /** 封面圆角，单位 dp（0 = 不圆角） */
    public final int coverRoundRadius;

    /** 封面占位图（@DrawableRes） */
    @DrawableRes
    public final int coverPlaceholder;

    /** 封面加载失败图（@DrawableRes） */
    @DrawableRes
    public final int coverError;

    /* =========================================================
     * 文字
     * ========================================================= */

    /** 标题文字大小，单位 sp */
    public final int titleTextSize;

    /** 标题文字颜色（@ColorInt） */
    @ColorInt
    public final int titleTextColor;

    /** 信息行文字大小，单位 sp */
    public final int infoTextSize;

    /** 信息行文字颜色（@ColorInt） */
    @ColorInt
    public final int infoTextColor;

    /* =========================================================
     * Glide
     * ========================================================= */

    /** 是否跳过内存缓存 */
    public final boolean skipMemoryCache;

    /** Glide 磁盘缓存策略 */
    public final DiskCacheStrategy diskCacheStrategy;

    /* =========================================================
     * 私有构造
     * ========================================================= */
    private CategoryConfig(Builder b) {
        this.backgroundColor = b.backgroundColor;
        this.backgroundRadius = b.backgroundRadius;

        this.coverHeight = b.coverHeight;
        this.coverRoundRadius = b.coverRoundRadius;
        this.coverPlaceholder = b.coverPlaceholder;
        this.coverError = b.coverError;

        this.titleTextSize = b.titleTextSize;
        this.titleTextColor = b.titleTextColor;
        this.infoTextSize = b.infoTextSize;
        this.infoTextColor = b.infoTextColor;

        this.skipMemoryCache = b.skipMemoryCache;
        this.diskCacheStrategy = b.diskCacheStrategy;
    }

    /* =========================================================
     * 默认配置（浅色主题，不含请求参数）
     * ========================================================= */
    public static CategoryConfig defaultConfig() {
        return new Builder()

                /* 卡片 */
                .backgroundColor(0xFF000000)
                .backgroundRadius(0)

                /* 封面 */
                .coverHeight(240)
                .coverRoundRadius(0)
                .coverPlaceholder(R.drawable.placeholder_category)
                .coverError(R.drawable.placeholder_category)

                /* 标题 */
                .titleTextSize(16)
                .titleTextColor(0xFFFFFFFF)

                /* 信息 */
                .infoTextSize(12)
                .infoTextColor(0xFFFFFFFF)

                /* Glide */
                .skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)

                .build();
    }

    /* =========================================================
     * Builder
     * ========================================================= */
    public static final class Builder {

        /* 卡片 */
        private int backgroundColor = 0xFF000000; // 黑色
        private int backgroundRadius = 0;         // dp

        /* 封面 */
        private int coverHeight = 240;            // dp
        private int coverRoundRadius = 0;         // dp
        private int coverPlaceholder = R.drawable.placeholder_category;
        private int coverError = R.drawable.placeholder_category;

        /* 文字 */
        private int titleTextSize = 16;           // sp
        private int titleTextColor = 0xFFFFFFFF;  // 白色
        private int infoTextSize = 12;            // sp
        private int infoTextColor = 0xFFFFFFFF;   // 白色

        /* Glide */
        private boolean skipMemoryCache = false;
        private DiskCacheStrategy diskCacheStrategy = DiskCacheStrategy.AUTOMATIC;

        /* -------- 卡片 -------- */
        public Builder backgroundColor(@ColorInt int color) {
            this.backgroundColor = color;
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

        public Builder coverRoundRadius(int dp) {
            this.coverRoundRadius = dp;
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

        /* -------- 标题 -------- */
        public Builder titleTextSize(int sp) {
            this.titleTextSize = sp;
            return this;
        }

        public Builder titleTextColor(@ColorInt int color) {
            this.titleTextColor = color;
            return this;
        }

        /* -------- 信息 -------- */
        public Builder infoTextSize(int sp) {
            this.infoTextSize = sp;
            return this;
        }

        public Builder infoTextColor(@ColorInt int color) {
            this.infoTextColor = color;
            return this;
        }

        /* -------- Glide -------- */
        public Builder skipMemoryCache(boolean skip) {
            this.skipMemoryCache = skip;
            return this;
        }

        public Builder diskCacheStrategy(DiskCacheStrategy strategy) {
            this.diskCacheStrategy = strategy;
            return this;
        }

        /* -------- 拷贝 -------- */
        public Builder copyFrom(CategoryConfig old) {
            if (old == null) return this;

            this.backgroundColor = old.backgroundColor;
            this.backgroundRadius = old.backgroundRadius;

            this.coverHeight = old.coverHeight;
            this.coverRoundRadius = old.coverRoundRadius;
            this.coverPlaceholder = old.coverPlaceholder;
            this.coverError = old.coverError;

            this.titleTextSize = old.titleTextSize;
            this.titleTextColor = old.titleTextColor;
            this.infoTextSize = old.infoTextSize;
            this.infoTextColor = old.infoTextColor;

            this.skipMemoryCache = old.skipMemoryCache;
            this.diskCacheStrategy = old.diskCacheStrategy;

            return this;
        }

        /* -------- 构建 -------- */
        public CategoryConfig build() {
            return new CategoryConfig(this);
        }
    }
}