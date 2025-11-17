package com.example.musicsal

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MusicPlayerViewModel : ViewModel() {

    private val _currentSong = MutableStateFlow<Song?>(null)
    val currentSong = _currentSong.asStateFlow()

    fun setCurrentSong(song: Song?) {
        _currentSong.value = song
    }
}
