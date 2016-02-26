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

package com.imoji.sdk.objects.json;

import android.net.Uri;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.imoji.sdk.RenderingOptions;
import com.imoji.sdk.objects.Imoji;

import java.lang.reflect.Type;
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
public class ImojiDeserializer implements JsonDeserializer<Imoji> {

    @Override
    public Imoji deserialize(JsonElement json,
                             Type typeOfT,
                             JsonDeserializationContext context) throws JsonParseException {
        JsonObject root = json.getAsJsonObject();

        String identifier;
        if (root.has("imojiId")) {
            identifier = root.get("imojiId").getAsString();
        } else {
            identifier = root.get("id").getAsString();
        }

        JsonArray tagsArray = root.get("tags").getAsJsonArray();
        List<String> tags = tagsArray.size() > 0 ? new ArrayList<String>(tagsArray.size()) :
                Collections.<String>emptyList();
        for (JsonElement tag : tagsArray) {
            tags.add(tag.getAsString());
        }

        Map<RenderingOptions, Imoji.Metadata> metadataMap = new HashMap<>();

        JsonObject images = root.get("images").getAsJsonObject();

        for (RenderingOptions.BorderStyle borderStyle : RenderingOptions.BorderStyle.values()) {
            for (RenderingOptions.ImageFormat imageFormat : RenderingOptions.ImageFormat.values()) {
                for (RenderingOptions.Size size : RenderingOptions.Size.values()) {
                    JsonObject subDocument;

                    switch (borderStyle) {
                        case Sticker:
                            subDocument = images.has("bordered") ? images.getAsJsonObject("bordered") : null;
                            break;
                        case None:
                            if (imageFormat == RenderingOptions.ImageFormat.AnimatedGif || imageFormat == RenderingOptions.ImageFormat.AnimatedWebp) {
                                subDocument = images.has("animated") ? images.getAsJsonObject("animated") : null;
                            } else {
                                subDocument = images.has("unbordered") ? images.getAsJsonObject("unbordered") : null;
                            }

                            break;
                        default:
                            subDocument = null;
                            break;
                    }

                    if (subDocument == null) {
                        continue;
                    }

                    switch (imageFormat) {
                        case Png:
                            subDocument = images.has("png") ? images.getAsJsonObject("png") : null;
                            break;
                        case WebP:
                        case AnimatedWebp:
                            subDocument = images.has("png") ? images.getAsJsonObject("webp") : null;
                            break;
                        case AnimatedGif:
                            subDocument = images.has("gif") ? images.getAsJsonObject("gif") : null;
                            break;
                    }

                    if (subDocument == null) {
                        continue;
                    }


                    switch (size) {
                        case Thumbnail:
                            subDocument = images.has("150") ? images.getAsJsonObject("150") : null;
                            break;
                        case FullResolution:
                            subDocument = images.has("1200") ? images.getAsJsonObject("1200") : null;
                            break;
                        case Resolution320:
                            subDocument = images.has("320") ? images.getAsJsonObject("320") : null;
                            break;
                        case Resolution512:
                            subDocument = images.has("512") ? images.getAsJsonObject("512") : null;
                            break;
                    }

                    if (subDocument != null) {
                        Uri url = Uri.parse(subDocument.get("url").getAsString());
                        Integer width = null, height = null, fileSize = null;

                        if (subDocument.has("width")) {
                            width = subDocument.get("width").getAsInt();
                        }

                        if (subDocument.has("height")) {
                            height = subDocument.get("height").getAsInt();
                        }

                        if (subDocument.has("fileSize")) {
                            fileSize = subDocument.get("fileSize").getAsInt();
                        }

                        metadataMap.put(
                                new RenderingOptions(borderStyle, imageFormat, size),
                                new Imoji.Metadata(
                                        url,
                                        width,
                                        height,
                                        fileSize
                                )
                        );
                    }
                }
            }
        }

        return new Imoji(identifier, tags, metadataMap);
    }
}
