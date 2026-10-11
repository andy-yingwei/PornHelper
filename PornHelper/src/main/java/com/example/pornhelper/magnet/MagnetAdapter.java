package com.example.pornhelper.magnet;

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
 * 磁力链接列表 Adapter（纯文字版，无图标加载）
 * 数据：List<Magnet>
 * UI 参数：MagnetConfig

 * 架构说明：
 * 使用 DiffUtil.ItemCallback + AsyncListDiffer 替代传统 notifyDataSetChanged

 * ======================
 * 使用范例：
 * ======================
 * <pre>
 * // 1. 开启日志（可选，建议在 Debug 环境开启）
 * MagnetAdapter.enableLog("Magnet");
 *
 * // 2. 构建配置（所有颜色使用 @ColorInt，尺寸单位见字段注释）
 * MagnetConfig cfg = new MagnetConfig.Builder()
 *
 *         // 卡片
 *         .itemBackground(0xFF000000)
 *         .itemCornerRadius(8)
 *
 *         // 标题
 *         .titleTextColor(0xFF938F99)
 *         .titleTextSize(14)
 *
 *         // 副信息
 *         .infoTextColor(0xFF938F99)
 *         .infoTextSize(10)
 *
 *         // 磁力药丸
 *         .magnetTextColor(0xFF938F99)
 *         .magnetTextSize(14)
 *         .pillStrokeColor(0xFF938F99)
 *         .pillStrokeWidth(1)
 *
 *         .build();
 *
 * // 3. 创建 Adapter（config 为 null 时自动使用默认配置）
 * MagnetAdapter adapter = new MagnetAdapter(magnetList, cfg);
 *
 * // 4. 设置点击监听
 * adapter.setListener((title, fileSize, downloads, url, pos) -> {
 *     // 处理点击事件，如复制磁力链接
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
public class MagnetAdapter extends RecyclerView.Adapter<MagnetAdapter.ViewHolder> {

    /* ==================== 日志控制 ==================== */

    /** 默认 tag */
    private static final String DEFAULT_TAG = "MagnetAdapter";

    /** DebugLog 实例 */
    private static DebugLog debugLog = new DebugLog(DEFAULT_TAG);

    /** 文件级开关，默认关闭 */
    private static boolean enabled = false;

    /**
     * 开启 MagnetAdapter 日志，传入自定义 tag，为 null 或空时自动使用默认 tag。
     * 日志受全局 DebugLog 开关和文件级开关双重控制。
     */
    public static void enableLog(String tag) {
        enabled = true;
        debugLog = new DebugLog((tag != null && !tag.trim().isEmpty()) ? tag : DEFAULT_TAG);
    }

    /**
     * 关闭 MagnetAdapter 文件级日志开关，不影响 DebugLog 全局开关。
     */
    public static void disableLog() {
        enabled = false;
    }

    /* ==================== DiffUtil + AsyncListDiffer ==================== */

    /**
     * DiffUtil 回调，负责判断两个 Magnet 是否为同一条数据以及内容是否相同。
     * areItemsTheSame 使用 url 作为唯一标识判断是否为同一项。
     * areContentsTheSame 调用 Magnet.isContentSame() 判断内容是否发生变化。
     */
    private static final DiffUtil.ItemCallback<Magnet> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Magnet>() {

                /**
                 * 判断是否为同一条数据，使用 url 字段做唯一标识。
                 * url 不为空且相等则认为是同一项。
                 */
                @Override
                public boolean areItemsTheSame(@NonNull Magnet oldItem, @NonNull Magnet newItem) {
                    if (oldItem.getUrl() != null) {
                        return oldItem.getUrl().equals(newItem.getUrl());
                    }
                    // url 都为 null 时，回退到 equals 或对象地址比较
                    return oldItem.equals(newItem);
                }

                /**
                 * 判断同一项的内容是否发生变化，
                 * 委托给 Magnet.isContentSame() 进行全字段比对。
                 */
                @Override
                public boolean areContentsTheSame(@NonNull Magnet oldItem, @NonNull Magnet newItem) {
                    return oldItem.isContentSame(newItem);
                }
            };

    /** AsyncListDiffer 实例，替代原有的 magnetList 直接持有 */
    private final AsyncListDiffer<Magnet> differ;

    /* ==================== 数据与配置 ==================== */

    private final MagnetConfig config;
    private ItemListener itemListener;

    /**
     * 唯一构造器。config 为 null 时自动使用全套默认 MagnetConfig。
     * 内部初始化 AsyncListDiffer 并提交初始数据列表。
     */
    public MagnetAdapter(List<Magnet> magnetList, MagnetConfig config) {
        this.config = (config == null) ? new MagnetConfig.Builder().build() : config;
        differ = new AsyncListDiffer<>(this, DIFF_CALLBACK);
        differ.submitList(magnetList == null ? new ArrayList<>() : new ArrayList<>(magnetList));
    }

    /* ==================== ViewHolder ==================== */

    /**
     * ViewHolder 持有 item 布局中的各个 View 引用，
     * 包括 CardView、标题、副信息、磁力药丸文本。
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView itemView;
        TextView titleView;
        TextView infoView;
        TextView magnetTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView.findViewById(R.id.item);
            titleView = itemView.findViewById(R.id.titleView);
            infoView = itemView.findViewById(R.id.infoView);
            magnetTextView = itemView.findViewById(R.id.magnetTextView);
        }
    }

    /**
     * 创建 ViewHolder，加载 adapter_magnet 布局并实例化 ViewHolder。
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_magnet, parent, false);
        return new ViewHolder(view);
    }

    /**
     * 绑定数据到 ViewHolder。设置标题、副信息拼接显示、
     * Card 背景圆角、文字样式、磁力药丸描边、点击事件等。
     * 所有逻辑与原版完全一致。
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Context ctx = holder.itemView.getContext();
        Magnet magnet = differ.getCurrentList().get(position);

        // 日志：绑定数据
        if (enabled && DebugLog.isLogEnabled()) {
            debugLog.d(getClass().getSimpleName() +"绑定数据 位置=" + position + "  数据=" + magnet.toString());
        }

        // ---------------- 文本绑定 ----------------
        holder.titleView.setText(magnet.getTitle());

        // ---------------- 副信息（size · downloads）----------------
        String size = magnet.getFileSize();
        String dl = magnet.getDownloads();
        boolean sizeEmpty = size == null || size.trim().isEmpty();
        boolean dlEmpty = dl == null || dl.trim().isEmpty();

        if (sizeEmpty && dlEmpty) {
            holder.infoView.setVisibility(View.GONE);
        } else {
            String sep = "   ";
            StringBuilder sb = new StringBuilder();
            if (!sizeEmpty) {
                sb.append(size.trim());
            }
            if (!sizeEmpty && !dlEmpty) {
                sb.append(sep);
            }
            if (!dlEmpty) {
                sb.append(dl.trim());
            }
            holder.infoView.setVisibility(View.VISIBLE);
            holder.infoView.setText(sb.toString());
        }

        holder.magnetTextView.setText(R.string.magnet_text);

        // ---------------- Card 背景 / 圆角 ----------------
        holder.itemView.setCardBackgroundColor(config.itemBackground);
        holder.itemView.setRadius(dp2px(ctx, config.itemCornerRadius));

        // ---------------- 标题 ----------------
        holder.titleView.setTextColor(config.titleTextColor);
        holder.titleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, config.titleTextSize);

        // ---------------- 副信息样式 ----------------
        holder.infoView.setTextColor(config.infoTextColor);
        holder.infoView.setTextSize(TypedValue.COMPLEX_UNIT_SP, config.infoTextSize);

        // ---------------- 磁力药丸 ----------------
        holder.magnetTextView.setTextColor(config.magnetTextColor);
        holder.magnetTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, config.magnetTextSize);
        if (holder.magnetTextView.getBackground() instanceof GradientDrawable gd) {
            gd.setStroke(dp2px(ctx, config.pillStrokeWidth), config.pillStrokeColor);
        }

        // ---------------- 点击 ----------------
        holder.magnetTextView.setOnClickListener(v -> {
            int pos = holder.getBindingAdapterPosition();
            Magnet mag = differ.getCurrentList().get(pos);
            if (enabled && DebugLog.isLogEnabled()) {
                debugLog.d(getClass().getSimpleName() +"点击了 位置=" + pos + "  数据=" + mag.toLogString());
            }
            if (pos != RecyclerView.NO_POSITION && itemListener != null) {
                itemListener.click(mag, pos);
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
    public void updateData(List<Magnet> list) {
        differ.submitList(list == null ? new ArrayList<>() : new ArrayList<>(list));
    }

    /* ==================== 接口 ==================== */

    /**
     * 点击监听接口，定义磁力药丸被点击时的回调方法。
     */
    public interface ItemListener {

        /**
         * 点击磁力药丸时触发的回调。
         *
         * @param magnet 磁力对象
         * @param position 点击位置
         */
        void click(Magnet magnet, int position);
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
