package com.example.travelapp.ui.home.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Hotel
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.travelapp.ui.home.homeNavigation.CreateTripTab
import com.example.travelapp.ui.theme.TealCyan

data class TravelOption(
    val title: String,
    val icon: ImageVector,
    val route: Any,
    val isAlert: Boolean = false
)

@Composable
fun HomeOptionCards(homeNavController: NavController) {
    val options = listOf(
        TravelOption("Create Trip", Icons.Default.Add, CreateTripTab),
        TravelOption("Bill Split", Icons.Default.AccountBalanceWallet, CreateTripTab),
        TravelOption("Translate", Icons.Default.Translate, CreateTripTab),
        TravelOption("Nearby", Icons.Default.LocationOn, CreateTripTab),
        TravelOption("Map", Icons.Default.Map, CreateTripTab),
        TravelOption("SOS Alert", Icons.Default.Warning, CreateTripTab, isAlert = true)
    )

    // The Master Glass Surface
    // The Master Glass Surface
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        shape = RoundedCornerShape(32.dp),

        // --- THE FIX: Guaranteed Contrast ---
        // onSurface is Black in day, White at night.
        // 5% opacity creates a perfect, visible frosted glass box on ANY background!
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f),

        contentColor = MaterialTheme.colorScheme.onSurface,

        // --- THE FIX: Cleaner Border Rendering ---
        // Using the native BorderStroke instead of Modifier.border
        border = androidx.compose.foundation.BorderStroke(
            width = 1.dp,
            brush = Brush.linearGradient(
                colors = listOf(
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f), // Visible edge
                    Color.Transparent,
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                )
            )
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp), // Inner padding moved inside the Surface content
            verticalArrangement = Arrangement.spacedBy(28.dp)
        ) {

            // A tiny, elegant header inside the card to ground the UI
            Text(
                text = "Essentials",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                letterSpacing = 1.5.sp,
                fontWeight = FontWeight.Bold
            )

            // The Grid Generation
            val rows = options.chunked(3)
            rows.forEach { rowItems ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    rowItems.forEach { option ->
                        Box(
                            modifier = Modifier.weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            AmbientGlowItem(
                                item = option,
                                onNavigate = { homeNavController.navigate(option.route) }
                            )
                        }
                    }

                    // Fills empty space perfectly if there are fewer than 3 items
                    repeat(3 - rowItems.size) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
fun AmbientGlowItem(
    item: TravelOption,
    onNavigate: () -> Unit
) {
    // Dynamic styling: Standard tools glow Cyan, SOS glows Crimson Red
    val baseColor = if (item.isAlert) Color(0xFFEF4444) else TealCyan

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable { onNavigate() }
            .padding(8.dp)
    ) {

        // --- THE AMBIENT GLOW EFFECT ---
        Box(
            modifier = Modifier.size(56.dp),
            contentAlignment = Alignment.Center
        ) {
            // 1. The Luminous Radial Backdrop
            Box(
                modifier = Modifier
                    .size(48.dp) // Slightly smaller than the container to let the glow fade
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                baseColor.copy(alpha = 0.35f), // Intense center
                                baseColor.copy(alpha = 0.1f),  // Mid fade
                                Color.Transparent              // Soft outer edge
                            )
                        )
                    )
            )

            // 2. The Crisp Foreground Icon
            Icon(
                imageVector = item.icon,
                contentDescription = item.title,
                // SOS stays red, other icons inherit the onSurface color automatically from the Surface!
                tint = if (item.isAlert) Color(0xFFFF6B6B) else MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(26.dp)
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        // --- Crisp Typography ---
        Text(
            text = item.title,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold
            // Notice: No color specified here! It automatically inherits perfectly from the Surface.
        )
    }
}