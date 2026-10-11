package com.example.pornhelper.magnet;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

/**
 * ****************************
 * 描述：磁力链接信息
 * ****************************
 */
public class Magnet implements Parcelable {

    /** 标题 */
    private String title;

    /** 文件大小 */
    private String fileSize;

    /** 下载次数 */
    private String downloads;

    /** 地址 */
    private String url;

    // ==================== 构造方法 ====================

    /** 无参构造 */
    public Magnet() {}

    /**
     * 有参构造
     * ⚠️ 参数顺序严格与字段定义一致
     */
    public Magnet(String title, String fileSize, String downloads, String url) {
        this.title = title;
        this.fileSize = fileSize;
        this.downloads = downloads;
        this.url = url;
    }

    // ==================== Parcelable 实现 ====================

    /** 从 Parcel 中读取数据 */
    protected Magnet(Parcel in) {
        title = in.readString();
        fileSize = in.readString();
        downloads = in.readString();
        url = in.readString();
    }

    /** CREATOR：用于从 Parcel 创建对象 */
    public static final Creator<Magnet> CREATOR = new Creator<Magnet>() {
        @Override
        public Magnet createFromParcel(Parcel in) {
            return new Magnet(in);
        }

        @Override
        public Magnet[] newArray(int size) {
            return new Magnet[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(fileSize);
        dest.writeString(downloads);
        dest.writeString(url);
    }

    // ==================== Getter / Setter ====================

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getDownloads() {
        return downloads;
    }

    public void setDownloads(String downloads) {
        this.downloads = downloads;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    // ==================== 日志 / 对比方法 ====================

    public String toLogString() {
        return "Magnet{title='" + title
                + "', fileSize='" + fileSize
                + "', downloads='" + downloads
                + "', url='" + url + "'}";
    }

    /**
     * 判断内容是否相同（排除 ID/标题等唯一标识时可用）
     */
    public boolean isContentSame(Magnet other) {
        if (other == null) return false;
        return Objects.equals(title, other.title)
                && Objects.equals(fileSize, other.fileSize)
                && Objects.equals(downloads, other.downloads)
                && Objects.equals(url, other.url);
    }

    // ==================== equals / hashCode / toString ====================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Magnet magnet = (Magnet) o;
        return Objects.equals(title, magnet.title)
                && Objects.equals(fileSize, magnet.fileSize)
                && Objects.equals(downloads, magnet.downloads)
                && Objects.equals(url, magnet.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, fileSize, downloads, url);
    }

}
