package com.example.mediaplayer

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.mediaplayer.data.JamendoApi
import com.example.mediaplayer.data.MusicTrack
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    val goldenColor = Color(0xFFFFD700)
    val purpleColor = Color(0xFF6200EE)
    var isDarkTheme by rememberSaveable { mutableStateOf(false) }
    val activeColor = if (isDarkTheme) goldenColor else purpleColor

    var currentIndex by rememberSaveable { mutableStateOf(-1) }
    var showMenu by remember { mutableStateOf(false) }
    var isPlayingUI by remember { mutableStateOf(false) }

    val tracksSaver = listSaver<MutableState<List<MusicTrack>?>, MusicTrack>(
        save = { state -> state.value ?: emptyList() },
        restore = { mutableStateOf(it) }
    )
    var tracks by rememberSaveable { mutableStateOf<List<MusicTrack>?>(null) }

    val audioPlayer = remember { AudioPlayer() }
    val api = remember { JamendoApi() }

    LaunchedEffect(Unit) {
        if (tracks == null) {
            tracks = api.getTracks()
        }
    }

    val playTrack = { index: Int ->
        if (tracks != null && index in tracks!!.indices) {
            currentIndex = index
            audioPlayer.play(tracks!![index].previewUrl)
        }
    }

    LaunchedEffect(currentIndex) {
        while (true) {
            isPlayingUI = audioPlayer.isPlaying()
            if (audioPlayer.getCurrentPosition() > 29000) {
                if (tracks != null && currentIndex < tracks!!.size - 1) {
                    playTrack(currentIndex + 1)
                }
            }
            delay(1000)
        }
    }

    MaterialTheme(
        colorScheme = if (isDarkTheme) darkColorScheme(primary = goldenColor)
        else lightColorScheme(primary = purpleColor)
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Dhun Music", color = activeColor) },
                    actions = {
                        Switch(checked = isDarkTheme, onCheckedChange = { isDarkTheme = it })
                        Box {
                            IconButton(onClick = { showMenu = true }) {
                                Icon(Icons.Default.Sort, null, tint = activeColor)
                            }
                            DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
                                DropdownMenuItem(
                                    text = { Text("Sort A-Z") },
                                    onClick = {
                                        tracks = tracks?.sortedBy { it.trackName }
                                        showMenu = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Sort Duration") },
                                    onClick = {
                                        tracks = tracks?.sortedBy { it.durationMillis }
                                        showMenu = false
                                    }
                                )
                            }
                        }
                    }
                )
            },
            bottomBar = {
                if (currentIndex != -1 && tracks != null && currentIndex < tracks!!.size) {
                    val track = tracks!![currentIndex]
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(8.dp),
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Row(
                            Modifier.padding(16.dp).fillMaxWidth(),
                            Arrangement.SpaceBetween,
                            Alignment.CenterVertically
                        ) {
                            Column(Modifier.weight(1f)) {
                                Text(track.trackName, color = activeColor, maxLines = 1)
                                Text(track.artistName, style = MaterialTheme.typography.bodySmall, maxLines = 1)
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(onClick = { if (currentIndex > 0) playTrack(currentIndex - 1) }) {
                                    Icon(Icons.Default.SkipPrevious, null, tint = activeColor)
                                }
                                FloatingActionButton(
                                    onClick = {
                                        if (audioPlayer.isPlaying()) audioPlayer.pause()
                                        else audioPlayer.play(track.previewUrl)
                                    },
                                    containerColor = activeColor,
                                    shape = CircleShape,
                                    modifier = Modifier.size(48.dp)
                                ) {
                                    Icon(if (isPlayingUI) Icons.Default.Pause else Icons.Default.PlayArrow, null,
                                        tint = if(isDarkTheme) Color.Black else Color.White)
                                }
                                IconButton(onClick = { if (currentIndex < tracks!!.size - 1) playTrack(currentIndex + 1) }) {
                                    Icon(Icons.Default.SkipNext, null, tint = activeColor)
                                }
                            }
                        }
                    }
                }
            }
        ) { padding ->
            if (tracks == null) {
                Box(Modifier.fillMaxSize(), Alignment.Center) {
                    CircularProgressIndicator(color = activeColor)
                }
            } else {
                LazyColumn(Modifier.padding(padding).fillMaxSize()) {
                    itemsIndexed(tracks!!) { index, track ->
                        ListItem(
                            headlineContent = { Text(track.trackName) },
                            supportingContent = { Text(track.artistName) },
                            leadingContent = {
                                SubcomposeAsyncImage(
                                    model = track.image,
                                    contentDescription = null,
                                    modifier = Modifier.size(50.dp).clip(CircleShape).background(Color.Gray),
                                    loading = {
                                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                            Icon(Icons.Default.Refresh, null, modifier = Modifier.size(24.dp))
                                        }
                                    },
                                    error = {
                                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                            Icon(Icons.Default.MusicNote, null, modifier = Modifier.size(24.dp))
                                        }
                                    }
                                )
                            },
                            trailingContent = {
                                val totalSecs = track.durationMillis / 1000
                                Text("${totalSecs / 60}:${(totalSecs % 60).toString().padStart(2, '0')}")
                            },
                            modifier = Modifier.clickable { playTrack(index) }
                        )
                    }
                }
            }
        }
    }
}