package com.example.capybara.findimage

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val url = intent.getStringExtra("a")

        Toast.makeText(this, url, Toast.LENGTH_SHORT).show()

    }
}
