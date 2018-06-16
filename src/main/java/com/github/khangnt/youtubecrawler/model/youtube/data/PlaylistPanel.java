package com.github.khangnt.youtubecrawler.model.youtube.data;

public class PlaylistPanel implements YouTubeData {
    private String playlistId;
    private String title;
    private String subtitle;
    private boolean isInfinite;
    private int totalVideos;
    private int currentIndex;

    public PlaylistPanel(String playlistId, String title, String subtitle,
                         boolean isInfinite, int totalVideos, int currentIndex) {
        this.playlistId = playlistId;
        this.title = title;
        this.subtitle = subtitle;
        this.isInfinite = isInfinite;
        this.totalVideos = totalVideos;
        this.currentIndex = currentIndex;
    }

    public String getPlaylistId() {
        return playlistId;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public boolean isInfinite() {
        return isInfinite;
    }

    public int getTotalVideos() {
        return totalVideos;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    @Override
    public String toString() {
        return "PlaylistPanel{" +
                "playlistId='" + playlistId + '\'' +
                ", title='" + title + '\'' +
                ", subtitle='" + subtitle + '\'' +
                ", isInfinite=" + isInfinite +
                ", totalVideos=" + totalVideos +
                ", currentIndex=" + currentIndex +
                '}';
    }

}
