package com.opennext.app.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.opennext.app.data.model.Game
import com.opennext.app.data.model.MembershipTier
import com.opennext.app.ui.theme.DarkCard
import com.opennext.app.ui.theme.Error
import com.opennext.app.ui.theme.GlowCyan
import com.opennext.app.ui.theme.OnDark
import com.opennext.app.ui.theme.OnDarkVariant
import com.opennext.app.ui.theme.PlayerGreen
import com.opennext.app.ui.theme.glowShadow
import com.opennext.app.ui.theme.tierGlowColor
import kotlin.math.abs

@Composable
fun GameCard(
    game: Game,
    onClick: () -> Unit,
    onFavoriteToggle: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.96f else 1f,
        label = "cardScale",
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .glowShadow(GlowCyan, blur = 8.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable(
                onClick = {
                    isPressed = true
                    onClick()
                },
            )
            .padding(
                start = 1.dp,
                top = 1.dp,
                end = 1.dp,
                bottom = 1.dp,
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = DarkCard),
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f),
            ) {
                AsyncImage(
                    model = game.imageUrl,
                    contentDescription = game.title,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
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
                                    DarkCard.copy(alpha = 0.85f),
                                ),
                                startY = 0f,
                                endY = Float.POSITIVE_INFINITY,
                            ),
                        ),
                )
                TierBadgeGlow(
                    tier = game.membershipTier,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp),
                )
                if (onFavoriteToggle != null) {
                    FavoriteButton(
                        isFavorite = game.isFavorite,
                        onClick = onFavoriteToggle,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(4.dp),
                    )
                }
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .fillMaxWidth()
                        .padding(12.dp),
                ) {
                    Text(
                        text = game.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = OnDark,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = game.publisher,
                        style = MaterialTheme.typography.bodySmall,
                        color = OnDarkVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        RatingDisplay(rating = game.rating)
                        if (game.lastPlayed != null) {
                            LastPlayedChip(lastPlayed = game.lastPlayed)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TierBadgeGlow(
    tier: MembershipTier,
    modifier: Modifier = Modifier,
) {
    val color = tierGlowColor(tier)
    Box(
        modifier = modifier
            .glowShadow(color, blur = 8.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(color.copy(alpha = 0.9f))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = tier.name,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = if (tier == MembershipTier.FREE) OnDark else Color.Black,
        )
    }
}

@Composable
fun FavoriteButton(
    isFavorite: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    IconButton(
        onClick = onClick,
        modifier = modifier.size(36.dp),
    ) {
        Icon(
            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
            contentDescription = if (isFavorite) "Remove from favorites" else "Add to favorites",
            tint = if (isFavorite) Error else Color.White.copy(alpha = 0.8f),
            modifier = Modifier.glowShadow(
                color = if (isFavorite) Error else Color.Transparent,
                blur = if (isFavorite) 8.dp else 0.dp,
            ),
        )
    }
}

@Composable
fun RatingDisplay(
    rating: Float,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier) {
        repeat(5) { index ->
            val filled = rating >= index + 1
            val halfFilled = !filled && rating >= index + 0.5f
            Icon(
                imageVector = when {
                    filled -> Icons.Filled.Star
                    halfFilled -> Icons.Filled.Star
                    else -> Icons.Outlined.StarOutline
                },
                contentDescription = null,
                tint = when {
                    filled || halfFilled -> PlayerGreen
                    else -> OnDarkVariant.copy(alpha = 0.4f)
                },
                modifier = Modifier.size(14.dp),
            )
        }
    }
}

@Composable
fun LastPlayedChip(
    lastPlayed: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = "Last played $lastPlayed",
        style = MaterialTheme.typography.labelSmall,
        color = OnDarkVariant,
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .background(OnDarkVariant.copy(alpha = 0.15f))
            .padding(horizontal = 6.dp, vertical = 2.dp),
    )
}