package com.github.khangnt.youtubecrawler.model.youtube.data;

import org.jetbrains.annotations.Nullable;

public class ShelfData implements YouTubeData {
    private String title;
    private String thumbnailUrl;
    @Nullable
    private String subtitle;
    @Nullable
    private String titleAnnotation;
    @Nullable
    private String endpoint;

    public ShelfData(String title, String thumbnailUrl, @Nullable String subtitle,
                     @Nullable String titleAnnotation, @Nullable String endpoint) {
        this.thumbnailUrl = thumbnailUrl;
        this.title = title;
        this.subtitle = subtitle;
        this.titleAnnotation = titleAnnotation;
        this.endpoint = endpoint;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public String getTitle() {
        return title;
    }

    @Nullable
    public String getSubtitle() {
        return subtitle;
    }

    @Nullable
    public String getTitleAnnotation() {
        return titleAnnotation;
    }

    @Nullable
    public String getEndpoint() {
        return endpoint;
    }

    @Override
    public String toString() {
        return "ShelfData{" +
                "thumbnailUrl='" + thumbnailUrl + '\'' +
                ", title='" + title + '\'' +
                ", subtitle='" + subtitle + '\'' +
                ", titleAnnotation='" + titleAnnotation + '\'' +
                ", endpoint='" + endpoint + '\'' +
                '}';
    }

}
