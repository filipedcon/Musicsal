package com.example.musicsal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.musicsal.ui.theme.MusicsalTheme

data class Song(val title: String, val artist: String)

@Composable
fun MusicListScreen(modifier: Modifier = Modifier) {
    val songs = listOf(
        Song("Dusk Till Dawn", "ZAYN ft. Sia"),
        Song("Song 2", "Artist 2"),
        Song("Song 3", "Artist 3"),
        Song("Song 4", "Artist 4"),
        Song("Song 5", "Artist 5")
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFE0F3FF),
                        Color(0xFFB4E0FF)
                    )
                )
            )
    ) {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Text(
                    text = "All Songs",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF003366),
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
            items(songs) { song ->
                SongListItem(song = song)
            }
        }
    }
}

@Composable
fun SongListItem(song: Song) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = song.title, fontWeight = FontWeight.Bold, color = Color(0xFF003366))
                Text(text = song.artist, fontSize = 12.sp, color = Color.Gray)
            }
            IconButton(onClick = { /* TODO: Play song */ }) {
                Icon(Icons.Default.PlayArrow, contentDescription = "Play Song", tint = Color(0xFF003366))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MusicListScreenPreview() {
    MusicsalTheme {
        MusicListScreen()
    }
}
