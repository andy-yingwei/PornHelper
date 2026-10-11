package com.example.pornhelper.playSource;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

/**
 * ****************************
 * 描述：PlaySource 播放信息
 * ****************************
 */
public class PlaySource implements Parcelable {

    /** 视频清晰度（如：480P / 720P / 1080P / 4K） */
    private String label;
    /** 视频时长（单位：秒） */
    private String duration;
    /** 线路是否被选中 */
    private boolean selected;
    /** 线路地址 */
    private String url;

    // ==================== 构造方法 ====================

    /** 无参构造 */
    public PlaySource() {}

    /** 有参构造 */
    public PlaySource(String label, String duration, boolean selected, String url) {
        this.label = label;
        this.duration = duration;
        this.selected = selected;
        this.url = url;
    }

    // ==================== Parcelable 实现 ====================

    /** 从 Parcel 中读取数据 */
    protected PlaySource(Parcel in) {
        label = in.readString();
        duration = in.readString();
        selected = in.readByte() != 0; // boolean -> byte
        url = in.readString();
    }

    /** CREATOR：用于从 Parcel 创建对象 */
    public static final Creator<PlaySource> CREATOR = new Creator<PlaySource>() {
        @Override
        public PlaySource createFromParcel(Parcel in) {
            return new PlaySource(in);
        }

        @Override
        public PlaySource[] newArray(int size) {
            return new PlaySource[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(label);
        dest.writeString(duration);
        dest.writeByte((byte) (selected ? 1 : 0)); // boolean -> byte
        dest.writeString(url);
    }

    // ==================== Getter / Setter ====================

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    // ==================== 日志 / 对比方法 ====================

    public String toLogString() {
        return "PlayLine{label='" + label
                + "', duration='" + duration
                + "', selected=" + selected
                + ", url='" + url + "'}";
    }

    /**
     * 判断内容是否相同
     */
    public boolean isContentSame(PlaySource other) {
        if (other == null) return false;
        return Objects.equals(label, other.label)
                && Objects.equals(duration, other.duration)
                && selected == other.selected
                && Objects.equals(url, other.url);
    }

    // ==================== equals / hashCode / toString ====================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlaySource that = (PlaySource) o;
        return selected == that.selected
                && Objects.equals(label, that.label)
                && Objects.equals(duration, that.duration)
                && Objects.equals(url, that.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(label, duration, selected, url);
    }

}
