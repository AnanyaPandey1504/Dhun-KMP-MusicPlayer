package com.example.mediaplayer

import android.media.MediaPlayer

actual class AudioPlayer {
    companion object {
        private var mediaPlayer: MediaPlayer? = null
    }

    actual fun play(url: String) {
        stop()
        mediaPlayer = MediaPlayer().apply {
            setDataSource(url)
            prepareAsync()
            setOnPreparedListener { start() }
        }
    }

    actual fun pause() {
        if (mediaPlayer?.isPlaying == true) mediaPlayer?.pause()
        else mediaPlayer?.start()
    }

    actual fun stop() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    actual fun isPlaying(): Boolean = mediaPlayer?.isPlaying ?: false
    actual fun seekTo(position: Int) { mediaPlayer?.seekTo(position) }
    actual fun getCurrentPosition(): Int = mediaPlayer?.currentPosition ?: 0
}