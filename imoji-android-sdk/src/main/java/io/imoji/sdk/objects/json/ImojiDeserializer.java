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
import io.imoji.sdk.objects.RenderingOptions;
import io.imoji.sdk.objects.Imoji;

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

        JsonArray tagsArray = root.getAsJsonArray("tags");
        List<String> tags;

        if (tagsArray != null && tagsArray.size() > 0) {
            tags = new ArrayList<>(tagsArray.size());
            for (JsonElement tag : tagsArray) {
                tags.add(tag.getAsString());
            }
        } else {
            tags = Collections.emptyList();
        }

        Map<RenderingOptions, Imoji.Metadata> metadataMap = new HashMap<>();

        JsonObject images = root.get("images").getAsJsonObject();

        for (RenderingOptions.BorderStyle borderStyle : RenderingOptions.BorderStyle.values()) {
            for (RenderingOptions.ImageFormat imageFormat : RenderingOptions.ImageFormat.values()) {
                for (RenderingOptions.Size size : RenderingOptions.Size.values()) {
                    JsonObject subDocument;

                    switch (borderStyle) {
                        case Sticker:
                            subDocument = images.getAsJsonObject("bordered");
                            break;
                        case None:
                            if (imageFormat == RenderingOptions.ImageFormat.AnimatedGif || imageFormat == RenderingOptions.ImageFormat.AnimatedWebp) {
                                subDocument = images.getAsJsonObject("animated");
                            } else {
                                subDocument = images.getAsJsonObject("unbordered");
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
                            subDocument = subDocument.getAsJsonObject("png");
                            break;
                        case WebP:
                        case AnimatedWebp:
                            subDocument = subDocument.getAsJsonObject("webp");
                            break;
                        case AnimatedGif:
                            subDocument = subDocument.getAsJsonObject("gif");
                            break;
                    }

                    if (subDocument == null) {
                        continue;
                    }


                    switch (size) {
                        case Thumbnail:
                            subDocument = subDocument.getAsJsonObject("150");
                            break;
                        case FullResolution:
                            subDocument = subDocument.getAsJsonObject("1200");
                            break;
                        case Resolution320:
                            subDocument = subDocument.getAsJsonObject("320");
                            break;
                        case Resolution512:
                            subDocument = subDocument.getAsJsonObject("512");
                            break;
                    }

                    if (subDocument != null) {
                        Uri url = Uri.parse(subDocument.get("url").getAsString());
                        Integer width = null, height = null, fileSize = null;

                        if (subDocument.has("width")) {
                            JsonElement widthObj = subDocument.get("width");
                            if (widthObj.isJsonPrimitive()) {
                                width = widthObj.getAsInt();
                            }
                        }

                        if (subDocument.has("height")) {
                            JsonElement heightObj = subDocument.get("height");
                            if (heightObj.isJsonPrimitive()) {
                                height = heightObj.getAsInt();
                            }
                        }

                        if (subDocument.has("fileSize")) {
                            JsonElement fileSizeObj = subDocument.get("fileSize");
                            if (fileSizeObj.isJsonPrimitive()) {
                                fileSize = fileSizeObj.getAsInt();
                            }
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
