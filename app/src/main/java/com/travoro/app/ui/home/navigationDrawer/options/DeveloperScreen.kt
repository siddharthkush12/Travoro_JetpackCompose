package com.travoro.app.ui.home.navigationDrawer.options

import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.AccountTree
import androidx.compose.material.icons.rounded.Code
import androidx.compose.material.icons.rounded.Terminal
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.travoro.app.R
import com.travoro.app.data.remote.dto.others.SocialItem
import com.travoro.app.ui.components.CustomTopBar
import com.travoro.app.ui.theme.TealCyan
import com.travoro.app.ui.theme.TealCyanLight

@Composable
fun DeveloperScreen(navController: NavController) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        CustomTopBar(
            title = "CORE ARCHITECT",
            icon = Icons.Rounded.Code,
            onBackClick = { navController.popBackStack() },
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.4f),
                border = BorderStroke(
                    1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f)
                ),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    TealCyan.copy(alpha = 0.15f),
                                    Color.Transparent,
                                ),
                            ),
                        )
                        .padding(vertical = 32.dp, horizontal = 24.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(140.dp)
                                .clip(CircleShape)
                                .border(
                                    width = 3.dp,
                                    brush = Brush.sweepGradient(
                                        listOf(
                                            TealCyan,
                                            TealCyanLight,
                                            TealCyan,
                                        ),
                                    ),
                                    shape = CircleShape,
                                )
                                .padding(6.dp),
                        ) {
                            AsyncImage(
                                model = "https://res.cloudinary.com/di4eksvat/image/upload/v1773258848/jtleoosj9psrnct7btt5.jpg",
                                contentDescription = "Siddharth Kushwaha",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop,
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "SIDDHARTH KUSHWAHA",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Black,
                                letterSpacing = 2.sp,
                                fontFamily = FontFamily(Font(R.font.inknutantiquamedium)),
                                fontSize = 20.sp,
                            ),
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Center,
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = TealCyan.copy(alpha = 0.1f),
                            border = BorderStroke(1.dp, TealCyan.copy(alpha = 0.3f)),
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(TealCyan),
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "FULL-STACK & ANDROID LEAD",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TealCyan,
                                    letterSpacing = 1.sp,
                                )
                            }
                        }
                    }
                }
            }

            ArchitectDataCard(
                title = "ABOUT",
                icon = Icons.Rounded.Terminal,
                content = "Engineering modern, high-performance mobile applications using Jetpack Compose and driving scalable, real-time backend architectures. Focused on AI integration, complex algorithmic optimization, and building seamless digital experiences.",
            )

            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Rounded.AccountTree,
                        contentDescription = null,
                        tint = TealCyan,
                        modifier = Modifier.size(18.dp),
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "COMMUNICATION PROTOCOLS",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp,
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                    )
                }

                DeveloperSocialGrid(
                    onLinkClick = { url ->
                        val intent = Intent(Intent.ACTION_VIEW, url.toUri())
                        context.startActivity(intent)
                    },
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun ArchitectDataCard(
    title: String,
    icon: ImageVector,
    content: String,
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.4f),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.09f)),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = TealCyan,
                    modifier = Modifier.size(18.dp),
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                    ),
                    color = TealCyan,
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = content,
                style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.9f),
            )
        }
    }
}

@Composable
fun DeveloperSocialGrid(onLinkClick: (String) -> Unit) {
    val items = listOf(
        SocialItem(
            "GITHUB REPO",
            R.drawable.github_color_svgrepo_com,
            "https://github.com/siddharthkush12",
        ),
        SocialItem(
            "LINKEDIN_NODE",
            R.drawable.linkedin_linked_in_svgrepo_com,
            "https://linkedin.com/in/siddharth02022002",
        ),
        SocialItem(
            "INSTAGRAM_FEED",
            R.drawable.instagram_svgrepo_com,
            "https://www.instagram.com/siddharth_kush2002/",
        ),
        SocialItem(
            "SECURE COMMS",
            R.drawable.brand_google_gmail_svgrepo_com,
            "mailto:siddharthkush12@gmail.com",
        ),
    )

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            SocialGridItem(
                item = items[0],
                modifier = Modifier.weight(1f),
            ) { onLinkClick(items[0].link) }
            SocialGridItem(
                item = items[1],
                modifier = Modifier.weight(1f),
            ) { onLinkClick(items[1].link) }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            SocialGridItem(
                item = items[2],
                modifier = Modifier.weight(1f),
            ) { onLinkClick(items[2].link) }
            SocialGridItem(
                item = items[3],
                modifier = Modifier.weight(1f),
            ) { onLinkClick(items[3].link) }
        }
    }
}

@Composable
fun SocialGridItem(
    item: SocialItem,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.3f),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.09f)),
        modifier = modifier
            .height(110.dp)
            .clickable { onClick() },
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(MaterialTheme.colorScheme.background.copy(alpha = 0.5f))
                    .border(1.dp, Color.White.copy(alpha = 0.02f), RoundedCornerShape(14.dp)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(item.icon),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(26.dp),
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = item.title,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp,
                ),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                textAlign = TextAlign.Center,
                maxLines = 1,
            )
        }
    }
}
