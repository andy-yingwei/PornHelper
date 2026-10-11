package com.example.pornhelper.tag;

import androidx.annotation.ColorInt;

/**
 * Tag UI 配置（样式严格对齐 PlaySourceConfig）
 */
public final class TagConfig {

    /* ===================== CardView 容器 ===================== */
    /** CardView 背景色（@ColorInt） */
    @ColorInt
    public final int itemBackground;

    /** CardView 圆角，单位 dp */
    public final int itemCornerRadius;

    /* ===================== 文字 ===================== */
    /** 文字颜色（@ColorInt） */
    @ColorInt
    public final int textColor;

    /** 文字大小，单位 sp */
    public final int textSize;

    /* ===================== 描边（GradientDrawable） ===================== */
    /** 描边颜色（@ColorInt） */
    @ColorInt
    public final int strokeColors;

    /** 描边宽度，单位 dp */
    public final int strokeWidth;

    private TagConfig(Builder b) {
        this.itemBackground = b.itemBackground;
        this.itemCornerRadius = b.itemCornerRadius;

        this.textColor = b.textColor;
        this.textSize = b.textSize;

        this.strokeColors = b.strokeColors;
        this.strokeWidth = b.strokeWidth;
    }

    /* =========================================================
     * 默认配置
     * ========================================================= */
    public static TagConfig defaultConfig() {
        return new Builder()
                .itemBackground(0xFF000000)      // 黑色
                .itemCornerRadius(0)            // 0dp

                .textColor(0xFF938F99)
                .textSize(12)                   // 12sp

                .strokeColors(0xFF938F99)
                .strokeWidth(1)                 // 1dp

                .build();
    }

    /* =========================================================
     * Builder
     * ========================================================= */
    public static final class Builder {

        private int itemBackground = 0xFF000000;
        private int itemCornerRadius = 0;

        private int textColor = 0xFF938F99;
        private int textSize = 12;

        private int strokeColors = 0xFF938F99;
        private int strokeWidth = 1;

        /* -------- Card -------- */
        public Builder itemBackground(@ColorInt int c) {
            this.itemBackground = c;
            return this;
        }

        public Builder itemCornerRadius(int dp) {
            this.itemCornerRadius = dp;
            return this;
        }

        /* -------- 文字 -------- */
        public Builder textColor(@ColorInt int c) {
            this.textColor = c;
            return this;
        }

        public Builder textSize(int sp) {
            this.textSize = sp;
            return this;
        }

        /* -------- 描边 -------- */
        public Builder strokeColors(@ColorInt int c) {
            this.strokeColors = c;
            return this;
        }

        public Builder strokeWidth(int dp) {
            this.strokeWidth = dp;
            return this;
        }

        /* -------- 构建 -------- */
        public TagConfig build() {
            return new TagConfig(this);
        }
    }
}