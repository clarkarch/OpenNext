package com.opennext.app.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryBooks
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LibraryBooks
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.opennext.app.data.mockGames
import com.opennext.app.data.mockUser
import com.opennext.app.data.model.Game
import com.opennext.app.ui.components.AdaptiveNavigation
import com.opennext.app.ui.components.EmptyState
import com.opennext.app.ui.components.GameCard
import com.opennext.app.ui.components.NavItem
import com.opennext.app.ui.theme.Blue40
import com.opennext.app.ui.theme.DarkBg
import com.opennext.app.ui.theme.DarkCard
import com.opennext.app.ui.theme.DarkSurface
import com.opennext.app.ui.theme.OnDark
import com.opennext.app.ui.theme.OnDarkVariant
import kotlinx.coroutines.launch

private enum class LibraryTab(val label: String) {
    ALL("All"),
    FAVORITES("Favorites"),
    RECENT("Recent"),
}

private enum class SortOption(val label: String) {
    NAME("Name"),
    RATING("Rating"),
    LAST_PLAYED("Last Played"),
}

private val navigationItems = listOf(
    NavItem("Home", Icons.Filled.Home, Icons.Outlined.Home),
    NavItem("Library", Icons.Filled.LibraryBooks, Icons.Outlined.LibraryBooks),
    NavItem("Settings", Icons.Filled.Settings, Icons.Outlined.Settings),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreen(
    onGameClick: (String) -> Unit,
    onBack: () -> Unit,
) {
    val configuration = LocalConfiguration.current
    val isTablet = configuration.screenWidthDp >= 600

    var selectedTab by remember { mutableIntStateOf(0) }
    var sortOption by remember { mutableStateOf(SortOption.NAME) }
    var selectedNav by remember { mutableIntStateOf(1) }
    var showSortMenu by remember { mutableStateOf(false) }

    val favoriteIds = remember {
        mutableStateListOf<String>().apply {
            addAll(mockGames.filter { it.isInLibrary && it.isFavorite }.map { it.id })
        }
    }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val libraryGames = remember(mockGames) {
        mockGames.filter { it.isInLibrary }
    }

    val filteredGames = remember(libraryGames, selectedTab, favoriteIds, sortOption) {
        val base = when (LibraryTab.entries[selectedTab]) {
            LibraryTab.ALL -> libraryGames
            LibraryTab.FAVORITES -> libraryGames.filter { it.id in favoriteIds }
            LibraryTab.RECENT -> libraryGames.filter { it.lastPlayed != null }
        }
        when (sortOption) {
            SortOption.NAME -> base.sortedBy { it.title.lowercase() }
            SortOption.RATING -> base.sortedByDescending { it.rating }
            SortOption.LAST_PLAYED -> base.sortedByDescending { it.lastPlayed }
        }
    }

    fun toggleFavorite(game: Game) {
        val isFav = game.id in favoriteIds
        if (isFav) favoriteIds.remove(game.id) else favoriteIds.add(game.id)
        val message = if (isFav) "Removed from favorites" else "Added to favorites"
        scope.launch {
            snackbarHostState.showSnackbar(message, duration = SnackbarDuration.Short)
        }
    }

    if (isTablet) {
        TabletLibraryContent(
            selectedTab = selectedTab,
            onTabSelected = { selectedTab = it },
            sortOption = sortOption,
            onSortSelected = { sortOption = it },
            showSortMenu = showSortMenu,
            onShowSortMenuChange = { showSortMenu = it },
            filteredGames = filteredGames,
            libraryGames = libraryGames,
            favoriteIds = favoriteIds,
            onGameClick = onGameClick,
            onFavoriteToggle = ::toggleFavorite,
            selectedNav = selectedNav,
            onNavSelected = { selectedNav = it },
            snackbarHostState = snackbarHostState,
        )
    } else {
        PhoneLibraryContent(
            selectedTab = selectedTab,
            onTabSelected = { selectedTab = it },
            sortOption = sortOption,
            onSortSelected = { sortOption = it },
            showSortMenu = showSortMenu,
            onShowSortMenuChange = { showSortMenu = it },
            filteredGames = filteredGames,
            libraryGames = libraryGames,
            favoriteIds = favoriteIds,
            onGameClick = onGameClick,
            onFavoriteToggle = ::toggleFavorite,
            onBack = onBack,
            selectedNav = selectedNav,
            onNavSelected = { selectedNav = it },
            snackbarHostState = snackbarHostState,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PhoneLibraryContent(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    sortOption: SortOption,
    onSortSelected: (SortOption) -> Unit,
    showSortMenu: Boolean,
    onShowSortMenuChange: (Boolean) -> Unit,
    filteredGames: List<Game>,
    libraryGames: List<Game>,
    favoriteIds: List<String>,
    onGameClick: (String) -> Unit,
    onFavoriteToggle: (Game) -> Unit,
    onBack: () -> Unit,
    selectedNav: Int,
    onNavSelected: (Int) -> Unit,
    snackbarHostState: SnackbarHostState,
) {
    Scaffold(
        containerColor = DarkBg,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Library",
                        fontWeight = FontWeight.Bold,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkBg,
                    titleContentColor = OnDark,
                    navigationIconContentColor = OnDark,
                ),
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            PhoneFilterChips(
                selectedTab = selectedTab,
                onTabSelected = onTabSelected,
                libraryGames = libraryGames,
                favoriteIds = favoriteIds,
            )

            PhoneSortBar(
                sortOption = sortOption,
                onSortSelected = onSortSelected,
                showSortMenu = showSortMenu,
                onShowSortMenuChange = onShowSortMenuChange,
                filteredCount = filteredGames.size,
            )

            if (filteredGames.isEmpty()) {
                val (icon, title, description) = when (LibraryTab.entries[selectedTab]) {
                    LibraryTab.ALL -> Triple(
                        Icons.Filled.LibraryBooks,
                        "No games in library",
                        "Browse the store to add games to your library.",
                    )
                    LibraryTab.FAVORITES -> Triple(
                        Icons.Outlined.FavoriteBorder,
                        "No favorites yet",
                        "Tap the heart icon on a game to add it to your favorites.",
                    )
                    LibraryTab.RECENT -> Triple(
                        Icons.Filled.Settings,
                        "No recently played games",
                        "Games you play will appear here.",
                    )
                }
                EmptyState(
                    icon = icon,
                    title = title,
                    description = description,
                )
            } else {
                PhoneGameGrid(
                    games = filteredGames,
                    favoriteIds = favoriteIds,
                    onGameClick = onGameClick,
                    onFavoriteToggle = onFavoriteToggle,
                )
            }
        }
    }
}

@Composable
private fun PhoneFilterChips(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    libraryGames: List<Game>,
    favoriteIds: List<String>,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        LibraryTab.entries.forEachIndexed { index, tab ->
            val count = when (tab) {
                LibraryTab.ALL -> libraryGames.size
                LibraryTab.FAVORITES -> libraryGames.count { it.id in favoriteIds }
                LibraryTab.RECENT -> libraryGames.count { it.lastPlayed != null }
            }
            FilterChip(
                selected = selectedTab == index,
                onClick = { onTabSelected(index) },
                label = {
                    Text(
                        text = "${tab.label} ($count)",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Medium,
                    )
                },
                shape = RoundedCornerShape(8.dp),
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Blue40.copy(alpha = 0.2f),
                    selectedLabelColor = Blue40,
                    containerColor = DarkCard,
                    labelColor = OnDarkVariant,
                ),
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PhoneSortBar(
    sortOption: SortOption,
    onSortSelected: (SortOption) -> Unit,
    showSortMenu: Boolean,
    onShowSortMenuChange: (Boolean) -> Unit,
    filteredCount: Int,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box {
            FilterChip(
                selected = true,
                onClick = { onShowSortMenuChange(!showSortMenu) },
                label = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Sort: ${sortOption.label}",
                            style = MaterialTheme.typography.labelMedium,
                        )
                        Icon(
                            imageVector = Icons.Filled.ArrowDropDown,
                            contentDescription = null,
                            modifier = Modifier.padding(start = 2.dp),
                        )
                    }
                },
                shape = RoundedCornerShape(8.dp),
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = DarkSurface,
                    selectedLabelColor = OnDark,
                ),
            )
            DropdownMenu(
                expanded = showSortMenu,
                onDismissRequest = { onShowSortMenuChange(false) },
            ) {
                SortOption.entries.forEach { option ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = option.label,
                                color = if (option == sortOption) Blue40 else OnDark,
                                fontWeight = if (option == sortOption) FontWeight.Bold else FontWeight.Normal,
                            )
                        },
                        onClick = {
                            onSortSelected(option)
                            onShowSortMenuChange(false)
                        },
                    )
                }
            }
        }
        Text(
            text = "$filteredCount games",
            style = MaterialTheme.typography.labelMedium,
            color = OnDarkVariant,
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun PhoneGameGrid(
    games: List<Game>,
    favoriteIds: List<String>,
    onGameClick: (String) -> Unit,
    onFavoriteToggle: (Game) -> Unit,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        itemsIndexed(games, key = { _, game -> game.id }) { index, game ->
            val isFav = game.id in favoriteIds
            AnimatedGameCard(
                game = game.copy(isFavorite = isFav),
                onClick = { onGameClick(game.id) },
                onFavoriteToggle = { onFavoriteToggle(game) },
                index = index,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TabletLibraryContent(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    sortOption: SortOption,
    onSortSelected: (SortOption) -> Unit,
    showSortMenu: Boolean,
    onShowSortMenuChange: (Boolean) -> Unit,
    filteredGames: List<Game>,
    libraryGames: List<Game>,
    favoriteIds: List<String>,
    onGameClick: (String) -> Unit,
    onFavoriteToggle: (Game) -> Unit,
    selectedNav: Int,
    onNavSelected: (Int) -> Unit,
    snackbarHostState: SnackbarHostState,
) {
    Row(modifier = Modifier.fillMaxSize()) {
        AdaptiveNavigation(
            items = navigationItems,
            selectedIndex = selectedNav,
            onItemSelected = onNavSelected,
            isTablet = true,
            avatarUrl = mockUser.avatarUrl,
        )

        Scaffold(
            containerColor = DarkBg,
            snackbarHost = { SnackbarHost(snackbarHostState) },
        ) { padding ->
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
            ) {
                TabletSidebar(
                    selectedTab = selectedTab,
                    onTabSelected = onTabSelected,
                    sortOption = sortOption,
                    onSortSelected = onSortSelected,
                    showSortMenu = showSortMenu,
                    onShowSortMenuChange = onShowSortMenuChange,
                    libraryGames = libraryGames,
                    favoriteIds = favoriteIds,
                    filteredCount = filteredGames.size,
                )

                if (filteredGames.isEmpty()) {
                    val (icon, title, description) = when (LibraryTab.entries[selectedTab]) {
                        LibraryTab.ALL -> Triple(
                            Icons.Filled.LibraryBooks,
                            "No games in library",
                            "Browse the store to add games to your library.",
                        )
                        LibraryTab.FAVORITES -> Triple(
                            Icons.Outlined.FavoriteBorder,
                            "No favorites yet",
                            "Tap the heart icon on a game to add it to your favorites.",
                        )
                        LibraryTab.RECENT -> Triple(
                            Icons.Filled.Settings,
                            "No recently played games",
                            "Games you play will appear here.",
                        )
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize(),
                    ) {
                        EmptyState(
                            icon = icon,
                            title = title,
                            description = description,
                        )
                    }
                } else {
                    TabletGameGrid(
                        games = filteredGames,
                        favoriteIds = favoriteIds,
                        onGameClick = onGameClick,
                        onFavoriteToggle = onFavoriteToggle,
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TabletSidebar(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    sortOption: SortOption,
    onSortSelected: (SortOption) -> Unit,
    showSortMenu: Boolean,
    onShowSortMenuChange: (Boolean) -> Unit,
    libraryGames: List<Game>,
    favoriteIds: List<String>,
    filteredCount: Int,
) {
    Column(
        modifier = Modifier
            .width(260.dp)
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = "Filters",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = OnDark,
        )

        LibraryTab.entries.forEachIndexed { index, tab ->
            val count = when (tab) {
                LibraryTab.ALL -> libraryGames.size
                LibraryTab.FAVORITES -> libraryGames.count { it.id in favoriteIds }
                LibraryTab.RECENT -> libraryGames.count { it.lastPlayed != null }
            }
            FilterChip(
                selected = selectedTab == index,
                onClick = { onTabSelected(index) },
                label = {
                    Text(
                        text = "${tab.label} ($count)",
                        style = MaterialTheme.typography.labelLarge,
                    )
                },
                shape = RoundedCornerShape(8.dp),
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Blue40.copy(alpha = 0.15f),
                    selectedLabelColor = Blue40,
                    containerColor = DarkCard,
                    labelColor = OnDarkVariant,
                ),
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Sort by",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
            color = OnDark,
        )

        Box {
            FilterChip(
                selected = true,
                onClick = { onShowSortMenuChange(!showSortMenu) },
                label = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = sortOption.label,
                            style = MaterialTheme.typography.labelLarge,
                        )
                        Icon(
                            imageVector = Icons.Filled.ArrowDropDown,
                            contentDescription = null,
                            modifier = Modifier.padding(start = 4.dp),
                        )
                    }
                },
                shape = RoundedCornerShape(8.dp),
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = DarkCard,
                    selectedLabelColor = OnDark,
                ),
            )
            DropdownMenu(
                expanded = showSortMenu,
                onDismissRequest = { onShowSortMenuChange(false) },
            ) {
                SortOption.entries.forEach { option ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = option.label,
                                color = if (option == sortOption) Blue40 else OnDark,
                                fontWeight = if (option == sortOption) FontWeight.Bold else FontWeight.Normal,
                            )
                        },
                        onClick = {
                            onSortSelected(option)
                            onShowSortMenuChange(false)
                        },
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Stats",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
            color = OnDark,
        )

        SidebarStat(label = "Total games", value = libraryGames.size.toString())
        SidebarStat(label = "Favorites", value = libraryGames.count { it.id in favoriteIds }.toString())
        SidebarStat(label = "Recently played", value = libraryGames.count { it.lastPlayed != null }.toString())
        SidebarStat(label = "Showing", value = filteredCount.toString())
    }
}

@Composable
private fun SidebarStat(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = OnDarkVariant,
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = OnDark,
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun TabletGameGrid(
    games: List<Game>,
    favoriteIds: List<String>,
    onGameClick: (String) -> Unit,
    onFavoriteToggle: (Game) -> Unit,
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 180.dp),
        modifier = Modifier
            .weight(1f)
            .fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        itemsIndexed(games, key = { _, game -> game.id }) { index, game ->
            val isFav = game.id in favoriteIds
            AnimatedGameCard(
                game = game.copy(isFavorite = isFav),
                onClick = { onGameClick(game.id) },
                onFavoriteToggle = { onFavoriteToggle(game) },
                index = index,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AnimatedGameCard(
    game: Game,
    onClick: () -> Unit,
    onFavoriteToggle: () -> Unit,
    index: Int,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .animateItem()
    ) {
        AnimatedVisibility(
            visible = true,
            enter = fadeIn(animationSpec = tween(300, delayMillis = index * 60)) +
                    slideInVertically(
                        animationSpec = tween(300, delayMillis = index * 60)
                    ) { it / 3 },
            exit = fadeOut(),
        ) {
            GameCard(
                game = game,
                onClick = onClick,
                onFavoriteToggle = onFavoriteToggle,
            )
        }
    }
}