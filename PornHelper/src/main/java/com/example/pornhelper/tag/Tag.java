package com.example.pornhelper.tag;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

/**
 * ****************************
 * 描述：标签信息
 * ****************************
 */
public class Tag implements Parcelable {

    /** 标签名称-英文 */
    private String englishName;

    /** 标签名称-中文 */
    private String chineseName;

    /** 标签视频数量 */
    private String videoCount;

    /** 标签地址 */
    private String url;

    // ==================== 构造方法 ====================

    /** 无参构造 */
    public Tag() {}

    /** 有参构造 */
    public Tag(String englishName, String chineseName, String videoCount, String url) {
        this.englishName = englishName;
        this.chineseName = chineseName;
        this.videoCount = videoCount;
        this.url = url;
    }

    // ==================== Parcelable 实现 ====================

    /** 从 Parcel 中读取数据 */
    protected Tag(Parcel in) {
        englishName = in.readString();
        chineseName = in.readString();
        videoCount = in.readString();
        url = in.readString();
    }

    /** CREATOR：用于从 Parcel 创建对象 */
    public static final Creator<Tag> CREATOR = new Creator<Tag>() {
        @Override
        public Tag createFromParcel(Parcel in) {
            return new Tag(in);
        }

        @Override
        public Tag[] newArray(int size) {
            return new Tag[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(englishName);
        dest.writeString(chineseName);
        dest.writeString(videoCount);
        dest.writeString(url);
    }

    // ==================== Getter / Setter ====================

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public String getChineseName() {
        return chineseName;
    }

    public void setChineseName(String chineseName) {
        this.chineseName = chineseName;
    }

    public String getVideoCount() {
        return videoCount;
    }

    public void setVideoCount(String videoCount) {
        this.videoCount = videoCount;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    // ==================== 日志 / 对比方法 ====================

    public String toLogString() {
        return "Tag{englishName='"
                + englishName + "', chineseName='"
                + chineseName + "', videoCount='"
                + videoCount + "', url='"
                + url + "'}";
    }

    /**
     * 判断内容是否相同
     */
    public boolean isContentSame(Tag other) {
        if (other == null) return false;
        return Objects.equals(englishName, other.englishName)
                && Objects.equals(chineseName, other.chineseName)
                && Objects.equals(videoCount, other.videoCount)
                && Objects.equals(url, other.url);
    }

    // ==================== equals / hashCode / toString ====================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return Objects.equals(englishName, tag.englishName)
                && Objects.equals(chineseName, tag.chineseName)
                && Objects.equals(videoCount, tag.videoCount)
                && Objects.equals(url, tag.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(englishName, chineseName, videoCount, url);
    }
}