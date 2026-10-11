package com.example.pornhelper.studio;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.Objects;

/**
 * ****************************
 * 描述：片商信息表-精简版
 * 架构对齐：ActorLite / VideoLite
 * 约定：favorite 字段位于 url 之前
 * ****************************
 */
public class Studio implements Parcelable {

    // ==================== 字段定义 ====================

    /** 片商ID */
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

    /** 收藏状态 */
    private boolean favorite;

    /** 片商地址 */
    private String url;

    // ==================== 构造方法 ====================

    /** 无参构造 */
    public Studio() {
    }

    /** 全参构造（字段顺序与声明顺序一致） */
    public Studio(String studioId, String name, String logo, String videoCount, String rating, String rank,
                  boolean favorite, String url) {
        this.studioId = studioId;
        this.name = name;
        this.logo = logo;
        this.videoCount = videoCount;
        this.rating = rating;
        this.rank = rank;
        this.favorite = favorite;
        this.url = url;
    }

    // ==================== Parcelable 实现 ====================

    /** 从 Parcel 中读取数据（顺序必须与字段声明一致） */
    protected Studio(Parcel in) {
        studioId = in.readString();
        name = in.readString();
        logo = in.readString();
        videoCount = in.readString();
        rating = in.readString();
        rank = in.readString();
        favorite = in.readByte() != 0;
        url = in.readString();
    }

    /** CREATOR：用于从 Parcel 创建对象 */
    public static final Creator<Studio> CREATOR = new Creator<Studio>() {
        @Override
        public Studio createFromParcel(Parcel in) {
            return new Studio(in);
        }

        @Override
        public Studio[] newArray(int size) {
            return new Studio[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    /** 将对象写入 Parcel（顺序必须与字段声明一致） */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(studioId);
        dest.writeString(name);
        dest.writeString(logo);
        dest.writeString(videoCount);
        dest.writeString(rating);
        dest.writeString(rank);
        dest.writeByte((byte) (favorite ? 1 : 0));
        dest.writeString(url);
    }

    // ==================== Getter / Setter ====================

    /**
     * 获取片商ID
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

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    // ==================== 日志 / 调试方法 ====================

    /**
     * 返回当前类对象的字段信息字符串
     */
    public String toLogString() {
        return "Studio{studioId='" + studioId
                + "', name='" + name
                + "', logo='" + logo
                + "', videoCount='" + videoCount
                + "', rating='" + rating
                + "', rank='" + rank
                + ", favorite=" + favorite
                + "', url='" + url + "'}";
    }

    // ==================== equals / hashCode ====================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Studio that)) return false;
        return favorite == that.favorite
                && Objects.equals(studioId, that.studioId)
                && Objects.equals(name, that.name)
                && Objects.equals(logo, that.logo)
                && Objects.equals(videoCount, that.videoCount)
                && Objects.equals(rating, that.rating)
                && Objects.equals(rank, that.rank)
                && Objects.equals(url, that.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                studioId,
                name,
                logo,
                videoCount,
                rating,
                rank,
                favorite,
                url
        );
    }

    // ==================== DiffUtil 辅助方法 ====================

    /**
     * 用于 RecyclerView DiffUtil
     * 判断内容是否相同（不包含 ID / URL 等标识字段）
     */
    public boolean isContentSame(@NonNull Studio other) {
        return Objects.equals(studioId, other.studioId)
                && Objects.equals(name, other.name)
                && Objects.equals(logo, other.logo)
                && Objects.equals(videoCount, other.videoCount)
                && Objects.equals(rating, other.rating)
                && Objects.equals(rank, other.rank)
                && favorite == other.favorite;
    }
}