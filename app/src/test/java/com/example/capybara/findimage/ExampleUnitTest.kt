package com.example.capybara.findimage

import com.example.capybara.findimage.network.SearchingImageService
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ExampleUnitTest {
    val SEARCHING_IMAGE_ADDRESS = "https://dapi.kakao.com/"
    val AUTHORIZATION = "KakaoAK 74d08370ac9ea1260a9a56d1e4ea25ad"
    val searchText = "설현"

    lateinit var service: SearchingImageService

    @Before
    fun before() {
        val retrofit = Retrofit.Builder()
            .baseUrl(SEARCHING_IMAGE_ADDRESS)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        service = retrofit.create<SearchingImageService>(SearchingImageService::class.java)

    }

    @Test
    fun testAPI() {
        val response = service.searchingImageResultRepo(AUTHORIZATION, searchText).execute()
        val body = response.body()
        val title = body?.documents?.get(0)?.thumbnail_url ?: ""

        assertEquals("is same title", "https://search4.kakaocdn.net/argon/130x130_85_c/EQa9bd9jV9t", title)

    }

    @Test
    fun testAPIWithPage() {
        val response = service.searchingImageResultRepo(AUTHORIZATION, searchText, 1).execute()
        val body = response.body()
        val isEnd = body?.meta?.is_end

        assertEquals("is last page", false, isEnd)

    }
}
