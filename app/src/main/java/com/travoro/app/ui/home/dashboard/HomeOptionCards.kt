package com.travoro.app.ui.home.dashboard

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.PersonSearch
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY
import com.travoro.app.data.remote.dto.others.TravelOption
import com.travoro.app.ui.home.homeNavigation.AiSearch
import com.travoro.app.ui.home.homeNavigation.BillSplit
import com.travoro.app.ui.home.homeNavigation.CreateTripTab
import com.travoro.app.ui.home.homeNavigation.FindMember
import com.travoro.app.ui.home.homeNavigation.Sos
import com.travoro.app.ui.theme.ErrorRed
import com.travoro.app.ui.theme.TealCyan
import com.travoro.app.ui.utils.LocationHelper
import com.travoro.app.ui.utils.mapNavigation.openGoogleMapsCurrentLocation
import kotlinx.coroutines.launch

@Composable
fun HomeOptionCards(
    homeNavController: NavController,
) {
    val context = LocalContext.current
    val locationHelper = LocationHelper(context)
    val scope = rememberCoroutineScope()

    val options = listOf(
        TravelOption("Create Trip", Icons.Default.Add, CreateTripTab),
        TravelOption("Bill Split", Icons.Default.AccountBalanceWallet, BillSplit),
        TravelOption("Ai Search", Icons.Default.SmartToy, AiSearch),
        TravelOption("Find Member", Icons.Default.PersonSearch, FindMember),
        TravelOption("Map", Icons.Default.Map, ""),
        TravelOption("SOS Alert", Icons.Default.Warning, Sos, isAlert = true),
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        shape = RoundedCornerShape(32.dp),
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f),
        contentColor = MaterialTheme.colorScheme.onSurface,
        border = BorderStroke(width = 1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(28.dp),
        ) {
            val rows = options.chunked(3)
            rows.forEach { rowItems ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    rowItems.forEach { option ->
                        Box(
                            modifier = Modifier.weight(1f),
                            contentAlignment = Alignment.Center,
                        ) {
                            AmbientGlowItem(
                                item = option,
                                onNavigate = {
                                    when (option.title) {
                                        "Map" -> {
                                            scope.launch {
                                                val location = locationHelper.getCurrentLocation(
                                                    PRIORITY_HIGH_ACCURACY,
                                                )
                                                location?.let {
                                                    openGoogleMapsCurrentLocation(
                                                        context,
                                                        it.latitude,
                                                        it.longitude,
                                                    )
                                                }
                                            }
                                        }
                                        else -> {
                                            homeNavController.navigate(option.route)
                                        }
                                    }
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AmbientGlowItem(
    item: TravelOption,
    onNavigate: () -> Unit,
) {
    val baseColor = if (item.isAlert) ErrorRed else TealCyan

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable { onNavigate() }
            .padding(8.dp),
    ) {
        Box(
            modifier = Modifier.size(56.dp),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                baseColor.copy(alpha = 0.35f),
                                baseColor.copy(alpha = 0.1f),
                                Color.Transparent,
                            ),
                        ),
                    ),
            )
            Icon(
                imageVector = item.icon,
                contentDescription = item.title,
                tint = if (item.isAlert) ErrorRed else MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(26.dp),
            )
        }
        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = item.title,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold,
        )
    }
}
