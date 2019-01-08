package com.example.capybara.findimage

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import com.example.capybara.findimage.MainActivity.SearchHandler.Companion.HANDLER_TIMER
import com.example.capybara.findimage.network.SearchingImageServiceManager
import com.example.capybara.findimage.network.repo.ImageResultRepo
import com.facebook.drawee.backends.pipeline.Fresco
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Response
import java.lang.ref.WeakReference
import android.content.Context.INPUT_METHOD_SERVICE
import android.view.inputmethod.InputMethodManager
import android.widget.EditText


class MainActivity : AppCompatActivity() {

    private lateinit var mHandler: SearchHandler
    private lateinit var manager: SearchingImageServiceManager
    private var imageResultAdapter: ImageResultAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fresco.initialize(this)
        setContentView(R.layout.activity_main)

        initializeHandler()
        initializeManager()

        search_text.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //todo recyclerview clear
                mHandler.removeMessages(HANDLER_TIMER)
                val text = search_text.text.toString()

                when (text) {
                    "" -> return
                    else -> {
                        val msg = Message()
                        msg.what = HANDLER_TIMER
                        msg.obj = text
                        mHandler.sendMessageDelayed(msg, 1000)
                    }
                }
            }

        })

        searchImage("Lezhin")//todo test

    }

    fun searchImage(searchText: String) {
        imageResultAdapter = null //데이터 초기화
        searchImage(searchText, 1)
    }

    fun searchImage(searchText: String, page: Int) {
        hideKeyboard()

        manager.searchImage(searchText, page, object : retrofit2.Callback<ImageResultRepo> {
            override fun onResponse(call: Call<ImageResultRepo>, response: Response<ImageResultRepo>) {
                when {
                    response.errorBody() != null -> toast(this@MainActivity, getString(R.string.wrong_network))
                    response.body()?.documents == null -> toast(this@MainActivity, getString(R.string.cant_search))
                    response.body()?.documents?.size == 0 -> toast(this@MainActivity, getString(R.string.no_result))
                    else -> {
                        response.body()?.let { imageResultRepo ->
                            when (imageResultAdapter == null) {
                                true -> {
                                    imageResultAdapter = ImageResultAdapter(imageResultRepo,
                                        View.OnClickListener {
                                            if (!imageResultRepo.meta.is_end) {
                                                searchImage(searchText, imageResultAdapter!!.page + 1)
                                            }
                                        })
                                    recyclerView.adapter = imageResultAdapter
                                }
                                else -> {
                                    imageResultAdapter?.addImageResult(imageResultRepo)
                                    imageResultAdapter?.notifyDataSetChanged()
                                }
                            }

                            toast(this@MainActivity, imageResultRepo.documents?.size.toString())

                        }

                    }
                }
            }

            override fun onFailure(call: Call<ImageResultRepo>, t: Throwable) {
                toast(this@MainActivity, getString(R.string.not_found))
            }

        })
    }

    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(search_text.windowToken, 0)
    }

    private fun initializeManager() {
        val appInfo = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
        val aBundle = appInfo.metaData
        val address = aBundle.getString("server_address")
        val authorization = aBundle.getString("kakao_key")
        manager = SearchingImageServiceManager(address, authorization)
    }

    private fun initializeHandler() {
        mHandler = SearchHandler(this)
    }

    private class SearchHandler internal constructor(target: MainActivity) : Handler() {

        companion object {
            const val HANDLER_TIMER = 1
        }

        private var mTarget: WeakReference<MainActivity>? = null

        init {
            mTarget = WeakReference(target)
        }

        override fun handleMessage(msg: Message) {
            mTarget?.get()?.let { activity ->
                when (msg.what) {
                    HANDLER_TIMER -> {
                        val searchText: String = msg.obj as String
                        toast(activity, searchText)
                        activity.searchImage(searchText)

                    }
                    else -> {
                    }
                }
            }
        }

    }

    companion object {
        fun toast(activity: MainActivity, text: String) {
            Toast.makeText(activity, text, Toast.LENGTH_SHORT).show()
        }
    }

}
