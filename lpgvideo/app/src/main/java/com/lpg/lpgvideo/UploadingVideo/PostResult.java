package com.lpg.lpgvideo.UploadingVideo;

import com.google.gson.annotations.SerializedName;

public class PostResult {
    @SerializedName("url")
    private String videoUrl;
    @SerializedName("success")
    private boolean isSuccessful;

    public String getVideoUrl() {
        return videoUrl;
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }
}
