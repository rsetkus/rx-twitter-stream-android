package co.uk.thejvm.thing.rxtwitter.data

import android.graphics.Bitmap

data class Tweet(val content: String, var avatarImage: Bitmap, val dateLabel: String = "")
