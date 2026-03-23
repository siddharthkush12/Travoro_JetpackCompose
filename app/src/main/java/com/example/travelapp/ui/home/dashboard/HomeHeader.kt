package com.example.travelapp.ui.home.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.travelapp.ui.theme.TealCyan
import com.example.travelapp.ui.theme.TealCyanDark
import com.example.travelapp.ui.theme.TealCyanLight
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalTextApi::class)
@Composable
fun HomeHeader(
    userName: String?,
    upcomingTripDate: String?
) {
    val daysLeft = upcomingTripDate?.let { calculateDaysLeft(it) }
    val timeGreeting = remember { getTimeOfDayGreeting() }


    val nameGradient = remember {
        Brush.linearGradient(
            colors = listOf(
                TealCyanDark,
                TealCyanLight
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 20.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = timeGreeting,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Medium
            )

            if (daysLeft != null && daysLeft >= 0) {
                Row(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(TealCyan.copy(alpha = 0.12f))
                        .border(1.dp, TealCyan.copy(alpha = 0.2f), CircleShape)
                        .padding(horizontal = 14.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.FlightTakeoff,
                        contentDescription = "Upcoming Trip",
                        tint = TealCyan,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "in $daysLeft days",
                        style = MaterialTheme.typography.labelMedium,
                        color = TealCyan,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = userName ?: "User",
            style = MaterialTheme.typography.displaySmall.copy(
                brush = nameGradient
            ),
            fontWeight = FontWeight.ExtraBold,
        )

        Spacer(modifier = Modifier.height((-7).dp))

        Text(
            text = "Ready to build a scalable itinerary today?",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
        )
    }
}


fun calculateDaysLeft(tripDateString: String): Long? {
    return try {
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val tripDate = format.parse(tripDateString) ?: return null
        val today = Date()
        val diff = tripDate.time - today.time
        TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)
    } catch (e: Exception) {
        null
    }
}

fun getTimeOfDayGreeting(): String {
    val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    return when (hour) {
        in 0..11 -> "Good morning,"
        in 12..16 -> "Good afternoon,"
        else -> "Good evening,"
    }
}