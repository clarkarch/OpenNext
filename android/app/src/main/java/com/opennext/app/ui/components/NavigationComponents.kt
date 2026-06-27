package com.opennext.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.opennext.app.ui.theme.Blue40
import com.opennext.app.ui.theme.DarkSurface
import com.opennext.app.ui.theme.OnDarkVariant

data class NavItem(
    val label: String,
    val selectedIcon: androidx.compose.ui.graphics.vector.ImageVector,
    val unselectedIcon: androidx.compose.ui.graphics.vector.ImageVector,
)

@Composable
fun PhoneBottomBar(
    items: List<NavItem>,
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavigationBar(
        modifier = modifier,
        containerColor = DarkSurface,
        contentColor = Color.White,
    ) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedIndex == index,
                onClick = { onItemSelected(index) },
                icon = {
                    Icon(
                        imageVector = if (selectedIndex == index) item.selectedIcon else item.unselectedIcon,
                        contentDescription = item.label,
                    )
                },
                label = { Text(item.label) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Blue40,
                    selectedTextColor = Blue40,
                    unselectedIconColor = OnDarkVariant,
                    unselectedTextColor = OnDarkVariant,
                    indicatorColor = Blue40.copy(alpha = 0.12f),
                ),
            )
        }
    }
}

@Composable
fun TabletNavigationRail(
    items: List<NavItem>,
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    avatarUrl: String? = null,
    logo: (@Composable () -> Unit)? = null,
) {
    Column(
        modifier = modifier
            .width(80.dp)
            .fillMaxHeight()
            .background(DarkSurface),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            contentAlignment = Alignment.Center,
        ) {
            if (logo != null) {
                logo()
            } else {
                Text(
                    text = "ON",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Blue40,
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        items.forEachIndexed { index, item ->
            NavigationRailItem(
                selected = selectedIndex == index,
                onClick = { onItemSelected(index) },
                icon = {
                    Icon(
                        imageVector = if (selectedIndex == index) item.selectedIcon else item.unselectedIcon,
                        contentDescription = item.label,
                    )
                },
                label = { Text(item.label) },
                colors = NavigationRailItemDefaults.colors(
                    selectedIconColor = Blue40,
                    selectedTextColor = Blue40,
                    unselectedIconColor = OnDarkVariant,
                    unselectedTextColor = OnDarkVariant,
                    indicatorColor = Blue40.copy(alpha = 0.12f),
                ),
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        if (avatarUrl != null) {
            AsyncImage(
                model = avatarUrl,
                contentDescription = "Profile",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun AdaptiveNavigation(
    items: List<NavItem>,
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
    isTablet: Boolean,
    avatarUrl: String? = null,
    logo: (@Composable () -> Unit)? = null,
) {
    if (isTablet) {
        TabletNavigationRail(
            items = items,
            selectedIndex = selectedIndex,
            onItemSelected = onItemSelected,
            avatarUrl = avatarUrl,
            logo = logo,
        )
    } else {
        PhoneBottomBar(
            items = items,
            selectedIndex = selectedIndex,
            onItemSelected = onItemSelected,
        )
    }
}
