package com.opennext.app.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.opennext.app.data.mockGames
import com.opennext.app.data.model.Game
import com.opennext.app.ui.components.AdaptiveNavigation
import com.opennext.app.ui.components.GameCard
import com.opennext.app.ui.components.HeroGameCard
import com.opennext.app.ui.components.NavItem
import com.opennext.app.ui.components.SearchBar
import com.opennext.app.ui.theme.Blue40
import com.opennext.app.ui.theme.DarkBg
import com.opennext.app.ui.theme.DarkCard
import com.opennext.app.ui.theme.OnDark
import com.opennext.app.ui.theme.OnDarkMuted
import com.opennext.app.ui.theme.OnDarkVariant
import com.opennext.app.ui.theme.PlayerGreen
import kotlinx.coroutines.delay

private val navigationItems = listOf(
    NavItem("Home", Icons.Filled.Home, Icons.Outlined.Home),
    NavItem("Library", Icons.Filled.LibraryBooks, Icons.Outlined.LibraryBooks),
    NavItem("Settings", Icons.Filled.Settings, Icons.Outlined.Settings),
)

@Composable
fun HomeScreen(
    onGameClick: (String) -> Unit,
    onLibraryClick: () -> Unit,
    onSettingsClick: () -> Unit,
) {
    var selectedNav by remember { mutableIntStateOf(0) }
    var searchQuery by remember { mutableStateOf("") }
    val configuration = LocalConfiguration.current
    val isTablet = configuration.screenWidthDp.dp >= 600.dp

    val featuredGames = remember { mockGames.filter { it.isFeatured } }
    val recentlyPlayed = remember {
        mockGames.filter { it.lastPlayed != null }
            .sortedByDescending { it.lastPlayed }
            .take(10)
    }
    val popularGames = remember {
        mockGames.sortedByDescending { it.rating }.take(12)
    }
    val allGames = remember { mockGames }

    val filteredGames = remember(searchQuery, allGames) {
        if (searchQuery.isBlank()) allGames
        else allGames.filter { it.title.contains(searchQuery, ignoreCase = true) }
    }

    val pagerState = rememberPagerState(pageCount = { featuredGames.size })

    LaunchedEffect(pagerState) {
        delay(5000)
        val next = (pagerState.currentPage + 1) % featuredGames.size
        pagerState.animateScrollToPage(next)
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
            searchQuery = searchQuery,
            onSearchQueryChange = { searchQuery = it },
            featuredGames = featuredGames,
            pagerState = pagerState,
            recentlyPlayed = recentlyPlayed,
            popularGames = popularGames,
            filteredGames = filteredGames,
            selectedNav = selectedNav,
            onNavSelected = ::handleNavSelected,
            onGameClick = onGameClick,
        )
    } else {
        PhoneHomeLayout(
            searchQuery = searchQuery,
            onSearchQueryChange = { searchQuery = it },
            featuredGames = featuredGames,
            pagerState = pagerState,
            recentlyPlayed = recentlyPlayed,
            popularGames = popularGames,
            filteredGames = filteredGames,
            selectedNav = selectedNav,
            onNavSelected = ::handleNavSelected,
            onGameClick = onGameClick,
        )
    }
}

@Composable
private fun PhoneHomeLayout(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    featuredGames: List<Game>,
    pagerState: androidx.compose.foundation.pager.PagerState,
    recentlyPlayed: List<Game>,
    popularGames: List<Game>,
    filteredGames: List<Game>,
    selectedNav: Int,
    onNavSelected: (Int) -> Unit,
    onGameClick: (String) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBg)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 80.dp),
        ) {
            item {
                SearchBar(
                    value = searchQuery,
                    onValueChange = onSearchQueryChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                        .padding(top = 32.dp),
                )
            }

            if (searchQuery.isBlank()) {
                item(key = "hero") {
                    HeroSection(
                        games = featuredGames,
                        pagerState = pagerState,
                        onGameClick = onGameClick,
                    )
                }

                if (recentlyPlayed.isNotEmpty()) {
                    item(key = "recently_played") {
                        GameRow(
                            title = "Recently Played",
                            games = recentlyPlayed,
                            onGameClick = onGameClick,
                        )
                    }
                }

                item(key = "popular") {
                    GameRow(
                        title = "Popular",
                        games = popularGames,
                        onGameClick = onGameClick,
                    )
                }
            }

            item(key = "all_games_header") {
                SectionTitle(
                    title = if (searchQuery.isBlank()) "All Games" else "Search Results",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                )
            }

            itemsIndexed(
                items = filteredGames,
                key = { _, game -> game.id }
            ) { index, game ->
                val animatedVisibility = fadeIn(
                    animationSpec = tween(300, delayMillis = index * 80)
                ) + slideInVertically(
                    animationSpec = tween(300, delayMillis = index * 80)
                ) { it / 2 }

                AnimatedVisibility(
                    visible = true,
                    enter = animatedVisibility,
                ) {
                    GameCard(
                        game = game,
                        onClick = { onGameClick(game.id) },
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                            .fillMaxWidth(),
                    )
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

        AdaptiveNavigation(
            items = navigationItems,
            selectedIndex = selectedNav,
            onItemSelected = onNavSelected,
            isTablet = false,
            modifier = Modifier.align(Alignment.BottomCenter),
        )
    }
}

@Composable
private fun TabletHomeLayout(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    featuredGames: List<Game>,
    pagerState: androidx.compose.foundation.pager.PagerState,
    recentlyPlayed: List<Game>,
    popularGames: List<Game>,
    filteredGames: List<Game>,
    selectedNav: Int,
    onNavSelected: (Int) -> Unit,
    onGameClick: (String) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBg),
    ) {
        AdaptiveNavigation(
            items = navigationItems,
            selectedIndex = selectedNav,
            onItemSelected = onNavSelected,
            isTablet = true,
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize(),
            contentPadding = PaddingValues(bottom = 16.dp),
        ) {
            item {
                SearchBar(
                    value = searchQuery,
                    onValueChange = onSearchQueryChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 12.dp)
                        .padding(top = 24.dp),
                )
            }

            if (searchQuery.isBlank()) {
                item(key = "hero") {
                    HeroSection(
                        games = featuredGames,
                        pagerState = pagerState,
                        onGameClick = onGameClick,
                        contentPadding = PaddingValues(horizontal = 24.dp),
                    )
                }

                item(key = "recently_played") {
                    GameRow(
                        title = "Recently Played",
                        games = recentlyPlayed,
                        onGameClick = onGameClick,
                        contentPadding = PaddingValues(horizontal = 24.dp),
                    )
                }

                item(key = "popular") {
                    GameRow(
                        title = "Popular",
                        games = popularGames,
                        onGameClick = onGameClick,
                        contentPadding = PaddingValues(horizontal = 24.dp),
                    )
                }
            }

            item(key = "all_games_header") {
                SectionTitle(
                    title = if (searchQuery.isBlank()) "All Games" else "Search Results",
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp),
                )
            }

            item(key = "all_games_grid") {
                var columns = 3
                if (filteredGames.size < 3) columns = filteredGames.size

                val gridColumns = StaggeredGridCells.Adaptive(minSize = 180.dp)

                LazyVerticalStaggeredGrid(
                    columns = gridColumns,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(((filteredGames.size / columns + 1) * 220).dp),
                    contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalItemSpacing = 12.dp,
                    userScrollEnabled = false,
                ) {
                    itemsIndexed(
                        items = filteredGames,
                        key = { _, game -> game.id },
                    ) { index, game ->
                        val animatedVisibility = fadeIn(
                            animationSpec = tween(300, delayMillis = index * 80)
                        ) + slideInVertically(
                            animationSpec = tween(300, delayMillis = index * 80)
                        ) { it / 2 }

                        AnimatedVisibility(
                            visible = true,
                            enter = animatedVisibility,
                        ) {
                            GameCard(
                                game = game,
                                onClick = { onGameClick(game.id) },
                            )
                        }
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

@Composable
private fun HeroSection(
    games: List<Game>,
    pagerState: androidx.compose.foundation.pager.PagerState,
    onGameClick: (String) -> Unit,
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
            PressableGameCard(
                game = games[page],
                onClick = { onGameClick(games[page].id) },
                modifier = Modifier.fillMaxWidth(),
            ) {
                HeroGameCard(
                    game = games[page],
                    onPlay = onGameClick,
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

@Composable
private fun GameRow(
    title: String,
    games: List<Game>,
    onGameClick: (String) -> Unit,
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp),
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
    ) {
        SectionTitle(
            title = title,
            modifier = Modifier.padding(horizontal = contentPadding.calculateHorizontalPadding()),
        )
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(
            contentPadding = contentPadding,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            itemsIndexed(
                items = games,
                key = { _, game -> game.id },
            ) { index, game ->
                val animatedVisibility = fadeIn(
                    animationSpec = tween(300, delayMillis = index * 60)
                ) + slideInVertically(
                    animationSpec = tween(300, delayMillis = index * 60)
                ) { it / 3 }

                AnimatedVisibility(
                    visible = true,
                    enter = animatedVisibility,
                ) {
                    PressableGameCard(
                        game = game,
                        onClick = { onGameClick(game.id) },
                        modifier = Modifier.width(160.dp),
                    ) {
                        GameCard(
                            game = game,
                            onClick = { onGameClick(game.id) },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionTitle(
    title: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = OnDark,
        modifier = modifier,
    )
}

@Composable
private fun <T : Any> PressableGameCard(
    game: T,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by androidx.compose.animation.core.animateFloatAsState(
        targetValue = if (isPressed) 0.96f else 1f,
        animationSpec = tween(150),
        label = "scale",
    )

    Box(
        modifier = modifier
            .scale(scale)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick,
            ),
    ) {
        content()
    }
}