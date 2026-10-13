package com.example.pornhelper.video;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

/**
 * ****************************
 * 描述：视频信息-精简版（架构对齐 ActorLite）
 * ****************************
 */
public class VideoLite implements Parcelable {

    /** 视频ID */
    private String videoId;

    /** 视频标题 */
    private String title;

    /** 视频封面 */
    private String cover;

    /** 视频时长 */
    private String duration;

    /** 视频观看量 */
    private String views;

    /** 视频收藏量 */
    private String favorites;

    /** 视频评分 */
    private String rating;

    /** 视频来源 */
    private String source;

    /** 视频上架日期 */
    private String releaseDate;

    /** 是否已收藏 */
    private boolean favorite;

    /** 视频地址 */
    private String url;

    // ==================== 构造方法 ====================

    /** 无参构造 */
    public VideoLite() {}

    /** 有参构造（含 favorite，favorite 在 url 前） */
    public VideoLite(String videoId, String title, String cover, String duration, String views,
                     String favorites, String rating, String source, String releaseDate,
                     boolean favorite, String url) {
        this.videoId = videoId;
        this.title = title;
        this.cover = cover;
        this.duration = duration;
        this.views = views;
        this.favorites = favorites;
        this.rating = rating;
        this.source = source;
        this.releaseDate = releaseDate;
        this.favorite = favorite;
        this.url = url;
    }

    // ==================== Parcelable 实现 ====================

    /** 从 Parcel 中读取数据（顺序与字段声明一致） */
    protected VideoLite(Parcel in) {
        videoId = in.readString();
        title = in.readString();
        cover = in.readString();
        duration = in.readString();
        views = in.readString();
        favorites = in.readString();
        rating = in.readString();
        source = in.readString();
        releaseDate = in.readString();
        favorite = in.readByte() != 0;   // favorite 在 url 前
        url = in.readString();
    }

    /** CREATOR */
    public static final Creator<VideoLite> CREATOR = new Creator<VideoLite>() {
        @Override
        public VideoLite createFromParcel(Parcel in) {
            return new VideoLite(in);
        }

        @Override
        public VideoLite[] newArray(int size) {
            return new VideoLite[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(videoId);
        dest.writeString(title);
        dest.writeString(cover);
        dest.writeString(duration);
        dest.writeString(views);
        dest.writeString(favorites);
        dest.writeString(rating);
        dest.writeString(source);
        dest.writeString(releaseDate);
        dest.writeByte((byte) (favorite ? 1 : 0));  // favorite 在 url 前
        dest.writeString(url);
    }

    // ==================== Getter / Setter ====================

    /**
     * 获取视频ID
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

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getCover() { return cover; }
    public void setCover(String cover) { this.cover = cover; }

    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }

    public String getViews() { return views; }
    public void setViews(String views) { this.views = views; }

    public String getFavorites() { return favorites; }
    public void setFavorites(String favorites) { this.favorites = favorites; }

    public String getRating() { return rating; }
    public void setRating(String rating) { this.rating = rating; }

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }

    public String getReleaseDate() { return releaseDate; }
    public void setReleaseDate(String releaseDate) { this.releaseDate = releaseDate; }

    /** 是否已收藏 */
    public boolean isFavorite() { return favorite; }
    public void setFavorite(boolean favorite) { this.favorite = favorite; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    // ==================== 返回类对象数据 ====================

    public String toLogString() {
        return "VideoLite{videoId='" + videoId
                + "', title='" + title
                + "', cover='" + cover
                + "', duration='" + duration
                + "', views='" + views
                + "', favorites='" + favorites
                + "', rating='" + rating
                + "', source='" + source
                + "', releaseDate='" + releaseDate
                + "', favorite=" + favorite
                + ", url='" + url + "'}";
    }

    // ==================== equals / hashCode（对齐 ActorLite） ====================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VideoLite that = (VideoLite) o;
        return favorite == that.favorite
                && Objects.equals(videoId, that.videoId)
                && Objects.equals(title, that.title)
                && Objects.equals(cover, that.cover)
                && Objects.equals(duration, that.duration)
                && Objects.equals(views, that.views)
                && Objects.equals(favorites, that.favorites)
                && Objects.equals(rating, that.rating)
                && Objects.equals(source, that.source)
                && Objects.equals(releaseDate, that.releaseDate)
                && Objects.equals(url, that.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(videoId, title, cover, duration, views, favorites, rating,
                source, releaseDate, favorite, url);
    }

    /**
     * 判断内容是否相同（用于 DiffUtil，对齐 ActorLite.isContentSame）
     */
    public boolean isContentSame(VideoLite other) {
        if (other == null) return false;
        return favorite == other.favorite
                && Objects.equals(videoId, other.videoId)
                && Objects.equals(title, other.title)
                && Objects.equals(cover, other.cover)
                && Objects.equals(duration, other.duration)
                && Objects.equals(views, other.views)
                && Objects.equals(favorites, other.favorites)
                && Objects.equals(rating, other.rating)
                && Objects.equals(source, other.source)
                && Objects.equals(releaseDate, other.releaseDate);
    }
}