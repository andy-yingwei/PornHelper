package com.example.pornhelper.actor;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.pornhelper.R;

import java.util.Map;


import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.example.glidehelper.ImageLoader;
import com.example.glidehelper.LoadOption;
import com.example.glidehelper.AesOption;

import com.example.pornhelper.dao.FavoriteActorDAO;
import com.example.pornhelper.helper.DebugLog;
import com.example.pornhelper.table.FavoriteActor;
import com.google.android.material.button.MaterialButton;
import com.varunest.sparkbutton.SparkButton;

/**
 * ============================================================
 * ActorProfileView —— 演员卡片自定义控件（三合一合并正式版 v4）
 * ============================================================
 *
 * <p><b>合并来源：</b></p>
 * <ul>
 *   <li>{@code ActorProfileAvatarView} — 普通头像加载</li>
 *   <li>{@code ActorProfileAvatarAesView} — AES 解密头像加载</li>
 *   <li>{@code ActorProfileAvatarCookieHeaderView} — Cookie/Header 头像加载</li>
 * </ul>
 *
 * <p><b>布局文件：</b>{@code res/layout/view_actor_profile_avatar.xml}</p>
 *
 * <p><b>头像加载策略（优先级从高到低）：</b></p>
 * <ol>
 *   <li>AES：通过 {@link #setAesKeyIv(String, String)} 设置 key/iv</li>
 *   <li>Cookie/Header：通过 {@link #setCookiesHeaders(Map, Map)} 设置</li>
 *   <li>普通：不设置上述参数时走普通 ImageLoader 加载</li>
 * </ol>
 *
 * <p><b>状态规则：</b></p>
 * <ul>
 *   <li>收藏状态唯一来源：{@code FavoriteActorDAO.existsByName(actorName)}</li>
 *   <li>SparkButton：不可点击，只负责星星状态 + 粒子动画</li>
 *   <li>MaterialButton：唯一可点击，点击 → 插/删 DAO → 切样式</li>
 *   <li>箭头：biography 有内容才显示，展开/折叠用两张图，不做旋转</li>
 * </ul>
 *
 * @author merged from ActorProfileAvatarView / ActorProfileAvatarAesView / ActorProfileAvatarCookieHeaderView
 * @see ActorProfileConfig
 * @see ActorProfile
 * @see ActorLite
 * ============================================================
 */
public class ActorProfileView extends LinearLayout {

    // ============================================================
    // 日志
    // ============================================================
    /** 默认 tag */
    private final String DEFAULT_TAG = "ActorProfileView";
    /** DebugLog 实例 */
    private  DebugLog debugLog = new DebugLog(DEFAULT_TAG);
    /** FavoriteActorDAO 文件级开关，默认关闭 */
    private  boolean enabled = false;
    /**
     * 开启 FavoriteActorDAO 日志
     * @param tag 自定义 tag；为 null 或空时自动使用 "FavoriteActorDAO"
     */
    public void enableLog(String tag) {
        enabled = true;
        debugLog = new DebugLog((tag != null && !tag.trim().isEmpty()) ? tag : DEFAULT_TAG);
        FavoriteActorDAO.enableLog(tag);
    }

    /**
     * 关闭 FavoriteActorDAO 日志（不影响 DebugLog 全局开关）
     */
    public void disableLog() {
        enabled = false;
    }

    // ============================================================
    // 业务参数（不在 Config 中，通过 setter 注入，默认全 null）
    // ============================================================
    private String aesKey;
    private String aesIv;
    private Map<String, String> cookies;
    private Map<String, String> headers;

    // ============================================================
    // UI 配置
    // ============================================================
    private ActorProfileConfig cfg;

    // ============================================================
    // 数据 & 状态
    // ============================================================
    private ActorProfile profile;
    private boolean currentFav;
    private boolean bioExpanded = false;

    // ============================================================
    // 回调
    // ============================================================
    private OnFavoriteActionListener favListener;

    // ============================================================
    // 布局控件引用（严格对齐 view_actor_profile_avatar.xml）
    // ============================================================
    private LinearLayout mainLayout;
    private ImageView avatarImageView;
    private TextView nameTextView;
    private ImageView arrowImageView;
    private ViewGroup biographyLayout;
    private TextView biographyView;
    private LinearLayout infoContainer;

    private SparkButton sparkButton;
    private MaterialButton favoriteMaterialButton;

    // ============================================================
    // 构造
    // ============================================================

    /**
     * 代码创建时使用。
     *
     * @param context 上下文
     */
    public ActorProfileView(Context context) {
        this(context, null);
    }

    /**
     * XML inflate 时使用。
     *
     * @param context 上下文
     * @param attrs   XML 属性
     */
    public ActorProfileView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.cfg = ActorProfileConfig.defaultConfig(context);
        initView();
    }

    // ============================================================
    // 初始化
    // ============================================================

    /**
     * 统一初始化入口：
     * <ol>
     *   <li>加载布局 {@code view_actor_profile_avatar.xml}</li>
     *   <li>绑定所有子控件</li>
     *   <li>禁用 SparkButton 点击（只做展示+动画）</li>
     *   <li>设置箭头和收藏按钮的点击事件</li>
     * </ol>
     */
    private void initView() {
        View root = LayoutInflater.from(getContext()).inflate(R.layout.view_actor_profile, this, true);

        mainLayout = root.findViewById(R.id.mainLayout);
        avatarImageView = root.findViewById(R.id.avatarImageView);
        nameTextView = root.findViewById(R.id.nameTextView);
        arrowImageView = root.findViewById(R.id.arrowImageView);
        biographyLayout = root.findViewById(R.id.biographyLayout);
        biographyView = root.findViewById(R.id.biographyView);
        infoContainer = root.findViewById(R.id.infoContainer);

        sparkButton = root.findViewById(R.id.sparkButton);
        favoriteMaterialButton = root.findViewById(R.id.favoriteMaterialButton);

        /* SparkButton 永远不能点，只做展示 + 动画 */
        sparkButton.setEnabled(false);

        arrowImageView.setOnClickListener(v -> toggleBiography());
        favoriteMaterialButton.setOnClickListener(v -> onFavButtonClicked());
    }

    // ============================================================
    // Config
    // ============================================================

    /**
     * 设置 UI 外观配置。
     * <p>调用后重新应用 UI、用当前收藏状态刷新按钮、同步箭头。</p>
     *
     * @param c 外观配置，null 时回退到 defaultConfig
     * @return this，支持链式调用
     */
    public ActorProfileView setConfig(ActorProfileConfig c) {
        this.cfg = c == null ? ActorProfileConfig.defaultConfig(getContext()) : c;
        applyConfig();
        applyFavStyle(currentFav, false);
        syncArrow();
        return this;
    }

    /**
     * 获取当前生效的 UI 配置。
     *
     * @return 当前 ActorProfileConfig 实例
     */
    public ActorProfileConfig getConfig() {
        return cfg;
    }

    // ============================================================
    // 业务参数 setter（决定头像加载策略）
    // ============================================================

    /**
     * 设置 AES 解密参数，用于加密头像的加载。
     * <p><b>调用此方法会自动清除 Cookie/Header 参数</b>，确保加载优先级：AES 最高。</p>
     *
     * @param aesKey AES 密钥，可为 null
     * @param aesIv  AES 初始向量，可为 null
     */
    public void setAesKeyIv(String aesKey, String aesIv) {
        this.aesKey = aesKey;
        this.aesIv = aesIv;
        this.cookies = null;
        this.headers = null;
    }

    /**
     * 设置 Cookie 和 Header 参数，用于需要鉴权的头像加载。
     * <p><b>调用此方法会自动清除 AES 参数</b>，确保加载优先级：Cookie/Header > 普通。</p>
     *
     * @param cookies HTTP Cookie Map，可为 null
     * @param headers HTTP Header Map，可为 null
     */
    public void setCookiesHeaders(Map<String, String> cookies, Map<String, String> headers) {
        this.cookies = cookies;
        this.headers = headers;
        this.aesKey = null;
        this.aesIv = null;
    }

    /**
     * 清除所有业务鉴权参数，回退到普通头像加载模式。
     */
    public void clearAuthParams() {
        this.aesKey = null;
        this.aesIv = null;
        this.cookies = null;
        this.headers = null;
    }

    // ============================================================
    // 数据入口（对外主入口）
    // ============================================================

    /**
     * 绑定演员数据到视图，触发所有 UI 刷新、头像加载、收藏状态查询。
     * <p>这是整个 View 的"唯一数据源入口"。</p>
     * <p>收藏状态从 {@code profile.getActorLite().isFavorite()} </p>
     *
     * @param p 演员数据实体，可为 null（null 时不做处理）
     */
    public void setData(ActorProfile p) {
        this.profile = p;

        if (enabled) {
            debugLog.d(getClass().getSimpleName()+"绑定数据: " + (p != null ? p.toLogString() : "null"));
        }

        applyConfig();
        bindData();

        if (p != null && p.getActorLite() != null) {
            boolean fav = profile.getActorLite().isFavorite();
            if (enabled) {
                debugLog.d(getClass().getSimpleName()+"查询收藏状态 name=" + p.getActorLite().getName() + " 结果=" + fav);
            }
            applyFavStyle(fav, false);
        }
        syncArrow();
    }

    // ============================================================
    // 收藏按钮点击
    // ============================================================

    /**
     * 收藏按钮点击事件处理。
     * <p>这是"唯一会改变收藏状态的地方"，流程：</p>
     * <ol>
     *   <li>回调给外部（只通知，不写库）</li>
     *   <li>写 FavoriteActorDAO（插/删）</li>
     *   <li>刷新 UI 样式</li>
     * </ol>
     */
    private void onFavButtonClicked() {
        if (profile == null ) return;

        ActorLite lite = profile.getActorLite();
        boolean willFav = !currentFav;

        if (enabled) {
            debugLog.d(getClass().getSimpleName() + " 收藏按钮被点击 name=" + lite.getName()
                    + " currentFav=" + currentFav + " willFav=" + willFav);
        }
        if (favListener != null) {
            favListener.onFavoriteClick(profile, willFav);
        }

        if (willFav) {
            FavoriteActor actor = new FavoriteActor();
            actor.setName(lite.getName());
            actor.setUrl(lite.getUrl());
            if (lite.getActorId() != null && !lite.getActorId().isEmpty()){
                actor.setActorId(lite.getActorId());
            }
            if (lite.getAvatar() != null && !lite.getAvatar().isEmpty()){
                actor.setAvatar(lite.getAvatar());
            }
            if (lite.getRank() != null && !lite.getRank().isEmpty()){
                actor.setRank(lite.getRank());
            }
            if (lite.getVideoCount() != null && !lite.getVideoCount().isEmpty()){
                actor.setVideoCount(lite.getVideoCount());
            }
            if (lite.getViews() != null && !lite.getViews().isEmpty()){
                actor.setViews(lite.getViews());
            }
            if (lite.getRating() != null && !lite.getRating().isEmpty()){
                actor.setRating(lite.getRating());
            }
            FavoriteActorDAO.insertIfNameNotExists(actor); // 添加记录
            applyFavStyle(true, true);
        } else {
            FavoriteActorDAO.deleteByName(lite.getName()); // 删除记录
            applyFavStyle(false, false);
        }
    }

    // ============================================================
    // 收藏样式
    // ============================================================

    /**
     * 根据收藏状态切换按钮和动效样式。
     * <p>同时控制：</p>
     * <ul>
     *   <li>MaterialButton（文字/描边/背景/圆角/字号）</li>
     *   <li>SparkButton（选中态 + 粒子动画）</li>
     * </ul>
     *
     * @param fav        是否收藏
     * @param allowAnim  是否允许播放粒子动画（仅添加收藏时 true）
     */
    private void applyFavStyle(boolean fav, boolean allowAnim) {
        currentFav = fav;
        favoriteMaterialButton.setText(fav ? cfg.favRemoveText : cfg.favAddText);
        favoriteMaterialButton.setTextColor(fav ? cfg.favTextColorActive : cfg.favTextColor);
        favoriteMaterialButton.setBackgroundColor(fav ? cfg.favBackgroundActive : cfg.favBackground);
        favoriteMaterialButton.setStrokeColor(
                android.content.res.ColorStateList.valueOf(fav ? cfg.favStrokeActive : cfg.favStroke)
        );
        favoriteMaterialButton.setStrokeWidth(dp(cfg.favStrokeWidth));
        favoriteMaterialButton.setCornerRadius(dp(cfg.favCornerRadius));
        favoriteMaterialButton.setTextSize(android.util.TypedValue.COMPLEX_UNIT_SP, cfg.favTextSize);

        sparkButton.setChecked(fav);

        if (allowAnim && fav) {
            sparkButton.post(sparkButton::playAnimation);
        }
    }

    // ============================================================
    // 箭头：简介展开/折叠
    // ============================================================

    /**
     * 切换简介区的展开/折叠状态。
     * <p>展开时 biographyLayout 从高度 0 动画到目标高度；</p>
     * <p>折叠时从当前高度动画到 0 后设为 GONE。</p>
     */
    private void toggleBiography() {
        if (arrowImageView.getVisibility() != View.VISIBLE) return;

        bioExpanded = !bioExpanded;
        syncArrow();

        if (bioExpanded) {
            biographyLayout.setVisibility(View.VISIBLE);
            biographyView.measure(
                    View.MeasureSpec.makeMeasureSpec(getWidth(), View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.UNSPECIFIED
            );

            int target = biographyView.getMeasuredHeight()
                    + biographyLayout.getPaddingTop()
                    + biographyLayout.getPaddingBottom();

            biographyLayout.getLayoutParams().height = 0;
            animateHeight(0, target, () ->
                    biographyLayout.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT);

        } else {
            int start = biographyLayout.getHeight();
            animateHeight(start, 0, () -> {
                biographyLayout.setVisibility(View.GONE);
                biographyLayout.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
            });
        }
    }

    /**
     * 高度动画。
     *
     * @param from   起始高度（px）
     * @param to     目标高度（px）
     * @param onEnd  动画结束后的回调
     */
    private void animateHeight(int from, int to, Runnable onEnd) {
        ValueAnimator a = ValueAnimator.ofInt(from, to);
        a.setDuration(250);
        a.addUpdateListener(anim -> {
            biographyLayout.getLayoutParams().height = (int) anim.getAnimatedValue();
            biographyLayout.requestLayout();
        });
        a.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(android.animation.Animator animation) {
                onEnd.run();
            }
        });
        a.start();
    }

    /**
     * 同步箭头图标（展开/折叠用两张图，不是旋转）。
     */
    private void syncArrow() {
        if (arrowImageView == null) return;

        arrowImageView.setImageResource(
                bioExpanded ? cfg.arrowExpanded : cfg.arrowCollapsed
        );

        if (cfg.arrowColor != 0) {
            arrowImageView.setColorFilter(cfg.arrowColor);
        }
    }

    /**
     * 对外设置展开/折叠状态。
     *
     * @param expanded true = 展开，false = 折叠
     */
    public void setArrowExpanded(boolean expanded) {
        if (bioExpanded == expanded) return;
        bioExpanded = expanded;
        syncArrow();
        biographyLayout.setVisibility(expanded ? View.VISIBLE : View.GONE);
    }

    // ============================================================
    // Config → View 映射
    // ============================================================

    /**
     * 将 Config 中的字段逐一应用到各子控件。
     * <p>在 setConfig / setData / 构造 时都会被调用。</p>
     */
    private void applyConfig() {
        if (cfg == null) return;

        mainLayout.setBackgroundColor(cfg.background);

        int avatarPx = dp(cfg.avatarSize);
        avatarImageView.setLayoutParams(new LinearLayout.LayoutParams(avatarPx, avatarPx));
        avatarImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        nameTextView.setTextSize(android.util.TypedValue.COMPLEX_UNIT_SP, cfg.nameTextSize);
        nameTextView.setTextColor(cfg.nameTextColor);

        int arrowPx = dp(cfg.arrowSize);
        arrowImageView.setLayoutParams(new LinearLayout.LayoutParams(arrowPx, arrowPx));

        biographyView.setTextSize(android.util.TypedValue.COMPLEX_UNIT_SP, cfg.infoTextSize);
        biographyView.setTextColor(cfg.infoTextColor);
    }

    // ============================================================
    // 数据绑定
    // ============================================================

    /**
     * 将数据映射到 UI 控件。
     * <p>只做"数据 → UI"，不决定收藏状态。</p>
     */
    private void bindData() {
        if (profile == null || profile.getActorLite() == null) return;

        ActorLite lite = profile.getActorLite();

        // ---- 头像（三路分支） ----
        loadAvatar(lite);

        // ---- 名字 ----
        nameTextView.setText(lite.getName());
        nameTextView.setVisibility(View.VISIBLE);
        // ---- 简介 ----
        String bio = profile.getBiography();
        biographyView.setText(TextUtils.isEmpty(bio) ? "" : bio);

        boolean hasBio = has(bio);
        arrowImageView.setVisibility(hasBio ? View.VISIBLE : View.GONE);
        arrowImageView.setClickable(hasBio);

        if (!hasBio) {
            bioExpanded = false;
            biographyLayout.setVisibility(View.GONE);
        }

        // ---- info 行 ----
        infoContainer.removeAllViews();
        addInfo(lite.getVideoCount(), lite.getViews());
        addInfo(profile.getAge(), profile.getBirthday());
        addInfo(profile.getHeight(), profile.getWeight());
        addInfo(profile.getBust(), profile.getHips());
        addInfo(profile.getNationality(), profile.getBirthplace());
        addInfoSingle(profile.getTags());
    }

    // ============================================================
    // 核心：三路头像加载分发
    // ============================================================

    /**
     * 头像加载核心方法，根据当前业务参数决定使用哪条加载路径。
     * <p>优先级：AES 解密 > Cookie/Header 鉴权 > 普通加载</p>
     *
     * @param lite 演员精简数据，不可为 null
     */
    private void loadAvatar(ActorLite lite) {
        if (lite == null) {
            if (enabled) debugLog.w(getClass().getSimpleName()+"加载头像跳过(ActorLite 为null)");
            avatarImageView.setImageResource(cfg.avatarError);
            return;
        }

        if (lite.getAvatar() == null || lite.getAvatar().trim().isEmpty()) {
            if (enabled) debugLog.w(getClass().getSimpleName()+"加载头像跳过(url 为null/空) url=" + lite.getAvatar());
            avatarImageView.setImageResource(cfg.avatarPlaceholder);
            return;
        }

        if (TextUtils.isEmpty(lite.getAvatar())) {
            if (enabled) debugLog.w(getClass().getSimpleName()+"加载头像跳过(url无效) url=" + lite.getAvatar());
            avatarImageView.setImageResource(cfg.avatarPlaceholder);
            return;
        }

        // ==================== 三路分支 ====================

        if (!TextUtils.isEmpty(aesKey) && !TextUtils.isEmpty(aesIv)) {
            // ---------------------------------------------------------
            // 路径 1：AES 解密加载
            // ---------------------------------------------------------
            if (enabled) {debugLog.d(getClass().getSimpleName()+"头像加载模式: AES");}

            try {
                ImageLoader imageLoader = ImageLoader.with(getContext());
                AesOption aesOption = AesOption.create(aesKey, aesIv);
                LoadOption option = new LoadOption()
                        .forList()
                        .placeholder(cfg.avatarPlaceholder)
                        .error(cfg.avatarError)
                        .round(cfg.avatarRadius);
                option.skipMemoryCache(cfg.skipMemoryCache);
                option.diskCacheStrategy(cfg.diskCacheStrategy);
                imageLoader.loadAesBitmap(lite.getAvatar(), avatarImageView, option, aesOption);
            } catch (Exception e) {
                if (enabled) debugLog.e(getClass().getSimpleName()+"AES加载图片错误 url=" + lite.getAvatar(), e);
                avatarImageView.setImageResource(cfg.avatarError);
            }
        } else if ((cookies != null && !cookies.isEmpty()) || (headers != null && !headers.isEmpty())) {
            // ---------------------------------------------------------
            // 路径 2：Cookie / Header 鉴权加载（有其一即可）
            // ---------------------------------------------------------
            if (enabled) {debugLog.d(getClass().getSimpleName()+"头像加载模式: Cookie/Header");}

            try {
                LoadOption option = new LoadOption()
                        .forList()
                        .placeholder(cfg.avatarPlaceholder)
                        .error(cfg.avatarError)
                        .round(cfg.avatarRadius);
                option.skipMemoryCache(cfg.skipMemoryCache);
                option.diskCacheStrategy(cfg.diskCacheStrategy);

                if (cookies != null && !cookies.isEmpty()) {
                    option.addCookies(cookies);
                    if (enabled) debugLog.d("  添加 cookies: " + cookies.size() + " 条");
                }

                if (headers != null && !headers.isEmpty()) {
                    option.addHeaders(headers);
                    if (enabled) debugLog.d("  添加 headers: " + headers.size() + " 条");
                }

                ImageLoader.with(getRootView().getContext()).load(lite.getAvatar(), avatarImageView, option);
            } catch (Exception e) {
                if (enabled) debugLog.e(getClass().getSimpleName()+"Cookie/Header加载图片错误 url=" + lite.getAvatar(), e);
                avatarImageView.setImageResource(cfg.avatarError);
            }

        } else {
            // ---------------------------------------------------------
            // 路径 3：普通加载
            // ---------------------------------------------------------
            if (enabled) {debugLog.d(getClass().getSimpleName()+"头像加载模式: 普通");}
            try {
                RequestOptions options = new RequestOptions().transform(new CenterCrop());
                if (cfg.avatarRadius > 0) {
                    options = options.transform(new CenterCrop(), new RoundedCorners(dp(cfg.avatarRadius)));
                }
                Glide.with(getContext())
                        .load(lite.getAvatar())
                        .apply(options)
                        .placeholder(cfg.avatarPlaceholder)
                        .error(cfg.avatarError)
                        .skipMemoryCache(cfg.skipMemoryCache)
                        .diskCacheStrategy(cfg.diskCacheStrategy)
                        .into(avatarImageView);
            } catch (Exception e) {
                if (enabled) debugLog.e(getClass().getSimpleName()+"普通加载图片错误 url=" + lite.getAvatar(), e);
                avatarImageView.setImageResource(cfg.avatarError);
            }
        }
    }

    // ============================================================
    // 信息行
    // ============================================================

    /**
     * 添加一行信息（左右两列）。
     *
     * @param left  左文本，可为空
     * @param right 右文本，可为空
     */
    private void addInfo(String left, String right) {
        if (!has(left) && !has(right)) return;

        TextView tv = new TextView(getContext());
        tv.setTextSize(android.util.TypedValue.COMPLEX_UNIT_SP, cfg.infoTextSize);
        tv.setTextColor(cfg.infoTextColor);
        tv.setGravity(android.view.Gravity.START);
        tv.setText(has(left) ? left + "        " + right : right);
        infoContainer.addView(tv);
    }

    /**
     * 添加单行信息。
     *
     * @param text 文本，可为空
     */
    private void addInfoSingle(String text) {
        if (!has(text)) return;
        TextView tv = new TextView(getContext());
        tv.setTextSize(android.util.TypedValue.COMPLEX_UNIT_SP, cfg.infoTextSize);
        tv.setTextColor(cfg.infoTextColor);
        tv.setText(text);
        infoContainer.addView(tv);
    }

    // ============================================================
    // 工具
    // ============================================================

    /**
     * dp 转 px。
     *
     * @param v dp 值
     * @return px 值
     */
    private int dp(int v) {
        return (int) (v * getResources().getDisplayMetrics().density + 0.5f);
    }

    /**
     * 判断字符串是否非空。
     *
     * @param s 字符串
     * @return true 表示非空
     */
    private boolean has(String s) {
        return s != null && !s.trim().isEmpty();
    }

    // ============================================================
    // 回调
    // ============================================================

    /**
     * 设置收藏点击监听。
     * <p>回调中只做通知（打点/Toast/同步其他页面），不要再写 DAO。</p>
     *
     * @param l 监听器
     */
    public void setOnFavoriteActionListener(OnFavoriteActionListener l) {
        this.favListener = l;
    }

    /**
     * 收藏点击回调接口。
     */
    public interface OnFavoriteActionListener {
        void onFavoriteClick(ActorProfile profile, boolean willFav);
    }

    // ============================================================
    // 对外辅助
    // ============================================================

    /**
     * 获取当前绑定的数据。
     *
     * @return 当前 ActorProfile 实例，可能为 null
     */
    public ActorProfile getCurrentData() {
        return profile;
    }

    /**
     * 获取 SparkButton 引用（供外部设置动效等）。
     *
     * @return SparkButton 实例
     */
    public SparkButton getSparkButton() {
        return sparkButton;
    }

    /**
     * 获取 MaterialButton 引用。
     *
     * @return MaterialButton 实例
     */
    public MaterialButton getFavoriteMaterialButton() {
        return favoriteMaterialButton;
    }
}