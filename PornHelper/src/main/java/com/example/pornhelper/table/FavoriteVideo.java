package com.example.pornhelper.table;

import org.litepal.crud.LitePalSupport;

/**
 * ****************************
 * 描述：收藏视频信息表
 * ****************************
 */
public class FavoriteVideo extends LitePalSupport {

    /** 视频ID */
    private String videoId;

    /** 视频名称 */
    private String title;

    /** 视频封面 */
    private String cover;

    /** 视频时长 */
    private String duration;

    /** 视频观看量 */
    private String views;

    /** 视频来源 */
    private String source;

    /** 视频评分 */
    private String rating;

    /** 视频地址 */
    private String url;

    // ==================== 构造方法 ====================

    /** 无参构造 */
    public FavoriteVideo() {}

    /** 有参构造 */
    public FavoriteVideo(String videoId, String title, String cover, String duration, String views,
                         String source, String rating, String url) {
        this.videoId = videoId;
        this.title = title;
        this.cover = cover;
        this.duration = duration;
        this.views = views;
        this.source = source;
        this.rating = rating;
        this.url = url;
    }

    // ==================== Getter / Setter ====================

    /**
     * 获取视频ID
     * @return 视频ID
     */
    public String getVideoId() {
        return videoId;
    }

    /**
     * 设置视频ID
     */
    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    /**
     * 获取视频名称
     * @return 视频名称
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置视频名称
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 获取视频封面
     * @return 视频封面
     */
    public String getCover() {
        return cover;
    }

    /**
     * 设置视频封面
     */
    public void setCover(String cover) {
        this.cover = cover;
    }

    /**
     * 获取视频时长
     * @return 视频时长
     */
    public String getDuration() {
        return duration;
    }

    /**
     * 设置视频时长
     */
    public void setDuration(String duration) {
        this.duration = duration;
    }

    /**
     * 获取视频观看量
     * @return 视频观看量
     */
    public String getViews() {
        return views;
    }

    /**
     * 设置视频观看量
     */
    public void setViews(String views) {
        this.views = views;
    }

    /**
     * 获取视频来源
     * @return 视频来源
     */
    public String getSource() {
        return source;
    }

    /**
     * 设置视频来源
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * 获取视频评分
     * @return 视频评分
     */
    public String getRating() {
        return rating;
    }

    /**
     * 设置视频评分
     */
    public void setRating(String rating) {
        this.rating = rating;
    }

    /**
     * 获取视频地址
     * @return 视频地址
     */
    public String getUrl() {
        return url;
    }

    /**
     * 设置视频地址
     */
    public void setUrl(String url) {
        this.url = url;
    }

    // ==================== 返回类对象数据 ====================

    /**
     * 返回当前类对象的字段信息字符串
     * @return 字段数据字符串
     */
    public String toLogString() {
        return "FavoriteVideo{videoId='" + videoId
                + "', title='" + title
                + "', cover='" + cover
                + "', duration='" + duration
                + "', views='" + views
                + "', source='" + source
                + "', rating='" + rating
                + "', url='" + url + "'}";
    }
}
