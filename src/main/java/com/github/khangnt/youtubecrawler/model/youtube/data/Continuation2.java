package com.github.khangnt.youtubecrawler.model.youtube.data;

public class Continuation2 {
    private String continuationToken;
    private String clickTrackingParam;

    public Continuation2(String continuationToken, String clickTrackingParam) {
        this.continuationToken = continuationToken;
        this.clickTrackingParam = clickTrackingParam;
    }

    public String getContinuationToken() {
        return continuationToken;
    }

    public String getClickTrackingParam() {
        return clickTrackingParam;
    }

    @Override
    public String toString() {
        return "Continuation{" +
                "continuationToken='" + continuationToken + '\'' +
                ", clickTrackingParam='" + clickTrackingParam + '\'' +
                '}';
    }

}
