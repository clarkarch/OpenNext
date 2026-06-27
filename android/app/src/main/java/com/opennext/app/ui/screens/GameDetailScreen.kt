package com.opennext.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.opennext.app.ui.theme.Blue40
import com.opennext.app.ui.theme.DarkBg
import com.opennext.app.ui.theme.DarkCard
import com.opennext.app.ui.theme.OnDarkVariant
import com.opennext.app.ui.theme.PlayerGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameDetailScreen(
    gameId: String,
    onPlay: (String) -> Unit,
    onBack: () -> Unit,
) {
    val displayName = gameId.replace("-", " ").replaceFirstChar { it.uppercase() }

    Scaffold(
        containerColor = DarkBg,
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    navigationIconContentColor = Color.White,
                ),
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState()),
        ) {
            Box(modifier = Modifier.height(240.dp)) {
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
                                colors = listOf(Color.Transparent, DarkBg),
                            ),
                        ),
                )
            }

            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Text(
                    text = displayName,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row {
                    MetadataChip("RPG")
                    Spacer(modifier = Modifier.width(8.dp))
                    MetadataChip("Open World")
                    Spacer(modifier = Modifier.width(8.dp))
                    MetadataChip("85h played")
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { onPlay(gameId) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Blue40,
                        contentColor = Color.White,
                    ),
                    shape = RoundedCornerShape(16.dp),
                ) {
                    Text(
                        text = "Play on GeForce NOW",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "About",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Explore a vast open world in this action RPG. Make choices that shape the story and engage in intense combat in a cyberpunk future.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = OnDarkVariant,
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Available On",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row {
                    StoreBadge("Steam")
                    Spacer(modifier = Modifier.width(8.dp))
                    StoreBadge("Epic Games")
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
private fun MetadataChip(label: String) {
    Text(
        text = label,
        style = MaterialTheme.typography.labelMedium,
        color = OnDarkVariant,
        modifier = Modifier
            .background(DarkCard, RoundedCornerShape(8.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp),
    )
}

@Composable
private fun StoreBadge(name: String) {
    Text(
        text = name,
        style = MaterialTheme.typography.labelMedium,
        color = Color.White,
        modifier = Modifier
            .background(DarkCard, RoundedCornerShape(8.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp),
    )
}
