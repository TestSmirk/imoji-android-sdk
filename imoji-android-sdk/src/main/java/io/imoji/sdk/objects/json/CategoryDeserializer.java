/*
 * Imoji Android SDK
 * Created by nkhoshini
 *
 * Copyright (C) 2016 Imoji
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to
 * deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KID, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 * IN THE SOFTWARE.
 *
 */

package io.imoji.sdk.objects.json;

import android.net.Uri;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import io.imoji.sdk.objects.Artist;
import io.imoji.sdk.objects.Category;
import io.imoji.sdk.objects.Imoji;

import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Imoji Android SDK
 * <p/>
 * Created by nkhoshini on 2/25/16.
 */
public class CategoryDeserializer implements JsonDeserializer<Category> {

    private static final Map<String, Category.URLCategory> URL_CATEGORY_MAP;

    static {
        Map<String, Category.URLCategory> urlCategoryMap = new HashMap<>();
        urlCategoryMap.put("website", Category.URLCategory.Website);
        urlCategoryMap.put("app store", Category.URLCategory.AppStore);
        urlCategoryMap.put("twitter", Category.URLCategory.Twitter);
        urlCategoryMap.put("instagram", Category.URLCategory.Instagram);
        urlCategoryMap.put("video", Category.URLCategory.Video);

        URL_CATEGORY_MAP = Collections.unmodifiableMap(urlCategoryMap);
    }

    @Override
    public Category deserialize(JsonElement json,
                                Type typeOfT,
                                JsonDeserializationContext context) throws JsonParseException {
        JsonObject root = json.getAsJsonObject();

        String identifier = root.get("searchText").getAsString();
        String title = root.get("title").getAsString();

        JsonArray imojisArray = root.get("imojis").getAsJsonArray();


        Category.Attribution attribution = null;

        if (root.has("artist") && !root.get("artist").isJsonNull()) {
            JsonObject artistJson = root.getAsJsonObject("artist");
            Artist artist = context.deserialize(artistJson, Artist.class);
            String attributionId = artistJson.get("packId").getAsString();
            Uri uri = Uri.parse(artistJson.get("packURL").getAsString());
            Category.URLCategory urlCategory = artistJson.has("packURLCategory") ?
                    URL_CATEGORY_MAP.get(artistJson.get("packURLCategory").getAsString()) : Category.URLCategory.Website;
            JsonArray relatedTagsArray = root.getAsJsonArray("relatedTags");
            List<String> relatedTags;

            if (relatedTagsArray != null && relatedTagsArray.size() > 0) {
                relatedTags = new ArrayList<>(relatedTagsArray.size());
                for (JsonElement tag : relatedTagsArray) {
                    relatedTags.add(tag.getAsString());
                }
            } else {
                relatedTags = Collections.emptyList();
            }

            if (urlCategory == null) {
                urlCategory = Category.URLCategory.Website;
            }


            attribution = new Category.Attribution(attributionId, artist, uri, relatedTags, urlCategory);
        }

        List<Imoji> previewImojis;
        if (imojisArray != null && imojisArray.size() > 0) {
            previewImojis = new ArrayList<>(imojisArray.size());

            for (JsonElement imojiJson : imojisArray) {
                previewImojis.add(context.<Imoji>deserialize(imojiJson, Imoji.class));
            }
        } else {
            previewImojis = Collections.emptyList();
        }

        return new Category(identifier, title, previewImojis, attribution);
    }
}
