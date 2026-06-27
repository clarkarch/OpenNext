package com.opennext.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.opennext.app.ui.theme.Blue40
import com.opennext.app.ui.theme.DarkBg
import com.opennext.app.ui.theme.OnDark
import com.opennext.app.ui.theme.OnDarkVariant

@Composable
fun LoginScreen(
    onSignIn: () -> Unit,
) {
    var isLoading by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBg),
    ) {
        Box(
            modifier = Modifier
                .size(300.dp)
                .align(Alignment.TopCenter)
                .blur(120.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(Blue40.copy(alpha = 0.15f), Color.Transparent),
                    ),
                ),
        )

        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxHeight()
                .widthIn(max = 480.dp)
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Icon(
                imageVector = Icons.Filled.PlayArrow,
                contentDescription = "OpenNOW logo",
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(Blue40.copy(alpha = 0.15f))
                    .padding(16.dp),
                tint = Blue40,
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "OpenNOW",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = OnDark,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "GeForce NOW Client",
                style = MaterialTheme.typography.bodyMedium,
                color = OnDarkVariant,
            )

            Spacer(modifier = Modifier.height(64.dp))

            Button(
                onClick = {
                    isLoading = true
                    onSignIn()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Blue40,
                    contentColor = Color.White,
                ),
                shape = RoundedCornerShape(16.dp),
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White,
                        strokeWidth = 2.dp,
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                }
                Text(
                    text = if (isLoading) "Signing in..." else "Sign In",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "You will be redirected to NVIDIA to authenticate",
                style = MaterialTheme.typography.bodySmall,
                color = OnDarkVariant,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(48.dp))

            ProviderChipsRow()
        }
    }
}

@Composable
private fun ProviderChipsRow() {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ProviderChip(label = "NVIDIA")
        Spacer(modifier = Modifier.width(12.dp))
        ProviderChip(label = "Steam")
    }
}

@Composable
private fun ProviderChip(label: String) {
    OutlinedButton(
        onClick = {},
        enabled = false,
        modifier = Modifier.height(36.dp),
        shape = RoundedCornerShape(18.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            disabledContentColor = OnDarkVariant.copy(alpha = 0.5f),
        ),
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun LoginScreenPreview() {
    LoginScreen(onSignIn = {})
}
