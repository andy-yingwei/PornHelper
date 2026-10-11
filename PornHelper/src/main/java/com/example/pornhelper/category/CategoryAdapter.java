package com.example.pornhelper.category;

import android.content.Context;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.glidehelper.AesOption;
import com.example.glidehelper.ImageLoader;
import com.example.glidehelper.LoadOption;
import com.example.pornhelper.R;
import com.example.pornhelper.helper.DebugLog;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * ============================================================
 * CategoryAdapter —— 分类卡片列表 Adapter（三合一融合版）
 * ============================================================
 *
 * <p><b>合并来源：</b></p>
 * <ul>
 *   <li>CategoryCoverAdapter — 普通 Glide 加载</li>
 *   <li>CategoryCoverAesAdapter — AES 解密加载</li>
 *   <li>CategoryCoverCookieHeaderAdapter — Cookie/Header 鉴权加载</li>
 * </ul>
 *
 * <p><b>封面加载优先级：</b> AES → Cookie/Header → 普通 Glide</p>
 * <p><b>外观：</b> CategoryConfig 完全驱动</p>
 * <p><b>鉴权：</b> setter 注入，与 CastAdapter 对齐</p>
 *
 * @see CategoryConfig
 * @see Category
 */
public class CategoryAdapter extends ListAdapter<Category, CategoryAdapter.ViewHolder> {

    // ============================================================
    // DiffUtil（复用 Category 实体类已有方法）
    // ============================================================
    private static final DiffUtil.ItemCallback<Category> DIFF_CALLBACK = new DiffUtil.ItemCallback<Category>() {

        @Override
        public boolean areItemsTheSame(@NonNull Category oldItem, @NonNull Category newItem) {
            String oldTitle = oldItem.getTitle();
            String newTitle = newItem.getTitle();
            if (!TextUtils.isEmpty(oldTitle) && !TextUtils.isEmpty(newTitle)) {
                if (oldItem.getUrl() != null && newItem.getUrl() != null) {
                    return oldTitle.equals(newTitle) && oldItem.getUrl().equals(newItem.getUrl());
                }
                return oldTitle.equals(newTitle);
            }
            return oldItem.equals(newItem);
        }

        @Override
        public boolean areContentsTheSame(@NonNull Category oldItem, @NonNull Category newItem) {
            return oldItem.isContentSame(newItem);
        }
    };

    // ============================================================
    // 日志
    // ============================================================
    private static final String DEFAULT_TAG = "CategoryAdapter";
    private static DebugLog debugLog = new DebugLog(DEFAULT_TAG);
    private static boolean enabled = false;

    public static void enableLog(@Nullable String tag) {
        enabled = true;
        debugLog = new DebugLog((tag != null && !tag.trim().isEmpty()) ? tag : DEFAULT_TAG);
    }

    public static void disableLog() {
        enabled = false;
    }

    // ============================================================
    // 鉴权参数（setter 注入，默认全 null → 普通模式）
    // ============================================================
    @Nullable
    private String aesKey;
    @Nullable
    private String aesIv;
    @Nullable
    private Map<String, String> cookies;
    @Nullable
    private Map<String, String> headers;

    // ============================================================
    // UI 配置
    // ============================================================
    @NonNull
    private final CategoryConfig config;

    // ============================================================
    // AES Option 缓存
    // ============================================================
    @Nullable
    private AesOption aesOption;

    // ============================================================
    // 点击监听
    // ============================================================
    @Nullable
    private OnCategoryClickListener listener;

    // ============================================================
    // 构造
    // ============================================================

    public CategoryAdapter(@Nullable List<Category> list, CategoryConfig config) {
        super(DIFF_CALLBACK);
        // ---- config 兜底 ----
        if (config == null) {
            if (enabled && DebugLog.isLogEnabled()) {
                debugLog.w("CategoryConfig is null, using defaultConfig()");
            }
        }
        this.config = config != null ? config : CategoryConfig.defaultConfig();
        // ---- 数据兜底 ----
        if (list != null) {
            submitList(new ArrayList<>(list));
        } else {
            submitList(new ArrayList<>());
            if (enabled && DebugLog.isLogEnabled()) {
                debugLog.w("Category list is null, init empty list");
            }
        }
    }
    // ============================================================
    // 鉴权参数 setter（互斥设计 · 对齐 CastAdapter）
    // ============================================================

    /**
     * 设置 AES 解密参数，自动清除 Cookie/Header
     */
    public void setAesKeyIv(@Nullable String aesKey, @Nullable String aesIv) {
        this.aesKey = aesKey;
        this.aesIv = aesIv;
        this.cookies = null;
        this.headers = null;
        if (!TextUtils.isEmpty(aesKey) && !TextUtils.isEmpty(aesIv)) {
            this.aesOption = AesOption.create(aesKey, aesIv);
        } else {
            this.aesOption = null;
        }
        if (enabled && DebugLog.isLogEnabled()) {
            debugLog.d("setAesKeyIv: AES已设置，cookies/headers已清空");
        }
    }

    /**
     * 设置 Cookie + Header，自动清除 AES
     */
    public void setCookiesHeaders(@Nullable Map<String, String> cookies, @Nullable Map<String, String> headers) {
        this.cookies = cookies;
        this.headers = headers;
        this.aesKey = null;
        this.aesIv = null;
        this.aesOption = null;
        if (enabled && DebugLog.isLogEnabled()) {
            debugLog.d("setCookiesHeaders: Cookie/Header已设置，AES已清空");
        }
    }

    /** 清除所有鉴权，回退普通模式 */
    public void clearAuthParams() {
        this.aesKey = null;
        this.aesIv = null;
        this.cookies = null;
        this.headers = null;
        this.aesOption = null;
        if (enabled && DebugLog.isLogEnabled()) {
            debugLog.d("clearAuthParams: 所有鉴权参数已清除，回退普通模式");
        }
    }

    // ============================================================
    // 点击监听
    // ============================================================

    public void setListener(@Nullable OnCategoryClickListener listener) {
        this.listener = listener;
    }

    // ============================================================
    // ListAdapter
    // ============================================================

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_category, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Category category = getItem(position);
        if (category == null) return;

        Context ctx = holder.itemView.getContext();

        // ---- 容器外观 ----
        holder.cardView.setCardBackgroundColor(config.backgroundColor);
        holder.cardView.setRadius(dp2px(ctx, config.backgroundRadius));

        // ---- 标题 ----
        holder.titleView.setText(category.getTitle());
        holder.titleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, config.titleTextSize);
        holder.titleView.setTextColor(config.titleTextColor);

        // ---- 封面尺寸 ----
        if (config.coverHeight > 0) {
            ViewGroup.LayoutParams lp = holder.coverView.getLayoutParams();
            lp.height = dp2px(ctx, config.coverHeight);
            holder.coverView.setLayoutParams(lp);
        }

        // ---- 封面加载（三路分发）----
        loadCover(holder, category, ctx);

        // ---- 信息行动态拼接 ----
        buildInfo(ctx, holder, category);

        // ---- 点击 ----
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                int pos = holder.getBindingAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    listener.onClick(category, pos);
                }
            }
        });
    }

    // ============================================================
    // 信息行动态拼接（videoCount + rating）
    // ============================================================

    private void buildInfo(@NonNull Context ctx, @NonNull ViewHolder holder, @NonNull Category category) {
        List<String> parts = new ArrayList<>();
        if (!TextUtils.isEmpty(category.getVideoCount())) parts.add(category.getVideoCount());
        if (!TextUtils.isEmpty(category.getRating())) parts.add(category.getRating());

        for (int i = 0; i < parts.size(); i++) {
            TextView tv;
            if (i < holder.infoContainer.getChildCount()) {
                tv = (TextView) holder.infoContainer.getChildAt(i);
            } else {
                tv = new TextView(ctx);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
                tv.setLayoutParams(lp);
                tv.setGravity(Gravity.CENTER);
                tv.setMaxLines(1);
                tv.setEllipsize(TextUtils.TruncateAt.END);
                holder.infoContainer.addView(tv);
            }
            tv.setText(parts.get(i));
            tv.setTextColor(config.infoTextColor);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, config.infoTextSize);
            tv.setVisibility(View.VISIBLE);
        }

        for (int i = parts.size(); i < holder.infoContainer.getChildCount(); i++) {
            holder.infoContainer.getChildAt(i).setVisibility(View.GONE);
        }

        holder.infoContainer.setVisibility(parts.isEmpty() ? View.GONE : View.VISIBLE);
    }

    // ============================================================
    // 核心：三路封面加载分发
    // ============================================================

    private void loadCover(@NonNull ViewHolder holder, @NonNull Category category, @NonNull Context ctx) {
        String coverUrl = category.getCover();

        // ---- 封面地址无效 → 隐藏 ----
        if (TextUtils.isEmpty(coverUrl)) {
            holder.coverView.setVisibility(View.GONE);
            if (enabled && DebugLog.isLogEnabled()) {debugLog.w("加载头像: 跳过，coverUrl 为空");}
            return;
        }

        // ==================== 路径 1：AES ====================
        if (!TextUtils.isEmpty(aesKey) && !TextUtils.isEmpty(aesIv)) {
            if (enabled && DebugLog.isLogEnabled()) {debugLog.d("加载头像: 模式=AES");}

            holder.coverView.setVisibility(View.VISIBLE);
            try {
                LoadOption option = new LoadOption()
                        .forList()
                        .placeholder(config.coverPlaceholder)
                        .error(config.coverError)
                        .round(config.coverRoundRadius);
                option.skipMemoryCache(config.skipMemoryCache);
                option.diskCacheStrategy(config.diskCacheStrategy);
                assert aesOption != null;
                ImageLoader.with(ctx).loadAesBitmap(coverUrl, holder.coverView, option, aesOption);
            } catch (Exception e) {
                if (enabled && DebugLog.isLogEnabled()) {
                    debugLog.e("加载头像: AES加载失败 url=" + coverUrl, e);
                }
                holder.coverView.setImageResource(config.coverError);
            }
            return;
        }

        // ==================== 路径 2：Cookie/Header（有其一即可）====================
        if ((cookies != null && !cookies.isEmpty()) || (headers != null && !headers.isEmpty())) {
            if (enabled && DebugLog.isLogEnabled()) {debugLog.d("加载封面: 模式=Cookie/Header");}

            holder.coverView.setVisibility(View.VISIBLE);
            try {
                LoadOption option = new LoadOption()
                        .forList()
                        .placeholder(config.coverPlaceholder)
                        .error(config.coverError)
                        .round(config.coverRoundRadius);
                option.skipMemoryCache(config.skipMemoryCache);
                option.diskCacheStrategy(config.diskCacheStrategy);

                if (cookies != null && !cookies.isEmpty()) {
                    option.addCookies(cookies);
                    if (enabled && DebugLog.isLogEnabled()) {
                        debugLog.d("  添加 cookies: " + cookies.size() + " 条");
                    }
                }

                if (headers != null && !headers.isEmpty()) {
                    option.addHeaders(headers);
                    if (enabled && DebugLog.isLogEnabled()) {
                        debugLog.d("  添加 headers: " + headers.size() + " 条");
                    }
                }

                ImageLoader.with(ctx).load(coverUrl, holder.coverView, option);
            } catch (Exception e) {
                if (enabled && DebugLog.isLogEnabled()) {
                    debugLog.e("加载封面: Cookie/Header加载失败 url=" + coverUrl, e);
                }
                holder.coverView.setImageResource(config.coverError);
            }
            return;
        }

        // ==================== 路径 3：普通 Glide ====================
        if (enabled && DebugLog.isLogEnabled()) {debugLog.d("loadCover: 模式=普通");}

        holder.coverView.setVisibility(View.VISIBLE);
        try {
            RequestOptions options = new RequestOptions().transform(new CenterCrop());
            if (config.coverRoundRadius > 0) {
                options = options.transform(
                        new CenterCrop(),
                        new RoundedCorners(dp2px(ctx, config.coverRoundRadius))
                );
            }
            Glide.with(ctx)
                    .load(coverUrl)
                    .apply(options)
                    .placeholder(config.coverPlaceholder)
                    .error(config.coverError)
                    .skipMemoryCache(config.skipMemoryCache)
                    .diskCacheStrategy(config.diskCacheStrategy)
                    .into(holder.coverView);
        } catch (Exception e) {
            if (enabled && DebugLog.isLogEnabled()) {
                debugLog.e("加载头像: 普通加载失败 url=" + coverUrl, e);
            }
            holder.coverView.setImageResource(config.coverError);
        }
    }

    // ============================================================
    // 工具
    // ============================================================

    private int dp2px(@NonNull Context ctx, int dp) {
        return Math.round(dp * ctx.getResources().getDisplayMetrics().density);
    }

    // ============================================================
    // ViewHolder
    // ============================================================

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final CardView cardView;
        final ImageView coverView;
        final TextView titleView;
        final LinearLayout infoContainer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.item);
            coverView = itemView.findViewById(R.id.coverView);
            titleView = itemView.findViewById(R.id.titleView);
            infoContainer = itemView.findViewById(R.id.infoContainer);
        }
    }

    // ============================================================
    // 点击接口
    // ============================================================

    public interface OnCategoryClickListener {
        void onClick(@NonNull Category category, int position);
    }
}