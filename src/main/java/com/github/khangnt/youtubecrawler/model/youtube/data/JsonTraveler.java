package com.github.khangnt.youtubecrawler.model.youtube.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.Map;

class JsonTraveler {
    interface Callback {
        /**
         * Visit a JsonObject node.
         *
         * @return true if this node is handled, otherwise we will travel deeper into sub-node.
         */
        boolean visit(JsonObject jsonObject);
    }

    static void travel(JsonElement jsonElement, Callback callback) {
        if (jsonElement instanceof JsonPrimitive
                || jsonElement instanceof JsonNull) {
            return;
        }
        if (jsonElement instanceof JsonObject) {
            JsonObject jsonObject = ((JsonObject) jsonElement);
            if (!callback.visit(jsonObject)) {
                for (Map.Entry<String, JsonElement> stringJsonElementEntry : jsonObject.entrySet()) {
                    travel(stringJsonElementEntry.getValue(), callback);
                }
            }
        } else {
            JsonArray jsonArray = ((JsonArray) jsonElement);
            for (JsonElement child : jsonArray) {
                travel(child, callback);
            }
        }
    }
}