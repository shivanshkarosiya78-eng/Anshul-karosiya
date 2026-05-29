package com.example.player

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.util.Log
import com.example.data.model.Song
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.random.Random

class AudioPlayerManager(private val context: Context) {

    private var mediaPlayer: MediaPlayer? = null
    private val coroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private var progressJob: Job? = null

    // State flows
    private val _currentSong = MutableStateFlow<Song?>(null)
    val currentSong: StateFlow<Song?> = _currentSong.asStateFlow()

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _currentPosition = MutableStateFlow(0L)
    val currentPosition: StateFlow<Long> = _currentPosition.asStateFlow()

    private val _duration = MutableStateFlow(0L)
    val duration: StateFlow<Long> = _duration.asStateFlow()

    private val _isBuffering = MutableStateFlow(false)
    val isBuffering: StateFlow<Boolean> = _isBuffering.asStateFlow()

    private val _isShuffle = MutableStateFlow(false)
    val isShuffle: StateFlow<Boolean> = _isShuffle.asStateFlow()

    private val _isRepeat = MutableStateFlow(false)
    val isRepeat: StateFlow<Boolean> = _isRepeat.asStateFlow()

    // Playlist Queue
    private var currentQueue: List<Song> = emptyList()
    private var currentIndex: Int = -1

    init {
        initializePlayer()
    }

    private fun initializePlayer() {
        try {
            mediaPlayer?.release()
            mediaPlayer = MediaPlayer().apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
                )
                setOnPreparedListener { mp ->
                    _isBuffering.value = false
                    _duration.value = mp.duration.toLong()
                    mp.start()
                    _isPlaying.value = true
                    startPositionUpdates()
                }
                setOnCompletionListener {
                    handleSongCompletion()
                }
                setOnErrorListener { _, what, extra ->
                    Log.e("AudioPlayerManager", "MediaPlayer Error: what = $what, extra = $extra")
                    _isBuffering.value = false
                    _isPlaying.value = false
                    stopPositionUpdates()
                    // Try to autoplay next on failure
                    skipNext()
                    true
                }
                setOnBufferingUpdateListener { _, percent ->
                    // Optional buffering state handling
                }
            }
        } catch (e: Exception) {
            Log.e("AudioPlayerManager", "Error initializing player", e)
        }
    }

    fun playSong(song: Song, queue: List<Song>) {
        currentQueue = queue
        currentIndex = queue.indexOfFirst { it.id == song.id }
        if (currentIndex == -1) {
            // If not in the queue, add and set index
            currentQueue = queue + song
            currentIndex = currentQueue.size - 1
        }

        _currentSong.value = song
        _isBuffering.value = true
        _currentPosition.value = 0L
        _duration.value = song.durationMs

        stopPositionUpdates()

        try {
            mediaPlayer?.apply {
                reset()
                // Set audio attributes again since reset clears them
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
                )
                setDataSource(song.streamUrl)
                prepareAsync()
            }
        } catch (e: Exception) {
            Log.e("AudioPlayerManager", "Error setting data source or preparing", e)
            _isBuffering.value = false
            _isPlaying.value = false
        }
    }

    fun togglePlayPause() {
        val player = mediaPlayer ?: return
        if (_currentSong.value == null && currentQueue.isNotEmpty()) {
            playSong(currentQueue.first(), currentQueue)
            return
        }

        if (player.isPlaying) {
            player.pause()
            _isPlaying.value = false
            stopPositionUpdates()
        } else {
            if (_isPlaying.value) return // If loading or preparing
            try {
                player.start()
                _isPlaying.value = true
                startPositionUpdates()
            } catch (e: Exception) {
                Log.e("AudioPlayerManager", "Failed to start player", e)
            }
        }
    }

    fun seekTo(positionMs: Long) {
        mediaPlayer?.let { player ->
            try {
                player.seekTo(positionMs.toInt())
                _currentPosition.value = positionMs
            } catch (e: Exception) {
                Log.e("AudioPlayerManager", "Error seeking", e)
            }
        }
    }

    fun skipNext() {
        if (currentQueue.isEmpty()) return

        if (_isShuffle.value) {
            currentIndex = Random.nextInt(currentQueue.size)
        } else {
            currentIndex = (currentIndex + 1) % currentQueue.size
        }

        val nextSong = currentQueue.getOrNull(currentIndex)
        if (nextSong != null) {
            playSong(nextSong, currentQueue)
        }
    }

    fun skipPrevious() {
        if (currentQueue.isEmpty()) return

        // If player is past 3 seconds of song, reset track instead of skipping previous
        if (_currentPosition.value > 3000L) {
            seekTo(0)
            return
        }

        currentIndex = if (currentIndex <= 0) {
            currentQueue.size - 1
        } else {
            currentIndex - 1
        }

        val prevSong = currentQueue.getOrNull(currentIndex)
        if (prevSong != null) {
            playSong(prevSong, currentQueue)
        }
    }

    fun setShuffle(enabled: Boolean) {
        _isShuffle.value = enabled
    }

    fun setRepeat(enabled: Boolean) {
        _isRepeat.value = enabled
    }

    private fun handleSongCompletion() {
        if (_isRepeat.value) {
            // Replay current song
            val current = _currentSong.value
            if (current != null) {
                playSong(current, currentQueue)
            }
        } else {
            skipNext()
        }
    }

    private fun startPositionUpdates() {
        progressJob?.cancel()
        progressJob = coroutineScope.launch {
            while (isActive) {
                mediaPlayer?.let { player ->
                    if (player.isPlaying) {
                        _currentPosition.value = player.currentPosition.toLong()
                    }
                }
                delay(250)
            }
        }
    }

    private fun stopPositionUpdates() {
        progressJob?.cancel()
        progressJob = null
    }

    fun release() {
        coroutineScope.cancel()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
