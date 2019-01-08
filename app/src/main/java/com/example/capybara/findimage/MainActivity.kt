package com.example.capybara.findimage

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import com.example.capybara.findimage.MainActivity.SearchHandler.Companion.HANDLER_TIMER
import java.lang.ref.WeakReference

class MainActivity : AppCompatActivity() {

    private lateinit var mHandler: SearchHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mHandler = SearchHandler(this)

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
                        Toast.makeText(activity, searchText, Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                    }
                }
            }
        }
    }

}
