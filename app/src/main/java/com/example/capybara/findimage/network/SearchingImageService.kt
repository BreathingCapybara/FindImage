package com.example.capybara.findimage.network

import com.example.capybara.findimage.network.repo.ImageResultRepo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query


interface SearchingImageService {
    @GET("v2/search/image")
    fun searchingImageResultRepo(@Header("Authorization") authorization: String, @Query("query") searchText: String): Call<ImageResultRepo>

    @GET("v2/search/image")
    fun searchingImageResultRepo(
        @Header("Authorization") authorization: String, @Query("query") searchText: String, @Query(
            "page"
        ) page: Int
    ): Call<ImageResultRepo>
}

