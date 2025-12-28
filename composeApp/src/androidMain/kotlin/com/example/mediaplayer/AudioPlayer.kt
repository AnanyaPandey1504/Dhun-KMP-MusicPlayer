package com.example.mediaplayer

import android.media.MediaPlayer

actual class AudioPlayer {
    companion object {
        private val mediaPlayer = MediaPlayer()
    }

    actual fun play(url: String) {
        try {
            mediaPlayer.reset()
            mediaPlayer.setDataSource(url)
            mediaPlayer.prepareAsync()
            mediaPlayer.setOnPreparedListener { it.start() }

            mediaPlayer.setOnErrorListener { mp, what, extra ->
                mp.reset()
                true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    actual fun pause() {
        try {
            if (mediaPlayer.isPlaying) mediaPlayer.pause()
            else mediaPlayer.start()
        } catch (e: Exception) {}
    }

    actual fun stop() {
        try { mediaPlayer.reset() } catch (e: Exception) {}
    }

    actual fun isPlaying(): Boolean = try { mediaPlayer.isPlaying } catch (e: Exception) { false }
    actual fun seekTo(position: Int) { try { mediaPlayer.seekTo(position) } catch (e: Exception) {} }
    actual fun getCurrentPosition(): Int = try { mediaPlayer.currentPosition } catch (e: Exception) { 0 }
}