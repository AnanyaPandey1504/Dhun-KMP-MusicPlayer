package com.example.mediaplayer

expect class AudioPlayer() {
    fun play(url: String)
    fun pause()
    fun stop()
    fun isPlaying(): Boolean
    fun seekTo(position: Int)
    fun getCurrentPosition(): Int
}