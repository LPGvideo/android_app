package com.lpg.lpgvideo.FetchInfoStream;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class FetchPojo {
    @SerializedName("feeds")
    private ArrayList<Item> videoLists;

    @SerializedName("success")
    private boolean isSuccess;

    public ArrayList<Item> getVideoLists() {
        return videoLists;
    }
}
