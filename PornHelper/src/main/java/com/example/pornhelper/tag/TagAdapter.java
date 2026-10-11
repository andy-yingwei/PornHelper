package com.example.pornhelper.tag;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pornhelper.R;
import com.example.pornhelper.helper.DebugLog;

import java.util.ArrayList;
import java.util.List;

/**
 * 标签横向列表 Adapter（纯文字版，无图标加载）
 * 数据：List<Tag>
 * UI 参数：TagConfig

 * 设计约定：
 * 1. XML 给底线，Config 给覆盖，Adapter 做合并
 * 2. Config 为 null 时，使用默认 TagConfig（Builder 全默认值）
 * 3. 标签文字由 Tag.chineseName 提供
 * 4. 描边画在 TextView 上（GradientDrawable.stroke），不碰 CardView

 * ======================
 * 日志规则：
 * ======================
 * 1. DebugLog 全局开关：DebugLog.enableLog()
 * 2. TagAdapter 文件级开关：TagAdapter.enableLog(tag)
 * 3. 两者都为 true 才打印
 * 4. 所有日志都走 DebugLog 的方法（d / e / w ...）

 * ======================
 * 架构说明：
 * ======================
 * 使用 DiffUtil.ItemCallback + AsyncListDiffer 替代传统 notifyDataSetChanged

 * ======================
 * 使用范例：
 * ======================
 * <pre>
 * // 1. 开启日志（可选，建议在 Debug 环境开启）
 * TagAdapter.enableLog("Tag");
 *
 * // 2. 构建配置（所有颜色使用 @ColorInt，尺寸单位见字段注释）
 * TagConfig cfg = new TagConfig.Builder()
 *
 *         // 卡片
 *         .itemBackground(0xFF000000)
 *         .itemCornerRadius(0)
 *
 *         // 文字
 *         .textColor(0xFF938F99)
 *         .textSize(12)
 *
 *         // 描边
 *         .strokeColors(0xFF938F99)
 *         .strokeWidth(1)
 *
 *         .build();
 *
 * // 3. 创建 Adapter（config 为 null 时自动使用默认配置）
 * TagAdapter adapter = new TagAdapter(tagList, cfg);
 *
 * // 4. 设置点击监听
 * adapter.setListener((englishName, chineseName, videoCount, url, pos) -> {
 *     // 处理点击事件，如跳转标签详情页
 * });
 *
 * // 5. 绑定到 RecyclerView
 * recyclerView.setAdapter(adapter);
 *
 * // 6. 数据更新（全量 / 单条，DiffUtil 自动计算差异并局部刷新）
 * adapter.updateData(newList);
 * adapter.refreshPositionData(position);
 * </pre>
 */
public class TagAdapter extends RecyclerView.Adapter<TagAdapter.ViewHolder> {

    /* ==================== 日志控制 ==================== */

    /** 默认 tag */
    private static final String DEFAULT_TAG = "TagAdapter";

    /** DebugLog 实例 */
    private static DebugLog debugLog = new DebugLog(DEFAULT_TAG);

    /** 文件级开关，默认关闭 */
    private static boolean enabled = false;

    /**
     * 开启 TagAdapter 日志，传入自定义 tag，为 null 或空时自动使用默认 tag。
     * 日志受全局 DebugLog 开关和文件级开关双重控制。
     */
    public static void enableLog(String tag) {
        enabled = true;
        debugLog = new DebugLog((tag != null && !tag.trim().isEmpty()) ? tag : DEFAULT_TAG);
    }

    /**
     * 关闭 TagAdapter 日志，不影响 DebugLog 全局开关。
     */
    public static void disableLog() {
        enabled = false;
    }

    /* ==================== DiffUtil + AsyncListDiffer ==================== */

    /**
     * DiffUtil 回调，负责判断两个 Tag 是否为同一条数据以及内容是否相同。
     * areItemsTheSame 使用 url 作为唯一标识判断是否为同一项。
     * areContentsTheSame 委托给 Tag.isContentSame() 进行全字段比对。
     */
    private static final DiffUtil.ItemCallback<Tag> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Tag>() {

                /**
                 * 判断是否为同一条数据，使用 url 字段做唯一标识。
                 * url 不为空且相等则认为是同一项。
                 */
                @Override
                public boolean areItemsTheSame(@NonNull Tag oldItem, @NonNull Tag newItem) {
                    return oldItem.getUrl() != null
                            && oldItem.getUrl().equals(newItem.getUrl());
                }

                /**
                 * 判断同一项的内容是否发生变化，
                 * 委托给 Tag.isContentSame() 进行全字段比对。
                 */
                @Override
                public boolean areContentsTheSame(@NonNull Tag oldItem, @NonNull Tag newItem) {
                    return oldItem.isContentSame(newItem);
                }
            };

    /** AsyncListDiffer 实例，替代原有的 tagList 直接持有 */
    private final AsyncListDiffer<Tag> differ;

    /* ==================== 数据与配置 ==================== */

    private final TagConfig config;
    private ItemListener itemListener;

    /**
     * 唯一构造器。config 为 null 时自动使用全套默认 TagConfig。
     * 内部初始化 AsyncListDiffer 并提交初始数据列表。
     */
    public TagAdapter(List<Tag> tagList, TagConfig config) {
        this.config = (config == null) ? new TagConfig.Builder().build() : config;
        differ = new AsyncListDiffer<>(this, DIFF_CALLBACK);
        differ.submitList(tagList == null ? new ArrayList<>() : new ArrayList<>(tagList));
    }

    /* ==================== ViewHolder ==================== */

    /**
     * ViewHolder 持有 item 布局中的各个 View 引用，
     * 包括 CardView 和标签文本 TextView。
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView item;
        TextView tagView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            item = itemView.findViewById(R.id.item);
            tagView = itemView.findViewById(R.id.tagView);
        }
    }

    /**
     * 创建 ViewHolder，加载 adapter_tag 布局并实例化 ViewHolder。
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_tag, parent, false);
        return new ViewHolder(view);
    }

    /**
     * 绑定数据到 ViewHolder。设置 Card 背景圆角、标签文字样式、
     * 描边（GradientDrawable）、点击事件等。所有逻辑与原版完全一致。
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Context ctx = holder.itemView.getContext();
        Tag tag = differ.getCurrentList().get(position);

        // 日志：绑定数据
        if (enabled && DebugLog.isLogEnabled()) {
            debugLog.d(getClass().getSimpleName() +"绑定数据 位置=" + position + "  数据=" + tag.toLogString());
        }

        // ---------------- Card 背景 / 圆角 ----------------
        holder.item.setCardBackgroundColor(config.itemBackground);
        holder.item.setRadius(dp2px(ctx, config.itemCornerRadius));

        // ---------------- 文字 ----------------
        holder.tagView.setText(tag.getChineseName());
        holder.tagView.setTextColor(config.textColor);
        holder.tagView.setTextSize(TypedValue.COMPLEX_UNIT_SP, config.textSize);

        // ---------------- 描边（GradientDrawable）----------------
        if (holder.tagView.getBackground() instanceof GradientDrawable gd) {
            gd.setStroke(
                    dp2px(ctx, config.strokeWidth),
                    config.strokeColors
            );
        }

        // ---------------- 点击 ----------------
        holder.item.setOnClickListener(v -> {
            int pos = holder.getBindingAdapterPosition();
            Tag t = differ.getCurrentList().get(pos);
            // 日志：点击事件
            if (enabled && DebugLog.isLogEnabled()) {
                debugLog.d(getClass().getSimpleName() +"点击了 位置=" + pos + "  数据=" + t.toLogString());
            }
            if (pos != RecyclerView.NO_POSITION && itemListener != null) {
                itemListener.click(t, pos);
            }
        });
    }

    /**
     * 返回当前列表中的数据总数，供 RecyclerView 确定 item 个数。
     */
    @Override
    public int getItemCount() {
        return differ.getCurrentList().size();
    }

    /* ==================== 数据更新 ==================== */

    /**
     * 更新全部数据。通过 AsyncListDiffer.submitList 提交新列表，
     * DiffUtil 自动计算差异并局部刷新，无需手动调用 notifyDataSetChanged。
     */
    @SuppressLint("NotifyDataSetChanged")
    public void updateData(List<Tag> list) {
        differ.submitList(list == null ? new ArrayList<>() : new ArrayList<>(list));
    }

    /**
     * 更新指定位置的数据。通过重新提交当前列表副本触发 DiffUtil 比对刷新。
     * position 越界时不执行任何操作。
     */
    public void refreshPositionData(int position) {
        List<Tag> current = new ArrayList<>(differ.getCurrentList());
        if (position >= 0 && position < current.size()) {
            differ.submitList(current);
        }
    }

    /* ==================== 接口 ==================== */

    /**
     * 点击监听接口，定义标签被点击时的回调方法。
     */
    public interface ItemListener {

        /**
         * 点击标签时触发的回调。
         *
         * @param tag 标签实体类对象
         * @param position 点击位置
         */
        void click(Tag tag, int position);
    }

    /**
     * 设置点击监听，传入实现 ItemListener 接口的对象。
     */
    public void setListener(ItemListener l) {
        this.itemListener = l;
    }

    /* ==================== 工具方法 ==================== */

    /**
     * 将 dp 单位转换为 px，用于动态设置 stroke 宽度、圆角等。
     */
    private static int dp2px(Context ctx, int dp) {
        return (int) (dp * ctx.getResources().getDisplayMetrics().density + 0.5f);
    }
}