package com.example.data.model

data class Song(
    val id: String,
    val title: String,
    val artist: String,
    val album: String,
    val genre: String,
    val language: String,
    val streamUrl: String,
    val imageUrl: String,
    val durationMs: Long,
    val isTrending: Boolean = false
) {
    companion object {
        val PREDEFINED_SONGS = listOf(
            Song(
                id = "1",
                title = "Tum Hi Ho (Acoustic Cover)",
                artist = "Sufi & Romance Strings",
                album = "Aashiqui Unplugged",
                genre = "Romantic",
                language = "Hindi",
                streamUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3",
                imageUrl = "https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?w=500",
                durationMs = 372000,
                isTrending = true
            ),
            Song(
                id = "2",
                title = "Midnight Drive",
                artist = "The Synthwave Collective",
                album = "Retro Neon",
                genre = "Pop/Electronic",
                language = "English",
                streamUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-2.mp3",
                imageUrl = "https://images.unsplash.com/photo-1470225620780-dba8ba36b745?w=500",
                durationMs = 425000,
                isTrending = true
            ),
            Song(
                id = "3",
                title = "Kesariya (Rendition)",
                artist = "Acoustic Guitar Sessions",
                album = "Love in Banaras",
                genre = "Pop",
                language = "Hindi",
                streamUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-3.mp3",
                imageUrl = "https://images.unsplash.com/photo-1514525253161-7a46d19cd819?w=500",
                durationMs = 302000,
                isTrending = true
            ),
            Song(
                id = "4",
                title = "Gidda Beat Anthem",
                artist = "Sher-E-Punjab Crew",
                album = "Bhangra Explosion",
                genre = "Regional / Folk",
                language = "Punjabi",
                streamUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-4.mp3",
                imageUrl = "https://images.unsplash.com/photo-1459749411175-04bf5292ceea?w=500",
                durationMs = 302000,
                isTrending = false
            ),
            Song(
                id = "5",
                title = "Raga Yaman (Sitar Meditation)",
                artist = "Pandit Alok Sharma",
                album = "Hindustani Classics",
                genre = "Classical",
                language = "Hindi",
                streamUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-5.mp3",
                imageUrl = "https://images.unsplash.com/photo-1511379938547-c1f69419868d?w=500",
                durationMs = 360000,
                isTrending = false
            ),
            Song(
                id = "6",
                title = "Neon Desperado",
                artist = "Dust & Gears",
                album = "Desert Rock Chronicle",
                genre = "Rock",
                language = "English",
                streamUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-6.mp3",
                imageUrl = "https://images.unsplash.com/photo-1498038432885-c6f3f1b912ee?w=500",
                durationMs = 398000,
                isTrending = true
            ),
            Song(
                id = "7",
                title = "Singara Chennai Beats",
                artist = "Anirudh & Boys",
                album = "Madras Kuthu",
                genre = "Regional / Kuthu",
                language = "Tamil",
                streamUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-7.mp3",
                imageUrl = "https://images.unsplash.com/photo-1510915361894-db8b60106cb1?w=500",
                durationMs = 345000,
                isTrending = true
            ),
            Song(
                id = "8",
                title = "Telugu Melodic Rain",
                artist = "Sravana Bhargavi Acoustic",
                album = "Kalyana Vaibhogame",
                genre = "Romantic",
                language = "Telugu",
                streamUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-8.mp3",
                imageUrl = "https://images.unsplash.com/photo-1514525253161-7a46d19cd819?w=500",
                durationMs = 318000,
                isTrending = false
            ),
            Song(
                id = "9",
                title = "Billo rani Dance Club",
                artist = "DJ Johal Remix",
                album = "Punjab Club Hits",
                genre = "Pop",
                language = "Punjabi",
                streamUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-9.mp3",
                imageUrl = "https://images.unsplash.com/photo-1459749411175-04bf5292ceea?w=500",
                durationMs = 354000,
                isTrending = true
            ),
            Song(
                id = "10",
                title = "Classical Carnatic Violin",
                artist = "Lalgudi Strings",
                album = "Devotional Beats",
                genre = "Classical",
                language = "Tamil",
                streamUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-10.mp3",
                imageUrl = "https://images.unsplash.com/photo-1511379938547-c1f69419868d?w=500",
                durationMs = 390000,
                isTrending = false
            )
        )
    }
}
