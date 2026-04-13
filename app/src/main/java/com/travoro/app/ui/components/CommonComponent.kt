package com.travoro.app.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ArrowBackIos
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.travoro.app.R
import com.travoro.app.ui.theme.ErrorRed
import com.travoro.app.ui.theme.TealCyan
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone.getTimeZone

@Composable
fun ErrorAlertDialog(
    message: String,
    onDismiss: () -> Unit,
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.questioning))
    val errorColor = MaterialTheme.colorScheme.error
    val dynamicColor = MaterialTheme.colorScheme.onSurface

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(16.dp),
        containerColor = MaterialTheme.colorScheme.surface,
        icon = {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .background(errorColor.copy(alpha = 0.05f), RoundedCornerShape(16.dp))
                    .border(1.dp, errorColor.copy(alpha = 0.2f), RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center,
            ) {
                LottieAnimation(
                    composition = composition,
                    iterations = LottieConstants.IterateForever,
                    modifier = Modifier.size(100.dp),
                )
            }
        },
        title = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "SYSTEM EXCEPTION",
                    style = TextStyle(
                        fontFamily = FontFamily.Monospace,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 2.sp,
                    ),
                    color = errorColor,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "ANOMALY DETECTED",
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp,
                    ),
                    color = dynamicColor,
                )
            }
        },
        text = {
            Text(
                text = message,
                style = TextStyle(
                    fontSize = 13.sp,
                    lineHeight = 20.sp,
                ),
                color = dynamicColor.copy(alpha = 0.7f),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = errorColor.copy(alpha = 0.1f),
                    contentColor = errorColor,
                ),
                border = BorderStroke(1.dp, errorColor.copy(alpha = 0.4f)),
                elevation = ButtonDefaults.buttonElevation(0.dp),
            ) {
                Text(
                    text = "ACKNOWLEDGE",
                    style = TextStyle(
                        fontFamily = FontFamily.Monospace,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp,
                    ),
                )
            }
        },
    )
}

@Composable
fun BackRoundButton(onClick: () -> Unit) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .padding(top = 50.dp, start = 20.dp)
            .size(46.dp)
            .zIndex(1f)
            .background(
                color = Color.White.copy(alpha = 0.12f),
                shape = CircleShape,
            ),
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back",
            tint = Color.White,
            modifier = Modifier.size(22.dp),
        )
    }
}

@Composable
fun CustomEditText(
    modifier: Modifier = Modifier,
    label: @Composable () -> Unit,
    value: String,
    onValueChange: (String) -> Unit,
    enabled: Boolean = true,
) {
    val dynamicColor = MaterialTheme.colorScheme.onSurface

    OutlinedTextField(
        value = value,
        onValueChange = { onValueChange(it) },
        modifier = modifier.fillMaxWidth(),
        singleLine = true,
        label = label,
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = dynamicColor,
            unfocusedTextColor = dynamicColor.copy(alpha = 0.9f),
            disabledTextColor = dynamicColor.copy(alpha = 0.4f),
            focusedContainerColor = dynamicColor.copy(alpha = 0.05f),
            unfocusedContainerColor = dynamicColor.copy(alpha = 0.02f),
            disabledContainerColor = dynamicColor.copy(alpha = 0.01f),
            focusedBorderColor = TealCyan,
            unfocusedBorderColor = dynamicColor.copy(alpha = 0.15f),
            disabledBorderColor = dynamicColor.copy(alpha = 0.05f),
            focusedLabelColor = TealCyan,
            unfocusedLabelColor = dynamicColor.copy(alpha = 0.6f),
            disabledLabelColor = dynamicColor.copy(alpha = 0.3f),
            cursorColor = TealCyan,
        ),
        enabled = enabled,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDropdownField(
    modifier: Modifier = Modifier,
    selectedValue: String,
    options: List<String>,
    label: String,
    onValueChange: (String) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    val dynamicColor = MaterialTheme.colorScheme.onSurface

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier,
    ) {
        OutlinedTextField(
            value = selectedValue,
            onValueChange = {},
            readOnly = true,
            singleLine = true,
            label = {
                Text(
                    text = label,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            },
            shape = RoundedCornerShape(16.dp),
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded,
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = dynamicColor,
                unfocusedTextColor = dynamicColor.copy(alpha = 0.9f),
                focusedContainerColor = dynamicColor.copy(alpha = 0.05f),
                unfocusedContainerColor = dynamicColor.copy(alpha = 0.02f),
                focusedBorderColor = TealCyan,
                unfocusedBorderColor = dynamicColor.copy(alpha = 0.15f),
                focusedLabelColor = TealCyan,
                unfocusedLabelColor = dynamicColor.copy(alpha = 0.6f),
            ),
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(type = ExposedDropdownMenuAnchorType.PrimaryEditable),
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.95f))
                .clip(RoundedCornerShape(8.dp)),
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = option,
                            color = dynamicColor.copy(alpha = 0.9f),
                            fontWeight = FontWeight.Medium,
                            maxLines = 1,
                            fontSize = 14.sp,
                            overflow = TextOverflow.Ellipsis,
                        )
                    },
                    onClick = {
                        onValueChange(option)
                        expanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDatePickerField(
    modifier: Modifier = Modifier,
    selectedDate: String,
    label: String = "DOB",
    onDateSelected: (String) -> Unit,
) {
    var showPicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val dynamicColor = MaterialTheme.colorScheme.onSurface

    if (showPicker) {
        DatePickerDialog(
            onDismissRequest = { showPicker = !showPicker },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                        formatter.timeZone = getTimeZone("UTC")
                        onDateSelected(formatter.format(Date(millis)))
                    }
                    showPicker = !showPicker
                }) {
                    Text(
                        "CONFIRM",
                        fontWeight = FontWeight.Black,
                        color = TealCyan,
                        letterSpacing = 1.sp,
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showPicker = !showPicker }) {
                    Text(
                        "CANCEL",
                        color = dynamicColor.copy(alpha = 0.5f),
                        fontWeight = FontWeight.Bold,
                    )
                }
            },
            shape = RoundedCornerShape(24.dp),
            colors = DatePickerDefaults.colors(
                containerColor = MaterialTheme.colorScheme.surface,
            ),
        ) {
            DatePicker(
                state = datePickerState,
                colors = DatePickerDefaults.colors(
                    todayContentColor = TealCyan,
                    todayDateBorderColor = TealCyan,
                    selectedDayContainerColor = TealCyan,
                    selectedDayContentColor = Color.White,
                ),
            )
        }
    }

    Box(modifier = modifier) {
        OutlinedTextField(
            value = selectedDate,
            onValueChange = {},
            readOnly = true,
            singleLine = true,
            label = {
                Text(text = label, maxLines = 1, overflow = TextOverflow.Ellipsis)
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            trailingIcon = {
                Icon(
                    imageVector = Icons.Rounded.CalendarMonth,
                    contentDescription = "Select Date",
                    tint = dynamicColor.copy(alpha = 0.5f),
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = dynamicColor,
                unfocusedTextColor = dynamicColor.copy(alpha = 0.9f),
                focusedContainerColor = dynamicColor.copy(alpha = 0.05f),
                unfocusedContainerColor = dynamicColor.copy(alpha = 0.02f),
                focusedBorderColor = TealCyan,
                unfocusedBorderColor = dynamicColor.copy(alpha = 0.15f),
                focusedLabelColor = TealCyan,
                unfocusedLabelColor = dynamicColor.copy(alpha = 0.6f),
            ),
        )

        Box(
            modifier = Modifier
                .matchParentSize()
                .background(Color.Transparent)
                .clickable { showPicker = !showPicker },
        )
    }
}

@Composable
fun CustomTopBar(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String? = null,
    subTitleSize: TextUnit = 10.sp,
    icon: ImageVector? = null,
    onBackClick: () -> Unit,
) {
    val borderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .drawBehind {
                val strokeWidth = 0.5.dp.toPx()
                val y = size.height - strokeWidth / 2
                drawLine(
                    color = borderColor,
                    start = Offset(0f, y),
                    end = Offset(size.width, y),
                    strokeWidth = strokeWidth,
                )
            },
        color = MaterialTheme.colorScheme.background.copy(alpha = 0.8f),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(TealCyan.copy(alpha = 0.1f))
                    .clickable { onBackClick() },
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBackIos,
                    contentDescription = "Back",
                    tint = TealCyan,
                    modifier = Modifier
                        .size(18.dp)
                        .padding(end = 2.dp),
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp),
            ) {
                Text(
                    text = title.uppercase(),
                    style = TextStyle(
                        fontSize = 13.sp,
                        letterSpacing = 3.sp,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.onSurface,
                        shadow = Shadow(
                            color = TealCyan.copy(alpha = 0.2f),
                            offset = Offset(0f, 2f),
                            blurRadius = 8f,
                        ),
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                if (subtitle != null) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = subtitle.uppercase(),
                        style = TextStyle(
                            fontSize = subTitleSize,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }

                Box(
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .width(18.dp)
                        .height(2.dp)
                        .clip(CircleShape)
                        .background(TealCyan),
                )
            }

            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = TealCyan.copy(alpha = 0.4f),
                    modifier = Modifier.size(24.dp),
                )
            } else {
                Spacer(modifier = Modifier.size(44.dp))
            }
        }
    }
}

@Composable
fun Avatar(
    imageUrl: String?,
    modifier: Modifier = Modifier,
    name: String? = null,
    size: Dp = 44.dp,
    borderWidth: Dp = 1.dp,
    borderColor: Color = TealCyan.copy(alpha = 0.5f),
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(TealCyan.copy(alpha = 0.1f))
            .border(borderWidth, borderColor, CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        if (!imageUrl.isNullOrBlank()) {
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,
            )
        } else if (!name.isNullOrBlank()) {
            Text(
                text = name.take(1).uppercase(),
                style = TextStyle(
                    fontWeight = FontWeight.Black,
                    color = TealCyan,
                    fontSize = (size.value * 0.4).sp,
                ),
            )
        } else {
            Icon(
                imageVector = Icons.Rounded.Person,
                contentDescription = null,
                tint = TealCyan.copy(alpha = 0.5f),
                modifier = Modifier.size(size * 0.6f),
            )
        }
    }
}

@Composable
fun CustomErrorMessage(message: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "SYSTEM ERROR: $message",
            color = ErrorRed,
            fontWeight = FontWeight.Black,
            letterSpacing = 1.sp,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
fun HomeBarHeaders(
    title: String,
    subtitle: String,
    topPadding: Dp,
    icon: ImageVector,
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.98f),
        shadowElevation = 8.dp,
    ) {
        Row(
            modifier = Modifier
                .padding(top = topPadding)
                .padding(horizontal = 24.dp, vertical = 20.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Black,
                        lineHeight = 32.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                    ),
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp,
                        color = TealCyan.copy(alpha = 0.7f),
                    ),
                )
            }

            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                TealCyan.copy(alpha = 0.2f),
                                TealCyan.copy(alpha = 0.05f),
                            ),
                        ),
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = "AI",
                    tint = TealCyan,
                    modifier = Modifier.size(32.dp),
                )
            }
        }
    }
}


fun formatTimeAgo(time: String?): String {
    if (time == null) return ""

    return try {
        val inputFormat = SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
            Locale.getDefault(),
        )
        inputFormat.timeZone = getTimeZone("UTC")

        val date = inputFormat.parse(time)
        val now = Date()

        val diff = now.time - date!!.time
        val minutes = diff / (1000 * 60)
        val hours = minutes / 60

        when {
            minutes < 1 -> "Just now"
            minutes < 60 -> "$minutes min ago"
            hours < 24 -> "$hours hr ago"
            else -> "${hours / 24} days ago"
        }
    } catch (e: Exception) {
        ""
    }
}

fun formatDate(iso: String?): String {
    if (iso == null) return ""

    val parser = SimpleDateFormat(
        "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
        Locale.getDefault(),
    )

    parser.timeZone = getTimeZone("UTC")

    val date = parser.parse(iso)
    val formatter = SimpleDateFormat(
        "dd MMM yyyy",
        Locale.getDefault(),
    )

    return formatter.format(date!!)
}

fun formatDayOfWeek(dateString: String): String {
    return try {
        val inputFormat = SimpleDateFormat(
            "yyyy-MM-dd",
            Locale.getDefault(),
        )
        val outputFormat = SimpleDateFormat(
            "EEE",
            Locale.getDefault(),
        )
        val date = inputFormat.parse(dateString)
        outputFormat.format(date!!).uppercase(Locale.getDefault())
    } catch (e: Exception) {
        dateString
    }
}

@SuppressLint("NewApi")
fun calculateEndDate(
    start: String,
    days: Int,
): String {
    val startDate = java.time.LocalDate.parse(start)

    val endDate = startDate.plusDays(
        (days - 1).toLong(),
    )
    return endDate.toString()
}

fun getTripDays(
    start: String?,
    end: String?,
): Int {
    if (start == null || end == null) return 1

    val sdf = SimpleDateFormat(
        "yyyy-MM-dd",
        Locale.getDefault(),
    )
    val cleanStart = start.substring(0, 10)
    val cleanEnd = end.substring(0, 10)

    val startDate = sdf.parse(cleanStart)
    val endDate = sdf.parse(cleanEnd)

    val diff = endDate.time - startDate.time

    return (diff / (1000 * 60 * 60 * 24)).toInt() + 1
}
