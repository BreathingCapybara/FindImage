package com.example.capybara.findimage

import com.example.capybara.findimage.network.SearchingImageServiceManager
import com.example.capybara.findimage.network.error
import com.example.capybara.findimage.network.result
import com.example.capybara.findimage.network.size
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ExampleUnitTest {
    val SEARCHING_IMAGE_ADDRESS = "https://dapi.kakao.com/"
    val AUTHORIZATION = "KakaoAK 74d08370ac9ea1260a9a56d1e4ea25ad"
    val searchText = "설현"

    lateinit var manager: SearchingImageServiceManager

    @Before
    fun before() {
        manager = SearchingImageServiceManager(SEARCHING_IMAGE_ADDRESS, AUTHORIZATION)
    }

    @Test
    fun testAPI() {
        val result = manager.searchImage(searchText).result()
        val title = result?.documents?.get(0)?.thumbnail_url ?: ""

        assertEquals("is same title", "https://search4.kakaocdn.net/argon/130x130_85_c/EQa9bd9jV9t", title)
    }

    @Test
    fun testAPINoResult() {
        val size = manager.searchImage("쌰탳ㅎ").result().size()
        assertEquals("is document count is zero", 0, size)
    }

    @Test
    fun testAPIError() {
        val errorBody = manager.searchImage("쌰탳ㅎ").error()
        assertEquals("is error is null", null, errorBody)
    }

    @Test
    fun testAPIWithPage() {
        val body = manager.searchImage("쌰탳ㅎ").result()
        val isEnd = body?.meta?.is_end
        assertEquals("is last page", true, isEnd)
    }
}

