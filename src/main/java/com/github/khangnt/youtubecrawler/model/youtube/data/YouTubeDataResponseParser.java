package com.github.khangnt.youtubecrawler.model.youtube.data;

import com.github.khangnt.youtubecrawler.internal.Utils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.github.khangnt.youtubecrawler.internal.Utils.isEmpty;
import static com.github.khangnt.youtubecrawler.internal.Utils.parseYouTubePlaylistIdFromUrl;

public class YouTubeDataResponseParser {
    private static final Set<String> BLACK_LIST_ITEM_TYPE;
    private static final String KEY_ITEM_TYPE = "item_type";

    static {
        BLACK_LIST_ITEM_TYPE = new HashSet<>();
        BLACK_LIST_ITEM_TYPE.add("watch_card_video_list");
        BLACK_LIST_ITEM_TYPE.add("formatted_string");
        BLACK_LIST_ITEM_TYPE.add("promoted_video");
        BLACK_LIST_ITEM_TYPE.add("icon");
        BLACK_LIST_ITEM_TYPE.add("sign_in_promo");
        BLACK_LIST_ITEM_TYPE.add("comment_section");
        BLACK_LIST_ITEM_TYPE.add("video_main_content");
    }

    public static YouTubeDataResponse parse(@Nullable JsonObject json) {
        ArrayList<YouTubeData> items = new ArrayList<>();
        Continuation2[] continuation = {null};

        if (json == null ||
                !YouTubeDataResponse.RESULT_OK.equals(json.get("result").getAsString())) {
            return new YouTubeDataResponse("error", items, null, null);
        }

        JsonObject content = json.getAsJsonObject("content");
        String nextUrl = safeGet(content, "next_url", null);

        JsonTraveler.travel(content, jsonObject -> {
            if (jsonObject.has("related_videos")) {
                // quick parse /watch page
                items.addAll(parseRelatedVideos(jsonObject.get("related_videos")));

                if (jsonObject.has("playlist_panel")) {
                    JsonObject playlistPanel = jsonObject.getAsJsonObject("playlist_panel");
                    items.add(parsePlaylistPanel(playlistPanel));
                    JsonArray playlistVideos = playlistPanel.getAsJsonArray("contents");
                    for (JsonElement playlistVideo : playlistVideos) {
                        items.add(parsePlaylistPanelVideo(((JsonObject) playlistVideo)));
                    }
                }
                return true;
            }


            String itemType = safeGet(jsonObject, KEY_ITEM_TYPE, null);
            if (itemType == null) return false;

            if (BLACK_LIST_ITEM_TYPE.contains(itemType)) {
                return true;
            }
            if ("tab".equals(itemType)) {
                // do not scan if this tab wasn't selected
                return !safeGet(jsonObject, "selected", false);
            }

            if ("playlist_video".equals(itemType)) {
                items.add(parsePlaylistVideo(jsonObject));
                return true;
            } else if ("compact_video".equals(itemType)) {
                items.add(parseCompactVideo(jsonObject));
                return true;
            } else if ("video_with_context".equals(itemType)) {
                items.add(parseVideoWithContext(jsonObject));
                return true;
            } else if ("compact_playlist".equals(itemType)) {
                items.add(parseCompactPlaylist(jsonObject));
                return true;
            } else if ("compact_radio".equals(itemType)) {
                items.add(parseRadioPlaylist(jsonObject));
                return true;
            } else if ("compact_channel".equals(itemType)) {
                items.add(parseCompactChannel(jsonObject));
                return true;
            } else if ("shelf".equals(itemType)) {
                items.add(parseShelfData(jsonObject));
                return false; // continue parse children
            } else if ("artist_watch_card".equals(itemType)) {
                items.add(parseArtistWatchCard(jsonObject));
                return false; // continue parse watch_card_album_list
            } else if ("watch_card_album_list".equals(itemType)) {
                items.addAll(parseArtistAlbums(jsonObject));
                return true;
            } else if ("next_continuation_data".equals(itemType)) {
                continuation[0] = new Continuation2(jsonObject.get("continuation").getAsString(),
                        safeGet(jsonObject, "click_tracking_params", ""));
                return true;
            }

            return false;
        });

        return new YouTubeDataResponse(YouTubeDataResponse.RESULT_OK, items, continuation[0], nextUrl);
    }

    private static VideoData parsePlaylistVideo(JsonObject jsonObject) {
        String length = parseRunTextOrNull(jsonObject.get("length"));
        return new VideoData(
                jsonObject.get("video_id").getAsString(),
                parseRunText(jsonObject.get("title")),
                parseRunTextOrNull(jsonObject.get("short_byline")),
                length,
                null,
                jsonObject.getAsJsonObject("endpoint").get("url").getAsString(),
                isEmpty(length)
        );
    }

    private static VideoData parseCompactVideo(JsonObject jsonObj) {
        String length = parseRunTextOrNull(jsonObj.getAsJsonObject("length"));
        return new VideoData(
                jsonObj.get("encrypted_id").getAsString(),
                parseRunText(jsonObj.getAsJsonObject("title")),
                parseRunTextOrNull(jsonObj.getAsJsonObject("short_byline")),
                length,
                parseRunTextOrNull(jsonObj.getAsJsonObject("view_count")),
                jsonObj.getAsJsonObject("endpoint").get("url").getAsString(),
                isEmpty(length)
        );
    }

    private static VideoData parseVideoWithContext(JsonObject jsonObj) {
        String length = parseRunTextOrNull(jsonObj.get("length_text"));
        return new VideoData(
                jsonObj.get("video_id").getAsString(),
                parseRunText(jsonObj.get("headline")),
                parseRunTextOrNull(jsonObj.get("short_byline_text")),
                length,
                parseRunTextOrNull(jsonObj.get("short_view_count_text")),
                jsonObj.getAsJsonObject("navigation_endpoint").get("url").getAsString(),
                isEmpty(length)
        );
    }

    private static PlaylistData parseCompactPlaylist(JsonObject jsonObj) {
        return new PlaylistData(
                jsonObj.get("playlist_id").getAsString(),
                parseRunText(jsonObj.get("title")),
                parseRunTextOrNull(jsonObj.get("owner")),
                parseRunTextOrNull(jsonObj.get("video_count_short")),
                jsonObj.getAsJsonObject("thumbnail_info").get("url").getAsString(),
                jsonObj.getAsJsonObject("endpoint").get("url").getAsString()
        );
    }

    private static PlaylistData parseRadioPlaylist(JsonObject jsonObj) {
        return new PlaylistData(
                jsonObj.get("playlist_id").getAsString(),
                parseRunText(jsonObj.getAsJsonObject("title")),
                null,
                parseRunTextOrNull(jsonObj.getAsJsonObject("video_count_short_text")),
                jsonObj.getAsJsonObject("thumbnail_info").get("url").getAsString(),
                jsonObj.getAsJsonObject("navigation_endpoint").get("url").getAsString()
        );
    }

    private static PlaylistData parseArtistWatchCard(JsonObject jsonObj) {
        JsonObject ctaObj = jsonObj.getAsJsonObject("call_to_action");
        String ctaEndpoint = ctaObj.getAsJsonObject("navigation_endpoint")
                .get("url").getAsString();
        String playlistId = Utils.parseYouTubePlaylistIdFromUrl(ctaEndpoint);
        if (playlistId == null) throw new IllegalStateException("Invalid artist watch card");
        return new PlaylistData(
                playlistId,
                parseRunText(jsonObj.get("title")),
                parseRunTextOrNull(jsonObj.get("collapsed_label")),
                null,
                safeGet(ctaObj.getAsJsonObject("left_thumbnail"), "url", null),
                ctaEndpoint
        );
    }

    @NonNls
    private static List<PlaylistData> parseArtistAlbums(JsonObject jsonObject) {
        JsonArray albums = jsonObject.getAsJsonArray("albums");
        if (albums != null && albums.size() > 0) {
            List<PlaylistData> result = new ArrayList<>();
            for (JsonElement album : albums) {
                JsonObject albumObj = album.getAsJsonObject();
                String endpoint = albumObj.getAsJsonObject("navigation_endpoint").get("url").getAsString();
                String playlistId = parseYouTubePlaylistIdFromUrl(endpoint);
                if (playlistId == null) continue;
                result.add(new PlaylistData(
                        playlistId,
                        parseRunText(albumObj.get("title")),
                        safeGet(albumObj, "year", "Year unknown"),
                        null,
                        safeGet(albumObj.getAsJsonObject("thumbnail"), "url", null),
                        endpoint
                ));
            }
            return result;
        }
        return Collections.emptyList();
    }

    private static ChannelData parseCompactChannel(JsonObject jsonObj) {
        return new ChannelData(
                parseRunText(jsonObj.get("title")),
                jsonObj.getAsJsonObject("thumbnail_info").get("url").getAsString(),
                parseRunTextOrNull(jsonObj.get("video_count")),
                parseRunTextOrNull(jsonObj.get("subscriber_count")),
                jsonObj.getAsJsonObject("endpoint").get("url").getAsString()
        );
    }

    private static ShelfData parseShelfData(JsonObject jsonObj) {
        return new ShelfData(
                parseRunText(jsonObj.getAsJsonObject("title")),
                safeGet(jsonObj.getAsJsonObject("thumbnail"), "url", null),
                parseRunTextOrNull(jsonObj.getAsJsonObject("subtitle")),
                parseRunTextOrNull(jsonObj.getAsJsonObject("title_annotation")),
                safeGet(jsonObj.getAsJsonObject("endpoint"), "url", null)
        );
    }

    private static PlaylistPanel parsePlaylistPanel(JsonObject jsonObject) {
        return new PlaylistPanel(
                jsonObject.get("playlist_id").getAsString(),
                parseRunText(jsonObject.get("title_text")),
                parseRunTextOrNull(jsonObject.get("short_byline_text")),
                jsonObject.get("is_infinite").getAsBoolean(),
                jsonObject.get("total_videos").getAsInt(),
                jsonObject.get("current_index").getAsInt()
        );
    }

    private static VideoData parsePlaylistPanelVideo(JsonObject jsonObject) {
        String unplayableText = parseRunTextOrNull(jsonObject.get("unplayable_text"));
        String length = parseRunTextOrNull(jsonObject.get("length"));
        return new VideoData(
                jsonObject.get("video_id").getAsString(),
                parseRunText(jsonObject.get("title")),
                parseRunTextOrNull(jsonObject.get("short_byline")) + " " + unplayableText,
                length,
                parseRunTextOrNull(jsonObject.get("view_count")),
                jsonObject.getAsJsonObject("endpoint").get("url").getAsString(),
                isEmpty(length)
        );
    }

    private static List<VideoData> parseRelatedVideos(JsonElement jsonElement) {
        if (jsonElement instanceof JsonArray) {
            List<VideoData> result = new ArrayList<>();
            for (JsonElement element : ((JsonArray) jsonElement)) {
                JsonObject videoObj = element.getAsJsonObject();
                String duration = safeGet(videoObj, "duration", null);
                result.add(new VideoData(
                        videoObj.get("encrypted_id").getAsString(),
                        videoObj.get("title").getAsString(),
                        videoObj.get("public_name").getAsString(),
                        duration,
                        safeGet(videoObj, "view_count_text", null),
                        videoObj.get("watch_link").getAsString(),
                        isEmpty(duration)
                ));
            }
            return result;
        }
        return Collections.emptyList();
    }

    static String safeGet(JsonObject jsonObject, String key, @Nullable String defaultValue) {
        if (jsonObject == null) return defaultValue;
        JsonElement jsonElement = jsonObject.get(key);
        if (!(jsonElement instanceof JsonPrimitive)
                || !((JsonPrimitive) jsonElement).isString()) {
            return defaultValue;
        } else {
            return jsonElement.getAsString();
        }
    }

    static boolean safeGet(JsonObject jsonObject, String key, boolean defaultValue) {
        if (jsonObject == null) return defaultValue;
        JsonElement jsonElement = jsonObject.get(key);
        if (!(jsonElement instanceof JsonPrimitive)
                || !((JsonPrimitive) jsonElement).isBoolean()) {
            return defaultValue;
        } else {
            return jsonElement.getAsBoolean();
        }
    }

    private static String parseRunText(JsonElement jsonElement) {
        return ((JsonObject) jsonElement).getAsJsonArray("runs").get(0)
                .getAsJsonObject().get("text").getAsString();
    }

    private static String parseRunTextOrNull(JsonElement jsonElement) {
        try {
            return parseRunText(jsonElement);
        } catch (Throwable ignore) {
            return null;
        }
    }

}
