package com.example.pornhelper.actor;

import android.content.Context;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.pornhelper.R;
import com.example.pornhelper.helper.Colors;

/**
 * =============================================================
 * ActorProfileConfig（Actor 详情页：头像 / 名字 / 箭头 / 信息 / 收藏按钮）
 * =============================================================
 * 设计原则（与 VideoProfileCoverAesConfig 架构完全对齐）：
 * 1. 不支持 XML 自定义属性（没有 attrs.xml / TypedArray）
 * 2. 所有样式只能在 Java 里用 Builder 配置
 * 3. 单位约定写在字段注释里：
 *      - TextSize → sp（float）
 *      - Size / Radius → dp（int）
 * 4. 颜色统一用 @ColorInt（已经是 ARGB 的 int）
 *      .background(0xFF000000)
 *      .nameTextColor(Colors.of(ctx, R.color.white))
 * 5. 图片资源用 @DrawableRes（R.drawable.xxx）
 * 6. Config 是不可变对象（final 字段 + Builder）
 * 7. Builder 每个字段都有合理非零默认值，setConfig() 直接替换不会出 0 值

 * 完整示例：
 * <pre>
 * ActorProfileConfig cfg = new ActorProfileConfig.Builder()
 *
 *         // 容器
 *         .background(Colors.of(ctx, R.color.black))
 *
 *         // 头像
 *         .avatarSize(150)
 *         .avatarRadius(0)
 *         .avatarPlaceholder(R.drawable.placeholder_actor)
 *         .avatarError(R.drawable.placeholder_actor)
 *         .skipMemoryCache(true)
 *         .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
 *
 *         // 名字
 *         .nameTextSize(16)
 *         .nameTextColor(Colors.of(ctx, R.color.white))
 *
 *         // 箭头
 *         .arrowSize(24)
 *         .arrowCollapsed(R.drawable.ic_arrow_down)
 *         .arrowExpanded(R.drawable.ic_arrow_up)
 *         .arrowColor(Colors.of(ctx, R.color.white))
 *
 *         // 信息文字
 *         .infoTextSize(12)
 *         .infoTextColor(Colors.of(ctx, R.color.white))
 *
 *         // 收藏按钮
 *         .favTextSize(14)
 *         .favTextColor(Colors.of(ctx, R.color.red))
 *         .favTextColorActive(Colors.of(ctx, R.color.white))
 *         .favStroke(Colors.of(ctx, R.color.red))
 *         .favStrokeActive(Colors.of(ctx, R.color.red))
 *         .favBackground(Colors.of(ctx, R.color.white))
 *         .favBackgroundActive(Colors.of(ctx, R.color.red))
 *         .favStrokeWidth(1)
 *         .favCornerRadius(10)
 *         .favAddText("添加收藏")
 *         .favRemoveText("取消收藏")
 *
 *         .build();
 * </pre>
 */
public final class ActorProfileConfig {

    /* =========================================================
     * 容器
     * ========================================================= */
    /** 整个 View 的背景色（@ColorInt） */
    @ColorInt
    public final int background;

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
     * 名字
     * ========================================================= */
    /** 名字字号，单位 sp */
    public final float nameTextSize;

    /** 名字颜色（@ColorInt） */
    @ColorInt
    public final int nameTextColor;

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
     * 信息文字（年龄/身高/三围等）
     * ========================================================= */
    /** 信息文字字号，单位 sp */
    public final float infoTextSize;

    /** 信息文字颜色（@ColorInt） */
    @ColorInt
    public final int infoTextColor;

    /* =========================================================
     * 收藏按钮
     * ========================================================= */
    /** 收藏按钮文字字号，单位 sp */
    public final float favTextSize;

    /** 收藏按钮文字颜色（未选中，@ColorInt） */
    @ColorInt
    public final int favTextColor;

    /** 收藏按钮文字颜色（选中/激活，@ColorInt） */
    @ColorInt
    public final int favTextColorActive;

    /** 收藏按钮描边颜色（未选中，@ColorInt） */
    @ColorInt
    public final int favStroke;

    /** 收藏按钮描边颜色（选中/激活，@ColorInt） */
    @ColorInt
    public final int favStrokeActive;

    /** 收藏按钮背景色（未选中，@ColorInt） */
    @ColorInt
    public final int favBackground;

    /** 收藏按钮背景色（选中/激活，@ColorInt） */
    @ColorInt
    public final int favBackgroundActive;

    /** 收藏按钮描边宽度，单位 dp */
    public final int favStrokeWidth;

    /** 收藏按钮圆角半径，单位 dp */
    public final int favCornerRadius;

    /** 收藏按钮：未收藏时文字 */
    public final String favAddText;

    /** 收藏按钮：已收藏时文字 */
    public final String favRemoveText;

    /* =========================================================
     * 私有构造：只允许 Builder 创建
     * ========================================================= */
    private ActorProfileConfig(Builder b) {
        this.background = b.background;

        this.avatarSize = b.avatarSize;
        this.avatarRadius = b.avatarRadius;
        this.avatarPlaceholder = b.avatarPlaceholder;
        this.avatarError = b.avatarError;
        this.skipMemoryCache = b.skipMemoryCache;
        this.diskCacheStrategy = b.diskCacheStrategy;

        this.nameTextSize = b.nameTextSize;
        this.nameTextColor = b.nameTextColor;

        this.arrowSize = b.arrowSize;
        this.arrowCollapsed = b.arrowCollapsed;
        this.arrowExpanded = b.arrowExpanded;
        this.arrowColor = b.arrowColor;

        this.infoTextSize = b.infoTextSize;
        this.infoTextColor = b.infoTextColor;

        this.favTextSize = b.favTextSize;
        this.favTextColor = b.favTextColor;
        this.favTextColorActive = b.favTextColorActive;
        this.favStroke = b.favStroke;
        this.favStrokeActive = b.favStrokeActive;
        this.favBackground = b.favBackground;
        this.favBackgroundActive = b.favBackgroundActive;
        this.favStrokeWidth = b.favStrokeWidth;
        this.favCornerRadius = b.favCornerRadius;
        this.favAddText = b.favAddText;
        this.favRemoveText = b.favRemoveText;
    }

    /* =========================================================
     * 默认配置（暗色主题，所有字段非零合理值）
     * ========================================================= */
    public static ActorProfileConfig defaultConfig(Context ctx) {
        return new Builder()

                /* 容器 */
                .background(Colors.of(ctx, R.color.black))

                /* 头像 */
                .avatarSize(150)
                .avatarRadius(0)
                .avatarPlaceholder(R.drawable.placeholder_actor)
                .avatarError(R.drawable.placeholder_actor)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)

                /* 名字 */
                .nameTextSize(16)
                .nameTextColor(Colors.of(ctx, R.color.white))

                /* 箭头 */
                .arrowSize(24)
                .arrowCollapsed(R.drawable.ic_arrow_down)
                .arrowExpanded(R.drawable.ic_arrow_up)
                .arrowColor(Colors.of(ctx, R.color.white))

                /* 信息文字 */
                .infoTextSize(12)
                .infoTextColor(Colors.of(ctx, R.color.white))

                /* 收藏按钮 */
                .favTextSize(14)
                .favTextColor(Colors.of(ctx, R.color.red))
                .favTextColorActive(Colors.of(ctx, R.color.white))
                .favStroke(Colors.of(ctx, R.color.red))
                .favStrokeActive(Colors.of(ctx, R.color.red))
                .favBackground(Colors.of(ctx, R.color.white))
                .favBackgroundActive(Colors.of(ctx, R.color.red))
                .favStrokeWidth(1)
                .favCornerRadius(10)
                .favAddText("添加收藏")
                .favRemoveText("取消收藏")

                .build();
    }

    /* =========================================================
     * 辅助方法
     * ========================================================= */
    /** 头像是否设置了圆角 */
    public boolean isAvatarRounded() {
        return avatarRadius > 0;
    }

    /* =========================================================
     * Builder
     * ========================================================= */
    public static final class Builder {

        /* 容器 */
        @ColorInt
        private int background = 0xFF000000;

        /* 头像 */
        private int avatarSize = 150;                              // dp，默认 150
        private int avatarRadius = 0;                              // dp，0 = 不圆角
        @DrawableRes
        private int avatarPlaceholder = R.drawable.placeholder_actor;
        @DrawableRes
        private int avatarError = R.drawable.placeholder_actor;
        private boolean skipMemoryCache = true;
        private DiskCacheStrategy diskCacheStrategy = DiskCacheStrategy.AUTOMATIC;

        /* 名字 */
        private float nameTextSize = 16f;                          // sp，默认 16
        @ColorInt
        private int nameTextColor = 0xFFFFFFFF;                    // 兜底：白色

        /* 箭头 */
        private int arrowSize = 24;                                // dp，默认 24
        @DrawableRes
        private int arrowCollapsed = R.drawable.ic_arrow_down;
        @DrawableRes
        private int arrowExpanded = R.drawable.ic_arrow_up;
        @ColorInt
        private int arrowColor = 0xFFFFFFFF;                       // 兜底：白色

        /* 信息文字 */
        private float infoTextSize = 12f;                          // sp，默认 12
        @ColorInt
        private int infoTextColor = 0xFFFFFFFF;                    // 兜底：白色

        /* 收藏按钮 */
        private float favTextSize = 14f;                           // sp，默认 14
        @ColorInt
        private int favTextColor = 0xFFFF0000;                     // 兜底：红色
        @ColorInt
        private int favTextColorActive = 0xFFFFFFFF;               // 兜底：白色
        @ColorInt
        private int favStroke = 0xFFFF0000;                        // 兜底：红色
        @ColorInt
        private int favStrokeActive = 0xFFFF0000;                  // 兜底：红色
        @ColorInt
        private int favBackground = 0xFFFFFFFF;                    // 兜底：白色
        @ColorInt
        private int favBackgroundActive = 0xFFFF0000;              // 兜底：红色
        private int favStrokeWidth = 1;                            // dp，默认 1
        private int favCornerRadius = 10;                          // dp，默认 10
        private String favAddText = "添加收藏";
        private String favRemoveText = "取消收藏";

        /* -------- 容器 -------- */
        public Builder background(@ColorInt int c) {
            this.background = c;
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

        public Builder skipMemoryCache(boolean v) {
            this.skipMemoryCache = v;
            return this;
        }

        public Builder diskCacheStrategy(DiskCacheStrategy strategy) {
            this.diskCacheStrategy = strategy;
            return this;
        }

        /* -------- 名字 -------- */
        public Builder nameTextSize(float sp) {
            this.nameTextSize = sp;
            return this;
        }

        public Builder nameTextColor(@ColorInt int c) {
            this.nameTextColor = c;
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

        /* -------- 信息文字 -------- */
        public Builder infoTextSize(float sp) {
            this.infoTextSize = sp;
            return this;
        }

        public Builder infoTextColor(@ColorInt int c) {
            this.infoTextColor = c;
            return this;
        }

        /* -------- 收藏按钮 -------- */
        public Builder favTextSize(float sp) {
            this.favTextSize = sp;
            return this;
        }

        public Builder favTextColor(@ColorInt int c) {
            this.favTextColor = c;
            return this;
        }

        public Builder favTextColorActive(@ColorInt int c) {
            this.favTextColorActive = c;
            return this;
        }

        public Builder favStroke(@ColorInt int c) {
            this.favStroke = c;
            return this;
        }

        public Builder favStrokeActive(@ColorInt int c) {
            this.favStrokeActive = c;
            return this;
        }

        public Builder favBackground(@ColorInt int c) {
            this.favBackground = c;
            return this;
        }

        public Builder favBackgroundActive(@ColorInt int c) {
            this.favBackgroundActive = c;
            return this;
        }

        public Builder favStrokeWidth(int dp) {
            this.favStrokeWidth = dp;
            return this;
        }

        public Builder favCornerRadius(int dp) {
            this.favCornerRadius = dp;
            return this;
        }

        public Builder favAddText(String text) {
            this.favAddText = text;
            return this;
        }

        public Builder favRemoveText(String text) {
            this.favRemoveText = text;
            return this;
        }

        /* -------- 拷贝 -------- */
        public Builder copyFrom(ActorProfileConfig old) {
            if (old == null) return this;
            this.background = old.background;
            this.avatarSize = old.avatarSize;
            this.avatarRadius = old.avatarRadius;
            this.avatarPlaceholder = old.avatarPlaceholder;
            this.avatarError = old.avatarError;
            this.skipMemoryCache = old.skipMemoryCache;
            this.diskCacheStrategy = old.diskCacheStrategy;
            this.nameTextSize = old.nameTextSize;
            this.nameTextColor = old.nameTextColor;
            this.arrowSize = old.arrowSize;
            this.arrowCollapsed = old.arrowCollapsed;
            this.arrowExpanded = old.arrowExpanded;
            this.arrowColor = old.arrowColor;
            this.infoTextSize = old.infoTextSize;
            this.infoTextColor = old.infoTextColor;
            this.favTextSize = old.favTextSize;
            this.favTextColor = old.favTextColor;
            this.favTextColorActive = old.favTextColorActive;
            this.favStroke = old.favStroke;
            this.favStrokeActive = old.favStrokeActive;
            this.favBackground = old.favBackground;
            this.favBackgroundActive = old.favBackgroundActive;
            this.favStrokeWidth = old.favStrokeWidth;
            this.favCornerRadius = old.favCornerRadius;
            this.favAddText = old.favAddText;
            this.favRemoveText = old.favRemoveText;
            return this;
        }

        /* -------- 构建 -------- */
        public ActorProfileConfig build() {
            return new ActorProfileConfig(this);
        }
    }
}
