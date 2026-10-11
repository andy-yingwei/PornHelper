package com.example.pornhelper.table;

import org.litepal.crud.LitePalSupport;

/**
 * ****************************
 * 描述：收藏片商信息表
 * ****************************
 */
public class FavoriteStudio extends LitePalSupport {

    /** 片商ID（String 类型，手动主键） */
    private String studioId;

    /** 片商名称 */
    private String name;

    /** 片商图片 */
    private String logo;

    /** 片商视频数量 */
    private String videoCount;

    /** 片商好评 */
    private String rating;

    /** 片商排名 */
    private String rank;

    /** 片商地址 */
    private String url;

    // ==================== 构造方法 ====================

    public FavoriteStudio() {}

    public FavoriteStudio(String studioId, String name, String logo, String videoCount,
                          String rating, String rank, String url) {
        this.studioId = studioId;
        this.name = name;
        this.logo = logo;
        this.videoCount = videoCount;
        this.rating = rating;
        this.rank = rank;
        this.url = url;
    }

    // ==================== Getter / Setter ====================

    /**
     * 获取片商ID
     * @return 片商ID
     */
    public String getStudioId() {
        return studioId;
    }

    /**
     * 设置片商ID
     */
    public void setStudioId(String studioId) {
        this.studioId = studioId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getVideoCount() {
        return videoCount;
    }

    public void setVideoCount(String videoCount) {
        this.videoCount = videoCount;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    // ==================== 调试输出 ====================

    public String toLogString() {
        return "FavoriteStudio{studioId='" + studioId
                + "', name='" + name
                + "', logo='" + logo
                + "', videoCount='" + videoCount
                + "', rating='" + rating
                + "', rank='" + rank
                + "', url='" + url + "'}";
    }
}
