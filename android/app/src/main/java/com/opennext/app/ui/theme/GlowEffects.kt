package com.opennext.app.ui.theme

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.opennext.app.data.model.MembershipTier

fun Modifier.glowShadow(
    color: Color,
    blur: Dp = 12.dp,
    corner: Shape? = null,
): Modifier = this.shadow(
    elevation = blur,
    shape = corner ?: androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
    ambientColor = color,
    spotColor = color,
)

fun Modifier.tierGlow(tier: MembershipTier, blur: Dp = 10.dp): Modifier {
    val color = tierGlowColor(tier)
    return this.glowShadow(color, blur)
}

fun tierGlowColor(tier: MembershipTier): Color = when (tier) {
    MembershipTier.FREE -> TierFree
    MembershipTier.PERFORMANCE -> TierPerformance
    MembershipTier.ULTIMATE -> TierUltimate
}

fun Modifier.ambientShadow(
    blur: Dp = 6.dp,
    corner: Shape? = null,
): Modifier = this.shadow(
    elevation = blur,
    shape = corner ?: androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
    ambientColor = Color.Black.copy(alpha = 0.6f),
    spotColor = Color.Black.copy(alpha = 0.6f),
)