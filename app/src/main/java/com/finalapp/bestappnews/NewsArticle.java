
package com.finalapp.bestappnews;

import android.graphics.Bitmap;

public class NewsArticle {

    private String webSectionName;
    private String webPublicationDate;
    private String webTitle;
    private String webTrailText;
    private String webUrl;
    private String byLine;
    private Bitmap thumbnail;


    public NewsArticle(String webSectionName, String webPublicationDate, String webTitle,
                       String webTrailText, String webUrl, String byLine, Bitmap thumbnail) {
        this.webSectionName = webSectionName;
        this.webPublicationDate = webPublicationDate;
        this.webTitle = webTitle;
        this.webTrailText = webTrailText;
        this.webUrl = webUrl;
        this.byLine = byLine;
        this.thumbnail = thumbnail;
    }

    public String getSectionName() {
        return webSectionName;
    }

    public String getPublishedDate() {
        return webPublicationDate;
    }

    public String getTitle() {
        return webTitle;
    }

    public String getTrailText() {
        return webTrailText;
    }

    public String getUrl() {
        return webUrl;
    }

    public String getAuthor() {
        return byLine;
    }

    public Bitmap getThumbnail() {
        return thumbnail;
    }
}