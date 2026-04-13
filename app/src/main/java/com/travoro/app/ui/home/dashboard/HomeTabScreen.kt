package com.travoro.app.ui.home.dashboard

import android.widget.Toast
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.travoro.app.data.remote.dto.suggestion.Data
import com.travoro.app.di.Session
import com.travoro.app.ui.home.homeNavigation.MyProfileTab
import com.travoro.app.ui.home.homeNavigation.TravelAITab
import com.travoro.app.ui.home.profile.ProfileViewModel
import com.travoro.app.ui.theme.GradientSunsetEnd
import com.travoro.app.ui.theme.MidnightBlue
import com.travoro.app.ui.theme.TealCyan
import com.travoro.app.ui.theme.TealCyanDark
import com.travoro.app.ui.theme.TealCyanLight
import com.travoro.app.ui.theme.WarningYellow
import kotlinx.coroutines.delay

@Composable
fun HomeTabScreen(
    paddingValues: PaddingValues,
    homeViewModel: HomeViewModel,
    profileViewModel: ProfileViewModel,
    homeNavController: NavController,
    session: Session,
) {
    val profile by profileViewModel.profile.collectAsStateWithLifecycle()
    val trendingDestination by homeViewModel.trendingDestination.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        homeViewModel.fetchLocationAndCity()
        delay(500)
        homeViewModel.startLocationForActiveTrips(session.getUserId())
    }

    LaunchedEffect(trendingDestination) {
        homeViewModel.fetchTrendingDestination()
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = paddingValues.calculateTopPadding())
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 120.dp),
    ) {
        item {
            HomeHeader(userName = profile?.fullname ?: "Traveler")
            Spacer(modifier = Modifier.height(5.dp))
        }

        if (profile != null && profile?.phone.isNullOrEmpty()) {
            item {
                ProfileCompletionBanner(
                    onClick = {
                        homeNavController.navigate(MyProfileTab)
                    },
                )
                Spacer(modifier = Modifier.height(24.dp))
            }
        }

        item {
            SectionTitle(title = "Essentials", subtitle = "Explore your travel needs")
            HomeOptionCards(homeNavController)
            Spacer(modifier = Modifier.height(24.dp))
        }

        item {
            AIBannerCard(onClick = { homeNavController.navigate(TravelAITab) })
            Spacer(modifier = Modifier.height(10.dp))
        }

        item {
            SectionTitle(
                title = "Trending Destinations", subtitle = "Scouting uncharted off-grid vectors"
            )
            when (trendingDestination) {
                is HomeViewModel.TrendingDestinationEvent.Loading -> {
                    CircularProgressIndicator()
                }

                is HomeViewModel.TrendingDestinationEvent.Success -> {
                    val data =
                        (trendingDestination as HomeViewModel.TrendingDestinationEvent.Success).data

                    VaniIntelFeed(data)
                }

                else -> {}
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun SectionTitle(
    title: String,
    subtitle: String,
) {
    Column(modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
fun VaniIntelFeed(trendingDestination: List<Data?>?) {
    val safeDestinations = trendingDestination?.filterNotNull() ?: emptyList()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp), // Large breathing room between massive cards
    ) {
        safeDestinations.forEach { dest ->
            IntelDossierCard(dest)
        }
    }
}

@Composable
fun IntelDossierCard(dest: Data) {
    val context = LocalContext.current
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(380.dp)
            .clip(RoundedCornerShape(24.dp))
            .clickable {
                Toast.makeText(context, "Feature coming soon..", Toast.LENGTH_SHORT).show()
            },
        shape = RoundedCornerShape(24.dp),
        color = MidnightBlue,
        border = BorderStroke(1.dp, TealCyan.copy(alpha = 0.2f)),
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = dest.image,
                contentDescription = dest.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            0.0f to Color.Black.copy(alpha = 0.4f),
                            0.3f to Color.Transparent,
                            0.6f to Color.Transparent,
                            1.0f to MidnightBlue.copy(alpha = 0.95f),
                        ),
                    ),
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = TealCyan.copy(alpha = 0.15f),
                        border = BorderStroke(1.dp, TealCyan.copy(alpha = 0.5f)),
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                Icons.Rounded.AutoAwesome,
                                null,
                                tint = TealCyan,
                                modifier = Modifier.size(12.dp)
                            )
                        }
                    }
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.Black.copy(alpha = 0.5f))
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            Icons.Default.Star,
                            null,
                            tint = GradientSunsetEnd,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = dest.rating ?: "4.8",
                            style = TextStyle(
                                fontFamily = FontFamily.Monospace,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            color = Color.White,
                        )
                    }
                }
                Column(modifier = Modifier.fillMaxWidth()) {
                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = dest.title?.substringAfter(" ") ?: "",
                        style = TextStyle(
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 0.5.sp,
                            lineHeight = 32.sp,
                        ),
                        color = Color.White,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = dest.description
                            ?: "Awaiting detailed VANI reconnaissance data for this sector.",
                        style = TextStyle(
                            fontSize = 14.sp,
                            lineHeight = 20.sp,
                            fontWeight = FontWeight.Medium,
                        ),
                        color = Color.White.copy(alpha = 0.7f),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(
                            text = "AWAITING DESTINATION",
                            style = TextStyle(
                                fontFamily = FontFamily.Monospace,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp,
                            ),
                            color = Color.White.copy(alpha = 0.3f),
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "VIEW INTEL",
                                style = TextStyle(
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 1.sp,
                                ),
                                color = TealCyan,
                            )
                            Spacer(Modifier.width(4.dp))
                            Icon(
                                Icons.AutoMirrored.Rounded.ArrowForward,
                                null,
                                tint = TealCyan,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AIBannerCard(onClick: () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition(label = "ai_banner_anim")
    val sparkleRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(6000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "sparkle_rot",
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .clip(RoundedCornerShape(24.dp))
            .clickable { onClick() }
            .background(
                Brush.linearGradient(
                    colors = listOf(TealCyanDark, TealCyan),
                ),
            ),
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = 40.dp, y = (-40).dp)
                .size(150.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.15f)),
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "AI",
                        tint = Color.White,
                        modifier = Modifier
                            .size(45.dp)
                            .graphicsLayer { rotationZ = sparkleRotation },
                    )

                    Spacer(modifier = Modifier.width(18.dp))
                    Text(
                        text = "Travoro AI",
                        color = Color.White.copy(alpha = 0.9f),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Build your dream itinerary in seconds.",
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    lineHeight = MaterialTheme.typography.titleMedium.lineHeight,
                )
            }

            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color.White.copy(alpha = 0.2f))
                    .border(1.dp, Color.White.copy(alpha = 0.3f), RoundedCornerShape(24.dp)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Try Now",
                    tint = Color.White,
                )
            }
        }
    }
}

@OptIn(ExperimentalTextApi::class)
@Composable
fun HomeHeader(userName: String?) {
    val nameGradient = remember {
        Brush.linearGradient(
            colors = listOf(
                TealCyanDark,
                TealCyanLight,
            ),
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 10.dp),
    ) {
        Text(
            text = userName ?: "User",
            style = MaterialTheme.typography.displaySmall.copy(
                brush = nameGradient,
            ),
            fontWeight = FontWeight.ExtraBold,
        )

        Spacer(modifier = Modifier.height((-7).dp))

        Text(
            text = "Ready to build a scalable itinerary today?",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
        )
    }
}

@Composable
fun ProfileCompletionBanner(
    onClick: () -> Unit
) {
    val dynamicColor = MaterialTheme.colorScheme.onSurface

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        color = WarningYellow.copy(alpha = 0.05f),
        border = BorderStroke(1.dp, WarningYellow.copy(alpha = 0.3f)),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(WarningYellow.copy(alpha = 0.1f))
                    .border(1.dp, WarningYellow.copy(alpha = 0.2f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Rounded.Warning,
                    contentDescription = "Action Required",
                    tint = WarningYellow,
                    modifier = Modifier.size(24.dp),
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {

                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(WarningYellow),
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "ACTION REQUIRED",
                        style = TextStyle(
                            fontFamily = FontFamily.Monospace,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp,
                        ),
                        color = WarningYellow,
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "COMMS LINK OFFLINE",
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 0.5.sp,
                    ),
                    color = dynamicColor,
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "Provide communication ID (phone) to enable crew tracking and secure invites.",
                    style = TextStyle(
                        fontSize = 11.sp,
                        lineHeight = 16.sp,
                    ),
                    color = dynamicColor.copy(alpha = 0.6f),
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = WarningYellow.copy(alpha = 0.1f),
                border = BorderStroke(1.dp, WarningYellow.copy(alpha = 0.4f)),
                modifier = Modifier.clickable { onClick() },
            ) {
                Text(
                    text = "RESOLVE",
                    style = TextStyle(
                        fontFamily = FontFamily.Monospace,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp,
                    ),
                    color = WarningYellow,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                )
            }
        }
    }
}
