package com.gy.crawler.model;

/**
 * Created by Administrator on 2016/6/27.
 */
public class ErrorCheck {
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getErrorWords() {
        return errorWords;
    }

    public void setErrorWords(String errorWords) {
        this.errorWords = errorWords;
    }

    private String url;
    private String errorWords;
}
