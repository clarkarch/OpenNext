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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.opennext.app.ui.theme.Blue40
import com.opennext.app.ui.theme.DarkBg
import com.opennext.app.ui.theme.OnDarkVariant
import com.opennext.app.ui.theme.PlayerGreen

@Composable
fun StreamLoadingScreen(
    gameId: String,
    onCancel: () -> Unit,
    onReady: (String) -> Unit,
) {
    val displayName = gameId.replace("-", " ").replaceFirstChar { it.uppercase() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBg),
    ) {
        Box(
            modifier = Modifier
                .size(400.dp)
                .align(Alignment.Center)
                .blur(150.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(Blue40.copy(alpha = 0.1f), Color.Transparent),
                    ),
                ),
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            AsyncImage(
                model = "https://cdn.cloudflare.steamstatic.com/steam/apps/1091500/header.jpg",
                contentDescription = displayName,
                modifier = Modifier
                    .size(120.dp)
                    .background(DarkBg, RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop,
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = displayName,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color.White,
            )

            Spacer(modifier = Modifier.height(32.dp))

            StepIndicator(
                steps = listOf("Queue", "Setup", "Ready"),
                currentStep = 0,
            )

            Spacer(modifier = Modifier.height(24.dp))

            CircularProgressIndicator(
                color = Blue40,
                modifier = Modifier.size(32.dp),
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Connecting to server...",
                style = MaterialTheme.typography.bodyMedium,
                color = OnDarkVariant,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Position in queue: #3",
                style = MaterialTheme.typography.bodySmall,
                color = OnDarkVariant,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(48.dp))

            Button(
                onClick = onCancel,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = OnDarkVariant,
                ),
                shape = RoundedCornerShape(12.dp),
            ) {
                Text(text = "Cancel")
            }
        }
    }
}

@Composable
private fun StepIndicator(
    steps: List<String>,
    currentStep: Int,
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth(),
    ) {
        steps.forEachIndexed { index, step ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .background(
                            color = if (index <= currentStep) Blue40 else OnDarkVariant.copy(alpha = 0.3f),
                            shape = CircleShape,
                        ),
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = step,
                    style = MaterialTheme.typography.labelSmall,
                    color = if (index <= currentStep) Color.White else OnDarkVariant,
                )
            }
            if (index < steps.lastIndex) {
                Box(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .padding(top = 5.dp)
                        .height(2.dp)
                        .width(40.dp)
                        .background(
                            if (index < currentStep) Blue40 else OnDarkVariant.copy(alpha = 0.3f),
                        ),
                )
            }
        }
    }
}
