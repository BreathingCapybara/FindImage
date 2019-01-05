package com.example.capybara.findimage

import com.example.capybara.findimage.network.SearchingImageService
import org.junit.Test

import org.junit.Assert.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ExampleUnitTest {
    val SEARCHING_IMAGE_ADDRESS = "https://dapi.kakao.com/"
    val AUTHORIZATION = "KakaoAK 74d08370ac9ea1260a9a56d1e4ea25ad"

    @Test
    fun addition_isCorrect() {
        val retrofit = Retrofit.Builder()
            .baseUrl(SEARCHING_IMAGE_ADDRESS)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create<SearchingImageService>(SearchingImageService::class.java)
        val response = service.searchingImageResultRepo(AUTHORIZATION, "설현").execute()
        val body = response.body()
        val title = body?.documents?.get(0)?.thumbnail_url ?: ""

        assertEquals("https://search4.kakaocdn.net/argon/130x130_85_c/EQa9bd9jV9t", title)

    }
}
