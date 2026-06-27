package com.opennext.app.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import com.opennext.app.ui.theme.DarkBg
import com.opennext.app.ui.theme.DarkCard
import com.opennext.app.ui.theme.GlowCyan
import com.opennext.app.ui.theme.OnDark
import com.opennext.app.ui.theme.OnDarkVariant
import com.opennext.app.ui.theme.PlayerGreen
import com.opennext.app.ui.theme.PlayerGreenGlow
import com.opennext.app.ui.theme.glowShadow

@Composable
fun HeroGameCard(
    game: Game,
    onPlay: (String) -> Unit,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    pageCount: Int = 1,
    currentPage: Int = 0,
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1f,
        label = "heroScale",
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .glowShadow(
                color = if (isSelected) GlowCyan else Color.Transparent,
                blur = if (isSelected) 16.dp else 4.dp,
            )
            .clip(RoundedCornerShape(16.dp))
            .clickable {
                isPressed = true
                onPlay(game.id)
            },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = DarkCard),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp),
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
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Transparent,
                                DarkBg.copy(alpha = 0.5f),
                                DarkBg.copy(alpha = 0.9f),
                            ),
                            startY = 0f,
                            endY = Float.POSITIVE_INFINITY,
                        ),
                    ),
            )
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {
                Text(
                    text = game.title,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = OnDark,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(
                        text = game.publisher,
                        style = MaterialTheme.typography.bodyMedium,
                        color = OnDarkVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    TierBadgeGlow(
                        tier = game.membershipTier,
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                PlayButton(
                    onClick = { onPlay(game.id) },
                )
                if (pageCount > 1) {
                    Spacer(modifier = Modifier.height(12.dp))
                    PageIndicatorDots(
                        pageCount = pageCount,
                        currentPage = currentPage,
                    )
                }
            }
        }
    }
}

@Composable
fun PlayButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .glowShadow(PlayerGreen, blur = 10.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(PlayerGreen)
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Filled.PlayArrow,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(20.dp),
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = "PLAY",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = Color.White,
        )
    }
}

@Composable
fun PageIndicatorDots(
    pageCount: Int,
    currentPage: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        repeat(pageCount) { index ->
            val isActive = index == currentPage
            Box(
                modifier = Modifier
                    .size(if (isActive) 10.dp else 8.dp)
                    .clip(CircleShape)
                    .background(
                        if (isActive) PlayerGreen
                        else OnDarkVariant.copy(alpha = 0.4f),
                    )
                    .glowShadow(
                        color = if (isActive) PlayerGreen else Color.Transparent,
                        blur = if (isActive) 6.dp else 0.dp,
                    ),
            )
        }
    }
}