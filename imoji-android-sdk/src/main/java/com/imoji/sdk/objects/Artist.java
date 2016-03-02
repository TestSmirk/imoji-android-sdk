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

package com.imoji.sdk.objects;

/**
 * Represents a creator of an Imoji or Category
 */
public class Artist {

    /**
     * Unique id for the artist
     */
    private final String identifier;

    /**
     * The name of the artist
     */
    private final String name;

    /**
     * Description of the artist
     */
    private final String description;

    /**
     * An Imoji image representing the profile picture for the artist
     */
    private final Imoji profileImoji;

    public Artist(String identifier, String name, String description, Imoji profileImoji) {
        this.identifier = identifier;
        this.name = name;
        this.description = description;
        this.profileImoji = profileImoji;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Imoji getProfileImoji() {
        return profileImoji;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Artist artist = (Artist) o;

        if (identifier != null ? !identifier.equals(artist.identifier) : artist.identifier != null)
            return false;
        if (name != null ? !name.equals(artist.name) : artist.name != null) return false;
        if (description != null ? !description.equals(artist.description) : artist.description != null)
            return false;
        return !(profileImoji != null ? !profileImoji.equals(artist.profileImoji) : artist.profileImoji != null);

    }

    @Override
    public int hashCode() {
        int result = identifier != null ? identifier.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (profileImoji != null ? profileImoji.hashCode() : 0);
        return result;
    }
}
