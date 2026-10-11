package com.example.pornhelper.studio;

import android.content.Context;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.pornhelper.R;
import com.example.pornhelper.helper.Colors;

/**
 * ============================================================
 * FilmStudioConfig
 * ============================================================
 *
 * <p><b>功能简介：</b></p>
 * <ul>
 *     <li>用于配置 {@code FilmStudioAdapter} 及其衍生 Adapter 的
 *         视觉样式与行为参数</li>
 *     <li>采用 <b>Builder 模式</b> 构建，支持链式调用与配置拷贝</li>
 *     <li>所有尺寸参数统一使用 <b>dp / sp</b> 逻辑单位</li>
 *     <li>提供 {@link #defaultConfig(Context)} 浅色主题默认配置</li>
 *     <li>配置对象为 <b>不可变（final）</b>，线程安全，可跨 Adapter 复用</li>
 * </ul>
 *
 * <p><b>配置分类：</b></p>
 * <table border="1" cellspacing="0" cellpadding="4">
 *     <tr><th>分类</th><th>关键参数</th><th>说明</th></tr>
 *     <tr><td>卡片</td><td>{@code backgroundColor}, {@code backgroundRadius}</td><td>卡片背景色与圆角</td></tr>
 *     <tr><td>封面</td><td>{@code coverHeight}, {@code coverRoundRadius}, {@code coverPlaceholder}, {@code coverError}</td><td>封面尺寸、圆角、占位/错误图</td></tr>
 *     <tr><td>标题</td><td>{@code nameTextSize}, {@code nameTextColor}</td><td>标题文字大小与颜色</td></tr>
 *     <tr><td>信息</td><td>{@code infoTextSize}, {@code infoTextColor}</td><td>信息行文字大小与颜色</td></tr>
 *     <tr><td>收藏</td><td>{@code favoriteIconSize}, {@code favoriteIcon}</td><td>收藏图标尺寸与资源</td></tr>
 *     <tr><td>缓存</td><td>{@code skipMemoryCache}, {@code diskCacheStrategy}</td><td>Glide 缓存策略</td></tr>
 * </table>
 *
 * <p><b>完整使用范例：</b></p>
 *
 * <pre>
 * // 1. 使用默认配置（浅色主题）
 * FilmStudioConfig config = FilmStudioConfig.defaultConfig(context);
 *
 * // 2. 自定义配置（链式 Builder）
 * FilmStudioConfig customConfig = new FilmStudioConfig.Builder()
 *         .backgroundColor(Color.WHITE)
 *         .backgroundRadius(8)
 *         .coverHeight(200)
 *         .coverRoundRadius(6)
 *         .coverPlaceholder(R.drawable.placeholder_film)
 *         .coverError(R.drawable.placeholder_film)
 *         .skipMemoryCache(false)
 *         .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
 *         .nameTextSize(16)
 *         .nameTextColor(Color.BLACK)
 *         .infoTextSize(12)
 *         .infoTextColor(Color.GRAY)
 *         .favoriteIconSize(40)
 *         .favoriteIcon(R.drawable.ic_star_fill)
 *         .build();
 *
 * // 3. 基于已有配置修改（拷贝 + 覆盖）
 * FilmStudioConfig modifiedConfig = new FilmStudioConfig.Builder()
 *         .copyFrom(config)
 *         .coverHeight(260)
 *         .nameTextSize(20)
 *         .build();
 *
 * // 4. 应用到 Adapter
 * FilmStudioAdapter adapter = new FilmStudioAdapter(list, customConfig);
 * recyclerView.setAdapter(adapter);
 * </pre>
 *
 * <p><b>注意事项：</b></p>
 * <ul>
 *     <li>配置对象创建后不可修改，需通过 {@code copyFrom()} + {@code build()} 派生新配置</li>
 *     <li>尺寸单位均为 dp / sp，Adapter 内部会自行转换为 px</li>
 *     <li>{@code defaultConfig(Context)} 依赖 {@code Colors.of()} 与 {@code R.color} 资源，
 *         请确保资源存在</li>
 *     <li>Glide 缓存策略仅对封面加载生效</li>
 * </ul>
 *
 * <p><b>作者：</b>TODO</p>
 * <p><b>创建时间：</b>TODO</p>
 *
 * @see StudioAdapter
 * @see com.bumptech.glide.load.engine.DiskCacheStrategy
 */
public final class StudioConfig {

    /* ===================== 卡片 ===================== */
    /** 卡片背景色（@ColorInt） */
    @ColorInt
    public final int backgroundColor;

    /** 卡片圆角，单位 dp */
    public final int backgroundRadius;

    /* ===================== 封面 ===================== */
    /** 封面高度，单位 dp */
    public final int coverHeight;

    /** 封面圆角，单位 dp */
    public final int coverRoundRadius;

    /** 占位图（@DrawableRes） */
    @DrawableRes
    public final int coverPlaceholder;

    /** 错误图（@DrawableRes） */
    @DrawableRes
    public final int coverError;

    /** 是否跳过内存缓存 */
    public final boolean skipMemoryCache;

    /** Glide 磁盘缓存策略 */
    public final DiskCacheStrategy diskCacheStrategy;

    /* ===================== 标题 ===================== */
    /** 标题文字大小，单位 sp */
    public final int nameTextSize;

    /** 标题文字颜色（@ColorInt） */
    @ColorInt
    public final int nameTextColor;

    /* ===================== 信息 ===================== */
    /** 信息行文字大小，单位 sp */
    public final int infoTextSize;

    /** 信息行文字颜色（@ColorInt） */
    @ColorInt
    public final int infoTextColor;

    /* ===================== 收藏 ===================== */
    /** 收藏图标宽高，单位 dp */
    public final int favoriteIconSize;

    /** 收藏图标（@DrawableRes） */
    @DrawableRes
    public final int favoriteIcon;

    /* =========================================================
     * 私有构造
     * ========================================================= */
    private StudioConfig(Builder b) {
        this.backgroundColor = b.backgroundColor;
        this.backgroundRadius = b.backgroundRadius;

        this.coverHeight = b.coverHeight;
        this.coverRoundRadius = b.coverRoundRadius;
        this.coverPlaceholder = b.coverPlaceholder;
        this.coverError = b.coverError;
        this.skipMemoryCache = b.skipMemoryCache;
        this.diskCacheStrategy = b.diskCacheStrategy;

        this.nameTextSize = b.nameTextSize;
        this.nameTextColor = b.nameTextColor;

        this.infoTextSize = b.infoTextSize;
        this.infoTextColor = b.infoTextColor;

        this.favoriteIconSize = b.favoriteIconSize;
        this.favoriteIcon = b.favoriteIcon;
    }

    /* =========================================================
     * 默认配置（浅色主题）
     * ========================================================= */
    public static StudioConfig defaultConfig(Context ctx) {
        return new Builder()
                /* 卡片 */
                .backgroundColor(Colors.of(ctx, R.color.black))
                .backgroundRadius(0)

                /* 封面 */
                .coverHeight(180)
                .coverRoundRadius(0)
                .coverPlaceholder(R.drawable.placeholder_video)
                .coverError(R.drawable.placeholder_video)
                .skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)

                /* 标题 */
                .nameTextSize(16)
                .nameTextColor(Colors.of(ctx, R.color.white))

                /* 信息 */
                .infoTextSize(12)
                .infoTextColor(Colors.of(ctx, R.color.white))

                /* 收藏 */
                .favoriteIconSize(20)
                .favoriteIcon(R.drawable.ic_star_fill)

                .build();
    }

    /* =========================================================
     * Builder
     * ========================================================= */
    public static final class Builder {

        /* 卡片 */
        private int backgroundColor = 0xFFFFFFFF;
        private int backgroundRadius = 0;

        /* 封面 */
        private int coverHeight = 180;
        private int coverRoundRadius = 0;
        private int coverPlaceholder = R.drawable.placeholder_video;
        private int coverError = R.drawable.placeholder_video;
        private boolean skipMemoryCache = false;
        private DiskCacheStrategy diskCacheStrategy = DiskCacheStrategy.AUTOMATIC;

        /* 标题 */
        private int nameTextSize = 16;
        private int nameTextColor = 0xFF000000;

        /* 信息 */
        private int infoTextSize = 12;
        private int infoTextColor = 0xFF000000;

        /* 收藏 */
        private int favoriteIconSize = 20;
        private int favoriteIcon = R.drawable.ic_star_fill;

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
            this.coverPlaceholder = (res == 0) ? R.drawable.placeholder_video : res;
            return this;
        }

        public Builder coverError(@DrawableRes int res) {
            this.coverError = (res == 0) ? R.drawable.placeholder_video : res;
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

        /* -------- 标题 -------- */
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

        /* -------- 拷贝 -------- */
        public Builder copyFrom(StudioConfig old) {
            if (old == null) return this;

            this.backgroundColor = old.backgroundColor;
            this.backgroundRadius = old.backgroundRadius;

            this.coverHeight = old.coverHeight;
            this.coverRoundRadius = old.coverRoundRadius;
            this.coverPlaceholder = old.coverPlaceholder;
            this.coverError = old.coverError;
            this.skipMemoryCache = old.skipMemoryCache;
            this.diskCacheStrategy = old.diskCacheStrategy;

            this.nameTextSize = old.nameTextSize;
            this.nameTextColor = old.nameTextColor;

            this.infoTextSize = old.infoTextSize;
            this.infoTextColor = old.infoTextColor;

            this.favoriteIconSize = old.favoriteIconSize;
            this.favoriteIcon = old.favoriteIcon;

            return this;
        }

        /* -------- 构建 -------- */
        public StudioConfig build() {
            return new StudioConfig(this);
        }
    }
}
