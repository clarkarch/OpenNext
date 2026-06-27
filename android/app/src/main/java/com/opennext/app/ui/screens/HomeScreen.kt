package com.opennext.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryBooks
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LibraryBooks
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.opennext.app.ui.theme.Blue40
import com.opennext.app.ui.theme.DarkBg
import com.opennext.app.ui.theme.DarkCard
import com.opennext.app.ui.theme.DarkSurface
import com.opennext.app.ui.theme.OnDarkVariant

private data class NavItem(
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
)

private val navItems = listOf(
    NavItem("Home", Icons.Filled.Home, Icons.Outlined.Home),
    NavItem("Library", Icons.Filled.LibraryBooks, Icons.Outlined.LibraryBooks),
    NavItem("Search", Icons.Filled.Search, Icons.Filled.Search),
    NavItem("Settings", Icons.Filled.Settings, Icons.Outlined.Settings),
)

private val mockGames = listOf(
    "Cyberpunk 2077" to "https://cdn.cloudflare.steamstatic.com/steam/apps/1091500/header.jpg",
    "Baldur's Gate 3" to "https://cdn.cloudflare.steamstatic.com/steam/apps/1086940/header.jpg",
    "The Witcher 3" to "https://cdn.cloudflare.steamstatic.com/steam/apps/292030/header.jpg",
    "Elden Ring" to "https://cdn.cloudflare.steamstatic.com/steam/apps/1245620/header.jpg",
    "Red Dead Redemption 2" to "https://cdn.cloudflare.steamstatic.com/steam/apps/1174180/header.jpg",
    "Hogwarts Legacy" to "https://cdn.cloudflare.steamstatic.com/steam/apps/990080/header.jpg",
)

private val sections = listOf(
    "Continue Playing",
    "Trending Now",
    "New Releases",
    "Action Games",
    "RPG Games",
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

    Scaffold(
        containerColor = DarkBg,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .padding(top = 32.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                TextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    placeholder = {
                        Text("Search games...", color = OnDarkVariant)
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = DarkSurface,
                        unfocusedContainerColor = DarkSurface,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = Blue40,
                    ),
                    singleLine = true,
                )
            }
        },
        bottomBar = {
            NavigationBar(
                containerColor = DarkSurface,
                contentColor = Color.White,
            ) {
                navItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedNav == index,
                        onClick = {
                            selectedNav = index
                            when (index) {
                                1 -> onLibraryClick()
                                3 -> onSettingsClick()
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = if (selectedNav == index) item.selectedIcon else item.unselectedIcon,
                                contentDescription = item.label,
                            )
                        },
                        label = { Text(item.label) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Blue40,
                            selectedTextColor = Blue40,
                            unselectedIconColor = OnDarkVariant,
                            unselectedTextColor = OnDarkVariant,
                            indicatorColor = Blue40.copy(alpha = 0.12f),
                        ),
                    )
                }
            }
        },
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(bottom = 16.dp),
        ) {
            item {
                HeroBanner()
            }

            sections.forEach { section ->
                item {
                    SectionHeader(title = section)
                }
                item {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        items(mockGames) { (title, imageUrl) ->
                            GameCard(
                                title = title,
                                imageUrl = imageUrl,
                                onClick = { onGameClick(title.lowercase().replace(" ", "-")) },
                            )
                        }
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
private fun HeroBanner() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .height(200.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = DarkCard),
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = "https://cdn.cloudflare.steamstatic.com/steam/apps/1091500/header.jpg",
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, DarkBg.copy(alpha = 0.9f)),
                        ),
                    ),
            )
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(20.dp),
            ) {
                Text(
                    text = "Cyberpunk 2077",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Open world action RPG",
                    style = MaterialTheme.typography.bodyMedium,
                    color = OnDarkVariant,
                )
            }
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = Color.White,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
    )
}

@Composable
private fun GameCard(
    title: String,
    imageUrl: String,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .width(160.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = DarkCard),
    ) {
        Column {
            AsyncImage(
                model = imageUrl,
                contentDescription = title,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
                    .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)),
                contentScale = ContentScale.Crop,
            )
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = Color.White,
                    maxLines = 1,
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "GeForce NOW",
                    style = MaterialTheme.typography.labelSmall,
                    color = OnDarkVariant,
                )
            }
        }
    }
}
