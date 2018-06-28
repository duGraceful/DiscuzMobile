package com.discuzmobile.my.discuzmobile.bean;

import java.io.Serializable;

/**
 * Created by DELL on 2018/5/7.
 */

public class CommentsBean implements Serializable {
    private String userImgUrl; //用户头像路径
    private String content;//用户评论消息
    private String username; // 用户姓名
    private String replyUser; // 作者回复消息

    public CommentsBean(String userImgUrl, String content, String username, String replyUser) {
        this.userImgUrl = userImgUrl;
        this.content = content;
        this.username = username;
        this.replyUser = replyUser;
    }

    public String getUserImgUrl() {
        return userImgUrl;
    }

    public void setUserImgUrl(String userImgUrl) {
        this.userImgUrl = userImgUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getReplyUser() {
        return replyUser;
    }

    public void setReplyUser(String replyUser) {
        this.replyUser = replyUser;
    }
}

