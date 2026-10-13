package com.example.pornhelper.cast;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

import java.util.Map;

/**
 * ============================================================
 * CastAdapter —— 演员/角色横向列表 Adapter（三合一融合 + 药丸描边最终版）
 * ============================================================
 *
 * <p><b>合并来源：</b></p>
 * <ul>
 *   <li>CastAvatarAdapter — 普通 Glide 加载</li>
 *   <li>CastAvatarAesAdapter — AES 解密加载</li>
 *   <li>CastAvatarCookieHeaderAdapter — Cookie/Header 鉴权加载</li>
 * </ul>
 *
 * <p><b>头像加载优先级：</b> AES → Cookie/Header → 普通 Glide</p>
 * <p><b>外观：</b> CastConfig 完全驱动（含药丸描边，不依赖 XML drawable）</p>
 * <p><b>鉴权：</b> setter 注入，与 ActorProfileView 对齐</p>
 *
 * @see CastConfig
 * @see Cast
 */
public class CastAdapter extends ListAdapter<Cast, CastAdapter.ViewHolder> {

    // ============================================================
    // DiffUtil（复用 Cast 实体类已有方法）
    // ============================================================
    private static final DiffUtil.ItemCallback<Cast> DIFF_CALLBACK = new DiffUtil.ItemCallback<Cast>() {

        @Override
        public boolean areItemsTheSame(@NonNull Cast oldItem, @NonNull Cast newItem) {
            String oldUrl = oldItem.getUrl();
            String newUrl = newItem.getUrl();
            if (!TextUtils.isEmpty(oldUrl) && !TextUtils.isEmpty(newUrl)) {
                return oldUrl.equals(newUrl);
            }
            return oldItem.equals(newItem);
        }

        @Override
        public boolean areContentsTheSame(@NonNull Cast oldItem, @NonNull Cast newItem) {
            return oldItem.isContentSame(newItem);
        }
    };

    // ============================================================
    // 日志
    // ============================================================
    private static final String DEFAULT_TAG = "CastAdapter";
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
    private final CastConfig config;

    // ============================================================
    // AES Option 缓存
    // ============================================================
    @Nullable
    private AesOption aesOption;

    // ============================================================
    // 点击监听
    // ============================================================
    @Nullable
    private OnCastClickListener listener;

    // ============================================================
    // 构造
    // ============================================================

    public CastAdapter(@NonNull CastConfig config) {
        super(DIFF_CALLBACK);
        this.config = config;
    }

    // ============================================================
    // 鉴权参数 setter（互斥设计）
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
    }

    /** 清除所有鉴权，回退普通模式 */
    public void clearAuthParams() {
        this.aesKey = null;
        this.aesIv = null;
        this.cookies = null;
        this.headers = null;
        this.aesOption = null;
    }

    // ============================================================
    // 点击监听
    // ============================================================

    public void setListener(@Nullable OnCastClickListener listener) {
        this.listener = listener;
    }

    // ============================================================
    // ListAdapter
    // ============================================================

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_cast, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Cast cast = getItem(position);
        if (cast == null) return;

        Context ctx = holder.itemView.getContext();

        // ---- 容器外观 ----
        CardView cardView = (CardView) holder.itemView;
        cardView.setCardBackgroundColor(config.background);
        cardView.setRadius(dp2px(ctx, config.backgroundRadius));

        // ---- 头像尺寸 ----
        int avatarPx = dp2px(ctx, config.avatarSize);
        ViewGroup.LayoutParams lp = holder.avatarView.getLayoutParams();
        lp.width = avatarPx;
        lp.height = avatarPx;
        holder.avatarView.setLayoutParams(lp);
        holder.avatarView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        // ---- 头像加载（三路分发）----
        loadAvatar(holder, cast, ctx);

        // ---- 文字 ----
        holder.castView.setText(cast.getName());
        holder.castView.setTextSize(android.util.TypedValue.COMPLEX_UNIT_SP, config.nameTextSize);
        holder.castView.setTextColor(config.nameTextColor);

        // ---- 药丸描边背景（完全由 Config 生成，不依赖 XML）----
        applyPillBackground(ctx, holder);

        // ---- 点击 ----
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                int pos = holder.getBindingAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    listener.onClick(cast, pos);
                }
            }
        });
    }

    // ============================================================
    // 药丸背景：完全由 CastConfig 驱动
    // ============================================================

    private void applyPillBackground(@NonNull Context ctx, @NonNull ViewHolder holder) {
        GradientDrawable pill = new GradientDrawable();
        pill.setShape(GradientDrawable.RECTANGLE);
        pill.setColor(Color.TRANSPARENT); // 内部透明
        pill.setCornerRadius(dp2px(ctx, 999)); // 药丸圆角
        pill.setStroke(dp2px(ctx, config.strokeWidth), config.strokeColor); // 描边
        holder.castView.setBackground(pill);
    }

    // ============================================================
    // 核心：三路头像加载分发
    // ============================================================

    private void loadAvatar(@NonNull ViewHolder holder, @NonNull Cast cast, @NonNull Context ctx) {
        String avatarUrl = cast.getAvatar();

        // ---- 头像地址无效 → 隐藏 ----
        if (TextUtils.isEmpty(avatarUrl)) {
            holder.avatarView.setVisibility(View.GONE);
            if (enabled && DebugLog.isLogEnabled()) {
                debugLog.w("加载头像: 跳过，avatarUrl 为空");
            }
            return;
        }

        // ==================== 路径 1：AES ====================
        if (!TextUtils.isEmpty(aesKey) && !TextUtils.isEmpty(aesIv)) {
            if (enabled && DebugLog.isLogEnabled()) {
                debugLog.d("加载头像: 模式=AES");
            }

            holder.avatarView.setVisibility(View.VISIBLE);
            try {
                LoadOption option = new LoadOption()
                        .forList()
                        .placeholder(config.avatarPlaceholder)
                        .error(config.avatarError)
                        .round(config.avatarRadius);
                option.skipMemoryCache(config.skipMemoryCache);
                option.diskCacheStrategy(config.diskCacheStrategy);
                assert aesOption != null;
                ImageLoader.with(ctx).loadAesBitmap(avatarUrl, holder.avatarView, option, aesOption);
            } catch (Exception e) {
                if (enabled && DebugLog.isLogEnabled()) {
                    debugLog.e("加载头像: AES加载失败 url=" + avatarUrl, e);
                }
                holder.avatarView.setImageResource(config.avatarError);
            }
            return;
        }

        // ==================== 路径 2：Cookie/Header（有其一即可）====================
        if ((cookies != null && !cookies.isEmpty()) || (headers != null && !headers.isEmpty())) {
            if (enabled && DebugLog.isLogEnabled()) {
                debugLog.d("加载头像: 模式=Cookie/Header");
            }

            holder.avatarView.setVisibility(View.VISIBLE);
            try {
                LoadOption option = new LoadOption()
                        .forList()
                        .placeholder(config.avatarPlaceholder)
                        .error(config.avatarError)
                        .round(config.avatarRadius);
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

                ImageLoader.with(ctx).load(avatarUrl, holder.avatarView, option);
            } catch (Exception e) {
                if (enabled && DebugLog.isLogEnabled()) {
                    debugLog.e("加载头像: Cookie/Header加载失败 url=" + avatarUrl, e);
                }
                holder.avatarView.setImageResource(config.avatarError);
            }
            return;
        }

        // ==================== 路径 3：普通 Glide ====================
        if (enabled && DebugLog.isLogEnabled()) {
            debugLog.d("加载头像: 模式=普通");
        }

        holder.avatarView.setVisibility(View.VISIBLE);
        try {
            RequestOptions options = new RequestOptions().transform(new CenterCrop());
            if (config.avatarRadius > 0) {
                options = options.transform(
                        new CenterCrop(),
                        new RoundedCorners(dp2px(ctx, config.avatarRadius))
                );
            }
            Glide.with(ctx)
                    .load(avatarUrl)
                    .apply(options)
                    .placeholder(config.avatarPlaceholder)
                    .error(config.avatarError)
                    .skipMemoryCache(config.skipMemoryCache)
                    .diskCacheStrategy(config.diskCacheStrategy)
                    .into(holder.avatarView);
        } catch (Exception e) {
            if (enabled && DebugLog.isLogEnabled()) {
                debugLog.e("加载头像: 普通加载失败 url=" + avatarUrl, e);
            }
            holder.avatarView.setImageResource(config.avatarError);
        }
    }

    // ============================================================
    // 工具
    // ============================================================

    private int dp2px(@NonNull Context ctx, int dp) {
        return Math.round(dp * ctx.getResources().getDisplayMetrics().density);
    }

    // ============================================================
    // ViewHolder（不再持有 config 引用，背景由 bind 时生成）
    // ============================================================

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView avatarView;
        final TextView castView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            avatarView = itemView.findViewById(R.id.avatarView);
            castView = itemView.findViewById(R.id.castView);
        }
    }

    // ============================================================
    // 点击接口
    // ============================================================

    public interface OnCastClickListener {
        void onClick(@NonNull Cast cast, int position);
    }
}
