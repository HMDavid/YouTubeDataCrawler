package com.github.khangnt.youtubecrawler.model.youtube.data;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public class YouTubeDataResponse {
    public static final String RESULT_OK = "ok";

    private String result;
    private List<YouTubeData> items;
    @Nullable
    private Continuation2 continuation;
    @Nullable
    private String nextUrl;

    public YouTubeDataResponse(String result, List<YouTubeData> items,
                               @Nullable Continuation2 continuation, @Nullable String nextUrl) {
        this.result = result;
        this.items = items;
        this.continuation = continuation;
        this.nextUrl = nextUrl;
    }

    public String getResult() {
        return result;
    }

    public boolean isOk() {
        return "ok".equalsIgnoreCase(result);
    }

    public List<YouTubeData> getItems() {
        return items;
    }

    @Nullable
    public Continuation2 getContinuation() {
        return continuation;
    }

    @Nullable
    public String getNextUrl() {
        return nextUrl;
    }

    public boolean hasNext() {
        return continuation != null || nextUrl != null;
    }

    @Override
    public String toString() {
        return "YouTubeDataResponse{" +
                "items=" + items +
                ", continuation=" + continuation +
                ", result='" + result + '\'' +
                '}';
    }
}
