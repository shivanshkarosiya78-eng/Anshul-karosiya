package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.local.MusicDatabase
import com.example.data.local.PlaylistEntity
import com.example.data.model.Song
import com.example.data.repository.MusicRepository
import com.example.player.AudioPlayerManager
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MusicViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: MusicRepository
    private val playerManager: AudioPlayerManager

    // Filter states
    private val _selectedLanguage = MutableStateFlow("All")
    val selectedLanguage = _selectedLanguage.asStateFlow()

    private val _selectedGenre = MutableStateFlow("All")
    val selectedGenre = _selectedGenre.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    // Base songs list
    val allSongs: List<Song> = Song.PREDEFINED_SONGS

    // Filtered songs for Home screen
    val filteredSongs: StateFlow<List<Song>> = combine(
        _selectedLanguage,
        _selectedGenre
    ) { language, genre ->
        var list = allSongs
        if (language != "All") {
            list = list.filter { it.language.equals(language, ignoreCase = true) }
        }
        if (genre != "All") {
            list = list.filter { it.genre.equals(genre, ignoreCase = true) }
        }
        list
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), allSongs)

    // Filtered songs for Search screen
    val searchedSongs: StateFlow<List<Song>> = combine(
        _searchQuery,
        _selectedLanguage,
        _selectedGenre
    ) { query, lang, gen ->
        var list = allSongs
        if (query.isNotEmpty()) {
            list = list.filter {
                it.title.contains(query, ignoreCase = true) ||
                it.artist.contains(query, ignoreCase = true) ||
                it.album.contains(query, ignoreCase = true)
            }
        }
        if (lang != "All") {
            list = list.filter { it.language.equals(lang, ignoreCase = true) }
        }
        if (gen != "All") {
            list = list.filter { it.genre.equals(gen, ignoreCase = true) }
        }
        list
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), allSongs)

    // Playlists & Favorites from Room DB
    val playlists: StateFlow<List<PlaylistEntity>>
    val favoriteSongIds: StateFlow<Set<String>>

    // Media Player States delegated from AudioPlayerManager
    val currentSong: StateFlow<Song?>
    val isPlaying: StateFlow<Boolean>
    val currentPosition: StateFlow<Long>
    val duration: StateFlow<Long>
    val isBuffering: StateFlow<Boolean>
    val isShuffle: StateFlow<Boolean>
    val isRepeat: StateFlow<Boolean>

    init {
        val database = MusicDatabase.getDatabase(application)
        repository = MusicRepository(database.musicDao())
        playerManager = AudioPlayerManager(application)

        playlists = repository.allPlaylists.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        favoriteSongIds = repository.favoriteSongIds.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptySet()
        )

        // Delegate flows
        currentSong = playerManager.currentSong
        isPlaying = playerManager.isPlaying
        currentPosition = playerManager.currentPosition
        duration = playerManager.duration
        isBuffering = playerManager.isBuffering
        isShuffle = playerManager.isShuffle
        isRepeat = playerManager.isRepeat
    }

    // Languages
    val availableLanguages = listOf("All", "Hindi", "English", "Punjabi", "Tamil", "Telugu")

    // Genres
    val availableGenres = listOf("All", "Pop", "Rock", "Classical", "Romantic", "Regional / Folk")

    fun selectLanguage(language: String) {
        _selectedLanguage.value = language
    }

    fun selectGenre(genre: String) {
        _selectedGenre.value = genre
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    // Player Actions
    fun playSong(song: Song, queue: List<Song>) {
        playerManager.playSong(song, queue)
    }

    fun togglePlayPause() {
        playerManager.togglePlayPause()
    }

    fun seekTo(positionMs: Long) {
        playerManager.seekTo(positionMs)
    }

    fun skipNext() {
        playerManager.skipNext()
    }

    fun skipPrevious() {
        playerManager.skipPrevious()
    }

    fun toggleShuffle() {
        playerManager.setShuffle(!playerManager.isShuffle.value)
    }

    fun toggleRepeat() {
        playerManager.setRepeat(!playerManager.isRepeat.value)
    }

    // Favorite Actions
    fun toggleFavorite(songId: String) {
        viewModelScope.launch {
            val isFav = favoriteSongIds.value.contains(songId)
            repository.toggleFavorite(songId, !isFav)
        }
    }

    // Playlist Actions
    fun createPlaylist(name: String) {
        viewModelScope.launch {
            repository.createPlaylist(name)
        }
    }

    fun deletePlaylist(playlistId: Int) {
        viewModelScope.launch {
            repository.deletePlaylist(playlistId)
        }
    }

    fun addSongToPlaylist(playlistId: Int, songId: String) {
        viewModelScope.launch {
            repository.addSongToPlaylist(playlistId, songId)
        }
    }

    fun removeSongFromPlaylist(playlistId: Int, songId: String) {
        viewModelScope.launch {
            repository.removeSongFromPlaylist(playlistId, songId)
        }
    }

    fun getSongsInPlaylist(playlistId: Int): Flow<List<Song>> {
        return repository.getSongsInPlaylist(playlistId)
    }

    override fun onCleared() {
        super.onCleared()
        playerManager.release()
    }

    // Factory Class
    class Factory(private val application: Application) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MusicViewModel::class.java)) {
                return MusicViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
