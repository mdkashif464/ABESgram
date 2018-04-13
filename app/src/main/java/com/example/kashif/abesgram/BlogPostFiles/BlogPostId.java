package com.example.kashif.abesgram.BlogPostFiles;

import android.support.annotation.NonNull;

import com.google.firebase.firestore.Exclude;

/**
 * Created by kashif on 14/4/18.
 */

public class BlogPostId {

    @Exclude
    public String BlogPostId;

    public <T extends BlogPostId> T withId(@NonNull final String id) {
        this.BlogPostId = id;
        return (T) this;
    }
}
