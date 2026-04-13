package com.travoro.app.ui.home.features.aiSearch

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.travoro.app.ui.components.CustomTopBar

@Composable
fun AiSearchScreen(homeNavController: NavController) {

    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
    ) {
        Column {
            CustomTopBar(
                title = "Ai Search",
                onBackClick = { homeNavController.popBackStack() },
            )

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "Coming Soon...",
                )
            }
        }
    }
}
