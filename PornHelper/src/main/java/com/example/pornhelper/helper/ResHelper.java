package com.example.pornhelper.helper;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.annotation.ArrayRes;
import androidx.annotation.BoolRes;
import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.IntegerRes;
import androidx.annotation.PluralsRes;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;

/**
 * ============================================================
 * ResHelper - Android 资源统一访问工具类
 * ============================================================

 * 合并颜色安全读取（原 ResourceHelper）与颜色工具（原 Colors），
 * 去重后统一入口，非颜色资源方法原样保留。

 * 所有方法均保证不崩溃，资源不存在或参数异常时返回安全默认值。

 * 方法一览：

 * 字符串：
 *   getString(Context, int)
 *   getString(Context, int, Object...)
 *   getQuantityString(Context, int, int, Object...)

 * 数组：
 *   getStringArray(Context, int)
 *   getIntArray(Context, int)

 * Drawable：
 *   getDrawable(Context, int)

 * 颜色：
 *   getColor(Context, int)
 *   getColorStateList(Context, int)
 *   getColorAttr(Context, int)
 *   parseColor(String)
 *   parseColor(String, int)
 *   color(int)
 *   withAlpha(int, int)
 *   isDark(int)

 * 尺寸：
 *   getDimension(Context, int)
 *   getDimensionPixelSize(Context, int)

 * 布尔 & 整型：
 *   getBoolean(Context, int)
 *   getInteger(Context, int)

 * 校验：
 *   hasResource(Context, int)
 * ============================================================
 */

/**
 * Android 资源统一安全访问工具类（不可实例化）
 */
public final class ResHelper {

    private static final String TAG = "ResHelper";

    private ResHelper() {
        throw new UnsupportedOperationException("工具类禁止实例化");
    }

    // ===============================
    // 基础方法
    // ===============================

    /**
     * 安全获取 Resources 实例，避免重复判空。
     *
     * <p>示例：
     * <pre>
     * Resources res = ResHelper.getResources(context);
     * if (res != null) {
     *     String s = res.getString(R.string.app_name);
     * }
     * </pre>
     *
     * @param context 上下文
     * @return Resources 实例，context 为 null 时返回 null
     */
    private static Resources getResources(Context context) {
        if (context == null) {
            Log.e(TAG, "Context 为 null");
            return null;
        }
        return context.getResources();
    }

    /**
     * 判断指定资源 ID 是否存在。
     *
     * <p>示例：
     * <pre>
     * if (ResHelper.hasResource(this, R.string.tips)) {
     *     showTips(ResHelper.getString(this, R.string.tips));
     * }
     * </pre>
     *
     * @param context 上下文
     * @param id      资源 ID
     * @return true 存在，false 不存在或 context 为 null
     */
    public static boolean hasResource(Context context, int id) {
        Resources res = getResources(context);
        if (res == null) {
            return false;
        }
        try {
            res.getResourceTypeName(id);
            return true;
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "资源不存在, id=" + id);
            return false;
        }
    }

    // ===============================
    // 一、字符串资源
    // ===============================

    /**
     * 安全获取字符串资源，资源不存在时返回空字符串，不崩溃。
     *
     * <p>示例：
     * <pre>
     * String appName = ResHelper.getString(this, R.string.app_name);
     * // R.string.app_name 不存在 → 返回 ""
     * </pre>
     *
     * @param context 上下文
     * @param id      字符串资源 ID
     * @return 字符串值，异常时返回空字符串
     */
    public static String getString(Context context, @StringRes int id) {
        Resources res = getResources(context);
        if (res == null) {
            return "";
        }
        try {
            return res.getString(id);
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "字符串未找到, id=" + id, e);
            return "";
        }
    }

    /**
     * 安全获取带格式化参数的字符串资源。
     *
     * <p>示例：
     * <pre>
     * String tip = ResHelper.getString(this, R.string.welcome, "张三", 18);
     * // R.string.welcome = "你好 %s，今年 %d 岁"
     * // → "你好 张三，今年 18 岁"
     * </pre>
     *
     * @param context 上下文
     * @param id      字符串资源 ID
     * @param args    格式化参数
     * @return 格式化后的字符串，异常时返回空字符串
     */
    public static String getString(Context context, @StringRes int id, Object... args) {
        Resources res = getResources(context);
        if (res == null) {
            return "";
        }
        try {
            return res.getString(id, args);
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "字符串未找到, id=" + id, e);
            return "";
        }
    }

    /**
     * 安全获取复数字符串（plurals），根据 quantity 自动选择单/复数形式。
     *
     * <p>示例：
     * <pre>
     * String desc = ResHelper.getQuantityString(this, R.plurals.photo_count, 3, 3);
     * // quantity=1 → "1 张照片"
     * // quantity>1 → "3 张照片"
     * </pre>
     *
     * @param context  上下文
     * @param id       plurals 资源 ID
     * @param quantity 数量（决定单复数）
     * @param args     格式化参数
     * @return 复数字符串，异常时返回空字符串
     */
    public static String getQuantityString(
            Context context,
            @PluralsRes int id,
            int quantity,
            Object... args
    ) {
        Resources res = getResources(context);
        if (res == null) {
            return "";
        }
        try {
            return res.getQuantityString(id, quantity, args);
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "复数字符串未找到, id=" + id, e);
            return "";
        }
    }

    // ===============================
    // 二、数组资源
    // ===============================

    /**
     * 安全获取字符串数组，资源不存在时返回空数组，不崩溃。
     *
     * <p>示例：
     * <pre>
     * String[] cities = ResHelper.getStringArray(this, R.array.cities);
     * // R.array.cities 不存在 → 返回 new String[0]
     * for (String city : cities) {
     *     Log.d(TAG, city);
     * }
     * </pre>
     *
     * @param context 上下文
     * @param id      数组资源 ID
     * @return 字符串数组，异常时返回空数组
     */
    public static String[] getStringArray(Context context, @ArrayRes int id) {
        Resources res = getResources(context);
        if (res == null) {
            return new String[0];
        }
        try {
            return res.getStringArray(id);
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "字符串数组未找到, id=" + id, e);
            return new String[0];
        }
    }

    /**
     * 安全获取整型数组，资源不存在时返回空数组，不崩溃。
     *
     * <p>示例：
     * <pre>
     * int[] weights = ResHelper.getIntArray(this, R.array.weights);
     * // R.array.weights 不存在 → 返回 new int[0]
     * </pre>
     *
     * @param context 上下文
     * @param id      数组资源 ID
     * @return 整型数组，异常时返回空数组
     */
    public static int[] getIntArray(Context context, @ArrayRes int id) {
        Resources res = getResources(context);
        if (res == null) {
            return new int[0];
        }
        try {
            return res.getIntArray(id);
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "整型数组未找到, id=" + id, e);
            return new int[0];
        }
    }

    // ===============================
    // 三、Drawable 资源
    // ===============================

    /**
     * 安全获取 Drawable 资源，资源不存在时返回 null，不崩溃。
     *
     * <p>示例：
     * <pre>
     * Drawable icon = ResHelper.getDrawable(this, R.drawable.ic_logo);
     * if (icon != null) {
     *     imageView.setImageDrawable(icon);
     * }
     * </pre>
     *
     * @param context 上下文
     * @param id      Drawable 资源 ID
     * @return Drawable 对象，异常时返回 null
     */
    public static Drawable getDrawable(Context context, @DrawableRes int id) {
        if (context == null) {
            Log.e(TAG, "Context 为 null");
            return null;
        }
        try {
            return ContextCompat.getDrawable(context, id);
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "可绘制资源未找到, id=" + id, e);
            return null;
        }
    }

    // ===============================
    // 四、颜色资源
    // ===============================

    /**
     * 安全获取颜色资源，资源不存在时返回 0，不崩溃。
     *
     * <p>示例：
     * <pre>
     * int bg = ResHelper.getColor(this, R.color.background);
     * // R.color.background 不存在 → 返回 0 (Color.TRANSPARENT)
     * view.setBackgroundColor(bg);
     * </pre>
     *
     * @param context 上下文
     * @param id      颜色资源 ID
     * @return 颜色值 @ColorInt，异常时返回 0
     */
    @ColorInt
    public static int getColor(Context context, @ColorRes int id) {
        if (context == null) {
            Log.e(TAG, "Context 为 null");
            return 0;
        }
        try {
            return ContextCompat.getColor(context, id);
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "颜色没有找到, id=" + id, e);
            return 0;
        }
    }

    /**
     * 安全获取颜色状态列表（selector），资源不存在时返回 null，不崩溃。
     *
     * <p>示例：
     * <pre>
     * ColorStateList csl = ResHelper.getColorStateList(this, R.color.tab_state);
     * if (csl != null) {
     *     tabLayout.setTabTextColors(csl);
     * }
     * </pre>
     *
     * @param context 上下文
     * @param id      颜色资源 ID
     * @return ColorStateList 对象，异常时返回 null
     */
    public static ColorStateList getColorStateList(Context context, @ColorRes int id) {
        if (context == null) {
            Log.e(TAG, "Context 为 null");
            return null;
        }
        try {
            return ContextCompat.getColorStateList(context, id);
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "颜色状态列表未找到, id=" + id, e);
            return null;
        }
    }

    /**
     * 从主题属性解析颜色值，如 ?attr/colorPrimary。
     * 解析失败时返回 0，不崩溃。
     *
     * <p>示例：
     * <pre>
     * int primary = ResHelper.getColorAttr(this, R.attr.colorPrimary);
     * // 等价于 ?attr/colorPrimary → @ColorInt
     * // 解析失败 → 返回 0
     * toolbar.setBackgroundColor(primary);
     * </pre>
     *
     * @param context   上下文
     * @param attrResId 主题属性 ID，如 R.attr.colorPrimary
     * @return 颜色值 @ColorInt，异常时返回 0
     */
    @ColorInt
    public static int getColorAttr(Context context, int attrResId) {
        if (context == null) {
            Log.e(TAG, "Context 为 null");
            return 0;
        }
        int[] attrs = {attrResId};
        TypedArray ta = null;
        try {
            ta = context.obtainStyledAttributes(attrs);
            return ta.getColor(0, 0);
        } catch (Exception e) {
            Log.e(TAG, "主题属性颜色解析失败, attr=" + attrResId, e);
            return 0;
        } finally {
            if (ta != null) {
                ta.recycle();
            }
        }
    }

    /**
     * 十六进制颜色字符串解析为 @ColorInt，格式错误时返回 0，不崩溃。
     *
     * <p>示例：
     * <pre>
     * int pink  = ResHelper.parseColor("#FF4081");
     * int white = ResHelper.parseColor("#FFFFFF");
     * int black = ResHelper.parseColor("#000");     // 简写三色值
     * // 格式错误 → 返回 0
     * </pre>
     *
     * @param hex 十六进制颜色字符串
     * @return 颜色值 @ColorInt，解析失败返回 0
     */
    @ColorInt
    public static int parseColor(String hex) {
        if (hex == null || hex.trim().isEmpty()) {
            Log.e(TAG, "颜色字符串为 null 或空");
            return 0;
        }
        try {
            return Color.parseColor(hex);
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "颜色字符串格式错误: " + hex, e);
            return 0;
        }
    }

    /**
     * 十六进制颜色字符串解析为 @ColorInt，格式错误时返回调用方指定的兜底色。
     * 适用于用户输入、动态配置等不可信来源的场景。
     *
     * <p>示例：
     * <pre>
     * int color = ResHelper.parseColor(userInput, 0xFFFF4081);
     * // userInput = "#FF4081" → 0xFFFF4081（正常解析）
     * // userInput = "#GGG"    → 0xFFFF4081（兜底）
     * // userInput = null      → 0xFFFF4081（兜底）
     * </pre>
     *
     * @param hex          十六进制颜色字符串
     * @param defaultColor 解析失败时返回的兜底色
     * @return 解析后的颜色值，失败返回 defaultColor
     */
    @ColorInt
    public static int parseColor(String hex, @ColorInt int defaultColor) {
        if (hex == null || hex.trim().isEmpty()) {
            Log.e(TAG, "颜色字符串为 null 或空，返回兜底色: " + defaultColor);
            return defaultColor;
        }
        try {
            return Color.parseColor(hex);
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "颜色字符串格式错误: " + hex + "，返回兜底色: " + defaultColor, e);
            return defaultColor;
        }
    }

    /**
     * 裸色值 @ColorInt 原样返回，用于 Config 中直接写 0xAARRGGBB。
     *
     * <p>⚠️ 警告：不要传入 R.color.xxx 资源 ID，此方法不会解析。
     * 从资源获取颜色请使用 {@link #getColor(Context, int)}。
     *
     * <p>示例：
     * <pre>
     * int black = ResHelper.color(0xFF000000);
     * int red   = ResHelper.color(0xFFFF0000);
     * // 直接透传，不做任何转换
     * </pre>
     *
     * @param colorInt 裸颜色值
     * @return 原样返回 colorInt
     */
    @ColorInt
    public static int color(@ColorInt int colorInt) {
        return colorInt;
    }

    /**
     * 给已有颜色设置 alpha 值（0x00 完全透明 ~ 0xFF 完全不透明）。
     *
     * <p>示例：
     * <pre>
     * int black = 0xFF000000;
     * int translucent = ResHelper.withAlpha(black, 0x80);  // 50% 透明黑
     * int faint       = ResHelper.withAlpha(black, 0x1A);  // ~10% 不透明黑
     * // 原颜色: 0xFF000000 → 0x80000000
     * </pre>
     *
     * @param color 原始颜色 @ColorInt
     * @param alpha alpha 值（0x00 ~ 0xFF）
     * @return 带新 alpha 的颜色值
     */
    @ColorInt
    public static int withAlpha(@ColorInt int color, int alpha) {
        return (color & 0x00FFFFFF) | ((alpha & 0xFF) << 24);
    }

    /**
     * 判断颜色是否为深色，基于相对亮度公式。
     * luminance = (0.299*R + 0.587*G + 0.114*B) / 255
     * luminance &lt; 0.5 判定为深色。
     *
     * <p>注意：alpha 通道不参与亮度计算。
     * 如需考虑背景透明度对视觉效果的影响，调用方应自行处理。
     *
     * <p>示例：
     * <pre>
     * int bg = ResHelper.getColor(this, R.color.background);
     * if (ResHelper.isDark(bg)) {
     *     titleView.setTextColor(Color.WHITE);
     * } else {
     *     titleView.setTextColor(Color.BLACK);
     * }
     * </pre>
     *
     * @param color 颜色值 @ColorInt
     * @return true 为深色，false 为浅色
     */
    public static boolean isDark(int color) {
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        double luminance = (0.299 * r + 0.587 * g + 0.114 * b) / 255;
        return luminance < 0.5;
    }

    // ===============================
    // 五、尺寸资源
    // ===============================

    /**
     * 安全获取尺寸资源（返回 float px 值），资源不存在时返回 0f，不崩溃。
     *
     * <p>示例：
     * <pre>
     * float dp16 = ResHelper.getDimension(this, R.dimen.dp_16);
     * // R.dimen.dp_16 = 16dp → 返回转换后的 px 值（如 48.0f on 3x screen）
     * // 不存在 → 返回 0f
     * </pre>
     *
     * @param context 上下文
     * @param id      尺寸资源 ID
     * @return px 值，异常时返回 0f
     */
    public static float getDimension(Context context, @DimenRes int id) {
        Resources res = getResources(context);
        if (res == null) {
            return 0f;
        }
        try {
            return res.getDimension(id);
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "尺寸未找到, id=" + id, e);
            return 0f;
        }
    }

    /**
     * 安全获取尺寸资源（返回 int px 值，四舍五入），资源不存在时返回 0，不崩溃。
     *
     * <p>示例：
     * <pre>
     * int padding = ResHelper.getDimensionPixelSize(this, R.dimen.padding);
     * // R.dimen.padding = 16dp → 返回 int px 值
     * // 不存在 → 返回 0
     * view.setPadding(padding, padding, padding, padding);
     * </pre>
     *
     * @param context 上下文
     * @param id      尺寸资源 ID
     * @return px 整数值，异常时返回 0
     */
    public static int getDimensionPixelSize(Context context, @DimenRes int id) {
        Resources res = getResources(context);
        if (res == null) {
            return 0;
        }
        try {
            return res.getDimensionPixelSize(id);
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "尺寸像素大小未找到, id=" + id, e);
            return 0;
        }
    }

    // ===============================
    // 六、布尔 & 整型资源
    // ===============================

    /**
     * 安全获取布尔资源，资源不存在时返回 false，不崩溃。
     *
     * <p>示例：
     * <pre>
     * boolean debug = ResHelper.getBoolean(this, R.bool.debug);
     * // R.bool.debug = true  → 返回 true
     * // 不存在              → 返回 false
     * if (debug) {
     *     enableLogging();
     * }
     * </pre>
     *
     * @param context 上下文
     * @param id      布尔资源 ID
     * @return 布尔值，异常时返回 false
     */
    public static boolean getBoolean(Context context, @BoolRes int id) {
        Resources res = getResources(context);
        if (res == null) {
            return false;
        }
        try {
            return res.getBoolean(id);
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "布尔值未找到, id=" + id, e);
            return false;
        }
    }

    /**
     * 安全获取整型资源，资源不存在时返回 0，不崩溃。
     *
     * <p>示例：
     * <pre>
     * int limit = ResHelper.getInteger(this, R.integer.limit);
     * // R.integer.limit = 100  → 返回 100
     * // 不存在                 → 返回 0
     * for (int i = 0; i < limit; i++) { ... }
     * </pre>
     *
     * @param context 上下文
     * @param id      整型资源 ID
     * @return 整型值，异常时返回 0
     */
    public static int getInteger(Context context, @IntegerRes int id) {
        Resources res = getResources(context);
        if (res == null) {
            return 0;
        }
        try {
            return res.getInteger(id);
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "整型值未找到, id=" + id, e);
            return 0;
        }
    }
}
