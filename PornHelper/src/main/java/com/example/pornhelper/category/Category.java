package com.example.pornhelper.category;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.Objects;

/**
 * ****************************
 * 描述：分类信息
 * ****************************
 */
public class Category implements Parcelable {

    // ==================== 字段定义 ====================

    /** 分类标题 */
    private String title;

    /** 分类封面 */
    private String cover;

    /** 分类视频数量 */
    private String videoCount;

    /** 评分 */
    private String rating;

    /** 分类地址 */
    private String url;

    // ==================== 构造方法 ====================

    /** 无参构造 */
    public Category() {
    }

    /**
     * 全参构造
     */
    public Category(String title, String cover, String videoCount, String rating, String url) {
        this.title = title;
        this.cover = cover;
        this.videoCount = videoCount;
        this.rating = rating;
        this.url = url;
    }

    // ==================== Parcelable 实现 ====================

    /**
     * 从 Parcel 中读取数据
     * 顺序：title → cover → videoCount → rating → url
     */
    protected Category(Parcel in) {
        title = in.readString();
        cover = in.readString();
        videoCount = in.readString();
        rating = in.readString();
        url = in.readString();
    }

    /** CREATOR */
    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * 写入 Parcel
     * 顺序：title → cover → videoCount → rating → url
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(cover);
        dest.writeString(videoCount);
        dest.writeString(rating);
        dest.writeString(url);
    }

    // ==================== Getter / Setter ====================

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    // ==================== 日志 / 调试 ====================

    public String toLogString() {
        return "Category{"
                + "title='" + title
                + "', cover='" + cover
                + "', videoCount='" + videoCount
                + "', rating='" + rating
                + "', url='" + url + "'}";
    }

    // ==================== equals / hashCode ====================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Category that)) return false;
        return Objects.equals(title, that.title)
                && Objects.equals(cover, that.cover)
                && Objects.equals(videoCount, that.videoCount)
                && Objects.equals(rating, that.rating)
                && Objects.equals(url, that.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, cover, videoCount, rating, url);
    }

    // ==================== DiffUtil 辅助 ====================

    /**
     * 判断内容是否相同（用于 DiffUtil）
     * 不包含 title / url 等标识字段
     */
    public boolean isContentSame(@NonNull Category other) {
        return Objects.equals(title, other.title)
                && Objects.equals(cover, other.cover)
                && Objects.equals(url, other.url);
    }
}
