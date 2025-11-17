package com.example.musicsal

import android.Manifest
import android.content.ContentUris
import android.content.Context
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.musicsal.ui.theme.MusicsalTheme
import java.io.File
import android.media.MediaPlayer
import androidx.compose.foundation.clickable

private const val TAG = "MUSIC_DEBUG"

data class Song(val id: Long, val title: String, val artist: String, val uri: Uri)

fun loadSongsFromDevice(context: Context): List<Song> {
    val songs = mutableListOf<Song>()

    val projection = arrayOf(
        MediaStore.Audio.Media._ID,
        MediaStore.Audio.Media.TITLE,
        MediaStore.Audio.Media.ARTIST,
        MediaStore.Audio.Media.IS_MUSIC
    )

    val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0"
    val sortOrder = "${MediaStore.Audio.Media.TITLE} ASC"

    val queryUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

    val cursor = context.contentResolver.query(
        queryUri,
        projection,
        selection,
        null,
        sortOrder
    )

    cursor?.use {
        Log.d(TAG, "MediaStore cursor contagem = ${it.count}")
        val idCol = it.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
        val titleCol = it.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
        val artistCol = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)

        while (it.moveToNext()) {
            val id = it.getLong(idCol)
            val title = it.getString(titleCol) ?: "Unknown"
            val artist = it.getString(artistCol) ?: "Unknown"
            val contentUri: Uri = ContentUris.withAppendedId(queryUri, id)

            Log.d(TAG, "Encontrei musica: id=$id title=$title artist=$artist uri=$contentUri")
            songs.add(Song(id = id, title = title, artist = artist, uri = contentUri))
        }
    } ?: run {
        Log.d(TAG, "cursor não encontrado")
    }

    return songs
}

fun scanMusicDirectory(context: Context, path: String = "/sdcard/Music") {
    try {
        val dir = File(path)
        if (!dir.exists()) {
            Log.d(TAG, "scanMusicDirectory: pasta não existe: $path")
            return
        }
        val files = dir.listFiles()?.filter { it.isFile }?.map { it.absolutePath }?.toTypedArray()
        if (files == null || files.isEmpty()) {
            Log.d(TAG, "scanMusicDirectory: sem arquivos para escanear em $path")
            return
        }

        Log.d(TAG, "scanMusicDirectory: escaneando ${files.size} arquivos na pasta $path")
        MediaScannerConnection.scanFile(context, files, null) { scannedPath, uri ->
            Log.d(TAG, "scanMusicDirectory: escaneado $scannedPath -> $uri")
        }
    } catch (e: Exception) {
        Log.e(TAG, "scanMusicDirectory error", e)
    }
}

@Composable
fun MusicListScreen(
    modifier: Modifier = Modifier,
    onNavigate: (AppDestinations) -> Unit
) {
    val context = LocalContext.current

    val requiredPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.READ_MEDIA_AUDIO
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }

    var permissionGranted by remember {
        mutableStateOf(
            context.checkSelfPermission(requiredPermission) == android.content.pm.PackageManager.PERMISSION_GRANTED
        )
    }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        permissionGranted = granted
        Log.d(TAG, "Resultado da permissão: $granted")
        if (granted) {
            scanMusicDirectory(context, "/sdcard/Music")
        }
    }

    var songs by remember { mutableStateOf<List<Song>>(emptyList()) }

    LaunchedEffect(permissionGranted) {
        if (permissionGranted) {
            Log.d(TAG, "Permissão concedida -> loading songs")
            scanMusicDirectory(context, "/sdcard/Music")
            songs = loadSongsFromDevice(context)
            Log.d(TAG, "Carregando ${songs.size} musicas de MediaStore")
        } else {
            Log.d(TAG, "Permissão não concedida")
        }
    }

    val mediaPlayer = remember { MediaPlayer() }

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
        if (!permissionGranted) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Permissão necessária para ler músicas", fontWeight = FontWeight.Bold)
                SpacerSmall()
                Button(onClick = { launcher.launch(requiredPermission) }) {
                    Text("Pedir permissão")
                }
            }
            return@Box
        }

        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Text(
                    text = "Todas as músicas",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF003366),
                    modifier = Modifier.padding(top = 24.dp, bottom = 16.dp)
                )
            }

            if (songs.isEmpty()) {
                item {
                    Text("Nenhuma música encontrada. Verifique se os arquivos estão em /sdcard/Music e se o MediaStore os indexou (use adb ou reinicie o emulador).")
                }
            }

            items(songs) { song ->
                SongListItem(song = song, onPlay = { ctx, s ->
                    playSong(ctx, mediaPlayer, s)
                    onNavigate(AppDestinations.FAVORITES)
                })
            }
        }
    }
}

fun playSong(context: Context, mediaPlayer: MediaPlayer, song: Song) {
    try {
        mediaPlayer.reset()
        mediaPlayer.setDataSource(context, song.uri)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            it.start()
            Log.d(TAG, "Tocando: ${song.title}")
        }
        mediaPlayer.setOnErrorListener { _, what, extra ->
            Log.e(TAG, "Erro ao tocar música: what=$what extra=$extra")
            true
        }
    } catch (e: Exception) {
        Log.e(TAG, "Erro em playSong()", e)
    }
}



@Composable
fun SongListItem(song: Song, onPlay: (Context, Song) -> Unit) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onPlay(context, song) },
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
                Text(song.title, fontWeight = FontWeight.Bold, color = Color(0xFF003366))
                Text(song.artist, fontSize = 12.sp, color = Color.Gray)
            }

            IconButton(onClick = { onPlay(context, song) }) {
                Icon(
                    Icons.Default.PlayArrow,
                    contentDescription = "Play Song",
                    tint = Color(0xFF003366)
                )
            }
        }
    }
}


@Composable
private fun SpacerSmall() {
    androidx.compose.foundation.layout.Spacer(modifier = Modifier.padding(6.dp))
}

@Composable
fun MusicListScreenPreview(modifier: Modifier, onNavigate: (AppDestinations) -> Unit) {
    MusicsalTheme {
        Surface {
            MusicListScreen(modifier, onNavigate)
        }
    }
}
