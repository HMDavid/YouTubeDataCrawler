package com.github.khangnt.youtubecrawler.model.youtube.data;

import org.jetbrains.annotations.Nullable;

public class PlaylistData implements YouTubeData {
    private String playlistId;
    private String title;
    @Nullable
    private String owner;
    @Nullable
    private String videoCountText;
    @Nullable
    private String thumbnailUrl;
    private String endpoint;

    public PlaylistData(String playlistId, String title, @Nullable String owner,
                        @Nullable String videoCountText, @Nullable String thumbnailUrl,
                        String endpoint) {
        this.playlistId = playlistId;
        this.title = title;
        this.owner = owner;
        this.videoCountText = videoCountText;
        this.thumbnailUrl = thumbnailUrl;
        this.endpoint = endpoint;
    }

    public String getTitle() {
        return title;
    }

    /**
     * @return Name of the owner, or 'YouTube' if this is endless playlist.
     */
    public String getOwner() {
        return owner != null ? owner : "YouTube";
    }

    /**
     * @return video count formatted as String, or maybe NULL if isRadio() == true.
     */
    @Nullable
    public String getVideoCountText() {
        return videoCountText;
    }

    @Nullable
    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public String getPlaylistId() {
        return playlistId;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public boolean isRadio() {
        return playlistId.startsWith("RD");
    }

    @Override
    public String toString() {
        return "PlaylistData{" +
                "title='" + title + '\'' +
                ", owner='" + owner + '\'' +
                ", videoCountText='" + videoCountText + '\'' +
                ", thumbnailUrl='" + thumbnailUrl + '\'' +
                ", playlistId='" + playlistId + '\'' +
                ", endpoint='" + endpoint + '\'' +
                ", isRadio=" + isRadio() +
                '}';
    }
}
