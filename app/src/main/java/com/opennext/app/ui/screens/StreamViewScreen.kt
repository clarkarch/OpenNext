package com.opennext.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Settings
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.opennext.app.ui.theme.DarkBg
import com.opennext.app.ui.theme.OnDarkVariant

@Composable
fun StreamViewScreen(
    gameId: String,
    onExit: () -> Unit,
) {
    val displayName = gameId.replace("-", " ").replaceFirstChar { it.uppercase() }
    var micEnabled by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
    ) {
        // Placeholder for WebRTC video stream
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(DarkBg),
            contentAlignment = Alignment.Center,
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Stream View",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = displayName,
                    style = MaterialTheme.typography.bodyLarge,
                    color = OnDarkVariant,
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "WebRTC stream will render here",
                    style = MaterialTheme.typography.bodySmall,
                    color = OnDarkVariant,
                )
            }
        }

        // Top overlay bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black.copy(alpha = 0.6f))
                .padding(horizontal = 8.dp, vertical = 8.dp)
                .padding(top = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = onExit) {
                Icon(
                    Icons.Filled.Close,
                    contentDescription = "Exit",
                    tint = Color.White,
                )
            }

            Text(
                text = displayName,
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                modifier = Modifier.weight(1f),
            )

            Text(
                text = "00:00",
                style = MaterialTheme.typography.bodyMedium,
                color = OnDarkVariant,
            )

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(onClick = { }) {
                Icon(
                    Icons.Filled.Settings,
                    contentDescription = "Settings",
                    tint = Color.White,
                )
            }

            IconButton(onClick = { micEnabled = !micEnabled }) {
                Icon(
                    if (micEnabled) Icons.Filled.Mic else Icons.Filled.MicOff,
                    contentDescription = "Microphone",
                    tint = if (micEnabled) Color.White else OnDarkVariant,
                )
            }

            IconButton(onClick = { }) {
                Icon(
                    Icons.Filled.PhotoCamera,
                    contentDescription = "Screenshot",
                    tint = Color.White,
                )
            }
        }

        // Stats overlay placeholder (bottom-left)
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
                .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(8.dp))
                .padding(12.dp),
        ) {
            StatsRow("Resolution", "1920x1080")
            StatsRow("FPS", "60")
            StatsRow("Bitrate", "50 Mbps")
            StatsRow("RTT", "32ms")
        }
    }
}

@Composable
private fun StatsRow(label: String, value: String) {
    Row(
        modifier = Modifier.padding(vertical = 1.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = "$label:",
            style = MaterialTheme.typography.labelSmall,
            color = OnDarkVariant,
        )
        Text(
            text = value,
            style = MaterialTheme.typography.labelSmall,
            color = Color.White,
        )
    }
}
