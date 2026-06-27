package com.opennext.app.ui.screens

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.opennext.app.data.mockUser
import com.opennext.app.ui.components.SettingsRowClickable
import com.opennext.app.ui.components.SettingsRowSlider
import com.opennext.app.ui.theme.Blue40
import com.opennext.app.ui.theme.DarkBg
import com.opennext.app.ui.theme.DarkCard
import com.opennext.app.ui.theme.Error
import com.opennext.app.ui.theme.OnDark
import com.opennext.app.ui.theme.OnDarkMuted
import com.opennext.app.ui.theme.OnDarkVariant
import com.opennext.app.ui.theme.TierUltimate

private val RESOLUTION_OPTIONS = listOf("Auto", "1080p", "720p", "480p")
private val FPS_OPTIONS = listOf("Auto", "60", "30")
private val CODEC_OPTIONS = listOf("Auto", "H.264", "HEVC", "AV1")
private const val BITRATE_MIN = 10f
private const val BITRATE_MAX = 50f

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    onSignOut: () -> Unit = {},
) {
    var showSignOutDialog by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = DarkBg,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Settings",
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
    ) { padding ->
        val screenWidth = LocalConfiguration.current.screenWidthDp
        val isTablet = screenWidth >= 600

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            if (isTablet) {
                TabletSettingsContent(
                    onSignOutClick = { showSignOutDialog = true },
                )
            } else {
                PhoneSettingsContent(
                    onSignOutClick = { showSignOutDialog = true },
                )
            }
        }
    }

    if (showSignOutDialog) {
        SignOutDialog(
            onConfirm = {
                showSignOutDialog = false
                onSignOut()
            },
            onDismiss = { showSignOutDialog = false },
        )
    }
}

@Composable
private fun PhoneSettingsContent(
    onSignOutClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
    ) {
        AccountSection()
        Spacer(modifier = Modifier.height(24.dp))
        StreamingSection()
        Spacer(modifier = Modifier.height(24.dp))
        AboutSection(onSignOutClick = onSignOutClick)
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun TabletSettingsContent(
    onSignOutClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            SettingsCard(
                title = "Account",
                modifier = Modifier.weight(1f),
            ) {
                AccountCardContent()
            }
            SettingsCard(
                title = "Streaming",
                modifier = Modifier.weight(1f),
            ) {
                StreamingCardContent()
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            SettingsCard(
                title = "About",
                modifier = Modifier.weight(1f),
            ) {
                AboutCardContent(onSignOutClick = onSignOutClick)
            }
            SettingsCard(
                title = "Quick Info",
                modifier = Modifier.weight(1f),
            ) {
                QuickInfoCardContent()
            }
        }
    }
}

// ── Account Section ──────────────────────────────────────────────

@Composable
private fun AccountSection() {
    SettingsSection(title = "Account") {
        AccountCardContent()
    }
}

@Composable
private fun AccountCardContent() {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = DarkCard),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AsyncImage(
                model = mockUser.avatarUrl,
                contentDescription = "Avatar",
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = mockUser.displayName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = OnDark,
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = mockUser.email,
                    style = MaterialTheme.typography.bodySmall,
                    color = OnDarkVariant,
                )
                Spacer(modifier = Modifier.height(4.dp))
                TierBadge(tier = mockUser.membershipTier.name)
            }
        }
    }
}

@Composable
private fun TierBadge(tier: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(TierUltimate.copy(alpha = 0.15f))
            .padding(horizontal = 8.dp, vertical = 2.dp),
    ) {
        Text(
            text = tier,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = TierUltimate,
        )
    }
}

// ── Streaming Section ────────────────────────────────────────────

@Composable
private fun StreamingSection() {
    SettingsSection(title = "Streaming") {
        StreamingCardContent()
    }
}

@Composable
private fun StreamingCardContent() {
    var resolution by remember { mutableStateOf(RESOLUTION_OPTIONS[0]) }
    var fps by remember { mutableStateOf(FPS_OPTIONS[0]) }
    var codec by remember { mutableStateOf(CODEC_OPTIONS[0]) }
    var bitrate by remember { mutableFloatStateOf(25f) }

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = DarkCard),
    ) {
        Column {
            SettingsDropdownRow(
                label = "Resolution",
                options = RESOLUTION_OPTIONS,
                selected = resolution,
                onSelected = { resolution = it },
            )
            SettingsDropdownRow(
                label = "FPS",
                options = FPS_OPTIONS,
                selected = fps,
                onSelected = { fps = it },
            )
            SettingsDropdownRow(
                label = "Codec",
                options = CODEC_OPTIONS,
                selected = codec,
                onSelected = { codec = it },
            )
            SettingsRowSlider(
                title = "Bitrate",
                value = bitrate,
                onValueChange = { bitrate = it },
                valueLabel = "${bitrate.toInt()} Mbps",
                valueRange = BITRATE_MIN..BITRATE_MAX,
            )
        }
    }
}

// ── About Section ────────────────────────────────────────────────

@Composable
private fun AboutSection(onSignOutClick: () -> Unit) {
    SettingsSection(title = "About") {
        AboutCardContent(onSignOutClick = onSignOutClick)
    }
}

@Composable
private fun AboutCardContent(onSignOutClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = DarkCard),
    ) {
        Column {
            SettingsRowClickable(
                title = "App Version",
                subtitle = "1.0.0",
                onClick = { },
            )
            SettingsRowClickable(
                title = "Licenses",
                subtitle = "Open source licenses",
                onClick = { },
            )
            Button(
                onClick = onSignOutClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Error.copy(alpha = 0.15f),
                    contentColor = Error,
                ),
            ) {
                Icon(
                    imageVector = Icons.Filled.Warning,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Sign Out",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                )
            }
        }
    }
}

// ── Quick Info (tablet only) ─────────────────────────────────────

@Composable
private fun QuickInfoCardContent() {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = DarkCard),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            QuickInfoRow(label = "Platform", value = "Android")
            QuickInfoRow(label = "Stream Protocol", value = "WebRTC")
            QuickInfoRow(label = "Codec Support", value = "H.264, HEVC, AV1")
            QuickInfoRow(label = "Max Resolution", value = "1080p")
            QuickInfoRow(label = "Max Frame Rate", value = "60 FPS")
        }
    }
}

@Composable
private fun QuickInfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
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
            fontWeight = FontWeight.Medium,
            color = OnDark,
        )
    }
}

// ── Settings Dropdown Row ────────────────────────────────────────

@Composable
private fun SettingsDropdownRow(
    label: String,
    options: List<String>,
    selected: String,
    onSelected: (String) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .clickable { expanded = true },
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge,
                color = OnDark,
                modifier = Modifier.weight(1f),
            )
            Text(
                text = selected,
                style = MaterialTheme.typography.bodyMedium,
                color = OnDarkVariant,
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            containerColor = DarkCard,
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = option,
                            color = if (option == selected) Blue40 else OnDark,
                        )
                    },
                    onClick = {
                        onSelected(option)
                        expanded = false
                    },
                )
            }
        }
    }
}

// ── Shared Layout Primitives ─────────────────────────────────────

@Composable
private fun SettingsSection(
    title: String,
    content: @Composable () -> Unit,
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
            color = Blue40,
            modifier = Modifier.padding(bottom = 8.dp),
        )
        content()
    }
}

@Composable
private fun SettingsCard(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = DarkCard),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = Blue40,
                modifier = Modifier.padding(bottom = 12.dp),
            )
            content()
        }
    }
}

// ── Sign Out Dialog ──────────────────────────────────────────────

@Composable
private fun SignOutDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = DarkCard,
        titleContentColor = OnDark,
        textContentColor = OnDarkVariant,
        title = {
            Text(
                text = "Sign Out",
                fontWeight = FontWeight.Bold,
            )
        },
        text = {
            Text(text = "Are you sure you want to sign out? You will need to sign in again to access your games.")
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Error,
                    contentColor = OnDark,
                ),
                shape = RoundedCornerShape(8.dp),
            ) {
                Text("Sign Out")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "Cancel",
                    color = OnDarkVariant,
                )
            }
        },
    )
}
