package com.example.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MusicDao {

    // Playlist Queries
    @Query("SELECT * FROM playlists ORDER BY createdAtMs DESC")
    fun getAllPlaylists(): Flow<List<PlaylistEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlist: PlaylistEntity): Long

    @Query("DELETE FROM playlists WHERE id = :playlistId")
    suspend fun deletePlaylist(playlistId: Int)

    // Playlist Songs Queries
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSongToPlaylist(playlistSong: PlaylistSongEntity)

    @Query("DELETE FROM playlist_songs WHERE playlistId = :playlistId AND songId = :songId")
    suspend fun removeSongFromPlaylist(playlistId: Int, songId: String)

    @Query("SELECT * FROM playlist_songs WHERE playlistId = :playlistId ORDER BY addedAtMs ASC")
    fun getSongsInPlaylist(playlistId: Int): Flow<List<PlaylistSongEntity>>

    @Query("DELETE FROM playlist_songs WHERE playlistId = :playlistId")
    suspend fun clearPlaylistSongs(playlistId: Int)

    // Favorite Songs Queries
    @Query("SELECT * FROM favorite_songs ORDER BY favoritedAtMs DESC")
    fun getAllFavorites(): Flow<List<FavoriteSongEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(favorite: FavoriteSongEntity)

    @Query("DELETE FROM favorite_songs WHERE songId = :songId")
    suspend fun removeFavorite(songId: String)
}
