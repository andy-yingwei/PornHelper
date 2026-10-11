package com.example.pornhelper.playSource;

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
 * 播放源 / 清晰度条 Adapter（CardView 容器 + TextView 标签）
 * 数据：List<PlaySource>
 * UI 参数：PlaySourceConfig

 * 设计约定：
 * 1. XML 给底线，Config 给覆盖，Adapter 做合并
 * 2. Config 为 null 时，使用默认 PlaySourceConfig（Builder 全默认值）
 * 3. 选中态由 selectedPosition 决定，文字色 / TextView 描边均允许 Config 覆盖
 * 4. 描边画在 TextView 上（GradientDrawable.stroke），不碰 CardView
 * 5. 药丸圆角固定 999dp，不读 CardView 的 itemCornerRadius

 * ======================
 * 日志规则：
 * ======================
 * 1. DebugLog 全局开关：DebugLog.enableLog()
 * 2. PlaySourceAdapter 文件级开关：PlaySourceAdapter.enableLog(tag)
 * 3. 两者都为 true 才打印
 * 4. 所有日志都走 DebugLog 的方法（d / e / w ...）

 * ======================
 * 架构说明：
 * ======================
 * 使用 DiffUtil.ItemCallback + AsyncListDiffer 替代传统 notifyDataSetChanged
 * 其余逻辑与原版完全一致

 * ======================
 * 使用范例：
 * ======================
 * <pre>
 * // 1. 开启日志（可选，建议在 Debug 环境开启）
 * PlaySourceAdapter.enableLog("PlaySource");
 *
 * // 2. 构建配置（所有颜色使用 @ColorInt，尺寸单位见字段注释）
 * PlaySourceConfig cfg = new PlaySourceConfig.Builder()
 *
 *         // 卡片
 *         .itemBackground(0xFF000000)
 *         .itemCornerRadius(8)
 *
 *         // 文字
 *         .textSize(14)
 *
 *         // 未选中
 *         .unselectedTextColor(0xFF938F99)
 *         .unselectedStrokeColor(0xFF938F99)
 *
 *         // 选中
 *         .selectedTextColor(0xFF000000)
 *         .selectedStrokeColor(0xFF000000)
 *
 *         // 描边
 *         .strokeWidth(1)
 *
 *         .build();
 *
 * // 3. 创建 Adapter（config 为 null 时自动使用默认配置）
 * PlaySourceAdapter adapter = new PlaySourceAdapter(sourceList, cfg);
 *
 * // 4. 设置点击监听
 * adapter.setListener((label, duration, selected, url, pos) -> {
 *     // 处理点击事件，如切换播放源
 * });
 *
 * // 5. 绑定到 RecyclerView
 * recyclerView.setAdapter(adapter);
 *
 * // 6. 数据更新（全量 / 单条，DiffUtil 自动计算差异并局部刷新）
 * adapter.updateData(newList);
 * adapter.refreshPositionData(position);
 *
 * // 7. 外部设置选中项
 * adapter.setSelectedPosition(2);
 * </pre>
 */
public class PlaySourceAdapter extends RecyclerView.Adapter<PlaySourceAdapter.ViewHolder> {

    /* ==================== 日志控制 ==================== */

    /** 默认 tag */
    private static final String DEFAULT_TAG = "PlaySourceAdapter";

    /** DebugLog 实例 */
    private static DebugLog debugLog = new DebugLog(DEFAULT_TAG);

    /** 文件级开关，默认关闭 */
    private static boolean enabled = false;

    /**
     * 开启 PlaySourceAdapter 日志，传入自定义 tag，为 null 或空时自动使用默认 tag。
     * 日志受全局 DebugLog 开关和文件级开关双重控制。
     */
    public static void enableLog(String tag) {
        enabled = true;
        debugLog = new DebugLog((tag != null && !tag.trim().isEmpty()) ? tag : DEFAULT_TAG);
    }

    /**
     * 关闭 PlaySourceAdapter 日志，不影响 DebugLog 全局开关。
     */
    public static void disableLog() {
        enabled = false;
    }

    /* ==================== DiffUtil + AsyncListDiffer ==================== */

    /**
     * DiffUtil 回调，负责判断两个 PlaySource 是否为同一条数据以及内容是否相同。
     * areItemsTheSame 使用 url 作为唯一标识判断是否为同一项。
     * areContentsTheSame 委托给 PlaySource.isContentSame() 进行全字段比对。
     */
    private static final DiffUtil.ItemCallback<PlaySource> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<PlaySource>() {

                /**
                 * 判断是否为同一条数据，使用 url 字段做唯一标识。
                 * url 不为空且相等则认为是同一项。
                 */
                @Override
                public boolean areItemsTheSame(@NonNull PlaySource oldItem, @NonNull PlaySource newItem) {
                    if (oldItem.getUrl() != null && newItem.getUrl() != null) {
                        return oldItem.getUrl().equals(newItem.getUrl());
                    }
                    // url 都为 null 时，回退到对象地址比较
                    return oldItem == newItem;
                }

                /**
                 * 判断同一项的内容是否发生变化，
                 * 委托给 PlaySource.isContentSame() 进行全字段比对。
                 */
                @Override
                public boolean areContentsTheSame(@NonNull PlaySource oldItem, @NonNull PlaySource newItem) {
                    return oldItem.isContentSame(newItem);
                }
            };

    /** AsyncListDiffer 实例，替代原有的 playSourceList 直接持有 */
    private final AsyncListDiffer<PlaySource> differ;

    /* ==================== 数据与配置 ==================== */

    private final PlaySourceConfig config;
    private ItemListener itemListener;

    /** 当前选中序号（唯一真相） */
    private int selectedPosition = 0;

    /**
     * 唯一构造器。config 为 null 时自动使用全套默认 PlaySourceConfig。
     * 内部初始化 AsyncListDiffer 并提交初始数据列表。
     * selectedPosition 做合法性保护。
     */
    @SuppressLint("NotifyDataSetChanged")
    public PlaySourceAdapter(List<PlaySource> playSourceList, PlaySourceConfig config) {
        this.config = (config == null) ? new PlaySourceConfig.Builder().build() : config;
        differ = new AsyncListDiffer<>(this, DIFF_CALLBACK);

        final int initPos; // ← 声明为 final
        if (playSourceList != null) {
            int temp = 0;
            for (int i = 0; i < playSourceList.size(); i++) {
                if (playSourceList.get(i).isSelected()) {
                    temp = i;
                    break;
                }
            }
            initPos = temp; // ← final 只赋值一次
        } else {
            initPos = 0;
        }

        List<PlaySource> list = playSourceList == null ? new ArrayList<>() : new ArrayList<>(playSourceList);
        differ.submitList(list, () -> {
            this.selectedPosition = safePosition(initPos);
            notifyDataSetChanged();
        });
    }
    /* ==================== ViewHolder ==================== */

    /**
     * ViewHolder 持有 item 布局中的各个 View 引用，
     * 包括 CardView 容器和标签文本 TextView。
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView item;
        TextView labelView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            item = itemView.findViewById(R.id.item);
            labelView = itemView.findViewById(R.id.labelView);
        }
    }

    /**
     * 创建 ViewHolder，加载 adapter_play_source 布局并实例化 ViewHolder。
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_play_source, parent, false);
        return new ViewHolder(v);
    }

    /* ==================== 绑定数据 ==================== */

    /**
     * 绑定数据到 ViewHolder。设置 Card 背景圆角、标签文字样式、
     * 选中态样式（文字色 + 描边）、点击事件等。
     * 所有逻辑与原版完全一致。
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Context ctx = holder.itemView.getContext();
        PlaySource ps = differ.getCurrentList().get(position);

        // 日志：绑定数据（受全局 + 文件级开关双重控制）
        if (enabled && DebugLog.isLogEnabled()) {
            debugLog.d(getClass().getSimpleName() +"绑定数据 位置=" + position + "  数据=" + ps.toLogString());
        }

        // ---- Card 背景 / 圆角（容器层，不画描边）----
        holder.item.setCardBackgroundColor(config.itemBackground);
        if (config.itemCornerRadius >= 0) {
            holder.item.setRadius(dp2px(ctx, config.itemCornerRadius));
        }

        // ---- 文字内容 / 大小 ----
        holder.labelView.setText(ps.getLabel());
        if (config.textSize > 0) {
            holder.labelView.setTextSize(TypedValue.COMPLEX_UNIT_SP, config.textSize);
        }

        // ---- TextView 样式（选中态由 position 决定）----
        applyLabelStyle(ctx, holder.labelView, position == selectedPosition);

        // ---- 点击：更新选中序号 ----
        holder.item.setClickable(true);
        holder.item.setOnClickListener(v -> {
            int pos = holder.getBindingAdapterPosition();
            PlaySource playSource = differ.getCurrentList().get(pos);
            if (pos == RecyclerView.NO_POSITION) return;
            // 日志：点击事件
            if (enabled && DebugLog.isLogEnabled()) {
                debugLog.d(getClass().getSimpleName() +"点击了 位置=" + pos + "  数据=" + playSource.toLogString());
            }

            setSelectedPositionInner(pos);
        });
    }

    /**
     * TextView 样式合并：
     * 优先级：Config > XML 默认（bg_pill_outline 药丸）
     * 选中 / 未选：文字色 + 描边色来自 PlaySourceConfig
     * 药丸圆角固定 999dp
     */
    private void applyLabelStyle(Context ctx, TextView tv, boolean selected) {

        // 文字色
        tv.setTextColor(
                selected ? config.selectedTextColor : config.unselectedTextColor
        );

        // TextView 背景 + 描边（纯 GradientDrawable）
        GradientDrawable d = new GradientDrawable();
        d.setShape(GradientDrawable.RECTANGLE);
        d.setColor(0x00000000); // 透明底
        d.setStroke(
                dp2px(ctx, config.strokeWidth),
                selected ? config.selectedStrokeColor : config.unselectedStrokeColor
        );
        // 药丸：和 bg_pill_outline.xml 完全一致
        d.setCornerRadius(dp2px(ctx, 999));
        tv.setBackground(d);
    }

    /**
     * 返回当前列表中的数据总数，供 RecyclerView 确定 item 个数。
     */
    @Override
    public int getItemCount() {
        return differ.getCurrentList().size();
    }

    /* ==================== 选中序号 API ==================== */

    /**
     * 获取当前选中序号。
     */
    public int getSelectedPosition() {
        return selectedPosition;
    }

    /**
     * 外部设置选中序号，非法值自动回退到 0。
     */
    public void setSelectedPosition(int pos) {
        setSelectedPositionInner(pos);
    }

    /**
     * 内部统一入口，处理选中态切换、刷新和回调分发。
     * 同一位置时仍保证 UI 正确并触发回调（覆盖首次绑定场景）。
     */
    private void setSelectedPositionInner(int pos) {
        int target = safePosition(pos);
        if (target == selectedPosition) {
            notifyItemChanged(target);  // 只刷新当前项
            dispatchClick(differ.getCurrentList().get(target), target);
            return;
        }

        int old = selectedPosition;
        selectedPosition = target;

        notifyItemChanged(old);
        notifyItemChanged(selectedPosition);
        dispatchClick(differ.getCurrentList().get(selectedPosition), selectedPosition);
    }

    /**
     * 序号保护：列表为空或序号越界时一律回退到 0。
     */
    private int safePosition(int pos) {
        List<PlaySource> current = differ.getCurrentList();
        if (current.isEmpty()) {
            return 0;
        }
        if (pos < 0 || pos >= current.size()) {
            return 0;
        }
        return pos;
    }

    /* ==================== 数据更新 ==================== */

    /**
     * 更新全部数据。通过 AsyncListDiffer.submitList 提交新列表，
     * DiffUtil 自动计算差异并局部刷新。
     * 同时重新校验 selectedPosition 合法性。
     */
    @SuppressLint("NotifyDataSetChanged")
    public void updateData(List<PlaySource> list) {
        List<PlaySource> newList = list == null ? new ArrayList<>() : new ArrayList<>(list);

        final int initPos;
        int temp = 0;
        for (int i = 0; i < newList.size(); i++) {
            if (newList.get(i).isSelected()) {
                temp = i;
                break;
            }
        }
        initPos = temp;

        differ.submitList(newList, () -> {
            this.selectedPosition = safePosition(initPos);
            notifyDataSetChanged();
        });
    }

    /**
     * 更新指定位置的数据，通过 notifyItemChanged 刷新单项。
     * position 越界时不执行任何操作。
     */
    public void refreshPositionData(int position) {
        List<PlaySource> current = differ.getCurrentList();
        if (position >= 0 && position < current.size()) {
            notifyItemChanged(position);
        }
    }

    /* ==================== 回调 ==================== */

    /**
     * 点击监听接口，定义播放源被点击时的回调方法。
     */
    public interface ItemListener {

        /**
         * 点击播放源时触发的回调。
         * @param playSource 数据
         * @param position 点击位置
         */
        void click(PlaySource playSource, int position);
    }

    /**
     * 设置点击监听，传入实现 ItemListener 接口的对象。
     */
    public void setListener(ItemListener l) {
        this.itemListener = l;
    }

    /**
     * 分发点击事件给监听者，数据或监听为 null 时不执行。
     */
    private void dispatchClick(PlaySource ps, int pos) {
        if (itemListener == null || ps == null) return;
        itemListener.click(ps, pos);
    }

    /* ==================== 工具方法 ==================== */

    /**
     * 将 dp 单位转换为 px，用于动态设置 stroke 宽度、圆角等。
     */
    private static int dp2px(Context ctx, int dp) {
        return (int) (dp * ctx.getResources().getDisplayMetrics().density + 0.5f);
    }
}
