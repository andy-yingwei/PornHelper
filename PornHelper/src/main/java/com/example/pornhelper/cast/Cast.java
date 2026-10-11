package com.example.pornhelper.cast;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

/**
 * ****************************
 * 描述：视频参演人员信息
 * ****************************
 */
public class Cast implements Parcelable {

    /** 演员ID */
    private String id;

    /** 姓名 */
    private String name;

    /** 头像 */
    private String avatar;

    /** 主页地址 */
    private String url;

    // ==================== 构造方法 ====================

    /** 无参构造 */
    public Cast() {}

    /** 有参构造 */
    public Cast(String id, String name, String avatar, String url) {
        this.id = id;
        this.name = name;
        this.avatar = avatar;
        this.url = url;
    }

    // ==================== Parcelable 实现 ====================

    /** 从 Parcel 中读取数据 */
    protected Cast(Parcel in) {
        id = in.readString();
        name = in.readString();
        avatar = in.readString();
        url = in.readString();
    }

    /** CREATOR：用于从 Parcel 创建对象 */
    public static final Creator<Cast> CREATOR = new Creator<Cast>() {
        @Override
        public Cast createFromParcel(Parcel in) {
            return new Cast(in);
        }

        @Override
        public Cast[] newArray(int size) {
            return new Cast[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(avatar);
        dest.writeString(url);
    }

    // ==================== Getter / Setter ====================

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    // ==================== 日志 / 对比方法 ====================

    public String toLogString() {
        return "Cast{ id='" + id
                + "', name='" + name
                + "', avatar='" + avatar
                + "', url='" + url + "'}";
    }

    public boolean isContentSame(Cast other) {
        if (other == null) return false;
        return Objects.equals(name, other.name)
                && Objects.equals(avatar, other.avatar)
                && Objects.equals(url, other.url);
    }

    // ==================== equals / hashCode ====================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cast cast = (Cast) o;
        return Objects.equals(name, cast.name)
                && Objects.equals(avatar, cast.avatar)
                && Objects.equals(url, cast.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, avatar, url);
    }
}
