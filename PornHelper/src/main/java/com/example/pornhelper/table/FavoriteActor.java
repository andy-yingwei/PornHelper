package com.example.pornhelper.table;

import org.litepal.crud.LitePalSupport;

/**
 * ****************************
 * 描述：收藏演员信息表
 * ****************************
 */
public class FavoriteActor extends LitePalSupport {

    /** 演员唯一ID */
    private String actorId;

    /** 演员姓名 */
    private String name;

    /** 演员头像图片地址 */
    private String avatar;

    /** 演员排名 */
    private String rank;

    /** 演员视频数量 */
    private String videoCount;

    /** 演员总观看量 */
    private String views;

    /** 演员评分 */
    private String rating;

    /** 演员详情页跳转地址 */
    private String url;

    // ==================== 构造方法 ====================

    /** 无参构造 */
    public FavoriteActor() {}

    /** 有参构造 */
    public FavoriteActor(String actorId, String name, String avatar, String rank,
                         String videoCount, String views, String rating, String url) {
        this.actorId = actorId;
        this.name = name;
        this.avatar = avatar;
        this.rank = rank;
        this.videoCount = videoCount;
        this.views = views;
        this.rating = rating;
        this.url = url;
    }

    // ==================== Getter / Setter ====================

    /**
     * 获取演员唯一ID
     * @return 演员ID
     */
    public String getActorId() {
        return actorId;
    }

    /**
     * 设置演员唯一ID
     */
    public void setActorId(String actorId) {
        this.actorId = actorId;
    }

    /**
     * 获取演员姓名
     * @return 姓名
     */
    public String getName() {
        return name;
    }

    /**
     * 设置演员姓名
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取演员头像
     * @return 头像URL
     */
    public String getAvatar() {
        return avatar;
    }

    /**
     * 设置演员头像
     */
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    /**
     * 获取演员排名
     * @return 排名原始文本
     */
    public String getRank() {
        return rank;
    }

    /**
     * 设置演员排名
     */
    public void setRank(String rank) {
        this.rank = rank;
    }

    /**
     * 获取演员视频数量
     * @return 视频数量原始文本
     */
    public String getVideoCount() {
        return videoCount;
    }

    /**
     * 设置演员视频数量
     */
    public void setVideoCount(String videoCount) {
        this.videoCount = videoCount;
    }

    /**
     * 获取演员观看量
     * @return 观看量原始文本
     */
    public String getViews() {
        return views;
    }

    /**
     * 设置演员观看量
     */
    public void setViews(String views) {
        this.views = views;
    }

    /**
     * 获取演员评分
     * @return 评分原始文本
     */
    public String getRating() {
        return rating;
    }

    /**
     * 设置演员评分
     */
    public void setRating(String rating) {
        this.rating = rating;
    }

    /**
     * 获取演员详情页地址
     * @return 跳转URL
     */
    public String getUrl() {
        return url;
    }

    /**
     * 设置演员详情页地址
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
        return "FavoriteActor{ actorId='" + actorId
                + "', name='" + name
                + "', avatar='" + avatar
                + "', rank='" + rank
                + "', videoCount='" + videoCount
                + "', views='" + views
                + "', rating='" + rating
                + "', url='" + url + "'}";
    }
}

