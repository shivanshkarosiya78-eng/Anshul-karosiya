package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LibraryMusic
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.FullPlayerSheet
import com.example.ui.components.MiniPlayer
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LibraryScreen
import com.example.ui.screens.SearchScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.SpotifyGreen
import com.example.ui.viewmodel.MusicViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: MusicViewModel by viewModels {
        MusicViewModel.Factory(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainContent(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MainContent(viewModel: MusicViewModel) {
    var selectedTab by remember { mutableStateOf("home") }
    var isPlayerExpanded by remember { mutableStateOf(false) }

    val currentSong by viewModel.currentSong.collectAsStateWithLifecycle()

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                Column {
                    // Mini music controller if a song is playing
                    if (currentSong != null) {
                        MiniPlayer(
                            viewModel = viewModel,
                            onExpandClick = { isPlayerExpanded = true },
                            modifier = Modifier.testTag("mini_music_player")
                        )
                    }

                    // Navigation Bar (resembles Spotify elegant tab views)
                    NavigationBar(
                        windowInsets = WindowInsets.navigationBars,
                        tonalElevation = 8.dp
                    ) {
                        NavigationBarItem(
                            selected = selectedTab == "home",
                            onClick = { selectedTab = "home" },
                            icon = {
                                Icon(
                                    imageVector = if (selectedTab == "home") Icons.Filled.Home else Icons.Outlined.Home,
                                    contentDescription = "Home"
                                )
                            },
                            label = { Text("Home") },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = SpotifyGreen,
                                selectedTextColor = SpotifyGreen,
                                indicatorColor = MaterialTheme.colorScheme.surfaceVariant
                            ),
                            modifier = Modifier.testTag("nav_home_tab")
                        )

                        NavigationBarItem(
                            selected = selectedTab == "search",
                            onClick = { selectedTab = "search" },
                            icon = {
                                Icon(
                                    imageVector = if (selectedTab == "search") Icons.Filled.Search else Icons.Outlined.Search,
                                    contentDescription = "Search"
                                )
                            },
                            label = { Text("Search") },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = SpotifyGreen,
                                selectedTextColor = SpotifyGreen,
                                indicatorColor = MaterialTheme.colorScheme.surfaceVariant
                            ),
                            modifier = Modifier.testTag("nav_search_tab")
                        )

                        NavigationBarItem(
                            selected = selectedTab == "library",
                            onClick = { selectedTab = "library" },
                            icon = {
                                Icon(
                                    imageVector = if (selectedTab == "library") Icons.Filled.LibraryMusic else Icons.Outlined.LibraryMusic,
                                    contentDescription = "Library"
                                )
                            },
                            label = { Text("Library") },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = SpotifyGreen,
                                selectedTextColor = SpotifyGreen,
                                indicatorColor = MaterialTheme.colorScheme.surfaceVariant
                            ),
                            modifier = Modifier.testTag("nav_library_tab")
                        )
                    }
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                when (selectedTab) {
                    "home" -> HomeScreen(viewModel = viewModel)
                    "search" -> SearchScreen(viewModel = viewModel)
                    "library" -> LibraryScreen(viewModel = viewModel)
                }
            }
        }

        // Full Interactive Neon Music Player Screen Cover (Slides up smooth and immersive)
        AnimatedVisibility(
            visible = isPlayerExpanded,
            enter = slideInVertically(
                initialOffsetY = { fullHeight -> fullHeight },
                animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing)
            ) + fadeIn(),
            exit = slideOutVertically(
                targetOffsetY = { fullHeight -> fullHeight },
                animationSpec = tween(durationMillis = 350, easing = FastOutSlowInEasing)
            ) + fadeOut()
        ) {
            FullPlayerSheet(
                viewModel = viewModel,
                onDismiss = { isPlayerExpanded = false },
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("full_music_player")
            )
        }
    }
}
