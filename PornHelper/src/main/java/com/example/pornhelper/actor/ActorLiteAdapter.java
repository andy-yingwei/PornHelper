package com.example.pornhelper.actor;

import android.content.Context;
import android.text.TextUtils;
import android.util.TypedValue;
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
import java.util.Objects;

/**
 * ============================================================
 * ActorLiteAdapter
 * ============================================================

 * ActorLite 列表适配器（ListAdapter + DiffUtil）
 * 支持卡片样式、头像加载（普通 / AES / Cookie-Header 三路分支）、
 * 名称显示、信息条（videoCount / views / rating / rank）、
 * 收藏图标、点击回调

 * 功能覆盖：
 *   - 卡片背景色、圆角
 *   - 头像加载三路分支（AES > Cookie/Header > 普通）
 *   - 名称文字样式
 *   - 信息条动态管理（favorite 标签跳过，TextView 复用/新建/隐藏）
 *   - 点击回调（OnItemClickListener）

 * 鉴权参数运行时互斥切换：
 *   - setAesKeyIv()        → AES 模式
 *   - setCookiesHeaders()  → Cookie/Header 模式
 *   - clearAuthParams()    → 普通模式

 * 布局：
 *   - R.layout.adapter_actor_lite（无头像布局）
 *   - 通过 setWithAvatar(boolean) 控制头像显隐

 * 日志：
 *   - 实例级 DebugLog + enabled 开关
 *   - 中文打印，格式对齐 FilmStudioAdapter

 * ============================================================
 * 使用范例
 * ============================================================

 * 1. 普通模式（无鉴权）：
 * <pre>
 * List<ActorLite> list = new ArrayList<>();
 * ActorLiteConfig config = ActorLiteConfig.defaultConfig(context);
 *
 * ActorLiteAdapter adapter = new ActorLiteAdapter(list, config);
 * adapter.setOnItemClickListener((item, position) -> {
 *     // 处理点击
 * });
 * recyclerView.setAdapter(adapter);
 *
 * // 更新数据
 * adapter.updateData(newList);
 * </pre>
 *
 * 2. AES 模式：
 * <pre>
 * ActorLiteAdapter adapter = new ActorLiteAdapter(list, config);
 * adapter.setAesKeyIv("key", "iv");  // 切换为 AES 加载
 * adapter.updateData(newList);
 * </pre>
 *
 * 3. Cookie/Header 模式：
 * <pre>
 * ActorLiteAdapter adapter = new ActorLiteAdapter(list, config);
 * Map<String, String> cookies = new HashMap<>();
 * Map<String, String> headers = new HashMap<>();
 * // cookies.put(...);
 * // headers.put(...);
 * adapter.setCookiesHeaders(cookies, headers);  // 切换为 Cookie/Header 加载
 * adapter.updateData(newList);
 * </pre>
 *
 * 4. 无头像布局：
 * <pre>
 * ActorLiteAdapter adapter = new ActorLiteAdapter(list, config);
 * adapter.setWithAvatar(false);  // 隐藏头像
 * recyclerView.setAdapter(adapter);
 * </pre>
 *
 * 5. 启用日志：
 * <pre>
 * ActorLiteAdapter adapter = new ActorLiteAdapter(list, config);
 * adapter.enableLog("ActorLite");
 * </pre>
 *
 * 6. 运行时切换鉴权模式（互斥）：
 * <pre>
 * // 从普通切换到 AES
 * adapter.setAesKeyIv("newKey", "newIv");
 *
 * // 从 AES 切换到 Cookie/Header
 * adapter.setCookiesHeaders(cookies, headers);
 *
 * // 清除所有鉴权，回退普通模式
 * adapter.clearAuthParams();
 * </pre>
 *
 * @author Merged
 * @date 2026-09-27
 * ============================================================
 */
public class ActorLiteAdapter extends ListAdapter<ActorLite, ActorLiteAdapter.ViewHolder> {

    /* ==================== DiffUtil ==================== */
    private static final DiffUtil.ItemCallback<ActorLite> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<ActorLite>() {
                @Override
                public boolean areItemsTheSame(@NonNull ActorLite oldItem, @NonNull ActorLite newItem) {
                    String n1 = oldItem.getName();
                    String n2 = newItem.getName();
                    if (n1 != null && n2 != null) {
                        if (oldItem.getUrl() != null && newItem.getUrl() != null) {
                            return n1.equals(n2) && oldItem.getUrl().equals(newItem.getUrl());
                        }
                        return n1.equals(n2);
                    }
                    return oldItem == newItem;
                }

                @Override
                public boolean areContentsTheSame(@NonNull ActorLite oldItem, @NonNull ActorLite newItem) {
                    return oldItem.isContentSame(newItem);
                }
            };

    /* ==================== 日志 ==================== */
    private final String DEFAULT_TAG = "ActorLiteMergedAdapter";
    private DebugLog debugLog = new DebugLog(DEFAULT_TAG);
    private boolean enabled = false;

    public void enableLog(String tag) {
        this.enabled = true;
        DebugLog.enableLog();
        debugLog = new DebugLog((tag != null && !tag.trim().isEmpty()) ? tag : DEFAULT_TAG);
    }

    public void disableLog() {
        this.enabled = false;
    }

    /* ==================== favorite刷新参数 ==================== */

    private String pendingName;
    private String pendingUrl;

    /* ==================== 鉴权参数 ==================== */
    private String aesKey;
    private String aesIv;
    private Map<String, String> cookies;
    private Map<String, String> headers;

    public void setAesKeyIv(String key, String iv) {
        this.aesKey = key;
        this.aesIv = iv;
        this.cookies = null;
        this.headers = null;
    }

    public void setCookiesHeaders(Map<String, String> cookies, Map<String, String> headers) {
        this.cookies = cookies;
        this.headers = headers;
        this.aesKey = null;
        this.aesIv = null;
    }

    public void clearAuthParams() {
        this.aesKey = null;
        this.aesIv = null;
        this.cookies = null;
        this.headers = null;
    }

    /* ==================== Config ==================== */
    private ActorLiteConfig config;

    private boolean withAvatar = true;

    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(ActorLite item, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    /* ==================== 构造 ==================== */

    public ActorLiteAdapter(@Nullable List<ActorLite> list, @Nullable ActorLiteConfig config) {
        super(DIFF_CALLBACK);
        this.config = config;
        this.debugLog = new DebugLog(DEFAULT_TAG);
        submitList(list == null ? new ArrayList<>() : new ArrayList<>(list));
    }

    public ActorLiteAdapter(@Nullable List<ActorLite> list) {
        this(list, null);
    }

    /* ==================== 数据更新 ==================== */
    public void updateData(@Nullable List<ActorLite> list) {
        int size = (list != null) ? list.size() : 0;
        if (enabled) debugLog.d(getClass().getSimpleName() + "更新数据 listSize=" + size);

        List<ActorLite> newList;
        if (list == null || list.isEmpty()) {
            newList = new ArrayList<>();
        } else {
            newList = new ArrayList<>(list);
        }

        submitList(newList, () -> {
        });
    }

    /* ==================== 无头像开关 ==================== */
    public void setWithAvatar(boolean withAvatar) {
        this.withAvatar = withAvatar;
    }

    /* ==================== ViewHolder 创建 ==================== */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_actor_lite, parent, false);

        ViewHolder holder = new ViewHolder(view);
        // 标记收藏图标，让 bindInfoContainer 跳过它
        holder.favoriteView.setTag("favorite");

        return holder;
    }

    /* ==================== 绑定 ==================== */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Context ctx = holder.itemView.getContext();
        ActorLite item = getItem(position);

        // config 延迟初始化
        if (config == null) {
            config = ActorLiteConfig.defaultConfig(ctx);
            if (enabled) debugLog.d("config 为 null，延迟初始化默认配置");
        }

        if (item == null) return;

        /* ---- 卡片 ---- */
        holder.card.setCardBackgroundColor(config.backgroundColor);
        if (config.backgroundRadius > 0) {
            holder.card.setRadius(dp2px(ctx, config.backgroundRadius));
        }

        /* ---- 名称 ---- */
        holder.nameView.setText(item.getName());
        holder.nameView.setTextColor(config.nameTextColor);
        holder.nameView.setTextSize(TypedValue.COMPLEX_UNIT_SP, config.nameTextSize);

        /* ---- 头像 ---- */
        if (withAvatar) {
            loadAvatar(holder, item, ctx);
        } else {
            holder.avatarView.setVisibility(View.GONE);
        }

        /* ---- 信息区（收藏图标 + 动态信息行 统一在这里管理） ---- */
        holder.infoContainer.setGravity(android.view.Gravity.CENTER_HORIZONTAL);
        bindInfoContainer(holder.infoContainer, ctx, item);

        /* ---- 点击 ---- */
        holder.card.setOnClickListener(v -> {
            int pos = holder.getBindingAdapterPosition();
            if (pos < 0) return;
            if (enabled) debugLog.d(getClass().getSimpleName() + "点击了 位置=" + pos + "  数据=" + item.toLogString());
            // 只记锚点，不持有对象
            pendingName = item.getName();
            pendingUrl = item.getUrl();
            if (listener != null) {
                listener.onItemClick(getItem(pos), pos);
            }
        });
    }

    /* ==================== 信息区（收藏图标 + 动态 TextView） ==================== */
    private void bindInfoContainer(LinearLayout container, Context ctx, ActorLite item) {
        List<String> infoList = new ArrayList<>();

        if (!TextUtils.isEmpty(item.getVideoCount())) infoList.add(item.getVideoCount());
        if (!TextUtils.isEmpty(item.getViews())) infoList.add(item.getViews());
        if (!TextUtils.isEmpty(item.getRating())) infoList.add(item.getRating());
        else if (!TextUtils.isEmpty(item.getRank())) infoList.add(item.getRank());

        // 第一步：处理收藏图标显隐（在 infoContainer 内的带 "favorite" tag 的 View）
        for (int i = 0; i < container.getChildCount(); i++) {
            View child = container.getChildAt(i);
            if (child.getTag() != null && "favorite".equals(child.getTag())) {
                if (item.isFavorite()) {
                    child.setVisibility(View.VISIBLE);
                    if (config != null) {
                        ((ImageView) child).setImageResource(config.favoriteIcon);
                        if (config.favoriteIconSize > 0) {
                            ViewGroup.LayoutParams lp = child.getLayoutParams();
                            lp.width = dp2px(ctx, config.favoriteIconSize);
                            lp.height = dp2px(ctx, config.favoriteIconSize);
                            child.setLayoutParams(lp);
                        }
                    }
                } else {
                    child.setVisibility(View.GONE);
                }
                break;
            }
        }

        // 第二步：收集已有的动态 TextView（排除 favoriteView）
        List<TextView> existingTextViews = new ArrayList<>();
        for (int i = 0; i < container.getChildCount(); i++) {
            View child = container.getChildAt(i);
            if (child.getTag() != null && "favorite".equals(child.getTag())) continue;
            if (child instanceof TextView) {
                existingTextViews.add((TextView) child);
            }
        }

        // 第三步：复用已有 TextView
        int reuseCount = Math.min(existingTextViews.size(), infoList.size());
        for (int i = 0; i < reuseCount; i++) {
            TextView tv = existingTextViews.get(i);
            tv.setText(infoList.get(i));
            tv.setTextColor(config.infoTextColor);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, config.infoTextSize);
            tv.setGravity(android.view.Gravity.CENTER_VERTICAL);
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) tv.getLayoutParams();
            if (lp != null) {
                lp.gravity = android.view.Gravity.CENTER_VERTICAL;
            }
            tv.setVisibility(View.VISIBLE);
        }

        // 第四步：不够就新建
        for (int i = reuseCount; i < infoList.size(); i++) {
            TextView tv = createInfoTextView(ctx, container);
            tv.setText(infoList.get(i));
            tv.setTextColor(config.infoTextColor);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, config.infoTextSize);
        }

        // 第五步：多余的 TextView 隐藏
        for (int i = infoList.size(); i < existingTextViews.size(); i++) {
            existingTextViews.get(i).setVisibility(View.GONE);
        }
    }

    private TextView createInfoTextView(Context ctx, LinearLayout container) {
        TextView tv = new TextView(ctx);
        tv.setGravity(android.view.Gravity.CENTER_VERTICAL);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMarginEnd(dp2px(ctx, 8));
        lp.gravity = android.view.Gravity.CENTER_VERTICAL;
        tv.setLayoutParams(lp);
        tv.setMaxLines(1);
        tv.setEllipsize(android.text.TextUtils.TruncateAt.END);
        container.addView(tv);
        return tv;
    }

    /* ==================== 头像加载（三路分支） ==================== */
    private void loadAvatar(@NonNull ViewHolder holder, ActorLite item, Context ctx) {
        String avatarUrl = item.getAvatar();

        if (TextUtils.isEmpty(avatarUrl)) {
            holder.avatarView.setVisibility(View.GONE);
            if (enabled) {
                debugLog.w(getClass().getSimpleName() + " 加载头像跳过(url无效) 位置="
                        + holder.getBindingAdapterPosition());
            }
            return;
        }

        holder.avatarView.setVisibility(View.VISIBLE);

        if (config.avatarHeight > 0) {
            ViewGroup.LayoutParams lp = holder.avatarView.getLayoutParams();
            lp.height = dp2px(ctx, config.avatarHeight);
            holder.avatarView.setLayoutParams(lp);
        }

        /* ===== 1. AES ===== */
        if (!TextUtils.isEmpty(aesKey) && !TextUtils.isEmpty(aesIv)) {

            if (enabled) debugLog.d(getClass().getSimpleName() + " 头像加载模式: AES");
            try {
                LoadOption option = new LoadOption()
                        .forList()
                        .placeholder(config.avatarPlaceholder)
                        .error(config.avatarError)
                        .round(config.avatarRoundRadius);
                option.skipMemoryCache(config.skipMemoryCache);
                option.diskCacheStrategy(config.diskCacheStrategy);

                ImageLoader.with(ctx).loadAesBitmap(
                        avatarUrl, holder.avatarView, option,
                        AesOption.create(aesKey, aesIv));
            } catch (Exception e) {
                if (enabled) debugLog.e(getClass().getSimpleName() + " AES加载头像错误", e);
                holder.avatarView.setImageResource(config.avatarError);
            }

            /* ===== 2. Cookie / Header（cookies 或 headers 有其一即可）===== */
        } else if ((cookies != null && !cookies.isEmpty()) || (headers != null && !headers.isEmpty())) {

            if (enabled) debugLog.d(getClass().getSimpleName() + " 头像加载模式: Cookie/Header");

            try {
                LoadOption option = new LoadOption()
                        .forList()
                        .placeholder(config.avatarPlaceholder)
                        .error(config.avatarError)
                        .round(config.avatarRoundRadius);
                option.skipMemoryCache(config.skipMemoryCache);
                option.diskCacheStrategy(config.diskCacheStrategy);

                if (cookies != null && !cookies.isEmpty()) {
                    option.addCookies(cookies);
                    if (enabled) debugLog.d("  添加 cookies: " + cookies.size() + " 条");
                }

                if (headers != null && !headers.isEmpty()) {
                    option.addHeaders(headers);
                    if (enabled) debugLog.d("  添加 headers: " + headers.size() + " 条");
                }

                ImageLoader.with(ctx).load(avatarUrl, holder.avatarView, option);
            } catch (Exception e) {
                if (enabled) debugLog.e(getClass().getSimpleName() + " Cookie/Header加载头像错误", e);
                holder.avatarView.setImageResource(config.avatarError);
            }

            /* ===== 3. 普通 ===== */
        } else {

            if (enabled) debugLog.d(getClass().getSimpleName() + " 头像加载模式: 普通");
            try {
                RequestOptions options = new RequestOptions().transform(new CenterCrop());
                if (config.avatarRoundRadius > 0) {
                    options = options.transform(
                            new CenterCrop(),
                            new RoundedCorners(dp2px(ctx, config.avatarRoundRadius)));
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
                if (enabled) debugLog.e(getClass().getSimpleName() + " 普通加载头像错误", e);
                holder.avatarView.setImageResource(config.avatarError);
            }
        }
    }

    /* ==================== 更新收藏图标 ==================== */
    /**
     * 从详情页返回后调用：翻转/同步收藏状态
     * @param newFavorite 查库后的真实收藏状态
     */
    public void applyFavorite(boolean newFavorite) {
        if (pendingName == null && pendingUrl == null) return;

        List<ActorLite> list = new ArrayList<>(getCurrentList());
        int target = -1;
        for (int i = 0; i < list.size(); i++) {
            ActorLite o = list.get(i);
            boolean nameSame = pendingName != null && pendingName.equals(o.getName());
            boolean urlSame = pendingUrl == null || Objects.equals(pendingUrl, o.getUrl());
            if (nameSame && urlSame) { target = i; break; }
        }

        if (target < 0) {
            if (enabled) debugLog.d("没有发现需要修改的条目，跳过");
            clearPending();
            return;
        }

        ActorLite old = list.get(target);
        ActorLite fresh = new ActorLite(
                old.getActorId(), old.getName(), old.getAvatar(),
                old.getVideoCount(), old.getViews(),
                old.getRating(), old.getRank(), newFavorite, old.getUrl()
        );
        list.set(target, fresh);
        updateData(list);

        if (enabled) debugLog.d("更新位置=" + target + " fav=" + newFavorite);
        clearPending();
    }

    private void clearPending() {
        pendingName = null;
        pendingUrl = null;
    }

    /* ==================== ViewHolder ==================== */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView card;
        ImageView avatarView;
        ImageView favoriteView;
        TextView nameView;
        LinearLayout infoContainer;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.item);
            avatarView = itemView.findViewById(R.id.avatarView);
            favoriteView = itemView.findViewById(R.id.favoriteView);
            nameView = itemView.findViewById(R.id.nameView);
            infoContainer = itemView.findViewById(R.id.infoContainer);
        }
    }

    /* ==================== 工具 ==================== */
    private int dp2px(Context ctx, int dp) {
        return (int) (dp * ctx.getResources().getDisplayMetrics().density + 0.5f);
    }
}

