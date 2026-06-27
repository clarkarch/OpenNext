package com.opennext.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryBooks
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LibraryBooks
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.opennext.app.data.mockGames
import com.opennext.app.data.mockUser
import com.opennext.app.data.model.Game
import com.opennext.app.ui.components.AdaptiveNavigation
import com.opennext.app.ui.components.GameCard
import com.opennext.app.ui.components.GameListItem
import com.opennext.app.ui.components.HeroGameCard
import com.opennext.app.ui.components.NavItem
import com.opennext.app.ui.components.SearchBar
import com.opennext.app.ui.components.SectionHeader
import com.opennext.app.ui.theme.Blue40
import com.opennext.app.ui.theme.DarkBg
import com.opennext.app.ui.theme.OnDarkMuted
import com.opennext.app.ui.theme.OnDarkVariant
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private val navigationItems = listOf(
    NavItem("Home", Icons.Filled.Home, Icons.Outlined.Home),
    NavItem("Library", Icons.Filled.LibraryBooks, Icons.Outlined.LibraryBooks),
    NavItem("Settings", Icons.Filled.Settings, Icons.Outlined.Settings),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onGameClick: (String) -> Unit,
    onLibraryClick: () -> Unit,
    onSettingsClick: () -> Unit,
) {
    var selectedNav by remember { mutableIntStateOf(0) }
    var searchQuery by remember { mutableStateOf("") }
    val games = remember { mutableStateListOf<Game>() }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val configuration = LocalConfiguration.current
    val isTablet = configuration.screenWidthDp.dp >= 600.dp

    LaunchedEffect(Unit) {
        games.clear()
        games.addAll(mockGames)
    }

    val filteredGames = remember(searchQuery, games) {
        if (searchQuery.isBlank()) games
        else games.filter {
            it.title.contains(searchQuery, ignoreCase = true)
        }
    }

    val featuredGames = remember(games) {
        games.filter { it.isFeatured }
    }

    val recentlyPlayed = remember(games) {
        games.filter { it.lastPlayed != null }
            .sortedByDescending { it.lastPlayed }
    }

    val pagerState = rememberPagerState(pageCount = { featuredGames.size })

    LaunchedEffect(featuredGames.size) {
        if (featuredGames.size > 1) {
            while (true) {
                delay(5000)
                val nextPage = (pagerState.currentPage + 1) % featuredGames.size
                pagerState.animateScrollToPage(nextPage)
            }
        }
    }

    fun handleFavoriteToggle(gameId: String) {
        val index = games.indexOfFirst { it.id == gameId }
        if (index != -1) {
            val game = games[index]
            val updated = game.copy(isFavorite = !game.isFavorite)
            games[index] = updated
            scope.launch {
                snackbarHostState.showSnackbar(
                    if (updated.isFavorite) "Added to favorites"
                    else "Removed from favorites"
                )
            }
        }
    }

    fun handleNavSelected(index: Int) {
        selectedNav = index
        when (index) {
            1 -> onLibraryClick()
            2 -> onSettingsClick()
        }
    }

    if (isTablet) {
        TabletHomeLayout(
            filteredGames = filteredGames,
            featuredGames = featuredGames,
            recentlyPlayed = recentlyPlayed,
            pagerState = pagerState,
            searchQuery = searchQuery,
            onSearchQueryChange = { searchQuery = it },
            selectedNav = selectedNav,
            onNavSelected = ::handleNavSelected,
            onGameClick = onGameClick,
            onFavoriteToggle = ::handleFavoriteToggle,
            snackbarHostState = snackbarHostState,
            avatarUrl = mockUser.avatarUrl,
        )
    } else {
        PhoneHomeLayout(
            filteredGames = filteredGames,
            featuredGames = featuredGames,
            recentlyPlayed = recentlyPlayed,
            pagerState = pagerState,
            searchQuery = searchQuery,
            onSearchQueryChange = { searchQuery = it },
            selectedNav = selectedNav,
            onNavSelected = ::handleNavSelected,
            onGameClick = onGameClick,
            onFavoriteToggle = ::handleFavoriteToggle,
            snackbarHostState = snackbarHostState,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PhoneHomeLayout(
    filteredGames: List<Game>,
    featuredGames: List<Game>,
    recentlyPlayed: List<Game>,
    pagerState: androidx.compose.foundation.pager.PagerState,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    selectedNav: Int,
    onNavSelected: (Int) -> Unit,
    onGameClick: (String) -> Unit,
    onFavoriteToggle: (String) -> Unit,
    snackbarHostState: SnackbarHostState,
) {
    Scaffold(
        containerColor = DarkBg,
        topBar = {
            SearchBar(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .padding(top = 32.dp),
            )
        },
        bottomBar = {
            AdaptiveNavigation(
                items = navigationItems,
                selectedIndex = selectedNav,
                onItemSelected = onNavSelected,
                isTablet = false,
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(bottom = 16.dp),
        ) {
            if (searchQuery.isBlank()) {
                item(key = "hero") {
                    HeroCarousel(
                        games = featuredGames,
                        pagerState = pagerState,
                        onPlay = onGameClick,
                    )
                }

                if (recentlyPlayed.isNotEmpty()) {
                    item(key = "recently_played_header") {
                        SectionHeader(title = "Recently Played")
                    }
                    item(key = "recently_played_row") {
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            items(
                                items = recentlyPlayed,
                                key = { it.id },
                            ) { game ->
                                GameCard(
                                    game = game,
                                    onClick = { onGameClick(game.id) },
                                    onFavoriteToggle = { onFavoriteToggle(game.id) },
                                    modifier = Modifier.width(160.dp),
                                )
                            }
                        }
                    }
                    item { Spacer(modifier = Modifier.height(8.dp)) }
                }
            }

            item(key = "all_games_header") {
                SectionHeader(
                    title = if (searchQuery.isBlank()) "All Games" else "Search Results",
                )
            }

            item(key = "all_games_grid") {
                val gridHeight = when {
                    filteredGames.isEmpty() -> 200.dp
                    filteredGames.size <= 2 -> 300.dp
                    else -> ((filteredGames.size + 1) / 2 * 300).dp
                }
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(gridHeight),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    userScrollEnabled = false,
                ) {
                    items(
                        items = filteredGames,
                        key = { it.id },
                    ) { game ->
                        GameCard(
                            game = game,
                            onClick = { onGameClick(game.id) },
                            onFavoriteToggle = { onFavoriteToggle(game.id) },
                        )
                    }
                }
            }

            if (filteredGames.isEmpty()) {
                item(key = "empty") {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(48.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = "No games found",
                            style = MaterialTheme.typography.bodyLarge,
                            color = OnDarkVariant,
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TabletHomeLayout(
    filteredGames: List<Game>,
    featuredGames: List<Game>,
    recentlyPlayed: List<Game>,
    pagerState: androidx.compose.foundation.pager.PagerState,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    selectedNav: Int,
    onNavSelected: (Int) -> Unit,
    onGameClick: (String) -> Unit,
    onFavoriteToggle: (String) -> Unit,
    snackbarHostState: SnackbarHostState,
    avatarUrl: String?,
) {
    Scaffold(
        containerColor = DarkBg,
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { padding ->
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            AdaptiveNavigation(
                items = navigationItems,
                selectedIndex = selectedNav,
                onItemSelected = onNavSelected,
                isTablet = true,
                avatarUrl = avatarUrl,
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize(),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 12.dp)
                        .padding(top = 24.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    SearchBar(
                        value = searchQuery,
                        onValueChange = onSearchQueryChange,
                        modifier = Modifier.weight(1f),
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                }

                if (searchQuery.isBlank()) {
                    HeroCarousel(
                        games = featuredGames,
                        pagerState = pagerState,
                        onPlay = onGameClick,
                        contentPadding = PaddingValues(horizontal = 24.dp),
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                ) {
                    Column(
                        modifier = Modifier
                            .width(320.dp)
                            .fillMaxSize(),
                    ) {
                        SectionHeader(
                            title = "Recently Played",
                            modifier = Modifier.padding(start = 8.dp),
                        )
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            items(
                                items = recentlyPlayed,
                                key = { it.id },
                            ) { game ->
                                GameListItem(
                                    game = game,
                                    onClick = { onGameClick(game.id) },
                                )
                            }
                        }
                    }

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize(),
                    ) {
                        SectionHeader(
                            title = if (searchQuery.isBlank()) "All Games" else "Search Results",
                            modifier = Modifier.padding(start = 8.dp),
                        )
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(3),
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            items(
                                items = filteredGames,
                                key = { it.id },
                            ) { game ->
                                GameCard(
                                    game = game,
                                    onClick = { onGameClick(game.id) },
                                    onFavoriteToggle = { onFavoriteToggle(game.id) },
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HeroCarousel(
    games: List<Game>,
    pagerState: androidx.compose.foundation.pager.PagerState,
    onPlay: (String) -> Unit,
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp),
) {
    if (games.isEmpty()) return

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(contentPadding),
    ) {
        HorizontalPager(
            state = pagerState,
            pageSpacing = 12.dp,
            modifier = Modifier.fillMaxWidth(),
        ) { page ->
            if (page < games.size) {
                HeroGameCard(
                    game = games[page],
                    onPlay = onPlay,
                )
            }
        }

        if (games.size > 1) {
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                repeat(games.size) { index ->
                    val isActive = pagerState.currentPage == index
                    Box(
                        modifier = Modifier
                            .height(6.dp)
                            .width(if (isActive) 24.dp else 6.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(
                                if (isActive) Blue40
                                else OnDarkMuted.copy(alpha = 0.4f)
                            ),
                    )
                }
            }
        }
    }
}
