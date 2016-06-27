package com.gy.crawler.model;

/**
 * Created by hadoop on 2015/7/21.
 */
public class External {
    private String url;
    private String rootUrl;
    private String fromUrl;
    private String title;
    private int statusCode;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRootUrl() {
        return rootUrl;
    }

    public void setRootUrl(String rootUrl) {
        this.rootUrl = rootUrl;
    }

    public String getFromUrl() {
        return fromUrl;
    }

    public void setFromUrl(String fromUrl) {
        this.fromUrl = fromUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
