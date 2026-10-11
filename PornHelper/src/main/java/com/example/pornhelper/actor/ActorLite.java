package com.example.pornhelper.actor;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

/**
 * ****************************
 * 描述：演员信息-精简版
 * ****************************
 */
public class ActorLite implements Parcelable {

    /** 演员ID */
    private String actorId;

    /** 演员姓名 */
    private String name;

    /** 演员头像 */
    private String avatar;

    /** 视频数量 */
    private String videoCount;

    /** 观看次数 */
    private String views;

    /** 评分 */
    private String rating;

    /** 排名 */
    private String rank;

    /** 是否已收藏 */
    private boolean favorite;

    /** 主页地址 */
    private String url;

    // ==================== 构造方法 ====================

    /** 无参构造 */
    public ActorLite() {}

    /** 有参构造 */
    public ActorLite(String actorId, String name, String avatar, String videoCount,
                     String views, String rating, String rank, boolean favorite, String url) {
        this.actorId = actorId;
        this.name = name;
        this.avatar = avatar;
        this.videoCount = videoCount;
        this.views = views;
        this.rating = rating;
        this.rank = rank;
        this.favorite = favorite;
        this.url = url;
    }

    // ==================== Parcelable 实现 ====================

    /** 从 Parcel 中读取数据 */
    protected ActorLite(Parcel in) {
        actorId = in.readString();
        name = in.readString();
        avatar = in.readString();
        videoCount = in.readString();
        views = in.readString();
        rating = in.readString();
        rank = in.readString();
        favorite = in.readByte() != 0;
        url = in.readString();
    }

    /** CREATOR：用于从 Parcel 创建对象 */
    public static final Creator<ActorLite> CREATOR = new Creator<ActorLite>() {
        @Override
        public ActorLite createFromParcel(Parcel in) {
            return new ActorLite(in);
        }

        @Override
        public ActorLite[] newArray(int size) {
            return new ActorLite[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(actorId);
        dest.writeString(name);
        dest.writeString(avatar);
        dest.writeString(videoCount);
        dest.writeString(views);
        dest.writeString(rating);
        dest.writeString(rank);
        dest.writeByte((byte) (favorite ? 1 : 0));
        dest.writeString(url);
    }

    // ==================== Getter / Setter ====================

    public String getActorId() { return actorId; }
    public void setActorId(String actorId) { this.actorId = actorId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }

    public String getVideoCount() { return videoCount; }
    public void setVideoCount(String videoCount) { this.videoCount = videoCount; }

    public String getViews() { return views; }
    public void setViews(String views) { this.views = views; }

    public String getRating() { return rating; }
    public void setRating(String rating) { this.rating = rating; }

    public String getRank() { return rank; }
    public void setRank(String rank) { this.rank = rank; }


    public boolean isFavorite() { return favorite; }
    public void setFavorite(boolean favorite) { this.favorite = favorite; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    // ==================== 返回类对象数据 ====================

    public String toLogString() {
        return "ActorLite{actorId='" + actorId
                + "', name='" + name
                + "', avatar='" + avatar
                + "', videoCount='" + videoCount
                + "', views='" + views
                + "', rating='" + rating
                + "', rank='" + rank
                + "', favorite=" + favorite
                + ", url='" + url + "'}";
    }

    // ==================== equals / hashCode ====================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActorLite actorLite = (ActorLite) o;
        return favorite == actorLite.favorite
                && Objects.equals(name, actorLite.name)
                && Objects.equals(avatar, actorLite.avatar)
                && Objects.equals(videoCount, actorLite.videoCount)
                && Objects.equals(views, actorLite.views)
                && Objects.equals(rating, actorLite.rating)
                && Objects.equals(rank, actorLite.rank)
                && Objects.equals(url, actorLite.url);
    }

    public boolean isContentSame(ActorLite other) {
        return favorite == other.favorite
                && Objects.equals(name, other.name)
                && Objects.equals(avatar, other.avatar)
                && Objects.equals(videoCount, other.videoCount)
                && Objects.equals(views, other.views)
                && Objects.equals(rating, other.rating)
                && Objects.equals(rank, other.rank)
                && Objects.equals(url, other.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, avatar, videoCount, views, rating, rank, favorite, url);
    }
}