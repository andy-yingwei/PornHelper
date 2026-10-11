package com.example.pornhelper.cast;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.pornhelper.R;

/**
 * =============================================================
 * CastConfig（演员 Cast / 头像列表项 UI 配置）
 * =============================================================
 * 设计原则（与 ActorProfileConfig 架构完全对齐）：
 * 1. 不支持 XML 自定义属性（没有 attrs.xml / TypedArray）
 * 2. 所有样式只能在 Java 里用 Builder 配置
 * 3. 单位约定写在字段注释里：
 *      - TextSize        → sp（int）
 *      - Size / Radius / Width → dp（int）
 * 4. 颜色统一用 @ColorInt（已经是 ARGB 的 int）
 *      .background(0xFF000000)
 *      .nameTextColor(0xFF938F99)
 * 5. 图片资源用 @DrawableRes（R.drawable.xxx）
 * 6. Config 是不可变对象（final 字段 + Builder）
 * 7. Builder 每个字段都有合理非零默认值，setConfig() 直接替换不会出 0 值

 * 完整示例：
 * <pre>
 * CastConfig cfg = new CastConfig.Builder()
 *
 *         // 容器
 *         .background(0xFF000000)
 *         .backgroundRadius(0)
 *
 *         // 头像
 *         .avatarSize(40)
 *         .avatarRadius(15)
 *         .avatarPlaceholder(R.drawable.placeholder_actor)
 *         .avatarError(R.drawable.placeholder_actor)
 *         .skipMemoryCache(true)
 *         .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
 *
 *         // 文字
 *         .nameTextSize(12)
 *         .nameTextColor(0xFF938F99)
 *
 *         // 描边（药丸）
 *         .strokeColor(0xFF938F99)
 *         .strokeWidth(1)
 *
 *         .build();
 * </pre>
 */
public final class CastConfig {

    /* =========================================================
     * 容器
     * ========================================================= */
    /** 整个 Item 的背景色（@ColorInt） */
    @ColorInt
    public final int background;

    /** 背景圆角半径，单位 dp */
    public final int backgroundRadius;

    /* =========================================================
     * 头像
     * ========================================================= */
    /** 头像尺寸，单位 dp（宽高一致） */
    public final int avatarSize;

    /** 头像圆角半径，单位 dp；0 = 不圆角 */
    public final int avatarRadius;

    /** 头像占位图（R.drawable.xxx） */
    @DrawableRes
    public final int avatarPlaceholder;

    /** 头像加载失败兜底图（R.drawable.xxx） */
    @DrawableRes
    public final int avatarError;

    /** 是否跳过内存缓存（Glide: skipMemoryCache） */
    public final boolean skipMemoryCache;

    /** Glide 磁盘缓存策略 */
    public final DiskCacheStrategy diskCacheStrategy;

    /* =========================================================
     * 文字
     * ========================================================= */
    /** 名字字号，单位 sp */
    public final int nameTextSize;

    /** 名字颜色（@ColorInt） */
    @ColorInt
    public final int nameTextColor;

    /* =========================================================
     * 描边（药丸 / Stroke）
     * ========================================================= */
    /** 描边颜色（@ColorInt） */
    @ColorInt
    public final int strokeColor;

    /** 描边宽度，单位 dp */
    public final int strokeWidth;

    /* =========================================================
     * 私有构造：只允许 Builder 创建
     * ========================================================= */
    private CastConfig(Builder b) {
        this.background = b.background;
        this.backgroundRadius = b.backgroundRadius;

        this.avatarSize = b.avatarSize;
        this.avatarRadius = b.avatarRadius;
        this.avatarPlaceholder = b.avatarPlaceholder;
        this.avatarError = b.avatarError;
        this.skipMemoryCache = b.skipMemoryCache;
        this.diskCacheStrategy = b.diskCacheStrategy;

        this.nameTextSize = b.nameTextSize;
        this.nameTextColor = b.nameTextColor;

        this.strokeColor = b.strokeColor;
        this.strokeWidth = b.strokeWidth;
    }

    /* =========================================================
     * 默认配置
     * ========================================================= */
    public static CastConfig defaultConfig() {
        return new Builder()
                .background(0xFF000000)
                .backgroundRadius(0)

                .avatarSize(40)
                .avatarRadius(15)
                .avatarPlaceholder(R.drawable.placeholder_actor)
                .avatarError(R.drawable.placeholder_actor)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)

                .nameTextSize(12)
                .nameTextColor(0xFFFFFFFF)

                .strokeColor(0xFFFFFFFF)
                .strokeWidth(1)

                .build();
    }

    /* =========================================================
     * 辅助方法
     * ========================================================= */
    /** 头像是否设置了圆角 */
    public boolean isAvatarRounded() {
        return avatarRadius > 0;
    }

    /** 背景是否设置了圆角 */
    public boolean isBackgroundRounded() {
        return backgroundRadius > 0;
    }

    /* =========================================================
     * Builder
     * ========================================================= */
    public static final class Builder {

        /* 容器 */
        @ColorInt
        private int background = 0xFF000000;
        private int backgroundRadius = 0;

        /* 头像 */
        private int avatarSize = 40;
        private int avatarRadius = 15;
        @DrawableRes
        private int avatarPlaceholder = R.drawable.placeholder_actor;
        @DrawableRes
        private int avatarError = R.drawable.placeholder_actor;
        private boolean skipMemoryCache = true;
        private DiskCacheStrategy diskCacheStrategy = DiskCacheStrategy.AUTOMATIC;

        /* 文字 */
        private int nameTextSize = 12;
        @ColorInt
        private int nameTextColor = 0xFFFFFFFF;

        /* 描边 */
        @ColorInt
        private int strokeColor = 0xFFFFFFFF;
        private int strokeWidth = 1;

        /* -------- 容器 -------- */
        public Builder background(@ColorInt int c) {
            this.background = c;
            return this;
        }

        public Builder backgroundRadius(int dp) {
            this.backgroundRadius = dp;
            return this;
        }

        /* -------- 头像 -------- */
        public Builder avatarSize(int dp) {
            this.avatarSize = dp;
            return this;
        }

        public Builder avatarRadius(int dp) {
            this.avatarRadius = dp;
            return this;
        }

        public Builder avatarPlaceholder(@DrawableRes int res) {
            this.avatarPlaceholder = res;
            return this;
        }

        public Builder avatarError(@DrawableRes int res) {
            this.avatarError = res;
            return this;
        }

        public Builder skipMemoryCache(boolean skip) {
            this.skipMemoryCache = skip;
            return this;
        }

        public Builder diskCacheStrategy(DiskCacheStrategy strategy) {
            this.diskCacheStrategy = strategy;
            return this;
        }

        /* -------- 文字 -------- */
        public Builder nameTextSize(int sp) {
            this.nameTextSize = sp;
            return this;
        }

        public Builder nameTextColor(@ColorInt int c) {
            this.nameTextColor = c;
            return this;
        }

        /* -------- 描边 -------- */
        public Builder strokeColor(@ColorInt int c) {
            this.strokeColor = c;
            return this;
        }

        public Builder strokeWidth(int dp) {
            this.strokeWidth = dp;
            return this;
        }

        /* -------- 拷贝 -------- */
        public Builder copyFrom(CastConfig old) {
            if (old == null) return this;
            this.background = old.background;
            this.backgroundRadius = old.backgroundRadius;
            this.avatarSize = old.avatarSize;
            this.avatarRadius = old.avatarRadius;
            this.avatarPlaceholder = old.avatarPlaceholder;
            this.avatarError = old.avatarError;
            this.skipMemoryCache = old.skipMemoryCache;
            this.diskCacheStrategy = old.diskCacheStrategy;
            this.nameTextSize = old.nameTextSize;
            this.nameTextColor = old.nameTextColor;
            this.strokeColor = old.strokeColor;
            this.strokeWidth = old.strokeWidth;
            return this;
        }

        /* -------- 构建 -------- */
        public CastConfig build() {
            return new CastConfig(this);
        }
    }
}


