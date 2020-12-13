package com.lpg.lpgvideo.Upload.Interface;


import com.lpg.lpgvideo.Upload.PostResult;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface videoService {

    @Multipart
    @POST("invoke/video")
    Call<PostResult> postVideo(@Query("student_id") String studentId, @Query("user_name")String userName, @Query("extra_value") String extraValue,
                               @Part MultipartBody.Part coverImage, @Part MultipartBody.Part video);
}