package com.example.musicsal

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.musicsal.ui.theme.MusicsalTheme

@Composable
fun MusicPlayerScreen(modifier: Modifier = Modifier) {
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
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Header()
            Spacer(modifier = Modifier.height(64.dp))
            MusicControls()
            Spacer(modifier = Modifier.weight(1f))
            LyricsSection()
        }
    }
}

@Composable
fun Header() {
    Column(horizontalAlignment = Alignment.Start, modifier = Modifier.padding(top=24.dp)) {
        Text(
            text = "DUSK TILL DOWN",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF003366)
        )
        Text(
            text = "ZAYN ft. Sia",
            fontSize = 22.sp,
            color = Color(0xFF003366)
        )
        Text(
            text = "Kazuya's AirPods Pro",
            fontSize = 18.sp,
            color = Color.Gray
        )
    }
}

@Composable
fun MusicControls() {
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(350.dp)) {
        Surface(
            shape = RoundedCornerShape(30.dp),
            color = Color.White.copy(alpha = 0.5f),
            modifier = Modifier
                .height(350.dp)
                .align(Alignment.CenterStart)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround,
                modifier = Modifier.padding(vertical = 16.dp, horizontal = 8.dp)
            ) {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(Icons.Default.Refresh, contentDescription = "Repeat")
                }
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(painterResource(id = R.drawable.skip_anterior), contentDescription = "Previous")
                }
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(Icons.Default.PlayArrow, contentDescription = "Play")
                }
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(painterResource(id = R.drawable.skip_next), contentDescription = "Previous")
                }
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(Icons.Default.Favorite, contentDescription = "Favorite", tint = Color.Red)
                }
            }
        }

        AlbumArtWithProgress(modifier = Modifier
            .align(Alignment.CenterEnd)
            .offset(x = 110.dp))
    }
}

@Composable
fun AlbumArtWithProgress(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.size(320.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidth = 12.dp.toPx()
            drawArc(
                color = Color.White.copy(alpha = 0.8f),
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = strokeWidth)
            )
            drawArc(
                color = Color.Red,
                startAngle = -90f,
                sweepAngle = 90f,
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
                .clip(CircleShape)
                .background(Color.Gray)
        ) {
            // Arte do album
        }
    }
}


@Composable
fun LyricsSection() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { /*TODO*/ },
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.7f))
        ) {
            Text(text = "Letra Completa", color = Color(0xFF003366))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MusicPlayerScreenPreview() {
    MusicsalTheme {
        MusicPlayerScreen()
    }
}
