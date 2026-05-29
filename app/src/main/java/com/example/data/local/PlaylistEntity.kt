package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlists")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val createdAtMs: Long = System.currentTimeMillis()
)

@Entity(tableName = "playlist_songs", primaryKeys = ["playlistId", "songId"])
data class PlaylistSongEntity(
    val playlistId: Int,
    val songId: String,
    val addedAtMs: Long = System.currentTimeMillis()
)

@Entity(tableName = "favorite_songs")
data class FavoriteSongEntity(
    @PrimaryKey val songId: String,
    val favoritedAtMs: Long = System.currentTimeMillis()
)
