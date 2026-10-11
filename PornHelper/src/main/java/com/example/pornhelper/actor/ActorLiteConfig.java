package com.example.pornhelper.actor;

import android.content.Context;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.pornhelper.R;
import com.example.pornhelper.helper.Colors;

/**
 * ============================================================
 * ActorLiteConfig
 * ============================================================

 * ActorLiteAdapter 的配置类（Builder 模式）
 * 控制卡片样式、头像、文字、收藏图标、Glide 策略等全部 UI 与加载参数

 * 字段分组：
 *   - 卡片：backgroundColor / backgroundRadius
 *   - 头像：avatarHeight / avatarRoundRadius / avatarPlaceholder / avatarError
 *   - 文字：nameTextSize / nameTextColor / infoTextSize / infoTextColor
 *   - 收藏：favoriteIconSize / favoriteIcon
 *   - Glide：skipMemoryCache / diskCacheStrategy

 * 提供默认配置 defaultConfig(Context)，基于暗色主题
 * 支持 copyFrom(ActorLiteConfig) 拷贝已有配置

 * ============================================================
 * 使用范例
 * ============================================================

 * 1. 默认暗色主题配置：
 * <pre>
 * ActorLiteConfig config = ActorLiteConfig.defaultConfig(context);
 * </pre>
 *
 * 2. 自定义配置（Builder）：
 * <pre>
 * ActorLiteConfig config = new ActorLiteConfig.Builder()
 *         .backgroundColor(Color.parseColor("#1A1A1A"))
 *         .backgroundRadius(8)
 *         .avatarHeight(200)
 *         .avatarRoundRadius(12)
 *         .avatarPlaceholder(R.drawable.placeholder_actor)
 *         .avatarError(R.drawable.placeholder_actor)
 *         .nameTextSize(18)
 *         .nameTextColor(Color.WHITE)
 *         .infoTextSize(14)
 *         .infoTextColor(Color.LTGRAY)
 *         .favoriteIconSize(24)
 *         .favoriteIcon(R.drawable.ic_star_fill)
 *         .skipMemoryCache(true)
 *         .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
 *         .build();
 * </pre>
 *
 * 3. 基于默认配置修改个别字段：
 * <pre>
 * ActorLiteConfig config = ActorLiteConfig.defaultConfig(context);
 * config = new ActorLiteConfig.Builder()
 *         .copyFrom(config)
 *         .avatarHeight(220)
 *         .nameTextSize(20)
 *         .build();
 * </pre>
 *
 * 4. 传入 Adapter 使用：
 * <pre>
 * ActorLiteAdapter adapter = new ActorLiteAdapter(
 *         context,
 *         config,
 *         true,
 *         debugLog,
 *         true
 * );
 * recyclerView.setAdapter(adapter);
 * </pre>
 *
 * @author Merged
 * @date 2026-09-27
 * ============================================================
 */
public final class ActorLiteConfig {

    /* =========================================================
     * 卡片
     * ========================================================= */

    /** 卡片背景色（@ColorInt） */
    @ColorInt
    public final int backgroundColor;

    /** 卡片圆角，单位 dp */
    public final int backgroundRadius;

    /* =========================================================
     * 头像
     * ========================================================= */

    /** 头像高度，单位 dp */
    public final int avatarHeight;

    /** 头像圆角，单位 dp */
    public final int avatarRoundRadius;

    /** 头像占位图（@DrawableRes） */
    @DrawableRes
    public final int avatarPlaceholder;

    /** 头像加载失败图（@DrawableRes） */
    @DrawableRes
    public final int avatarError;

    /* =========================================================
     * 文字
     * ========================================================= */

    /** 名字文字大小，单位 sp */
    public final int nameTextSize;

    /** 名字文字颜色（@ColorInt） */
    @ColorInt
    public final int nameTextColor;

    /** 信息行文字大小，单位 sp */
    public final int infoTextSize;

    /** 信息行文字颜色（@ColorInt） */
    @ColorInt
    public final int infoTextColor;

    /* =========================================================
     * 收藏图标
     * ========================================================= */

    /** 收藏图标宽高，单位 dp */
    public final int favoriteIconSize;

    /** 收藏图标（@DrawableRes） */
    @DrawableRes
    public final int favoriteIcon;

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
    private ActorLiteConfig(Builder b) {
        this.backgroundColor = b.backgroundColor;
        this.backgroundRadius = b.backgroundRadius;

        this.avatarHeight = b.avatarHeight;
        this.avatarRoundRadius = b.avatarRoundRadius;
        this.avatarPlaceholder = b.avatarPlaceholder;
        this.avatarError = b.avatarError;

        this.nameTextSize = b.nameTextSize;
        this.nameTextColor = b.nameTextColor;
        this.infoTextSize = b.infoTextSize;
        this.infoTextColor = b.infoTextColor;

        this.favoriteIconSize = b.favoriteIconSize;
        this.favoriteIcon = b.favoriteIcon;

        this.skipMemoryCache = b.skipMemoryCache;
        this.diskCacheStrategy = b.diskCacheStrategy;
    }

    /* =========================================================
     * 默认配置（暗色主题）
     * ========================================================= */
    public static ActorLiteConfig defaultConfig(Context ctx) {
        return new Builder()

                /* 卡片 */
                .backgroundColor(Colors.of(ctx, R.color.black))
                .backgroundRadius(0)

                /* 头像 */
                .avatarHeight(170)
                .avatarRoundRadius(0)
                .avatarPlaceholder(R.drawable.placeholder_actor)
                .avatarError(R.drawable.placeholder_actor)

                /* 名字 */
                .nameTextSize(16)
                .nameTextColor(Colors.of(ctx, R.color.white))

                /* 信息 */
                .infoTextSize(12)
                .infoTextColor(Colors.of(ctx, R.color.white))

                /* 收藏 */
                .favoriteIconSize(20)
                .favoriteIcon(R.drawable.ic_star_fill)

                /* Glide */
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)

                .build();
    }

    /* =========================================================
     * Builder
     * ========================================================= */
    public static final class Builder {

        /* 卡片 */
        private int backgroundColor = 0xFFFFFFFF;
        private int backgroundRadius = 0;

        /* 头像 */
        private int avatarHeight = 170;
        private int avatarRoundRadius = 0;
        private int avatarPlaceholder = R.drawable.placeholder_actor;
        private int avatarError = R.drawable.placeholder_actor;

        /* 文字 */
        private int nameTextSize = 16;
        private int nameTextColor = 0xFF000000;
        private int infoTextSize = 12;
        private int infoTextColor = 0xFF000000;

        /* 收藏 */
        private int favoriteIconSize = 20;
        private int favoriteIcon = R.drawable.ic_star_fill;

        /* Glide */
        private boolean skipMemoryCache = true;
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

        /* -------- 头像 -------- */
        public Builder avatarHeight(int dp) {
            this.avatarHeight = dp;
            return this;
        }

        public Builder avatarRoundRadius(int dp) {
            this.avatarRoundRadius = dp;
            return this;
        }

        public Builder avatarPlaceholder(@DrawableRes int res) {
            this.avatarPlaceholder = (res == 0) ? R.drawable.placeholder_actor : res;
            return this;
        }

        public Builder avatarError(@DrawableRes int res) {
            this.avatarError = (res == 0) ? R.drawable.placeholder_actor : res;
            return this;
        }

        /* -------- 名字 -------- */
        public Builder nameTextSize(int sp) {
            this.nameTextSize = sp;
            return this;
        }

        public Builder nameTextColor(@ColorInt int color) {
            this.nameTextColor = color;
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

        /* -------- 收藏 -------- */
        public Builder favoriteIconSize(int dp) {
            this.favoriteIconSize = dp;
            return this;
        }

        public Builder favoriteIcon(@DrawableRes int res) {
            this.favoriteIcon = res;
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
        public Builder copyFrom(ActorLiteConfig old) {
            if (old == null) return this;

            this.backgroundColor = old.backgroundColor;
            this.backgroundRadius = old.backgroundRadius;

            this.avatarHeight = old.avatarHeight;
            this.avatarRoundRadius = old.avatarRoundRadius;
            this.avatarPlaceholder = old.avatarPlaceholder;
            this.avatarError = old.avatarError;

            this.nameTextSize = old.nameTextSize;
            this.nameTextColor = old.nameTextColor;
            this.infoTextSize = old.infoTextSize;
            this.infoTextColor = old.infoTextColor;

            this.favoriteIconSize = old.favoriteIconSize;
            this.favoriteIcon = old.favoriteIcon;

            this.skipMemoryCache = old.skipMemoryCache;
            this.diskCacheStrategy = old.diskCacheStrategy;

            return this;
        }

        /* -------- 构建 -------- */
        public ActorLiteConfig build() {
            return new ActorLiteConfig(this);
        }
    }
}