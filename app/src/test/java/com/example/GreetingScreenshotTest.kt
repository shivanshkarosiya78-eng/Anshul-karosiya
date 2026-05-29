package com.example

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.screens.TrendingSongCard
import com.example.data.model.Song
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(qualifiers = RobolectricDeviceQualifiers.Pixel8, sdk = [36])
class GreetingScreenshotTest {

  @get:Rule val composeTestRule = createComposeRule()

  @Test
  fun greeting_screenshot() {
    val sampleSong = Song(
        id = "99",
        title = "Symphony Beats",
        artist = "Premium AI Streamer",
        album = "Studio Tracks",
        genre = "Ambient",
        language = "Multilingual",
        streamUrl = "http://localhost",
        imageUrl = "https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?w=500",
        durationMs = 180000
    )

    composeTestRule.setContent {
      MyApplicationTheme {
        TrendingSongCard(song = sampleSong, onClick = {})
      }
    }

    composeTestRule.onRoot().captureRoboImage(filePath = "src/test/screenshots/greeting.png")
  }
}
