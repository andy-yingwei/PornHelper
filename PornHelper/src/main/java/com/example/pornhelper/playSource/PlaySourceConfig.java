package com.example.pornhelper.playSource;

import androidx.annotation.ColorInt;

/**
 * 播放源 / 清晰度条 UI 配置
 */
public final class PlaySourceConfig {

    /* ===================== CardView 容器 ===================== */
    /** CardView 背景色（@ColorInt） */
    @ColorInt
    public final int itemBackground;

    /** CardView 圆角，单位 dp */
    public final int itemCornerRadius;

    /* ===================== 文字 ===================== */
    /** 未选中文字颜色（@ColorInt） */
    @ColorInt
    public final int unselectedTextColor;

    /** 选中文字颜色（@ColorInt） */
    @ColorInt
    public final int selectedTextColor;

    /** 文字大小，单位 sp */
    public final int textSize;

    /* ===================== 描边（GradientDrawable） ===================== */
    /** 未选中描边颜色（@ColorInt） */
    @ColorInt
    public final int unselectedStrokeColor;

    /** 选中描边颜色（@ColorInt） */
    @ColorInt
    public final int selectedStrokeColor;

    /** 描边宽度，单位 dp */
    public final int strokeWidth;

    private PlaySourceConfig(Builder b) {
        this.itemBackground = b.itemBackground;
        this.itemCornerRadius = b.itemCornerRadius;

        this.unselectedTextColor = b.unselectedTextColor;
        this.selectedTextColor = b.selectedTextColor;
        this.textSize = b.textSize;

        this.unselectedStrokeColor = b.unselectedStrokeColor;
        this.selectedStrokeColor = b.selectedStrokeColor;
        this.strokeWidth = b.strokeWidth;
    }

    /* =========================================================
     * 默认配置
     * ========================================================= */
    public static PlaySourceConfig defaultConfig() {
        return new Builder()
                .itemBackground(0xFF000000)     // 黑色
                .itemCornerRadius(0)            // 0dp

                .unselectedTextColor(0xFF938F99)
                .selectedTextColor(0xFFF5222D)
                .textSize(12)                   // 12sp

                .unselectedStrokeColor(0xFF938F99)
                .selectedStrokeColor(0xFFF5222D)
                .strokeWidth(1)                 // 1dp

                .build();
    }

    /* =========================================================
     * Builder
     * ========================================================= */
    public static final class Builder {

        private int itemBackground = 0xFF000000;
        private int itemCornerRadius = 0;

        private int unselectedTextColor = 0xFF938F99;
        private int selectedTextColor = 0xFFF5222D;
        private int textSize = 12;

        private int unselectedStrokeColor = 0xFF938F99;
        private int selectedStrokeColor = 0xFFF5222D;
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
        public Builder unselectedTextColor(@ColorInt int c) {
            this.unselectedTextColor = c;
            return this;
        }

        public Builder selectedTextColor(@ColorInt int c) {
            this.selectedTextColor = c;
            return this;
        }

        public Builder textSize(int sp) {
            this.textSize = sp;
            return this;
        }

        /* -------- 描边 -------- */
        public Builder unselectedStrokeColor(@ColorInt int c) {
            this.unselectedStrokeColor = c;
            return this;
        }

        public Builder selectedStrokeColor(@ColorInt int c) {
            this.selectedStrokeColor = c;
            return this;
        }

        public Builder strokeWidth(int dp) {
            this.strokeWidth = dp;
            return this;
        }

        public PlaySourceConfig build() {
            return new PlaySourceConfig(this);
        }
    }
}
