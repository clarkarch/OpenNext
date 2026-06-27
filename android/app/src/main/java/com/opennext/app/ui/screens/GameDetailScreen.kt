package com.opennext.app.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.opennext.app.data.mockGames
import com.opennext.app.data.model.Game
import com.opennext.app.ui.components.TierBadgeBanner
import com.opennext.app.ui.theme.DarkBg
import com.opennext.app.ui.theme.DarkCard
import com.opennext.app.ui.theme.DarkSurface
import com.opennext.app.ui.theme.Error
import com.opennext.app.ui.theme.OnDark
import com.opennext.app.ui.theme.OnDarkVariant
import com.opennext.app.ui.theme.PlayerGreen
import kotlinx.coroutines.launch

@Composable
fun GameDetailScreen(
    gameId: String,
    onPlay: (String) -> Unit,
    onBack: () -> Unit,
) {
    val game: Game? = remember(gameId) {
        mockGames.find {
            it.id == gameId || it.title.lowercase().replace(" ", "-") == gameId
        }
    }

    if (game == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(DarkBg),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "Game not found",
                style = MaterialTheme.typography.titleLarge,
                color = OnDarkVariant,
                textAlign = TextAlign.Center,
            )
        }
        return
    }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var isFavorite by remember { mutableStateOf(game.isFavorite) }
    var isInLibrary by remember { mutableStateOf(game.isInLibrary) }

    val configuration = LocalConfiguration.current
    val isTablet = configuration.screenWidthDp >= 600

    Scaffold(
        containerColor = DarkBg,
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { padding ->
        if (isTablet) {
            TabletGameDetail(
                game = game,
                isFavorite = isFavorite,
                isInLibrary = isInLibrary,
                onBack = onBack,
                onPlay = {
                    Toast.makeText(context, "Starting stream...", Toast.LENGTH_SHORT).show()
                    onPlay(game.id)
                },
                onToggleFavorite = {
                    isFavorite = !isFavorite
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            if (isFavorite) "${game.title} added to favorites"
                            else "${game.title} removed from favorites",
                        )
                    }
                },
                onToggleLibrary = {
                    isInLibrary = !isInLibrary
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            if (isInLibrary) "${game.title} added to library"
                            else "${game.title} removed from library",
                        )
                    }
                },
                modifier = Modifier.padding(padding),
            )
        } else {
            PhoneGameDetail(
                game = game,
                isFavorite = isFavorite,
                isInLibrary = isInLibrary,
                onBack = onBack,
                onPlay = {
                    Toast.makeText(context, "Starting stream...", Toast.LENGTH_SHORT).show()
                    onPlay(game.id)
                },
                onToggleFavorite = {
                    isFavorite = !isFavorite
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            if (isFavorite) "${game.title} added to favorites"
                            else "${game.title} removed from favorites",
                        )
                    }
                },
                onToggleLibrary = {
                    isInLibrary = !isInLibrary
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            if (isInLibrary) "${game.title} added to library"
                            else "${game.title} removed from library",
                        )
                    }
                },
                modifier = Modifier.padding(padding),
            )
        }
    }
}

@Composable
private fun PhoneGameDetail(
    game: Game,
    isFavorite: Boolean,
    isInLibrary: Boolean,
    onBack: () -> Unit,
    onPlay: () -> Unit,
    onToggleFavorite: () -> Unit,
    onToggleLibrary: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        ) {
            HeroImage(
                game = game,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
            )

            ContentBody(
                game = game,
                isFavorite = isFavorite,
                isInLibrary = isInLibrary,
                onPlay = onPlay,
                onToggleFavorite = onToggleFavorite,
                onToggleLibrary = onToggleLibrary,
            )
        }

        BackButton(
            onClick = onBack,
            modifier = Modifier
                .padding(12.dp)
                .align(Alignment.TopStart),
        )
    }
}

@Composable
private fun TabletGameDetail(
    game: Game,
    isFavorite: Boolean,
    isInLibrary: Boolean,
    onBack: () -> Unit,
    onPlay: () -> Unit,
    onToggleFavorite: () -> Unit,
    onToggleLibrary: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize(),
        ) {
            AsyncImage(
                model = game.heroImageUrl,
                contentDescription = game.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                Color.Transparent,
                                DarkBg.copy(alpha = 0.3f),
                                DarkBg.copy(alpha = 0.8f),
                                DarkBg,
                            ),
                        ),
                    ),
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 32.dp, vertical = 16.dp),
        ) {
            BackButton(
                onClick = onBack,
                modifier = Modifier.padding(bottom = 8.dp),
            )

            TabletInfoPanel(
                game = game,
                isFavorite = isFavorite,
                isInLibrary = isInLibrary,
                onPlay = onPlay,
                onToggleFavorite = onToggleFavorite,
                onToggleLibrary = onToggleLibrary,
            )
        }
    }
}

@Composable
private fun BackButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    IconButton(onClick = onClick, modifier = modifier) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(DarkSurface.copy(alpha = 0.8f), CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = OnDark,
            )
        }
    }
}

@Composable
private fun HeroImage(game: Game, modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        AsyncImage(
            model = game.heroImageUrl,
            contentDescription = game.title,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Transparent,
                            DarkBg.copy(alpha = 0.6f),
                            DarkBg,
                        ),
                    ),
                ),
        )
    }
}

@Composable
private fun ContentBody(
    game: Game,
    isFavorite: Boolean,
    isInLibrary: Boolean,
    onPlay: () -> Unit,
    onToggleFavorite: () -> Unit,
    onToggleLibrary: () -> Unit,
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            text = game.title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = OnDark,
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = game.publisher,
            style = MaterialTheme.typography.bodyMedium,
            color = OnDarkVariant,
        )
        Spacer(modifier = Modifier.height(10.dp))
        TierBadgeBanner(tier = game.membershipTier)
        Spacer(modifier = Modifier.height(20.dp))
        ActionButtonsRow(
            isFavorite = isFavorite,
            isInLibrary = isInLibrary,
            onPlay = onPlay,
            onToggleFavorite = onToggleFavorite,
            onToggleLibrary = onToggleLibrary,
        )
        Spacer(modifier = Modifier.height(28.dp))
        SectionTitle(text = "About")
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = game.description,
            style = MaterialTheme.typography.bodyMedium,
            color = OnDarkVariant,
        )
        Spacer(modifier = Modifier.height(24.dp))
        SectionTitle(text = "Screenshots")
        Spacer(modifier = Modifier.height(10.dp))
        ScreenshotsRow(imageUrl = game.imageUrl)
        Spacer(modifier = Modifier.height(24.dp))
        SectionTitle(text = "Genres")
        Spacer(modifier = Modifier.height(10.dp))
        GenreChipRow(genres = game.genres)
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun TabletInfoPanel(
    game: Game,
    isFavorite: Boolean,
    isInLibrary: Boolean,
    onPlay: () -> Unit,
    onToggleFavorite: () -> Unit,
    onToggleLibrary: () -> Unit,
) {
    Column {
        Text(
            text = game.title,
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = OnDark,
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = game.publisher,
            style = MaterialTheme.typography.bodyLarge,
            color = OnDarkVariant,
        )
        Spacer(modifier = Modifier.height(12.dp))
        TierBadgeBanner(tier = game.membershipTier)
        Spacer(modifier = Modifier.height(24.dp))
        ActionButtonsRow(
            isFavorite = isFavorite,
            isInLibrary = isInLibrary,
            onPlay = onPlay,
            onToggleFavorite = onToggleFavorite,
            onToggleLibrary = onToggleLibrary,
        )
        Spacer(modifier = Modifier.height(32.dp))
        SectionTitle(text = "About")
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = game.description,
            style = MaterialTheme.typography.bodyLarge,
            color = OnDarkVariant,
        )
        Spacer(modifier = Modifier.height(28.dp))
        SectionTitle(text = "Screenshots")
        Spacer(modifier = Modifier.height(12.dp))
        ScreenshotsRow(imageUrl = game.imageUrl)
        Spacer(modifier = Modifier.height(28.dp))
        SectionTitle(text = "Genres")
        Spacer(modifier = Modifier.height(12.dp))
        GenreChipRow(genres = game.genres)
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun ActionButtonsRow(
    isFavorite: Boolean,
    isInLibrary: Boolean,
    onPlay: () -> Unit,
    onToggleFavorite: () -> Unit,
    onToggleLibrary: () -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Button(
            onClick = onPlay,
            modifier = Modifier
                .weight(1f)
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = PlayerGreen,
                contentColor = Color.White,
            ),
            shape = RoundedCornerShape(24.dp),
        ) {
            Icon(
                imageVector = Icons.Filled.PlayArrow,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "Play",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
        }

        OutlinedButton(
            onClick = onToggleLibrary,
            modifier = Modifier.height(48.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = if (isInLibrary) PlayerGreen else OnDark,
            ),
            shape = RoundedCornerShape(24.dp),
        ) {
            Text(
                text = if (isInLibrary) "In Library" else "Add to Library",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
            )
        }

        IconButton(
            onClick = onToggleFavorite,
            modifier = Modifier
                .size(48.dp)
                .background(DarkCard, CircleShape),
        ) {
            Icon(
                imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                contentDescription = if (isFavorite) "Remove from favorites" else "Add to favorites",
                tint = if (isFavorite) Error else OnDarkVariant,
                modifier = Modifier.size(22.dp),
            )
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = OnDark,
    )
}

@Composable
private fun ScreenshotsRow(imageUrl: String) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.horizontalScroll(rememberScrollState()),
    ) {
        repeat(4) { index ->
            Box(
                modifier = Modifier
                    .width(200.dp)
                    .aspectRatio(16f / 9f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(DarkCard),
            ) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = "Screenshot ${index + 1}",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                )
            }
        }
    }
}

@Composable
private fun GenreChipRow(genres: List<String>) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.horizontalScroll(rememberScrollState()),
    ) {
        genres.forEach { genre ->
            FilterChip(
                selected = false,
                onClick = {},
                label = {
                    Text(
                        text = genre,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Medium,
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = DarkCard,
                    labelColor = OnDarkVariant,
                ),
                border = FilterChipDefaults.filterChipBorder(
                    borderColor = OnDarkVariant.copy(alpha = 0.2f),
                    enabled = true,
                    selected = false,
                ),
                shape = RoundedCornerShape(20.dp),
            )
        }
    }
}
