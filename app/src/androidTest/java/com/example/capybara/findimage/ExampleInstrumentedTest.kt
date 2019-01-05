package com.example.capybara.findimage

import android.content.pm.PackageManager
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.example.capybara.findimage.network.SearchingImageService
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun searchImage() {
        val appContext = InstrumentationRegistry.getTargetContext()
        assertEquals("com.example.capybara.findimage", appContext.packageName)

        val appInfo = appContext.packageManager.getApplicationInfo(appContext.packageName, PackageManager.GET_META_DATA)
        val aBundle = appInfo.metaData

        val SEARCHING_IMAGE_ADDRESS =aBundle.getString("server_address")
        val AUTHORIZATION = aBundle.getString("kakao_key")

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
