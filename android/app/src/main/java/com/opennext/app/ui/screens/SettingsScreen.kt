package com.opennext.app.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.opennext.app.data.mockUser
import com.opennext.app.ui.components.SettingsRowClickable
import com.opennext.app.ui.components.SettingsRowDropdown
import com.opennext.app.ui.components.SettingsRowSlider
import com.opennext.app.ui.components.SettingsRowToggle
import com.opennext.app.ui.theme.Blue40
import com.opennext.app.ui.theme.DarkBg
import com.opennext.app.ui.theme.DarkCard
import com.opennext.app.ui.theme.Error
import com.opennext.app.ui.theme.GlowCyanBright
import com.opennext.app.ui.theme.OnDark
import com.opennext.app.ui.theme.OnDarkMuted
import com.opennext.app.ui.theme.OnDarkVariant
import com.opennext.app.ui.theme.TierUltimate

private val QUALITY_PRESETS = listOf("Auto", "Low", "Medium", "High", "Ultra")
private val FPS_OPTIONS = listOf("Auto", "60", "120")
private val CODEC_OPTIONS = listOf("Auto", "H.264", "HEVC", "AV1")
private val RESOLUTION_OPTIONS = listOf("Auto", "2160p", "1080p", "720p", "480p")

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
                        color = OnDark,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = OnDark,
                        )
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
                TabletSettingsLayout(onSignOutClick = { showSignOutDialog = true })
            } else {
                PhoneSettingsLayout(onSignOutClick = { showSignOutDialog = true })
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
private fun PhoneSettingsLayout(
    onSignOutClick: () -> Unit,
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp),
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        StreamSection(index = 0)
        Spacer(modifier = Modifier.height(24.dp))
        AudioSection(index = 1)
        Spacer(modifier = Modifier.height(24.dp))
        DisplaySection(index = 2)
        Spacer(modifier = Modifier.height(24.dp))
        AccountSection(index = 3)
        Spacer(modifier = Modifier.height(24.dp))
        AboutSection(index = 4, onSignOutClick = onSignOutClick)
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun TabletSettingsLayout(
    onSignOutClick: () -> Unit,
) {
    val scrollState = rememberScrollState()

    Row(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .width(240.dp)
                .fillMaxHeight()
                .background(DarkCard.copy(alpha = 0.3f))
                .padding(vertical = 16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 16.dp),
            ) {
                SectionHeader(title = "SETTINGS", modifier = Modifier.padding(bottom = 16.dp))
                SectionHeader(title = "STREAM", modifier = Modifier.padding(top = 16.dp, bottom = 8.dp))
                SectionHeader(title = "AUDIO", modifier = Modifier.padding(top = 16.dp, bottom = 8.dp))
                SectionHeader(title = "DISPLAY", modifier = Modifier.padding(top = 16.dp, bottom = 8.dp))
                SectionHeader(title = "ACCOUNT", modifier = Modifier.padding(top = 16.dp, bottom = 8.dp))
                SectionHeader(title = "ABOUT", modifier = Modifier.padding(top = 16.dp, bottom = 8.dp))
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            StreamSection(index = 0)
            AudioSection(index = 1)
            DisplaySection(index = 2)
            AccountSection(index = 3)
            AboutSection(index = 4, onSignOutClick = onSignOutClick)
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun AnimatedRow(
    visible: Boolean,
    index: Int,
    content: @Composable () -> Unit,
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(200, delayMillis = index * 40)) +
                slideInHorizontally(animationSpec = tween(200, delayMillis = index * 40)) { it / 3 },
    ) {
        content()
    }
}

@Composable
private fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelMedium,
        fontWeight = FontWeight.Bold,
        color = Blue40,
        letterSpacing = 1.5.sp,
        modifier = modifier,
    )
}

@Composable
private fun SettingsCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(DarkCard)
            .border(0.5.dp, OnDarkMuted.copy(alpha = 0.15f), RoundedCornerShape(16.dp))
            .padding(16.dp),
        content = content,
    )
}

@Composable
private fun ColumnScope.StreamSection(index: Int) {
    var qualityPreset by remember { mutableStateOf(QUALITY_PRESETS[0]) }
    var fps60 by remember { mutableStateOf(false) }
    var fps120 by remember { mutableStateOf(false) }
    var codec by remember { mutableStateOf(CODEC_OPTIONS[0]) }

    AnimatedRow(visible = true, index = index) {
        SettingsCard {
            Text(
                text = "STREAM",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = Blue40,
                letterSpacing = 1.5.sp,
                modifier = Modifier.padding(bottom = 12.dp),
            )
            Spacer(modifier = Modifier.height(4.dp))

            SettingsRowDropdown(
                title = "Quality Preset",
                options = QUALITY_PRESETS,
                selected = qualityPreset,
                onSelected = { qualityPreset = it },
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                SettingsRowToggle(
                    title = "60 FPS",
                    subtitle = "Standard frame rate",
                    checked = fps60,
                    onCheckedChange = {
                        fps60 = it
                        if (it) fps120 = false
                    },
                    modifier = Modifier.weight(1f),
                )
                SettingsRowToggle(
                    title = "120 FPS",
                    subtitle = "High frame rate",
                    checked = fps120,
                    onCheckedChange = {
                        fps120 = it
                        if (it) fps60 = false
                    },
                    modifier = Modifier.weight(1f),
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            SettingsRowDropdown(
                title = "Codec",
                options = CODEC_OPTIONS,
                selected = codec,
                onSelected = { codec = it },
            )
        }
    }
}

@Composable
private fun ColumnScope.AudioSection(index: Int) {
    var masterVolume by remember { mutableFloatStateOf(80f) }
    var micVolume by remember { mutableFloatStateOf(70f) }
    var muted by remember { mutableStateOf(false) }

    AnimatedRow(visible = true, index = index) {
        SettingsCard {
            Text(
                text = "AUDIO",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = Blue40,
                letterSpacing = 1.5.sp,
                modifier = Modifier.padding(bottom = 12.dp),
            )
            Spacer(modifier = Modifier.height(4.dp))

            SettingsRowSlider(
                title = "Master Volume",
                value = if (muted) 0f else masterVolume,
                onValueChange = { masterVolume = it },
                valueLabel = "${if (muted) 0 else masterVolume.toInt()}%",
                valueRange = 0f..100f,
            )
            Spacer(modifier = Modifier.height(8.dp))

            SettingsRowSlider(
                title = "Mic Volume",
                value = if (muted) 0f else micVolume,
                onValueChange = { micVolume = it },
                valueLabel = "${if (muted) 0 else micVolume.toInt()}%",
                valueRange = 0f..100f,
            )
            Spacer(modifier = Modifier.height(8.dp))

            SettingsRowToggle(
                title = "Mute All Audio",
                subtitle = "Temporarily silence all sounds",
                checked = muted,
                onCheckedChange = { muted = it },
            )
        }
    }
}

@Composable
private fun ColumnScope.DisplaySection(index: Int) {
    var fullscreen by remember { mutableStateOf(true) }
    var resolution by remember { mutableStateOf(RESOLUTION_OPTIONS[0]) }
    var vSync by remember { mutableStateOf(true) }

    AnimatedRow(visible = true, index = index) {
        SettingsCard {
            Text(
                text = "DISPLAY",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = Blue40,
                letterSpacing = 1.5.sp,
                modifier = Modifier.padding(bottom = 12.dp),
            )
            Spacer(modifier = Modifier.height(4.dp))

            SettingsRowToggle(
                title = "Fullscreen",
                subtitle = "Fill entire display when streaming",
                checked = fullscreen,
                onCheckedChange = { fullscreen = it },
            )
            Spacer(modifier = Modifier.height(8.dp))

            SettingsRowDropdown(
                title = "Resolution",
                options = RESOLUTION_OPTIONS,
                selected = resolution,
                onSelected = { resolution = it },
            )
            Spacer(modifier = Modifier.height(8.dp))

            SettingsRowToggle(
                title = "V-Sync",
                subtitle = "Synchronize refresh rate",
                checked = vSync,
                onCheckedChange = { vSync = it },
            )
        }
    }
}

@Composable
private fun ColumnScope.AccountSection(index: Int) {
    AnimatedRow(visible = true, index = index) {
        SettingsCard {
            Text(
                text = "ACCOUNT",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = Blue40,
                letterSpacing = 1.5.sp,
                modifier = Modifier.padding(bottom = 12.dp),
            )
            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
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
                    Spacer(modifier = Modifier.height(6.dp))
                    TierBadge(tier = mockUser.membershipTier.name)
                }
            }
        }
    }
}

@Composable
private fun TierBadge(tier: String) {
    Box(
        modifier = Modifier
            .shadow(4.dp, RoundedCornerShape(6.dp), ambientColor = TierUltimate.copy(alpha = 0.3f))
            .clip(RoundedCornerShape(6.dp))
            .background(TierUltimate.copy(alpha = 0.15f))
            .border(0.5.dp, TierUltimate.copy(alpha = 0.4f), RoundedCornerShape(6.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp),
    ) {
        Text(
            text = tier,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = TierUltimate,
            letterSpacing = 0.5.sp,
        )
    }
}

@Composable
private fun ColumnScope.AboutSection(
    index: Int,
    onSignOutClick: () -> Unit,
) {
    AnimatedRow(visible = true, index = index) {
        SettingsCard {
            Text(
                text = "ABOUT",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = Blue40,
                letterSpacing = 1.5.sp,
                modifier = Modifier.padding(bottom = 12.dp),
            )
            Spacer(modifier = Modifier.height(4.dp))

            SettingsRowClickable(
                title = "App Version",
                subtitle = "1.0.0",
                onClick = { },
            )
            Spacer(modifier = Modifier.height(8.dp))

            SettingsRowClickable(
                title = "Licenses",
                subtitle = "Open source licenses",
                onClick = { },
            )
            Spacer(modifier = Modifier.height(8.dp))

            SettingsRowClickable(
                title = "Privacy Policy",
                subtitle = "View our privacy policy",
                onClick = { },
            )
            Spacer(modifier = Modifier.height(16.dp))

            SignOutButton(onClick = onSignOutClick)
        }
    }
}

@Composable
private fun SignOutButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(6.dp, RoundedCornerShape(10.dp), ambientColor = Error.copy(alpha = 0.25f))
            .clip(RoundedCornerShape(10.dp))
            .background(Error.copy(alpha = 0.12f))
            .border(0.5.dp, Error.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 14.dp),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                contentDescription = null,
                tint = Error,
                modifier = Modifier.size(20.dp),
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "Sign Out",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = Error,
            )
        }
    }
}

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