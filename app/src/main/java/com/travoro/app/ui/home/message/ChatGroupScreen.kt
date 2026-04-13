package com.travoro.app.ui.home.message

import androidx.compose.animation.core.EaseInOutQuart
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Hub
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.rounded.Forum
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.travoro.app.data.remote.dto.message.ChatGroup
import com.travoro.app.ui.components.HomeBarHeaders
import com.travoro.app.ui.components.formatDate
import com.travoro.app.ui.home.homeNavigation.MessageScreenTab
import com.travoro.app.ui.theme.TealCyan

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatGroupScreen(
    navController: NavController,
    viewModel: ChatGroupViewModel = hiltViewModel(),
) {
    val groups by viewModel.groups.collectAsStateWithLifecycle()
    val infiniteTransition = rememberInfiniteTransition(label = "messenger_pulse")

    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 2.15f,
        animationSpec =
            infiniteRepeatable(
                animation = tween(1300, easing = EaseInOutQuart),
                repeatMode = RepeatMode.Reverse,
            ),
        label = "iconScale",
    )

    LaunchedEffect(Unit) {
        viewModel.fetchGroups()
    }

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
    ) {
        HomeBarHeaders(
            title = "Trip Messenger",
            subtitle = "SECURE COMMS PROTOCOL",
            icon = Icons.Default.Hub,
            topPadding = WindowInsets.statusBars.asPaddingValues().calculateTopPadding(),
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding =
                PaddingValues(
                    start = 10.dp,
                    end = 10.dp,
                    top = 16.dp,
                    bottom = 95.dp,
                ),
            verticalArrangement = Arrangement.spacedBy(5.dp),
        ) {
            item {
                Column(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 6.dp, vertical = 16.dp),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Box(
                            modifier =
                                Modifier.graphicsLayer {
                                    scaleX = scale
                                    scaleY = scale
                                },
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Forum,
                                contentDescription = null,
                                tint = TealCyan.copy(alpha = 0.2f),
                                modifier = Modifier.size(32.dp),
                            )
                            Icon(
                                imageVector = Icons.Rounded.Forum,
                                contentDescription = "Active Comms",
                                tint = TealCyan,
                                modifier = Modifier.size(24.dp),
                            )
                        }

                        Spacer(Modifier.width(24.dp))

                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = "Trip ",
                                style =
                                    MaterialTheme.typography.headlineMedium.copy(
                                        fontWeight = FontWeight.ExtraLight,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                                    ),
                            )
                            Text(
                                text = "Messenger.",
                                style =
                                    MaterialTheme.typography.headlineMedium.copy(
                                        fontWeight = FontWeight.Black,
                                        color = TealCyan,
                                    ),
                            )
                        }
                    }

                    Spacer(Modifier.height(18.dp))

                    Text(
                        text = "Vani Comms Protocol active. Select a node to coordinate your journey.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        modifier = Modifier.padding(horizontal = 6.dp),
                    )
                }
            }

            items(groups) { group ->
                ChatGroupItem(
                    group = group,
                    onClick = {
                        navController.navigate(MessageScreenTab(group._id))
                    },
                )
            }
        }
    }
}

@Composable
fun ChatGroupItem(
    group: ChatGroup,
    onClick: () -> Unit,
) {
    val dynamicColor = MaterialTheme.colorScheme.onSurface

    Surface(
        modifier =
            Modifier
                .padding(horizontal = 6.dp, vertical = 6.dp)
                .fillMaxWidth()
                .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.3f),
        border =
            BorderStroke(
                1.dp,
                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.09f),
            ), // Subtle architectural edge
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box {
                AsyncImage(
                    model = group.trip.coverImage,
                    contentDescription = null,
                    modifier =
                        Modifier
                            .size(78.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .border(
                                1.dp,
                                TealCyan.copy(alpha = 0.2f),
                                RoundedCornerShape(14.dp),
                            ),
                    // Neon rim
                    contentScale = ContentScale.Crop,
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    text = group.trip.title.uppercase(),
                    style =
                        TextStyle(
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 0.5.sp,
                            color = dynamicColor,
                        ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        modifier = Modifier.size(12.dp),
                        tint = TealCyan.copy(alpha = 0.7f),
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = group.trip.destination,
                        fontSize = 12.sp,
                        color = dynamicColor.copy(alpha = 0.6f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Awaiting coordinate sync...",
                    fontSize = 13.sp,
                    color = dynamicColor.copy(alpha = 0.4f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }

            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.height(78.dp),
            ) {
                Text(
                    text = formatDate(group.updatedAt),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = dynamicColor.copy(alpha = 0.3f),
                )
            }
        }
    }
}
