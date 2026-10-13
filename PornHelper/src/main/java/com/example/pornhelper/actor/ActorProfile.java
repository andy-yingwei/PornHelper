package com.example.pornhelper.actor;

import com.example.pornhelper.video.VideoLite;

import java.util.List;

/**
 * ****************************
 * 描述：演员信息-详细资料
 * ****************************
 */
public class ActorProfile {

    /** 演员信息-精简版 */
    private ActorLite actorLite;

    /** 演员视频列表 */
    private List<VideoLite> videoList;

    /** 性别 */
    private String gender;

    /** 年龄 */
    private String age;

    /** 身高 */
    private String height;

    /** 体重 */
    private String weight;

    /** 胸围 */
    private String bust;

    /** 臀围 */
    private String hips;

    /** 生日 */
    private String birthday;

    /** 出生地 */
    private String birthplace;

    /** 国籍 */
    private String nationality;

    /** 简介 */
    private String biography;

    /** 标签 */
    private String tags;

    // ==================== 构造方法 ====================

    /** 无参构造 */
    public ActorProfile() {}

    /** 有参构造 */
    public ActorProfile(ActorLite actorLite, List<VideoLite> videoList,
                        String gender, String age, String height, String weight,
                        String bust, String hips, String birthday, String birthplace,
                        String nationality, String biography, String tags) {
        this.actorLite = actorLite;
        this.videoList = videoList;
        this.gender = gender;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.bust = bust;
        this.hips = hips;
        this.birthday = birthday;
        this.birthplace = birthplace;
        this.nationality = nationality;
        this.biography = biography;
        this.tags = tags;
    }

    // ==================== Getter / Setter ====================

    public ActorLite getActorLite() {
        return actorLite;
    }

    public void setActorLite(ActorLite actorLite) {
        this.actorLite = actorLite;
    }

    public List<VideoLite> getVideoList() {
        return videoList;
    }

    public void setVideoList(List<VideoLite> videoList) {
        this.videoList = videoList;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getBust() {
        return bust;
    }

    public void setBust(String bust) {
        this.bust = bust;
    }

    public String getHips() {
        return hips;
    }

    public void setHips(String hips) {
        this.hips = hips;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getBirthplace() {
        return birthplace;
    }

    public void setBirthplace(String birthplace) {
        this.birthplace = birthplace;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    /** 获取标签 */
    public String getTags() {
        return tags;
    }

    /** 设置标签 */
    public void setTags(String tags) {
        this.tags = tags;
    }

    // ==================== 返回类对象数据 ====================

    public String toLogString() {
        return "ActorProfile{" + (actorLite != null ? actorLite.toLogString() : "null")
                + ", videoList=" + (videoList != null ? "size=" + videoList.size() : "null")
                + "', gender='" + gender
                + "', age='" + age
                + "', height='" + height
                + "', weight='" + weight
                + "', bust='" + bust
                + "', hips='" + hips
                + "', birthday='" + birthday
                + "', birthplace='" + birthplace
                + "', nationality='" + nationality
                + "', biography='" + biography
                + "', tags='" + tags + "}";
    }
}