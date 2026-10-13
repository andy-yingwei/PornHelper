package com.example.pornhelper.video;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.glidehelper.AesOption;
import com.example.glidehelper.ImageLoader;
import com.example.glidehelper.LoadOption;
import com.example.pornhelper.R;
import com.example.pornhelper.cast.Cast;
import com.example.pornhelper.cast.CastAdapter;
import com.example.pornhelper.cast.CastConfig;
import com.example.pornhelper.magnet.MagnetAdapter;
import com.example.pornhelper.playSource.PlaySourceAdapter;
import com.example.pornhelper.tag.TagAdapter;
import com.example.pornhelper.dao.FavoriteVideoDAO;
import com.example.pornhelper.helper.DebugLog;
import com.example.pornhelper.magnet.Magnet;
import com.example.pornhelper.playSource.PlaySource;
import com.example.pornhelper.tag.Tag;
import com.example.pornhelper.table.FavoriteVideo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * ============================================================
 * VideoProfileView
 * ============================================================
 * Video 详情页自定义控件，负责展示视频封面、标题、简介、
 * 演职员、标签、播放源、磁力列表，以及收藏/下载交互。
 *
 * <p><b>合并来源：</b></p>
 * <ul>
 *   <li>{@code VideoProfileCoverView} — 普通封面加载</li>
 *   <li>{@code VideoProfileCoverAesView} — AES 解密封面加载</li>
 *   <li>{@code VideoProfileCoverCookieHeaderView} — Cookie/Header 鉴权封面加载</li>
 * </ul>
 *
 * <p><b>布局文件：</b>{@code res/layout/view_video_profile.xml}</p>
 *
 * <p><b>封面加载策略（优先级从高到低）：</b></p>
 * <ol>
 *   <li>AES 解密：通过 {@link #setAesKeyIv(String, String)} 设置 key/iv</li>
 *   <li>Cookie/Header 鉴权：通过 {@link #setCookiesHeaders(Map, Map)} 设置</li>
 *   <li>普通加载：不设置上述参数时走普通 Glide 加载</li>
 * </ol>
 *
 * <p><b>演员头像加载：</b>使用 {@link CastAdapter}（三合一融合），
 * 鉴权参数通过 setter 注入，与封面加载策略完全对齐。</p>
 *
 * <p><b>状态规则：</b></p>
 * <ul>
 *   <li><b>收藏状态：</b>
 *     <ul>
 *       <li>内存状态 {@code currentFav} 由 {@link #applyFavoriteStyle(boolean)} 维护</li>
 *       <li>{@link #bindData()} 从 {@code VideoLite.isFavorite()} 初始化收藏状态</li>
 *       <li>{@link #onFavoriteClicked()} 通过 DAO 持久化 + 调用 {@code applyFavoriteStyle()} 更新 UI</li>
 *       <li>选中态使用 {@code favoriteIconColor} 原色，未选中态使用 70% 透明度的同色值</li>
 *     </ul>
 *   </li>
 *   <li><b>简介展开/折叠：</b>有内容才显示箭头，使用高度动画（{@link #toggleBiography()}）</li>
 *   <li><b>下载列表：</b>点击下载按钮展开/折叠磁力列表（GONE/VISIBLE 切换，无独立动画）</li>
 *   <li><b>Config（{@link VideoProfileConfig}）：</b>
 *     <ul>
 *       <li>只负责 UI 外观（颜色 / 尺寸 / 图标 / 圆角等）</li>
 *       <li>{@link #setConfig(VideoProfileConfig)} 只刷新外观，不刷新任何业务状态（收藏、展开等）</li>
 *       <li>不包含任何网络鉴权参数</li>
 *     </ul>
 *   </li>
 *   <li><b>网络鉴权参数（AES / Cookies / Headers）：</b>通过 setter 注入，与 Config 完全解耦</li>
 * </ul>
 *
 * <p><b>数据入口：</b></p>
 * <ul>
 *   <li>{@link #setData(VideoProfile)} — 绑定数据并刷新全部 UI；传入 {@code null} 时清空所有内容</li>
 *   <li>{@link #setConfig(VideoProfileConfig)} — 仅刷新纯外观配置，不影响业务状态</li>
 * </ul>
 *
 * <p><b>子列表配置：</b></p>
 * <ul>
 *   <li>Cast / Tag / PlaySource / Magnet 均支持外部传入 Adapter 或自动创建兜底 Adapter</li>
 *   <li>各列表列数可通过 {@code setXxxSpan(int)} 动态调整</li>
 * </ul>
 *
 * <p><b>完整示例：</b></p>
 * <pre>
 * VideoProfileView profileView = new VideoProfileView(context);
 *
 * // 1. 设置外观配置
 * profileView.setConfig(
 *     new VideoProfileConfig.Builder()
 *         .copyFrom(VideoProfileConfig.defaultConfig(context))
 *         .coverHeight(240)
 *         .favoriteIconColor(0xFFFF4081)
 *         .build()
 * );
 *
 * // 2. 设置鉴权参数（可选，按需选择一种）
 * // profileView.setAesKeyIv("key", "iv");
 * // profileView.setCookiesHeaders(cookies, headers);
 *
 * // 3. 设置外部 Adapter（推荐）
 * profileView.setCastAdapter(castAdapter);
 * profileView.setTagAdapter(tagAdapter);
 * profileView.setPlaySourceAdapter(playSourceAdapter);
 * profileView.setMagnetAdapter(magnetAdapter);
 *
 * // 4. 设置事件监听
 * profileView.setListener(new VideoProfileView.VideoProfileListener() {
 *     {@literal @}Override
 *     public void onCastClick(Cast cast, int pos) { }
 *     {@literal @}Override
 *     public void onTagClick(Tag tag, int pos) { }
 *     {@literal @}Override
 *     public void onPlaySourceClick(PlaySource source, int pos) { }
 *     {@literal @}Override
 *     public void onMagnetClick(Magnet magnet, int pos) { }
 *     {@literal @}Override
 *     public void onFavoriteClick(VideoProfile p, boolean willFav) { }
 *     {@literal @}Override
 *     public void onDownloadClick(VideoProfile p, boolean willShow) { }
 * });
 *
 * // 5. 绑定数据
 * profileView.setData(videoProfile);
 * </pre>
 *
 * @see VideoProfileConfig
 * @see VideoProfile
 * @see VideoLite
 * @see CastAdapter
 * @see CastConfig
 * @see VideoProfileListener
 */
public class VideoProfileView extends LinearLayout {

    // ============================================================
    // 日志
    // ============================================================
    private final String DEFAULT_TAG = "VideoProfileView";
    private DebugLog debugLog = new DebugLog(DEFAULT_TAG);
    private boolean enabled = false;

    public void enableLog(String tag) {
        this.enabled = true;
        debugLog = new DebugLog((tag != null && !tag.trim().isEmpty()) ? tag : DEFAULT_TAG);
        FavoriteVideoDAO.enableLog(tag);
        if (castAdapter != null) CastAdapter.enableLog(tag);
        if (tagAdapter != null) TagAdapter.enableLog(tag);
        if (playSourceAdapter != null) PlaySourceAdapter.enableLog(tag);
        if (magnetAdapter != null) MagnetAdapter.enableLog(tag);
    }

    public void disableLog() {
        this.enabled = false;
    }

    // ============================================================
    // 业务参数（不在 Config 中，通过 setter 注入，默认全 null）
    // 完全对齐 ActorProfileView 的 aesKey/aesIv/cookies/headers 模式
    // ============================================================
    private String aesKey;
    private String aesIv;
    private Map<String, String> cookies;
    private Map<String, String> headers;

    public void setAesKeyIv(String key, String iv) {
        this.aesKey = key;
        this.aesIv = iv;
        this.cookies = null;
        this.headers = null;
        // 同步给 CastAdapter
        if (castAdapter != null) {
            castAdapter.setAesKeyIv(key, iv);
        }
    }

    public void setCookiesHeaders(Map<String, String> cookies, Map<String, String> headers) {
        this.cookies = cookies;
        this.headers = headers;
        this.aesKey = null;
        this.aesIv = null;
        // 同步给 CastAdapter
        if (castAdapter != null) {
            castAdapter.setCookiesHeaders(cookies, headers);
        }
    }

    public void clearAuthParams() {
        this.aesKey = null;
        this.aesIv = null;
        this.cookies = null;
        this.headers = null;
        // 同步给 CastAdapter
        if (castAdapter != null) {
            castAdapter.clearAuthParams();
        }
    }

    // ============================================================
    // UI 配置（纯外观，不含任何网络参数）
    // ============================================================
    private VideoProfileConfig cfg;

    // ============================================================
    // 数据 & 状态
    // ============================================================
    private VideoProfile profile;
    private boolean currentFav = false;
    private boolean bioExpanded = false;
    private boolean downloadGroupOpen = false;

    private VideoProfileListener listener;

    private String videoTitle;

    // ============================================================
    // 子 Adapter
    // ============================================================
    private CastAdapter castAdapter;
    private TagAdapter tagAdapter;
    private PlaySourceAdapter playSourceAdapter;
    private MagnetAdapter magnetAdapter;

    // ============================================================
    // 列数
    // ============================================================
    private int castSpan = 4;
    private int tagSpan = 4;
    private int playSourceSpan = 4;
    private int magnetSpan = 1;

    // ============================================================
    // UI 组件（严格对齐 view_video_profile.xml）
    // ============================================================
    private LinearLayout mainLayout;
    private TextView titleView;
    private ImageView arrowImageView;
    private AppCompatImageButton favoriteButton;
    private AppCompatImageButton downloadButton;
    private ImageView coverView;

    private ViewGroup castGroup;
    private RecyclerView castRecyclerView;
    private ViewGroup tagsGroup;
    private RecyclerView tagsRecyclerView;
    private ViewGroup playSourceGroup;
    private RecyclerView playSourceRecyclerView;

    private ViewGroup biographyGroup;
    private TextView biographyView;

    private ViewGroup downloadLaGroup;
    private RecyclerView downloadRecyclerView;

    // ============================================================
    // 构造
    // ============================================================
    public VideoProfileView(Context context) {
        this(context, null);
    }

    public VideoProfileView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.cfg = VideoProfileConfig.defaultConfig(context);
        this.debugLog = new DebugLog(DEFAULT_TAG);
        initView();
        applyConfig();
        applyAllLayout();
    }

    // ============================================================
    // 初始化
    // ============================================================
    private void initView() {
        setOrientation(VERTICAL);
        View root = LayoutInflater.from(getContext()).inflate(R.layout.view_video_profile, this, true);

        mainLayout = root.findViewById(R.id.mainLayout);
        titleView = root.findViewById(R.id.titleView);
        arrowImageView = root.findViewById(R.id.arrowImageView);
        favoriteButton = root.findViewById(R.id.favoriteButton);
        downloadButton = root.findViewById(R.id.downloadButton);
        coverView = root.findViewById(R.id.coverView);

        castGroup = root.findViewById(R.id.castGroup);
        castRecyclerView = root.findViewById(R.id.castRecyclerView);
        tagsGroup = root.findViewById(R.id.tagsGroup);
        tagsRecyclerView = root.findViewById(R.id.tagsRecyclerView);
        playSourceGroup = root.findViewById(R.id.playSourceGroup);
        playSourceRecyclerView = root.findViewById(R.id.playSourceRecyclerView);

        biographyGroup = root.findViewById(R.id.biographyGroup);
        biographyView = root.findViewById(R.id.biographyView);

        downloadLaGroup = root.findViewById(R.id.downloadLaGroup);
        downloadRecyclerView = root.findViewById(R.id.downloadRecyclerView);

        // 标题跑马灯
        titleView.setSingleLine(true);
        titleView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        titleView.setMarqueeRepeatLimit(-1);
        titleView.setHorizontallyScrolling(true);
        titleView.setSelected(true);

        arrowImageView.setOnClickListener(v -> toggleBiography());
        favoriteButton.setOnClickListener(v -> onFavoriteClicked());
        downloadButton.setOnClickListener(v -> onDownloadClicked());
    }

    // ============================================================
    // Config
    // ============================================================
    public VideoProfileView setConfig(VideoProfileConfig c) {
        this.cfg = c == null ? VideoProfileConfig.defaultConfig(getContext()) : c;
        applyConfig();
        syncArrow();
        return this;
    }

    public VideoProfileConfig getConfig() {
        return cfg;
    }

    // ============================================================
    // Span 设置
    // ============================================================
    public void setCastSpan(int span) {
        this.castSpan = span > 0 ? span : 4;
        applyCastLayout();
    }

    public void setTagSpan(int span) {
        this.tagSpan = span > 0 ? span : 3;
        applyTagLayout();
    }

    public void setPlaySourceSpan(int span) {
        this.playSourceSpan = span > 0 ? span : 4;
        applyPlaySourceLayout();
    }

    public void setMagnetSpan(int span) {
        this.magnetSpan = span > 0 ? span : 1;
        applyMagnetLayout();
    }

    private void applyAllLayout() {
        applyCastLayout();
        applyTagLayout();
        applyPlaySourceLayout();
        applyMagnetLayout();
    }

    private void applyCastLayout() {
        if (castRecyclerView == null) return;
        castRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), castSpan));
        castRecyclerView.setNestedScrollingEnabled(false);
        if (enabled) debugLog.d("CastRecyclerView 列数=" + castSpan);
    }

    private void applyTagLayout() {
        if (tagsRecyclerView == null) return;
        tagsRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), tagSpan));
        tagsRecyclerView.setNestedScrollingEnabled(false);
        if (enabled) debugLog.d("TagsRecyclerView 列数=" + tagSpan);
    }

    private void applyPlaySourceLayout() {
        if (playSourceRecyclerView == null) return;
        playSourceRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), playSourceSpan));
        playSourceRecyclerView.setNestedScrollingEnabled(false);
        if (enabled) debugLog.d("PlaySourceRecyclerView 列数=" + playSourceSpan);
    }

    private void applyMagnetLayout() {
        if (downloadRecyclerView == null) return;
        downloadRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), magnetSpan));
        downloadRecyclerView.setNestedScrollingEnabled(false);
        if (enabled) debugLog.d("DownloadRecyclerView 列数=" + magnetSpan);
    }

    // ============================================================
    // 外部直接设置 Adapter（推荐 / 主路径）
    // ============================================================
    public void setCastAdapter(CastAdapter a) {
        this.castAdapter = a;
        if (castRecyclerView != null) castRecyclerView.setAdapter(a);
        // 注入时同步鉴权参数
        if (a != null) {
            if (!TextUtils.isEmpty(aesKey) && !TextUtils.isEmpty(aesIv)) {
                a.setAesKeyIv(aesKey, aesIv);
            } else if (cookies != null && headers != null) {
                a.setCookiesHeaders(cookies, headers);
            }
        }
        bindCastListener();
    }

    public VideoProfileView setTagAdapter(TagAdapter a) {
        this.tagAdapter = a;
        if (tagsRecyclerView != null) tagsRecyclerView.setAdapter(a);
        bindTagListener();
        return this;
    }

    public VideoProfileView setPlaySourceAdapter(PlaySourceAdapter a) {
        this.playSourceAdapter = a;
        if (playSourceRecyclerView != null) playSourceRecyclerView.setAdapter(a);
        bindPlaySourceListener();
        return this;
    }

    public VideoProfileView setMagnetAdapter(MagnetAdapter a) {
        this.magnetAdapter = a;
        if (downloadRecyclerView != null) downloadRecyclerView.setAdapter(a);
        bindMagnetListener();
        return this;
    }

    // ============================================================
    // 自动填充（兜底路径） 确保 Adapter 存在
    // ============================================================
    private void ensureAdapters() {
        if (castAdapter == null) {
            // ✅ 修正：使用 Builder 创建 CastConfig（CastConfig 是 final + private 构造）
            CastConfig castConfig = new CastConfig.Builder()
                    .copyFrom(CastConfig.defaultConfig())
                    .avatarSize(40)
                    .avatarRadius(15)
                    .nameTextSize(12)
                    .nameTextColor(0xFF938F99)
                    .strokeColor(0xFF938F99)
                    .strokeWidth(1)
                    .build();

            // 创建 CastAdapter
            castAdapter = new CastAdapter(castConfig);

            // 透传鉴权参数（和封面加载逻辑保持一致）
            if (!TextUtils.isEmpty(aesKey) && !TextUtils.isEmpty(aesIv)) {
                castAdapter.setAesKeyIv(aesKey, aesIv);
            } else if (cookies != null && headers != null) {
                castAdapter.setCookiesHeaders(cookies, headers);
            }

            castRecyclerView.setAdapter(castAdapter);
            bindCastListener();
        }
        if (tagAdapter == null) {
            tagAdapter = new TagAdapter(new ArrayList<>(), null);
            tagsRecyclerView.setAdapter(tagAdapter);
            bindTagListener();
        }
        if (playSourceAdapter == null) {
            playSourceAdapter = new PlaySourceAdapter(new ArrayList<>(), null);
            playSourceRecyclerView.setAdapter(playSourceAdapter);
            bindPlaySourceListener();
        }
        if (magnetAdapter == null) {
            magnetAdapter = new MagnetAdapter(new ArrayList<>(), null);
            downloadRecyclerView.setAdapter(magnetAdapter);
            bindMagnetListener();
        }
    }

    // ============================================================
    // 监听绑定
    // ============================================================
    private void bindCastListener() {
        if (castAdapter == null) return;
        castAdapter.setListener((cast, pos) -> {
            if (enabled) debugLog.d(getClass().getSimpleName() + "点击了CastAdapter 位置=" + pos + "  数据=" + cast.toLogString());
            if (listener != null) listener.onCastClick(cast, pos);
        });
    }

    private void bindTagListener() {
        if (tagAdapter == null) return;
        tagAdapter.setListener((tag, pos) -> {
            if (enabled) debugLog.d(getClass().getSimpleName() + "点击了TagAdapter 位置=" + pos + "  数据=" + tag.toLogString());
            if (listener != null) listener.onTagClick(tag, pos);
        });
    }

    private void bindPlaySourceListener() {
        if (playSourceAdapter == null) return;
        playSourceAdapter.setListener((playSource, pos) -> {
            if (enabled) debugLog.d(getClass().getSimpleName() + " PlaySourceAdapter点击了 位置=" + pos + " 数据=" + playSource.toLogString());
            if (listener != null) listener.onPlaySourceClick(playSource, pos);
        });
    }

    private void bindMagnetListener() {
        if (magnetAdapter == null) return;
        magnetAdapter.setListener((magnet, pos) -> {
            if (enabled) debugLog.d(getClass().getSimpleName() + " MagnetAdapter点击了 位置=" + pos + " 数据=" + magnet.toLogString());
            if (listener != null) listener.onMagnetClick(magnet, pos);
        });
    }

    // ============================================================
    // 监听接口
    // ============================================================
    public interface VideoProfileListener {
        void onCastClick(Cast cast, int pos);
        void onTagClick(Tag tag, int pos);
        void onPlaySourceClick(PlaySource playSource, int pos);
        void onMagnetClick(Magnet magnet, int pos);
        void onFavoriteClick(VideoProfile p, boolean willFav);
        void onDownloadClick(VideoProfile p, boolean willShow);
    }

    public VideoProfileView setListener(VideoProfileListener l) {
        this.listener = l;
        return this;
    }

    // ============================================================
    // 数据入口
    // ============================================================
    public void setData(VideoProfile p) {
        this.profile = p;

        if (enabled) {
            debugLog.d(getClass().getSimpleName() + " 绑定数据: " + (p != null ? p.toLogString() : "null"));
        }

        if (p == null) {
            titleView.setText("");
            coverView.setImageResource(cfg.coverPlaceholder);
            applyFavoriteStyle(false);
            setGroupVisible(castGroup, false);
            setGroupVisible(tagsGroup, false);
            setGroupVisible(playSourceGroup, false);
            downloadButton.setVisibility(View.GONE);
            downloadLaGroup.setVisibility(View.GONE);
            biographyView.setText("");
            biographyGroup.setVisibility(View.GONE);
            arrowImageView.setVisibility(View.GONE);
            return;
        }

        applyConfig();
        bindData();
        syncArrow();
    }

    // ============================================================
    // 数据绑定
    // ============================================================
    private void bindData() {
        videoTitle = profile.getVideoLite() != null ? profile.getVideoLite().getTitle() : "";
        titleView.setText(videoTitle);
        titleView.setSelected(true);

        loadCover(profile.getVideoLite());

        applyFavoriteStyle(profile.getVideoLite().isFavorite());
        ensureAdapters();

        if (nullOrEmpty(profile.getCastList())) {
            setGroupVisible(castGroup, false);
        } else {
            List<Cast> castList = profile.getCastList();
            castAdapter.submitList(castList != null ? new ArrayList<>(castList) : null);
            setGroupVisible(castGroup, true);
        }

        if (nullOrEmpty(profile.getTagList())) {
            setGroupVisible(tagsGroup, false);
        } else {
            tagAdapter.updateData(profile.getTagList());
            setGroupVisible(tagsGroup, true);
        }

        if (nullOrEmpty(profile.getPlaySourceList())) {
            setGroupVisible(playSourceGroup, false);
        } else {
            playSourceAdapter.updateData(profile.getPlaySourceList());
            setGroupVisible(playSourceGroup, true);
        }

        boolean hasMagnet = !nullOrEmpty(profile.getMagnetList());
        downloadButton.setVisibility(hasMagnet ? View.VISIBLE : View.GONE);
        if (hasMagnet) {
            magnetAdapter.updateData(profile.getMagnetList());
        }
        downloadGroupOpen = false;
        downloadLaGroup.setVisibility(View.GONE);

        String bio = profile.getSummary();
        biographyView.setText(has(bio) ? bio : "");
        arrowImageView.setVisibility(has(bio) ? View.VISIBLE : View.GONE);
        bioExpanded = false;
        biographyGroup.setVisibility(View.GONE);
    }

    // ============================================================
    // 封面加载（三路分支，完全对齐 ActorProfileView.loadAvatar）
    // ============================================================
    private void loadCover(VideoLite videoLite) {

        if (videoLite == null) {
            if (enabled) {
                debugLog.w(getClass().getSimpleName() + " 加载封面跳过(VideoLite 为null)");
            }
            coverView.setImageResource(cfg.coverPlaceholder);
            return;
        }

        String url = videoLite.getCover();

        if (url == null || url.trim().isEmpty()) {
            if (enabled) {
                debugLog.w(getClass().getSimpleName() + " 加载封面跳过(url 为null/空) url=" + url);
            }
            coverView.setImageResource(cfg.coverPlaceholder);
            return;
        }

        // ==================== 三路分支 ====================

        if (!TextUtils.isEmpty(aesKey) && !TextUtils.isEmpty(aesIv)) {

            if (enabled) {debugLog.d(getClass().getSimpleName() + " 封面加载模式: AES");}

            try {
                ImageLoader imageLoader = ImageLoader.with(getRootView().getContext());
                AesOption aesOption = AesOption.create(aesKey, aesIv);
                LoadOption option = new LoadOption()
                        .forList()
                        .placeholder(cfg.coverPlaceholder)
                        .error(cfg.coverError)
                        .round(cfg.coverRadius);
                option.skipMemoryCache(cfg.skipMemoryCache);
                option.diskCacheStrategy(cfg.diskCacheStrategy);
                imageLoader.loadAesBitmap(url, coverView, option, aesOption);
            } catch (Exception e) {
                if (enabled) {
                    debugLog.e(getClass().getSimpleName() + " AES加载封面错误 url=" + url, e);
                }
                coverView.setImageResource(cfg.coverError);
            }

        } else if ((cookies != null && !cookies.isEmpty()) || (headers != null && !headers.isEmpty())) {

            if (enabled) {debugLog.d(getClass().getSimpleName() + " 封面加载模式: Cookie/Header");}

            try {
                LoadOption option = new LoadOption()
                        .forList()
                        .placeholder(cfg.coverPlaceholder)
                        .error(cfg.coverError)
                        .round(cfg.coverRadius);
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

                ImageLoader.with(getRootView().getContext()).load(url, coverView, option);
            } catch (Exception e) {
                if (enabled) {
                    debugLog.e(getClass().getSimpleName() + " Cookie/Header加载封面错误 url=" + url, e);
                }
                coverView.setImageResource(cfg.coverError);
            }

        } else {

            if (enabled) {debugLog.d(getClass().getSimpleName() + " 封面加载模式: 普通");}
            try {
                RequestOptions options = new RequestOptions().transform(new CenterCrop());
                if (cfg.coverRadius > 0) {
                    options = options.transform(new CenterCrop(), new RoundedCorners(dp(cfg.coverRadius)));
                }
                Glide.with(getRootView().getContext())
                        .load(url)
                        .apply(options)
                        .placeholder(cfg.coverPlaceholder)
                        .error(cfg.coverError)
                        .skipMemoryCache(cfg.skipMemoryCache)
                        .diskCacheStrategy(cfg.diskCacheStrategy)
                        .into(coverView);
            } catch (Exception e) {
                if (enabled) {debugLog.e(getClass().getSimpleName() + " 普通加载封面错误 url=" + url, e);}
                coverView.setImageResource(cfg.coverError);
            }
        }
    }

    // ============================================================
    // 收藏
    // ============================================================
    private void onFavoriteClicked() {
        if (profile == null ) {return;}
        
        VideoLite lite = profile.getVideoLite();
        boolean willFav = !currentFav;

        if (enabled) {
            debugLog.d(getClass().getSimpleName() + " 收藏按钮被点击 title=" + videoTitle
                    + " currentFav=" + currentFav + " willFav=" + willFav);
        }

        if (listener != null) {
            listener.onFavoriteClick(profile, willFav);
        }
        
        if (willFav) {
            FavoriteVideo v = new FavoriteVideo();
            v.setTitle(lite.getTitle());
            v.setUrl(lite.getUrl());
            if (lite.getCover() != null && !lite.getCover().isEmpty()){
                v.setCover(lite.getCover());
            }
            if (lite.getDuration() != null && !lite.getDuration().isEmpty()){
                v.setDuration(lite.getDuration());
            }
            if (lite.getViews() != null && !lite.getViews().isEmpty()){
                v.setViews(lite.getViews());
            }
            if (lite.getSource() != null && !lite.getSource().isEmpty()){
                v.setSource(lite.getSource());
            }
            if (lite.getRating() != null && !lite.getRating().isEmpty()){
                v.setRating(lite.getRating());
            }
            FavoriteVideoDAO.insertIfTitleNotExists(v);
            applyFavoriteStyle(true);
        } else {
            FavoriteVideoDAO.deleteByTitle(videoTitle);
            applyFavoriteStyle(false);
        }
    }

    private void applyFavoriteStyle(boolean fav) {
        currentFav = fav;
        if (favoriteButton == null) return;
        favoriteButton.setImageResource(
                fav ? cfg.favoriteIconSelected : cfg.favoriteIconUnselected
        );
        int tintColor = fav
                ? cfg.favoriteIconColor
                : (cfg.favoriteIconColor & 0x00FFFFFF) | 0x4D000000;
        favoriteButton.setColorFilter(tintColor);
        resizeImageButton(favoriteButton);
    }

    // ============================================================
    // 下载
    // ============================================================
    private void onDownloadClicked() {
        if (profile == null) return;
        downloadGroupOpen = !downloadGroupOpen;
        downloadLaGroup.setVisibility(downloadGroupOpen ? View.VISIBLE : View.GONE);
        if (listener != null) listener.onDownloadClick(profile, downloadGroupOpen);
    }

    // ============================================================
    // 简介箭头
    // ============================================================
    private void toggleBiography() {
        if (arrowImageView.getVisibility() != View.VISIBLE) return;
        bioExpanded = !bioExpanded;
        syncArrow();

        if (bioExpanded) {
            biographyGroup.setVisibility(View.VISIBLE);
            biographyView.measure(
                    View.MeasureSpec.makeMeasureSpec(getWidth(), View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.UNSPECIFIED
            );
            int target = biographyView.getMeasuredHeight()
                    + biographyGroup.getPaddingTop()
                    + biographyGroup.getPaddingBottom();
            biographyGroup.getLayoutParams().height = 0;
            animateHeight(0, target, () -> {
                biographyGroup.getLayoutParams().height = target;
                biographyGroup.requestLayout();
            });
        } else {
            int start = biographyGroup.getHeight();
            animateHeight(start, 0, () -> {
                biographyGroup.setVisibility(View.GONE);
                biographyGroup.getLayoutParams().height = 0;
                biographyGroup.requestLayout();
            });
        }
    }

    private void animateHeight(int from, int to, Runnable onEnd) {
        android.animation.ValueAnimator a = android.animation.ValueAnimator.ofInt(from, to);
        a.setDuration(250);
        a.addUpdateListener(anim -> {
            biographyGroup.getLayoutParams().height = (int) anim.getAnimatedValue();
            biographyGroup.requestLayout();
        });
        a.addListener(new android.animation.AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(android.animation.Animator animation) {
                onEnd.run();
            }
        });
        a.start();
    }

    private void syncArrow() {
        if (arrowImageView == null) return;
        arrowImageView.setImageResource(
                bioExpanded ? cfg.arrowExpanded : cfg.arrowCollapsed
        );
        if (cfg.arrowColor != 0) arrowImageView.setColorFilter(cfg.arrowColor);
    }

    public void setArrowExpanded(boolean expanded) {
        if (bioExpanded == expanded) return;
        bioExpanded = expanded;
        syncArrow();
        biographyGroup.setVisibility(expanded ? View.VISIBLE : View.GONE);
    }

    // ============================================================
    // Config → View 映射
    // ============================================================
    private void applyConfig() {
        if (cfg == null) return;

        if (mainLayout != null) mainLayout.setBackgroundColor(cfg.backgroundColor);

        if (titleView != null) {
            titleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, cfg.titleTextSize);
            titleView.setTextColor(cfg.titleTextColor);
        }

        if (arrowImageView != null) {
            int px = dp(cfg.arrowSize);
            ViewGroup.LayoutParams lp = arrowImageView.getLayoutParams();
            if (lp != null) {
                lp.width = px;
                lp.height = px;
                arrowImageView.setLayoutParams(lp);
            }
        }

        if (coverView != null) {
            ViewGroup.LayoutParams lp = coverView.getLayoutParams();
            if (lp != null) {
                lp.width = MATCH_PARENT;
                lp.height = dp(cfg.coverHeight);
                coverView.setLayoutParams(lp);
            }
            coverView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }

        if (favoriteButton != null) {
            favoriteButton.setImageResource(cfg.favoriteIconUnselected);
            favoriteButton.setColorFilter((cfg.favoriteIconColor & 0x00FFFFFF) | 0x4D000000);
            resizeImageButton(favoriteButton);
        }
        applyIconButtonUI(downloadButton, cfg.downloadIcon, cfg.downloadIconColor);

        if (biographyView != null) {
            biographyView.setTextSize(TypedValue.COMPLEX_UNIT_SP, cfg.biographyTextSize);
            biographyView.setTextColor(cfg.biographyTextColor);
        }
    }

    private void applyIconButtonUI(AppCompatImageButton btn, int iconRes, int tint) {
        if (btn == null) return;
        btn.setImageResource(iconRes);
        btn.setColorFilter(tint);
        btn.setScaleType(ImageView.ScaleType.FIT_CENTER);
        btn.setBackgroundResource(android.R.color.transparent);
        resizeImageButton(btn);
    }

    private void resizeImageButton(AppCompatImageButton btn) {
        if (btn == null) return;
        int px = dp(cfg.iconButtonSize);
        ViewGroup.LayoutParams lp = btn.getLayoutParams();
        if (lp == null) {
            lp = new LinearLayout.LayoutParams(px, px);
        } else {
            lp.width = px;
            lp.height = px;
        }
        btn.setLayoutParams(lp);
    }

    // ============================================================
    // 工具方法
    // ============================================================
    private void setGroupVisible(ViewGroup g, boolean v) {
        if (g != null) g.setVisibility(v ? View.VISIBLE : View.GONE);
    }

    private boolean has(String s) {
        return s != null && !s.trim().isEmpty();
    }

    private boolean nullOrEmpty(List<?> l) {
        return l == null || l.isEmpty();
    }

    private int dp(int v) {
        return (int) (v * getResources().getDisplayMetrics().density + 0.5f);
    }
}