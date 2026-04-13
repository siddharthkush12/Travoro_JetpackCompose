package com.travoro.app.ui.home.message

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Forum
import androidx.compose.material.icons.rounded.Image
import androidx.compose.material.icons.rounded.Videocam
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.travoro.app.data.remote.dto.message.Message
import com.travoro.app.di.Session
import com.travoro.app.ui.components.CustomTopBar
import com.travoro.app.ui.components.formatTimeAgo
import com.travoro.app.ui.theme.TealCyan

@Composable
fun MessageScreen(
    groupId: String,
    userId: String,
    viewModel: MessageViewModel = hiltViewModel(),
    onClickBack: () -> Unit,
    session: Session,
) {
    val messages by viewModel.messages.collectAsStateWithLifecycle()
    val text by viewModel.text.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()
    val dynamicColor = MaterialTheme.colorScheme.onSurface
    var showAttachmentMenu by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadMessages(groupId)
        viewModel.joinGroup(groupId)
        viewModel.listenMessages()
    }

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background.copy(alpha = 0.9f)),
    ) {
        CustomTopBar(
            modifier = Modifier.fillMaxWidth(),
            title = "SECURE COMMS",
            icon = Icons.Rounded.Forum,
            onBackClick = onClickBack,
        )

        LazyColumn(
            modifier =
                Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
            state = listState,
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            items(messages) { message ->
                MessageBubble(
                    message = message,
                    isMine = message.sender._id == userId,
                    session = session,
                )
            }
        }

        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .drawBehind {
                        drawLine(
                            brush =
                                Brush.horizontalGradient(
                                    listOf(
                                        Color.Transparent,
                                        TealCyan.copy(alpha = 0.3f),
                                        Color.Transparent,
                                    ),
                                ),
                            start = Offset(0f, 0f),
                            end = Offset(size.width, 0f),
                            strokeWidth = 1.dp.toPx(),
                        )
                    }
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.8f))
                    .navigationBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box {
                    IconButton(
                        onClick = { showAttachmentMenu = true },
                        modifier =
                            Modifier
                                .size(50.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(dynamicColor.copy(alpha = 0.03f))
                                .border(
                                    1.dp,
                                    dynamicColor.copy(alpha = 0.08f),
                                    RoundedCornerShape(16.dp),
                                ),
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Add,
                            contentDescription = null,
                            tint = TealCyan,
                        )
                    }

                    DropdownMenu(
                        expanded = showAttachmentMenu,
                        onDismissRequest = { showAttachmentMenu = false },
                        shape = RoundedCornerShape(16.dp),
                        modifier =
                            Modifier
                                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.98f))
                                .border(1.dp, TealCyan.copy(alpha = 0.2f), RoundedCornerShape(16.dp)),
                    ) {
                        DropdownMenuItem(leadingIcon = {
                            Icon(
                                Icons.Rounded.Image,
                                contentDescription = null,
                                tint = TealCyan,
                            )
                        }, text = {
                            Text(
                                "IMAGE PAYLOAD",
                                fontWeight = FontWeight.Black,
                                fontSize = 11.sp,
                                letterSpacing = 1.sp,
                            )
                        }, onClick = { showAttachmentMenu = false })
                        DropdownMenuItem(leadingIcon = {
                            Icon(
                                Icons.Rounded.Videocam,
                                contentDescription = null,
                                tint = TealCyan,
                            )
                        }, text = {
                            Text(
                                "VIDEO FEED",
                                fontWeight = FontWeight.Black,
                                fontSize = 11.sp,
                                letterSpacing = 1.sp,
                            )
                        }, onClick = { showAttachmentMenu = false })
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                OutlinedTextField(
                    value = text,
                    onValueChange = { viewModel.onTextChange(it) },
                    modifier = Modifier.weight(1f),
                    placeholder = {
                        Text(
                            "Awaiting input...",
                            color = dynamicColor.copy(alpha = 0.3f),
                            fontSize = 14.sp,
                            letterSpacing = 0.5.sp,
                        )
                    },
                    shape = RoundedCornerShape(20.dp),
                    maxLines = 4,
                    colors =
                        OutlinedTextFieldDefaults.colors(
                            focusedTextColor = dynamicColor,
                            unfocusedTextColor = dynamicColor.copy(alpha = 0.9f),
                            focusedContainerColor = dynamicColor.copy(alpha = 0.03f),
                            unfocusedContainerColor = dynamicColor.copy(alpha = 0.01f),
                            focusedBorderColor = TealCyan.copy(alpha = 0.8f),
                            unfocusedBorderColor = dynamicColor.copy(alpha = 0.1f),
                            cursorColor = TealCyan,
                        ),
                )

                Spacer(modifier = Modifier.width(12.dp))

                FloatingActionButton(
                    onClick = { if (text.isNotBlank()) viewModel.sendMessage(groupId, userId) },
                    containerColor = TealCyan,
                    shape = RoundedCornerShape(16.dp),
                    elevation =
                        FloatingActionButtonDefaults.elevation(
                            defaultElevation = 0.dp,
                            pressedElevation = 0.dp,
                        ),
                    modifier =
                        Modifier
                            .size(50.dp)
                            .border(1.dp, Color.White.copy(alpha = 0.2f), RoundedCornerShape(16.dp)),
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.offset(x = 2.dp),
                    )
                }
            }
        }
    }
}

@Composable
fun MessageBubble(
    message: Message,
    isMine: Boolean,
    session: Session,
) {
    val dynamicColor = MaterialTheme.colorScheme.onSurface

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isMine) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Bottom,
    ) {
        if (!isMine) {
            AsyncImage(
                model = message.sender.profilePic,
                contentDescription = null,
                modifier =
                    Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .border(1.dp, dynamicColor.copy(alpha = 0.1f), RoundedCornerShape(10.dp)),
                contentScale = ContentScale.Crop,
            )
            Spacer(modifier = Modifier.width(12.dp))
        }

        Column(
            horizontalAlignment = if (isMine) Alignment.End else Alignment.Start,
            modifier = Modifier.weight(1f, fill = false),
        ) {
            if (!isMine) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(start = 2.dp, bottom = 6.dp),
                ) {
                    Box(
                        modifier =
                            Modifier
                                .size(4.dp)
                                .clip(CircleShape)
                                .background(TealCyan),
                    )
                    Spacer(modifier = Modifier.width(1.dp))
                    Text(
                        text = "> ${message.sender.fullname?.uppercase() ?: "UNKNOWN"}",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp,
                        color = TealCyan.copy(alpha = 0.8f),
                    )
                }
            }

            Surface(
                shape =
                    RoundedCornerShape(
                        topStart = 20.dp,
                        topEnd = 20.dp,
                        bottomStart = if (isMine) 20.dp else 2.dp,
                        bottomEnd = if (isMine) 2.dp else 20.dp,
                    ),
                color =
                    if (isMine) {
                        Color.Transparent
                    } else {
                        MaterialTheme.colorScheme.surface.copy(
                            alpha = 0.5f,
                        )
                    },
                border =
                    if (isMine) {
                        null
                    } else {
                        BorderStroke(
                            width = 1.dp,
                            brush =
                                Brush.verticalGradient(
                                    listOf(
                                        TealCyan.copy(alpha = 0.4f),
                                        Color.Transparent,
                                    ),
                                ),
                        )
                    },
                modifier =
                    if (isMine) {
                        Modifier.background(
                            brush =
                                Brush.linearGradient(
                                    colors = listOf(TealCyan, Color(0xFF00B4D8)),
                                ),
                            shape =
                                RoundedCornerShape(
                                    topStart = 20.dp,
                                    topEnd = 20.dp,
                                    bottomStart = 20.dp,
                                    bottomEnd = 2.dp,
                                ),
                        )
                    } else {
                        Modifier
                    },
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 18.dp, vertical = 14.dp),
                ) {
                    when (message.type) {
                        "text" -> {
                            Text(
                                text = message.text ?: "",
                                color = if (isMine) Color.White else dynamicColor.copy(alpha = 0.9f),
                                fontSize = 14.sp,
                                lineHeight = 22.sp,
                                fontWeight = FontWeight.Medium,
                            )
                        }

                        "image" -> {
                            Box(
                                modifier =
                                    Modifier.border(
                                        1.dp,
                                        Color.White.copy(alpha = 0.2f),
                                        RoundedCornerShape(12.dp),
                                    ),
                            ) {
                                AsyncImage(
                                    model = message.mediaUrl,
                                    contentDescription = null,
                                    modifier =
                                        Modifier
                                            .size(220.dp)
                                            .clip(RoundedCornerShape(12.dp)),
                                    contentScale = ContentScale.Crop,
                                )
                            }
                        }

                        "video" ->
                            Text(
                                ">> DECRYPTING VIDEO FEED",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp,
                                color = if (isMine) Color.White else TealCyan,
                            )

                        "file" ->
                            Text(
                                ">> ENCRYPTED PAYLOAD ATTACHED",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp,
                                color = if (isMine) Color.White else TealCyan,
                            )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = formatTimeAgo(message.createdAt).uppercase(),
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp,
                        color =
                            if (isMine) {
                                Color.White.copy(alpha = 0.7f)
                            } else {
                                dynamicColor.copy(
                                    alpha = 0.3f,
                                )
                            },
                        modifier = Modifier.align(if (isMine) Alignment.End else Alignment.Start),
                    )
                }
            }
        }

        if (isMine) {
            Spacer(modifier = Modifier.width(12.dp))
            AsyncImage(
                model = session.getProfileImage(),
                contentDescription = null,
                modifier =
                    Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .border(1.dp, TealCyan.copy(alpha = 0.6f), RoundedCornerShape(10.dp)),
                contentScale = ContentScale.Crop,
            )
        }
    }
}
