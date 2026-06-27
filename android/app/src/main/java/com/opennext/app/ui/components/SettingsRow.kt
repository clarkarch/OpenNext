package com.opennext.app.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.opennext.app.ui.theme.Blue40
import com.opennext.app.ui.theme.Blue50
import com.opennext.app.ui.theme.DarkBg
import com.opennext.app.ui.theme.DarkCard
import com.opennext.app.ui.theme.GlowCyanBright
import com.opennext.app.ui.theme.OnDark
import com.opennext.app.ui.theme.OnDarkMuted
import com.opennext.app.ui.theme.OnDarkVariant

@Composable
fun SettingsRowClickable(
    title: String,
    subtitle: String? = null,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(DarkCard)
            .border(0.5.dp, OnDarkMuted.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodyLarge,
                        color = OnDark,
                    )
                    if (subtitle != null) {
                        Text(
                            text = subtitle,
                            style = MaterialTheme.typography.labelSmall,
                            color = OnDarkVariant,
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = OnDarkMuted,
            )
        }
    }
}

@Composable
fun SettingsRowToggle(
    title: String,
    subtitle: String? = null,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val thumbGlow by animateColorAsState(
        targetValue = if (checked) GlowCyanBright else Color.Unspecified,
        animationSpec = tween(200),
        label = "thumbGlow"
    )
    val glowAlpha by animateFloatAsState(
        targetValue = if (checked) 0.6f else 0f,
        animationSpec = tween(200),
        label = "glowAlpha"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(DarkCard)
            .border(0.5.dp, OnDarkMuted.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
            .padding(horizontal = 16.dp, vertical = 10.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodyLarge,
                        color = OnDark,
                    )
                    if (subtitle != null) {
                        Text(
                            text = subtitle,
                            style = MaterialTheme.typography.labelSmall,
                            color = OnDarkVariant,
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .shadow(
                        elevation = if (checked) 8.dp else 0.dp,
                        shape = CircleShape,
                        ambientColor = GlowCyanBright.copy(alpha = glowAlpha),
                        spotColor = GlowCyanBright.copy(alpha = glowAlpha),
                    )
            ) {
                Switch(
                    checked = checked,
                    onCheckedChange = onCheckedChange,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = Blue40,
                        uncheckedThumbColor = OnDarkMuted,
                        uncheckedTrackColor = DarkBg,
                    ),
                )
            }
        }
    }
}

@Composable
fun SettingsRowSlider(
    title: String,
    value: Float,
    onValueChange: (Float) -> Unit,
    valueLabel: String,
    valueRange: ClosedFloatingPointRange<Float> = 0f..100f,
    modifier: Modifier = Modifier,
) {
    var dragging by remember { mutableStateOf(false) }
    val thumbColor by animateColorAsState(
        targetValue = if (dragging) Blue50 else Blue40,
        animationSpec = tween(150),
        label = "thumbColor"
    )
    val trackColor by animateColorAsState(
        targetValue = if (dragging) GlowCyanBright else Blue40,
        animationSpec = tween(150),
        label = "trackColor"
    )
    val glowElevation by animateFloatAsState(
        targetValue = if (dragging) 12f else 0f,
        animationSpec = tween(150),
        label = "glowElevation"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(DarkCard)
            .border(0.5.dp, OnDarkMuted.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
            .padding(horizontal = 16.dp, vertical = 10.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = OnDark,
                )
                Box(
                    modifier = Modifier
                        .shadow(
                            elevation = glowElevation.dp,
                            shape = CircleShape,
                            ambientColor = GlowCyanBright.copy(alpha = 0.3f),
                            spotColor = GlowCyanBright.copy(alpha = 0.3f),
                        )
                ) {
                    Slider(
                        value = value,
                        onValueChange = onValueChange,
                        valueRange = valueRange,
                        onValueChangeFinished = { dragging = false },
                        interactionSource = remember { MutableInteractionSource() }
                            .also { source ->
                                LaunchedEffect(source) {
                                    source.interactions.collect { interaction ->
                                        dragging = interaction is DragInteraction.Start
                                    }
                                }
                            },
                        colors = SliderDefaults.colors(
                            thumbColor = thumbColor,
                            activeTrackColor = trackColor,
                            inactiveTrackColor = OnDarkMuted.copy(alpha = 0.3f),
                        ),
                    )
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = valueLabel,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Medium,
                color = if (dragging) GlowCyanBright else Blue40,
            )
        }
    }
}

@Composable
fun SettingsRowDropdown(
    title: String,
    options: List<String>,
    selected: String,
    onSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }
    var handledSelection by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(DarkCard)
            .border(0.5.dp, OnDarkMuted.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
            .clickable { expanded = true }
            .padding(horizontal = 16.dp, vertical = 14.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = OnDark,
                modifier = Modifier.weight(1f),
            )
            Text(
                text = selected,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = Blue40,
            )
        }

        androidx.compose.material3.DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
                handledSelection = false
            },
            modifier = Modifier.background(DarkCard),
        ) {
            options.forEach { option ->
                androidx.compose.material3.DropdownMenuItem(
                    text = {
                        Text(
                            text = option,
                            color = if (option == selected) Blue40 else OnDark,
                            fontWeight = if (option == selected) FontWeight.Medium else FontWeight.Normal,
                        )
                    },
                    onClick = {
                        if (!handledSelection) {
                            onSelected(option)
                            handledSelection = true
                        }
                        expanded = false
                    },
                )
            }
        }
    }
}