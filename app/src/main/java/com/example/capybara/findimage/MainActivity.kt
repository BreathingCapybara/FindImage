package com.example.capybara.findimage

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        search_text.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //todo recyclerview clear
                timeHandler.removeMessages(HANDLER_TIMER)
                val text = search_text.text.toString()

                when (text) {
                    "" -> return
                    else -> {
                        val msg = Message()
                        msg.what = HANDLER_TIMER
                        msg.obj = text
                        timeHandler.sendMessageDelayed(msg, 1000)
                    }
                }
            }

        })
    }

    private val HANDLER_TIMER = 1

    val timeHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                HANDLER_TIMER -> {
                    val searchText: String = msg.obj as String
                    Toast.makeText(applicationContext, searchText, Toast.LENGTH_SHORT).show()
                }
                else -> {
                }
            }
        }
    }


}
