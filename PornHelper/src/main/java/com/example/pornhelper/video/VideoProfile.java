package com.example.pornhelper.video;

import com.example.pornhelper.cast.Cast;
import com.example.pornhelper.category.Category;
import com.example.pornhelper.studio.Studio;
import com.example.pornhelper.magnet.Magnet;
import com.example.pornhelper.playSource.PlaySource;
import com.example.pornhelper.tag.Tag;

import java.util.List;

/**
 * ****************************
 * 描述：视频信息-完整资料
 * ****************************
 */
public class VideoProfile {

    /** 视频信息-精简版 */
    private VideoLite videoLite;

    /** 视频简介 */
    private String summary;

    /** 导演 */
    private String director;

    /** 分类 */
    private Category category;

    /** 出品公司 */
    private Studio studio;

    /** 视频截图/预览图列表 */
    private List<String> screenshots;

    /** 演员列表 */
    private List<Cast> castList;

    /** 标签列表 */
    private List<Tag> tagList;

    /** 磁力链接列表 */
    private List<Magnet> magnetList;

    /** M3U8 列表 */
    private List<PlaySource> playSourceList;

    /** 视频地址 */
    private String url;

    // ==================== 构造方法 ====================

    /** 无参构造 */
    public VideoProfile() {}

    /** 有参构造 */
    public VideoProfile(VideoLite videoLite, String summary, String director,
                        Category category, Studio studio, List<String> screenshots,
                        List<Cast> castList, List<Tag> tagList,
                        List<Magnet> magnetList, List<PlaySource> playSourceList, String url) {
        this.videoLite = videoLite;
        this.summary = summary;
        this.director = director;
        this.category = category;
        this.studio = studio;
        this.screenshots = screenshots;
        this.castList = castList;
        this.tagList = tagList;
        this.magnetList = magnetList;
        this.playSourceList = playSourceList;
        this.url = url;
    }

    // ==================== Getter / Setter ====================

    /**
     * 获取视频信息-精简版
     * @return 视频信息-精简版
     */
    public VideoLite getVideoLite() {
        return videoLite;
    }

    /**
     * 设置视频信息-精简版
     */
    public void setVideoLite(VideoLite videoLite) {
        this.videoLite = videoLite;
    }

    /**
     * 获取视频简介
     * @return 视频简介
     */
    public String getSummary() {
        return summary;
    }

    /**
     * 设置视频简介
     */
    public void setSummary(String summary) {
        this.summary = summary;
    }

    /**
     * 获取导演
     * @return 导演
     */
    public String getDirector() {
        return director;
    }

    /**
     * 设置导演
     */
    public void setDirector(String director) {
        this.director = director;
    }

    /**
     * 获取分类
     * @return 分类
     */
    public Category getCategory() {
        return category;
    }

    /**
     * 设置分类
     */
    public void setCategory(Category category) {
        this.category = category;
    }

    /**
     * 获取出品公司
     * @return 出品公司
     */
    public Studio getStudio() {
        return studio;
    }

    /**
     * 设置出品公司
     */
    public void setStudio(Studio studio) {
        this.studio = studio;
    }

    /**
     * 获取视频截图/预览图列表
     * @return 视频截图/预览图列表
     */
    public List<String> getScreenshots() {
        return screenshots;
    }

    /**
     * 设置视频截图/预览图列表
     */
    public void setScreenshots(List<String> screenshots) {
        this.screenshots = screenshots;
    }

    /**
     * 获取演员列表
     * @return 演员列表
     */
    public List<Cast> getCastList() {
        return castList;
    }

    /**
     * 设置演员列表
     */
    public void setCastList(List<Cast> castList) {
        this.castList = castList;
    }

    /**
     * 获取标签列表
     * @return 标签列表
     */
    public List<Tag> getTagList() {
        return tagList;
    }

    /**
     * 设置标签列表
     */
    public void setTagList(List<Tag> tagList) {
        this.tagList = tagList;
    }

    /**
     * 获取磁力链接列表
     * @return 磁力链接列表
     */
    public List<Magnet> getMagnetList() {
        return magnetList;
    }

    /**
     * 设置磁力链接列表
     */
    public void setMagnetList(List<Magnet> magnetList) {
        this.magnetList = magnetList;
    }

    /**
     * 获取 M3U8 列表
     * @return M3U8 列表
     */
    public List<PlaySource> getPlaySourceList() {
        return playSourceList;
    }

    /**
     * 设置 M3U8 列表
     */
    public void setPlaySourceList(List<PlaySource> playSourceList) {
        this.playSourceList = playSourceList;
    }

    /**
     * 获取视频地址
     * @return 视频地址
     */
    public String getUrl() {
        return url;
    }

    /**
     * 设置视频地址
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
        return "VideoProfile{videoLite=" + (videoLite != null ? videoLite.toLogString() : "null")
                + ", summary='" + summary
                + "', director='" + director
                + "', category=" + (category != null ? category.toLogString() : "null")
                + ", studio=" + (studio != null ? studio.toLogString() : "null")
                + ", screenshots=" + (screenshots != null ? "size=" + screenshots.size() : "null")
                + ", castList=" + (castList != null ? "size=" + castList.size() : "null")
                + ", tagList=" + (tagList != null ? "size=" + tagList.size() : "null")
                + ", magnetList=" + (magnetList != null ? "size=" + magnetList.size() : "null")
                + ", m3u8List=" + (playSourceList != null ? "size=" + playSourceList.size() : "null")
                + ", url='" + url + "'}";
    }
}
