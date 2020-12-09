package com.lpg.lpgvideo.FetchInfoStream;

import retrofit2.Call;
import retrofit2.http.GET;

public interface FetchService {
    @GET("invoke/video")
    Call<FetchPojo> fetchVideoList();
}
