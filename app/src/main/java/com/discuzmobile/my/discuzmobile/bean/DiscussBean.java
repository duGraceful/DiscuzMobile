package com.discuzmobile.my.discuzmobile.bean;

public class DiscussBean {
    private Long discuzId;
    private Long userId;
    private Long kindId;
    private String title;
    private String discussion;
    private Long reportTime;
    private String image;
    private String kindName;

    public Long getDiscuzId() {
        return discuzId;
    }

    public void setDiscuzId(Long discuzId) {
        this.discuzId = discuzId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getKindId() {
        return kindId;
    }

    public void setKindId(Long kindId) {
        this.kindId = kindId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDiscussion() {
        return discussion;
    }

    public void setDiscussion(String discussion) {
        this.discussion = discussion;
    }

    public Long getReportTime() {
        return reportTime;
    }

    public void setReportTime(Long reportTime) {
        this.reportTime = reportTime;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getKindName() {
        return kindName;
    }

    public void setKindName(String kindName) {
        this.kindName = kindName;
    }

    @Override
    public String toString() {
        return "DiscussBean{" +
                "discuzId=" + discuzId +
                ", userId=" + userId +
                ", kindId=" + kindId +
                ", title='" + title + '\'' +
                ", discussion='" + discussion + '\'' +
                ", reportTime=" + reportTime +
                ", image='" + image + '\'' +
                ", kindName='" + kindName + '\'' +
                '}';
    }
}