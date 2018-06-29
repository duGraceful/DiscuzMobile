package com.discuzmobile.my.discuzmobile.bean;

public class ListBean {
    private Long id;
    private String name;
    private String url;
    private Long time;
    private String discussion;

    public ListBean(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public ListBean(Long id, String name, String url) {
        this.id = id;
        this.name = name;
        this.url = url;
    }

    public ListBean(Long id, String name, String url, Long time, String discussion) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.time = time;
        this.discussion = discussion;
    }

    public ListBean(Long id, String name, String url, Long time) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.time = time;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getDiscussion() {
        return discussion;
    }

    public void setDiscussion(String discussion) {
        this.discussion = discussion;
    }
}
