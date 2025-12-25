package com.example.mediaplayer

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform