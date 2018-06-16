package com.github.khangnt.youtubecrawler.model.youtube.data;

import org.jetbrains.annotations.Nullable;

public class VideoData implements YouTubeData {
    private String videoId;
    private String title;
    @Nullable
    private String channelTitle;
    @Nullable
    private String lengthText;
    @Nullable
    private String viewCountText;
    private String endpoint;
    private boolean isLive;

    public VideoData(String videoId, String title, @Nullable String channelTitle,
                     @Nullable String lengthText, @Nullable String viewCountText, String endpoint,
                     boolean isLive) {
        this.videoId = videoId;
        this.title = title;
        this.channelTitle = channelTitle;
        this.lengthText = lengthText;
        this.viewCountText = viewCountText;
        this.endpoint = endpoint;
        this.isLive = isLive;
    }

    public String getTitle() {
        return title;
    }

    @Nullable
    public String getChannelTitle() {
        return channelTitle;
    }

    /**
     * @return the length of this video formatted as string, or NULL if isLive() == true.
     */
    @Nullable
    public String getLengthText() {
        return lengthText;
    }

    /**
     * @return view count of this video formatted as string, or NULL it's not available.
     */
    @Nullable
    public String getViewCountText() {
        return viewCountText;
    }

    public boolean isLive() {
        return isLive;
    }

    public String getVideoId() {
        return videoId;
    }

    public String getEndpoint() {
        return endpoint;
    }

    @Override
    public String toString() {
        return "VideoData{" +
                "title='" + title + '\'' +
                ", videoId='" + videoId + '\'' +
                ", channelTitle='" + channelTitle + '\'' +
                ", lengthText='" + lengthText + '\'' +
                ", viewCountText='" + viewCountText + '\'' +
                ", endpoint='" + endpoint + '\'' +
                ", isLive=" + isLive +
                '}';
    }
}
