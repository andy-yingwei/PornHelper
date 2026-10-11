package com.example.pornhelper.magnet;

import androidx.annotation.ColorInt;

/**
 * MagnetAdapter 的 UI 配置（样式严格对齐 TagConfig）
 */
public final class MagnetConfig {

    /* ===================== Item（CardView） ===================== */
    /** Item 背景色（@ColorInt） */
    @ColorInt
    public final int itemBackground;

    /** Item 圆角，单位 dp */
    public final int itemCornerRadius;

    /* ===================== 标题文字 ===================== */
    /** 标题文字颜色（@ColorInt） */
    @ColorInt
    public final int titleTextColor;

    /** 标题文字大小，单位 sp */
    public final int titleTextSize;

    /* ===================== 副信息文字（大小 / 下载量） ===================== */
    /** 副信息文字颜色（@ColorInt） */
    @ColorInt
    public final int infoTextColor;

    /** 副信息文字大小，单位 sp */
    public final int infoTextSize;

    /* ===================== 磁力药丸文字 ===================== */
    /** 磁力药丸文字颜色（@ColorInt） */
    @ColorInt
    public final int magnetTextColor;

    /** 磁力药丸文字大小，单位 sp */
    public final int magnetTextSize;

    /* ===================== 药丸描边（GradientDrawable） ===================== */
    /** 药丸描边颜色（@ColorInt） */
    @ColorInt
    public final int pillStrokeColor;

    /** 药丸描边宽度，单位 dp */
    public final int pillStrokeWidth;

    private MagnetConfig(Builder b) {
        this.itemBackground = b.itemBackground;
        this.itemCornerRadius = b.itemCornerRadius;

        this.titleTextColor = b.titleTextColor;
        this.titleTextSize = b.titleTextSize;

        this.infoTextColor = b.infoTextColor;
        this.infoTextSize = b.infoTextSize;

        this.magnetTextColor = b.magnetTextColor;
        this.magnetTextSize = b.magnetTextSize;

        this.pillStrokeColor = b.pillStrokeColor;
        this.pillStrokeWidth = b.pillStrokeWidth;
    }

    /* =========================================================
     * 默认配置（和 XML 同源）
     * ========================================================= */
    public static MagnetConfig defaultConfig() {
        return new Builder()
                .itemBackground(0xFF000000)
                .itemCornerRadius(0)

                .titleTextColor(0xFF938F99)
                .titleTextSize(12)

                .infoTextColor(0xFF938F99)
                .infoTextSize(10)

                .magnetTextColor(0xFF938F99)
                .magnetTextSize(12)

                .pillStrokeColor(0xFF938F99)
                .pillStrokeWidth(1)

                .build();
    }

    /* =========================================================
     * Builder
     * ========================================================= */
    public static final class Builder {

        private int itemBackground = 0xFF000000;
        private int itemCornerRadius = 0;

        private int titleTextColor = 0xFF938F99;
        private int titleTextSize = 12;

        private int infoTextColor = 0xFF938F99;
        private int infoTextSize = 10;

        private int magnetTextColor = 0xFF938F99;
        private int magnetTextSize = 12;

        private int pillStrokeColor = 0xFF938F99;
        private int pillStrokeWidth = 1;

        /* -------- Item -------- */
        public Builder itemBackground(@ColorInt int c) {
            this.itemBackground = c;
            return this;
        }

        public Builder itemCornerRadius(int dp) {
            this.itemCornerRadius = dp;
            return this;
        }

        /* -------- 标题 -------- */
        public Builder titleTextColor(@ColorInt int c) {
            this.titleTextColor = c;
            return this;
        }

        public Builder titleTextSize(int sp) {
            this.titleTextSize = sp;
            return this;
        }

        /* -------- 副信息 -------- */
        public Builder infoTextColor(@ColorInt int c) {
            this.infoTextColor = c;
            return this;
        }

        public Builder infoTextSize(int sp) {
            this.infoTextSize = sp;
            return this;
        }

        /* -------- 磁力药丸 -------- */
        public Builder magnetTextColor(@ColorInt int c) {
            this.magnetTextColor = c;
            return this;
        }

        public Builder magnetTextSize(int sp) {
            this.magnetTextSize = sp;
            return this;
        }

        /* -------- 描边 -------- */
        public Builder pillStrokeColor(@ColorInt int c) {
            this.pillStrokeColor = c;
            return this;
        }

        public Builder pillStrokeWidth(int dp) {
            this.pillStrokeWidth = dp;
            return this;
        }

        /* -------- 构建 -------- */
        public MagnetConfig build() {
            return new MagnetConfig(this);
        }
    }
}