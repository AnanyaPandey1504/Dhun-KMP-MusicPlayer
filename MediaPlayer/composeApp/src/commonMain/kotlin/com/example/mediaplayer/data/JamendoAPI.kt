package com.example.mediaplayer.data

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

class JamendoApi {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                coerceInputValues = true
                prettyPrint = true
            }, contentType = ContentType.Any)
        }
    }

    suspend fun getTracks(): List<MusicTrack> {
        return try {
            val url = "https://itunes.apple.com/search?term=bollywood&entity=song&limit=30"
            val response = client.get(url).body<iTunesResponse>()
            response.results.filter { it.previewUrl.isNotEmpty() }
        } catch (e: Exception) {
            println("Dhun Error: ${e.message}")
            emptyList()
        }
    }
}