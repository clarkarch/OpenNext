package com.opennext.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.opennext.app.data.model.MembershipTier
import com.opennext.app.ui.theme.OnDark
import com.opennext.app.ui.theme.OnDarkMuted
import com.opennext.app.ui.theme.TierFree
import com.opennext.app.ui.theme.TierPerformance
import com.opennext.app.ui.theme.TierUltimate

@Composable
fun TierBadgeSmall(
    tier: MembershipTier,
    modifier: Modifier = Modifier,
) {
    val (color, label) = tier.toBadgeProps()
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Spacer(
            modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(color),
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = color,
        )
    }
}

@Composable
fun TierBadgeBanner(
    tier: MembershipTier,
    modifier: Modifier = Modifier,
) {
    val (color, label) = tier.toBadgeProps()
    Text(
        text = label,
        style = MaterialTheme.typography.labelSmall,
        fontWeight = FontWeight.SemiBold,
        color = OnDark,
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .background(color)
            .padding(horizontal = 8.dp, vertical = 2.dp),
    )
}

@Composable
private fun MembershipTier.toBadgeProps(): Pair<Color, String> = when (this) {
    MembershipTier.FREE -> TierFree to "FREE"
    MembershipTier.PERFORMANCE -> TierPerformance to "PERFORMANCE"
    MembershipTier.ULTIMATE -> TierUltimate to "ULTIMATE"
}
