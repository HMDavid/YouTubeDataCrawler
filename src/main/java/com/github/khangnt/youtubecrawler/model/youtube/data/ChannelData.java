package com.github.khangnt.youtubecrawler.model.youtube.data;

import org.jetbrains.annotations.Nullable;

public class ChannelData implements YouTubeData {
    private String title;
    private String thumbnailUrl;
    @Nullable
    private String videoCountText;
    @Nullable
    private String subscriberCount;
    private String endpoint;

    public ChannelData(String title, String thumbnailUrl, @Nullable String videoCountText,
                       @Nullable String subscriberCount, String endpoint) {
        this.title = title;
        this.thumbnailUrl = thumbnailUrl;
        this.videoCountText = videoCountText;
        this.subscriberCount = subscriberCount;
        this.endpoint = endpoint;
    }

    public String getTitle() {
        return title;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    @Nullable
    public String getVideoCountText() {
        return videoCountText;
    }

    /**
     * @return subscriber count formatted as String or NULL if data not available.
     */
    @Nullable
    public String getSubscriberCount() {
        return subscriberCount;
    }

    public String getEndpoint() {
        return endpoint;
    }

    @Override
    public String toString() {
        return "ChannelData{" +
                "title='" + title + '\'' +
                ", thumbnailUrl='" + thumbnailUrl + '\'' +
                ", videoCountText='" + videoCountText + '\'' +
                ", subscriberCount='" + subscriberCount + '\'' +
                ", endpoint='" + endpoint + '\'' +
                '}';
    }
}
