package com.example.tweetonmap

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_tweet_display.*

class TweetDisplay : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tweet_display)


        val receivedTweets = intent.getStringExtra("Tweet Details")
        tv_tweet.text = receivedTweets
    }
}