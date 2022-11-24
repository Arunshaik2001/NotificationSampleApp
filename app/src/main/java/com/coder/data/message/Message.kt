package com.coder.data.message

import androidx.annotation.DrawableRes

data class Message(
    val id: Int,
    val sender: String,
    val text: String,
    @DrawableRes val image: Int
)
