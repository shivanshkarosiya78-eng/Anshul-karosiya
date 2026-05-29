package com.example.data.repository

import com.example.data.local.FavoriteSongEntity
import com.example.data.local.MusicDao
import com.example.data.local.PlaylistEntity
import com.example.data.local.PlaylistSongEntity
import com.example.data.model.Song
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MusicRepository(private val musicDao: MusicDao) {

    // Predefined static list of tracks
    fun getAllSongs(): List<Song> = Song.PREDEFINED_SONGS

    fun getSongsByLanguage(language: String): List<Song> {
        return if (language.lowercase() == "all") {
            getAllSongs()
        } else {
            getAllSongs().filter { it.language.equals(language, ignoreCase = true) }
        }
    }

    fun getSongsByGenre(genre: String): List<Song> {
        return if (genre.lowercase() == "all") {
            getAllSongs()
        } else {
            getAllSongs().filter { it.genre.equals(genre, ignoreCase = true) }
        }
    }

    // Playlists Methods
    val allPlaylists: Flow<List<PlaylistEntity>> = musicDao.getAllPlaylists()

    suspend fun createPlaylist(name: String): Long {
        return musicDao.insertPlaylist(PlaylistEntity(name = name))
    }

    suspend fun deletePlaylist(playlistId: Int) {
        musicDao.clearPlaylistSongs(playlistId)
        musicDao.deletePlaylist(playlistId)
    }

    // Playlist Songs Methods
    suspend fun addSongToPlaylist(playlistId: Int, songId: String) {
        musicDao.addSongToPlaylist(PlaylistSongEntity(playlistId = playlistId, songId = songId))
    }

    suspend fun removeSongFromPlaylist(playlistId: Int, songId: String) {
        musicDao.removeSongFromPlaylist(playlistId = playlistId, songId = songId)
    }

    fun getSongsInPlaylist(playlistId: Int): Flow<List<Song>> {
        return musicDao.getSongsInPlaylist(playlistId).map { mappingList ->
            val songIdMap = mappingList.map { it.songId }.toSet()
            getAllSongs().filter { it.id in songIdMap }
        }
    }

    // Favorites Methods
    val favoriteSongIds: Flow<Set<String>> = musicDao.getAllFavorites().map { favorites ->
        favorites.map { it.songId }.toSet()
    }

    suspend fun toggleFavorite(songId: String, isFav: Boolean) {
        if (isFav) {
            musicDao.addFavorite(FavoriteSongEntity(songId = songId))
        } else {
            musicDao.removeFavorite(songId)
        }
    }
}
