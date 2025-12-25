package com.example.mediaplayer.data

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
@Serializable
data class MusicTrack(
    val trackId: Long = 0,
    val trackName: String = "Unknown Song",
    val artistName: String = "Unknown Artist",
    val previewUrl: String = "",
    @SerialName("artworkUrl100") val image: String = "",
    @SerialName("trackTimeMillis") val durationMillis: Long = 0,
    val kind: String = ""
) : Parcelable

@Serializable
data class iTunesResponse(val results: List<MusicTrack> = emptyList())