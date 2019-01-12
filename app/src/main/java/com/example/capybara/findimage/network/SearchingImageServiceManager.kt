package com.example.capybara.findimage.network

import com.example.capybara.findimage.network.repo.ImageResultRepo
import okhttp3.ResponseBody
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchingImageServiceManager(ADDRESS: String, private val AUTHORIZATION: String) {

    var service: SearchingImageService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(ADDRESS)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        service = retrofit.create<SearchingImageService>(SearchingImageService::class.java)
    }

    fun searchImage(searchText: String): Response<ImageResultRepo> {
        return service.searchingImageResultRepo(AUTHORIZATION, searchText).execute()
    }

    fun searchImage(searchText: String, page: Int, callback: Callback<ImageResultRepo>) {
        service.searchingImageResultRepo(AUTHORIZATION, searchText, page).enqueue(callback)
    }

}

fun <T> Response<T>.result(): T? {
    return this.body()
}

fun <T> Response<T>.error(): ResponseBody? {
    return this.errorBody()
}

fun ImageResultRepo?.size(): Any {
    return this?.documents?.size ?: 0
}
